package kr.co.pulmuone.v1.order.email.service;

import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.mapper.order.email.OrderEmailMapper;
import kr.co.pulmuone.v1.order.email.dto.*;
import kr.co.pulmuone.v1.order.email.dto.vo.BosOrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderRegularReqInfoVo;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderEmailService {

    private final OrderEmailMapper orderEmailMapper;

	/**
	 * @Desc 주문 상품 정보 조회
	 * @param odOrderId, odOrderDetlIdList
	 * @return List<OrderDetailGoodsDto>
	 */
    protected List<OrderDetailGoodsDto> getOrderDetailGoodsListForEmail(Long odOrderId, List<Long> odOrderDetlIdList) throws Exception{
        return orderEmailMapper.getOrderDetailGoodsListForEmail(odOrderId, odOrderDetlIdList);
    }


	/**
	 * 직접배송 미등록 송장 리스트
	 * @return
	 * @throws Exception
	 */
	protected List<UnregistetedInvoiceDto> getUnregistetedInvoiceList() throws Exception {
    	return orderEmailMapper.getUnregistetedInvoiceList();
	}

    /**
	 * @Desc 주문 접수 완료 이메일 템플릿
	 * @param odOrderId
	 * @param goodsTpCd
	 * @return OrderInfoForEmailVo
	 */
    protected OrderInfoForEmailVo getOrderInfoForEmail(Long odOrderId, String goodsTpCd) throws Exception{
        return orderEmailMapper.getOrderInfoForEmail(odOrderId, goodsTpCd);
    }

    /**
	 * @Desc 주문상세 PK로 주문PK,송장번호 조회
	 * @param odOrderDetlIdList
	 * @return List<OrderDetailGoodsDto>
	 */
    protected List<OrderDetailGoodsDto> getOrderIdList(List<Long> odOrderDetlIdList) throws Exception{
        return orderEmailMapper.getOrderIdList(odOrderDetlIdList);
    }

    /**
	 * @Desc 클레임PK로 클레임 주문상세PK 조회
	 * @param odClaimId
	 * @return List<Long>
	 */
    protected List<Long> getClaimDetlIdList(Long odClaimId) throws Exception{
        return orderEmailMapper.getClaimDetlIdList(odClaimId);
    }

    /**
	 * @Desc 클레임 PK로 주문 정보 조회
	 * @param odClaimId
	 * @return OrderInfoForEmailVo
	 */
    protected OrderInfoForEmailVo getOrderClaimInfoForEmail(Long odClaimId) throws Exception{
        return orderEmailMapper.getOrderClaimInfoForEmail(odClaimId);
    }

    /**
	 * @Desc 정기배송 주문 신청정보 조회
	 * @param odRegularReqId
	 * @return OrderRegularReqInfoVo
	 */
    protected OrderRegularReqInfoVo getOrderRegularReqInfoForEmail(Long odRegularReqId) throws Exception{
        return orderEmailMapper.getOrderRegularReqInfoForEmail(odRegularReqId);
    }

    /**
	 * @Desc 정기배송신청PK로 정기배송 상품 목록 조회
	 * @param odRegularReqId
	 * @return List<OrderDetailGoodsDto>
	 */
    protected List<OrderDetailGoodsDto> getOrderRegularGoodsListForEmail(Long odRegularReqId) throws Exception{
        return orderEmailMapper.getOrderRegularGoodsListForEmail(odRegularReqId);
    }

    /**
	 * @Desc 정기배송주문신청 결과 정보 조회
	 * @param odRegularResultId
	 * @return OrderRegularResultDto
	 */
    protected OrderRegularResultDto getRegularOrderResultCreateGoodsListForEmail(Long odRegularResultId) throws Exception{
        return orderEmailMapper.getRegularOrderResultCreateGoodsListForEmail(odRegularResultId);
    }

    /**
	 * @Desc 정기배송 주문신청결과 정보
	 * @param odRegularResultDetlId
	 * @return OrderRegularResultDto
	 */
    protected OrderRegularResultDto getRegularOrderResultDetlInfoForEmail(Long odRegularResultDetlId) throws Exception{
        return orderEmailMapper.getRegularOrderResultDetlInfoForEmail(odRegularResultDetlId);
    }


    /**
	 * @Desc 정기배송 주문신청결과 목록 조회
	 * @param odRegularResultId
	 * @return List<OrderRegularResultDto>
	 */
    protected List<OrderRegularResultDto> getOrderRegularResultList(Long odRegularResultId) throws Exception{
        return orderEmailMapper.getOrderRegularResultList(odRegularResultId);
    }

    /**
	 * @Desc 정기배송 만료 SMS 대상 조회(BATCH)
	 * @Desc 전일 9시~금일9시 사이 배송완료 된 주문건 중 정기배송 마지막 회차만 조회
	 * @return List<Long>
	 */
    protected List<Long> getTargetOrderRegularExpired() throws Exception{
        return orderEmailMapper.getTargetOrderRegularExpired();
    }

    /**
	 * @Desc 정기배송 만료 SMS 발송 상태 업데이트 (BATCH)
	 * @Param odRegularResultId
	 * @return
	 */
    protected int putSmsSendStatusByOrderRegularExpired(Long odRegularResultId) throws Exception{
    	return orderEmailMapper.putSmsSendStatusByOrderRegularExpired(odRegularResultId);
    }

    /**
	 * @Desc 녹즙 일일배송 종료 SMS 대상 조회(BATCH)
	 * @Desc 마지막 도착예정일 D-4인 녹즙 주문건 조회
	 * @return List<Long>
	 */
    protected List<Long> getTargetOrderDailyGreenJuiceEnd() throws Exception{
        return orderEmailMapper.getTargetOrderDailyGreenJuiceEnd();
    }

    /**
	 * @Desc 주문상세 PK로 주문 정보 조회 -> 녹즙 일일배송 종료 SMS 발송 전용
	 * @param odOrderDetlId
	 * @return OrderInfoForEmailVo
	 */
    protected OrderInfoForEmailVo getOrderDailyGreenJuiceEndInfoForEmail(Long odOrderDetlId) throws Exception{
    	return orderEmailMapper.getOrderDailyGreenJuiceEndInfoForEmail(odOrderDetlId);
    }

    /**
	 * @Desc 정기배송 상품금액변동 자동메일 발송 위한 금액변동 상품목록 조회(BATCH)
	 * @Desc 금일 상품가격 변동 된 상품 목록 조회
	 * @return List<HashMap<String,Long>>
	 */
    protected List<HashMap<String,BigInteger>> getTargetOrderRegularGoodsPriceChangeList() throws Exception{
        return orderEmailMapper.getTargetOrderRegularGoodsPriceChangeList();
    }

    /**
	 * @Desc 정기배송 상품금액변동 자동메일 발송 위한 상품 가격변동 내역 조회(BATCH)
	 * @Desc 금일 상품가격 변동 된 상품 목록 조회
	 * @param odRegularReqId
	 * @param ilGoodsId
	 * @return List<Long>
	 */
    protected OrderRegularGoodsPriceChangeDto getOrderRegularGoodsPriceChangeInfo(Long odRegularReqId, Long ilGoodsId) throws Exception{
    	return orderEmailMapper.getOrderRegularGoodsPriceChangeInfo(odRegularReqId, ilGoodsId);
    }

	/**
	 * @Desc BOS 주문 상태 알림 발송 대상 거래처 조회(BATCH)
	 * @return List<BosOrderStatusNotiDto>
	 */
	protected List<BosOrderStatusNotiDto> getTargetBosOrderStatusNotification() throws Exception{
		return orderEmailMapper.getTargetBosOrderStatusNotification();
	}

	/**
	 * @Desc BOS 주문 상태 알림(BATCH) - 거래처별 주문 건수 조회
	 * @return BosOrderInfoForEmailVo
	 */
	protected BosOrderInfoForEmailVo getOrderCountForOrderStatusNotification(long urClientId) throws Exception{
		return orderEmailMapper.getOrderCountForOrderStatusNotification(urClientId);
	}

	/**
	 * @Desc BOS 올가 식품안전팀 주의 주문 발생 확인
	 * @param odOrderId
	 * @return boolean
	 */
	protected boolean checkBosOrgaCautionOrderNotification(Long odOrderId) throws Exception{
		// 주문정보 조회
		OrderInfoForEmailVo orderInfoVo = orderEmailMapper.getOrderInfoForEmail(odOrderId, null);

		// 주문상품중에 올가홀푸드 상품 있을 경우
		if(orderInfoVo.getOrgaGoodsCount() > 0){
			// 수취인명, 배송지 상세주소 체크
			for(SendEnums.OrgaCautionOrderTargetByName target : SendEnums.OrgaCautionOrderTargetByName.values()){
				if(orderInfoVo.getRecvNm().contains(target.getCodeName()) || orderInfoVo.getRecvAddr().contains(target.getCodeName())){
					return true;
				}
			}
			// 주문자 email 주소 체크
			for(SendEnums.OrgaCautionOrderTargetByEmail target : SendEnums.OrgaCautionOrderTargetByEmail.values()){
				if(orderInfoVo.getMail().contains(target.getCodeName())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @Desc 부분취소 배송비 추가결제 가상계좌 발급
	 * @param odPaymentMasterId
	 * @return OrderInfoForEmailVo
	 */
	protected OrderInfoForEmailVo getPayAdditionalShippingPriceInfo(Long odPaymentMasterId) throws Exception{
		return orderEmailMapper.getPayAdditionalShippingPriceInfo(odPaymentMasterId);
	}

	/**
	 * @Desc 주문 접수 완료 이메일 템플릿
	 * @param odOrderId
	 * @param goodsTpCd
	 * @return OrderInfoForEmailVo
	 */
    protected OrderInfoForEmailVo getOrderShopPickupInfoForEmail(Long odOrderId, String goodsTpCd) throws Exception{
        return orderEmailMapper.getOrderShopPickupInfoForEmail(odOrderId, goodsTpCd);
    }

	/**
	 * @Desc 정기배송 결제실패 정보 조회
	 * @param odRegularResultId
	 * @return OrderRegularResultDto
	 */
	protected OrderRegularResultDto getOrderRegularInfoForEmailForPaymentFail(Long odRegularResultId) throws Exception{
		return orderEmailMapper.getRegularOrderResultForPaymentFail(odRegularResultId);
	}

    /**
	 * @Desc 정기배송 상품금액 변동 알림발송 이력 등록
	 * @Param orderRegularGoodsPriceChangeDto
	 * @return
	 */
    protected int putOdRegularInfoSendHist(OrderRegularGoodsPriceChangeDto orderRegularGoodsPriceChangeDto) {
    	return orderEmailMapper.putOdRegularInfoSendHist(orderRegularGoodsPriceChangeDto);
    }
}