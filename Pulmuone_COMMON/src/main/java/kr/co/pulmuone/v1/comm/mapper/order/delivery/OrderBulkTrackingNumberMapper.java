package kr.co.pulmuone.v1.comm.mapper.order.delivery;

import java.util.List;

import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberUploadDto;
import kr.co.pulmuone.v1.order.delivery.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlListRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberListRequestDto;

/**
* <PRE>
* Forbiz Korea
* 일괄송장 Mapper
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 24.        이규한          	  최초작성
* =======================================================================
* </PRE>
*/

@Mapper
public interface OrderBulkTrackingNumberMapper {

	/**
	 * @Desc 주문상세 번호 존재여부 조회
	 * @param odOrderDetlId
	 * @return int
	 */
	Integer getOdOrderDetlIdCount(@Param("odid") String odid, @Param("odOrderDetlSeq") int odOrderDetlSeq);

	/**
	 * @Desc 택배사 존재여부 조회
	 * @param psShippingCompId
	 * @return int
	 */
	int getPsShippingCompIdCount(@Param("psShippingCompId") Long psShippingCompId);

	/**
	 * @Desc 일괄송장정보 등록
	 * @param OrderBulkTrackingNumberVo
	 * @return int
	 */
	int addOrderBulkTrackingNumber(OrderBulkTrackingNumberVo paramVo);

	/**
	 * @Desc 주문상세 번호 존재여부 조회
	 * @param odOrderDetlId
	 * @return int
	 */
	int getOrderTrackingNumberCnt(@Param("odOrderDetlId") Long odOrderDetlId);

	/**
	 * @Desc 주문상세 송장번호 등록
	 * @param OrderTrackingNumberVo
	 * @return int
	 */
	int addOrderTrackingNumber(OrderTrackingNumberVo paramVo);

	/**
	 * @Desc 주문상세 송장번호 수정
	 * @param OrderTrackingNumberVo
	 * @return int
	 */
	int putOrderTrackingNumber(OrderTrackingNumberVo paramVo);

	/**
	 * @Desc 주문상세 송장번호 삭제
	 * @param OrderTrackingNumberVo
	 * @return int
	 */
	int delOrderTrackingNumber(OrderTrackingNumberVo paramVo);

	/**
	 * 취소요청 건 조회
	 * @param odOrderDetlId
	 * @return
	 */
	int selectCancelRequestClaimInfo(@Param("odOrderDetlId") long odOrderDetlId);

	/**
	 * 취소거부 사유 업데이트
	 * @param odOrderDetlId
	 * @return
	 * @throws Exception
	 */
	int putCancelDenialReason(@Param("odOrderDetlId") long odOrderDetlId, @Param("denialReason") String denialReason);

	/**
	 * 주문정보 취소수량 원복
	 * @param odOrderDetlId
	 * @return
	 */
	int putOrderDetlCancelCnt(@Param("odOrderDetlId") long odOrderDetlId);

	/**
	 * 취소거부로 업데이트
	 * @param odOrderDetlId
	 * @return
	 */
	int putCancelRequestClaimDenial(@Param("odOrderDetlId") long odOrderDetlId);

	/**
	 * @Desc 일괄송장 성공내역 테이블 등록
	 * @param OrderBulkTrackingNumberSuccVo
	 * @return int
	 */
	int addOrderBulkTrackingNumberSucc(OrderBulkTrackingNumberSuccVo paramVo);

	/**
	 * @Desc 일괄송장 실패내역 테이블 등록
	 * @param OrderBulkTrackingNumberFailVo
	 * @return int
	 */
	int addOrderBulkTrackingNumberFail(OrderBulkTrackingNumberFailVo paramVo);

	/**
	 * @Desc 일괄송장정보 수정(성공/실패 건수 업데이트)
	 * @param OrderBulkTrackingNumberVo
	 * @return int
	 */
	int putOrderBulkTrackingNumber(OrderBulkTrackingNumberVo paramVo);

    /**
     * @Desc 일괄송장 입력 내역 목록 조회
     * @param OrderBulkTrackingNumberListRequestDto
     * @return Page<OrderBulkTrackingNumberVo>
     */
	Page<OrderBulkTrackingNumberVo> getOrderBulkTrackingNumberList(OrderBulkTrackingNumberListRequestDto paramDto);

	/**
     * @Desc 일괄송장 입력 실패내역 목록 조회
     * @param OrderBulkTrackingNumberListRequestDto
     * @return List<OrderBulkTrackingNumberFailVo>
     */
	List<OrderBulkTrackingNumberFailVo> getOrderBulkTrackingNumberFailList(OrderBulkTrackingNumberListRequestDto paramDto);

	/**
     * @Desc 일괄 송장 입력 내역 상세 목록 조회
     * @param OrderBulkTrackingNumberDetlListRequestDto
     * @return Page<OrderBulkTrackingNumberDetlDto>
     */
	Page<OrderBulkTrackingNumberDetlDto> getOrderBulkTrackingNumberDetlList(OrderBulkTrackingNumberDetlListRequestDto paramDto);

	/**
	 * @Desc 일괄송장 입력 가능 여부 조회(주문상태가 DR이고 클레임주문건이 아닌경우만 송장 입력 가능)
	 * @param odOrderDetlId
	 * @return int
	 */
	int getOrderBulkTrackingNumberOrderStatus(Long odOrderDetlId);

	/**
	 * 주문상세번호에 따른 클레임 번호 조회
	 * @param odOrderDetlId
	 * @return
	 */
	ClaimNumberSearchVo getOdClaimId(@Param("odOrderDetlId") long odOrderDetlId);

	/**
	 * odid로 주문 정보 조회
	 * @param odid
	 * @return
	 * @throws Exception
	 */
	OrderClaimRegisterRequestDto getOrderInfo(@Param(value = "odid") String odid, @Param(value = "odClaimId") long odClaimId) throws Exception;

	/**
	 * 클레임 정보로 주문 상세 정보 조회
	 * @param claimInfoExcelUploadSuccessVoList
	 * @return
	 * @throws Exception
	 */
	List<OrderClaimGoodsInfoDto> getOrderDetlInfoList(@Param(value = "odid") String odid, @Param(value = "odClaimId") long odClaimId) throws Exception;
}
