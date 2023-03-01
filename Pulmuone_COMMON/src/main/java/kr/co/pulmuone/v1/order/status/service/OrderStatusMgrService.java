package kr.co.pulmuone.v1.order.status.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.mapper.order.status.OrderStatusMgrMapper;
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
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusTypeActionVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusVo;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상태 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 *  1.1	   2020. 12. 16.			최윤지		   추가작성
 * =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class OrderStatusMgrService {

    private final OrderStatusMgrMapper orderStatusMgrMapper;

    public OrderStatusSearchResponseDto getSearchCode(OrderStatusSearchRequestDto orderStatusSearchRequestDto) {
        OrderStatusSearchResponseDto orderStatusSearchResponseDto = new OrderStatusSearchResponseDto();
        orderStatusSearchResponseDto.setRows(orderStatusMgrMapper.getSearchCode(orderStatusSearchRequestDto));
        return orderStatusSearchResponseDto;
    }

    /**
     * @Desc 주문상태 리스트 조회
     * @param baseRequestPageDto
     * @return GetOrderStatusListResponseDto
     */
    protected OrderStatusListResponseDto getOrderStatusList() {
    	OrderStatusListResponseDto getOrderStatusListResponseDto = new OrderStatusListResponseDto();
    	List<OrderStatusVo> rows = orderStatusMgrMapper.getOrderStatusList();

    	getOrderStatusListResponseDto.setRows(rows);

    	return getOrderStatusListResponseDto;
    }

    /**
     * @Desc 주문상태 등록
     * @param orderStatusVo
     * @throws Exception
     * @return int
     */
    protected int addOrderStatus(OrderStatusVo orderStatusVo) throws Exception{

    	if(orderStatusVo.getSearchGrp() != "") {
    		orderStatusVo
    			.setSearchGrpList(Arrays.asList(orderStatusVo.getSearchGrp().split("∀")));
    	}

		return orderStatusMgrMapper.addOrderStatus(orderStatusVo);
	}

    /**
     * @Desc 주문상태 등록 시 OD_STATUS_DISPLAY 업데이트
     * @return void
     */
    protected void addOrderStatusDisplayOdStatus() {
		orderStatusMgrMapper.addOrderStatusDisplayOdStatus();
	}

    /**
     * @Desc 주문상태 수정
     * @param addOrderStatusRequestDto
     * @return int
     */
    protected int putOrderStatus(OrderStatusRequestDto orderStatusRequestDto) {

    	if(orderStatusRequestDto.getSearchGrp() != "") {
    		orderStatusRequestDto
    			.setSearchGrpList(Arrays.asList(orderStatusRequestDto.getSearchGrp().split("∀")));
    	}

    	//update();

		return orderStatusMgrMapper.putOrderStatus(orderStatusRequestDto);
	}

    /**
     * @Desc 주문상태 상세조회
     * @param statusCd
     * @return GetOrderStatusResponseDto
     */
    protected OrderStatusResponseDto getOrderStatus(String statusCd) {

    	OrderStatusResponseDto orderStatusResponseDto = new OrderStatusResponseDto();
    	OrderStatusVo orderStatusVo =  orderStatusMgrMapper.getOrderStatus(statusCd);
    	orderStatusResponseDto.setRows(orderStatusVo);
    	return orderStatusResponseDto;
	}

    /**
     * @Desc 주문상태유형 리스트 조회
     * @param getOrderStatusGoodsTypeListRequestDto
     * @return GetOrderStatusGoodsTypeListResponseDto
     */
    protected OrderStatusGoodsTypeListResponseDto getOrderStatusGoodsTypeList() {
		OrderStatusGoodsTypeListResponseDto orderStatusGoodsTypeListResponseDto = new OrderStatusGoodsTypeListResponseDto();
		List<OrderStatusGoodsTypeVo> rows = orderStatusMgrMapper.getOrderStatusGoodsTypeList();
		orderStatusGoodsTypeListResponseDto.setRows(rows);

		return orderStatusGoodsTypeListResponseDto;
	}

    /**
     * @Desc 주문상태유형 상세조회
     * @param typeCd
     * @return GetOrderStatusGoodsTypeVo
     */
    protected OrderStatusGoodsTypeResponseDto getOrderStatusGoodsType(String typeCd) {
    	OrderStatusGoodsTypeResponseDto orderStatusGoodsTypeResponseDto = new OrderStatusGoodsTypeResponseDto();
		OrderStatusGoodsTypeVo orderStatusGoodsTypeVo = orderStatusMgrMapper.getOrderStatusGoodsType(typeCd);
		orderStatusGoodsTypeResponseDto.setRows(orderStatusGoodsTypeVo);

		return orderStatusGoodsTypeResponseDto;
	}

    /**
     * @Desc 주문상태유형 등록
     * @param orderStatusGoodsTypeVo
     * @return int
     */
    protected int addOrderStatusGoodsType(OrderStatusGoodsTypeVo orderStatusGoodsTypeVo) {

    	//update(); for문

		return orderStatusMgrMapper.addOrderStatusGoodsType(orderStatusGoodsTypeVo);
	}

    /**
     * @Desc 주문상태유형 등록 시 OD_STATUS_DISPLAY 업데이트
     * @return void
     */
    protected void addOrderStatusDisplayOdStatusGoodsType() {
		orderStatusMgrMapper.addOrderStatusDisplayOdStatusGoodsType();
	}

    /**
     * @Desc 주문상태유형 수정
     * @param addOrderStatusGoodsTypeRequestDto
     * @return int
     */
    protected int putOrderStatusGoodsType(String typeNm, String typeCd, String useType) {

    	//update(); for문

		return orderStatusMgrMapper.putOrderStatusGoodsType(typeNm, typeCd, useType);
	}

    /**
     * @Desc 주문상태 중복체크
     * @param statusCd
     * @return int
     */
    protected int hasOrderStatusDuplicate(String statusCd) {
		return orderStatusMgrMapper.hasOrderStatusDuplicate(statusCd);
	}

    /**
     * @Desc 주문상태유형 중복체크
     * @param typeCd
     * @return int
     */
    protected int hasOrderStatusGoodsTypeDuplicate(String typeCd) {
		return orderStatusMgrMapper.hasOrderStatusGoodsTypeDuplicate(typeCd);
	}

    /**
     * @Desc 주문상태실행 리스트 조회
     * @param baseRequestPageDto
     * @return GetOrderStatusActionListResponseDto
     */
    protected OrderStatusActionListResponseDto getOrderStatusActionList(BaseRequestPageDto baseRequestPageDto) {
		OrderStatusActionListResponseDto orderStatusActionListResponseDto = new OrderStatusActionListResponseDto();

    	PageMethod.startPage(baseRequestPageDto.getPage(), baseRequestPageDto.getPageSize());

    	Page<OrderStatusActionVo> rows = orderStatusMgrMapper.getOrderStatusActionList();

    	orderStatusActionListResponseDto.setTotal((int)rows.getTotal());
    	orderStatusActionListResponseDto.setRows(rows.getResult());
    	return orderStatusActionListResponseDto;
	}

    /**
     * @Desc 주문상태실행 상세조회
     * @param actionId
     * @return GetOrderStatusActionResponseDto
     */
    protected OrderStatusActionResponseDto getOrderStatusAction(Long actionId) {
    	OrderStatusActionResponseDto orderStatusActionResponseDto = new OrderStatusActionResponseDto();
		OrderStatusActionVo orderStatusActionVo = orderStatusMgrMapper.getOrderStatusAction(actionId);
		orderStatusActionResponseDto.setRows(orderStatusActionVo);

		return orderStatusActionResponseDto;
	}

    /**
     * @Desc 주문상태실행 등록
     * @param orderStatusActionVo
     * @return int
     */
    protected int addOrderStatusAction(OrderStatusActionVo orderStatusActionVo) {
		return orderStatusMgrMapper.addOrderStatusAction(orderStatusActionVo);

	}

    /**
     * @Desc 주문상태실행 중복체크
     * @param actionExecId
     * @return int
     */
    protected int hasOrderStatusActionDuplicate(String actionExecId) {
		return orderStatusMgrMapper.hasOrderStatusActionDuplicate(actionExecId);
	}

    /**
     * @Desc 주문상태실행 수정
     * @param orderStatusActionVo
     * @return int
     */
    protected int putOrderStatusAction(OrderStatusActionVo orderStatusActionVo) {
		return orderStatusMgrMapper.putOrderStatusAction(orderStatusActionVo);
	}

    /**
     * @Desc 주문유형별 상태실행관리 리스트 조회
     * @return GetOrderStatusTypeActionListResponseDto
     */
    protected OrderStatusTypeActionListResponseDto getOrderStatusTypeActionList(OrderStatusTypeActionSearchRequestDto orderStatusTypeActionSearchRequestDto) {
    	OrderStatusTypeActionListResponseDto orderStatusTypeActionListResponseDto = new OrderStatusTypeActionListResponseDto();

    	List<OrderStatusTypeActionVo> rows = orderStatusMgrMapper.getOrderStatusTypeActionList(orderStatusTypeActionSearchRequestDto);

    	orderStatusTypeActionListResponseDto.setRows(rows);
		return orderStatusTypeActionListResponseDto;
	}

    /**
     * @Desc 주문유형별 상태실행관리 삭제
     * @param actionSeq
     * @param useType
     * @return int
     */
    protected int delOrderStatusTypeAction(Long actionSeq, String useType) {
		return orderStatusMgrMapper.delOrderStatusTypeAction(actionSeq, useType);
	}

    /**
     * @Desc 주문상태실행 실행ID 정보조회
     * @return GetCodeListResponseDto
     */
    protected GetCodeListResponseDto getOrderStatusActionIdList() {
    	GetCodeListResponseDto getOrderStatusActionIdListResponseDto = new GetCodeListResponseDto();

		List<GetCodeListResultVo> rows = orderStatusMgrMapper.getOrderStatusActionIdList();

		getOrderStatusActionIdListResponseDto.setRows(rows);
		return getOrderStatusActionIdListResponseDto;
	}

	/**
	 * @Desc 주문상태 주문상태ID 정보조회
	 * @return GetCodeListResponseDto
	 */
	protected GetCodeListResponseDto getOrderStatusStatusCdList() {
		GetCodeListResponseDto getOrderStatusStatusCdListResponseDto = new GetCodeListResponseDto();

		List<GetCodeListResultVo> rows = orderStatusMgrMapper.getOrderStatusStatusCdList();

		getOrderStatusStatusCdListResponseDto.setRows(rows);
		return getOrderStatusStatusCdListResponseDto;
	}

	/**
	 * @Desc 주문상태유형 노출영역 정보조회
	 * @return GetCodeListResponseDto
	 */
	protected GetCodeListResponseDto getOrderStatusGoodsTypeUseTypeList() {
		GetCodeListResponseDto getOrderStatusGoodsTypeUseTypeListResponseDto = new GetCodeListResponseDto();

		List<GetCodeListResultVo> rows = orderStatusMgrMapper.getOrderStatusGoodsTypeUseTypeList();

		getOrderStatusGoodsTypeUseTypeListResponseDto.setRows(rows);
		return getOrderStatusGoodsTypeUseTypeListResponseDto;
	}

	/**
	 * @Desc 주문상태유형 상품유형ID 정보조회
	 * @return GetCodeListResponseDto
	 */
	protected GetCodeListResponseDto getOrderStatusGoodsTypeTypeCdList() {
		GetCodeListResponseDto getOrderStatusGoodsTypeTypeCdListResponseDto = new GetCodeListResponseDto();

		List<GetCodeListResultVo> rows = orderStatusMgrMapper.getOrderStatusGoodsTypeTypeCdList();

		getOrderStatusGoodsTypeTypeCdListResponseDto.setRows(rows);
		return getOrderStatusGoodsTypeTypeCdListResponseDto;
	}

    /**
     * @Desc 주문유형별 상태실행관리 추가
     * @param addOrderStatusTypeActionRequestDto
     * @return int
     */
    protected int addOrderStatusTypeAction(OrderStatusTypeActionRequestDto orderStatusTypeActionRequestDto) {

    	//update(); for문

		return orderStatusMgrMapper.addOrderStatusTypeAction(orderStatusTypeActionRequestDto);

	}

    /**
     * @Desc 주문유형별 상태실행관리 중복체크
     * @param statusCd
     * @param useType
     * @param typeCd
     * @param actionId
     * @param actionNm
     * @return int
     */
    protected int hasOrderStatusTypeActionDuplicate(String statusCd, String useType, String typeCd, Long actionId,
			String actionNm) {
		return orderStatusMgrMapper.hasOrderStatusTypeActionDuplicate(statusCd,useType,typeCd,actionId,actionNm);
	}

    /**
     * @Desc  주문유형별 상태실행관리 노출상태관리 업데이트
     * @param putOrderStatusDisplayRequestDto
     * @return int
     */
    protected int putStatusNmOrderStatusDisplay(OrderStatusDisplayRequestDto orderStatusDisplayRequestDto) {

    	//update(); for문

		return orderStatusMgrMapper.putStatusNmOrderStatusDisplay(orderStatusDisplayRequestDto);
    }

	/**
	 * @param actionSeq
	 * @param actionNm
	 * @param useType
	 * @Desc 주문상태 FRONT-BOS-ACTION_JSON 업데이트
	 * @return int
	 */
	protected int putJsonOrderStatus(String useType) {
		return orderStatusMgrMapper.putJsonOrderStatus(useType);
	}




}