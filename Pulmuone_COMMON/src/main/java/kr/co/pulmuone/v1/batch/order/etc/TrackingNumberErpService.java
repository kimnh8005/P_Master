package kr.co.pulmuone.v1.batch.order.etc;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.batch.order.etc.dto.TrackingNumberOrderInfoDto;
import kr.co.pulmuone.v1.batch.order.etc.dto.vo.*;
import kr.co.pulmuone.v1.comm.api.constant.TransferServerTypes;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.inside.ClaimExcelUploadMapper;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.order.OrderErpMapper;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 송장조회 ERP API배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class TrackingNumberErpService {

	private final OrderErpMapper orderErpMapper;

	private final ClaimExcelUploadMapper claimExcelUploadMapper;

	@Autowired
    private final ErpApiExchangeService erpApiExchangeService;

	private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

	@Autowired
	private OrderStatusBiz orderStatusBiz;

	@Autowired
	private ClaimProcessBiz claimProcessBiz;

	/**
     * @Desc 송장정보 저장
     * @param trackingNumberList
     * @return
     * @throws BaseException
     */
	protected void putTrackingNumberJob(List<TrackingNumberInfoVo> trackingNumberList) throws BaseException {

		List<TrackingNumberInfoUpdateVo> trackingNumberInfoUpdateList = new ArrayList<TrackingNumberInfoUpdateVo>();

		if (trackingNumberList != null && trackingNumberList.size() > 0) {
			trackingNumberList.forEach(trackingNumberInfoVo -> {

				// I/F HeaderCondition 셋팅
	  			TrackingNumberHeaderConditionVo trackingNumberHeaderConditionVo = TrackingNumberHeaderConditionVo.builder()
	  					.hrdSeq(trackingNumberInfoVo.getHrdSeq())
						.oriSysSeq(trackingNumberInfoVo.getOriSysSeq())
	  					.ordNum(trackingNumberInfoVo.getOdid())
	  					.build();

	  			// I/F Headerline 셋팅
	  			List<TrackingNumberDetailConditionVo> line = new ArrayList<TrackingNumberDetailConditionVo>();
	  			List<Long> orderDetlIdList = new ArrayList<>(); // 송장업데이한 OD_ORDER_DETL_ID만 세팅
				
				if (trackingNumberInfoVo.getTrackingNumberDetailList() != null && trackingNumberInfoVo.getTrackingNumberDetailList().size() > 0) {
					trackingNumberInfoVo.getTrackingNumberDetailList().forEach(trackingNumberDetailVo -> {
						// 송장정보 개별저장
						TrackingNumberDetailInfoConditionVo trackingNumberDetailInfoConditionVo = saveTrackingNumberInfo(trackingNumberDetailVo);
						if (trackingNumberDetailInfoConditionVo != null){
							line.add(TrackingNumberDetailConditionVo.builder()
									.condition(trackingNumberDetailInfoConditionVo).build());
							orderDetlIdList.add(StringUtil.nvlLong(trackingNumberDetailVo.getOdOrderDetlId()));
						}
					});

					// 자동메일 발송
					try{
						//List<Long> odOrderDetlIdList = trackingNumberInfoVo.getTrackingNumberDetailList().stream().map(m-> Long.parseLong(m.getOdOrderDetlId())).collect(Collectors.toList());
						if(orderDetlIdList.size() > 0)
							orderStatusBiz.sendOrderGoodsDelivery(orderDetlIdList, false);
					}catch(Exception e){
						log.error("ERROR ====== 상품발송 자동메일 발송 오류 ODID ::" + trackingNumberInfoVo.getOdid());
						log.error(e.getMessage());
					}
				}

				// 실제 저장 데이터 존재시 I/F 대상에 추가
				if (line.size() > 0)
					trackingNumberInfoUpdateList.add(TrackingNumberInfoUpdateVo.builder()
							.condition(trackingNumberHeaderConditionVo).line(line).build());

	  		});
		}

		// I/F송장 정보 존재여부 체크 후 저장완료 I/F요청
		if (trackingNumberInfoUpdateList.size() > 0) {
			try {
				putTrackingNumberErp(ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_FLAG_INTERFACE_ID.getCode(), trackingNumberInfoUpdateList);
			} catch (Exception e) {
				new BaseException(e.getMessage());
			}
		}
	}

	/**
  	 * @Desc  송장정보 개별저장(트랜잭션 단위)
  	 * @param trackingNumberDetailVo
  	 * @return TrackingNumberDetailInfoConditionVo
  	 */
	protected TrackingNumberDetailInfoConditionVo saveTrackingNumberInfo(TrackingNumberDetailVo trackingNumberDetailVo) {
		try {
			// 주문상세번호 조회
			TrackingNumberOrderInfoDto trackingNumberOrderInfoDto = getodOrderDetlId(trackingNumberDetailVo);
			String odOrderDetlId	= trackingNumberOrderInfoDto.getOdOrderDetlId();
			long odClaimId			= StringUtil.nvlLong(trackingNumberOrderInfoDto.getOdClaimId());
			long odClaimDetlId		= StringUtil.nvlLong(trackingNumberOrderInfoDto.getOdClaimDetlId());
			int cancelCompleteCnt	= StringUtil.nvlInt(trackingNumberOrderInfoDto.getCancelCompleteCnt()); // 취소완료 클레임 건 수
			int orderCnt			= StringUtil.nvlInt(trackingNumberOrderInfoDto.getOrderCnt()); // 주문 건 수

			// 취소 완료 건수가 주문건수랑 같은 경우 (전체 취소) 처리 하지 않음
			if(orderCnt == cancelCompleteCnt) {
				return TrackingNumberDetailInfoConditionVo.builder()
						.hrdSeq(trackingNumberDetailVo.getHrdSeq())
						.oriSysSeq(trackingNumberDetailVo.getOriSysSeq())
						.dlvNo(trackingNumberDetailVo.getTrackingNumber())
						.ordNoDtl(trackingNumberDetailVo.getOdOrderDetlSeq())
						.schLinNo(StringUtil.isEmpty(trackingNumberDetailVo.getOdOrderDetlDailySchSeq()) ? "" : trackingNumberDetailVo.getOdOrderDetlDailySchSeq())
						.build();
			}

			trackingNumberDetailVo.setOdOrderDetlId(odOrderDetlId);

			// 전송 대상 시스템 구분 코드값이 CJ인 경우 CJ택배사 기본코드 값 셋팅
			if (TransferServerTypes.CJWMS.getCode().equals(trackingNumberDetailVo.getCrpCd()))
				trackingNumberDetailVo.setPsShippingCompId(ErpApiEnums.PolicyShippingComp.CJ.getCode());

			// 주문상세번호, 송장번호가 존재하는 경우 저장 진행
			if (StringUtil.isNotEmpty(odOrderDetlId) && StringUtil.isNotEmpty(trackingNumberDetailVo.getTrackingNumber())) {

				// 기존 송장정보 삭제
				delOrderTrackingNumber(trackingNumberDetailVo);
				// 신규 송장정보 등록
				addOrderTrackingNumber(trackingNumberDetailVo);
				// 배송중 상태 업데이트
				putOrderDetlDeliveryIng(StringUtil.nvlLong(odOrderDetlId), Constants.BATCH_CREATE_USER_ID, OrderEnums.OrderStatus.DELIVERY_ING.getCode());
				// 이력 등록
				addOrderDetailStatusHist(StringUtil.nvlLong(odOrderDetlId), Constants.BATCH_CREATE_USER_ID, OrderEnums.OrderStatus.DELIVERY_READY.getCode(), trackingNumberDetailVo.getTrackingNumber(), OrderEnums.OrderStatus.DELIVERY_ING.getCode());

				// 취소요청 존재시 취소거부 처리
				if (odClaimId > 0 && odClaimDetlId > 0) {
					try {
						procClaimDenyDefer(trackingNumberDetailVo.getOdid(), odClaimId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// 스케줄 라인번호 존재시 스케줄 송장정보 업데이트
				if (StringUtil.isNotEmpty(trackingNumberDetailVo.getOdOrderDetlDailySchSeq())) putOrderDailyScheduleTrakingNumber(trackingNumberDetailVo);

				return TrackingNumberDetailInfoConditionVo.builder()
						.hrdSeq(trackingNumberDetailVo.getHrdSeq())
						.oriSysSeq(trackingNumberDetailVo.getOriSysSeq())
						.dlvNo(trackingNumberDetailVo.getTrackingNumber())
						.ordNoDtl(trackingNumberDetailVo.getOdOrderDetlSeq())
						.schLinNo(StringUtil.isEmpty(trackingNumberDetailVo.getOdOrderDetlDailySchSeq()) ? "" : trackingNumberDetailVo.getOdOrderDetlDailySchSeq())
						.build();
			}
			// 주문상세번호, 가맹점 코드가 존재하는 경우 저장 진행
			else if(StringUtil.isNotEmpty(odOrderDetlId) && StringUtil.isNotEmpty(trackingNumberDetailVo.getStoCd())) {

				String schLinNo = StringUtil.isEmpty(trackingNumberDetailVo.getOdOrderDetlDailySchSeq()) ? "" : trackingNumberDetailVo.getOdOrderDetlDailySchSeq();

				if(trackingNumberOrderInfoDto.getOrderStatusCd().equals(OrderEnums.OrderStatus.DELIVERY_READY.getCode())){
					
					// 직배송 택배로 오늘날짜 송장번호로 셋팅
					trackingNumberDetailVo.setPsShippingCompId(BatchConstants.ETC);
					trackingNumberDetailVo.setTrackingNumber(DateUtil.getCurrentDate("yyyyMMdd"));

					// 기존 송장정보 삭제
					delOrderTrackingNumber(trackingNumberDetailVo);
					// 신규 송장정보 등록
					addOrderTrackingNumber(trackingNumberDetailVo);

					// 배송중 상태 업데이트
					putOrderDetlDeliveryIng(StringUtil.nvlLong(odOrderDetlId), Constants.BATCH_CREATE_USER_ID, OrderEnums.OrderStatus.DELIVERY_ING.getCode());

					// 이력 등록
					addOrderDetailStatusHist(StringUtil.nvlLong(odOrderDetlId), Constants.BATCH_CREATE_USER_ID, OrderEnums.OrderStatus.DELIVERY_READY.getCode(), trackingNumberDetailVo.getStoCd(), OrderEnums.OrderStatus.STORE_DELIVERY.getCode());

					// 취소요청 존재시 취소거부 처리
					if (odClaimId > 0 && odClaimDetlId > 0) {
						try {
							procClaimDenyDefer(trackingNumberDetailVo.getOdid(), odClaimId);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}

				// 초기화
				trackingNumberDetailVo.setPsShippingCompId(null);
				trackingNumberDetailVo.setTrackingNumber("");

				// 가맹점 코드 업데이트
				putOrderStoCd(trackingNumberDetailVo);

				if(!"".equals(schLinNo)) {
					// 스케쥴 가맹점 코드 업데이트
					putOrderDailyScheduleDeliveryComplete(trackingNumberDetailVo);
				}

				return TrackingNumberDetailInfoConditionVo.builder()
						.hrdSeq(trackingNumberDetailVo.getHrdSeq())
						.oriSysSeq(trackingNumberDetailVo.getOriSysSeq())
						.dlvNo(trackingNumberDetailVo.getTrackingNumber())
						.ordNoDtl(trackingNumberDetailVo.getOdOrderDetlSeq())
						.schLinNo(schLinNo)
						.build();
			}

		} catch (Exception e) {
			log.error("ERROR ====== 송장 저장 오류 ODID ::" + trackingNumberDetailVo.getOdid());
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
  	}

	/**
     * @Desc 기타송장정보 저장
     * @param trackingNumberEtcList
     * @return
     * @throws BaseException
     */
	protected void putTrackingNumberEtcJob(List<TrackingNumberEtcInfoVo> trackingNumberEtcList) throws BaseException {

		List<TrackingNumberEtcInfoUpdateVo> trackingNumberEtcInfoUpdateList = new ArrayList<TrackingNumberEtcInfoUpdateVo>();

		if (trackingNumberEtcList != null && trackingNumberEtcList.size() > 0) {

			trackingNumberEtcList.forEach(trackingNumberEtcInfoVo -> {
				TrackingNumberEtcHeaderConditionVo trackingNumberEtcHeaderConditionVo = saveTrackingNumberEtcInfo(trackingNumberEtcInfoVo);

				// 실제 저장 데이터 존재시 I/F 대상에 추가
				if (trackingNumberEtcHeaderConditionVo != null) {
					trackingNumberEtcInfoUpdateList.add(TrackingNumberEtcInfoUpdateVo.builder().condition(trackingNumberEtcHeaderConditionVo).build());

                    // 자동메일 발송
                    try {
                        List<Long> odOrderDetlIdList = new ArrayList<>();
                        odOrderDetlIdList.add(Long.parseLong(trackingNumberEtcInfoVo.getOdOrderDetlId()));
                        orderStatusBiz.sendOrderGoodsDelivery(odOrderDetlIdList, false);
                    } catch (Exception e) {
                        log.error("ERROR ====== 상품발송 자동메일 발송 오류 ODID ::" + trackingNumberEtcInfoVo.getOdid());
                        log.error(e.getMessage());
                    }
				}
			});
		}

		// I/F송장 정보 존재여부 체크 후 저장완료 I/F요청
		if (trackingNumberEtcInfoUpdateList.size() > 0) {
			try {
				putTrackingNumberErp(ErpApiEnums.ErpInterfaceId.TRACKINGNUMBER_ETC_FLAG_INTERFACE_ID.getCode(), trackingNumberEtcInfoUpdateList);
			} catch (Exception e) {
				new BaseException(e.getMessage());
			}
		}
	}

	/**
  	 * @Desc  기타송장정보 개별저장(트랜잭션 단위)
  	 * @param trackingNumberEtcInfoVo
  	 * @return TrackingNumberEtcHeaderConditionVo
  	 */
	protected TrackingNumberEtcHeaderConditionVo saveTrackingNumberEtcInfo(TrackingNumberEtcInfoVo trackingNumberEtcInfoVo) {
		try {
			TrackingNumberDetailVo trackingNumberDetailVo = OBJECT_MAPPER.convertValue(trackingNumberEtcInfoVo, TrackingNumberDetailVo.class);

			// 주문상세번호 조회
			TrackingNumberOrderInfoDto trackingNumberOrderInfoDto = getodOrderDetlId(trackingNumberDetailVo);
			String odOrderDetlId	= trackingNumberOrderInfoDto.getOdOrderDetlId();
			long odClaimId			= StringUtil.nvlLong(trackingNumberOrderInfoDto.getOdClaimId());
			long odClaimDetlId		= StringUtil.nvlLong(trackingNumberOrderInfoDto.getOdClaimDetlId());
			int cancelCompleteCnt	= StringUtil.nvlInt(trackingNumberOrderInfoDto.getCancelCompleteCnt()); // 취소완료 클레임 건 수
			int orderCnt			= StringUtil.nvlInt(trackingNumberOrderInfoDto.getOrderCnt()); // 주문 건 수

			// 취소 완료 건수가 주문건수랑 같은 경우 (전체 취소) 처리 하지 않음
			if(orderCnt == cancelCompleteCnt) {
				return TrackingNumberEtcHeaderConditionVo.builder()
						.ordNum(trackingNumberDetailVo.getOdid())
						.ordNoDtl(trackingNumberDetailVo.getOdOrderDetlSeq())
						.dlvNo(trackingNumberDetailVo.getTrackingNumber())
						.build();
			}

			trackingNumberDetailVo.setOdOrderDetlId(odOrderDetlId);
			trackingNumberEtcInfoVo.setOdOrderDetlId(odOrderDetlId);

			// 주문상세번호, 송장번호가 존재하는 경우 저장 진행
			if (StringUtil.isNotEmpty(odOrderDetlId) && StringUtil.isNotEmpty(trackingNumberEtcInfoVo.getTrackingNumber())) {

				// 기존 송장정보 삭제
				delOrderTrackingNumber(trackingNumberDetailVo);
				// 신규 송장정보 등록
				addOrderTrackingNumber(trackingNumberDetailVo);
				// 배송중 상태 업데이트
				putOrderDetlDeliveryIng(StringUtil.nvlLong(odOrderDetlId), Constants.BATCH_CREATE_USER_ID, OrderEnums.OrderStatus.DELIVERY_ING.getCode());
				// 이력 등록
				addOrderDetailStatusHist(StringUtil.nvlLong(odOrderDetlId), Constants.BATCH_CREATE_USER_ID, OrderEnums.OrderStatus.DELIVERY_READY.getCode(), trackingNumberDetailVo.getTrackingNumber(), OrderEnums.OrderStatus.DELIVERY_ING.getCode());
				// 취소요청 존재시 취소거부 처리
				if (odClaimId > 0 && odClaimDetlId > 0) {
					try {
						procClaimDenyDefer(trackingNumberDetailVo.getOdid(), odClaimId);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				return TrackingNumberEtcHeaderConditionVo.builder()
						.ordNum(trackingNumberDetailVo.getOdid())
						.ordNoDtl(trackingNumberDetailVo.getOdOrderDetlSeq())
						.dlvNo(trackingNumberDetailVo.getTrackingNumber())
						.build();
			}
		} catch (Exception e) {
			log.error("ERROR ====== 송장 저장 오류 ODID ::" + trackingNumberEtcInfoVo.getOdid());
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
  	}

	/**
	 * 클레임 취소거부 처리
	 * @param odid
	 * @param odClaimId
	 * @throws Exception
	 */
	protected void procClaimDenyDefer(String odid, long odClaimId) throws Exception {

		// odid로 주문정보 조회
		OrderClaimRegisterRequestDto claimReqInfo = claimExcelUploadMapper.getOrderInfoByTarckingNumber(odid, odClaimId);

		// 클레임상태구분 취소
		claimReqInfo.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode());

		// 클레임상태코드 취소거부
		claimReqInfo.setClaimStatusCd(OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode());

		List<OrderClaimGoodsInfoDto> goodsInfoList = claimExcelUploadMapper.getOrderDetlInfoListByTarckingNumber(odid, odClaimId);
		claimReqInfo.setGoodsInfoList(goodsInfoList);
		claimReqInfo.setUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
		claimReqInfo.setCustomUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
		claimReqInfo.setOdClaimId(odClaimId);
		claimReqInfo.setFrontTp(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BATCH.getCodeValue());

		claimProcessBiz.procClaimDenyDefer(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode(), OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode(), claimReqInfo, true);
	}

	/**
  	 * @Desc  주문상세번호 조회
  	 * @param trackingNumberDetailInfo
  	 * @return String
  	 */
  	protected TrackingNumberOrderInfoDto getodOrderDetlId(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException {
  		return orderErpMapper.getodOrderDetlId(trackingNumberDetailInfo);
  	}

  	/**
  	 * @Desc  송장번호 삭제
  	 * @param trackingNumberDetailInfo
  	 * @return int
  	 */
  	protected int delOrderTrackingNumber(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException {
  		return orderErpMapper.delOrderTrackingNumber(trackingNumberDetailInfo);
  	}

  	/**
  	 * @Desc  송장번호 등록
  	 * @param trackingNumberDetailInfo
  	 * @return int
  	 */
  	protected int addOrderTrackingNumber(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException {
  		return orderErpMapper.addOrderTrackingNumber(trackingNumberDetailInfo);
  	}

	/**
	 * @Desc  배송중 업데이트
	 * @param odOrderDetlId
	 * @param createId
	 * @param orderStatusCd
	 * @return int
	 */
	protected int putOrderDetlDeliveryIng(long odOrderDetlId, long createId, String orderStatusCd) throws BaseException {
		return orderErpMapper.putOrderDetlDeliveryIng(odOrderDetlId, createId, orderStatusCd);
	}


	/**
	 * 주문 상태 이력 등록
	 * @param odOrderDetlId
	 * @param createId
	 * @param orderStatusCd
	 * @param trackingNumber
	 * @return
	 * @throws BaseException
	 */
	protected int addOrderDetailStatusHist(long odOrderDetlId, long createId, String orderStatusCd, String trackingNumber, String OrderStatus) throws BaseException {
		OrderEnums.OrderDetailStatusHistMsg orderDetailStatusHistMsg = OrderEnums.OrderDetailStatusHistMsg.findByCode(OrderStatus);

		OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
				.odOrderDetlId(odOrderDetlId)
				.statusCd(orderStatusCd)
				.histMsg(MessageFormat.format(orderDetailStatusHistMsg.getMessage(), "배치", trackingNumber))
				.createId(createId)
				.build();

		return orderErpMapper.addOrderDetailStatusHist(orderDetlHistVo);
	}




  	/**
  	 * @Desc  일일배송 스케줄 송장번호 수정
  	 * @param trackingNumberDetailInfo
  	 * @return int
  	 */
  	protected int putOrderDailyScheduleTrakingNumber(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException {
  		return orderErpMapper.putOrderDailyScheduleTrakingNumber(trackingNumberDetailInfo);
  	}

	/**
  	 * @Desc  송장정보 조회 완료 I/F
  	 * @param ifId(I/F ID), trackingNumberInfoUpdateList(요청 파라미터)
  	 * @return BaseApiResponseVo (ERP 연동 후 결과값 return)
  	 */
	protected BaseApiResponseVo putTrackingNumberErp(String ifId, List<?> trackingNumberInfoUpdateList) throws Exception {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(trackingNumberInfoUpdateList, ifId);

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    // API 호출 오류 시
	    if (!baseApiResponseVo.isSuccess()) {
	    	// TODO : SMS 개발 추가
	        // 별도 확인 필요 : BOS상에 송장정보 업데이트 완료, ERP API 상의 송장정보 완료 업데이트 API 호출 실패 Case
	        log.error("API Call Failure");
	    }
        return baseApiResponseVo;
	}

	/**
	 * 가맹점 코드 업데이트
	 * @param trackingNumberDetailInfo
	 * @return int
	 */
	protected int putOrderStoCd(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException {
		return orderErpMapper.putOrderStoCd(trackingNumberDetailInfo);
	}


	/**
	 * @Desc  취소요청 존재시 클레임 거부사유 업데이트
	 * @param odClaimId
	 * @return int
	 */
	protected int putClaimRejectReasonMsg(long odClaimId, String rejectReasonMsg) throws BaseException {
		return orderErpMapper.putClaimRejectReasonMsg(odClaimId, rejectReasonMsg);
	}

	/**
	 * @Desc  취소요청 존재시 거부 업데이트
	 * @param odOrderDetlId
	 * @return int
	 */
	protected int putCancelRequestDenial(long odOrderDetlId, String orderStatusCd, String updateOrderStatusCd) throws BaseException {
		return orderErpMapper.putCancelRequestDenial(odOrderDetlId, orderStatusCd, updateOrderStatusCd);
	}

	/**
	 * 주문 상태 취소 거부 이력 등록
	 * @param odOrderDetlId
	 * @param createId
	 * @param orderStatusCd
	 * @return
	 * @throws BaseException
	 */
	protected int addOrderDetailCEStatusHist(long odOrderDetlId, long createId, String orderStatusCd, String histMsg) throws BaseException {
		OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
				.odOrderDetlId(odOrderDetlId)
				.statusCd(orderStatusCd)
				.histMsg(histMsg)
				.createId(createId)
				.build();

		return orderErpMapper.addOrderDetailStatusHist(orderDetlHistVo);
	}

	/**
	 * 스케쥴 관리 배송완료 여부 업데이트
	 * @param trackingNumberDetailInfo
	 * @return
	 * @throws BaseException
	 */
	protected int putOrderDailyScheduleDeliveryComplete(TrackingNumberDetailVo trackingNumberDetailInfo) throws BaseException {
		return orderErpMapper.putOrderDailyScheduleDeliveryComplete(trackingNumberDetailInfo);
	}

}