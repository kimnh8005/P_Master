package kr.co.pulmuone.v1.comm.mapper.order.regular;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.order.regular.dto.RegularReqListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqListRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultReqRoundGoodsListDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularInfoVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularPaymentKeyVo;
import kr.co.pulmuone.v1.order.regular.dto.vo.RegularPaymentKeyVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.ShippingTemplateGroupByVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;

@Mapper
public interface OrderRegularMapper {

	OrderRegularInfoVo getActiveRegularInfo(Long urUserId) throws Exception;

	List<SpCartVo> getGoodsListByShippingPolicy(
			@Param("shippingTemplateData") ShippingTemplateGroupByVo shippingTemplateData,
			@Param("urUserId") Long urUserId) throws Exception;

	GetSessionShippingResponseDto getRegularShippingZone(Long odRegularReqId) throws Exception;

	int addRegularPaymentKey(OrderRegularPaymentKeyVo orderRegularPaymentKeyVo) throws Exception;

	RegularPaymentKeyVo getRegularPaymentKey(@Param("urUserId") Long urUserId) throws Exception;

	int putNoPaymentRegularPaymentKey(@Param("urUserId") Long urUserId,
			@Param("noPaymentReason") String noPaymentReason) throws Exception;

	/**
	 * 정기배송 주문 신청 리스트 조회
	 * @param regularReqListRequestDto
	 * @return
	 * @throws Exception
	 */
	Page<RegularReqListDto> getOrderRegularReqList(RegularReqListRequestDto regularReqListRequestDto) throws Exception;


	/**
	 * 정기배송 주문 결과 상세 상품 수량 정보 업데이트
	 * @param regularReqDetailGoodsList
	 * @param stdDate
	 * @return
	 * @throws Exception
	 */
	int putOrderRegularResultDetailGoodsInfo(@Param("odRegularReqId") long odRegularReqId, @Param("stdDate") LocalDate stdDate) throws Exception;

	/**
	 * 정기배송 신청 번호로 출고처 목록 조회
	 * @param odRegularReqId
	 * @return
	 * @throws Exception
	 */
	List<Long> getOdRegularUrWarehouseIdsByOdRegularReqId(@Param("odRegularReqId") long odRegularReqId);

	/**
	 * 출고처 휴일 조회
	 * @param urWarehouseIds
	 * @return
	 * @throws Exception
	 */
	List<String> getUrWarehouseHolidayList(@Param("urWarehouseIds") List<Long> urWarehouseIds, @Param("urWarehouseIdsSize") int urWarehouseIdsSize);
}
