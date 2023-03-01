package kr.co.pulmuone.v1.comm.mapper.order.claim;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.order.claim.dto.MallOrderClaimAddGoodsListDto;
import kr.co.pulmuone.v1.order.claim.dto.MallOrderClaimDto;
import kr.co.pulmuone.v1.order.claim.dto.MallOrderClaimRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.MallOrderDetlClaimDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimAccountVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimAttcVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlDtVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlHistVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimShippingZoneVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;

/**
 * <PRE>
 * Forbiz Korea
 * 프론트 주문 클레임 요청 리스트 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 22.	김명진 		최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface MallOrderClaimMapper {

    /**
     * @Desc 주문 조회
     * @param MallOrderListRequestDto
     * @return Page<MallOrderListDto>
     */
    MallOrderClaimDto getOrderClaim(MallOrderClaimRequestDto mallOrderClaimRequestDto);

    /**
     * 주문 상세 목록 조회
     * @param mallOrderClaimRequestDto
     * @return
     */
    List<MallOrderDetlClaimDto> getOrderDetlClaimList(MallOrderClaimRequestDto mallOrderClaimRequestDto);

    /**
     * 주문 상세 추가 상품 목록 조회
     * @param mallOrderClaimRequestDto
     * @return
     */
    List<MallOrderClaimAddGoodsListDto> getOrderDetlAddGoodsList(MallOrderClaimRequestDto mallOrderClaimRequestDto);

	/**
	 * 주문 상세 상태 업데이트
	 * @param odOrderDetlId
	 * @param orderStatusCd
	 * @return
	 */
	int putOrderDetailStatus(OrderDetlVo orderDetlVo);

	/**
	 * 주문 클레임 상세 상태 이력 등록
	 * @param claimDetlHistVo
	 * @return
	 */
	int putClaimDetailStatusHist(ClaimDetlHistVo claimDetlHistVo);

	/**
	 * 주문 상세 상태 조회
	 * @param odOrderId
	 * @param odOrderDetlId
	 * @return
	 */
	String getOrderDetlStatusCd(@Param("odOrderId") long odOrderId, @Param("odOrderDetlId") long odOrderDetlId);

	/**
	 * 주문 클레임 id 시퀀스 조회
	 * @return
	 */
	long getClaimIdSeq();

	/**
	 * 주문 클레임 상세 id 시퀀스 조회
	 * @return
	 */
	long getClaimDetlIdSeq();

    /**
     * 주문 클레임 상세 이력 id 시퀀스 조회
     * @return
     */
    long getOdClaimDetlHistId();

	/**
	 * 주문 클레임 등록
	 * @param claimVo
	 * @return
	 */
	int addClaim(ClaimVo claimVo);

	/**
	 * 주문 클레임 상세 등록
	 * @param claimDetlVo
	 * @return
	 */
	int addClaimDetl(ClaimDetlVo claimDetlVo);

	/**
	 * 주문 클레임 상세 일자 등록
	 * @param claimDetlDtVo
	 * @return
	 */
	int addClaimDetlDt(ClaimDetlDtVo claimDetlDtVo);

	/**
	 * 주문 클레임 환불 계좌 등록
	 * @param claimAccountVo
	 * @return
	 */
	int addClaimAccount(ClaimAccountVo claimAccountVo);

	/**
	 *
	 * 주문 클레임 첨부파일 등록
	 * @param claimAttcVo
	 * @return
	 */
	int addClaimAttc(ClaimAttcVo claimAttcVo);

	/**
	 *
	 * 주문 클레임 배송지 등록
	 * @param claimShippingZoneVo
	 * @return
	 */
	int addClaimShippingZone(ClaimShippingZoneVo claimShippingZoneVo);
}
