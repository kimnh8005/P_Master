package kr.co.pulmuone.v1.order.status.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
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
 * 주문상태 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 *  1.1    2020. 12. 16.			최윤지		   추가작성
 * =======================================================================
 * </PRE>
 */
@Service
public class OrderStatusMgrBizImpl implements OrderStatusMgrBiz {
    @Autowired
    private OrderStatusMgrService orderStatusService;


    @Override
    public OrderStatusSearchResponseDto getSearchCode(OrderStatusSearchRequestDto orderStatusSearchRequestDto) {
        return orderStatusService.getSearchCode(orderStatusSearchRequestDto);
    }

    /**
     * @Desc 주문상태 리스트 조회
     * @param baseRequestPageDto
     */
    @Override
    public OrderStatusListResponseDto getOrderStatusList() {
        return orderStatusService.getOrderStatusList();
    }

    /**
     * @Desc 주문상태 등록
     * @param addOrderStatusRequestDto
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addOrderStatus(OrderStatusVo orderStatusVo) throws Exception{
    	//중복체크
    	int count = orderStatusService.hasOrderStatusDuplicate(orderStatusVo.getStatusCd());

    	if(count > 0) {
    		return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
    	} else {
    		orderStatusService.addOrderStatus(orderStatusVo);
    		orderStatusService.addOrderStatusDisplayOdStatus();
    	}
    	return ApiResult.success();
    }

    /**
     * @Desc 주문상태 수정
     * @param addOrderStatusRequestDto
     * @throws Exception
     */
    @Override
    public int putOrderStatus(OrderStatusRequestDto orderStatusRequestDto) throws Exception{

    	int putInt = orderStatusService.putOrderStatus(orderStatusRequestDto);
    	if (putInt > 0) {
    		orderStatusService.putJsonOrderStatus(null); //JSON 업데이트
    	}
    	return putInt;
    }

    /**
     * @Desc 주문상태 상세조회
     * @param statusCd
     */
    @Override
    public OrderStatusResponseDto getOrderStatus(String statusCd)  {
    	return orderStatusService.getOrderStatus(statusCd);
    }

    /**
     * @Desc 주문상태유형 리스트 조회
     * @param getOrderStatusGoodsTypeListRequestDto
     */
    @Override
    public OrderStatusGoodsTypeListResponseDto getOrderStatusGoodsTypeList() {
        return orderStatusService.getOrderStatusGoodsTypeList();
    }

	/**
	 * @Desc 주문상태유형 상세조회
	 * @param typeCd
	 */
	@Override
	public OrderStatusGoodsTypeResponseDto getOrderStatusGoodsType(String typeCd) {
		return orderStatusService.getOrderStatusGoodsType(typeCd);
	}

	/**
	 * @Desc 주문상태유형 등록
	 * @param addOrderStatusGoodsTypeRequestDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addOrderStatusGoodsType(OrderStatusGoodsTypeVo orderStatusGoodsTypeVo) throws Exception{
		//중복체크
    	int count = orderStatusService.hasOrderStatusGoodsTypeDuplicate(orderStatusGoodsTypeVo.getTypeCd());

    	if(count > 0) {
    		return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
    	} else {
    		orderStatusService.addOrderStatusGoodsType(orderStatusGoodsTypeVo);
    		orderStatusService.addOrderStatusDisplayOdStatusGoodsType();
    		orderStatusService.putJsonOrderStatus(orderStatusGoodsTypeVo.getUseType()); //JSON 업데이트
    	}
		return  ApiResult.success();
	}

	/**
	 * @Desc 주문상태유형 수정
	 * @param addOrderStatusGoodsTypeRequestDto
	 * @throws Exception
	 */
	@Override
	public int putOrderStatusGoodsType(String typeNm, String typeCd, String useType) throws Exception{

		int putInt = orderStatusService.putOrderStatusGoodsType(typeNm, typeCd, useType);

		if(putInt > 0) {
    		orderStatusService.putJsonOrderStatus(useType); //JSON 업데이트
		}

		return putInt;

	}

	/**
	 * @Desc 주문상태실행 리스트 조회
	 * @param getOrderStatusActionListRequestDto
	 */
	@Override
	public OrderStatusActionListResponseDto getOrderStatusActionList(BaseRequestPageDto baseRequestPageDto) {
		return orderStatusService.getOrderStatusActionList(baseRequestPageDto);
	}

	/**
	 * @Desc 주문상태실행 상세조회
	 * @param actionId
	 */
	@Override
	public OrderStatusActionResponseDto getOrderStatusAction(Long actionId) {
		return orderStatusService.getOrderStatusAction(actionId);
	}

	/**
	 * @Desc 주문상태실행 등록
	 * @param addOrderStatusActionRequestDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addOrderStatusAction(OrderStatusActionVo orderStatusActionVo) throws Exception {
		//중복체크
    	int count = orderStatusService.hasOrderStatusActionDuplicate(orderStatusActionVo.getActionExecId());

    	if(count > 0) {
    		return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
    	} else {
    		orderStatusService.addOrderStatusAction(orderStatusActionVo);
    	}
		return  ApiResult.success();
	}

	/**
	 * @Desc 주문상태실행 수정
	 * @param addOrderStatusActionRequestDto
	 * @throws Exception
	 */
	@Override
	public int putOrderStatusAction(OrderStatusActionVo orderStatusActionVo) throws Exception {
		return orderStatusService.putOrderStatusAction(orderStatusActionVo);

	}

	/**
	 * @Desc 주문유형별 상태실행관리 리스트 조회
	 */
	@Override
	public OrderStatusTypeActionListResponseDto getOrderStatusTypeActionList(OrderStatusTypeActionSearchRequestDto orderStatusTypeActionSearchRequestDto) {
		return orderStatusService.getOrderStatusTypeActionList(orderStatusTypeActionSearchRequestDto);
	}

	/**
	 * @Desc 주문유형별 상태실행관리 삭제
	 * @param actionSeq
	 * @throws Exception
	 */
	@Override
	public int delOrderStatusTypeAction(Long actionSeq, String useType) throws Exception {

		int delInt = orderStatusService.delOrderStatusTypeAction(actionSeq, useType);

		if(delInt > 0) {
			orderStatusService.putJsonOrderStatus(useType); //JSON 업데이트
		}
		return delInt;
	}

	/**
	 * @Desc 주문상태실행 실행ID 정보조회
	 */
	@Override
	public GetCodeListResponseDto getOrderStatusActionIdList() {
		return orderStatusService.getOrderStatusActionIdList();
	}

	/**
	 * @Desc 주문상태 주문상태ID 정보조회
	 */
	@Override
	public GetCodeListResponseDto getOrderStatusStatusCdList() {
		return orderStatusService.getOrderStatusStatusCdList();
	}

	/**
	 * @Desc 주문상태유형 노출영역 정보조회
	 */
	@Override
	public GetCodeListResponseDto getOrderStatusGoodsTypeUseTypeList() {
		return orderStatusService.getOrderStatusGoodsTypeUseTypeList();
	}

	/**
	 * @Desc 주문상태유형 상품유형ID 정보조회
	 */
	@Override
	public GetCodeListResponseDto getOrderStatusGoodsTypeTypeCdList() {
		return orderStatusService.getOrderStatusGoodsTypeTypeCdList();
	}

	/**
	 * @Desc 주문유형별 상태실행관리 추가
	 * @param addOrderStatusTypeActionRequestDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addOrderStatusTypeAction(OrderStatusTypeActionRequestDto orderStatusTypeActionRequestDto) throws Exception{

		//중복체크
    	int count = orderStatusService.hasOrderStatusTypeActionDuplicate(
    			orderStatusTypeActionRequestDto.getStatusCd(),
    			orderStatusTypeActionRequestDto.getUseType(),
    			orderStatusTypeActionRequestDto.getTypeCd(),
    			orderStatusTypeActionRequestDto.getActionId(),
    			orderStatusTypeActionRequestDto.getActionNm());

    	if(count > 0) {
    		return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
    	} else {
    		orderStatusService.addOrderStatusTypeAction(orderStatusTypeActionRequestDto);
    		orderStatusService.putJsonOrderStatus(
    				orderStatusTypeActionRequestDto.getUseType()); //JSON 업데이트
    	}
		return  ApiResult.success();
	}

	/**
	 * @Desc 주문유형별 상태실행관리 노출상태관리 업데이트
	 * @param putOrderStatusDisplayRequestDto
	 */
	@Override
	public int putStatusNmOrderStatusDisplay(OrderStatusDisplayRequestDto orderStatusDisplayRequestDto) {
		int putInt = orderStatusService.putStatusNmOrderStatusDisplay(orderStatusDisplayRequestDto);

		if(putInt > 0) {
			orderStatusService.putJsonOrderStatus(orderStatusDisplayRequestDto.getUseType()); //JSON 업데이트
		}
		return putInt;
	}

	/**
	 * @Desc 주문상태 FRONT-BOS-ACTION_JSON 업데이트
	 */
	@Override
	public int putJsonOrderStatus(String useType) {
		return orderStatusService.putJsonOrderStatus(useType);
	}


}