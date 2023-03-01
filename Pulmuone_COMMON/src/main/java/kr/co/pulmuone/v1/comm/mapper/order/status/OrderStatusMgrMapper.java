package kr.co.pulmuone.v1.comm.mapper.order.status;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusDisplayRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusSearchRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusTypeActionRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusTypeActionSearchRequestDto;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusActionVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusGoodsTypeVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusTypeActionVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusVo;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상태 관련 Mapper
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
@Mapper
public interface OrderStatusMgrMapper {


	List<OrderStatusVo> getSearchCode(OrderStatusSearchRequestDto orderStatusSearchRequestDto);

	List<OrderStatusVo> getOrderStatusList();

	int addOrderStatus(OrderStatusVo orderStatusVo);

	void addOrderStatusDisplayOdStatus();

	int putOrderStatus(OrderStatusRequestDto orderStatusRequestDto);

	int putOrderStatusDisplay(OrderStatusRequestDto orderStatusRequestDto);

	int hasOrderStatusDuplicate(String statusCd);

	OrderStatusVo getOrderStatus(String statusCd);

	List<OrderStatusGoodsTypeVo> getOrderStatusGoodsTypeList();

	OrderStatusGoodsTypeVo getOrderStatusGoodsType(String typeCd);

	int addOrderStatusGoodsType(OrderStatusGoodsTypeVo orderStatusGoodsTypeVo);

	void addOrderStatusDisplayOdStatusGoodsType();

	int putOrderStatusGoodsType(@Param("typeNm") String typeNm, @Param("typeCd" )String typeCd, @Param("useType" ) String useType);

	int hasOrderStatusGoodsTypeDuplicate(String typeCd);

	Page<OrderStatusActionVo> getOrderStatusActionList();

	OrderStatusActionVo getOrderStatusAction(Long actionId);

	int addOrderStatusAction(OrderStatusActionVo orderStatusActionVo);

	int hasOrderStatusActionDuplicate(String actionExecId);

	int putOrderStatusAction(OrderStatusActionVo orderStatusActionVo);

	List<OrderStatusTypeActionVo> getOrderStatusTypeActionList(OrderStatusTypeActionSearchRequestDto orderStatusTypeActionSearchRequestDto);

	int delOrderStatusTypeAction(@Param("actionSeq") Long actionSeq, @Param("useType") String useType);

	List<GetCodeListResultVo> getOrderStatusActionIdList();

	List<GetCodeListResultVo> getOrderStatusStatusCdList();

	List<GetCodeListResultVo> getOrderStatusGoodsTypeUseTypeList();

	List<GetCodeListResultVo> getOrderStatusGoodsTypeTypeCdList();

	int addOrderStatusTypeAction(OrderStatusTypeActionRequestDto orderStatusTypeActionRequestDto);

	int hasOrderStatusTypeActionDuplicate(@Param("statusCd") String statusCd,@Param("useType") String useType,@Param("typeCd") String typeCd, @Param("actionId") Long actionId,
			@Param("actionNm") String actionNm);

	int putStatusNmOrderStatusDisplay(OrderStatusDisplayRequestDto orderStatusDisplayRequestDto);

	int putJsonOrderStatus(String useType);












}
