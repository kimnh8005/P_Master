package kr.co.pulmuone.v1.policy.shiparea.service;

import java.util.List;

import kr.co.pulmuone.v1.policy.shiparea.dto.vo.NonDeliveryAreaInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.policy.shiparea.PolicyShipareaMapper;
import kr.co.pulmuone.v1.policy.shiparea.dto.GetBackCountryListResponseDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.GetBackCountryResponseDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.PolicyShipareaDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.vo.GetBackCountryResultVo;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20201103		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */

@Service
@RequiredArgsConstructor
public class PolicyShipareaService {

	@Autowired
	private final PolicyShipareaMapper policyShipareaMapper;

	protected GetBackCountryListResponseDto getBackCountryList(PolicyShipareaDto paramDto) {
		GetBackCountryListResponseDto result = new GetBackCountryListResponseDto();

    	PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
    	Page<GetBackCountryResultVo> rows = policyShipareaMapper.getBackCountryList(paramDto);
		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());
		return result;
    }

    protected int duplicateBackCountryCount(PolicyShipareaDto dupParamDto) {
        return policyShipareaMapper.duplicateBackCountryCount(dupParamDto);
    }

    protected ApiResult<?> addBackCountry(PolicyShipareaDto addParam) {
         int result = policyShipareaMapper.addBackCountry(addParam);
         if(result < 1) {
        	 return ApiResult.result(BaseEnums.Default.FAIL);
         }
         return ApiResult.success();
    }

    protected ApiResult<?> putBackCountry(PolicyShipareaDto putParam) {
    	int result = policyShipareaMapper.putBackCountry(putParam);
        if(result < 1) {
       	 return ApiResult.result(BaseEnums.Default.FAIL);
        }
        return ApiResult.success();
    }

    protected ApiResult<?> delBackCountry(PolicyShipareaDto paramDto) {
    	int result = policyShipareaMapper.delBackCountry(paramDto);
        if(result < 1) {
       	 return ApiResult.result(BaseEnums.Default.FAIL);
        }
        return ApiResult.success();
    }

    /**
	 * 도서산관 상세조회
	 * @param GetBackCountryRequestDto
	 * @return GetBackCountryResponseDto
	 * @throws Exception
	 */
	public GetBackCountryResponseDto getBackCountry(PolicyShipareaDto dto){

		GetBackCountryResponseDto result = new GetBackCountryResponseDto();
		GetBackCountryResultVo vo = policyShipareaMapper.getBackCountry(dto);
		result.setRows(vo);

		return result;
	}

	protected ExcelDownloadDto getBackCountryExcelList(String[] zipCodes) {

		/*
         * 엑셀 파일 / 워크시트 정보
         *
         */

        String excelFileName = "back_country"; // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
        String excelSheetName = "sheet1"; // 엑셀 파일 내 워크시트 이름

        /*
         * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
         *
         */

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                150, 250, 250, 250 };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "center" };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "zipCode", "jejuYn", "islandYn", "createDate" };

        /*
         * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
         *
         */
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "우편번호", "도서산간 (1 권역)", "제구 (2 권역)", "등록일자"
        };

        /*
         * 워크시트 DTO 생성 후 정보 세팅
         *
         */
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
         *
         */
        List<GetBackCountryResultVo> rows = policyShipareaMapper.getBackCountryExcelList(zipCodes);

		firstWorkSheetDto.setExcelDataList(rows);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;

	}


	protected boolean isUndeliverableArea(String undeliverableType, String zipCode) throws Exception {
		return policyShipareaMapper.isUndeliverableArea(undeliverableType, zipCode);
	}

	/**
	 * 배송불가권역 여부
	 * @param undeliverableTypes
	 * @param zipCode
	 * @return
	 * @throws Exception
	 */
	protected boolean isNonDeliveryArea(String[] undeliverableTypes, String zipCode) throws Exception {
		return policyShipareaMapper.isNonDeliveryArea(undeliverableTypes, zipCode);
	}

	/**
	 * 배송불가권역 안내 메시지
	 *
	 * @param undeliverableTypes
	 * @param zipCode
	 * @return
	 * @throws Exception
	 */
	protected NonDeliveryAreaInfoVo getNonDeliveryAreaInfo(String[] undeliverableTypes, String zipCode) throws Exception {
		return policyShipareaMapper.getNonDeliveryAreaInfo(undeliverableTypes, zipCode);
	}
}
