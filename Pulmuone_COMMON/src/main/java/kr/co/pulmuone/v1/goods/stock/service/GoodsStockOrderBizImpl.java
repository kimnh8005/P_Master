package kr.co.pulmuone.v1.goods.stock.service;

import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.DeliveryEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDeliveryType;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.StockCheckOrderDetailDto;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.StockEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockOrderResultVo;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsStockOrderBizImpl implements GoodsStockOrderBiz {

	@Autowired
	GoodsStockOrderService goodsStockOrderService;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	/**
	 * 주문 타입에 타입에 따른 재고수량 체크
	 * @param dto
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> validStockOrderHandle(StockOrderRequestDto dto) throws Exception {

		// 1. 주문이 가능한지 재고 확인
		for (StockOrderRequestDto vo : dto.getOrderList()) {

			if (!"Y".equals(vo.getOrderYn())) // 주문취소의 경우 재고 체크를 하지 않는다.
				continue;

			// 매장주문일 경우 재고 체크를 하지 않는다
			if ("Y".equals(vo.getStoreYn())) {
				continue;
			}

			StockOrderResultVo resultVo = goodsStockOrderService.getIlItemWarehouseIdInfo(vo);

			if (GoodsEnums.SaleType.RESERVATION.getCode().equals(resultVo.getSaleTp())) // 예약판매의 경우 잔여 재고 체크를 하지 않는다. MALL에서 주문시 확인함.
				continue;

			if ("N".equals(resultVo.getStockOrderYn()) && "Y".equals(resultVo.getUnlimitStockYn())) // 미연동 무제한 재고일 경우는 잔여 재고 체크를 하지 않는다.
				continue;

			if (resultVo.getAvailableOrderCnt() < vo.getOrderQty()) // 주문수량이 재고수량보다 많으면
				return ApiResult.result(StockEnums.StockQtyErrMsg.OUT_OF_STOCK);// 에러메시지 출력
		}

		return ApiResult.success();
	}

	/**
	 * 주문 타입에 타입에 따른 재고수량 계산
	 *
	 * @param dto
	 * @return ApiResult
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> stockOrderHandle(StockOrderRequestDto dto) throws Exception {

		long createId = 0;
		int rest = 0;

		// 1. 주문이 가능한지 재고 확인
		ApiResult<?> stockRes = this.validStockOrderHandle(dto);
		if (!BaseEnums.Default.SUCCESS.getCode().equals(stockRes.getCode())) {
			return stockRes;
		}

		// 2. 주문이 가능하면 재고 현행화
		for (StockOrderRequestDto vo : dto.getOrderList()) {

			StockOrderResultVo resultVo = goodsStockOrderService.getIlItemWarehouseIdInfo(vo);

// 예약판매의 경우 주문이 가능한지 재고확인은 하지 않지만 재고 차감은 진행하기위해 아래 코드 코멘트 처리.(HGRM-10201) S
//			if (GoodsEnums.SaleType.RESERVATION.getCode().equals(resultVo.getSaleTp())) // 예약판매의 경우 재고 재계산을 하지 않는다.
//				continue;
// 예약판매의 경우 주문이 가능한지 재고확인은 하지 않지만 재고 차감은 진행하기위해 아래 코드 코멘트 처리.(HGRM-10201) E

			// 매장주문일 경우 재고 재계산을 하지 않는다.
			if ("Y".equals(vo.getStoreYn())) {
				continue;
			}

			vo.setCreateId(createId);

			if ("Y".equals(resultVo.getStockOrderYn())) { // 연동 재고
				resultVo.setScheduleDt(vo.getScheduleDt());
				if ("GOODS_TYPE.DISPOSAL".equals(resultVo.getGoodsTp())) { // 폐기예정상품
					resultVo.setStockTp("ERP_STOCK_TP.DISCARD_ORDER");
				}
				else { // 폐기예정상품 이외
					resultVo.setStockTp("ERP_STOCK_TP.ORDER");
				}

				if ("Y".equals(vo.getOrderYn())) { // 주문생성
					resultVo.setOrderQty(vo.getOrderQty()); // 주문생성 수량
				}
				else { // 주문취소
					resultVo.setOrderQty(vo.getOrderQty() * -1); // 주문취소 수량
				}

				resultVo.setMemo(vo.getMemo()); // 기타정보(주문번호등)

				goodsStockOrderService.addErpStockOrder(resultVo);
				goodsStockOrderService.addErpStockOrderHistory(resultVo.getIlItemErpStockId());// 주문생성 수량 이력저장

				goodsStockOrderService.spItemStockCaculated(resultVo);// 재고수량 재계산 프로시저
			}
			else { // 미연동 재고
				if ("Y".equals(resultVo.getUnlimitStockYn())) {// 무제한
					continue; // 할일없음
				}
				else {
					if ("Y".equals(vo.getOrderYn())) {// 주문생성
						rest = resultVo.getAvailableOrderCnt() - vo.getOrderQty();// 재고수량-주문수량;
					}
					else {// 주문취소
						rest = resultVo.getAvailableOrderCnt() + vo.getOrderQty();// 재고수량+주문수량
					}

					resultVo.setOrderQty(rest);
					goodsStockOrderService.putNotIfStockCnt(resultVo);// 미연동 재고 수량 수정
				}
			}

		}

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> putOrderStock(Long odOrderId, String orderYn) throws Exception {
		List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<StockOrderRequestDto>();
		StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();

		List<StockCheckOrderDetailDto> orderGoodsList = orderOrderBiz.getStockCheckOrderDetailList(odOrderId);
		for (StockCheckOrderDetailDto goods : orderGoodsList) {

			if(StringUtils.isEmpty(StringUtil.nvl(goods.getShippingDt(), ""))){
				return ApiResult.fail();
			}
			StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
			stockOrderReqDto.setIlGoodsId(goods.getIlGoodsId());
			stockOrderReqDto.setOrderQty(goods.getOrderCnt());
			stockOrderReqDto.setScheduleDt(goods.getShippingDt().toString());
			stockOrderReqDto.setOrderYn(orderYn);
			stockOrderReqDto.setStoreYn(GoodsDeliveryType.SHOP.getCode().equals(goods.getGoodsDeliveryType()) ? "Y" : "N");
			stockOrderReqDto.setMemo(String.valueOf(goods.getOdOrderDetlId()));
			stockOrderReqDtoList.add(stockOrderReqDto);
		}
		stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
		ApiResult<?> stockRes = this.stockOrderHandle(stockOrderRequestDto);
		if (!stockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
			return ApiResult.result(DeliveryEnums.ChangeArriveDateValidation.LACK_STOCK);
		}

		return ApiResult.success();
	}

	@Override
	public ApiResult<?> putOrderStockByOdOrderDetlId(Long odOrderDetlId, String orderYn) throws Exception {
		List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<StockOrderRequestDto>();
		StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();

		List<StockCheckOrderDetailDto> orderGoodsList = orderOrderBiz.getStockCheckOrderDetailListByOdOrderDetlId(odOrderDetlId);
		for (StockCheckOrderDetailDto goods : orderGoodsList) {

			if(StringUtils.isEmpty(StringUtil.nvl(goods.getShippingDt(), ""))){
				return ApiResult.fail();
			}
			StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
			stockOrderReqDto.setIlGoodsId(goods.getIlGoodsId());
			stockOrderReqDto.setOrderQty(goods.getOrderCnt());
			stockOrderReqDto.setScheduleDt(goods.getShippingDt().toString());
			stockOrderReqDto.setOrderYn(orderYn);
			stockOrderReqDto.setStoreYn(GoodsDeliveryType.SHOP.getCode().equals(goods.getGoodsDeliveryType()) ? "Y" : "N");
			stockOrderReqDto.setMemo(String.valueOf(goods.getOdOrderDetlId()));
			stockOrderReqDtoList.add(stockOrderReqDto);
		}
		stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
		ApiResult<?> stockRes = this.stockOrderHandle(stockOrderRequestDto);
		if (!stockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
			return ApiResult.result(DeliveryEnums.ChangeArriveDateValidation.LACK_STOCK);
		}

		return ApiResult.success();
	}
}
