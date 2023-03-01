package kr.co.pulmuone.v1.comm.mapper.order.create;

import java.util.HashMap;
import java.util.List;

import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.order.create.dto.OrderCopyBaseInfoDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCopyDetailInfoRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCopyDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;

/**
*
* <PRE>
* Forbiz Korea
* 주문 복사 관련 Mapper
* </PRE>
*
* <PRE>
* <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 02. 24.		이규한	최초작성
 * =======================================================================
* </PRE>
*/

@Mapper
public interface OrderCopyMapper {

	/**
	 * @Desc 조회구분별(주문번호, 외부몰 주문번호, 수집몰주문번호) 주문 기본정보 조회
	 * @param orderCopyDetailInfoRequestDto
	 * @return OrderCopyBaseInfoDto
	 */
	OrderCopyBaseInfoDto getOrderCopyBaseInfoDto(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception;

	/**
	 * @Desc 주문수량 변경 할 때 조회
	 * @param orderCopyDetailInfoRequestDto
	 * @return
	 * @throws Exception
	 */
	List<OrderCopyBaseInfoDto> getOrderCopyCntChangeInfo(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception;

	/**
	 * @Desc 주문수량 변경 할 때 조회 (매출만 전송)
	 * @param orderCopyDetailInfoRequestDto
	 * @return
	 * @throws Exception
	 */
	List<OrderCopyBaseInfoDto> getOrderCntChangeSalIfInfo(OrderCopyDetailInfoRequestDto orderCopyDetailInfoRequestDto) throws Exception;

	/**
	 * @Desc 주문복사 주문 신규 생성
	 * @param orderVo
	 * @return
	 */
	void addOrderCopyOdOrder(OrderVo orderVo);

	/**
	 * @Desc 주문복사 주문상세 신규 생성
	 * @param orderCopyDto
	 * @return
	 */
	void addOrderCopyOrderDetl(OrderCopyDto orderCopyDto);

	/**
	 *  @Desc 주문복사 할 주문상세 할인금액 건수가 있는지 조회.
	 * @param srchOdOrderId
	 * @param srchOdOrderDetlId
	 * @return
	 */
	int getOrderCopyDiscountCnt(@Param(value ="srchOdOrderId" ) long srchOdOrderId, @Param(value ="srchOdOrderDetlId" ) long srchOdOrderDetlId);

	/**
	 * @Desc 주문복사 할 주문상세 패키지 건수가 있는지 조회.
	 * @param srchOdOrderDetlId
	 * @return
	 */
	int getOrderCopyPackageCnt(long srchOdOrderDetlId);

	/**
	 * @Desc 주문상품 상태값 조회
	 * @param odOrderDetlIdList
	 * @return List<GoodsVo>
	 */
	List<GoodsVo> getOrderDetlGoodsSaleStatus(List<Long> odOrderDetlIdList);

	/**
	 * @Desc 주문상품 출고처 조회
	 * @param odOrderDetlIdList
	 * @return List<GoodsVo>
	 */
	List<GoodsVo> getOrderDetlGoodsWarehouseCode(List<Long> odOrderDetlIdList);

	/**
	 * @Desc 주문복사 할 주문상세에 반품완료 건수 체크
	 * @param srchOdOrderDetlId
	 * @return
	 */
	int getOrderDetlReturnCompletedStatusCheck(long srchOdOrderDetlId);
}