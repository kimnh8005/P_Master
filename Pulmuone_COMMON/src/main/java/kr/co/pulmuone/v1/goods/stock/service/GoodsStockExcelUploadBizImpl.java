package kr.co.pulmuone.v1.goods.stock.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.StockEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockExcelUploadRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExcelUploadResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 재고 엑셀 업로드
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.11.11  이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class GoodsStockExcelUploadBizImpl implements GoodsStockExcelUploadBiz {

	@Autowired
	GoodsStockExcelUploadService goodsStockExcelUploadService;

	/**
	 * ERP 재고 엑셀 업로드
	 * @param StockExcelUploadRequestDto
	 * @return int
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addExcelUpload(StockExcelUploadRequestDto stockExcelUploadRequestDto) throws Exception {

		//업로드 설정시간 조회
		String env = goodsStockExcelUploadService.getEnvironment();
		String[] timeValue = env.split("-");

		//현재시간을 구함
		LocalTime now = LocalTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HHmmss");

		int currentTime = Integer.parseInt(now.format(dateTimeFormatter));
		int startTime   = Integer.parseInt(timeValue[0]);
		int endTime     = Integer.parseInt(timeValue[1]);

		//설정시간 안에 있으면 업로드 거부
        if(currentTime >= startTime && currentTime <= endTime) {
		   return ApiResult.result(StockEnums.StockExcelUploadErrMsg.UPLOAD_REJECT);
        }

        List<StockExcelUploadResultVo> resultList = null;

		int totalCnt = 0;  //총건수
		int successCnt = 0;//성공건수
		int failCnt = 0;   //실패건수
		String result = "";//결과

		String psKey = StockEnums.UrWarehouseId.WAREHOUSE_YONGIN_ID.getCode();

		String urWarehouseId = goodsStockExcelUploadService.getConfigValue(psKey);//용인 출고처 조회

		goodsStockExcelUploadService.addExcelUploadLog();//재고 엑셀 업로드 로그 최초저장

		// [S] 동일한 기준일의 엑셀 업로드에 의한 용인 출고처의 품목 유통기한별 재고 삭제한다.
		if (stockExcelUploadRequestDto.getUploadList().size() > 0) {
	        String baseDt = LocalDate.now().plusDays(1).toString().replaceAll("-", ""); // 엑셀 데이터는 기준일이 내일
			goodsStockExcelUploadService.delStockExprYongin(baseDt); //품목정보 중복 삭제
		}
		// [E] 동일한 기준일의 엑셀 업로드에 의한 용인 출고처의 품목 유통기한별 재고 삭제한다.

		for(StockExcelUploadListRequestDto dto : stockExcelUploadRequestDto.getUploadList()) {

			String reg = "^[0-9]*$";//정규표현식(숫자만  체크)

			boolean ilItemCd = true;
			if(dto.getIlItemCd() == null || "".equals(dto.getIlItemCd())) {
				ilItemCd = false;
			}

			boolean qty = true;
			if(dto.getStockQty() == null || "".equals(dto.getStockQty())) {
				qty = false;
			}else {
				qty = Pattern.matches(reg, dto.getStockQty());//재고수량 숫자체크
			}

			boolean date = true;
			if(dto.getExpirationDt() == null || "".equals(dto.getExpirationDt())) {
			   date = false;
			}else {
			   date = Pattern.matches("\\d{2}\\d{2}\\d{2}\\d{2}", dto.getExpirationDt().replaceAll("-", ""));//날짜형식 체크
			}

			if (dto.getIlItemCd() != null && !"".equals(dto.getIlItemCd())
					&& ilItemCd == true && dto.getExpirationDt() != null
					&& !"".equals(dto.getExpirationDt()) && date == true && dto.getStockQty() != null
					&& !"".equals(dto.getStockQty()) && qty == true) {

	            dto.setUrWarehouseId(Integer.parseInt(urWarehouseId)); //용인 출고처

	            resultList = goodsStockExcelUploadService.getStockInfoList(dto);//품목정보 조회

	        for(StockExcelUploadResultVo vo : resultList) {
	        	goodsStockExcelUploadService.addExcelUpload(vo); //엑셀 업로드 저장
	        	vo.setSuccessYn("Y");
	        	goodsStockExcelUploadService.addSuccessExcelDetlLog(vo); //재고 엑셀 업로드 로그 상세 저장(성공)
	        }

	        if(resultList == null || resultList.isEmpty()) {
	           dto.setSuccessYn("N");
	           dto.setMsg(StockEnums.StockExcelUploadErrMsg.NO_REG_IL_ITEMCD.getMessage());
	           goodsStockExcelUploadService.addFailExcelDetlLog(dto); //재고 엑셀 업로드 로그 상세 저장(실패)
	        }

		  } else {
				if(dto.getIlItemCd() == null || "".equals(dto.getIlItemCd()) || ilItemCd == false) {
					dto.setSuccessYn("N");
					dto.setMsg(StockEnums.StockExcelUploadErrMsg.NO_IL_ITEMCD.getMessage());//품목코드 에러
					goodsStockExcelUploadService.addFailExcelDetlLog(dto); //재고 엑셀 업로드 로그 상세 저장(실패)
				}else if(dto.getExpirationDt() == null || "".equals(dto.getExpirationDt()) || date == false) {
					dto.setSuccessYn("N");
					dto.setMsg(StockEnums.StockExcelUploadErrMsg.NO_EXPIRATION_DT.getMessage());//유통기한 에러
					goodsStockExcelUploadService.addFailExcelDetlLog(dto); //재고 엑셀 업로드 로그 상세 저장(실패)
				}else if(dto.getStockQty() == null || "".equals(dto.getStockQty()) || qty == false){
					dto.setSuccessYn("N");
					dto.setMsg(StockEnums.StockExcelUploadErrMsg.NO_STOCK_QTY.getMessage());//재고수량 에러
					goodsStockExcelUploadService.addFailExcelDetlLog(dto); //재고 엑셀 업로드 로그 상세 저장(실패)
				}
			}//end if

		}//end for

		String fileNm = stockExcelUploadRequestDto.getUploadList().get(0).getFileNm();//파일명
		String userId = stockExcelUploadRequestDto.getUploadList().get(0).getUserVo().getUserId();//userId

		successCnt = goodsStockExcelUploadService.getSuccessCnt();//성공건수 조회
		failCnt    = goodsStockExcelUploadService.getFailCnt();   //실패건수 조회

		goodsStockExcelUploadService.putExcelUploadLog(fileNm, userId, successCnt, failCnt);//재고 엑셀 업로드 로그 수정

		totalCnt = successCnt + failCnt; //총건수

		result = Integer.toString(totalCnt)+"|"+Integer.toString(successCnt)+"|"+Integer.toString(failCnt); //총건수|성공건수|실패건수

		return ApiResult.success(result);
	}

	/**
	 * 용인물류 유통기한별 재고리스트 엑셀 다운로드
	 * @param	StockListRequestDto
	 * @return	List<StockListResultVo>
	 */
	@Override
	public ExcelDownloadDto getStockExprListExportExcel() {
		 String excelFileName = "item_stockExprList"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = { //
	                300, 200, 150, 500, 170, 150};

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = { //
	        		"center", "center", "center", "center", "center", "left"};

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
//	                "baseDt", "ilItemCd", "ilGoodsId", "itemNm", "expirationDt", "stockQty"
	                "baseDt", "ilItemCd", "ilGoodsId", "itemNm", "", "stockQty"
	        	};

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "기준일자(날짜형식 : 2021-07-01)", "erp코드", "이샵코드", "제품명", "재고유통기한", "재고수량"};

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
	        List<StockExcelUploadResultVo> orgaDisList = goodsStockExcelUploadService.getStockExprList();

	        firstWorkSheetDto.setExcelDataList(orgaDisList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;
	}

}
