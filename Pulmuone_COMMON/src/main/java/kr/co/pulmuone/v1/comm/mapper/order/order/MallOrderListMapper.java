package kr.co.pulmuone.v1.comm.mapper.order.order;

import java.util.List;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.order.order.dto.mall.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <PRE>
 * Forbiz Korea
 * 프론트 주문 리스트 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 12.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface MallOrderListMapper {

    /**
     * @Desc 주문 목록 조회
     * @param mallOrderListRequestDto
     * @return Page<MallOrderListDto>
     */
    Page<MallOrderListDto> getOrderList(MallOrderListRequestDto mallOrderListRequestDto);

    /**
     * @Desc 취소/반품 리스트 조회
     * @param mallOrderListRequestDto
     * @return Page<MallOrderListDto>
     */
	Page<MallOrderListDto> getOrderClaimList(MallOrderListRequestDto mallOrderListRequestDto);

	/**
	 * @Desc 일일배송 주문 목록 전체 갯수 조회
	 * @param mallOrderDailyListRequestDto
	 * @return Page<MallOrderListDto>
	 * @throws
	 */
	long getOrderDailyListCount(MallOrderDailyListRequestDto mallOrderDailyListRequestDto);

	/**
	 * @Desc 일일배송 주문 목록 조회
	 * @param mallOrderDailyListRequestDto
	 * @return List<MallOrderDailyListDto>
	 * @throws
	 */
	List<MallOrderDailyListDto> getOrderDailyList(MallOrderDailyListRequestDto mallOrderDailyListRequestDto);

	MallOrderDetailGoodsDto getClaimDetailGoodsListForOrderList(@Param("odOrderId") String odOrderId,@Param("ilGoodsId") String ilGoodsId);

	MallOrderDetailGoodsDto getOrderDetailGoodsListForOrderList(@Param("odOrderId")String odOrderId, @Param("ilGoodsId") String ilGoodsId);

	/**
	 * 보낸선물함 리스트 조회
	 * @param mallOrderListRequestDto
	 * @return
	 */
	Page<MallOrderPresentListDto> getOrderPresentList(MallOrderListRequestDto mallOrderListRequestDto);
}