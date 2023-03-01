package kr.co.pulmuone.v1.batch.goods.goods;

import kr.co.pulmuone.v1.api.api2pdf.client.Api2PdfClient;
import kr.co.pulmuone.v1.api.api2pdf.models.Api2PdfResponse;
import kr.co.pulmuone.v1.batch.goods.goods.dto.GoodsDetailImageBatchRequestDto;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsDetailImageBatchBizImpl implements GoodsDetailImageBatchBiz {
    @Autowired
    private final GoodsDetailImageBatchService goodsDetailImageBatchService;

    @Value("${app.storage.public.public-root-location}")
    private String publicRootLocation; // public 파일의 최상위 저장경로

    @Value("${api2pdf.authKey}")
    private String api2pdfAuthKey; // api2pdf authKey

    @Value("${resource.server.url.mall}")
    private String mallDomain;   // mall Domain

    @Value("${module-name}")
    private String moduleName; // 현재 가동중인 모듈명


    private String getFilePath(String path) {
        return Paths.get(File.separator + "BOS" + File.separator + "il" + File.separator + "goods" + File.separator + "imageName" + File.separator + path).toString();
    }

    /**
     *
     * @param
     * @return void
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public void runCreateGoodsDetailImage() throws IOException {
//        Api2PdfClient a2pClient = new Api2PdfClient("d4dd1f92-a38c-4ebb-9013-0efeb4f3cea9");
        Api2PdfClient a2pClient = new Api2PdfClient(api2pdfAuthKey);
//        String targetUrl = "https://shop.pulmuone.co.kr/shop/goodsView?goods=30842";
//        String targetUrl = "https://qashop.pulmuone.online/shop/goodsView?goods=";
        String targetUrl = mallDomain + "/shop/externalMallPreview?goods=";

        Map<String, String> options = new HashMap<String, String>();
        options.put("puppeteerWaitForMethod", "WaitForNavigation");
        options.put("puppeteerWaitForValue", "networkidle0");

        System.out.println("publicRootLocation == : " + publicRootLocation );

        System.out.println("mallDomain == : " + mallDomain );


        // 배치 생성되지 않은 상품 상세 이미지 조회
        List<Long> goodsDetailImageVoList = goodsDetailImageBatchService.getNoBatchGoodsDetailImage();

        for(Long ilGoodsId : goodsDetailImageVoList){
            String imageUrl;
            GoodsDetailImageBatchRequestDto goodsDetailImageBatchRequestDto = new GoodsDetailImageBatchRequestDto();
            imageUrl = targetUrl + ilGoodsId;

            // 파일명 : PC_상품번호_날짜(시간포함)
            String imageName = "PC_" + ilGoodsId + "_" + DateUtil.getCurrentDate("yyyyMMddHHmmss") + ".png";
            String fileRootName = "";
            String inputPcImgName = "";      // PC 이미지 파일명

            Api2PdfResponse response = new Api2PdfResponse();

            for(int runCnt = 0; runCnt<3; runCnt++) {   // 이미지 변환처리 3회 진행 (오류 발생 시 10초 후 진행)
                try {
                    response = a2pClient.chromeUrlToImage(imageUrl , true , imageName, options);
                    System.out.println("response File === :" + response.getFile());
                    break;
                }catch(Exception e) {
                    System.out.println("chromeUrlToImage == Fail GoodsId Try Cnt=== : " + runCnt + "=== GoodsId === :  " + ilGoodsId);
                }
                try{
                    Thread.sleep(10000);
                } catch (Exception ex){
                    log.error(ex.getMessage());
                }
            }

            if (response.getSuccess() && response.getError() == null && response.getFile() != null && response.getFile() != "") {
                inputPcImgName = getFilePath(DateUtil.getCurrentDate("yyyy") + File.separator + DateUtil.getCurrentDate("MM") + File.separator + DateUtil.getCurrentDate("dd"));
                fileRootName = publicRootLocation + inputPcImgName;
                inputPcImgName += File.separator + imageName;
                imageName = fileRootName + File.separator + imageName;

                String fileUrl = response.getFile();

                System.out.println("fileRootName === : " + fileRootName);
                Path pathFile = Paths.get(fileRootName);

                File dir = new File(pathFile.toString());
                if (!dir.exists()) {
                    dir.mkdirs();
                    System.out.println("mkdirs === success " );
                }

                try (BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(imageName)) {
                    byte dataBuffer[] = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }
                    System.out.println("fileWrite === success " );
                } catch (IOException e) {
                    // handle exception
                    System.out.println("fileWrite === fail " );
                    continue;
                }
            }

            System.out.println("inputPcImgName === : " + inputPcImgName);

            goodsDetailImageBatchRequestDto.setIlGoodsId(ilGoodsId);
            goodsDetailImageBatchRequestDto.setPcImgNm(inputPcImgName);

            // 상세 이미지 테이블 DB UPDATE
            goodsDetailImageBatchService.updateGoodsDetailImageGenInfo(goodsDetailImageBatchRequestDto);

            System.out.println("Success ilGoodsId === : " + ilGoodsId);

        }

    }

}
