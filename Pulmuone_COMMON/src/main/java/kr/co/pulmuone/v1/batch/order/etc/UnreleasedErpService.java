package kr.co.pulmuone.v1.batch.order.etc;

import kr.co.pulmuone.v1.batch.order.etc.dto.vo.*;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.order.OrderErpMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 미출조회 ERP API배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UnreleasedErpService {

	private final OrderErpMapper orderErpMapper;

	@Autowired
    private final ErpApiExchangeService erpApiExchangeService;

	@Autowired
	private ClaimProcessBiz claimProcessBiz;

	/**
     * @Desc 미출정보 저장
     * @param unreleasedList
     * @return
     * @throws BaseException
     */
	protected void putUnreleasedJob(List<UnreleasedInfoVo> unreleasedList) throws Exception {

		List<UnreleasedInfoUpdateVo> unreleasedListInfoUpdateList = new ArrayList<UnreleasedInfoUpdateVo>();

		if (unreleasedList != null && unreleasedList.size() > 0) {
			for(UnreleasedInfoVo unreleasedInfoVo : unreleasedList) {
				// I/F HeaderCondition 셋팅
				UnreleasedHeaderConditionVo unreleasedHeaderConditionVo = UnreleasedHeaderConditionVo.builder()
	  					.hrdSeq(unreleasedInfoVo.getHrdSeq())
						.oriSysSeq(unreleasedInfoVo.getOriSysSeq())
	  					.ordNum(unreleasedInfoVo.getOdid())
	  					.build();

	  			// I/F Headerline 셋팅
	  			List<UnreleasedDetailConditionVo> line = new ArrayList<UnreleasedDetailConditionVo>();

				if (unreleasedInfoVo.getUnreleasedDetailList() != null && unreleasedInfoVo.getUnreleasedDetailList().size() > 0) {
					for(UnreleasedDetailVo unreleasedDetailVo : unreleasedInfoVo.getUnreleasedDetailList()){
						// 미출정보 개별저장
						UnreleasedDetailInfoConditionVo unreleasedDetailInfoConditionVo = saveUnreleasedInfo(unreleasedDetailVo);
						if (unreleasedDetailInfoConditionVo != null)
							line.add(UnreleasedDetailConditionVo.builder()
									.condition(unreleasedDetailInfoConditionVo).build());
					}
				}

				// 실제 저장 데이터 존재시 I/F 대상에 추가
				if (line.size() > 0)
					unreleasedListInfoUpdateList.add(UnreleasedInfoUpdateVo.builder()
							.condition(unreleasedHeaderConditionVo).line(line).build());
	  		}
		}

		// I/F미출 정보 존재여부 체크 후 저장완료 I/F요청
		if (unreleasedListInfoUpdateList.size() > 0) {
			putUnreleasedErp(unreleasedListInfoUpdateList);
		}
	}

	/**
  	 * @Desc  미출정보 개별저장(트랜잭션 단위)
  	 * @param unreleasedDetailVo
  	 * @return UnreleasedDetailInfoConditionVo
  	 */
	protected UnreleasedDetailInfoConditionVo saveUnreleasedInfo(UnreleasedDetailVo unreleasedDetailVo) throws Exception {

		// 주문번호, 상품순번이 존재하는 경우 저장 진행
		if (StringUtil.isNotEmpty(unreleasedDetailVo.getOdid()) && StringUtil.isNotEmpty(unreleasedDetailVo.getOdOrderDetlSeq())) {
			// 미출정보 등록
			addOrderUnreleased(unreleasedDetailVo);

			// 하이톡이고 스케줄 라인순번이 있으면
			if(SourceServerTypes.HITOK.getCode().equals(unreleasedDetailVo.getCrpCd()) && StringUtil.isNotEmpty(unreleasedDetailVo.getSchLinNo())) {
				return UnreleasedDetailInfoConditionVo.builder().
						hrdSeq(unreleasedDetailVo.getHrdSeq()).
						oriSysSeq(unreleasedDetailVo.getOriSysSeq()).
						ordNum(unreleasedDetailVo.getOdid()).
						ordNoDtl(unreleasedDetailVo.getOdOrderDetlSeq()).
						schLinNo(unreleasedDetailVo.getSchLinNo()).
						build();
			} else {
				return UnreleasedDetailInfoConditionVo.builder().
						hrdSeq(unreleasedDetailVo.getHrdSeq()).
						oriSysSeq(unreleasedDetailVo.getOriSysSeq()).
						ordNum(unreleasedDetailVo.getOdid()).
						ordNoDtl(unreleasedDetailVo.getOdOrderDetlSeq()).
						build();
			}

		}
		return null;
  	}

  	/**
  	 * @Desc  미출정보 등록
  	 * @param unreleasedDetailVo
  	 * @return int
  	 */
  	protected int addOrderUnreleased(UnreleasedDetailVo unreleasedDetailVo) throws Exception {

  		// 미출 정보 등록
  		int insertCnt = orderErpMapper.addOrderUnreleased(unreleasedDetailVo);

  		// 해당 미출 정보가 반품 승인상태의 클레임이 등록 되어 있는지 체크
		int riCnt = orderErpMapper.getOrderClaimReturnsIngCnt(unreleasedDetailVo);
		// 반품 승인상태의 클레임이 존재할 경우
		if (riCnt > 0) {

			try {
				// 1. ODID로 주문, 클레임 정보 목록 조회
				List<OrderClaimRegisterRequestDto> orderClaimList = orderErpMapper.getOrderClaimReturnsIngInfoList(unreleasedDetailVo);
				if (!orderClaimList.isEmpty()) {

					for (OrderClaimRegisterRequestDto orderClaimItem : orderClaimList) {

						orderClaimItem.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.RETURN.getCode());
						orderClaimItem.setClaimStatusCd(OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode());
						// 2. ODID, 클레임 번호로 클레임 상품 정보 조회
						List<OrderClaimGoodsInfoDto> goodsInfoList = orderErpMapper.getOrderClaimReturnsIngDetlInfoList(orderClaimItem);
						orderClaimItem.setGoodsInfoList(goodsInfoList);
						orderClaimItem.setUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
						orderClaimItem.setCustomUrUserId(String.valueOf(Constants.BATCH_CREATE_USER_ID));
						orderClaimItem.setFrontTp(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BATCH.getCodeValue());
						orderClaimItem.setRejectReasonMsg(ClaimEnums.ClaimReasonMsg.CLAIM_UNRELEASED_REASON_RE.getCodeName());

						// 3. 클레임 상세 정보 반품 요청 상태로 변경 처리
						orderErpMapper.putOrderClaimDetlInfoClaimStatusCd(orderClaimItem);

						// 4. 반품 거부 처리
						claimProcessBiz.procClaimDenyDefer(OrderClaimEnums.ClaimStatusTp.RETURN.getCode(), OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode(), orderClaimItem, true);
					}
				}
			}
			catch(Exception e) {
				log.error("미출 데이터 반품 거부 처리 오류 :: <{}>", e.toString());
				e.printStackTrace();
			}
		}

		return insertCnt;
  	}

	/**
  	 * @Desc  미출정보 조회 완료 I/F
  	 * @param unreleasedListInfoUpdateList(요청 파라미터)
  	 * @return BaseApiResponseVo (ERP 연동 후 결과값 return)
  	 */
	protected BaseApiResponseVo putUnreleasedErp(List<UnreleasedInfoUpdateVo> unreleasedListInfoUpdateList) {
		// baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
	    BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(unreleasedListInfoUpdateList, ErpApiEnums.ErpInterfaceId.UNRELEASED_FLAG_INTERFACE_ID.getCode());

	    log.info("baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));

	    // API 호출 오류 시
	    if (!baseApiResponseVo.isSuccess()) {
	    	// TODO : SMS 개발 추가
	        // 별도 확인 필요 : BOS상에 송장정보 업데이트 완료, ERP API 상의 송장정보 완료 업데이트 API 호출 실패 Case
	        log.error("API Call Failure");
	    }
        return baseApiResponseVo;
	}
}