package kr.co.pulmuone.v1.outmall.sellers.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SellersEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.outmall.sellers.dto.*;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersVo;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeVo;
import kr.co.pulmuone.v1.policy.fee.service.OmBasicFeeBiz;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SellersBizImpl implements SellersBiz {
    @Autowired
    private SellersService sellersService;

    @Autowired
    private OmBasicFeeBiz omBasicFeeBiz;

    @Override
    public ApiResult<?> getSellersList(SellersListRequestDto sellersListRequestDto) throws Exception{
        return ApiResult.success(sellersService.getSellersList(sellersListRequestDto));
    }

    @Override
    public ExcelDownloadDto getSellersExcelList(SellersListRequestDto sellersListRequestDto) {

        String excelFileName = "sellersList"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 150, 150, 150, 150, 150, 150, 150, 300, 300, 150, 150, 150};

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center", "center", "center", "center", "right","left","left","center","center","center"};

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "omSellersId", "sellersNm", "logisticsGubun", "sellersGroupNm", "supplierNm", "supplierCd", "calcType", "fee", "sellersUrl", "sellersAdminUrl", "interfaceYn", "useYn", "createDt"};

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "판매처코드", "판매처명", "물류구분", "판매처그룹", "공급업체명", "공급처코드", "정산방식", "위탁매출수수료", "판매처 URL", "판매처 관리자 URL", "수집몰 연동여부", "사용여부", "등록일자"};

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
        List<SellersExcelDto> orgaDisList = sellersService.getSellersExcelList(sellersListRequestDto);

        firstWorkSheetDto.setExcelDataList(orgaDisList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }

    @Override
    public ApiResult<?> getSellersGroupList(SellersListRequestDto sellersListRequestDto) throws Exception{
        return ApiResult.success(sellersService.getSellersGroupList(sellersListRequestDto));
    }

    @Override
    @UserMaskingRun(system = "BOS")
    public ApiResult<?> getSellers(SellersRequestDto sellersRequestDto) throws Exception{
        SellersResponseDto result = new SellersResponseDto();

        // 외부몰 조회
        SellersVo sellersVo = sellersService.getSellers(sellersRequestDto);

        // 외부몰 공급처 목록 조회
        List<OmBasicFeeListDto> applyOmBasicFeeList = sellersService.getApplyOmBasicFeeList(sellersRequestDto.getOmSellersId());
        sellersVo.setSellersSupplierList(applyOmBasicFeeList);
        result.setRows(sellersVo);

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> addSellers(SellersRequestDto sellersRequestDto) throws Exception{

    	//외부몰명 중복확인
    	if(duplicateSellersInfo(sellersRequestDto)) {
    		return ApiResult.result(SellersEnums.SellersMessage.DUPLICATE_SELLER_NAME);
    	}

        // #001. 외부몰 저장
        sellersService.addSellers(sellersRequestDto);

        // #002. 외부몰 공급처 등록
        addOmBasicFee(sellersRequestDto);

        return ApiResult.success();
    }

    @Override
    public ApiResult<?> putSellers(SellersRequestDto sellersRequestDto) throws Exception {

    	//외부몰명 중복확인
    	if(duplicateSellersInfo(sellersRequestDto)) {
    		return ApiResult.result(SellersEnums.SellersMessage.DUPLICATE_SELLER_NAME);
    	}

        // #001. 외부몰 저장
        sellersService.putSellers(sellersRequestDto);

        // #002. 외부몰 공급처 목록 조회
        List<OmBasicFeeListDto> applyOmBasicFeeList = sellersService.getApplyOmBasicFeeList(sellersRequestDto.getOmSellersId());
        List<OmBasicFeeVo> requestOmBasicFeeList = new ArrayList<>();

        // 기존 외부몰에 등록된 공급처목록과 요청dto값 비교에서 없는 공급처만 insert
        for(OmBasicFeeVo vo : sellersRequestDto.getSellersSupplierList()) {
        	if(applyOmBasicFeeList.stream().map(m -> m.getUrSupplierId()).noneMatch(a -> a.equals(vo.getUrSupplierId()))) {
        		requestOmBasicFeeList.add(vo);
        	}
        }

        // #003. 외부몰 공급처 저장
        if(CollectionUtils.isNotEmpty(requestOmBasicFeeList)) {
        	sellersRequestDto.setSellersSupplierList(requestOmBasicFeeList);
        	addOmBasicFee(sellersRequestDto);
        }

        // #004. 공급처목록 판매처그룹 업데이트
        for(OmBasicFeeListDto vo : applyOmBasicFeeList) {
            OmBasicFeeVo omBasicFeeVo = new OmBasicFeeVo();

            omBasicFeeVo.setOmBasicFeeId(vo.getOmBasicFeeId());
            omBasicFeeVo.setSellersGroupCd(sellersRequestDto.getSellersGroupCode());
            putOmBasicFeeSellersGroup(omBasicFeeVo);
        }

        return ApiResult.success();
    }

    public int addOmBasicFee(SellersRequestDto sellersRequestDto) throws Exception{
    	List<OmBasicFeeVo> sellersSuppilerList = sellersRequestDto.getSellersSupplierList();

    	for(OmBasicFeeVo omBasicFeeVo: sellersSuppilerList){
    		omBasicFeeVo.setSellersGroupCd(sellersRequestDto.getSellersGroupCode());
        	omBasicFeeVo.setOmSellersId(sellersRequestDto.getOmSellersId());
        	SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd");
        	Calendar cal = Calendar.getInstance();
        	String startDt = nowDate.format(cal.getTime());
        	omBasicFeeVo.setStartDt(startDt);

        	omBasicFeeBiz.addOmBasicFee(omBasicFeeVo);
        }
    	return 1;
    }

    public boolean duplicateSellersInfo(SellersRequestDto sellersRequestDto) throws Exception{
    	SellersListRequestDto sellersListRequestDto = new SellersListRequestDto();
    	SellersListResponseDto sellersListResponseDto = sellersService.getSellersList(sellersListRequestDto);

    	return sellersListResponseDto.getRows().stream()
				.anyMatch(a -> a.getSellersName().equals(sellersRequestDto.getSellersName()) && a.getOmSellersId() != sellersRequestDto.getOmSellersId());
    }


    /**
     * 기본 수수료 판매처 그룹 수정
     * @param omBasicFeeVo
     * @return
     * @throws Exception
     */
    public int putOmBasicFeeSellersGroup(OmBasicFeeVo omBasicFeeVo) throws Exception{
            omBasicFeeBiz.putOmBasicFeeSellersGroup(omBasicFeeVo);
        return 1;
    }

    @Override
    public List<OmBasicFeeListDto> getApplyOmBasicFeeList(long omSellersId) throws Exception{
        return sellersService.getApplyOmBasicFeeList(omSellersId);
    }

    @Override
    public Long putErpInterfaceStatusChg(SellersRequestDto sellersRequestDto) throws Exception{
        return sellersService.putErpInterfaceStatusChg(sellersRequestDto);
    }
}
