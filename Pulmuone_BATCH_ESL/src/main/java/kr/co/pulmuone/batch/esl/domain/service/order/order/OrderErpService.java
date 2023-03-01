package kr.co.pulmuone.batch.esl.domain.service.order.order;

import kr.co.pulmuone.batch.esl.common.Constants;
import kr.co.pulmuone.batch.esl.common.enums.*;
import kr.co.pulmuone.batch.esl.common.util.StringUtil;
import kr.co.pulmuone.batch.esl.domain.model.order.order.dto.EatsslimOrderHeaderDto;
import kr.co.pulmuone.batch.esl.domain.model.order.order.dto.vo.EatsslimOrderListVo;
import kr.co.pulmuone.batch.esl.domain.model.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.batch.esl.infra.mapper.order.master.OrderErpMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderErpService {

    private final OrderErpMapper orderErpMapper;

    /**
     * 주문 데이터 리스트 조회
     * @param erpServiceType
     * @return List<?>
     * @throws Exception
     */
    public List<?> getErpOrderList(ErpApiEnums.ErpServiceType erpServiceType) {

        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_EATSSLIM_ORDER)) {
            return getEatsslimOrderList();
        }
        // 잇슬림 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_EATSSLIM_NORMAL_DELIVERY_ORDER)) {
            return getEatsslimNormalDeliveryOrderList();
        }

        return null;
    }

    /**
     * 잇슬림 일일배송 주문 리스트 조회
     * @param
     * @return List<EatsslimOrderListVo>
     * @throws Exception
     */
    private List<EatsslimOrderListVo> getEatsslimOrderList() {

        String[] urWarehouseId = { ErpApiEnums.UrWarehouseId.EATSSLIM_D3_FRANCHISEE.getCode() }; // 출고처: 잇슬림 D3(가맹점)
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        return selectEatsslimOrderList(urWarehouseId, orderStatusCd);
    }

    /**
     * 잇슬림 일일배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<EatsslimOrderListVo>
     * @throws
     */
    private List<EatsslimOrderListVo> selectEatsslimOrderList(String[] urWarehouseId, String orderStatusCd) {
        return orderErpMapper.selectEatsslimOrderList(urWarehouseId, orderStatusCd);
    }

    /**
     * 잇슬림 택배배송 주문 리스트 조회
     * @param
     * @return List<EatsslimOrderListVo>
     * @throws Exception
     */
    private List<EatsslimOrderListVo> getEatsslimNormalDeliveryOrderList() {

        String[] urWarehouseId = { ErpApiEnums.UrWarehouseId.EATSSLIM_D3_DELIVERY.getCode() // 출고처: 잇슬림 D3(도안택배)
                , ErpApiEnums.UrWarehouseId.EATSSLIM_D2_3PL.getCode()  }; // 잇슬림 D2(3PL택배)
        String orderStatusCd = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode(); // 주문상태: 결제완료

        return selectEatsslimNormalDeliveryOrderList(urWarehouseId, orderStatusCd);
    }

    /**
     * 잇슬림 택배배송 주문 리스트 조회
     * @param urWarehouseId
     * @param orderStatusCd
     * @return List<EatsslimOrderListVo>
     * @throws
     */
    private List<EatsslimOrderListVo> selectEatsslimNormalDeliveryOrderList(String[] urWarehouseId, String orderStatusCd) {
        return orderErpMapper.selectEatsslimNormalDeliveryOrderList(urWarehouseId, orderStatusCd);
    }

    /**
     * 주문 header 생성
     * @param erpServiceType
     * @param headerItem
     * @return List<?>
     * @throws
     */
    public List<?> getErpApiHeaderList(ErpApiEnums.ErpServiceType erpServiceType, Object headerItem) throws Exception {

        // 잇슬림 일일배송 주문
        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_EATSSLIM_ORDER)) {
            return getEatsslimOrderHeaderList(headerItem);
        }
        // 잇슬림 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_EATSSLIM_NORMAL_DELIVERY_ORDER)) {
            return getEatsslimNormalDeliveryOrderHeaderList(headerItem);
        }

        return null;
    }

    /**
     * 잇슬림 일일배송 주문 Header 생성
     * @param headerItem
     * @return List<?>
     * @throws
     */
    private List<?> getEatsslimOrderHeaderList(Object headerItem) throws Exception {

        EatsslimOrderListVo eatsslimOrderListVo = (EatsslimOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(getEatsslimOrderHeader(eatsslimOrderListVo));

        return headerList;
    }

    /**
     * 잇슬림 택배배송 주문 Header 생성
     * @param headerItem
     * @return List<?>
     * @throws
     */
    private List<?> getEatsslimNormalDeliveryOrderHeaderList(Object headerItem) throws Exception {

        EatsslimOrderListVo eatsslimOrderListVo = (EatsslimOrderListVo) headerItem;

        List<Object> headerList = new ArrayList<>();
        headerList.add(getEatsslimNormalDeliveryOrderHeader(eatsslimOrderListVo));

        return headerList;
    }

    /**
     * 잇슬림 일일배송 주문 header 생성
     * @param headerItem
     * @return EatsslimOrderHeaderDto
     * @throws
     */
    public EatsslimOrderHeaderDto getEatsslimOrderHeader(EatsslimOrderListVo headerItem) throws Exception {
        return EatsslimOrderHeaderDto.builder()
                .outOrderNum(headerItem.getOdid()) // 풀샵 주문번호
                .ordChannelCd( getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                .orderName(getCutName(headerItem.getBuyerNm())) // 주문자명
                .recvName(getCutName(headerItem.getRecvNm()))  // 받는사람명
                .recvZipcode(headerItem.getRecvZipCd()) // 받는사람우편번호
                .recvAddr1(headerItem.getRecvAddr1()) // 받는사람 주소1
                .recvAddr2(headerItem.getRecvAddr2()) // 받는사람 주소2
                .recvHp(headerItem.getRecvHp()) // 받는사람 휴대폰번호
                .recvTel(headerItem.getRecvTel()) // 받는사람 전화번호
                .recvReqMsg(StringUtils.isEmpty(headerItem.getDeliveryMsg()) ? "" : new String(headerItem.getDeliveryMsg().getBytes("KSC5601"), "euc-kr"))  // 배송메세지
                .doorMsg(StringUtils.isEmpty(headerItem.getDoorMsg()) ? "" : new String(headerItem.getDoorMsg().getBytes("KSC5601"), "euc-kr"))
                .doorMsgDtl(StringUtils.isEmpty(headerItem.getDoorMsgNm()) ? "" : new String(headerItem.getDoorMsgNm().getBytes("KSC5601"), "euc-kr"))
                .goodsPrice(StringUtil.nvl(
                        headerItem.getRecommendedPrice()
                                *StringUtil.nvlInt(GoodsEnums.GoodsCycleType.findByCode(headerItem.getGoodsCycleTp()).getTypeQty())
                                *StringUtil.nvlInt(GoodsEnums.GoodsCycleTermType.findByCode(headerItem.getGoodsCycleTermTp()).getTypeQty())
                )) // 정상판매금액
                .payPrice(StringUtil.nvl(
                        headerItem.getSalePrice()
                                *StringUtil.nvlInt(GoodsEnums.GoodsCycleType.findByCode(headerItem.getGoodsCycleTp()).getTypeQty())
                                *StringUtil.nvlInt(GoodsEnums.GoodsCycleTermType.findByCode(headerItem.getGoodsCycleTermTp()).getTypeQty())
                )) // 판매가격
                .couponPrice(StringUtil.nvl(headerItem.getDirectPrice())) //임직원할인금액
                .orderDate(headerItem.getCreateDt()) // 주문일
                .groupCode(headerItem.getPdmGroupCd()) // 채널상품코드
                .delvType(ErpApiEnums.ErpDelvType.DAILY_DELIVERY.getCode()) // 배송방법 : 일배
                .jisaCd(headerItem.getUrStoreId()) // 가맹점코드
                .orderQty(StringUtil.nvl(headerItem.getOrderCancelCnt())) // 주문수량
                .delvFirstDate(headerItem.getDeliveryDt()) // 첫배송일
                .devlDay(GoodsEnums.GoodsCycleType.findByCode(headerItem.getGoodsCycleTp()).getTypeQty())// 배송기간 일
                .devlWeek(GoodsEnums.GoodsCycleTermType.findByCode(headerItem.getGoodsCycleTermTp()).getTypeQty()) // 배송기간 주
                .build();
    }

    /**
     * 주문경로
     * @param agentTypeCd
     * @param buyerTypeCd
     * @return String
     * @throws
     */
    private static String getOrderHpnCd(String agentTypeCd, String buyerTypeCd) {

        String orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_EMPLOYEE.getCode();
        if(SystemEnums.AgentType.ADMIN.getCode().equals(agentTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.MANAGER.getCode(); // 관리자
        else if(SystemEnums.AgentType.PC.getCode().equals(agentTypeCd)) { // 웹
            if(UserEnums.BuyerType.USER.getCode().equals(buyerTypeCd) || UserEnums.BuyerType.GUEST.getCode().equals(buyerTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_CUSTOMER.getCode(); // 고객
            else if(UserEnums.BuyerType.EMPLOYEE.getCode().equals(buyerTypeCd) || UserEnums.BuyerType.EMPLOYEE_BASIC.getCode().equals(buyerTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_EMPLOYEE.getCode(); // 임직원
        } else if(SystemEnums.AgentType.MOBILE.getCode().equals(agentTypeCd) || SystemEnums.AgentType.APP.getCode().equals(agentTypeCd)) { // 모바일, 앱
            if(UserEnums.BuyerType.USER.getCode().equals(buyerTypeCd) || UserEnums.BuyerType.GUEST.getCode().equals(buyerTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_CUSTOMER.getCode(); // 고객
            else if(UserEnums.BuyerType.EMPLOYEE.getCode().equals(buyerTypeCd) || UserEnums.BuyerType.EMPLOYEE_BASIC.getCode().equals(buyerTypeCd)) orderHpnCd = ErpApiEnums.ErpOrderHpnCd.WEB_EMPLOYEE.getCode(); // 임직원
        }

        return orderHpnCd;
    }

    /**
     * 잇슬림 택배배송 주문 header 생성
     * @param headerItem
     * @return EatsslimOrderHeaderDto
     * @throws
     */
    public EatsslimOrderHeaderDto getEatsslimNormalDeliveryOrderHeader(EatsslimOrderListVo headerItem) throws Exception {
        return EatsslimOrderHeaderDto.builder()
                .outOrderNum(headerItem.getOdid()) // 풀샵 주문번호
                .ordChannelCd( getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                .orderName(getCutName(headerItem.getBuyerNm())) // 주문자명
                .recvName(getCutName(headerItem.getRecvNm()))  // 받는사람명
                .recvZipcode(headerItem.getRecvZipCd()) // 받는사람우편번호
                .recvAddr1(headerItem.getRecvAddr1()) // 받는사람 주소1
                .recvAddr2(headerItem.getRecvAddr2()) // 받는사람 주소2
                .recvHp(headerItem.getRecvHp()) // 받는사람 휴대폰번호
                .recvTel(headerItem.getRecvTel()) // 받는사람 전화번호
                .recvReqMsg(StringUtils.isEmpty(headerItem.getDeliveryMsg()) ? "" : new String(headerItem.getDeliveryMsg().getBytes("KSC5601"), "euc-kr"))  // 배송메세지
                .doorMsg(StringUtils.isEmpty(headerItem.getDoorMsg()) ? "" : new String(headerItem.getDoorMsg().getBytes("KSC5601"), "euc-kr"))
                .doorMsgDtl(StringUtils.isEmpty(headerItem.getDoorMsgNm()) ? "" : new String(headerItem.getDoorMsgNm().getBytes("KSC5601"), "euc-kr"))
                .goodsPrice(StringUtil.nvl(headerItem.getRecommendedPrice())) // 정상판매금액
                .payPrice(StringUtil.nvl(headerItem.getSalePrice())) // 판매가격
                .couponPrice(StringUtil.nvl(headerItem.getDirectPrice())) //임직원할인금액
                .orderDate(headerItem.getCreateDt()) // 주문일
                .groupCode(headerItem.getPdmGroupCd()) // 채널상품코드
                .delvType(ErpApiEnums.ErpDelvType.NORMAL_DELIVERY.getCode()) // 배송방법 : 택배
                .jisaCd(headerItem.getUrStoreId()) // 가맹점코드
                .orderQty(StringUtil.nvl(headerItem.getOrderCancelCnt())) // 주문수량
                .delvFirstDate(headerItem.getDeliveryDt()) // 첫배송일
                .devlDay(headerItem.getGoodsCycleTp())// 배송기간 일
                .devlWeek(headerItem.getGoodsCycleTermTp()) // 배송기간 주
                .build();
    }

    /**
     * 성공 시 주문 배치완료 업데이트
     * @param erpServiceType
     * @param lineList
     * @return void
     * @throws Exception
     */
    public void putErpOrderBatchCompleteUpdate(ErpApiEnums.ErpServiceType erpServiceType, List<?> lineList) throws Exception {

        // 잇슬림 일일배송 주문
        if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_EATSSLIM_ORDER)) {
            putEatsslimOrderBatchCompleteUpdate(lineList);
        }
        // 잇슬림 택배배송 주문
        else if(erpServiceType.equals(ErpApiEnums.ErpServiceType.ERP_EATSSLIM_NORMAL_DELIVERY_ORDER)) {
            putEatsslimOrderBatchCompleteUpdate(lineList);
        }

    }

    /**
     * 성공 잇슬림 주문 배치완료 업데이트
     * @param linelist
     * @return void
     * @throws Exception
     */
    private void putEatsslimOrderBatchCompleteUpdate(List<?> linelist) throws Exception {

        for(int i=0; i<linelist.size(); i++) {
            EatsslimOrderListVo lineItem = (EatsslimOrderListVo) linelist.get(i);
            String odOrderDetlId = lineItem.getOdOrderDetlId();
            putOrderBatchCompleteUpdate(odOrderDetlId);
        }

    }

    /**
     * 성공 주문 배치완료 업데이트 (매출 O)
     * @param odOrderDetlId
     * @return void
     * @throws
     */
    private void putOrderBatchCompleteUpdate(String odOrderDetlId) throws Exception {
        String orderStatusCd = OrderEnums.OrderStatus.DELIVERY_READY.getCode();
        orderErpMapper.putOrderBatchCompleteUpdate(orderStatusCd, odOrderDetlId);
        // 이력 등록
        try {
            addOrderDetailStatusHist(StringUtil.nvlLong(odOrderDetlId), Constants.BATCH_CREATE_USER_ID, OrderEnums.OrderStatus.INCOM_COMPLETE.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("잇슬림 배치 이력등록 에러 "+e.getMessage());
        }

    }

    /**
     * 주문 상태 이력 등록
     * @param odOrderDetlId
     * @param createId
     * @param orderStatusCd
     * @return int
     * @throws BaseException
     */
    protected int addOrderDetailStatusHist(long odOrderDetlId, long createId, String orderStatusCd) throws Exception {
        OrderEnums.OrderDetailStatusHistMsg orderDetailStatusHistMsg = OrderEnums.OrderDetailStatusHistMsg.findByCode(OrderEnums.OrderStatus.DELIVERY_READY.getCode());

        OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
                .odOrderDetlId(odOrderDetlId)
                .statusCd(orderStatusCd)
                .histMsg(orderDetailStatusHistMsg.getMessage())
                .createId(createId)
                .build();

        return orderErpMapper.addOrderDetailStatusHist(orderDetlHistVo);
    }

    /**
     * 주문자명, 수령자명 자르기
     * @param name
     * @return
     */
    private String getCutName(String name) {
        return StringUtil.getByteStr(name, 1, 40, 3, false);
    }
}
