package kr.co.pulmuone.v1.customer.feedback.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosResponseDto;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosDetailVo;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosListVo;
import kr.co.pulmuone.v1.comm.constants.Constants;

@Service
public class FeedbackBizImpl implements FeedbackBiz{

    @Autowired
    private FeedbackService feedbackService;


    /**
     * 후기관리 목록조회
     *
     * @throws Exception
     */
    @Override
    public  ApiResult<?>  getFeedbackList(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception {

    	FeedbackBosResponseDto result = new FeedbackBosResponseDto();

    	if (!feedbackBosRequestDto.getFindKeyword().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(feedbackBosRequestDto.getFindKeyword(), "\n|,");
			while (st.hasMoreElements()) {
				String object = (String) st.nextElement();
				array.add(object);
			}
			feedbackBosRequestDto.setFindKeywordArray(array);
		}

    	if( StringUtils.isNotEmpty(feedbackBosRequestDto.getDisplayYn()) && feedbackBosRequestDto.getDisplayYn().indexOf("ALL") < 0 ) {
    		feedbackBosRequestDto.setDisplayYnList(Stream.of(feedbackBosRequestDto.getDisplayYn().split(Constants.ARRAY_SEPARATORS))
                                                      .map(String::trim)
                                                      .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                                      .collect(Collectors.toList()));
        }

        if( StringUtils.isNotEmpty(feedbackBosRequestDto.getFeedbackFilter()) ) {
        	feedbackBosRequestDto.setFeedbackFilterList(Stream.of(feedbackBosRequestDto.getFeedbackFilter().split(Constants.ARRAY_SEPARATORS))
                                                       .map(String::trim)
                                                       .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                                       .collect(Collectors.toList()));
        }

        Page<FeedbackBosListVo> voList = feedbackService.getFeedbackList(feedbackBosRequestDto);

        result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());

        return ApiResult.success(result);
    }




    /**
     * @Desc 후기관리 리스트 엑셀 다운로드 목록 조회
     * @param FeedbackBosRequestDto : 후기관리 리스트 검색 조건 request dto
     * @return ExcelDownloadDto : ExcelDownloadView 에서 처리할 엑셀 다운로드 dto
     */
    @Override
    public ExcelDownloadDto feedbackExportExcel(FeedbackBosRequestDto feedbackBosRequestDto) {

        String excelFileName = "후기관리 리스트"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                70, 200, 200, 300, 200, 300, 150, 150, 150, 200, 100 };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center", "center", "left", "center", "left", "center", "center", "center", "center", "center" };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "rowNumber", "feedbackProductTypeName", "feedbackTypeName", "itemName", "itemCode", "comment", "userName", "userId", "satisfactionScore", "createDate", "displayYn" };

        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "No", "후기구분", "후기작성유형", "품목명", "품목코드", "후기내용", "회원명", "회원ID", "만족도", "등록일", "노출여부" };

        // 워크시트 DTO 생성 후 정보 세팅
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        if( StringUtils.isNotEmpty(feedbackBosRequestDto.getFeedbackFilter()) ) {
        	feedbackBosRequestDto.setFeedbackFilterList(Stream.of(feedbackBosRequestDto.getFeedbackFilter().split(Constants.ARRAY_SEPARATORS))
                                                       .map(String::trim)
                                                       .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                                       .collect(Collectors.toList()));
        }

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
         * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
         */
        List<FeedbackBosListVo> itemList = feedbackService.feedbackExportExcel(feedbackBosRequestDto);

        firstWorkSheetDto.setExcelDataList(itemList);

        // excelDownloadDto 생성 후 workSheetDto 추가
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;

    }

    /**
     * 후기관리 상세조회
     *
     * @throws Exception
     */
	@Override
    public ApiResult<?> getDetailFeedback(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception{
		FeedbackBosDetailResponseDto result = new FeedbackBosDetailResponseDto();
		FeedbackBosDetailVo vo = new FeedbackBosDetailVo();
		vo = feedbackService.getDetailFeedback(feedbackBosRequestDto);

		result.setRow(vo);

    	return ApiResult.success(result);
    }


	/**
	 * @Desc  후기관리 상세 첨부파일 이미지
	 * @param String
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getImageList(String feedbackId) throws Exception {
		FeedbackBosDetailResponseDto result = new FeedbackBosDetailResponseDto();

		List<FeedbackBosDetailVo> imageList = feedbackService.getImageList(feedbackId);
		result.setRows(imageList);

		return ApiResult.success(result);
	}



	/**
	 * 후기관리정보 수정
	 *
	 * @param FeedbackBosRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putFeedbackInfo(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception {

		feedbackService.putFeedbackInfo(feedbackBosRequestDto);

    	return ApiResult.success();
	}



}
