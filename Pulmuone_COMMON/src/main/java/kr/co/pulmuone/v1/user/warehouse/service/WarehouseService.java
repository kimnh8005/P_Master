package kr.co.pulmuone.v1.user.warehouse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.user.warehouse.WarehouseMapper;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseModifyDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseTemplateRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service
public class WarehouseService {

	private final WarehouseMapper warehouseMapper;


    /**
     * 출고처 목록 조회
     * @param warehouseRequestDto
     * @return
     * @throws Exception
     */
    protected Page<WarehouseResultVo> getWarehouseList(WarehouseRequestDto warehouseRequestDto) throws Exception {
    	PageMethod.startPage(warehouseRequestDto.getPage(), warehouseRequestDto.getPageSize());
        return warehouseMapper.getWarehouseList(warehouseRequestDto);
    }

    /**
     * 출고처 정보 상세 조회
     * @param warehouseRequestDto
     * @return
     * @throws Exception
     */
    protected WarehouseResultVo getWarehouse(WarehouseRequestDto warehouseRequestDto) throws Exception {
    	WarehouseResultVo resultVo = new WarehouseResultVo();
    	resultVo = warehouseMapper.getWarehouse(warehouseRequestDto);
    	return resultVo;
    }

    /**
     * 공급처 정보 조회
     * @param warehouseRequestDto
     * @return
     * @throws Exception
     */
    protected List<WarehouseResultVo> getSupplierList(WarehouseRequestDto warehouseRequestDto) throws Exception {

    	List<WarehouseResultVo> supplierList = warehouseMapper.getSupplierList(warehouseRequestDto);

    	return supplierList;
    }

    /**
     * 출고처 정보 등록
     * @param warehouseModifyDto
     * @return
     * @throws Exception
     */
    protected WarehouseResponseDto addWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception{
    	WarehouseResponseDto result = new WarehouseResponseDto();
    	warehouseMapper.addWarehouse(warehouseModifyDto);
    	return result;
    }

    /**
     * 출고처 정보 수정
     * @param warehouseModifyDto
     * @return
     * @throws Exception
     */
    protected WarehouseResponseDto putWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception{
    	WarehouseResponseDto result = new WarehouseResponseDto();
    	warehouseMapper.putWarehouse(warehouseModifyDto);
    	return result;
    }


    /**
     * 공급처 정보 삭제
     * @param warehouseModifyDto
     * @return
     * @throws Exception
     */
    protected WarehouseResponseDto delSupplierWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception{
    	WarehouseResponseDto result = new WarehouseResponseDto();
    	warehouseMapper.delSupplierWarehouse(warehouseModifyDto);
    	return result;
    }


    /**
     * 공급처 정보 등록
     * @param warehouseModifyDto
     * @return
     * @throws Exception
     */
    protected WarehouseResponseDto addSupplierWarehouse(WarehouseModifyDto warehouseModifyDto) throws Exception{
    	WarehouseResponseDto result = new WarehouseResponseDto();
    	warehouseMapper.addSupplierWarehouse(warehouseModifyDto);
    	return result;
    }

    /**
     * 배송정책  정보 조회
     * @param warehouseTemplateRequestDto
     * @return
     * @throws Exception
     */
    protected WarehouseResultVo getWarehouseTemplateInfo(WarehouseTemplateRequestDto warehouseTemplateRequestDto) throws Exception {
    	WarehouseResultVo vo = warehouseMapper.getWarehouseTemplateInfo(warehouseTemplateRequestDto);
    	return vo;
    }


    /**
     * 배송비 템플릿 정보
     * @param warehouseTemplateRequestDto
     * @return
     * @throws Exception
     */
    protected List<ShippingTemplateVo> getShippingTemplateList(WarehouseTemplateRequestDto warehouseTemplateRequestDto) throws Exception {
    	List<ShippingTemplateVo> voList = warehouseMapper.getShippingTemplateList(warehouseTemplateRequestDto);
    	return voList;
    }


    /**
     * 업체명 중복 확인
     * @param warehouseModifyDto
     * @return
     * @throws Exception
     */
    protected int getDuplicateCompanyName(WarehouseModifyDto warehouseModifyDto) throws Exception{
    	return warehouseMapper.getDuplicateCompanyName(warehouseModifyDto);
    }

    /**
     * 출고처 - 업체명 조회(약관용)
     * @return List<String>
     * @throws Exception Exception
     */
    protected List<String> getWarehouseCompanyName() throws Exception{
        return warehouseMapper.getWarehouseCompanyName();
    }

    /**
     * @Desc 출고처 리스트 엑셀 다운로드 목록 조회
     * @param warehouseRequestDto :출고처 리스트 검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     */
    protected ExcelDownloadDto getWarehouseExcelDownload(WarehouseRequestDto warehouseRequestDto){
        String excelFileName = "출고처 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                  100, 100, 100, 100, 100
                , 100, 100, 100, 100, 100
                , 100, 100, 100, 100, 100
                , 100, 200, 200, 100, 200
                , 100, 100, 100, 100, 100 };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                  "center", "center", "center", "center", "center"
                , "center", "center", "center", "center", "center"
                , "center", "center", "center", "center", "center"
                , "center", "center", "center", "center", "center"
                , "center", "center", "center", "center", "center" };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "warehouseName", "warehouseGroupName", "companyName", "warehouseTelephone1", "warehouseTelephone2"
                , "warehouseTelephone3", "warehouseMemo", "stockOrderYn", "stlmnYn", "orderChangeType"
                , "limitCount", "deliveryPatternName", "hour", "minute", "undeliverableAreaTpGrp"
                , "dawnDlvryYn", "dawnLimitCnt", "dawnDeliveryPatternName", "dawnUndeliverableAreaTpGrp", "dawnHour"
                , "dawnMinute", "receiverZipCode", "receiverAddress1", "receiverAddress2", "createInfo"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "출고처명", "출고처그룹", "공급업체", "연락처1", "연락처2"
                , "연락처3", "출고처메모", "재고발주여부", "물류비정산여부", "주문변경여부"
                , "일별출고한도", "배송패턴", "주문마감시간(HH)", "주문마감시간(MM)", "배송불가지역"
                , "새벽배송가능여부", "새벽일별출고한도", "새벽배송패턴", "새벽배송불가지역", "새벽배송주문마감시간(HH)"
                , "새벽배송주문마감시간(MM)", "우편번호", "주소1", "주소2", "등록정보"};

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<WarehouseResultVo> itemList = warehouseMapper.getWarehouseExcelDownload(warehouseRequestDto);
        List<WarehouseResultVo> warehouseList = new ArrayList<>();

        for(WarehouseResultVo resultVo : itemList) {
            String undeliverableAreaTpGrpNames = null;
            String dawnUndeliverableAreaTpGrpNames = null;

            if (!resultVo.getUndeliverableAreaTpGrp().isEmpty()){
                resultVo.setUndeliverableAreaTpGrpList(getSearchKeyToSearchKeyList(resultVo.getUndeliverableAreaTpGrp(), Constants.ARRAY_SEPARATORS)); // 구분
                undeliverableAreaTpGrpNames = warehouseMapper.getDicNames(resultVo.getUndeliverableAreaTpGrpList());
                resultVo.setUndeliverableAreaTpGrp(undeliverableAreaTpGrpNames);
            }

            if (!resultVo.getDawnUndeliverableAreaTpGrp().isEmpty()){
                resultVo.setDawnUndeliverableAreaTpGrpList(getSearchKeyToSearchKeyList(resultVo.getDawnUndeliverableAreaTpGrp(), Constants.ARRAY_SEPARATORS)); // 구분
                dawnUndeliverableAreaTpGrpNames = warehouseMapper.getDicNames(resultVo.getDawnUndeliverableAreaTpGrpList());
                resultVo.setDawnUndeliverableAreaTpGrp(dawnUndeliverableAreaTpGrpNames);
            }
            warehouseList.add(resultVo);
        }


        firstWorkSheetDto.setExcelDataList(warehouseList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }

    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        if( StringUtils.isNotEmpty(searchKey) && searchKey.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));
        }

        return searchKeyList;
    }
}
