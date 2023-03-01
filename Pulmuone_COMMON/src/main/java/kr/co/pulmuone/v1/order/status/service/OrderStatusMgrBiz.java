package kr.co.pulmuone.v1.order.status.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusActionListResponseDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusActionResponseDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusDisplayRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusGoodsTypeListResponseDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusGoodsTypeResponseDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusListResponseDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusResponseDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusSearchRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusSearchResponseDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusTypeActionListResponseDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusTypeActionRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusTypeActionSearchRequestDto;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusActionVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusGoodsTypeVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusVo;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
public interface OrderStatusMgrBiz {
    OrderStatusSearchResponseDto getSearchCode(OrderStatusSearchRequestDto orderStatusSearchRequestDto);

    OrderStatusListResponseDto getOrderStatusList();

    ApiResult<?> addOrderStatus(OrderStatusVo orderStatusVo) throws Exception;

	int putOrderStatus(OrderStatusRequestDto orderStatusRequestDto) throws Exception;

	OrderStatusGoodsTypeListResponseDto getOrderStatusGoodsTypeList();

	OrderStatusResponseDto getOrderStatus(String statusCd);

	OrderStatusGoodsTypeResponseDto getOrderStatusGoodsType(String typeCd);

	ApiResult<?> addOrderStatusGoodsType(OrderStatusGoodsTypeVo orderStatusGoodsTypeVo) throws Exception;

	int putOrderStatusGoodsType(String typeNm, String typeCd, String useType) throws Exception;

	OrderStatusActionListResponseDto getOrderStatusActionList(BaseRequestPageDto baseRequestPageDto);

	OrderStatusActionResponseDto getOrderStatusAction(Long actionId);

	ApiResult<?> addOrderStatusAction(OrderStatusActionVo orderStatusActionVo) throws Exception;

	int putOrderStatusAction(OrderStatusActionVo orderStatusActionVo) throws Exception;

	OrderStatusTypeActionListResponseDto getOrderStatusTypeActionList(OrderStatusTypeActionSearchRequestDto orderStatusTypeActionSearchRequestDto);

	int delOrderStatusTypeAction(Long actionSeq, String useType) throws Exception;

	GetCodeListResponseDto getOrderStatusActionIdList();

	GetCodeListResponseDto getOrderStatusStatusCdList();

	GetCodeListResponseDto getOrderStatusGoodsTypeUseTypeList();

	GetCodeListResponseDto getOrderStatusGoodsTypeTypeCdList();

	ApiResult<?> addOrderStatusTypeAction(OrderStatusTypeActionRequestDto orderStatusTypeActionRequestDto) throws Exception;

	int putStatusNmOrderStatusDisplay(OrderStatusDisplayRequestDto orderStatusDisplayRequestDto);

	int putJsonOrderStatus(String useType);






}
