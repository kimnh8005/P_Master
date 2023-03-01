package kr.co.pulmuone.v1.goods.stock.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.SaleStatus;
import kr.co.pulmuone.v1.comm.enums.StockEnums.StockExprType;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.goods.stock.GoodsStockDisposalMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.stock.dto.GoodsStockDisposalRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.GoodsStockDisposalResultVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GoodsStockDisposalService {

    private final GoodsStockDisposalMapper goodsStockDisposalMapper;

    Page<GoodsStockDisposalResultVo> getGoodsStockDisposalList(GoodsStockDisposalRequestDto goodsStockDisposalRequestDto) {

        if (!StringUtil.isEmpty(goodsStockDisposalRequestDto.getStockExprTp())) {
            List<String> stockExprTpList = this.getSearchKeyToSearchKeyList(goodsStockDisposalRequestDto.getStockExprTp(), Constants.ARRAY_SEPARATORS);
            goodsStockDisposalRequestDto.setStockExprTpList(stockExprTpList);
        }

        Page<GoodsStockDisposalResultVo> resultVoList = goodsStockDisposalMapper.getGoodsStockDisposalList(goodsStockDisposalRequestDto);

        resultVoList
            .forEach(e -> {
                GoodsType goodsType = GoodsType.findByCode(e.getGoodsTp());
                SaleStatus saleStatus = SaleStatus.findByCode(e.getSaleStatus());
                StockExprType stockExprType = StockExprType.findByCode(e.getStockTp());
                SaleStatus disposalSaleStatus = SaleStatus.findByCode(e.getDisposalSaleStatus());

                if (!ObjectUtils.isEmpty(goodsType)) e.setGoodsTp(goodsType.getCodeName());
                if (!ObjectUtils.isEmpty(saleStatus)) e.setSaleStatus(saleStatus.getCodeName());
                if (!ObjectUtils.isEmpty(stockExprType)) e.setStockTp(stockExprType.getCodeName());
                if (!ObjectUtils.isEmpty(disposalSaleStatus)) e.setDisposalSaleStatus(saleStatus.getCodeName());
            });

        return resultVoList;
    }

    ExcelDownloadDto getGoodsStockDisposalExcelList(GoodsStockDisposalRequestDto goodsStockDisposalRequestDto) {
        List<GoodsStockDisposalResultVo> resultVoList = this.getGoodsStockDisposalList(goodsStockDisposalRequestDto);

        String excelFileName = "item_GoodsStockDisposalList"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                200, 150, 150, 150, 180, 250, 250, 500, 230, 250, 150, 250, 150};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "left", "center", "center", "center","center", "center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "ilItemCd", "itemBarcode", "goodsTp", "ilGoodsId", "saleStatus", "disposalGoodsId", "disposalSaleStatus", "itemNm", "stockTp", "expirationDt", "leftDays", "disposalLeftDays", "stockQty"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "품목코드", "품목바코드", "상품유형", "상품코드", "판매상태", "폐기임박상품 코드", "폐기임박상품 판매상태", "마스터품목명", "임박/출고기준", "유통기한", "유통기한 잔여일", "폐기예정 전환 잔여일", "수량"};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        firstWorkSheetDto.setExcelDataList(resultVoList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    /**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        if( StringUtils.isNotEmpty(searchKey) ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .filter( x -> x.indexOf("ALL") < 0 )
                    .collect(Collectors.toList()));
        }

        return searchKeyList;
    }

}
