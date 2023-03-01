package kr.co.pulmuone.v1.order.order.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.order.order.dto.mall.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.order.order.MallOrderListMapper;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문리스트 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 12.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class MallOrderListService {

    private final MallOrderListMapper mallOrderListMapper;

    /**
     * @Desc 주문/배송 리스트 조회
     * @param mallOrderListRequestDto
     * @return Page<MallOrderListDto>
     * @throws
     */
   	protected Page<MallOrderListDto> getOrderList(MallOrderListRequestDto mallOrderListRequestDto) {
   		PageMethod.startPage(mallOrderListRequestDto.getPage(), mallOrderListRequestDto.getLimit());

		Page<MallOrderListDto> mallOrderList = mallOrderListMapper.getOrderList(mallOrderListRequestDto);

		// 주문 대표상품의 상태값 수정
		if(CollectionUtils.isNotEmpty(mallOrderList)){
			for(MallOrderListDto dto : mallOrderList.getResult()){
				MallOrderDetailGoodsDto claimDto = mallOrderListMapper.getClaimDetailGoodsListForOrderList(dto.getOdOrderId(), dto.getIlGoodsId());
				MallOrderDetailGoodsDto orderDto = mallOrderListMapper.getOrderDetailGoodsListForOrderList(dto.getOdOrderId(), dto.getIlGoodsId());

				// 정상주문이 있으면 정상주문 상태값이 우선
				if(ObjectUtils.isNotEmpty(orderDto)){
					dto.setRepGoodsOrderStatusNm(orderDto.getOrderStatusNm());
					dto.setRepGoodsOrderStatusCd(orderDto.getOrderStatusCd());
				}else{
					// 정상주문이 없으면 클레임 주문건의 상태값으로 수정
					if(ObjectUtils.isNotEmpty(claimDto)){
						dto.setRepGoodsOrderStatusNm(claimDto.getOrderStatusNm());
						dto.setRepGoodsOrderStatusCd(claimDto.getOrderStatusCd());
					}
				}

				// 상담원 주문결제 -> 입금대기중 상태일때만 노출(HGRM-8708)
				if(!OrderEnums.OrderStatus.INCOM_READY.getCode().equals(dto.getRepGoodsOrderStatusCd())){
					dto.setDirectPaymentYn("N");
				}
			}
		}


        return mallOrderList;
	}

    /**
     * @Desc 취소/반품 리스트 조회
     * @param mallOrderListRequestDto
     * @return Page<MallOrderListDto>
     * @throws
     */
	public Page<MallOrderListDto> getOrderClaimList(MallOrderListRequestDto mallOrderListRequestDto) {
   		PageMethod.startPage(mallOrderListRequestDto.getPage(), mallOrderListRequestDto.getLimit());
        return mallOrderListMapper.getOrderClaimList(mallOrderListRequestDto);
	}

	/**
	 * @Desc 일일배송 주문 목록 전체 갯수 조회
	 * @param mallOrderDailyListRequestDto
	 * @return Page<MallOrderListDto>
	 * @throws
	 */
	public long getOrderDailyListCount(MallOrderDailyListRequestDto mallOrderDailyListRequestDto) {
		return mallOrderListMapper.getOrderDailyListCount(mallOrderDailyListRequestDto);
	}

	/**
	 * @Desc 일일배송 주문 목록 조회
	 * @param mallOrderDailyListRequestDto
	 * @return List<
	 * @throws
	 */
	public List<MallOrderDailyListDto> getOrderDailyList(MallOrderDailyListRequestDto mallOrderDailyListRequestDto) {
		return mallOrderListMapper.getOrderDailyList(mallOrderDailyListRequestDto);

	}

	/**
     * @Desc 보낸선물함 리스트 조회
     * @param mallOrderListRequestDto
     * @return Page<MallOrderPresentListDto>
     * @throws
     */
   	protected Page<MallOrderPresentListDto> getOrderPresentList(MallOrderListRequestDto mallOrderListRequestDto) {
   		PageMethod.startPage(mallOrderListRequestDto.getPage(), mallOrderListRequestDto.getLimit());

		Page<MallOrderPresentListDto> mallOrderList = mallOrderListMapper.getOrderPresentList(mallOrderListRequestDto);

		// 주문 대표상품의 상태값 수정
		if(CollectionUtils.isNotEmpty(mallOrderList)){
			for(MallOrderPresentListDto dto : mallOrderList.getResult()){
				MallOrderDetailGoodsDto claimDto = mallOrderListMapper.getClaimDetailGoodsListForOrderList(dto.getOdOrderId(), dto.getIlGoodsId());
				MallOrderDetailGoodsDto orderDto = mallOrderListMapper.getOrderDetailGoodsListForOrderList(dto.getOdOrderId(), dto.getIlGoodsId());

				// 정상주문이 있으면 정상주문 상태값이 우선
				if(ObjectUtils.isNotEmpty(orderDto)){
					dto.setRepGoodsOrderStatusNm(orderDto.getOrderStatusNm());
					dto.setRepGoodsOrderStatusCd(orderDto.getOrderStatusCd());
				}else{
					// 정상주문이 없으면 클레임 주문건의 상태값으로 수정
					if(ObjectUtils.isNotEmpty(claimDto)){
						dto.setRepGoodsOrderStatusNm(claimDto.getOrderStatusNm());
						dto.setRepGoodsOrderStatusCd(claimDto.getOrderStatusCd());
					}
				}
			}
		}


        return mallOrderList;
	}
}