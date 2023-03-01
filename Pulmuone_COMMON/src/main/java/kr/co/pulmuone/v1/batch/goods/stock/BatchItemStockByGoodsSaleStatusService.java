package kr.co.pulmuone.v1.batch.goods.stock;

import kr.co.pulmuone.v1.batch.goods.stock.dto.BatchItemStockByGoodsSaleStatusReqeustDto;
import kr.co.pulmuone.v1.batch.system.job.SystemBatchJobBiz;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.stock.BatchItemStockByGoodsSaleStatusMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 품목 재고에 따른 상품 판매상태 변경
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 02. 23.               임상건         최초작성
* =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class BatchItemStockByGoodsSaleStatusService {

	@Autowired
	private BatchItemStockByGoodsSaleStatusMapper batchItemStockByGoodsSaleStatusMapper;
	
	private final SystemBatchJobBiz systemBatchJobBiz;


	//품목 재고 수량 0에 따른 상품 상태 품절(시스템) 변경
	protected void runItemStockByGoodsUpdateOutOfStockBySystem() throws BaseException {
		BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto = BatchItemStockByGoodsSaleStatusReqeustDto.builder()
				.baseDate(DateUtil.getCurrentDate())
				.goodsType(GoodsEnums.GoodsType.PACKAGE.getCode())
				.saleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode())
				.chgSaleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode())
				.comparisonOperator("=")
				.build();


		batchItemStockByGoodsSaleStatusReqeustDto.setSaleType(GoodsEnums.SaleType.RESERVATION.getCode());	//상품 판매상태가 예약판매인 경우에는 품절 처리 대상에서 제외함
		
		/**
		 * 전일마감재고가 성공적으로 끝났는지 확인하여 어제 날짜를 볼지, 오늘 날짜를 볼지 결정
		 */
		if(!(Boolean)systemBatchJobBiz.getBatchJobStockClosed().getData()) {
			batchItemStockByGoodsSaleStatusReqeustDto.setBaseDate(DateUtil.getAddDayOfDate(DateUtil.getCurrentDate(), -1));
		}
		
		
		/******************************************************************* 재고 연동 상품 대상 시작!!!!! ****************************************************/
		int erpGoodsNum = 0;			//재고 연동 update 대상 갯수
		int updateErpGoodsNum = 0;		//UPDATE 갯수

		//묶음 상품 제외
		erpGoodsNum = itemErpStockByGoodsSaleStatusChangeNum(batchItemStockByGoodsSaleStatusReqeustDto);

		if(erpGoodsNum > 0) {
			updateErpGoodsNum = itemErpStockByGoodsSaleStatusChangeUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
		}

		if(erpGoodsNum > 0 && erpGoodsNum != updateErpGoodsNum) {	//재고 연동 대상이 존재하고 연동 대상의 갯수와 UPDATE갯수가 맞지 않다면
			throw new BaseException("연동 대상의 갯수와 UPDATE 갯수가 맞지 않습니다. 확인해 주세요.");
		}

		erpGoodsNum = 0;			//재고 연동 update 대상 갯수 초기화
		updateErpGoodsNum = 0;		//UPDATE 갯수 초기화
		//묶음 상품 대상
		erpGoodsNum = itemErpStockByGoodsPackageSaleStatusOutOfStockBySystemNum(batchItemStockByGoodsSaleStatusReqeustDto);

		if(erpGoodsNum > 0) {
			updateErpGoodsNum = itemErpStockByGoodsPackageSaleStatusOutOfStockBySystemUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
		}

		if(erpGoodsNum > 0 && erpGoodsNum != updateErpGoodsNum) {	//재고 연동 대상이 존재하고 연동 대상의 갯수와 UPDATE갯수가 맞지 않다면
			throw new BaseException("연동 대상의 갯수와 UPDATE 갯수가 맞지 않습니다. 확인해 주세요.");
		}
		/******************************************************************* 재고 연동 상품 대상 끝!!!!! ****************************************************/



		/******************************************************************* 재고 미연동 상품 대상 시작!!!!! ****************************************************/
		//묶음 상품 제외
		int goodsNum = 0;			//재고 미연동 update 대상 갯수
		int updateGoodsNum = 0;		//UPDATE 갯수

		goodsNum = itemStockByGoodsSaleStatusChangeNum(batchItemStockByGoodsSaleStatusReqeustDto);

		if(goodsNum > 0) {
			updateGoodsNum = itemStockByGoodsSaleStatusChangeUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
		}

		if(goodsNum > 0 && goodsNum != updateGoodsNum) {	//재고 미연동 대상이 존재하고 연동 대상의 갯수와 UPDATE갯수가 맞지 않다면
			throw new BaseException("연동 대상의 갯수와 UPDATE 갯수가 맞지 않습니다. 확인해 주세요.");
		}

		//묶음 상품 대상
		goodsNum = 0;			//재고 미연동 update 대상 갯수 초기화
		updateGoodsNum = 0;		//UPDATE 갯수 초기화

		goodsNum = itemStockByGoodsPackageSaleStatusOutOfStockBySystemNum(batchItemStockByGoodsSaleStatusReqeustDto);

		if(goodsNum > 0) {
			updateGoodsNum = itemStockByGoodsPackageSaleStatusOutOfStockBySystemUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
		}

		if(goodsNum > 0 && goodsNum != updateGoodsNum) {	//재고 미연동 대상이 존재하고 연동 대상의 갯수와 UPDATE갯수가 맞지 않다면
			throw new BaseException("연동 대상의 갯수와 UPDATE 갯수가 맞지 않습니다. 확인해 주세요.");
		}
		/******************************************************************* 재고 미연동 상품 대상 끝!!!!! ****************************************************/
	}

	//품목 재고 수량이 0보다 크면  상품 상태 판매중 변경
	protected void runItemStockByGoodsUpdateOnSale() throws BaseException {
		BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto = BatchItemStockByGoodsSaleStatusReqeustDto.builder()
				.baseDate(DateUtil.getCurrentDate())
				.goodsType(GoodsEnums.GoodsType.PACKAGE.getCode())
				.saleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode())
				.chgSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode())
				.comparisonOperator(">")
				.build();

		/******************************************************************* 재고 연동 상품 대상 시작!!!!! ****************************************************/
		Integer erpGoodsNum = null;			//재고 연동 update 대상 갯수
		int updateErpGoodsNum = 0;		//UPDATE 갯수

		//묶음 상품 제외
		erpGoodsNum = itemErpStockByGoodsSaleStatusChangeNum(batchItemStockByGoodsSaleStatusReqeustDto);

		if(erpGoodsNum != null && erpGoodsNum > 0) {
			updateErpGoodsNum = itemErpStockByGoodsSaleStatusChangeUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
		}

		if(erpGoodsNum > 0 && erpGoodsNum != updateErpGoodsNum) {	//재고 연동 대상이 존재하고 연동 대상의 갯수와 UPDATE갯수가 맞지 않다면
			throw new BaseException("연동 대상의 갯수와 UPDATE 갯수가 맞지 않습니다. 확인해 주세요.");
		}

		erpGoodsNum = null;			//재고 연동 UPDATE 대상 갯수 초기화
		updateErpGoodsNum = 0;		//UPDATE 갯수 초기화
		//묶음 상품 대상
		erpGoodsNum = itemErpStockByGoodsPackageSaleStatusOnSaleNum(batchItemStockByGoodsSaleStatusReqeustDto);

		if(erpGoodsNum != null && erpGoodsNum > 0) {
			updateErpGoodsNum = itemErpStockByGoodsPackageSaleStatusOnSaleUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
		}

		if(erpGoodsNum != null && erpGoodsNum > 0 && erpGoodsNum != updateErpGoodsNum) {	//재고 연동 대상이 존재하고 연동 대상의 갯수와 UPDATE갯수가 맞지 않다면
			throw new BaseException("연동 대상의 갯수와 UPDATE 갯수가 맞지 않습니다. 확인해 주세요.");
		}
		/******************************************************************* 재고 연동 상품 대상 끝!!!!! ****************************************************/



		/******************************************************************* 재고 미연동 상품 대상 시작!!!!! ****************************************************/
		//묶음 상품 제외
		Integer goodsNum = null;			//재고 미연동 update 대상 갯수
		int updateGoodsNum = 0;		//UPDATE 갯수

		goodsNum = itemStockByGoodsSaleStatusChangeNum(batchItemStockByGoodsSaleStatusReqeustDto);

		if(goodsNum != null && goodsNum > 0) {
			updateGoodsNum = itemStockByGoodsSaleStatusChangeUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
		}

		if(goodsNum > 0 && goodsNum != updateGoodsNum) {	//재고 미연동 대상이 존재하고 연동 대상의 갯수와 UPDATE갯수가 맞지 않다면
			throw new BaseException("연동 대상의 갯수와 UPDATE 갯수가 맞지 않습니다. 확인해 주세요.");
		}

		goodsNum = null;
		updateGoodsNum = 0;
		//묶음 상품 대상
		goodsNum = itemStockByGoodsPackageSaleStatusOnSaleNum(batchItemStockByGoodsSaleStatusReqeustDto);

		if(goodsNum != null && goodsNum > 0) {
			updateGoodsNum = itemStockByGoodsPackageSaleStatusOnSaleUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
		}

		if(goodsNum != null && goodsNum > 0 && goodsNum != updateGoodsNum) {	//재고 연동 대상이 존재하고 연동 대상의 갯수와 UPDATE갯수가 맞지 않다면
			throw new BaseException("연동 대상의 갯수와 UPDATE 갯수가 맞지 않습니다. 확인해 주세요.");
		}
		/******************************************************************* 재고 미연동 상품 대상 끝!!!!! ****************************************************/
	}
	
	//묶음상품의 구성품중에 하나라도 '판매중' 상태가 아니라면 묶음상품도 '판매중지(시스템)' 처리
	protected void runGoodsSaleStatusByGoodsPackageSaleStatusUpdate() throws BaseException {
		BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto = BatchItemStockByGoodsSaleStatusReqeustDto.builder()
				.saleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode())
				.chgSaleStatus(GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode())
				.build();
		
		Integer goodsNum = null;			//묶음상품의 구성품중에 하나라도 '판매중' 상태가 아닌 묶음상품 갯수
		int updateGoodsPackageNum = 0;		//UPDATE 갯수
		
		goodsNum = goodsSaleStatusByGoodsPackageSaleStatusNum(batchItemStockByGoodsSaleStatusReqeustDto);
		
		if(goodsNum != null && goodsNum > 0) {
			updateGoodsPackageNum = goodsSaleStatusByGoodsPackageSaleStatusUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
		}
		
		if(goodsNum > 0 && goodsNum != updateGoodsPackageNum) {	//묶음상품 구성품중에 하나라도 '판매중' 상태가 아닌 묶음상품이 존재하면
			throw new BaseException("연동 대상의 갯수와 UPDATE 갯수가 맞지 않습니다. 확인해 주세요.");
		}
	}

	/**
	 * 묶음상품 제외
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 0 인 상품 ID 갯수
	 * @Param String baseDate : 기준일, String goodsType : 상품상태, String saleStatus : 판매상태, String chgSaleStatus : 변경할 판매상태
	 */
	protected int itemErpStockByGoodsSaleStatusChangeNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemErpStockByGoodsSaleStatusChangeNum(batchItemStockByGoodsSaleStatusReqeustDto);
	}

	/**
	 * 묶음상품 제외
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 0 인 상품
	 * 판매 상태 변경
	 */
	protected int itemErpStockByGoodsSaleStatusChangeUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemErpStockByGoodsSaleStatusChangeUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
	}


	/**
	 * 묶음상품 대상
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 0 인 상품 ID 갯수
	 * 판매 상태 : SALE_STATUS.ON_SALE(판매중), 재고 수량 0인 상태의 갯수만 확인
	 * @Param String baseDate : 기준일, String goodsType : 상품상태, String saleStatus : 판매상태, String chgSaleStatus : 변경할 판매상태
	 */
	protected int itemErpStockByGoodsPackageSaleStatusOutOfStockBySystemNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemErpStockByGoodsPackageSaleStatusOutOfStockBySystemNum(batchItemStockByGoodsSaleStatusReqeustDto);
	}
	/**
	 * 묶음상품 대상
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 0 인 상품
	 * 판매 상태 : SALE_STATUS.ON_SALE(판매중), 재고 수량 0인 상태만 체크해서 판매 상태 SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM(품절(시스템))으로 변경
	 */
	protected int itemErpStockByGoodsPackageSaleStatusOutOfStockBySystemUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemErpStockByGoodsPackageSaleStatusOutOfStockBySystemUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
	}
	/**
	 * 묶음상품 대상
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 1 이상인 상품 갯수
	 */
	protected Integer itemErpStockByGoodsPackageSaleStatusOnSaleNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemErpStockByGoodsPackageSaleStatusOnSaleNum(batchItemStockByGoodsSaleStatusReqeustDto);
	}
	/**
	 * 묶음상품 대상
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 1 이상인 상품 판매 상태 변경
	 * 묶음상품의 모든 구성품(증정품 포함)의 수량 상태를 보고 모두 다 1 이상이어야 판매중으로 변경 처리
	 */
	protected int itemErpStockByGoodsPackageSaleStatusOnSaleUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemErpStockByGoodsPackageSaleStatusOnSaleUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
	}


	/**
	 * 묶음상품 제외
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 ID 갯수 확인
	 * @Param String baseDate : 기준일, String goodsType : 상품상태, String saleStatus : 판매상태, String chgSaleStatus : 변경할 판매상태
	 */
	protected int itemStockByGoodsSaleStatusChangeNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemStockByGoodsSaleStatusChangeNum(batchItemStockByGoodsSaleStatusReqeustDto);
	}

	/**
	 * 묶음상품 제외
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 판매상태 변경
	 * 판매 상태 변경
	 */
	protected int itemStockByGoodsSaleStatusChangeUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemStockByGoodsSaleStatusChangeUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
	}

	/**
	 * 묶음상품 대상
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 ID 갯수 확인
	 * 판매 상태 : SALE_STATUS.ON_SALE(판매중), 재고 수량 0인 상태의 갯수만 확인
	 * @Param String baseDate : 기준일, String goodsType : 상품상태, String saleStatus : 판매상태, String chgSaleStatus : 변경할 판매상태
	 */
	protected int itemStockByGoodsPackageSaleStatusOutOfStockBySystemNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemStockByGoodsPackageSaleStatusOutOfStockBySystemNum(batchItemStockByGoodsSaleStatusReqeustDto);
	}
	/**
	 * 묶음상품 대상
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 판매상태 변경
	 * 판매 상태 : SALE_STATUS.ON_SALE(판매중), 재고 수량 0인 상태만 체크해서 판매 상태 SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM(품절(시스템))으로 변경
	 * 판매 상태 변경
	 */
	protected int itemStockByGoodsPackageSaleStatusOutOfStockBySystemUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemStockByGoodsPackageSaleStatusOutOfStockBySystemUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
	}
	/**
	 * 묶음상품 대상
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 ID 갯수 확인
	 */
	protected Integer itemStockByGoodsPackageSaleStatusOnSaleNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemStockByGoodsPackageSaleStatusOnSaleNum(batchItemStockByGoodsSaleStatusReqeustDto);
	}
	/**
	 * 묶음상품 대상
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 1이상인 상품 판매상태 변경
	 * 묶음상품의 모든 구성품(증정품 포함)의 수량 상태를 보고 모두 다 1 이상이어야 판매중으로 변경 처리
	 */
	protected int itemStockByGoodsPackageSaleStatusOnSaleUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.itemStockByGoodsPackageSaleStatusOnSaleUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
	}
	
	/**
	 * 묶음상품 대상
	 * 묶음상품의 구성품중에 하나라도 '판매중' 상태가 아닌 묶음상품 갯수
	 */
	protected Integer goodsSaleStatusByGoodsPackageSaleStatusNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.goodsSaleStatusByGoodsPackageSaleStatusNum(batchItemStockByGoodsSaleStatusReqeustDto);
	}
	/**
	 * 묶음상품 대상
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 1이상인 상품 판매상태 변경
	 * 묶음상품의 구성품중에 하나라도 '판매중' 상태가 아니라면 묶음상품도 '판매중지(시스템)' 처리
	 */
	protected int goodsSaleStatusByGoodsPackageSaleStatusUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto) {
		return batchItemStockByGoodsSaleStatusMapper.goodsSaleStatusByGoodsPackageSaleStatusUpdate(batchItemStockByGoodsSaleStatusReqeustDto);
	}
}
