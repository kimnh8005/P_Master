package kr.co.pulmuone.v1.goods.stock.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.StockEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockListResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.ItemErpStockCommonVo;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockListResultVo;

/**
 * <PRE>
 * Forbiz Korea
 * 품목별 재고리스트 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20201104    이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class GoodsStockListBizImpl implements GoodsStockListBiz {

	@Autowired
    GoodsStockListService goodsStockListService;

	@Autowired
    GoodsStockCommonBiz goodsStockCommonBiz;

	/**
	 * 품목별 재고리스트 조회
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockList(StockListRequestDto dto) throws Exception {
		StockListResponseDto stockListResponseDto = new StockListResponseDto();

		Page<StockListResultVo> stockDeadlineList = goodsStockListService.getStockList(dto);

		stockListResponseDto.setTotal(stockDeadlineList.getTotal());
		stockListResponseDto.setRows(stockDeadlineList.getResult());

        return ApiResult.success(stockListResponseDto);

	}

	/**
	 * 품목별 재고리스트  주문정보 조회
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockInfo(StockListRequestDto stockListRequestDto) throws Exception {
		StockListResponseDto stockListResponseDto = new StockListResponseDto();

		StockListResultVo stockListResultVo;
		if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(stockListRequestDto.getGoodsType())) stockListResultVo = goodsStockListService.getPackageGoodsStockInfo(stockListRequestDto);
		else stockListResultVo = goodsStockListService.getStockInfo(stockListRequestDto); // rows

		stockListResponseDto.setStockListResultVo(stockListResultVo);

		return ApiResult.success(stockListResponseDto);
	}

	/**
	 * 품목별 재고상세 목록 조회
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockDetailList(StockListRequestDto stockListRequestDto) throws Exception {
		StockListResponseDto stockListResponseDto = new StockListResponseDto();

		Page<StockListResultVo> stockDetlList = goodsStockListService.getStockDetailList(stockListRequestDto);

		String restDay = "";

		for(StockListResultVo vo : stockDetlList) {
			int restPeriod = Integer.parseInt(vo.getRestDistributionDday());//잔여 유통기간 D-day계산:출고기준-잔여기간

			if(restPeriod >= 0) {
				restDay = vo.getRestDistributionPeriod()+"(D+"+restPeriod+")";
				vo.setRestDistributionPeriod(restDay);
			}else {
				restDay = vo.getRestDistributionPeriod()+"(D"+restPeriod+")";
				vo.setRestDistributionPeriod(restDay);
			}
		}

		stockListResponseDto.setTotal(stockDetlList.getTotal());
		stockListResponseDto.setRows(stockDetlList.getResult());

        return ApiResult.success(stockListResponseDto);

	}

	/**
	 * 공통 선주문 설정 팝업 정보 조회
	 * @param StockListRequestDto
	 * @return int
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> getStockPreOrderPopupInfo(StockListRequestDto stockListRequestDto) throws Exception {
		return ApiResult.success(goodsStockListService.getStockPreOrderPopupInfo(stockListRequestDto));
	}

	/**
	 * 폼목 재고리스트 선주문 여부 수정
	 * @param StockListRequestDto
	 * @return int
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putStockPreOrder(StockListRequestDto stockListRequestDto) throws Exception {

		goodsStockListService.putStockPreOrder(stockListRequestDto);

		return ApiResult.success();
	}

	/**
	 * 품목별 재고리스트 엑셀 다운로드
	 * @param	StockListRequestDto
	 * @return	List<StockListResultVo>
	 */
	@Override
	public ExcelDownloadDto getStockListExportExcel(StockListRequestDto dto) {
		 String excelFileName = "item_stockList"; // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
		 String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

	        /*
	         * 컬럼별 width 목록 : 단위 pixel
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
	         */
	        Integer[] widthListOfFirstWorksheet = { //
	                150, 150, 150, 150, 170, 350, 150, 250, 150, 200, 150, 180, 180, 180, 250};

	        /*
	         * 본문 데이터 컬럼별 정렬 목록
	         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
	         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
	         */
	        String[] alignListOfFirstWorksheet = { //
	        		"center", "center", "center", "center", "center", "left","center","center","center","center","center","center","center","center","center"};

	        /*
	         * 본문 데이터 컬럼별 데이터 property 목록
	         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
	         */
	        String[] propertyListOfFirstWorksheet = { //
	                "ilItemCd", "barcode", "goodsTpNm", "ilGoodsId", "saleStatusNm", "itemNm", "supplierNm", "ctgryStdNm", "storageMethodTpNm", "warehouseNm", "preOrderYn", "stockClosingCount", "stockConfirmedCount", "stockScheduledCount", "wmsCount"};

	        // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
	        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
	                "품목코드", "품목바코드", "상품유형", "상품코드", "상품판매상태", "마스터품목명", "공급업체", "표준카테고리(대분류)", "보관방법", "출고처", "선주문여부", "전일마감재고", "당일입고확정", "당일입고예정", "오프라인물류재고량"};

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
	        List<StockListResultVo> orgaDisList = goodsStockListService.getStockList(dto);

	        firstWorkSheetDto.setExcelDataList(orgaDisList);

	        // excelDownloadDto 생성 후 workSheetDto 추가
	        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
	                .excelFileName(excelFileName) //
	                .build();

	        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

	        return excelDownloadDto;
	}

	/**
	 * 출고기준관리 DorpDownList 조회
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getStockDeadlineDropDownList(StockListRequestDto dto) throws Exception {
		StockListResponseDto stockListResponseDto = new StockListResponseDto();

		List<StockListResultVo> rows = goodsStockListService.getStockDeadlineDropDownList(dto);

		stockListResponseDto.setRows(rows);

        return ApiResult.success(stockListResponseDto);

	}


	/**
	 * 출고기준관리 수정
	 * @param	StockListRequestDto
	 * @return	StockListResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putStockDeadlineInfo(StockListRequestDto stockListRequestDto) throws Exception {

		goodsStockListService.putStockDeadlineInfoBasicYn(stockListRequestDto);

		goodsStockListService.putStockDeadlineInfo(stockListRequestDto);

		return ApiResult.success();
	}

	/**
	 * 재고조정수량 저장/수정
	 * @param	StockListRequestDto
	 * @return	int
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putStockAdjustCount(StockListRequestDto stockListRequestDto) throws Exception {

		ItemErpStockCommonVo itemErpStockCommonVo = new ItemErpStockCommonVo();
		itemErpStockCommonVo.setIlItemWarehouseId(stockListRequestDto.getIlItemWarehouseId());
		itemErpStockCommonVo.setBaseDt(stockListRequestDto.getBaseDt());
		itemErpStockCommonVo.setCreateId(Long.valueOf(SessionUtil.getBosUserVO().getUserId()));
		itemErpStockCommonVo.setModifyId(Long.valueOf(SessionUtil.getBosUserVO().getUserId()));

		// 전일마감재고 조정수량
		itemErpStockCommonVo.setIlItemErpStockId(0); // 초기화
		itemErpStockCommonVo.setStockTp(StockEnums.ErpStockType.ERP_STOCK_CLOSED_ADJ.getCode());
		itemErpStockCommonVo.setStockQty(stockListRequestDto.getD0StockClosedAdjQty());
		goodsStockCommonBiz.processMergeErpStock(itemErpStockCommonVo, false);

		// 입고예정 조정수량
		itemErpStockCommonVo.setIlItemErpStockId(0); // 초기화
		itemErpStockCommonVo.setStockTp(StockEnums.ErpStockType.ERP_STOCK_SCHEDULED_ADJ.getCode());
		itemErpStockCommonVo.setStockQty(stockListRequestDto.getD0StockScheduledAdjQty());
		goodsStockCommonBiz.processMergeErpStock(itemErpStockCommonVo, false);

		//입고확정 조정수량
		itemErpStockCommonVo.setIlItemErpStockId(0); // 초기화
		itemErpStockCommonVo.setStockTp(StockEnums.ErpStockType.ERP_STOCK_CONFIRMED_ADJ.getCode());
		itemErpStockCommonVo.setStockQty(stockListRequestDto.getD0StockConfirmedAdjQty());
		goodsStockCommonBiz.processMergeErpStock(itemErpStockCommonVo, true);

		return ApiResult.success();
	}

}
