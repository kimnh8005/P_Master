package kr.co.pulmuone.bos.order.status;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusDisplayRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusSearchRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusTypeActionRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusTypeActionSearchRequestDto;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusActionVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusGoodsTypeVo;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusVo;
import kr.co.pulmuone.v1.order.status.service.OrderStatusMgrBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 주문상 관련 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 14.            이명수         최초작성
 *  1.1	   2020. 12. 16.			최윤지 		   추가작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class OrderStatusMgrController {

    @Autowired
    private OrderStatusMgrBiz orderStatusMgrBiz;

    @Autowired(required=true)
    private HttpServletRequest request;

    /**
     * 주문 상태 검색 조회
     *
     * @param orderStatusSearchRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문 상태 검색 조회")
    @GetMapping(value = "/admin/statusMgr/getSearchCode")
    public ApiResult<?> getSearchCode(OrderStatusSearchRequestDto orderStatusSearchRequestDto) throws Exception {
        return ApiResult.success(orderStatusMgrBiz.getSearchCode(orderStatusSearchRequestDto));
    }

    /**
     * @Desc 주문상태 리스트 조회
     * @param getOrderStatusListRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태 리스트 조회")
    @PostMapping(value = "/admin/order/statusMgr/getOrderStatusList")
    public ApiResult<?> getOrderStatusList() throws Exception{
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatusList());
    }

    /**
     * @Desc 주문상태 등록
     * @param addOrderStatusRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태 등록")
    @PostMapping(value = "/admin/order/statusMgr/addOrderStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
      })
    public ApiResult<?> addOrderStatus(OrderStatusVo orderStatusVo) throws Exception{

        return ApiResult.success(orderStatusMgrBiz.addOrderStatus(orderStatusVo));

    }

    /**
     * @Desc 주문상태 수정
     * @param addOrderStatusRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태 수정")
    @PostMapping(value = "/admin/order/statusMgr/putOrderStatus")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
      })
    public ApiResult<?> purOrderStatus(OrderStatusRequestDto orderStatusRequestDto) throws Exception {

    	return ApiResult.success(orderStatusMgrBiz.putOrderStatus(orderStatusRequestDto));
    }

    /**
     * @Desc 주문상태 상세조회
     * @param statusCd
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태 상세조회")
    @PostMapping(value = "/admin/order/statusMgr/getOrderStatus")
    public ApiResult<?> getOrderStatus(String statusCd) {
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatus(statusCd));
    }

    /**
     * @Desc 주문상태유형 리스트 조회
     * @param getOrderStatusGoodsTypeListRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태유형 리스트 조회")
    @PostMapping(value = "/admin/order/statusMgr/getOrderStatusGoodsTypeList")
    public ApiResult<?> getOrderStatusGoodsTypeList() throws Exception{
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatusGoodsTypeList());
    }

    /**
     * @Desc 주문상태유형 상세조회
     * @param typeCd
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태유형 상세조회")
    @PostMapping(value = "/admin/order/statusMgr/getOrderStatusGoodsType")
    public ApiResult<?> getOrderStatusGoodsType(String typeCd){
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatusGoodsType(typeCd));
    }

    /**
     * @Desc 주문상태유형 등록
     * @param addOrderStatusGoodsTypeRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태유형 등록")
    @PostMapping(value = "/admin/order/statusMgr/addOrderStatusGoodsType")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
      })
    public ApiResult<?> addOrderStatusGoodsType(OrderStatusGoodsTypeVo orderStatusGoodsTypeVo) throws Exception{

        return ApiResult.success(orderStatusMgrBiz.addOrderStatusGoodsType(orderStatusGoodsTypeVo));

    }

    /**
     * @Desc 주문상태유형 수정
     * @param addOrderStatusGoodsTypeRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태유형 수정")
    @PostMapping(value = "/admin/order/statusMgr/putOrderStatusGoodsType")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
      })
    public ApiResult<?> putOrderStatusGoodsType(String typeNm, String typeCd, String useType) throws Exception{

    	return ApiResult.success(orderStatusMgrBiz.putOrderStatusGoodsType(typeNm, typeCd, useType));

    }

    /**
     * @Desc 주문상태실행 리스트 조회
     * @param getOrderStatusActionListRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value="주문상태실행 리스트 조회")
    @PostMapping(value="/admin/order/statusMgr/getOrderStatusActionList")
    public ApiResult<?> getOrderStatusActionList()  throws Exception{
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatusActionList(BindUtil.bindDto(request,BaseRequestPageDto.class)));
    }

    /**
     * @Desc 주문상태실행 상세조회
     * @param actionId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태실행 상세조회")
    @PostMapping(value = "/admin/order/statusMgr/getOrderStatusAction")
    public ApiResult<?> getOrderStatusAction(Long actionId) {
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatusAction(actionId));
    }

    /**
     * @Desc 주문상태실행 등록
     * @param addOrderStatusActionRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태실행 등록")
    @PostMapping(value = "/admin/order/statusMgr/addOrderStatusAction")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
      })
    public ApiResult<?> addOrderStatusAction(OrderStatusActionVo orderStatusActionVo) throws Exception{
        return ApiResult.success(orderStatusMgrBiz.addOrderStatusAction(orderStatusActionVo));

    }

    /**
     * @Desc 주문상태실행 수정
     * @param addOrderStatusActionRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문상태실행 수정")
    @PostMapping(value = "/admin/order/statusMgr/putOrderStatusAction")
    @ApiResponses(value = {
    		@ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
    public ApiResult<?> putOrderStatusAction(OrderStatusActionVo orderStatusActionVo) throws Exception{
    	return ApiResult.success(orderStatusMgrBiz.putOrderStatusAction(orderStatusActionVo));

    }

    /**
     * @Desc 주문유형별 상태실행관리 리스트 조회
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value="주문유형별 상태실행관리 리스트 조회")
    @PostMapping(value="/admin/order/statusMgr/getOrderStatusTypeActionList")
    public ApiResult<?> getOrderStatusTypeActionList(HttpServletRequest request, OrderStatusTypeActionSearchRequestDto orderStatusTypeActionSearchRequestDto) throws Exception {
    	return  ApiResult.success(orderStatusMgrBiz.getOrderStatusTypeActionList(BindUtil.bindDto(request, OrderStatusTypeActionSearchRequestDto.class)));
    }

    /**
     * @Desc 주문유형별 상태실행관리 삭제
     * @param actionSeq
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value="주문유형별 상태실행관리 삭제")
    @PostMapping(value="/admin/order/statusMgr/delOrderStatusTypeAction")
    public ApiResult<?> delOrderStatusTypeAction(Long actionSeq, String useType) throws Exception {
    	return  ApiResult.success(orderStatusMgrBiz.delOrderStatusTypeAction(actionSeq, useType));
    }

    /**
     * @Desc 주문상태실행 실행ID 정보조회
     * @return ApiResult<?>
     */
    @ApiOperation(value="주문상태실행 실행ID 정보조회")
    @GetMapping(value="/admin/order/statusMgr/getOrderStatusActionIdList")
    public ApiResult<?> getOrderStatusActionIdList() {
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatusActionIdList());
    }

    /**
     * @Desc 주문상태 주문상태ID 정보조회
     * @return ApiResult<?>
     */
    @ApiOperation(value="주문상태 주문상태ID 정보조회")
    @GetMapping(value="/admin/order/statusMgr/getOrderStatusStatusCdList")
    public ApiResult<?> getOrderStatusStatusCdList() {
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatusStatusCdList());
    }

    /**
     * @Desc 주문상태유형 노출영역 정보조회
     * @return ApiResult<?>
     */
    @ApiOperation(value="주문상태유형 노출영역 정보조회")
    @GetMapping(value="/admin/order/statusMgr/getOrderStatusGoodsTypeUseTypeList")
    public ApiResult<?> getOrderStatusGoodsTypeUseTypeList() {
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatusGoodsTypeUseTypeList());
    }

    /**
     * @Desc 주문상태유형 상품유형ID 정보조회
     * @return ApiResult<?>
     */
    @ApiOperation(value="주문상태유형 상품유형ID 정보조회")
    @GetMapping(value="/admin/order/statusMgr/getOrderStatusGoodsTypeTypeCdList")
    public ApiResult<?> getOrderStatusGoodsTypeTypeCdList() {
    	return ApiResult.success(orderStatusMgrBiz.getOrderStatusGoodsTypeTypeCdList());
    }

    /**
     * @Desc 주문유형별 상태실행관리 추가
     * @param addOrderStatusTypeActionRequestDto
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value="주문유형별 상태실행관리 추가")
    @PostMapping(value="/admin/order/statusMgr/addOrderStatusTypeAction")
    @ApiResponses(value = {
    		@ApiResponse(code = 901, message = "" + "DUPLICATE_DATA - 중복된 데이터가 존재합니다.")
    })
    public ApiResult<?> addOrderStatusTypeAction(OrderStatusTypeActionRequestDto orderStatusTypeActionRequestDto) throws Exception {
    	return ApiResult.success(orderStatusMgrBiz.addOrderStatusTypeAction(orderStatusTypeActionRequestDto));
    }

    /**
     * @Desc 주문유형별 상태실행관리 노출상태관리 업데이트
     * @param putOrderStatusDisplayRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value="주문유형별 상태실행관리 노출상태관리 업데이트")
    @PostMapping(value="/admin/order/statusMgr/putStatusNmOrderStatusDisplay")
    public ApiResult<?> putStatusNmOrderStatusDisplay(OrderStatusDisplayRequestDto orderStatusDisplayRequestDto) throws Exception {
    	return ApiResult.success(orderStatusMgrBiz.putStatusNmOrderStatusDisplay(orderStatusDisplayRequestDto));
    }

}
