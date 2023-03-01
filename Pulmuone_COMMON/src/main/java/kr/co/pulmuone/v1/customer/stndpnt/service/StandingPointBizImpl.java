package kr.co.pulmuone.v1.customer.stndpnt.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointMallRequestDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.QnaEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointRequestDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointResponseDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.GetStandingPointAttachResultVo;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.GetStandingPointListResultVo;

@Service
public class StandingPointBizImpl implements StandingPointBiz{

    @Autowired
    private StandingPointService standingPointService;

	/**
	 * @Desc  상품입점상담 리스트 조회
	 * @param getFaqListByUserRequsetDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?>  getStandingPointList(StandingPointRequestDto standingPointRequestDto) throws Exception {
		StandingPointResponseDto result = new StandingPointResponseDto();

		if( StringUtils.isNotEmpty(standingPointRequestDto.getApprSearchStat()) && standingPointRequestDto.getApprSearchStat().indexOf("ALL") < 0 ) {
			standingPointRequestDto.setApprSearchStatList(Stream.of(standingPointRequestDto.getApprSearchStat().split(Constants.ARRAY_SEPARATORS))
								                .map(String::trim)
								                .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
								                .collect(Collectors.toList())); // 검색어
		}
		Page<GetStandingPointListResultVo> voList = standingPointService.getStandingPointList(standingPointRequestDto);

		result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());

        return ApiResult.success(result);
	}



    /**
     * @Desc 상품입점상담 리스트 엑셀 다운로드 목록 조회
     * @param StandingPointRequestDto : 상품입점상담 리스트 검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     */
    @Override
    public ExcelDownloadDto getStandingPointExportExcel(StandingPointRequestDto standingPointRequestDto) {

        String excelFileName = "상품입점 상담 관리 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                100, 300, 800, 300, 500, 300, 300, 300, 300, 300, 300 };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "left", "center", "left", "center", "center", "center", "center", "center", "center" };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "rowNumber", "compNm", "question", "managerUserName", "address", "mobile", "tel", "email", "questionStatName", "apprUserName", "createDt" };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "No", "회사명", "문의내용", "담당자명", "주소", "휴대폰번호", "연락처", "이메일", "문의상태", "승인담당자", "등록일자" };

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        if( StringUtils.isNotEmpty(standingPointRequestDto.getApprSearchStat()) && standingPointRequestDto.getApprSearchStat().indexOf("ALL") < 0 ) {
			standingPointRequestDto.setApprSearchStatList(Stream.of(standingPointRequestDto.getApprSearchStat().split(Constants.ARRAY_SEPARATORS))
								                .map(String::trim)
								                .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
								                .collect(Collectors.toList())); // 검색어
		}

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<GetStandingPointListResultVo> itemList = standingPointService.getStandingPointExportExcel(standingPointRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }


    /**
     * 상품입점상담 관리 상세조회
     *
     * @throws Exception
     */
	@Override
    public ApiResult<?> getDetailStandingPoint(StandingPointRequestDto standingPointRequestDto) throws Exception{
		StandingPointResponseDto result = new StandingPointResponseDto();

		if(standingPointRequestDto.getQuestionStat().equals(QnaEnums.StndPointApprType.RECEIVE.getCode())) {
			standingPointRequestDto.setQuestionStat(QnaEnums.StndPointApprType.REVIEW.getCode());
			standingPointService.putStandingPointStatus(standingPointRequestDto);
		}

		GetStandingPointListResultVo vo = new GetStandingPointListResultVo();
		vo = standingPointService.getDetailStandingPoint(standingPointRequestDto);

		GetStandingPointAttachResultVo rowsFile = standingPointService.getStandingPointAttach(standingPointRequestDto);

		result.setRow(vo);
		result.setRowsFile(rowsFile);

    	return ApiResult.success(result);
    }




    /**
     * 상품입점상담 승인 상태변경
     *
     * @throws Exception
     */
	@Override
    public ApiResult<?> putStandingPointStatus(StandingPointRequestDto standingPointRequestDto) throws Exception{

		standingPointRequestDto.setQuestionStat(standingPointRequestDto.getApprStat());
		return standingPointService.putStandingPointStatus(standingPointRequestDto);
    }

	@Override
	public void addStandingPointQna(StandingPointMallRequestDto dto) throws Exception {
		dto.setQuestionStatus(QnaEnums.StndPointApprType.RECEIVE.getCode());
		standingPointService.addStandingPointQna(dto);
	}

}
