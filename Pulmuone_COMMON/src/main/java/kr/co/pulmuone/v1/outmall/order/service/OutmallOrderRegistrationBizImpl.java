package kr.co.pulmuone.v1.outmall.order.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import kr.co.pulmuone.v1.order.registration.dto.*;
import kr.co.pulmuone.v1.outmall.order.util.OutmallOrderUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailConsultRequestDto;
import kr.co.pulmuone.v1.order.order.dto.StockCheckOrderDetailDto;
import kr.co.pulmuone.v1.order.order.service.OrderDetailBiz;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.registration.service.OrderBindCollectionMallCreateBizImpl;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.order.status.dto.OrderStatusUpdateRequestDto;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class OutmallOrderRegistrationBizImpl implements OutmallOrderRegistrationBiz {
    @Autowired
    private OutmallOrderService outmallOrderService;

    @Autowired
    private OrderRegistrationBiz orderRegistrationBiz;

    @Autowired
    private OrderOrderBiz orderOrderBiz;

    @Autowired
    private OrderDetailBiz orderDetailBiz;

    @Autowired
    private GoodsStockOrderBiz goodsStockOrderBiz;

    @Autowired
    private OrderStatusBiz orderStatusBiz;

    @Autowired
    OrderBindCollectionMallCreateBizImpl orderBindBiz;

    @Autowired
    private OutmallOrderCreateBiz outmallOrderCreateBiz;

    private static final int BATCH_END_MINUTE = 28;	// 배치강제종료 분

    /**
     * 배치 실행 대상 정보 조회
     */
    @Override
    public long getLastIfOutmallExcelInfo() {
        return outmallOrderService.getLastIfOutmallExcelInfo();
    }

    /**
     * 배치 대상 조회
     * @param ifOutmallExcelInfoId
     * @return
     */
    @Override
    public Map<String,Integer> setBindOrderOrder(long ifOutmallExcelInfoId, LocalDateTime startTime) throws Exception {
        Map<String,Integer> orderCreateResult = new HashMap<>();

        // 외부몰 주문 성공정보 테이블에서 주문생성여부 N만 조회
        List<OutMallOrderDto> list = outmallOrderService.getOutmallOrderCreateTargetList(ifOutmallExcelInfoId);

        int orderCreateCnt = 0;

        if(!list.isEmpty()) {

            // COLLECTION_MALL_ID별 그룹핑
            Map<String, Map<String, List<OutMallOrderDto>>> shippingZoneMap = list.stream()
                    .collect(
                            groupingBy(OutMallOrderDto::getCollectionMallId, LinkedHashMap::new,    // 1. 주문번호 COLLECTION_MALL_ID
                                    //groupingBy(OutMallOrderDto::getGrpShippingZone, LinkedHashMap::new,     // 2. 배송지별 수취인 정보
                                    groupingBy(OutMallOrderDto::getGrpShippingPrice, LinkedHashMap::new,           // 3. 배송정책별 BUNDLE_GRP
                                            toList()

                                    )));

            for(String key : shippingZoneMap.keySet()){
                Map<String, List<OutMallOrderDto>> shippingPriceMap = shippingZoneMap.get(key);

                try{

                    // 주문데이터 바인딩 및 주문생성
                    orderCreateCnt += outmallOrderCreateBiz.orderDataBindAndCreateOrder(shippingPriceMap,ifOutmallExcelInfoId);

                }catch(BaseException baseException) {
                    // 주문생성실패시 내역 저장
                    log.error(" OutmallOrderRegistrationBizImpl setBindOrderOrder error ==== ::{}", shippingPriceMap);
                    failCreateOrder((List<OrderBindDto>) baseException.getReturnObj(), ifOutmallExcelInfoId, baseException.getMessage());
                }catch(Exception e){
                    // 주문성공PK로 실패내역 저장
                    log.error(" OutmallOrderRegistrationBizImpl setBindOrderOrder error ==== ::{}", shippingPriceMap);
                    failCreateOrderBySuccId(shippingPriceMap,  ifOutmallExcelInfoId);
                }

//                // 다음 배치시간에 맞춰 강제종료 --> 무중단으로 수정 SPMO-1433
//                LocalDateTime nowTime = LocalDateTime.now();
//                long batchExecTime = ChronoUnit.SECONDS.between(startTime, nowTime);
//                if(batchExecTime >= 60 * BATCH_END_MINUTE ){ // 배치강제종료 28분
//                    orderCreateResult.put(EZAdminEnums.EZAdminSyncCd.SYNC_CD_ING.getCode(),orderCreateCnt);
//                    return orderCreateResult;
//                }

            }

        }

        orderCreateResult.put(OutmallEnums.OutmallBatchStatusCd.END.getCode(),orderCreateCnt);
        return orderCreateResult;
    }

    @Override
    public List<OrderBindDto> orderDataBind(Map<String, List<OutMallOrderDto>> shippingPriceMap) throws Exception{
        List<OrderBindDto> orderBindList = orderBindBiz.orderDataBind(shippingPriceMap);
        return orderBindList;
    }

    @Override
    public void failCreateOrder(List<OrderBindDto> orderBindList, long ifOutmallExcelInfoId, String failMessage) throws Exception{

        List<Map<String, Object>> resultFailList = new ArrayList<>();
        for(OrderBindDto item: orderBindList){

            for(Map<String, Object> failItem: item.getCollectionMalllFailList()){

                Map<String, Object> failMap = new HashMap<>();
                failMap.put("succId", failItem.get("succId"));
                failMap.put("failMessage", failItem.get("failMessage"));
                resultFailList.add(failMap);
            }

            List<OrderBindShippingZoneDto> orderShippingZoneList = item.getOrderShippingZoneList().stream().collect(Collectors.toList());
            for (OrderBindShippingZoneDto shippingZoneItem : orderShippingZoneList) {
                OrderShippingZoneVo orderShippingZoneVo = shippingZoneItem.getOrderShippingZoneVo();
                List<OrderBindShippingPriceDto> shippingPriceList = shippingZoneItem.getShippingPriceList().stream().collect(Collectors.toList());

                for (OrderBindShippingPriceDto shippingPriceItem : shippingPriceList) {

                    OrderShippingPriceVo orderShippingPriceVo = shippingPriceItem.getOrderShippingPriceVo();
                    List<OrderBindOrderDetlDto> orderDetlList = (shippingPriceItem.getOrderDetlList().stream()
                            .collect(Collectors.toList()));

                    for (OrderBindOrderDetlDto orderDetlItem : orderDetlList) {
                        OrderDetlVo orderDetlVo = orderDetlItem.getOrderDetlVo();
                        boolean addFailFlag = true;

                        for (Map<String, Object> failMap : resultFailList){
                            if (StringUtil.nvl(failMap.get("succId"), "").equals(orderDetlVo.getIfOutmallExcelSuccId())){
                                addFailFlag = false;
                                break;
                            }
                        }

                        if (addFailFlag == true){

                            Map<String, Object> failMap = new HashMap<>();
                            failMap.put("succId", orderDetlVo.getIfOutmallExcelSuccId());
                            failMap.put("failMessage", failMessage);
                            resultFailList.add(failMap);
                        }
                    }
                }
            }
        }

        resultFailList = resultFailList.stream().distinct().sorted((o1, o2) -> o1.get("succId").toString().compareTo(o2.get("succId").toString())).collect(Collectors.toList());

        // 실패내역 저장
        for (Map<String, Object> failItem : resultFailList){
            outmallOrderService.addOutMallExcelFailSelectInsert(ifOutmallExcelInfoId, StringUtil.nvlLong(failItem.get("succId")), StringUtil.nvl(failItem.get("failMessage")));
            outmallOrderService.deleteOutMallExcelSuccess(StringUtil.nvlLong(failItem.get("succId")));
        }

    }

    private void failCreateOrderBySuccId(Map<String, List<OutMallOrderDto>> shippingPriceMap, long ifOutmallExcelInfoId){
        List<OutMallOrderDto> outmallOrderDtoList = shippingPriceMap.entrySet().iterator().next().getValue();

        try{
            if(CollectionUtils.isNotEmpty(outmallOrderDtoList)){
                String failMessage = OutmallEnums.orderCreateMsg.ORDER_BIND_FAIL.getMessage();

                for(OutMallOrderDto dto : outmallOrderDtoList){
                    outmallOrderService.addOutMallExcelFailSelectInsert(ifOutmallExcelInfoId, StringUtil.nvlLong(dto.getIfOutmallExcelSuccId()), failMessage);
                    outmallOrderService.deleteOutMallExcelSuccess(StringUtil.nvlLong(dto.getIfOutmallExcelSuccId()));
                }
            }
        }catch(Exception e){
            log.error(" OutmallOrderRegistrationBizImpl failCreateOrderBySuccId error ==== ::{}", shippingPriceMap);
            e.printStackTrace();
        }
    }

    @Override
    public void verificationOrderCreateSuccess(long ifOutmallExcelInfoId) throws Exception{
        // 1. 주문생성 안된 건 조회
        List<HashMap> notOrderCreateList =  outmallOrderService.getNotOrderCreateInIfOutmallExcelSucc(ifOutmallExcelInfoId);
        
        if(CollectionUtils.isNotEmpty(notOrderCreateList)){
            // 2. 실패테이블에 저장, 성공테이블에서 삭제
            String failMessage = OutmallEnums.orderCreateMsg.ORDER_CREATE_FAIL_IN_VERIFICATION.getMessage();

            for(HashMap<String,Long> orderMap : notOrderCreateList){
                outmallOrderService.addOutMallExcelFailSelectInsert(ifOutmallExcelInfoId, Long.parseLong(String.valueOf(orderMap.get("IF_OUTMALL_EXCEL_SUCC_ID"))), failMessage);
                outmallOrderService.deleteOutMallExcelSuccess(Long.parseLong(String.valueOf(orderMap.get("IF_OUTMALL_EXCEL_SUCC_ID"))));
            }
        }
    }

    @Override
    public int getOrderCreateCount(long ifOutmallExcelInfoId) throws Exception{
        return outmallOrderService.getOrderCreateCount(ifOutmallExcelInfoId);
    }

}