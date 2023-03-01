package kr.co.pulmuone.v1.batch.order.inside.service;

import kr.co.pulmuone.v1.batch.order.inside.dto.DawnDeliveryInfoDto;
import kr.co.pulmuone.v1.batch.order.inside.dto.vo.DawnDeliveryAreaVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.DawnDeliveryEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.FTPUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * CJ 새벽권역 sFTP 배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class DawnDeliveryBizImpl implements DawnDeliveryBiz {

	@Autowired
	private final DawnDeliveryService dawnDeliveryService;

	@Value("${cj-fresh.sftp.host}")
	String cjHost;

	@Value("${cj-fresh.sftp.user}")
	String cjUser;

	@Value("${cj-fresh.sftp.password}")
	String cjPassword;

	@Value("${cj-fresh.sftp.path}")
	String cjPath;

	@Value("${cj-fresh.sftp.filename}")
	String cjFileNm;

	@Value("${cj-fresh.sftp.download_path}")
	String localDir;

    /**
     * CJ새벽배송 권역 배치
     * @param erpApiEnums
     * @return void
     * @throws BaseException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
    public ApiResult<?> batchDwanDeliveryArea() throws Exception {
    	log.info("sftp 접속 정보 <{}>, <{}>, <{}>, <{}>, <{}>, <{}>", cjHost, cjUser, cjPassword, cjPath, cjFileNm, localDir);
    	FTPUtil ftpUtil = new FTPUtil(cjHost, cjUser, cjPassword);

		ftpUtil.download(cjPath + "/" + cjFileNm, localDir);
		log.info("파일 다운로드 성공!!!!");

		log.info("새벽권역 시작 !!!!");
    	int insertCnt = getFileRead(localDir + "/" + cjFileNm);

		log.info("파일 데이터 등록 갯수 :: <" + insertCnt + ">");

    	//txt파일을 읽어 들여서 업로드 한다.
    	if (insertCnt > 0) {

			//System.out.println("임시데이터 저장 처리");
    		//데이터를 임시로 저장한다.
    		//addDawnDeliveryTemp(fileReadDto);

			DawnDeliveryInfoDto fileReadDto = new DawnDeliveryInfoDto();
			log.info("UR_DAWN_DELIVERY_AREA_TEMP -> UR_DAWN_DELIVERY_AREA 등록 처리");
    		// 1. TEMP 테이블 데이터를 추가한다
			fileReadDto.setAddDeleteTp(DawnDeliveryEnums.AddDeleteTp.ADD.getCode());
			fileReadDto.setAddDeleteTpNm(DawnDeliveryEnums.AddDeleteTp.ADD.getCodeName());
			addDawnDelivery(fileReadDto);
	    	addDawnDeliveryHist(fileReadDto);

			log.info("UR_DAWN_DELIVERY_AREA 기존 데이터 삭제 처리");
			// 2. 기존 데이터 삭제한다
			fileReadDto.setAddDeleteTp(DawnDeliveryEnums.AddDeleteTp.DELETE.getCode());
			fileReadDto.setAddDeleteTpNm(DawnDeliveryEnums.AddDeleteTp.DELETE.getCodeName());
			// 이력 먼저 쌓고 삭제처리 한다
			addDawnDeliveryHist(fileReadDto);
			deleteDawnDelivery(fileReadDto);

			log.info("UR_DAWN_DELIVERY_AREA_TEMP 데이터 삭제 처리");
			// 3. TEMP 테이블 비운다
			deleteDawnDeliveryTemp();
    	}

		//추가할 데이터를 조회 한다.
//    	List<DawnDeliveryAreaVo> addList = getDawnDelivderyAddInfoList();
//		System.out.println("추가 데이터 존재유무 :: <" + CollectionUtils.isNotEmpty(addList) + ">");
//
//    	if (CollectionUtils.isNotEmpty(addList)) {
//	    	DawnDeliveryInfoDto insertDto = new DawnDeliveryInfoDto();
//			System.out.println("insert size() ::: <" + addList.size() + ">");
//	    	insertDto.setInsertList(addList);
//	    	insertDto.setHistList(addList);
//
//	    	addDawnDelivery(insertDto);
//	    	addDawnDeliveryHist(insertDto);
//    	}

    	//기존데이터를 삭제한다.
//    	List<DawnDeliveryAreaVo> removeList = getDawnDelivderyRemoveInfoList();
//		System.out.println("삭제데이터 존재유무 <{}>", CollectionUtils.isNotEmpty(removeList));
//
//    	if (CollectionUtils.isNotEmpty(removeList)) {
//    		DawnDeliveryInfoDto deleteDto = new DawnDeliveryInfoDto();
//			System.out.println("deleteDto size() ::: <" + removeList.size() + ">");
//	    	deleteDto.setDeleteList(removeList);
//	    	deleteDto.setHistList(removeList);
//
//    		for (DawnDeliveryAreaVo removeDto : removeList) {
//    			deleteDawnDelivery(removeDto);
//    		}
//	    	addDawnDeliveryHist(deleteDto);
//    	}

//    	deleteDawnDeliveryTemp();
//		System.out.println("새벽배송 임시 테이블 삭제 완료 !!!");

		// 4. 파일 삭제
		File newFile = new File(localDir + "/" + cjFileNm);
		log.info("새벽배송 다운로드 파일 삭제할 파일경로 및 이름  :: <" + newFile + ">");
		if (newFile.isFile()) newFile.delete();
		log.info("새벽배송 다운로드 파일 존재유무  :: <" + newFile.isFile() + "> ");
		log.info("새벽배송 업데이트 완료 !!!");

    	return ApiResult.success();
    }

    /**
     * CJ 새벽권역 파일을 읽어 들인다.
     * @param fileName (읽을 파일 이름)
     * @return
     */
    public int getFileRead(String fileName) throws Exception {

    	int insertCnt = 0;

    	List<DawnDeliveryAreaVo> resultList = new ArrayList<>();
    	DawnDeliveryInfoDto dto = new DawnDeliveryInfoDto();
		log.info("임시데이터 읽기!!!");
    	try(BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            int i = 1;
            while ((line = br.readLine()) != null) {
            	String[] lineArray = line.split("	");
            	if (lineArray.length == 2 && !line.contains("ZIPNUM")) {
            		DawnDeliveryAreaVo vo = DawnDeliveryAreaVo.builder()
            				.zipCd(lineArray[0])
            				.buildingNo(lineArray[1])
            				.build();
	            	resultList.add(vo);

					if(i % 50000 == 0) {
						dto.setTempList(resultList);
						insertCnt += dawnDeliveryService.addDawnDeliveryTemp(dto);
						resultList = new ArrayList<>();
						log.info("[" + insertCnt + "]");
					}
					else if(i % 1000 == 0){
						log.info("[" + i + "]");
					}
	            	i++;
            	}
            }
			if(!resultList.isEmpty()) {
				dto.setTempList(resultList);
				insertCnt += dawnDeliveryService.addDawnDeliveryTemp(dto);
				log.info("[" + insertCnt + "]");
			}
			log.info("새벽 배송 권역 TEMP 등록 완료");
        }
        catch (IOException e) {
			log.info("An error occurred.");
			throw new Exception(e.getMessage());
        }

    	return insertCnt;
    }

	/**
	 * 임시 데이터가 존재하는 조회
	 * @return
	 */
	public int getDawnDelivderyTempCheck() {
		return dawnDeliveryService.getDawnDelivderyTempCheck();
	}

    /**
	 * 새벽배송권역 삭제할 데이터 조회
	 * @return
	 */
	public List<DawnDeliveryAreaVo> getDawnDelivderyRemoveInfoList() {
		return dawnDeliveryService.getDawnDelivderyRemoveInfoList();
	}

	/**
	 * 새벽배송권역 추가할 데이터 조회
	 * @return
	 */
	public List<DawnDeliveryAreaVo> getDawnDelivderyAddInfoList() {
		return dawnDeliveryService.getDawnDelivderyAddInfoList();
	}

	/**
	 * 임시 새벽배송권역을 추가한다.
	 * @param dawnDeliveryInfoDto
	 * @return
	 */
	public int addDawnDeliveryTemp(DawnDeliveryInfoDto dawnDeliveryInfoDto) {
		return dawnDeliveryService.addDawnDeliveryTemp(dawnDeliveryInfoDto);
	}

	/**
	 * 새벽배송권역을 추가한다.
	 * @param dawnDeliveryInfoDto
	 * @return
	 */
	public int addDawnDelivery(DawnDeliveryInfoDto dawnDeliveryInfoDto) {
		return dawnDeliveryService.addDawnDelivery(dawnDeliveryInfoDto);
	}

	/**
	 * 새벽배송권역을 삭제한다.
	 * @param dawnDeliveryInfoDto
	 * @return
	 */
	public int deleteDawnDelivery(DawnDeliveryInfoDto dawnDeliveryInfoDto) {
		return dawnDeliveryService.deleteDawnDelivery(dawnDeliveryInfoDto);
	}

	/**
	 * 새벽배송권역 이력을 저장한다.
	 * @param dawnDeliveryAreaHistVo
	 * @return
	 */
	public int addDawnDeliveryHist(DawnDeliveryInfoDto dawnDeliveryInfoDto) {
		return dawnDeliveryService.addDawnDeliveryHist(dawnDeliveryInfoDto);
	}

	/**
	 * 새벽배송권역임시 데이터를 삭제한다.
	 * @param dawnDeliveryAreaHistVo
	 * @return
	 */
	public int deleteDawnDeliveryTemp() {
		return dawnDeliveryService.deleteDawnDeliveryTemp();
	}

}