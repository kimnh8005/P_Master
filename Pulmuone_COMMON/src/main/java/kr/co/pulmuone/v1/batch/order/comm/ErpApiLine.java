package kr.co.pulmuone.v1.batch.order.comm;

import kr.co.pulmuone.v1.batch.order.dto.line.*;
import kr.co.pulmuone.v1.batch.order.dto.vo.*;
import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.api.constant.StockWarehouseTypes;
import kr.co.pulmuone.v1.comm.api.constant.SupplierTypes;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 Line
 * </PRE>
 */

@Component
public class ErpApiLine {

    /**
     * 용인물류 일반배송 풀무원식품 주문 line 생성
     * @param lineItem
     * @return NormalDeliveryOrderFoodLineDto
     * @throws
     */
    public NormalDeliveryOrderFoodLineDto getNormalDeliveryOrderFoodLine(DeliveryOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return NormalDeliveryOrderFoodLineDto.builder()
                                     .crpCd(LegalTypes.FOOD.getCode()) // 풀무원식품: PFF
                                     .hrdSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                     .oriSysSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                     .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                     .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                     .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                     .ordCnt(lineItem.getOrderCancelCnt()) // 주문수량-주문취소수량
                                     .dlvStrDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                     .dlvReqDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                     .selPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSaleNonTaxPrice()) // 상품판매 단가(부가세제외)
                                     .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                     .shpCd(ErpApiEnums.ErpShpCode.PULMUONE.getCode()) // 샵구분: 01
                                     .totOrdAmt(isGiftTypeGoods ? 0 : lineItem.getTotalSalePrice()) // 상품판매 총금액(부가세포함)
                                     .totOrdAmtNonTax(isGiftTypeGoods ? 0 : lineItem.getTotalSaleNonTaxPrice()) // 상품판매 총금액(부가세제외)
                                     .totOrdTax(isGiftTypeGoods ? 0 : lineItem.getTotalTaxPrice()) // 상품판매 총금액(부가세)
                                     .wahCd(StockWarehouseTypes.PFF_YONGIN.getCode()) // 출고처 창고코드값: 802
                                     .ctx(BatchConstants.BATCH_CONTEXT) // CONTEXT 값 고정
                                     .linCatCd(ErpApiEnums.ErpLineCategoriCode.LINE_ORDER.getCode()) // 라인유형 카테고리 코드: ORDER
                                     .linTyp( isGiftTypeGoods
                                                        ? ErpApiEnums.ErpFoodOrderLineType.LINE_SHIP_ONLY.getCode()
                                                        : ErpApiEnums.ErpFoodOrderLineType.LINE_STANDARD.getCode() ) // 주문 Line 유형: 일반거래일때 STANDARD, 미거래(증정품)일때 SHIP-ONLY
                                     .ordPoTyp(ErpApiEnums.ErpOrderPoType.NORMAL_DELIVERY.getCode()) // 주문/발주 연계 유형: 50
                                     .shiIns(lineItem.getDeliveryMsg()) // 고객 요청사항
                                     .linTypId(Integer.parseInt(ErpApiEnums.ErpFoodOrderLineId.ORDER_LINE_ID.getCode())) // 주문 Line 유형 ID: 1214
                                     .shiToOrgId( GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd()) // 식품마케팅증정일경우
                                                     ? lineItem.getGiftFoodMarketingCd()
                                                     : ErpApiComm.getShiToOrgId(lineItem.getSupplierCd()) ) // 납품처 ID
                                     .ordSrc( ErpApiComm.getOrdSrc(lineItem.getSupplierCd(), ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode()) ) // 주문출처
                                     .shiPriCd(ErpApiEnums.ErpShiPriCd.YONGIN_DELIVERY.getCode()) // 용인출고 : D-1
                                     .ordTyp( isGiftTypeGoods
                                              ? ErpApiEnums.ErpOrderType.NON_ORDER.getCode() // 증정일때 미거래주문
                                              : ErpApiEnums.ErpOrderType.NORMAL_ORDER.getCode() ) // 일반일때 일반주문
                                     .prcDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                     .taxYn(lineItem.getTaxYn()) // 과세여부
                                     .build();
    }

    /**
     * 용인물류 일반배송 올가홀푸드 주문 line 생성
     * @param lineItem
     * @return NormalDeliveryOrderOrgaLineDto
     * @throws
     */
    public NormalDeliveryOrderOrgaLineDto getNormalDeliveryOrderOrgaLine(DeliveryOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return NormalDeliveryOrderOrgaLineDto.builder()
                                     .crpCd(LegalTypes.ORGA.getCode()) // 올가홀푸드: OGH
                                     .hrdSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                     .oriSysSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                     .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                     .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                     .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                     .kanCd(lineItem.getItemBarcode()) // 바코드
                                     .ordCnt(lineItem.getOrderCancelCnt()) // 주문수량-주문취소수량
                                     .dlvStrDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                     .dlvReqDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                     .selPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSaleNonTaxOrgaPrice()) // 상품판매 단가(부가세제외)
                                     .ordTaxPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSalePrice()) // 상품판매 단가(부가세포함)
                                     .taxPrc(isGiftTypeGoods ? 0 : lineItem.getUnitTaxPrice()) // 상품판매 단가(부가세)
                                     .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                     .shpCd(ErpApiEnums.ErpShpCode.PULMUONE.getCode()) // 샵구분: 01
                                     .dlvGrpTyp(ErpApiEnums.ErpDlvGrpType.NORMAL_ORDER.getCode()) // 배송그룹유형: 일반
                                     .totOrdAmt(isGiftTypeGoods ? 0 : lineItem.getTotalSalePrice()) // 상품판매 총금액(부가세포함)
                                     .totOrdAmtNonTax(isGiftTypeGoods ? 0 : lineItem.getTotalSaleNonTaxPrice()) // 상품판매 총금액(부가세제외)
                                     .totOrdTax(isGiftTypeGoods ? 0 : lineItem.getTotalTaxPrice()) // 상품판매 총금액(부가세)
                                     .wahCd(StockWarehouseTypes.OGH_YONGIN.getCode()) // 출고처 창고코드값: O13
                                     .ctx(BatchConstants.BATCH_CONTEXT) // CONTEXT 값 고정
                                     .linCatCd(ErpApiEnums.ErpLineCategoriCode.LINE_ORDER.getCode()) // 라인 유형 코드: ORDER
                                     .linTyp( isGiftTypeGoods // 주문 Line 유형
                                              ? ErpApiEnums.ErpOrgaOrderLineType.NO_LINE_NORMAL_GIFT.getCode() // NO_일반매출_증정
                                              : ErpApiEnums.ErpOrgaOrderLineType.NO_LINE_NORMAL.getCode() ) // : NO_일반매출
                                     .ordPoTyp(ErpApiEnums.ErpOrderPoType.NORMAL_DELIVERY.getCode()) // 주문/발주 연계 유형: 50
                                     .ordCntUom(BatchConstants.SUBJECT_UNIT) // 품목단위 고정
                                     .orgId(BatchConstants.ORGA_ORGANIZATION_CODE) // 조직코드 고정
                                     .prcDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                     .recOrgId(BatchConstants.PARTNERS_ORGANIZATION_CODE) // 협력업체 입고 조직 고정
                                     .salCnlTyp(SystemEnums.AgentType.OUTMALL.getCode().equals(lineItem.getAgentTypeCd())
                                                        ? ErpApiEnums.ErpSalesChannelType.OUTMALL_ORDER.getCode()
                                                        : ErpApiEnums.ErpSalesChannelType.OUTMALL_EXCEPT_ORDER.getCode() ) // 정산유형: 외부몰이면 MIM, 나머지 일반
                                     .shiIns(lineItem.getDeliveryMsg()) // 고객 요청사항
                                     .linTypId(Integer.parseInt(ErpApiEnums.ErpOrgaOrderLineId.ORDER_LINE_ID.getCode())) // 주문 Line 유형 ID: 1646
                                     .ordTyp(ErpApiEnums.ErpOrderType.NO_SHOPPING_MALL.getCode()) // ERP 주문 유형: NO_쇼핑몰
                                     .shiToOrgId( ErpApiComm.getShiToOrgId(lineItem.getSupplierCd()) ) // 납품처 ID
                                     .ordSrc(ErpApiEnums.ErpOrgaOrderSource.SHOPPING_MALL.getCode()) // 주문출처: 쇼핑몰
                                     .prmDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                     .taxYn(lineItem.getTaxYn()) // 과세여부
                                     .build();
    }

    /**
     * 용인물류 새벽배송 풀무원식품 주문 line 생성
     * @param lineItem
     * @return DawnDeliveryOrderFoodLineDto
     * @throws
     */
    public DawnDeliveryOrderFoodLineDto getDawnDeliveryOrderFoodLine(DeliveryOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return DawnDeliveryOrderFoodLineDto.builder()
                                    .crpCd(LegalTypes.FOOD.getCode()) // 풀무원식품: PFF
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .ordCnt(lineItem.getOrderCancelCnt()) // 주문수량-주문취소수량
                                    .dlvStrDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                    .dlvReqDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                    .selPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSaleNonTaxPrice()) // 상품판매 단가(부가세제외)
                                    .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                    .shpCd(ErpApiEnums.ErpShpCode.PULMUONE.getCode()) // 샵구분: 01
                                    .totOrdAmt(isGiftTypeGoods ? 0 : lineItem.getTotalSalePrice()) // 상품판매 총금액(부가세포함)
                                    .totOrdAmtNonTax(isGiftTypeGoods ? 0 : lineItem.getTotalSaleNonTaxPrice()) // 상품판매 총금액(부가세제외)
                                    .totOrdTax(isGiftTypeGoods ? 0 : lineItem.getTotalTaxPrice()) // 상품판매 총금액(부가세)
                                    .wahCd(StockWarehouseTypes.PFF_YONGIN.getCode()) // 출고처 창고코드값: 802
                                    .ctx(BatchConstants.BATCH_CONTEXT) // CONTEXT 값 고정
                                    .linCatCd(ErpApiEnums.ErpLineCategoriCode.LINE_ORDER.getCode()) // 라인유형 카테고리 코드: ORDER
                                    .linTyp( GoodsEnums.GoodsType.NORMAL.getCode().equals(lineItem.getGoodsTpCd())
                                            ? ErpApiEnums.ErpFoodOrderLineType.LINE_STANDARD.getCode()
                                            : ErpApiEnums.ErpFoodOrderLineType.LINE_SHIP_ONLY.getCode() ) // 주문 Line 유형: 일반거래일때 STANDARD, 미거래(증정품)일때 SHIP-ONLY
                                    .ordPoTyp(ErpApiEnums.ErpOrderPoType.DAWN_DELIVERY.getCode()) // 주문/발주 연계 유형: 190
                                    .shiIns(lineItem.getDeliveryMsg()) // 고객 요청사항
                                    .linTypId(Integer.parseInt(ErpApiEnums.ErpFoodOrderLineId.ORDER_LINE_ID.getCode())) // 주문 Line 유형 ID: 1214
                                    .shiToOrgId( GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd()) // 식품마케팅증정일경우
                                            ? lineItem.getGiftFoodMarketingCd()
                                            : ErpApiComm.getShiToOrgId(lineItem.getSupplierCd()) ) // 납품처 ID
                                    .ordSrc( ErpApiComm.getOrdSrc(lineItem.getSupplierCd(), ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode()) ) // 주문출처
                                    .shiPriCd(ErpApiEnums.ErpShiPriCd.YONGIN_DELIVERY.getCode()) // 용인출고 : D-1
                                    .ordTyp( GoodsEnums.GoodsType.NORMAL.getCode().equals(lineItem.getGoodsTpCd())
                                            ? ErpApiEnums.ErpOrderType.NORMAL_ORDER.getCode() // 일반일때 일반주문
                                            : ErpApiEnums.ErpOrderType.NON_ORDER.getCode() ) // 증정일때 미거래주문
                                    .prcDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .build();
    }

    /**
     * 용인물류 새벽배송 올가홀푸드 주문 line 생성
     * @param lineItem
     * @return DawnDeliveryOrderOrgaLineDto
     * @throws
     */
    public DawnDeliveryOrderOrgaLineDto getDawnDeliveryOrderOrgaLine(DeliveryOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return DawnDeliveryOrderOrgaLineDto.builder()
                                    .crpCd(LegalTypes.ORGA.getCode()) // 올가홀푸드: OGH
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .kanCd(lineItem.getItemBarcode()) // 바코드
                                    .ordCnt(lineItem.getOrderCancelCnt()) // 주문수량-주문취소수량
                                    .dlvStrDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                    .dlvReqDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                    .selPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSaleNonTaxOrgaPrice()) // 상품판매 단가(부가세제외)
                                    .ordTaxPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSalePrice()) // 상품판매 단가(부가세포함)
                                    .taxPrc(isGiftTypeGoods ? 0 : lineItem.getUnitTaxPrice()) // 상품판매 단가(부가세)
                                    .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                    .shpCd(ErpApiEnums.ErpShpCode.PULMUONE.getCode()) // 샵구분: 01
                                    .dlvGrpTyp(ErpApiEnums.ErpDlvGrpType.NORMAL_ORDER.getCode()) // 배송그룹유형: 일반
                                    .totOrdAmt(isGiftTypeGoods ? 0 : lineItem.getTotalSalePrice()) // 상품판매 총금액(부가세포함)
                                    .totOrdAmtNonTax(isGiftTypeGoods ? 0 : lineItem.getTotalSaleNonTaxPrice()) // 상품판매 총금액(부가세제외)
                                    .totOrdTax(isGiftTypeGoods ? 0 : lineItem.getTotalTaxPrice()) // 상품판매 총금액(부가세)
                                    .wahCd(StockWarehouseTypes.OGH_YONGIN.getCode()) // 출고처 창고코드값: O13
                                    .ctx(BatchConstants.BATCH_CONTEXT) // CONTEXT 값 고정
                                    .linCatCd(ErpApiEnums.ErpLineCategoriCode.LINE_ORDER.getCode()) // 라인 유형 코드: ORDER
                                    .linTyp( isGiftTypeGoods // 주문 Line 유형
                                             ? ErpApiEnums.ErpOrgaOrderLineType.NO_LINE_NORMAL_GIFT.getCode() // NO_일반매출_증정
                                             : ErpApiEnums.ErpOrgaOrderLineType.NO_LINE_NORMAL.getCode() ) // : NO_일반매출
                                    .ordPoTyp(ErpApiEnums.ErpOrderPoType.DAWN_DELIVERY.getCode()) // 주문/발주 연계 유형: 190
                                    .ordCntUom(BatchConstants.SUBJECT_UNIT) // 품목단위 고정
                                    .orgId(BatchConstants.ORGA_ORGANIZATION_CODE) // 조직코드 고정
                                    .prcDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                    .recOrgId(BatchConstants.PARTNERS_ORGANIZATION_CODE) // 협력업체 입고 조직 고정
                                    .salCnlTyp( SystemEnums.AgentType.OUTMALL.getCode().equals(lineItem.getAgentTypeCd())
                                            ? ErpApiEnums.ErpSalesChannelType.OUTMALL_ORDER.getCode()
                                            : ErpApiEnums.ErpSalesChannelType.OUTMALL_EXCEPT_ORDER.getCode() ) // 정산유형: 외부몰이면 MIM, 나머지 일반
                                    .shiIns(lineItem.getDeliveryMsg()) // 고객 요청사항
                                    .linTypId(Integer.parseInt(ErpApiEnums.ErpOrgaOrderLineId.ORDER_LINE_ID.getCode())) // 주문 Line 유형 ID: 1646
                                    .ordTyp(ErpApiEnums.ErpOrderType.NO_SHOPPING_MALL.getCode()) // ERP 주문 유형: NO_쇼핑몰
                                    .shiToOrgId( ErpApiComm.getShiToOrgId(lineItem.getSupplierCd()) ) // 납품처 ID
                                    .ordSrc(ErpApiEnums.ErpOrgaOrderSource.SHOPPING_MALL.getCode()) // 주문출처: 쇼핑몰
                                    .prmDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .build();
    }

    /**
     * 백암물류 주문 line 생성
     * @param lineItem
     * @return CjOrderLineDto
     * @throws
     */
    public CjOrderLineDto getCjOrderLine(CjOrderListVo lineItem) {
        return CjOrderLineDto.builder()
                                    .crpCd(SourceServerTypes.CJWMS.getCode()) // 백암물류: CJWMS
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .ordCnt(lineItem.getOrderCancelCnt()) // 주문수량-주문취소수량
                                    .dlvStrDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                    .dlvReqDat(lineItem.getOrderIfDt()) // 인터페이스일자
                                    .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                    .creDat(lineItem.getCreateDt()) // 데이터생성일자
                                    .stcTyp( GoodsEnums.GoodsType.DISPOSAL.getCode().equals(lineItem.getGoodsTpCd()) // 상품이 폐기임박일 경우
                                            ? ErpApiEnums.ErpCjStcTypType.IMMINENT_EVENT.getCode() // "AC"
                                            : ErpApiEnums.ErpCjStcTypType.NORMAL.getCode()) // "NN"
                                    .build();
    }

    /**
     * 올가 매장배송 주문 line 생성
     * @param lineItem
     * @return OrgaStoreDeliveryOrderLineDto
     * @throws
     */
    public OrgaStoreDeliveryOrderLineDto getOrgaStoreDeliveryOrderLine(OrgaStoreDeliveryOrderListVo lineItem) {
        return OrgaStoreDeliveryOrderLineDto.builder()
                                    .crpCd(SourceServerTypes.ORGAOMS.getCode()) // ORGA OMS: ORGAOMS
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .kanCd(lineItem.getItemBarcode()) // 바코드
                                    .ordCnt(lineItem.getOrderCancelCnt()) // BUY상품적용수량
                                    .dlvReqDat(lineItem.getCreateDt()) // 영업일자
                                    .creDat(lineItem.getCreateDt()) // 데이터 생성일자
                                    .salFg(Double.parseDouble(ErpApiEnums.ErpSaleFg.SALES_ORDER.getCode())) // 매출액표시기준: 1 판매
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .selPrc(lineItem.getUnitSalePrice()) // 판매단가
                                    .stdAmt(lineItem.getSalePrice()) // 매출액
                                    .salYn(ErpApiEnums.ErpSalYn.SALES_ORDER.getCode()) // 판매여부: Y 판매
                                    //.dcAmt(lineItem.getDiscountPrice()) // 할인액
                                    .vatAmt(lineItem.getVatAmt()) // 부가세
                                    .posInsDt(lineItem.getCreateDt()) // POS 등록일시
                                    .shpCd(lineItem.getShpCd()) // 매장코드
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .build();
    }

    /**
     * 하이톡 택배배송 주문 line 생성
     * @param lineItem
     * @return HitokNormalDeliveryOrderLineDto
     * @throws
     */
    public HitokNormalDeliveryOrderLineDto getHitokNormalDeliveryOrderLine(HitokNormalDeliveryOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return HitokNormalDeliveryOrderLineDto.builder()
                                    .crpCd(SourceServerTypes.HITOK.getCode()) // 녹즙: HITOK
                                    .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목 Code
                                    .ordCnt(lineItem.getOrderCancelCnt()) // 1회배달 수량
                                    .dlvGrp(ErpApiEnums.ErpHitokDlvGrp.NORMAL_DELIVERY.getCode()) // 1 : 택배
                                    .dlvReqDat(lineItem.getDeliveryDt()) // 배송예정일(YYYYMMDDHHMISS)
                                    .ordAmt(isGiftTypeGoods ? 0 : lineItem.getSalePrice()) // 판매가격
                                    .totOrdCnt(lineItem.getOrderCancelCnt()) // 총수량
                                    .ordTyp( isGiftTypeGoods
                                                ? ErpApiEnums.ErpHitokOrdTypLin.PRESENTATION.getCode()
                                                : ErpApiEnums.ErpHitokOrdTypLin.ORDER.getCode() ) // 주문일때: OT01 증정일때: OT02 (행사일때: OT03) 행사상품에 대한 구분이 따로 없음
                                    .shiToOrgId(lineItem.getSupplierCd()) // 납품처 ID
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .build();
    }

    /**
     * 하이톡 일일배송 주문 line 생성
     * @param lineItem
     * @return HitokDailyDeliveryOrderLineDto
     * @throws
     */
    public HitokDailyDeliveryOrderLineDto getHitokDailyDeliveryOrderLine(HitokDailyDeliveryOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return HitokDailyDeliveryOrderLineDto.builder()
                                    .crpCd(SourceServerTypes.HITOK.getCode()) // 녹즙: HITOK
                                    .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목 Code
                                    .ordCnt(lineItem.getOrderCnt()) // 1회배달 수량
                                    .dlvGrp(ErpApiEnums.ErpHitokDlvGrp.DAILY_DELIVERY.getCode()) // 0 : 일배
                                    .dlvReqDat(lineItem.getDeliveryDt()) // 배송예정일(YYYYMMDDHHMISS)
                                    .ordAmt(isGiftTypeGoods ? 0 : lineItem.getSalePrice()) // 판매가격
                                    .totOrdCnt(lineItem.getOrderCancelCnt()) // 총수량
                                    .ordTyp( isGiftTypeGoods
                                            ? ErpApiEnums.ErpHitokOrdTypLin.PRESENTATION.getCode()
                                            : ErpApiEnums.ErpHitokOrdTypLin.ORDER.getCode() ) // 주문일때: OT01 증정일때: OT02 (행사일때: OT03) 행사상품에 대한 구분이 따로 없음
                                    .shiToOrgId(lineItem.getSupplierCd()) // 납품처 ID
                                    .schLinNo(lineItem.getOdOrderDetlDailySchSeq()) // 스케줄 라인
                                    .stoCd(lineItem.getUrStoreId()) // 가맹점 코드
                                    .drnkPtrn(ErpApiComm.getGoodsCycleTp(lineItem)) // 배달주기
                                    .prtnChnl( GoodsEnums.StoreDeliveryType.OFFICE.getCode().equals(lineItem.getStoreDeliveryTp()) // 배달장소
                                               ? ErpApiEnums.ErpPrtnChnl.OFFICE.getCode() // 오피스
                                               : ErpApiEnums.ErpPrtnChnl.HOME.getCode() ) // 홈
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .build();
    }

    /**
     * 풀무원건강생활(LDS) 주문 line 생성
     * @param lineItem
     * @return LohasDirectSaleOrderLineDto
     * @throws
     */
    public LohasDirectSaleOrderLineDto getLohasDirectSaleOrderLine(LohasDirectSaleOrderListVo lineItem) {
        return LohasDirectSaleOrderLineDto.builder()
                                    .crpCd(SourceServerTypes.LDSPHI.getCode()) // LDS PHI: LDSPHI
                                    .hrdSeq(BatchConstants.ERP_LOHAS_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_LOHAS_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .malItmNo(lineItem.getIlGoodsId()) // 상품코드
                                    .ordCnt(lineItem.getOrderCancelCnt()) // 주문수량
                                    .dlvGrp(ErpApiEnums.ErpLdsDlvGrp.CUSTOMER_DELIVERY.getCode()) // 2 : 고객택배
                                    .dlvReqDat(lineItem.getPayOutDt()) // 결제일자(YYYYMMDDHHMISS)
                                    .ordAmt(lineItem.getSalePrice()) // 판매금액
                                    .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                    .creDat(lineItem.getCreateDt()) // 데이터 생성일자(YYYYMMDDHHMISS)
                                    .ordCnlFlg(ErpApiEnums.ErpOrderSchStatus.ORDER.getCode()) // 주문구분 [ 1 : 주문 ]
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .build();
    }

    /**
     * 베이비밀 일일배송 주문 line 생성
     * @param lineItem
     * @return BabymealDailyOrderLineDto
     * @throws
     */
    public BabymealDailyOrderLineDto getBabymealDailyOrderLine(BabymealOrderListVo lineItem) {
        return BabymealDailyOrderLineDto.builder()
                                    .crpCd(SourceServerTypes.DMPHI.getCode()) // DM PHI: DMPHI
                                    .hrdSeq(BatchConstants.ERP_BABYMEAL_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_BABYMEAL_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .malItmNo( lineItem.getIlGoodsId()) // 상품코드
                                    .ordCnt( lineItem.getOrderCancelCnt() ) // 주문수량
                                    .dlvReqDat(lineItem.getDeliveryDt()) // 배송예정일(YYYYMMDDHHMISS)
                                    .stdPrc(lineItem.getRecommendedPrice()) // 상품 정상판매금액
                                    .selPrc(lineItem.getSalePrice()) // 상품 판매금액
                                    .dlvGrp( GoodsEnums.GoodsDeliveryType.NORMAL.getCode().equals(lineItem.getGoodsDeliveryType())
                                                ? ErpApiEnums.ErpBabymealDlvGrp.DELIVERY.getCode()    // 택배: 1
                                                : ErpApiEnums.ErpBabymealDlvGrp.FRANCHISEE.getCode() ) // 가맹점: 0
                                    .creDat(lineItem.getCreateDt()) // 등록일자(YYYYMMDDHHMISS)
                                    .age(lineItem.getPdmGroupCd()) // 상품그룹(품목 Code와 같은 값)
                                    .onDlvCd( "Y".equals(lineItem.getDailyBulkYn())             // 배송형태
                                              ? ErpApiEnums.ErpBabymealOnDlvCd.BATCH_DELIVERY.getCode()   // 0002 : 일괄배송
                                              : ErpApiEnums.ErpBabymealOnDlvCd.DAILY_DELIVERY.getCode() ) // 0001 : 일배배송
                                    .dlvMt(ErpApiComm.getDlvMt(lineItem)) // 배달방법
                                    .onCnt("Y".equals(lineItem.getDailyBulkYn()) // 1회배달 수량
                                            ? BatchConstants.BABYMEAL_DELIVERY  // 일괄이면 0
                                            : lineItem.getMonCnt() ) // 일배이면 1회배달 수량
                                    .wekId("Y".equals(lineItem.getDailyBulkYn()) // 주차
                                            ? BatchConstants.BABYMEAL_DELIVERY   // 일괄이면 0
                                            : StringUtil.nvlInt(GoodsEnums.GoodsCycleTermType.findByCode(lineItem.getGoodsCycleTermTp()).getTypeQty()) ) // 일배이면 주차
                                    .argYn(lineItem.getAllergyYn()) // 알러지상품 대체 여부
                                    .stoCd(lineItem.getUrStoreId()) // 가맹점 코드
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .build();
    }

    /**
     * 베이비밀 택배배송 주문 line 생성
     * @param lineItem
     * @return BabymealNormalOrderLineDto
     * @throws
     */
    public BabymealNormalOrderLineDto getBabymealNormalOrderLine(BabymealNormalOrderListVo lineItem) {
        return BabymealNormalOrderLineDto.builder()
                                    .crpCd(SourceServerTypes.DMPHI.getCode()) // DM PHI: DMPHI
                                    .hrdSeq(BatchConstants.ERP_BABYMEAL_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_BABYMEAL_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .malItmNo(lineItem.getIlGoodsId()) // 상품코드
                                    .ordCnt(lineItem.getOrderCancelCnt()) // 주문수량-주문취소수량
                                    .dlvReqDat(lineItem.getDeliveryDt()) // 배송예정일(YYYYMMDDHHMISS)
                                    .stdPrc(lineItem.getRecommendedPrice()) // 상품 정상판매금액
                                    .selPrc(lineItem.getSalePrice()) // 상품 판매금액
                                    .dlvGrp(ErpApiEnums.ErpBabymealDlvGrp.DELIVERY.getCode()) // 배송/택배 구분 - 택배: 1
                                    .creDat(lineItem.getCreateDt()) // 등록일자(YYYYMMDDHHMISS)
                                    .age(lineItem.getPdmGroupCd()) // 상품그룹(품목 Code와 같은 값)
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .onDlvCd(ErpApiEnums.ErpBabymealOnDlvCd.BATCH_DELIVERY.getCode())   // 0002 : 일괄배송
                                    .build();
    }

    /**
     * 매출 주문 (풀무원식품) line 생성
     * @param lineItem
     * @return SalesOrderFoodLineDto
     * @throws
     */
    public SalesOrderFoodLineDto getSalesOrderFoodLine(SalesOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return SalesOrderFoodLineDto.builder()
                                    .crpCd(LegalTypes.FOOD.getCode()) // 풀무원식품: PFF
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getHeaderType()+"-"+lineItem.getNowDt()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getHeaderType()+"-"+lineItem.getNowDt()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .kanCd(lineItem.getItemBarcode()) // 바코드
                                    .ordCnt(lineItem.getOrderCancelCnt()) // 주문수량-주문취소수량
                                    .dlvStrDat(lineItem.getNowDt()) // 오늘일자
                                    .dlvReqDat(lineItem.getNowDt()) // 오늘일자
                                    .selPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSaleNonTaxPrice()) // 상품판매 단가(부가세제외)
                                    .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                    .totOrdAmt(isGiftTypeGoods ? 0 : lineItem.getTotalSalePrice()) // 상품판매 총금액(부가세포함)
                                    .totOrdAmtNonTax(isGiftTypeGoods ? 0 : lineItem.getTotalSaleNonTaxPrice()) // 상품판매 총금액(부가세제외)
                                    .totOrdTax(isGiftTypeGoods ? 0 : lineItem.getTotalTaxPrice()) // 상품판매 총금액(부가세)
                                    .wahCd( ErpApiEnums.UrWarehouseId.BAEKAM_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId()) // 출고처 창고코드값
                                            ? StockWarehouseTypes.PFF_BAEGAM.getCode() // 백암물류이면 803
                                            : StockWarehouseTypes.PFF_YONGIN.getCode() ) // 나머지는 802
                                    .ctx(BatchConstants.BATCH_CONTEXT) // CONTEXT 값 고정
                                    .linCatCd(ErpApiEnums.ErpLineCategoriCode.LINE_ORDER.getCode()) // 라인유형 카테고리 코드: ORDER
                                    .linTyp( isGiftTypeGoods
                                             ? ErpApiEnums.ErpFoodOrderLineType.LINE_SHIP_ONLY.getCode()
                                             : ErpApiEnums.ErpFoodOrderLineType.LINE_STANDARD.getCode() ) // 주문 Line 유형: 일반거래일때 STANDARD, 미거래(증정품)일때 SHIP-ONLY
                                    .ordCntUom(BatchConstants.SUBJECT_UNIT) // 품목단위 고정
                                    .orgId(BatchConstants.FOOD_ORGANIZATION_CODE) // 조직코드 고정
                                    .linTypId(Integer.parseInt(ErpApiEnums.ErpFoodOrderLineId.SALES_LINE_ID.getCode())) // 주문 Line 유형 ID: 1001
                                    .ordTyp( isGiftTypeGoods
                                             ? ErpApiEnums.ErpOrderType.NON_ORDER.getCode() // 증정일때 미거래주문
                                             : ErpApiEnums.ErpOrderType.NORMAL_ORDER.getCode() ) // 일반일때 일반주문
                                    .shiToOrgId( GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd()) // 식품마케팅증정일경우
                                            ? lineItem.getGiftFoodMarketingCd()
                                            : ErpApiComm.getShiToOrgId(lineItem.getSupplierCd()) ) // 납품처 ID
                                    .ordSrc( ErpApiComm.getOrdSrc(lineItem.getSupplierCd(), lineItem.getPsWarehouseId()) ) // 주문출처
                                    .prcDat(lineItem.getNowDt()) // 오늘일자
                                    .shiPriCd( ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId())
                                                || ErpApiEnums.UrWarehouseId.BAEKAM_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId()) // 용인출고 또는 백암출고이면
                                               ? ErpApiEnums.ErpShiPriCd.YONGIN_DELIVERY.getCode()  // D-1
                                               : ErpApiEnums.ErpShiPriCd.DIRECT_DELIVERY.getCode() ) // 직송
                                    .prmDat(lineItem.getNowDt()) // 오늘일자
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .refVal02(ErpApiEnums.ErpBatchExecFl.COPY.getCode().equals(lineItem.getBatchExecFl()) ? "Y" : null ) // 매출만 연동 여부
                                    .build();
    }

    /**
     * 매출 주문 (올가홀푸드) line 생성
     * @param lineItem
     * @return SalesOrderOrgaLineDto
     * @throws
     */
    public SalesOrderOrgaLineDto getSalesOrderOrgaLine(SalesOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return SalesOrderOrgaLineDto.builder()
                                    .crpCd(LegalTypes.ORGA.getCode()) // 올가홀푸드: OGH
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getNowDt()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getNowDt()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .kanCd(lineItem.getItemBarcode()) // 바코드
                                    .ordCnt(lineItem.getOrderCancelCnt()) // 주문수량-주문취소수량
                                    .dlvStrDat(lineItem.getNowDt()) // 오늘일자
                                    .dlvReqDat(lineItem.getNowDt()) // 오늘일자
                                    .selPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSaleNonTaxOrgaPrice()) // 상품판매 단가(부가세제외)
                                    .ordTaxPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSalePrice()) // 상품판매 단가(부가세포함)
                                    .taxPrc(isGiftTypeGoods ? 0 : lineItem.getUnitTaxPrice()) // 상품판매 단가(부가세)
                                    .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                    .dlvGrpTyp(ErpApiEnums.ErpDlvGrpType.NORMAL_ORDER.getCode()) // 배송그룹유형: 일반
                                    .totOrdAmt(isGiftTypeGoods ? 0 : lineItem.getTotalSalePrice()) // 상품판매 총금액(부가세포함)
                                    .totOrdAmtNonTax(isGiftTypeGoods ? 0 : lineItem.getTotalSaleNonTaxPrice()) // 상품판매 총금액(부가세제외)
                                    .totOrdTax(isGiftTypeGoods ? 0 : lineItem.getTotalTaxPrice()) // 상품판매 총금액(부가세)
                                    .wahCd(StockWarehouseTypes.OGH_SALSE.getCode()) // 출고처 창고코드값: O20
                                    .ctx(BatchConstants.BATCH_CONTEXT) // CONTEXT 값 고정
                                    .linCatCd(ErpApiEnums.ErpLineCategoriCode.LINE_ORDER.getCode()) // 라인유형 카테고리 코드: ORDER
                                    .linTyp( ErpApiEnums.ErpBatchExecFl.COPY.getCode().equals(lineItem.getBatchExecFl()) // 주문 Line 유형
                                             ? ErpApiEnums.ErpOrgaOrderLineType.NO_LINE_VIRTUAL_WAREHOUSE_RELEASE.getCode() // 주문복사로 인한 NO_가상창고출고
                                             : ErpApiEnums.ErpOrgaOrderLineType.NO_LINE_NORMAL_DIRECT.getCode() ) // 그 이외는 NO_일반매출_직송
                                    .ordPoTyp(ErpApiEnums.ErpOrderPoType.DIRECT_DELIVERY.getCode()) // 주문/발주 연계 유형: 100
                                    .ordCntUom(BatchConstants.SUBJECT_UNIT) // 품목단위 고정
                                    .orgId(BatchConstants.ORGA_ORGANIZATION_CODE) // 조직코드 고정
                                    .recOrgId(BatchConstants.DIRECT_PARTNERS_ORGANIZATION_CODE) // 협력업체 입고 조직 고정
                                    .salCnlTyp( SystemEnums.AgentType.OUTMALL.getCode().equals(lineItem.getAgentTypeCd())
                                                ? ErpApiEnums.ErpSalesChannelType.OUTMALL_ORDER.getCode()
                                                : ErpApiEnums.ErpSalesChannelType.OUTMALL_EXCEPT_ORDER.getCode() ) // 정산유형: 외부몰이면 MIM, 나머지 일반
                                    .linTypId(Integer.parseInt(ErpApiEnums.ErpOrgaOrderLineId.DIRECT_DELIVERY_ORDER_LINE_ID.getCode())) // 주문 Line 유형 ID: 1647
                                    .ordTyp(ErpApiEnums.ErpOrderType.NO_SHOPPING_MALL.getCode()) // NO_쇼핑몰
                                    .shiToOrgId( ErpApiComm.getShiToOrgId(lineItem.getSupplierCd()) ) // 납품처 ID
                                    .ordSrc(ErpApiEnums.ErpOrgaOrderSource.SHOPPING_MALL.getCode()) // 주문출처: 쇼핑몰
                                    .prcDat(lineItem.getNowDt()) // 오늘일자
                                    .prmDat(lineItem.getNowDt()) // 오늘일자
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .refVal02(ErpApiEnums.ErpBatchExecFl.COPY.getCode().equals(lineItem.getBatchExecFl()) ? "Y" : null ) // 매출만 연동 여부
                                    .build();
    }

    /**
     * 반품매출 풀무원식품 주문 line 생성
     * @param lineItem
     * @return ReturnSalesOrderFoodLineDto
     * @throws
     */
    public ReturnSalesOrderFoodLineDto getReturnSalesOrderFoodLine(ReturnSalesOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return ReturnSalesOrderFoodLineDto.builder()
                                    .crpCd(LegalTypes.FOOD.getCode()) // 풀무원식품: PFF
                                    .hrdSeq(BatchConstants.ERP_RETURN_KEY+lineItem.getGoodsType()+"-"+lineItem.getHeaderType()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_RETURN_KEY+lineItem.getGoodsType()+"-"+lineItem.getHeaderType()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .kanCd(lineItem.getItemBarcode()) // 바코드
                                    .ordCnt(lineItem.getClaimCnt()) // 클레임수량
                                    .dlvStrDat(lineItem.getFcDt()) // 인터페이스일자
                                    .dlvReqDat(lineItem.getFcDtOne()) // 인터페이스일자+1
                                    .selPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSaleNonTaxPrice()) // 상품판매 단가(부가세제외)
                                    .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                    .totOrdAmt(isGiftTypeGoods ? 0 : lineItem.getTotalSalePrice()) // 상품판매 총금액(부가세포함)
                                    .totOrdAmtNonTax(isGiftTypeGoods ? 0 : lineItem.getTotalSaleNonTaxPrice()) // 상품판매 총금액(부가세제외)
                                    .totOrdTax(isGiftTypeGoods ? 0 : lineItem.getTotalTaxPrice()) // 상품판매 총금액(부가세)
                                    .wahCd( ErpApiEnums.UrWarehouseId.BAEKAM_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId()) // 출고처 창고코드값
                                            ? StockWarehouseTypes.PFF_BAEGAM.getCode() // 백암물류이면 803
                                            : StockWarehouseTypes.PFF_YONGIN.getCode() ) // 나머지는 802
                                    .ctx(BatchConstants.BATCH_CONTEXT) // CONTEXT 값 고정
                                    .linCatCd(ErpApiEnums.ErpLineCategoriCode.LINE_RETURN.getCode()) // 라인유형 카테고리 코드: RETURN
                                    .linTyp(ErpApiEnums.ErpFoodOrderLineType.LINE_RETURN.getCode()) // 주문 Line 유형: RETURN
                                    .ordCntUom(BatchConstants.SUBJECT_UNIT) // 품목단위 고정
                                    .orgId(BatchConstants.FOOD_ORGANIZATION_CODE) // 조직코드 고정 165
                                    .prcDat(lineItem.getCreateDt()) // 반품시 원주문의 주문일자
                                    .prmDat(lineItem.getFcDt()) // 반품일
                                    .rtnRsnDes( ErpApiComm.getFoodRtnRsnDes(lineItem.getClaimNm()) ) // 반품주문 사유(반품시 입력)
                                    .linTypId(Integer.parseInt(ErpApiEnums.ErpFoodOrderLineId.RETURN_LINE_ID.getCode())) // 주문 Line 유형 ID: 1002
                                    .ordTyp(lineItem.getClaimNm()) // 주문유형
                                    .shiToOrgId( GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd()) // 식품마케팅증정일경우
                                            ? lineItem.getGiftFoodMarketingCd()
                                            : ErpApiComm.getShiToOrgId(lineItem.getSupplierCd()) ) // 납품처 ID
                                    .ordSrc( ErpApiComm.getOrdSrc(lineItem.getSupplierCd(), lineItem.getPsWarehouseId()) ) // 주문출처
                                    .shiPriCd( ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId())
                                                    || ErpApiEnums.UrWarehouseId.BAEKAM_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId()) // 용인출고 또는 백암출고일때
                                               ? ErpApiEnums.ErpShiPriCd.YONGIN_DELIVERY.getCode() // D-1
                                               : ErpApiEnums.ErpShiPriCd.DIRECT_DELIVERY.getCode() ) // 직송
                                    .rtnOriSysDocRef(lineItem.getOriSysSeq()) // 원주문의 oriSysSeq 값
                                    .rtnOriSysLinRef(lineItem.getOdOrderDetlSeq()) // 원주문의 상품라인 값
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .refVal01(lineItem.getOdClaimDetlId()) // 주문 클레임 상세 PK
                                    .build();
    }

    /**
     * 반품매출 올가홀푸드 주문 line 생성
     * @param lineItem
     * @return ReturnSalesOrderOrgaLineDto
     * @throws
     */
    public ReturnSalesOrderOrgaLineDto getReturnSalesOrderOrgaLine(ReturnSalesOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return ReturnSalesOrderOrgaLineDto.builder()
                                    .crpCd(LegalTypes.ORGA.getCode()) // 올가홀푸드: OGH
                                    .hrdSeq(BatchConstants.ERP_RETURN_KEY+lineItem.getGoodsType()+"-"+lineItem.getOdClaimId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_RETURN_KEY+lineItem.getGoodsType()+"-"+lineItem.getOdClaimId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .kanCd(lineItem.getItemBarcode()) // 바코드
                                    .ordCnt(lineItem.getClaimCnt()) // 클레임수량
                                    .dlvStrDat(lineItem.getFcDt()) // 인터페이스일자
                                    .dlvReqDat(lineItem.getFcDtOne()) // 인터페이스일자+1
                                    .selPrc(isGiftTypeGoods ? 0 : lineItem.getUnitSaleNonTaxOrgaPrice()) // 상품판매 단가(부가세제외)
                                    .ordTaxPrc(isGiftTypeGoods? 0 : lineItem.getUnitSalePrice()) // 상품판매 단가(부가세포함)
                                    .taxPrc(isGiftTypeGoods ? 0 : lineItem.getUnitTaxPrice()) // 상품판매 단가(부가세)
                                    .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                    .dlvGrpTyp(ErpApiEnums.ErpDlvGrpType.NORMAL_ORDER.getCode()) // 배송그룹유형: 일반
                                    .totOrdAmt(isGiftTypeGoods ? 0 : lineItem.getTotalSalePrice()) // 상품판매 총금액(부가세포함)
                                    .totOrdAmtNonTax(isGiftTypeGoods ? 0 : lineItem.getTotalSaleNonTaxPrice()) // 상품판매 총금액(부가세제외)
                                    .totOrdTax(isGiftTypeGoods ? 0 : lineItem.getTotalTaxPrice()) // 상품판매 총금액(부가세)
                                    .wahCd(StockWarehouseTypes.ORGA_YONGIN.getCode()) // 출고처 창고코드값: O10
                                    .ordNoDtlCnl(lineItem.getOdOrderDetlSeq()) // 취소 또는 일부 반품 시 원주문라인 번호
                                    .ctx(BatchConstants.BATCH_CONTEXT) // CONTEXT 값 고정
                                    .linCatCd(ErpApiEnums.ErpLineCategoriCode.LINE_RETURN.getCode()) // 라인유형 카테고리 코드: RETURN
                                    .linTyp( ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId()) // 주문 Line 유형
                                             ? ErpApiEnums.ErpOrgaOrderLineType.NO_LINE_NORMAL_RETURN.getCode() // 용인물류이면 NO_일반매출_반품
                                             : ErpApiEnums.ErpOrgaOrderLineType.NO_LINE_NORMAL_DIRECT_CANCEL.getCode() ) // 아니면 NO_일반매출_직송_취소
                                    .ordPoTyp( ErpApiComm.getOrdPoTyp(lineItem) ) // 주문/발주 연계 유형
                                    .ordCntUom(BatchConstants.SUBJECT_UNIT) // 품목단위 고정
                                    .orgId(BatchConstants.ORGA_ORGANIZATION_CODE) // 조직코드 고정
                                    .prcDat(lineItem.getCreateDt()) // 반품시 원주문의 주문일자
                                    .prmDat(lineItem.getFcDt()) // 반품일
                                    .recOrgId( ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId()) // 협력업체 입고 조직
                                               ? BatchConstants.PARTNERS_ORGANIZATION_CODE // 용인물류이면 7870
                                               : BatchConstants.DIRECT_PARTNERS_ORGANIZATION_CODE ) // 아니면 7933
                                    .rtnOriSysDocRef(lineItem.getOriSysSeq()) // 원주문의 oriSysSeq 값
                                    .rtnOriSysLinRef(lineItem.getOdOrderDetlSeq()) // 원주문의 상품라인 값
                                    .rtnRsnDes(ErpApiEnums.ErpRtnRsnDes.RETURN_ORDER.getCode()) // 반품주문 사유(반품시 입력) Code
                                    .rtnTrcTyp(lineItem.getClaimCd()) // 반품 처리에 따른 구분값
                                    .salCnlTyp( SystemEnums.AgentType.OUTMALL.getCode().equals(lineItem.getAgentTypeCd())
                                                ? ErpApiEnums.ErpSalesChannelType.OUTMALL_ORDER.getCode()
                                                : ErpApiEnums.ErpSalesChannelType.OUTMALL_EXCEPT_ORDER.getCode() ) // 정산유형: 외부몰이면 MIM, 나머지 일반
                                    .linTypId( ErpApiEnums.UrWarehouseId.YONGIN_LOGISTICS.getCode().equals(lineItem.getPsWarehouseId()) // 주문 Line 유형 ID
                                               ? Integer.parseInt(ErpApiEnums.ErpOrgaOrderLineId.RETURN_LINE_ID.getCode()) // 용인물류이면 1650
                                               : Integer.parseInt(ErpApiEnums.ErpOrgaOrderLineId.DIRECT_DELIVERY_RETURN_LINE_ID.getCode()) ) // 아니면 1652
                                    .ordTyp(ErpApiEnums.ErpOrderType.NO_SHOPPING_MALL.getCode()) // NO_쇼핑몰
                                    .shiToOrgId( ErpApiComm.getShiToOrgId(lineItem.getSupplierCd()) ) // 납품처 ID
                                    .ordSrc(ErpApiEnums.ErpOrgaOrderSource.SHOPPING_MALL.getCode()) // 주문출처: 쇼핑몰
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .refVal01(lineItem.getOdClaimDetlId()) // 주문 클레임 상세 PK
                                    .build();
    }

    /**
     * 하이톡 택배배송 반품 주문 line 생성
     * @param lineItem
     * @return HitokNormalDeliveryOrderLineDto
     * @throws
     */
    public HitokNormalDeliveryReturnOrderLineDto getHitokNormalDeliveryReturnOrderLine(HitokDeliveryReturnOrderListVo lineItem) {
        boolean isGiftTypeGoods = GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd());
        return HitokNormalDeliveryReturnOrderLineDto.builder()
                                    .crpCd(SourceServerTypes.HITOK.getCode()) // 녹즙: HITOK
                                    .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdClaimId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdClaimId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목 Code
                                    .ordCnt(lineItem.getClaimCnt()) // 1회배달 수량
                                    .dlvGrp(ErpApiEnums.ErpHitokDlvGrp.NORMAL_DELIVERY.getCode()) // 1 : 택배
                                    .dlvReqDat(lineItem.getDeliveryDt()) // 배송예정일(YYYYMMDDHHMISS)
                                    .ordAmt(isGiftTypeGoods ? 0 : lineItem.getSalePrice()) // 판매가격
                                    .totOrdCnt(lineItem.getClaimCnt()) // 총수량
                                    .ordTyp( isGiftTypeGoods
                                            ? ErpApiEnums.ErpHitokOrdTypLin.PRESENTATION.getCode()
                                            : ErpApiEnums.ErpHitokOrdTypLin.ORDER.getCode() ) // 주문일때: OT01 증정일때: OT02 (행사일때: OT03) 행사상품에 대한 구분이 따로 없음
                                    .shiToOrgId(lineItem.getSupplierCd()) // 납품처 ID
                                    .ordNoDtlCnl(lineItem.getOdOrderDetlSeq()) // 원주문 라인 번호
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .refVal01(lineItem.getOdClaimDetlId()) // 주문 클레임 상세 PK
                                    .build();
    }

    /**
     * 풀무원건강생활(LDS) 반품 주문 line 생성
     * @param lineItem
     * @return LohasDirectSaleReturnOrderLineDto
     * @throws
     */
    public LohasDirectSaleReturnOrderLineDto getLohasDirectSaleReturnOrderLine(LohasDirectSaleReturnOrderListVo lineItem) {
        return LohasDirectSaleReturnOrderLineDto.builder()
                                    .crpCd(SourceServerTypes.LDSPHI.getCode()) // LDS PHI: LDSPHI
                                    .hrdSeq(BatchConstants.ERP_LOHAS_ORDER_KEY+lineItem.getOdClaimId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_LOHAS_ORDER_KEY+lineItem.getOdClaimId()+"-"+lineItem.getSeqNo()) // ERP 전용 key 값
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .malItmNo(lineItem.getIlGoodsId()) // 상품코드
                                    .ordCnt(lineItem.getClaimCnt()) // 주문수량
                                    .dlvGrp(ErpApiEnums.ErpLdsDlvGrp.CUSTOMER_DELIVERY.getCode()) // 2 : 고객택배
                                    .dlvReqDat(lineItem.getPayOutDt()) // 결제일자(YYYYMMDDHHMISS)
                                    .ordAmt(lineItem.getSalePrice()) // 판매금액
                                    .shpItmNam(lineItem.getGoodsNm()) // 상품명
                                    .creDat(lineItem.getCreateDt()) // 데이터 생성일자(YYYYMMDDHHMISS)
                                    .ordCnlFlg(ErpApiEnums.ErpOrderSchStatus.CANCEL.getCode()) // 주문구분 [ 2 : 취소 ]
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .refVal01(lineItem.getOdClaimDetlId()) // 주문 클레임 상세 PK
                                    .build();
    }

}
