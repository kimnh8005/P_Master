package kr.co.pulmuone.v1.batch.order.inside;

import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsTrackingResponseDto;
import kr.co.pulmuone.v1.api.cjlogistics.dto.vo.CJLogisticsTrackingVo;
import kr.co.pulmuone.v1.api.cjlogistics.service.CJLogisticsBiz;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisTrackingResponseDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.vo.LotteGlogisTrackingVo;
import kr.co.pulmuone.v1.api.lotteglogis.service.LotteGlogisBiz;
import kr.co.pulmuone.v1.batch.order.etc.TrackingNumberErpBiz;
import kr.co.pulmuone.v1.batch.order.etc.dto.TrackingNumberOrderInfoDto;
import kr.co.pulmuone.v1.batch.order.etc.dto.vo.TrackingNumberDetailVo;
import kr.co.pulmuone.v1.batch.order.inside.dto.vo.OrderStatusInfoVo;
import kr.co.pulmuone.v1.batch.order.inside.dto.vo.TrackingShippingCompVo;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ApiEnums.CJLogisticsTrackingStatus;
import kr.co.pulmuone.v1.comm.enums.ApiEnums.LotteGlogisTrackingStatus;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.inside.DeliveryCompleteMapper;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.order.OrderErpMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <PRE>
 * Forbiz Korea
 * 배송완료 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveryCompleteService {

	private final DeliveryCompleteMapper deliveryCompleteMapper;				// 배송완료 배치 Mapper

	private final OrderErpMapper orderErpMapper;								// 주문 API 배치 Mapper

	@Autowired
	private final CJLogisticsBiz cjLogisticsBiz;								// CJ택배 Biz

	@Autowired
	private final LotteGlogisBiz lotteGlogisBiz;								// 롯데택배 Biz

	@Autowired
	private TrackingNumberErpBiz trackingNumberErpBiz;

	private Long cjId, lotteId; // CJ, LOTTE 택배사ID

	/**
     * @Desc 배송완료 대상 리스트 조회
     * @return List<OrderStatusInfoVo>
     * @throws BaseException
     */
	protected List<OrderStatusInfoVo> getDeliveryCompleteList() throws BaseException {
		// 자동 배송완료 설정기간 조회
		int deliveryCompleteDay = deliveryCompleteMapper.getDeliveryCompleteDay(BatchConstants.DELIVERY_COMPLETE_DAY_KEY);
		return deliveryCompleteDay >= 1 ? deliveryCompleteMapper.getDeliveryCompleteList(OrderEnums.OrderStatus.DELIVERY_ING.getCode(), deliveryCompleteDay) : null;
    }

	/**
	 * 매장배송 배송완료 배치
	 * @throws BaseException
	 */
	protected void putStoreDeliveryCompleteList() throws BaseException {

		String inOrderStatusCd = OrderEnums.OrderStatus.DELIVERY_ING.getCode();

		// 매장배송 배송완료 대상조회
		List<OrderStatusInfoVo> orderStatusInfoList = deliveryCompleteMapper.gutStoreDeliveryCompleteList(inOrderStatusCd);

		putDeliveryCompleteList(orderStatusInfoList);
	}

	/**
     * @Desc 배송완료 대상 리스트(일일배송:녹즙, 베이비밀, 잇슬림) 조회
     * @return List<OrderStatusInfoVo>
     * @throws BaseException
     */
	protected List<OrderStatusInfoVo> getDeliveryCompleteDailyList() throws BaseException {
		return deliveryCompleteMapper.getDeliveryCompleteList(OrderEnums.OrderStatus.DELIVERY_ING.getCode(), null);
    }

	/**
     * @Desc 배송완료 트래킹 대상 리스트(CJ/롯데) 조회
     * @return List<OrderStatusInfoVo>
     * @throws BaseException
     */
	protected List<OrderStatusInfoVo> getDeliveryCompleteTrackingList() throws BaseException {
		return deliveryCompleteMapper.getDeliveryCompleteTrackingList(OrderEnums.OrderStatus.DELIVERY_ING.getCode());
	}

	/**
	 * @Desc 배송완료 트래킹 대상 리스트(CJ/롯데) API 통신
	 * @return List<OrderStatusInfoVo>
	 * @throws BaseException
	 */
	protected List<OrderStatusInfoVo> apiDeliveryCompleteTrackingList(List<OrderStatusInfoVo> deliveryCompleteTrackingList) throws BaseException {
		if (deliveryCompleteTrackingList == null || deliveryCompleteTrackingList.isEmpty()) return null;

		// 트래킹 택배사(CJ/롯데) 정보 조회
		List<TrackingShippingCompVo> trackingListShippingCompList = deliveryCompleteMapper.getTrackingListShippingCompList();
		if (trackingListShippingCompList == null || trackingListShippingCompList.isEmpty()) return null;

		// 트래킹 택배사(CJ/롯데) ID 셋팅
		for (TrackingShippingCompVo trackingShippingCompVo : trackingListShippingCompList) {
			if (OrderEnums.LogisticsCd.CJ.getLogisticsCode().equals(StringUtil.nvl(trackingShippingCompVo.getPsShippingCompVal()))){
				cjId 	= trackingShippingCompVo.getPsShippingCompId();
			}
			if (OrderEnums.LogisticsCd.LOTTE.getLogisticsCode().equals(StringUtil.nvl(trackingShippingCompVo.getPsShippingCompVal()))){
				lotteId 	= trackingShippingCompVo.getPsShippingCompId();
			}
		}

		// 배송완료 저장 List
		List<OrderStatusInfoVo> saveDeliveryCompleteList = new ArrayList<>();

		// 각 택배사별 트래킹 조회 - API 통신오류시 제외처리
		for (OrderStatusInfoVo orderStatusInfoVo : deliveryCompleteTrackingList) {
			try {
				if (StringUtil.nvlLong(cjId) == orderStatusInfoVo.getPsShippingCompId()) {
					saveDeliveryCompleteList.add(getCjTrackingInfo(orderStatusInfoVo));
				}
				if (StringUtil.nvlLong(lotteId) == orderStatusInfoVo.getPsShippingCompId()) {
					saveDeliveryCompleteList.add(getLotteTrackingInfo(orderStatusInfoVo));
				}
			} catch (Exception e) {
				log.info("API Error : " + e);
				log.info("orderStatusInfoVo : " + orderStatusInfoVo);
			}
		}

		// Null 데이터 제거
		saveDeliveryCompleteList.removeIf(Objects::isNull);
		return saveDeliveryCompleteList;
	}

	/**
     * @Desc CJ택배 트래킹 정보 조회
     * @param orderStatusInfoVo(송장정보)
     * @return orderStatusInfoVo(변경할 송장 정보)
	 */
	private OrderStatusInfoVo getCjTrackingInfo(OrderStatusInfoVo orderStatusInfoVo) {
		// CJ택배 트래킹 정보 조회
		CJLogisticsTrackingResponseDto cjTrackingInfo = cjLogisticsBiz.getCJLogisticsTrackingList(orderStatusInfoVo.getTrackingNo());

		// 트래킹 정보 Empty
		if (cjTrackingInfo == null || cjTrackingInfo.getMaster() == null || cjTrackingInfo.getMaster().getTracking() == null || cjTrackingInfo.getMaster().getTracking().size() < 1) return null;

		// 트래킹 정보 배송완료 여부 체크
		List<CJLogisticsTrackingVo> cjTrackingList = cjTrackingInfo.getMaster().getTracking();
		int count = (int) cjTrackingList.stream().filter(vo -> CJLogisticsTrackingStatus.DELIVERY_COMPLETED.getMessage().equals(vo.getScanTypeName())).count();
		if (count == 0) return null;

		return orderStatusInfoVo;
	}

	/**
     * @Desc 롯데택배 트래킹 정보 조회
     * @param orderStatusInfoVo(송장정보)
     * @return orderStatusInfoVo(변경할 송장 정보)
	 */
	private OrderStatusInfoVo getLotteTrackingInfo(OrderStatusInfoVo orderStatusInfoVo) {
		// 롯데택배 트래킹 정보 조회
		LotteGlogisTrackingResponseDto lotteTrackingInfo = lotteGlogisBiz.getLotteGlogisTrackingList(orderStatusInfoVo.getTrackingNo(), OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());

		// 트래킹 정보 Empty
		if (lotteTrackingInfo == null || lotteTrackingInfo.getTracking() == null || lotteTrackingInfo.getTracking().size() < 1) return null;

		// 트래킹 정보 배송완료 여부 체크 (배달완료, 인수증등록)
		List<LotteGlogisTrackingVo> lotteTrackingList = lotteTrackingInfo.getTracking();
		int count = (int) lotteTrackingList.stream().filter(vo -> LotteGlogisTrackingStatus.DELIVERY_COMPLETED.getCode().equals(vo.getTrackingStatusCode())
							|| LotteGlogisTrackingStatus.CONSIGNEE_REGISTRATION.getCode().equals(vo.getTrackingStatusCode())).count();
		if (count == 0) return null;

		return orderStatusInfoVo;
	}

	/**
	 * @Desc 배송완료대상 리스트 배송완료로 변경 - 트랜잭션
	 * @param deliveryCompleteList
	 * @return
	 * @throws BaseException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void putDeliveryCompleteListTransaction(List<OrderStatusInfoVo> deliveryCompleteList) throws BaseException {
		if (deliveryCompleteList == null || deliveryCompleteList.isEmpty()) return;

		deliveryCompleteList.forEach(orderStatusInfoVo -> {
			orderStatusInfoVo.setOrderStatusCd(OrderEnums.OrderStatus.DELIVERY_COMPLETE.getCode());	// 주문상태(배송완료)
			orderStatusInfoVo.setCreateId(Constants.BATCH_CREATE_USER_ID);							// 등록자(Batch)
			saveDeliveryCompleteInfo(orderStatusInfoVo);
		});
	}
	
	/**
     * @Desc 배송완료대상 리스트 배송완료로 변경
     * @param deliveryCompleteList
     * @return
     * @throws BaseException
     */
	protected void putDeliveryCompleteList(List<OrderStatusInfoVo> deliveryCompleteList) throws BaseException {
		if (deliveryCompleteList == null || deliveryCompleteList.isEmpty()) return;

        deliveryCompleteList.forEach(orderStatusInfoVo -> {
            orderStatusInfoVo.setOrderStatusCd(OrderEnums.OrderStatus.DELIVERY_COMPLETE.getCode());	// 주문상태(배송완료)
            orderStatusInfoVo.setCreateId(Constants.BATCH_CREATE_USER_ID);							// 등록자(Batch)
            saveDeliveryCompleteInfo(orderStatusInfoVo);
        });
	}

	/**
  	 * @Desc  배송완료 개별저장 (트랜잭션 단위) 배송중 -> 배송완료
  	 * @param orderStatusInfoVo
  	 * @return
  	 */
	protected void saveDeliveryCompleteInfo(OrderStatusInfoVo orderStatusInfoVo) {
		try {
			deliveryCompleteMapper.putDeliveryCompleteInfo(orderStatusInfoVo);
			OrderEnums.OrderDetailStatusHistMsg orderDetailStatusHistMsg = OrderEnums.OrderDetailStatusHistMsg.findByCode(orderStatusInfoVo.getOrderStatusCd());

			OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
					.odOrderDetlId(orderStatusInfoVo.getOdOrderDetlId())
					.statusCd(OrderEnums.OrderStatus.DELIVERY_ING.getCode())
					.histMsg(MessageFormat.format(orderDetailStatusHistMsg.getMessage(), "배치", orderStatusInfoVo.getOdOrderDetlId()))
					.createId(orderStatusInfoVo.getCreateId())
					.build();
			orderErpMapper.addOrderDetailStatusHist(orderDetlHistVo);
		} catch (Exception e) {
			// TODO : SMS 개발 추가
			e.printStackTrace();
			log.error(e.getMessage());
		}
  	}

	/**
	 * 하이톡(녹즙) 스케줄 배송완료 저장
	 * @throws BaseException
	 */
	protected void putHitokScheduleDeliveryComplete() throws BaseException {
		deliveryCompleteMapper.putHitokScheduleDeliveryComplete();
	}

	/**
	 * 잇슬림 일일배송 배송중 배치
	 * @throws BaseException
	 */
	protected void putEatsslimDailyDeliveryIng() throws BaseException {

		String putOrderStatusCd = OrderEnums.OrderStatus.DELIVERY_ING.getCode();
		long createId = Constants.BATCH_CREATE_USER_ID;
		String urWarehouseId = ErpApiEnums.UrWarehouseId.EATSSLIM_D3_FRANCHISEE.getCode();
		String inOrderStatusCd = OrderEnums.OrderStatus.DELIVERY_READY.getCode();

		// 잇슬림 일일배송 배송중 대상조회
		List<OrderStatusInfoVo> orderStatusInfoList = deliveryCompleteMapper.getEatsslimDailyDeliveryIngList(urWarehouseId, inOrderStatusCd);

		for(OrderStatusInfoVo orderStatusInfoVo : orderStatusInfoList){
			orderStatusInfoVo.setCreateId(createId);
			orderStatusInfoVo.setOrderStatusCd(putOrderStatusCd);
			deliveryCompleteMapper.putEatsslimDailyDeliveryIng(orderStatusInfoVo);

			// 직배송 택배로 오늘날짜 송장번호로 셋팅
			TrackingNumberDetailVo trackingNumberDetailVo = new TrackingNumberDetailVo();
			trackingNumberDetailVo.setPsShippingCompId(BatchConstants.ETC);
			trackingNumberDetailVo.setTrackingNumber(DateUtil.getCurrentDate("yyyyMMdd"));
			trackingNumberDetailVo.setOdOrderDetlId(StringUtil.nvl(orderStatusInfoVo.getOdOrderDetlId()));
			trackingNumberDetailVo.setOdid(orderStatusInfoVo.getOdid());
			trackingNumberDetailVo.setOdOrderDetlSeq(StringUtil.nvl(orderStatusInfoVo.getOdOrderDetlSeq()));

			// 기존 송장정보 삭제
			orderErpMapper.delOrderTrackingNumber(trackingNumberDetailVo);
			// 신규 송장정보 등록
			orderErpMapper.addOrderTrackingNumber(trackingNumberDetailVo);

			// 이력등록
			OrderEnums.OrderDetailStatusHistMsg orderDetailStatusHistMsg = OrderEnums.OrderDetailStatusHistMsg.findByCode(OrderEnums.OrderStatus.STORE_DELIVERY.getCode());
			OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
										.odOrderDetlId(orderStatusInfoVo.getOdOrderDetlId())
										.statusCd(inOrderStatusCd)
										.histMsg(MessageFormat.format(orderDetailStatusHistMsg.getMessage(), "배치", orderStatusInfoVo.getUrStoreId()))
										.createId(createId)
										.build();
			orderErpMapper.addOrderDetailStatusHist(orderDetlHistVo);

			// 주문상세번호에 따른 클레임 번호 조회
			TrackingNumberOrderInfoDto trackingNumberOrderInfoDto = orderErpMapper.getodOrderDetlId(trackingNumberDetailVo);
			// 취소요청 존재시 취소거부 처리
			long odClaimId = StringUtil.nvlLong(trackingNumberOrderInfoDto.getOdClaimId());
			long odClaimDetlId = StringUtil.nvlLong(trackingNumberOrderInfoDto.getOdClaimDetlId());
			if (odClaimId > 0 && odClaimDetlId > 0) {
				try {
					trackingNumberErpBiz.procClaimDenyDefer(orderStatusInfoVo.getOdid(), odClaimId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}