package kr.co.pulmuone.v1.batch.legacysync.stock;

import kr.co.pulmuone.v1.batch.legacysync.stock.dto.*;
import kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo.LegacyOrderStockIfItemWarehouseVo;
import kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo.LegacyOrderStockIfVo;
import kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo.OrderDetailStockGroupByVo;
import kr.co.pulmuone.v1.batch.legacysync.stock.dto.vo.OrderStockIfGroupByVo;
import kr.co.pulmuone.v1.comm.mappers.batch.master.legacysync.LegacySyncBatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LegacySyncStockBatchService {

    @Autowired
    private LegacySyncBatchMapper legacySyncBatchMapper;

    /**
     * 통합몰 주문 동기화 테이블 등록 서비스
     */
    protected int orderStockInterface() {
        int resultCnt = 0;

        // 출고일 기준 현재 날짜보다 크거나 같은 주문 상세 조회
        List<OrderDetailStockGroupByVo> orderDetailStockList = orderDetailStockGroupByList();

        // 통합몰 주문 상세 정보에서 재고 차감 데이터 추출
        for (OrderDetailStockGroupByVo orderDetailVo : orderDetailStockList) {
            // 클레임에 의한 취소 재고 여부
            String craimYn = "N";

            // 주문 생성 재고 처리
            OrderStockIfGroupByVo orderStockIfGroupByVo = orderStockIfGroupByInfo(orderDetailVo, "ORDER");
            if(orderStockIfGroupByVo == null) {
                resultCnt += insertOrderStockIf(orderDetailVo, "ORDER", orderDetailVo.getOrderCnt(), craimYn);
            } else {
                // 주문 상세 테이블 상품 주문 수량 > 동기화 테이블 상품 주문 수량 - 비교
                // 통합몰 주문 상세의 상품 주문 재고가 동기화 테이블의 상품 주문 재고 수량보다 많은 경우
                // 두 재고의 차이 만큼만 동기화 테이블에 주문 수량으로 등록, 취소/클레임 시에도 주문 상세의 ORDER_CNT는 변함 없음
                if(orderDetailVo.getOrderCnt() > orderStockIfGroupByVo.getOrderCnt()) {
                    resultCnt += insertOrderStockIf(orderDetailVo, "ORDER", orderDetailVo.getOrderCnt() - orderStockIfGroupByVo.getOrderCnt(), craimYn);
                }
                else if (orderDetailVo.getOrderCnt() < orderStockIfGroupByVo.getOrderCnt()){
                    // 통합몰 주문 재고 - I/F 주문재고 수량 비교
                    int cancelStockCount = Math.abs(orderDetailVo.getOrderCnt() - orderStockIfGroupByVo.getOrderCnt());

                    // I/F 클레임 취소 재고가 아닌 일반 취소 재고 조회
                    OrderStockIfGroupByVo cancelStockIfGroupByVo = cancelStockIfGroupByInfo(orderDetailVo, "CANCEL");

                    // 배송 예정일 변경에 의한 주문 재고 수 차이가 발생하는 경우
                    // 최초 차이나는 재고 수량 만큼 insert
                    // 추가로 취소 재고가 등록된 경우 기존 등록 된 취소 재고 수량 보다 추가된 수량 만큼만 계산하여 취소 재고 등록
                    if (cancelStockIfGroupByVo == null) {
                        resultCnt += insertOrderStockIf(orderDetailVo, "CANCEL", cancelStockCount, craimYn);
                    } else {
                        if(cancelStockCount > cancelStockIfGroupByVo.getOrderCnt()) {
                            // (통합몰 주문 재고 - I/F 주문재고) > I/F 취소재고 면 (통합몰 주문 재고 - I/F 주문재고) - I/F 취소재고 = I/F 취소 재고 수량 추가
                            resultCnt += insertOrderStockIf(orderDetailVo, "CANCEL", cancelStockCount - cancelStockIfGroupByVo.getOrderCnt(), craimYn);
                        } else if(cancelStockCount < cancelStockIfGroupByVo.getOrderCnt()) {
                            // (통합몰 주문 재고 - I/F 주문재고) < I/F 취소재고 면 (통합몰 주문 재고 - I/F 주문재고) - I/F 취소재고 = I/F 주문 재고 수량 추가
                            resultCnt += insertOrderStockIf(orderDetailVo, "ORDER", Math.abs(cancelStockCount - cancelStockIfGroupByVo.getOrderCnt()), craimYn);
                        }
                    }
                }
            }

            // 클레임에 의한 취소 주문 재고 처리
            if (orderDetailVo.getCraimCnt() > 0) {
                craimYn = "Y";
                OrderStockIfGroupByVo orderCancelStockIfGroupByVo = orderStockIfGroupByInfo(orderDetailVo, "CANCEL");

                if (orderCancelStockIfGroupByVo == null) {
                    resultCnt += insertOrderStockIf(orderDetailVo, "CANCEL", orderDetailVo.getCraimCnt(), craimYn);
                } else {
                    // 주문 상세 테이블 상품 주문 취소 수량 > 동기화 테이블 상품 주문 취 수량 - 비교
                    // 통합몰 주문 상세의 상품 주문 취소 수량이 동기화 테이블의 상품 주문 취소 수량보다 많은 경우
                    // 두 수량의 차이 만큼 동기화 테이블에 취소 수량으로 등록
                    // 취소, 클레임의 경우 주문서는 그대로 유지하고 주문 상세의 취소 컬럼에 수량을 업데이트 하기 때문에 변동 없음
                    if (orderDetailVo.getCraimCnt() > orderCancelStockIfGroupByVo.getOrderCnt()) {
                        resultCnt += insertOrderStockIf(orderDetailVo, "CANCEL", orderDetailVo.getCraimCnt() - orderCancelStockIfGroupByVo.getOrderCnt(), craimYn);
                    } else if (orderDetailVo.getCraimCnt() < orderCancelStockIfGroupByVo.getOrderCnt()) {
                        // 통합몰 주문 상세의 상품 주문 취소 수량이 동기화 테이블의 상품 주문 취소 수량보다 적은 경우
                        // 두 수량의 차이 만큼 동기화 테이블에 주문 재고 수량으로 등록
                        craimYn = "N";
                        resultCnt += insertOrderStockIf(orderDetailVo, "ORDER", Math.abs(orderDetailVo.getCraimCnt() - orderCancelStockIfGroupByVo.getOrderCnt()), craimYn);
                    }
                }
            }
        }

        return resultCnt;
    }

    /**
     * 풀샵/올가샵 -> 통합몰 재고 계산
     *
     */
    protected int legacyOrderStockCalculated() {

        int resultCnt = 0;

        // 풀샵,올가샵 I/F 재고 정보 조회
        List<LegacyOrderStockIfVo> legacyOrderStockIfList = legacySyncBatchMapper.getLegacyOrderStockIfList();

        for (LegacyOrderStockIfVo legacyOrderStockIfVo : legacyOrderStockIfList) {
            // 통합몰 <-> Legacy 연동되는 품목이 아닌 경우 해당 품목을 확인하기 위한 flag 값 - 연동 품목이 아닌 경우 'E'
            String resultFlag = "E";

            // IL_ITEM_WAREHOUSE_ID 조회
            SearchItemWarehouseIdDto searchItemWarehouseIdDto = SearchItemWarehouseIdDto.builder()
                    .itemCd(legacyOrderStockIfVo.getChnnGoodsNo())
                    .supplierId(legacyOrderStockIfVo.getUrSupplierId())
                    .warehouseId(legacyOrderStockIfVo.getUrWarehouseId())
                    .build();

            LegacyOrderStockIfItemWarehouseVo legacyOrderStockIfItemWarehouseVo = legacySyncBatchMapper.getLegacyOrderStockIfItemWarehouse(searchItemWarehouseIdDto);

            // 품목별 출고처 코드 가 존재하는 경우 IL_ITEM_ERP_STOCK 테이블에 insert
            // stockTp 가 주문 인 경우 bastDt와 scheduleDt의 값이 일치해야 함.
            if(legacyOrderStockIfItemWarehouseVo !=  null) {
                CreateItemErpStockDto createItemErpStockDto = CreateItemErpStockDto.builder()
                        .baseDt(legacyOrderStockIfVo.getIfSendSchdDt())
                        .ilItemWarehouseId(legacyOrderStockIfItemWarehouseVo.getItemWarehouseId())
                        .stockTp("ERP_STOCK_TP.ORDER")
                        .stockQty(legacyOrderStockIfVo.getOrderType().equals("ORDER") ?
                                                            legacyOrderStockIfVo.getOrdCnt()
                                                            : legacyOrderStockIfVo.getOrdCnt() * -1)
                        .scheduleDt(legacyOrderStockIfVo.getIfSendSchdDt())
                        .build();

                resultCnt += legacySyncBatchMapper.insertLegacyOrderStockIlItemErpStock(createItemErpStockDto);
                // IL_ITEM_ERP_STOCK_HISTORY 이력 저장
                legacySyncBatchMapper.insertLegacyOrderStockIlItemErpStockHistory(createItemErpStockDto.getIlItemErpStockId());

                // 통합몰 <-> Legacy 연동되는 품목인 경우
                resultFlag = "Y";

            }
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("seq", legacyOrderStockIfVo.getSeq());
            parameterMap.put("updateFlag", resultFlag);

            // 처리 완료된 I/F flag 업데이트
            legacySyncBatchMapper.updateLegacyOrderStockFlag(parameterMap);
        }

        // CALL SP_ITEM_STOCK_CACULATED_PREPARE() 재고 재계산 프로시저 호출
        if (resultCnt > 0) {
            legacySyncBatchMapper.spItemStockCalculatedPrepareByLegacyOrderStock();
        }

        return resultCnt;
    }


    /**
     * 통합몰 주문 상세 기준 주문 채결 된 상품량 재 수량 조회
     * @return
     */
    private List<OrderDetailStockGroupByVo> orderDetailStockGroupByList() {
        return legacySyncBatchMapper.getOrderDetailStockGroupByList();
    }

    /**
     *
     * 통합몰 주문 상세 기준 주문/취소 상품 재고 수 조회
     * @param orderDetailStockGroupByVo
     * @param orderType
     * @return
     */
    private OrderStockIfGroupByVo orderStockIfGroupByInfo(OrderDetailStockGroupByVo orderDetailStockGroupByVo, String orderType) {
        SearchOrderStockIfDto searchOrderStockIfDto = SearchOrderStockIfDto.builder()
                .ifSendSchdDt(orderDetailStockGroupByVo.getShippingDt())
                .chnnNo(orderDetailStockGroupByVo.getSupplierCd())
                .pdSeq(orderDetailStockGroupByVo.getWarehouseId())
                .chnnGoodsNo(orderDetailStockGroupByVo.getIlItemCd())
                .orderType(orderType)
                .build();

        return legacySyncBatchMapper.getOrderStockIfGroupByInfo(searchOrderStockIfDto);

    }

    /**
     *
     * 통합몰 주문 상세 기준 취소 상품 재고 수 조회
     * @param orderDetailStockGroupByVo
     * @param orderType
     * @return
     */
    private OrderStockIfGroupByVo cancelStockIfGroupByInfo(OrderDetailStockGroupByVo orderDetailStockGroupByVo, String orderType) {
        SearchOrderStockIfDto searchOrderStockIfDto = SearchOrderStockIfDto.builder()
                .ifSendSchdDt(orderDetailStockGroupByVo.getShippingDt())
                .chnnNo(orderDetailStockGroupByVo.getSupplierCd())
                .pdSeq(orderDetailStockGroupByVo.getWarehouseId())
                .chnnGoodsNo(orderDetailStockGroupByVo.getIlItemCd())
                .orderType(orderType)
                .build();

        return legacySyncBatchMapper.getCancelStockIfGroupByInfo(searchOrderStockIfDto);

    }
    /**
     * 통합몰 주문 I/F 테이블에 출고처별 재고 수량 등록
     * @param orderDetailStockGroupByVo
     * @param orderType
     * @param orderCnt
     * @return
     */
    private int insertOrderStockIf(OrderDetailStockGroupByVo orderDetailStockGroupByVo, String orderType, int orderCnt, String craimYn) {
        CreateOrderStockIfDto createOrderStockDto = CreateOrderStockIfDto.builder()
                .ifSendSchdDt(orderDetailStockGroupByVo.getShippingDt())
                .chnnNo(orderDetailStockGroupByVo.getSupplierCd())
                .pdSeq(orderDetailStockGroupByVo.getWarehouseId())
                .chnnGoodsNo(orderDetailStockGroupByVo.getIlItemCd())
                .itemBarcode(orderDetailStockGroupByVo.getItemBarcode())
                .delivSchdDt(orderDetailStockGroupByVo.getDeliveryDt())
                .orderType(orderType)
                .orderCnt(orderCnt)
                .craimCancelYn(craimYn)
                .build();

        return legacySyncBatchMapper.insertOrderStockIf(createOrderStockDto);
    }

}
