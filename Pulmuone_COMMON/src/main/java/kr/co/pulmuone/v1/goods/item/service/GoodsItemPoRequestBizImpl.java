package kr.co.pulmuone.v1.goods.item.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoRequestResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPoRequestVo;
import kr.co.pulmuone.v1.outmall.sellers.dto.vo.SellersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <PRE>
 * Forbiz Korea
 * 행사발주관리 Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.01.21  정형진
 * =======================================================================
 * </PRE>
 */
@Service
public class GoodsItemPoRequestBizImpl implements GoodsItemPoRequestBiz{

	@Autowired
	GoodsItemPoRequestService goodsItemPoRequestService;

	@Autowired
	GoodsItemPoTypeService goodsItemPoTypeService;

	@Override
	@UserMaskingRun(system = "MUST_MASKING")  // 강제 마스킹
	public ApiResult<?> getPoRequestList(ItemPoRequestDto paramDto) {
		// TODO Auto-generated method stub
		ItemPoRequestResponseDto result = new ItemPoRequestResponseDto();

		Page<ItemPoRequestVo> rows = goodsItemPoRequestService.getPoRequestList(paramDto);

		result.setTotal(rows.getTotal());
		result.setRows(rows.getResult());

		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> addItemPoRequest(ItemPoRequestDto paramDto) throws Exception {
		// TODO Auto-generated method stub
		goodsItemPoRequestService.addItemPoRequest(paramDto);

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> getPoRequest(String ilPoEventId) {
		// TODO Auto-generated method stub
		ItemPoRequestResponseDto result = new ItemPoRequestResponseDto();
		ItemPoRequestVo vo = goodsItemPoRequestService.getPoRequest(ilPoEventId);

		result.setDetail(vo);

		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> putPoRequest(ItemPoRequestDto paramDto) throws Exception {
		// TODO Auto-generated method stub
		goodsItemPoRequestService.putPoRequest(paramDto);

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> delPoRequest(ItemPoRequestDto paramDto) throws Exception {
		// TODO Auto-generated method stub
		goodsItemPoRequestService.delPoRequest(paramDto);

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> addPoRequestExcelUpload(ItemPoRequestDto paramDto) throws Exception {
		// TODO Auto-generated method stub
		ItemPoRequestVo resultVo = new ItemPoRequestVo();

		String result = "";//결과

		int failCnt = 0;
		int successCnt = 0;
		boolean logInsertFlag = false;
		String logId = "";

		// 업로드 내용이 없을 경우
		if(paramDto.getUploadList().size() < 1) {
			result = Integer.toString(0)+"|"+Integer.toString(0)+"|"+Integer.toString(0); //총건수|성공건수|실패건수
			return ApiResult.success(result);
		}

		for(ItemPoRequestVo itemPoRequestVo : paramDto.getUploadList()) {
			itemPoRequestVo.setSuccessYn("Y");

			GoodsSearchVo goodsInfo = null;
			String reg = "^[0-9]*$";//정규표현식(숫자만  체크)

			// 상품 코드 체크
			if(null == itemPoRequestVo.getIlGoodsId()) {
				itemPoRequestVo.setSuccessYn("N");
				itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.GOODS_VALUE_NONE.getMessage());
			} else {
				if (Pattern.matches(reg, itemPoRequestVo.getIlGoodsId())) {
					goodsInfo = goodsItemPoRequestService.getGoodsIdInfo(itemPoRequestVo.getIlGoodsId());

					if(goodsInfo == null) {
						itemPoRequestVo.setSuccessYn("N");
						itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.GOODS_ID_EMPTY.getMessage());
					}
				} else {
					itemPoRequestVo.setSuccessYn("N");
					itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.GOODS_ID_FORMAT_ERROR.getMessage());
				}
			}

			// 행사 발주 수량 체크
			if ("Y".equals(itemPoRequestVo.getSuccessYn())) {
				if(null == itemPoRequestVo.getPoEventQty()) {
					itemPoRequestVo.setSuccessYn("N");
					itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.PO_EVENT_QTY_NONE.getMessage());
				} else {
					if (Pattern.matches(reg, itemPoRequestVo.getPoEventQty())) {
						try {
							int poQty = Integer.valueOf(itemPoRequestVo.getPoEventQty());

							if(poQty < 0) {
								itemPoRequestVo.setSuccessYn("N");
								itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.PO_EVENT_QTY_MIN_ERROR.getMessage());
							}
						} catch(NumberFormatException e) {
							itemPoRequestVo.setSuccessYn("N");
							itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.PO_EVENT_QTY_PARSE_ERROR.getMessage());
						}
					} else {
						itemPoRequestVo.setSuccessYn("N");
						itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.PO_EVENT_QTY_PARSE_ERROR.getMessage());
					}
				}
			}

			// 판매처 코드 체크
			if ("Y".equals(itemPoRequestVo.getSuccessYn())) {
				if(null == itemPoRequestVo.getOmSellersId()) {
					itemPoRequestVo.setSuccessYn("N");
					itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.SELLERS_ID_EMPTY.getMessage());
				}else {
					SellersVo sellersVo = goodsItemPoRequestService.getSellersInfo(itemPoRequestVo.getOmSellersId());

					if(sellersVo == null) {
						itemPoRequestVo.setSuccessYn("N");
						itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.SELLERS_ID_NONE.getMessage());
					}else {
						itemPoRequestVo.setSellersCd(sellersVo.getSellersCd());
					}

				}
			}

			// 행사 시작일 체크
			if ("Y".equals(itemPoRequestVo.getSuccessYn())) {
				if(null == itemPoRequestVo.getEventStartDt()) {
					itemPoRequestVo.setSuccessYn("N");
					itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.EVENT_START_DT_NONE.getMessage());
				}
				else {
					try {
						LocalDate eventStartDt = LocalDate.parse(itemPoRequestVo.getEventStartDt()); //행사시작일
						if (eventStartDt.isBefore(LocalDate.now())) {
							itemPoRequestVo.setSuccessYn("N");
							itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.EVENT_START_DT_MIN_ERROR.getMessage());
						}
					} catch(DateTimeParseException e) {
						itemPoRequestVo.setSuccessYn("N");
						itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.EVENT_START_DT_FORMAT_ERROR.getMessage());
					}
				}
			}

			// 행사 종료일 체크
			if ("Y".equals(itemPoRequestVo.getSuccessYn())) {
				if(null == itemPoRequestVo.getEventEndDt()) {
					itemPoRequestVo.setSuccessYn("N");
					itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.EVENT_END_DT_NONE.getMessage());
				} else {
					try {
						LocalDate eventStartDt = LocalDate.parse(itemPoRequestVo.getEventStartDt()); //행사시작일
						LocalDate eventEndDt = LocalDate.parse(itemPoRequestVo.getEventEndDt()); //행사종료일
						if (eventEndDt.isBefore(eventStartDt)) {
							itemPoRequestVo.setSuccessYn("N");
							itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.EVENT_END_DT_MIN_ERROR.getMessage());
						}
					} catch(DateTimeParseException e) {
						itemPoRequestVo.setSuccessYn("N");
						itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.EVENT_END_DT_FORMAT_ERROR.getMessage());
					}
				}
			}

			// 입고요청일, 발주예정일 체크
			if ("Y".equals(itemPoRequestVo.getSuccessYn())) {
				HashMap eventResult = goodsItemPoTypeService.getItemPoDayForEvent(goodsInfo.getIlPoTpId(), itemPoRequestVo.getEventStartDt()); // 입고요청일, 발주예정일 계산

				if (eventResult != null && eventResult.get("PO_SCHEDULE_DT") != null) { // 발주예정일
					try {
						LocalDate poScheduleDt = LocalDate.parse(eventResult.get("PO_SCHEDULE_DT").toString());
						if (poScheduleDt.isBefore(LocalDate.now())) {
							itemPoRequestVo.setSuccessYn("N");
							itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.PO_SCHEDULE_DT_MIN_ERROR.getMessage());
						} else if (poScheduleDt.isEqual(LocalDate.now())) {
							if (LocalTime.now().isAfter(LocalTime.of(12, 0, 0))) {
								itemPoRequestVo.setSuccessYn("N");
								itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.PO_SCHEDULE_DT_MIN_ERROR.getMessage());
							}
						} else { // 발주예정일 성공
							itemPoRequestVo.setPoScheduleDt(eventResult.get("PO_SCHEDULE_DT").toString());
						}
					} catch(DateTimeParseException e) {
						itemPoRequestVo.setSuccessYn("N");
						itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.PO_SCHEDULE_DT_FORMAT_ERROR.getMessage());
					}
				} else {
					itemPoRequestVo.setSuccessYn("N");
					itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.PO_SCHEDULE_DT_NONE.getMessage());
				}

				if (eventResult != null && eventResult.get("RECEVING_REQ_DT") != null) { // 입고요청일
					try {
						LocalDate poScheduleDt = LocalDate.parse(eventResult.get("PO_SCHEDULE_DT").toString()); // 날짜 포멧확인을 위해 LocalDate 형식으로 parsing
						itemPoRequestVo.setRecevingReqDt(eventResult.get("RECEVING_REQ_DT").toString()); // 입고요청일 성공
					} catch(DateTimeParseException e) {
						itemPoRequestVo.setSuccessYn("N");
						itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.RECEVING_REQ_DT_FORMAT_ERROR.getMessage());
					}
				} else {
					itemPoRequestVo.setSuccessYn("N");
					itemPoRequestVo.setMsg(ItemEnums.ItemPoReqeust.RECEVING_REQ_DT_NONE.getMessage());
				}
			}

			itemPoRequestVo.setCreateId(paramDto.getUserVo().getUserId());

			if(!logInsertFlag) {
				goodsItemPoRequestService.addPoEventUploadLog(itemPoRequestVo);
				logInsertFlag = true;
				logId = itemPoRequestVo.getLogId();
			}
			itemPoRequestVo.setLogId(logId);

			goodsItemPoRequestService.addPoEventUploadDtlLog(itemPoRequestVo);

			if ("Y".equals(itemPoRequestVo.getSuccessYn())) {
				successCnt++;
				goodsItemPoRequestService.addItemPoRequestUpload(itemPoRequestVo);
			} else {
				failCnt++;
			}
		}

		if(logInsertFlag) {

			paramDto.setSuccessCnt(successCnt);
			paramDto.setFailCnt(failCnt);
			paramDto.setLogId(logId);
			goodsItemPoRequestService.putPoEventUploadLog(paramDto);

		}
		result = Integer.toString(paramDto.getUploadList().size())+"|"+Integer.toString(successCnt)+"|"+Integer.toString(failCnt); //총건수|성공건수|실패건수

		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getPoRequestUploadList(ItemPoRequestDto paramDto) {
		// TODO Auto-generated method stub
		ItemPoRequestResponseDto result = new ItemPoRequestResponseDto();

		Page<ItemPoRequestVo> rows = goodsItemPoRequestService.getPoRequestUploadList(paramDto);

		result.setTotal(rows.getTotal());
		result.setRows(rows.getResult());
		return ApiResult.success(result);
	}

	@Override
	public ExcelDownloadDto createPoRequestUplodFailList(ItemPoRequestDto paramDto) {
		// TODO Auto-generated method stub
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		String excelFileName = "행사 발주 엑셀 업로드 실패 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				150, 180, 180, 180, 180,
				250
		};

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */

		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center",
				"center"
		};

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
		 * 함
		 */

		String[] propertyListOfFirstWorksheet = { //
				"ilGoodsId", "poEventQty", "eventStartDt", "eventEndDt", "omSellersId",
				"msg"
		};

		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"상품코드", "행사발주수량", "행사시작일", "행사종료일", "판매처코드",
				"실패사유"
		};

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
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData 를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		List<ItemPoRequestVo> goodsPriceList = goodsItemPoRequestService.getPoRequestUploadFailList(paramDto);
		firstWorkSheetDto.setExcelDataList(goodsPriceList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();
		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	@Override
	public ExcelDownloadDto createPoRequestList(ItemPoRequestDto paramDto) {
		// TODO Auto-generated method stub
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		String excelFileName = "행사 발주 내역"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨

		/*
		 * 컬럼별 width 목록 : 단위 pixel ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는
		 * 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				150, 180, 180, 180, 250,
				250, 250, 180, 180, 180,
				150, 180, 180, 180, 180,
				180, 180,
				180, 180, 180
		};

		/*
		 * 본문 데이터 컬럼별 정렬 목록 ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left"
		 * (좌측 정렬) 로 고정 "left", "center", "right", "justify", "distributed" 가 아닌 다른 값
		 * 지정시 "left" (좌측 정렬) 로 지정됨
		 */

		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center",
				"center", "center", "center", "center", "center",
				"center", "center",
				"center", "center", "center"
		};

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록 ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야
		 * 함
		 */


		String[] propertyListOfFirstWorksheet = { //
				"ilItemCd", "itemBarcode", "goodsTpName", "ilGoodsId", "goodsNm",
				"warehouseNm", "poTpNm", "poScheduleDt", "recevingReqDt", "eventStartDt",
				"eventEndDt", "sellersNm", "pcsPerBox", "poEventQty", "orderCnt",
				"diffCntEx", "orderAvgCnt",
				"createInfo","createDt", "modifyDt"
		};
		// 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"마스터품목코드", "품목바코드", "상품유평", "상품코드", "상품명",
				"출고처", "발주유형", "발주예정일", "입고요청일", "행사시작일",
				"행사종료일", "판매처", "박스입수량", "행사발주수량", "주문수량",
				"차이수량", "일평균(30일)판매량",
				"관리자", "등록일", "수정일"
		};

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
		 * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능, 페이지네이션 처리하지 않음 excelData 를 세팅하지 않으면 샘플
		 * 엑셀로 다운로드됨
		 */
		List<ItemPoRequestVo> goodsPriceList = goodsItemPoRequestService.getPoRequestList(paramDto);
		firstWorkSheetDto.setExcelDataList(goodsPriceList);

		// excelDownloadDto 생성 후 workSheetDto 추가
		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
				.excelFileName(excelFileName) //
				.build();
		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}
}
