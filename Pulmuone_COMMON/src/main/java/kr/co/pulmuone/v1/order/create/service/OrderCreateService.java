package kr.co.pulmuone.v1.order.create.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.mapper.order.create.OrderCreateMapper;
import kr.co.pulmuone.v1.comm.mapper.order.status.OrderStatusMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemWarehouseVo;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelResponseDto;
import kr.co.pulmuone.v1.order.create.dto.OrderInfoDto;
import kr.co.pulmuone.v1.order.create.dto.PaymentInfoDto;
import kr.co.pulmuone.v1.order.create.dto.UserGroupInfoDto;
import kr.co.pulmuone.v1.order.create.dto.vo.CreateInfoVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.create.dto.CreateInfoDto;
import kr.co.pulmuone.v1.order.create.dto.GoodsInfoDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCardPayRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateListRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelRequestDto;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateDto;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoAdditionalGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartListGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartAddGoodsVo;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class OrderCreateService {
    private final OrderCreateMapper orderCreateMapper;
    private static final String ORDER_CREATE_DIR_INFO 			= "PC";		// 디바이스 정보
    private static final boolean ORDER_CREATE_IS_APP 			= false;	// 앱 여부
    private static final boolean ORDER_CREATE_IS_EMPLOYEE 		= false;	// 임직원 여부

    @Autowired
    private ShoppingCartBiz shoppingCartBiz;

    @Autowired
    private GoodsGoodsBiz goodsGoodsBiz;

    @Autowired
    private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

    /**
     * @Desc 엑셀업로드시 상품 정보 조회 목록
     * @param orderExcelRequestDto
     * @return
     * @throws Exception
     */
    protected List<OrderExcelResponseDto> getExcelUploadList(OrderExcelRequestDto orderExcelRequestDto) throws Exception {
        List<OrderExcelResponseDto> excelUploadList = orderCreateMapper.getExcelUploadList(orderExcelRequestDto);

        Map<String, Map<String, List<OrderExcelResponseDto>>> shippingZoneMap = excelUploadList.stream()
                // 수취인명, 수취인 연락처, 우편번호, 주소1, 주소2로 그룹
                .collect(groupingBy(OrderExcelResponseDto::getKey, LinkedHashMap::new,
                        // 출고처, 배송정책으로 그룹
                        groupingBy(OrderExcelResponseDto::getGrpShippingId, LinkedHashMap::new, toList())));

        shippingZoneMap.entrySet().forEach(shippingZoneEntry ->{
            Map<String, List<OrderExcelResponseDto>> shippingPriceMap = shippingZoneEntry.getValue();

            shippingPriceMap.entrySet().forEach(shippingPriceEntry ->{
                List<OrderExcelResponseDto> goodsList = shippingPriceEntry.getValue();
                OrderExcelResponseDto goodsDto = goodsList.get(0);
                int shippingPrice = 0;

                ShippingDataResultVo subShippingDataResultVo = null;
                try {
                    subShippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(Long.parseLong(goodsDto.getIlShippingTmplId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ShippingPriceResponseDto subShippingPriceDto = null;
                if (subShippingDataResultVo != null) {
                    try {
                        subShippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(subShippingDataResultVo, StringUtil.nvlInt(goodsDto.getOrderAmt()), Integer.parseInt(goodsDto.getOrderCnt()), goodsDto.getRecvZipCd());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    shippingPrice = subShippingPriceDto.getShippingPrice();
                }

                int finalShippingPrice = shippingPrice;
                shippingPriceEntry.getValue().stream().forEach(f->f.setShippingPrice(finalShippingPrice));

            });

        });

        return excelUploadList;
    }

    /**
     * @Desc 주문생성 시 상품 정보 조회 목록
     * @param ilGoodsId
     * @return
     * @throws Exception
     */
    protected GoodsInfoDto getGoodsInfo(long ilGoodsId) throws Exception {
    	return orderCreateMapper.getGoodsInfo(ilGoodsId);
    }

    /**
     * @Desc 주문생성 시 회원 그룹정보 조회
     * @param urUserId
     * @return
     * @throws Exception
     */
    protected UserGroupInfoDto getUserGroupInfo(long urUserId) throws Exception {
        return orderCreateMapper.getUserGroupInfo(urUserId);
    }

    /**
     * @Desc 주문생성 테이블 입력
     * @param createInfoVo
     * @return
     * @throws Exception
     */
    protected int addCreateInfo(CreateInfoVo createInfoVo) throws Exception {
    	return orderCreateMapper.addCreateInfo(createInfoVo);
    }

    /**
     * @Desc 개별생성일 때 주문 마스터 정보를 다시 업데이트 한다.
     * @param odIdList
     * @return
     * @throws Exception
     */
    protected int putOrderInfo(OrderCreateRequestDto orderCreateRequestDto) throws Exception {
    	return orderCreateMapper.putOrderInfo(orderCreateRequestDto);
    }

    /**
     * 주문생성 내역 조회
     * @param orderCreateListRequestDto
     * @return
     * @throws Exception
     */
    protected Page<CreateInfoDto> getOrderCreateList(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
       	PageMethod.startPage(orderCreateListRequestDto.getPage(), orderCreateListRequestDto.getPageSize());
        return orderCreateMapper.getOrderCreateList(orderCreateListRequestDto);
    }

    /**
     * 주문생성 삭제
     * @param orderCreateListRequestDto
     * @return
     * @throws Exception
     */
    protected int deleteOrderCreateInfo(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
    	return orderCreateMapper.deleteOrderCreateInfo(orderCreateListRequestDto);
    }

    /**
     * 주문 삭제
     * @param orderCreateListRequestDto
     * @return
     * @throws Exception
     */
    protected int deleteOrder(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
    	return orderCreateMapper.deleteOrder(orderCreateListRequestDto);
    }

    /**
     * 주문날짜 삭제
     * @param orderCreateListRequestDto
     * @return
     * @throws Exception
     */
    protected int deleteOrderDt(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
    	return orderCreateMapper.deleteOrderDt(orderCreateListRequestDto);
    }

    /**
     * 배송지 삭제
     * @param orderCreateListRequestDto
     * @return
     * @throws Exception
     */
    protected int deleteShippingZone(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
    	return orderCreateMapper.deleteShippingZone(orderCreateListRequestDto);
    }

    /**
     * 배송지 이력 삭제
     * @param orderCreateListRequestDto
     * @return
     * @throws Exception
     */
    protected int deleteShippingZoneHist(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
    	return orderCreateMapper.deleteShippingZoneHist(orderCreateListRequestDto);
    }

    /**
     * 배송비 삭제
     * @param orderCreateListRequestDto
     * @return
     * @throws Exception
     */
    protected int deleteShippingPrice(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
    	return orderCreateMapper.deleteShippingPrice(orderCreateListRequestDto);
    }

    /**
     * 주문상세 삭제
     * @param orderCreateListRequestDto
     * @return
     * @throws Exception
     */
    protected int deleteOrderDetl(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
    	return orderCreateMapper.deleteOrderDetl(orderCreateListRequestDto);
    }

    /**
     * 신용카드 비인증 주문정보 조회
     * @param OrderCardPayRequestDto
     * @return
     * @throws Exception
     */
    protected OrderInfoDto getOrderInfo(OrderCardPayRequestDto OrderCardPayRequestDto) throws Exception {
    	return orderCreateMapper.getOrderInfo(OrderCardPayRequestDto);
    }

    /**
     * 신용카드 비인증 주문결제 정보 조회
     * @param odPaymentMasterId
     * @return
     * @throws Exception
     */
    protected PaymentInfoDto getPaymentInfo(long odPaymentMasterId) throws Exception {
    	return orderCreateMapper.getPaymentInfo(odPaymentMasterId);
    }

    /**
     * 주문결졔 마스터 신용카드,무통장 업데이트
     * @param OrderPaymentMasterVo
     * @return
     * @throws Exception
     */
    protected int putPaymentMasterInfo(OrderPaymentMasterVo OrderPaymentMasterVo) throws Exception {
    	return orderCreateMapper.putPaymentMasterInfo(OrderPaymentMasterVo);
    }

    /**
     * 주문연동 매출만연동시 결제완료시 처리
     * @param odOrderId
     */
    protected void orderCopySalIfExecute(long odOrderId, String orderCopySalIfYn, String orderCopyOdid) throws Exception  {
        // 주문연동방법 매출만 연동 선택시 & 신용카드결제
        // 배송중일자 입력


        UserVo userVo = null;
        try {
            userVo = SessionUtil.getBosUserVO();
        } catch (Exception e){

        }

        String createId = "0";
        if (userVo != null) {
            createId = userVo.getUserId();
        } else {
            BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
            if (buyerVo != null) {
                createId = StringUtil.nvl(buyerVo.getUrUserId(), "0");
            }
        }

        // 주문상세정보 - 주문상세PK, 주문상태코드 수정
        String orderStatusCd = "";
        List<OrderStatusUpdateDto> orderStatusUpdateList = new ArrayList<>();
        List<String> list = orderCreateMapper.selectOrderDetailDcList(odOrderId);


        // 매출만연동시
        if ("Y".equals(orderCopySalIfYn)) {
            //List<OrderDetlVo> list = orderStatusMapper.selectOrderDetailDcList(odOrderId);
            if (StringUtil.isNotEmpty(list)) {
                for (String item : list) {
                    // 주문상세 - 정상주문상태 (배송완료) 업데이트
                    orderStatusCd = OrderEnums.OrderStatus.DELIVERY_ING.getCode();
                    OrderStatusUpdateDto orderStatusUpdateDIDto = new OrderStatusUpdateDto();
                    orderStatusUpdateDIDto.setOdOrderDetlId(Long.parseLong(item));
                    orderStatusUpdateDIDto.setOrderStatusCd(orderStatusCd);
                    orderStatusUpdateDIDto.setHistMsg("주문복사 매출연동 배송중 처리");
                    orderStatusUpdateDIDto.setDiId(Long.parseLong(createId));
                    orderStatusUpdateList.add(orderStatusUpdateDIDto);

                    orderStatusCd = OrderEnums.OrderStatus.DELIVERY_COMPLETE.getCode();
                    OrderStatusUpdateDto orderStatusUpdateDCDto = new OrderStatusUpdateDto();
                    orderStatusUpdateDCDto.setOdOrderDetlId(Long.parseLong(item));
                    orderStatusUpdateDCDto.setOrderStatusCd(orderStatusCd);
                    orderStatusUpdateDCDto.setHistMsg("주문복사 매출연동 배송완료 처리");
                    orderStatusUpdateDCDto.setDcId(Long.parseLong(createId));
                    orderStatusUpdateList.add(orderStatusUpdateDCDto);

                    // 주문상세 - 정상주문상태 (구매확정) 업데이트
                    orderStatusCd = OrderEnums.OrderStatus.BUY_FINALIZED.getCode();
                    OrderStatusUpdateDto orderStatusUpdateBFDto = new OrderStatusUpdateDto();
                    orderStatusUpdateBFDto.setOdOrderDetlId(Long.parseLong(item));
                    orderStatusUpdateBFDto.setOrderStatusCd(orderStatusCd);
                    orderStatusUpdateBFDto.setHistMsg("주문복사 매출연동 구매확정 처리");
                    orderStatusUpdateBFDto.setBfId(Long.parseLong(createId));
                    orderStatusUpdateList.add(orderStatusUpdateBFDto);

                }



                //orderStatusBiz.putOrderDetailStatus(orderStatusUpdateList);
                int cnt = 0;
                for (OrderStatusUpdateDto orderStatusUpdateDto : orderStatusUpdateList) {
                    OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
                            .odOrderDetlId(orderStatusUpdateDto.getOdOrderDetlId())    // 주문상세PK
                            .statusCd(orderStatusUpdateDto.getOrderStatusCd())        // 변경상태값
                            .histMsg(orderStatusUpdateDto.getHistMsg())        // 처리이력내용
                            .createId(orderStatusUpdateDto.getUserId())    // 등록자
                            .build();
                    // 주문상세상태 이력 등록 (테이블 : OD_ORDER_DETL_HIST)
                    // 일일배송상품 스케쥴 변경 시 "원스케쥴정보" >> "변경스케쥴정보" 확인
                    //orderStatusService.putOrderDetailStatusHist(orderDetlHistVo);
                    orderCreateMapper.addOrderDetailStatusHistory(orderDetlHistVo);
                    cnt += orderCreateMapper.putOrderDetailStatus(orderStatusUpdateDto);
                }
            }
        }

        // 송장번호 처리
        if (StringUtil.isNotEmpty(orderCopyOdid)) {
            if (StringUtil.isNotEmpty(list)) {
                for (String item : list) {
                    orderCreateMapper.addOrderCopyTrackingNumber(Long.parseLong(item), orderCopyOdid, createId);
                }
            }
        }
    }

    public void putOrderFail(Long odOrderId) {
        orderCreateMapper.putOrderFail(odOrderId);
    }

    public void putOrderSuccess(Long odPaymentMasterId) {
        orderCreateMapper.putOrderSuccess(odPaymentMasterId);
    }

    public ItemWarehouseVo getIlItemWarehouseIdByIlGoodsId(Long ilGoodsId){
        return orderCreateMapper.getIlItemWarehouseIdByIlGoodsId(ilGoodsId);
    }

    /**
     * 장바구니 추가시 추가상품 있을 경우
     * @param spCartId
     * @return
     * @throws Exception
     */
    public ApiResult<?> getPutCartInfoAdditionalGoods(long spCartId, List<AddCartListGoodsRequestDto> additionalGoodsList, boolean isMember) throws Exception{
        List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsRequestDtoList = new ArrayList<>();

        //기존 장바구니에 추가상품이 있는 경우 -> 기존 추가상품 삭제 안되게 하기 위해
        List<SpCartAddGoodsVo> addVoList = shoppingCartBiz.getCartAddGoodsIdList(spCartId);
        if(CollectionUtils.isNotEmpty(addVoList)){
            for(SpCartAddGoodsVo vo : addVoList){
                AddCartInfoAdditionalGoodsRequestDto additionalGoodsDto = new AddCartInfoAdditionalGoodsRequestDto();
                additionalGoodsDto.setIlGoodsId(vo.getIlGoodsId());
                additionalGoodsDto.setQty(vo.getQty());
                additionalGoodsRequestDtoList.add(additionalGoodsDto);
            }
        }

        // 장바구니에 추가하려는 상품이 추가구성상품일 때 && 기존 장바구니에 해당 추가상품이 없을때
        if (additionalGoodsList != null && additionalGoodsList.size() > 0) {
            for (AddCartListGoodsRequestDto additionalGoodsRequestDto : additionalGoodsList) {

                // 기존 장바구니에 해당 추가상품 존재 여부 확인
                boolean isExistAdditionalGoods = addVoList.stream().anyMatch(f->f.getIlGoodsId().equals(additionalGoodsRequestDto.getIlGoodsId()));
                if(!isExistAdditionalGoods){

                    GoodsRequestDto addGoodsRequestDto = GoodsRequestDto.builder()
                            .ilGoodsId(additionalGoodsRequestDto.getIlGoodsId()).deviceInfo(ORDER_CREATE_DIR_INFO).isApp(ORDER_CREATE_IS_APP)
                            .isMember(isMember).isEmployee(ORDER_CREATE_IS_EMPLOYEE).build();

                    BasicSelectGoodsVo addGoods = goodsGoodsBiz.getGoodsBasicInfo(addGoodsRequestDto);
                    // 추가 구성 상품이 없을 경우
                    if (addGoods == null) {
                        return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS);
                    } else {
                        // 추가 상품이 판매중이 아닐때
                        if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(addGoods.getSaleStatus())) {
                            return ApiResult.result(ShoppingEnums.AddCartInfo.NO_ADD_GOODS_STATUS_ON_SALE);
                        }

                        // 추가 상품 재고가 구매 수량보다 작을경우
                        if (addGoods.getStockQty() < additionalGoodsRequestDto.getQty()) {
                            return ApiResult.result(ShoppingEnums.AddCartInfo.ADD_GOODS_LACK_STOCK);
                        }
                    }

                    AddCartInfoAdditionalGoodsRequestDto additionalGoodsDto = new AddCartInfoAdditionalGoodsRequestDto();
                    additionalGoodsDto.setIlGoodsId(additionalGoodsRequestDto.getIlGoodsId());
                    additionalGoodsDto.setQty(additionalGoodsRequestDto.getQty());
                    additionalGoodsRequestDtoList.add(additionalGoodsDto);
                    
                }else{
                    // 기존 장바구니에 상품 있는 경우-> 넘어온 값으로 수량만 변경
                    additionalGoodsRequestDtoList.stream()
                            .filter(f->f.getIlGoodsId().equals(additionalGoodsRequestDto.getIlGoodsId()))
                            .peek(p -> p.setQty(additionalGoodsRequestDto.getQty()))
                            .collect(Collectors.toList());
                }
            }
        }
        return ApiResult.success(additionalGoodsRequestDtoList);
    }
}


