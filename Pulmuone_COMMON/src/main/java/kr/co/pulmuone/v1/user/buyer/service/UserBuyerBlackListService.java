package kr.co.pulmuone.v1.user.buyer.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import com.google.gson.Gson;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalGoodsListRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.mapper.user.buyer.UserBlackMapper;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadDto;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadResponseDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadFailVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadInfoVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadSuccessVo;
import kr.co.pulmuone.v1.order.claim.util.ClaimExcelUploadUtil;
import kr.co.pulmuone.v1.user.dormancy.dto.*;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserBlackHistoryListResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserBlackListResultVo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <PRE>
 * Forbiz Korea
 * 회원관리 - 블랙리스트 회원 ServiceImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200625    	  박영후           최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class UserBuyerBlackListService {

	@Autowired
	private UserBlackMapper userBlackMapper;

	DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * 회원 블랙리스트 조회
	 * @param getUserBlackListRequestDto
	 * @return GetUserBlackListResponseDto
	 * @throws Exception
	 */
	protected GetUserBlackListResponseDto getBlackListUserList(GetUserBlackListRequestDto getUserBlackListRequestDto) throws Exception {
		if(StringUtils.isNotEmpty(getUserBlackListRequestDto.getCondiValue())) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(getUserBlackListRequestDto.getCondiValue(), "\n|,");
			while(st.hasMoreElements()) {
				String object = (String)st.nextElement();
				array.add(object);
			}
			getUserBlackListRequestDto.setCondiValueArray(array);
		}

//		int total = userBlackMapper.getBlackListUserListCount(getUserBlackListRequestDto);	// total
		PageMethod.startPage(getUserBlackListRequestDto.getPage(), getUserBlackListRequestDto.getPageSize());
		Page<GetUserBlackListResultVo> rows = userBlackMapper.getBlackListUserList(getUserBlackListRequestDto);	// rows

		return GetUserBlackListResponseDto.builder()
										.total((int) rows.getTotal())
										.rows(rows.getResult())
										.build();
	}



	/**
	 * 회원 블랙리스트 히스토리 조회
	 * @param getUserBlackHistoryListRequestDto
	 * @return GetUserBlackHistoryListResponseDto
	 * @throws Exception
	 */
	protected GetUserBlackHistoryListResponseDto getUserBlackHistoryList(GetUserBlackHistoryListRequestDto getUserBlackHistoryListRequestDto) throws Exception {

		 Page<GetUserBlackHistoryListResultVo> rows = userBlackMapper.getUserBlackHistoryList(getUserBlackHistoryListRequestDto);	// rows

		 return GetUserBlackHistoryListResponseDto.builder()
												 .rows(rows.getResult())
												 .build();
	 }


	/**
	 * 회원 블랙 리스트 등록
	 * @param addUserBlackRequestDto
	 * @return	AddUserBlackResponseDto
	 * @throws Exception
	 */
	protected AddUserBlackResponseDto addUserBlack(AddUserBlackRequestDto addUserBlackRequestDto) throws Exception {
		AddUserBlackResponseDto result = new AddUserBlackResponseDto();

		userBlackMapper.addUserBlack(addUserBlackRequestDto);

		result.setUrUserId(addUserBlackRequestDto.getUrUserId());

		return result;
	}

	/**
	 * 회원 블랙리스트 리스트조회 엑셀 다운로드
	 * @param getUserBlackListRequestDto
	 * @return
	 */
	protected ExcelDownloadDto getBlackListUserListExportExcel(GetUserBlackListRequestDto getUserBlackListRequestDto) throws Exception {

		String excelFileName = "블랙리스트 회원"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		/*
		 * 컬럼별 width 목록 : 단위 pixel
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				200, 200, 200, 200, 200, 200, 200, 200 };

		/*
		 * 본문 데이터 컬럼별 정렬 목록
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
		 * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center", "center", "center", "center" };

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록
		 * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] propertyListOfFirstWorksheet = { //
				"employeeYn", "userName", "loginId", "mobile", "mail", "eventLimitTp", "accumulateCount", "lastCreateDate" };

		String[] cellTypeListOfFirstWorksheet = {
				"string", "string", "string", "string", "string", "string", "string", "string" };

		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"회원유형", "회원명", "회원ID", "휴대폰", "EMAIL", "이벤트제한여부", "누적횟수", "최근등록일" };

		// 워크시트 DTO 생성 후 정보 세팅
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.cellTypeList(cellTypeListOfFirstWorksheet)
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();

		// 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

		/*
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음
		 * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
		 */
		if(StringUtils.isNotEmpty(getUserBlackListRequestDto.getCondiValue())) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(getUserBlackListRequestDto.getCondiValue(), "\n|,");
			while(st.hasMoreElements()) {
				String object = (String)st.nextElement();
				array.add(object);
			}
			getUserBlackListRequestDto.setCondiValueArray(array);
		}
		List<GetUserBlackListResultVo> itemList = userBlackMapper.getBlackListUserListExportExcel(getUserBlackListRequestDto);

		firstWorkSheetDto.setExcelDataList(itemList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;

	}

	/**
	 * 회원 블랙리스트 엑셀업로드
	 * @param file
	 * @return
	 * @throws Exception
	 */
	protected ApiResult<?> addClaimExcelUpload(MultipartFile file) throws Exception {

		if (ExcelUploadUtil.isFile(file) != true)
			return ApiResult.result(ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

		// Excel Import 정보 -> Dto 변환
		Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
		if (uploadSheet == null)
			return ApiResult.result(ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);

		// Excel 데이터 Mapping
		List<AddUserBlackRequestDto> excelList = this.setExcelUploadExcelData(uploadSheet);

		if (excelList.isEmpty()) {
			return ApiResult.result(ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);
		}

		// 항목별 검증 진행
		for (AddUserBlackRequestDto addUserBlackRequestDto : excelList) {

			// 회원아이디체크
			String urUserId = userBlackMapper.selectLoginIdCheck(addUserBlackRequestDto);
			if(urUserId == null) {
				return ApiResult.result(ExcelUploadValidateEnums.UploadResponseCode.NOT_URUSERID);
			}
			// 값체크
			if(!StringUtil.isEquals(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode(), addUserBlackRequestDto.getEventLimitTp()) && !StringUtil.isEquals(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode(), addUserBlackRequestDto.getEventLimitTp())) {
				return ApiResult.result(ExcelUploadValidateEnums.UploadResponseCode.IS_EVENTLIMIT);
			}
			// 공란체트
			if(StringUtil.isNvl(addUserBlackRequestDto.getLoginId()) || StringUtil.isNvl(addUserBlackRequestDto.getUserBlackReason()) || StringUtil.isNvl(addUserBlackRequestDto.getEventLimitTp())) {
				return ApiResult.result(ExcelUploadValidateEnums.UploadResponseCode.IS_NVL);
			}
			// 회원아이디 중복체크
			int count = (int) excelList.stream().filter(vo -> StringUtil.nvl(addUserBlackRequestDto.getLoginId()).equals(vo.getLoginId())).count();
			if(count > 1) {
				return ApiResult.result(ExcelUploadValidateEnums.UploadResponseCode.OVERLAP_URUSERID);
			}

		}

		int successCnt = 0;
		// 저장
		for(AddUserBlackRequestDto addUserBlackRequestDto : excelList) {

			String urUserId = userBlackMapper.selectLoginIdCheck(addUserBlackRequestDto);
			addUserBlackRequestDto.setUrUserId(urUserId);

			userBlackMapper.addUserBlack(addUserBlackRequestDto);
			userBlackMapper.putUserEventJoinYn(addUserBlackRequestDto);
			successCnt += 1;
		}


		AddUserBlackResponseDto result = new AddUserBlackResponseDto();

		result.setSuccessCnt(successCnt);

		return ApiResult.success(result);
	}

	/**
	 * 회원 블랙리스트 엑셀업로드 데이터 Mapping
	 * @param sheet
	 * @return
	 */
	private List<AddUserBlackRequestDto> setExcelUploadExcelData(Sheet sheet) {

		int addRowNum = 1;
		List<AddUserBlackRequestDto> excelList = new ArrayList<>();

		for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);

			// 첫번째 두번째가 Null이 아닌경우만 데이터 생성
			if (row != null && (StringUtil.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0))) || StringUtil.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(1))))
			) {
				String eventLimitTp = "E";
				if(StringUtil.isEquals(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCodeName(), ExcelUploadUtil.cellValue(row.getCell(2)))){
					eventLimitTp = "Y";
				} else if(StringUtil.isEquals(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCodeName(), ExcelUploadUtil.cellValue(row.getCell(2)))){
					eventLimitTp = "N";
				}

				excelList.add(AddUserBlackRequestDto.builder()
						.urUserId("")
						.userBlackReason(ExcelUploadUtil.cellValue(row.getCell(3)))
						.loginId(ExcelUploadUtil.cellValue(row.getCell(1)))
						.eventLimitTp(eventLimitTp)
						.build());
			}
		}
		return excelList;
	}

}
