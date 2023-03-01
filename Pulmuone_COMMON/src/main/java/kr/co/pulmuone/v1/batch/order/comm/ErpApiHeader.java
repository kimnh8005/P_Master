package kr.co.pulmuone.v1.batch.order.comm;

import kr.co.pulmuone.v1.batch.order.dto.header.*;
import kr.co.pulmuone.v1.batch.order.dto.vo.*;
import kr.co.pulmuone.v1.comm.api.constant.HeaderTypes;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.PhoneUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 header
 * </PRE>
 */

@Component
public class ErpApiHeader {

    /**
     * 용인물류 일반배송 주문 header 생성
     * @param headerItem
     * @param lineBindList
     * @return NormalDeliveryOrderHeaderDto
     * @throws
     */
    public NormalDeliveryOrderHeaderDto getNormalDeliveryHeader(DeliveryOrderListVo headerItem, List<?> lineBindList) {
        return NormalDeliveryOrderHeaderDto.builder()
                                      .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                      .hrdSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                      .oriSysSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                      .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 주문 1
                                      .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                      .ordDat(headerItem.getOrderDt())  // 주문일시
                                      .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                      .ordTel( "".equals(StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())))
                                               ? PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())
                                               : StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) ) // 주문자 전화번호
                                      .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 휴대폰번호
                                      .ordEml(ErpApiComm.getCutEmail(headerItem.getBuyerMail())) // 주문자 이메일
                                      .dlvNam(ErpApiComm.getCutName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()))) // 수령자명 : 대체배송시 prefix+수령자명
                                      .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                      .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                      .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                      .dlvTel( "".equals(StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())))
                                               ? PhoneUtil.makePhoneNumber(headerItem.getRecvHp())
                                               : StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) ) // 수령자 전화번호
                                      .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                      .dlvCst1(headerItem.getShippingPrice()) // 배송비
                                      .ordCls(ErpApiEnums.ErpOrderClass.SALES_ORDER.getCode()) // 주문유형: SALES ORDER
                                      .divCod(BatchConstants.BUSINESS_CODE) // 사업부 코드 고정
                                      .dlvTyp(ErpApiEnums.ErpDlvGrpType.NORMAL_ORDER.getCode()) // 배송유형: 일반
                                      .perCod(headerItem.getUrUserId()) // 회원번호
                                      .pacTyp(ErpApiEnums.ErpPackingType.NORMAL_PACKING.getCode()) // 포장유형: 일반포장
                                      .erpPicDat(headerItem.getOrderIfDt()) // 인터페이스일자
                                      .ordNumErp(headerItem.getOdid()) // 통합몰 주문번호 ERP
                                      .line(lineBindList)
                                      .build();
    }

    /**
     * 대체배송 시 수령인 이름 앞 prefix 추가
     * @param alternateDeliveryType
     * @param recvNm
     * @return
     */
    private String getAlternateRecipientName(BaseEnums.AlternateDeliveryType alternateDeliveryType, String recvNm) {
        return (alternateDeliveryType != null) ? BatchConstants.PREFIX_ALTERNATE_DELIVERY + recvNm : recvNm;
    }

    /**
     * 용인물류 새벽배송 주문 header 생성
     * @param headerItem
     * @param lineBindList
     * @return DawnDeliveryOrderHeaderDto
     * @throws
     */
    public DawnDeliveryOrderHeaderDto getDawnDeliveryHeader(DeliveryOrderListVo headerItem, List<?> lineBindList) {
        return DawnDeliveryOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 주문 1
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordDat(headerItem.getOrderDt())  // 주문일시
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel( "".equals(StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())))
                                             ? PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())
                                             : StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) ) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 휴대폰번호
                                    .ordEml(ErpApiComm.getCutEmail(headerItem.getBuyerMail())) // 주문자 이메일
                                    .dlvNam(ErpApiComm.getCutName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()))) // 수령자명 : 대체배송시 prefix+수령자명
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvTel( "".equals(StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())))
                                             ? PhoneUtil.makePhoneNumber(headerItem.getRecvHp())
                                             : StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) ) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvCst1(headerItem.getShippingPrice()) // 배송비
                                    .ordCls(ErpApiEnums.ErpOrderClass.SALES_ORDER.getCode()) // 주문유형: SALES ORDER
                                    .divCod(BatchConstants.BUSINESS_CODE) // 사업부 코드 고정
                                    .dlvTyp(ErpApiEnums.ErpDlvGrpType.NORMAL_ORDER.getCode()) // 배송유형: 일반
                                    .perCod(headerItem.getUrUserId()) // 회원번호
                                    .pacTyp(ErpApiEnums.ErpPackingType.NORMAL_PACKING.getCode()) // 포장유형: 일반포장
                                    .erpPicDat(headerItem.getOrderIfDt()) // 인터페이스일자
                                    .ordNumErp(headerItem.getOdid()) // 통합몰 주문번호 ERP
                                    .dlvErlPwd(headerItem.getDoorMsg()) // 공동현관 비밀번호
                                    .dlvErlDor(headerItem.getDoorMsgNm()) // 공동현관 출입방법
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 백암물류 주문 header 생성
     * @param headerItem
     * @param lineBindList
     * @return CjOrderHeaderDto
     * @throws
     */
    public CjOrderHeaderDto getCjHeader(CjOrderListVo headerItem, List<?> lineBindList) {
        return CjOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 주문 1
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordNam(ErpApiComm.getCjCutName(headerItem.getBuyerNm(), BatchConstants.CJ_RECIPIENT_NAME_MAX_SIZE)) // 주문자명
                                    .dlvNam(ErpApiComm.getCjCutName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()), BatchConstants.CJ_RECIPIENT_NAME_MAX_SIZE)) // 수령자명 : 대체배송시 prefix+수령자명
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvTel( "".equals(StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())))
                                             ? PhoneUtil.makePhoneNumber(headerItem.getRecvHp())
                                             : StringUtil.nvl(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) ) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvMsg(ErpApiComm.getCjCutName(headerItem.getDeliveryMsg(), 100)) // 배송메시지
                                    .shiToCj( SystemEnums.AgentType.OUTMALL.getCode().equals(headerItem.getAgentTypeCd())
                                              ? headerItem.getLogisticsGubun()
                                              : BatchConstants.CJ_ACCOUNT_CODE ) // 거래처코드
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 올가 매장배송 주문 header 생성
     * @param headerItem
     * @param lineBindList
     * @return OrgaStoreDeliveryOrderHeaderDto
     * @throws
     */
    public OrgaStoreDeliveryOrderHeaderDto getOrgaStoreDeliveryHeader(OrgaStoreDeliveryOrderListVo headerItem, List<?> lineBindList) {
        return OrgaStoreDeliveryOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 주문 1
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 전화번호
                                    .dlvNam(ErpApiComm.getCutName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()))) // 수령자명 : 대체배송시 prefix+수령자명
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvAdr1(ErpApiComm.getCutAddr(headerItem.getRecvAddr1())) // 수령자 주소 앞
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 전화번호
                                    .dlvMsg( OrderEnums.OrderStatusDetailType.SHOP_PICKUP.getCode().equals(headerItem.getOrderStatusDeliTp()) // 배달주문구분
                                            ? "" // 매장픽업 : 배송메시지 제외 SPMO-677
                                            : headerItem.getDeliveryMsg() ) // 배송메시지
                                    .creDat(headerItem.getCreateDt()) // 영업일자
                                    .shpCd(headerItem.getShpCd()) // 매장코드
                                    .salYn(ErpApiEnums.ErpSalYn.SALES_ORDER.getCode()) // 판매여부: Y 판매
                                    .salFg(Integer.parseInt(ErpApiEnums.ErpSaleFg.SALES_ORDER.getCode())) // 매출액표시기준: 1 판매
                                    //.totSalAmt(headerItem.getTotSaleAmt() + headerItem.getShippingPrice()) // 총매출액 (총 상품 할인 전 정상 판매가격기준의 주문 금액의 합 + 배송비 포함)
                                    //.totDcAmt(headerItem.getTotDcAmt()) // 총할인액 (총 상품별 할인 금액의 합)
                                    //.dcmSalAmt(headerItem.getTotSaleAmt() - headerItem.getTotDcAmt() + headerItem.getShippingPrice()) // 실매출액 (총 상품별 정산 판매가에서 할인 금액을 제외한 합계 금액 + 배송비 포함)
                                    //.vatSalAmt(headerItem.getVatSalAmt()) // 과세매출액 (상품 중 과세상품에서 실매출액 기준으로 부가세 제외한 합계 금액)
                                    //.vatAmt(headerItem.getTotSaleAmt() - headerItem.getTotDcAmt() + headerItem.getShippingPrice() - headerItem.getTotalSaleNonTaxPrice()) // 부가세 (상품 중 과세상품에서 실매출액 기준으로 부가세 합계 금액)
                                    //.noVatSalAmt(headerItem.getNoVatSalAmt()) // 면세매출액 (상품 중 면세상품의 실매출액 기준의 판매 합계 금액)
                                    //.noTaxSalAmt(headerItem.getTotalSaleNonTaxPrice() - headerItem.getTotDcAmt() + headerItem.getShippingPrice()) // 순매출액(실매출액 - 부가세 금액)
                                    .dlvOrdFg( OrderEnums.OrderStatusDetailType.SHOP_PICKUP.getCode().equals(headerItem.getOrderStatusDeliTp()) // 배달주문구분
                                                ? ErpApiEnums.ErpDlvOrdFg.STORE_PICK_UP.getCode() // 매장픽업 : 4
                                                : ErpApiEnums.ErpDlvOrdFg.STORE_DELIVERY.getCode() ) // 매장배송 : 2
                                    .posInsDt(headerItem.getCreateDt()) // 등록일시
                                    .payOutDt(headerItem.getPayOutDt()) // 결제시각
                                    .dlvFroDt(headerItem.getDlvFroDt()) // 배송 예약 시작 시간
                                    .dlvToDt(headerItem.getDlvToDt()) // 배송 예약 종료 시간
                                    .dlvIdx(headerItem.getDlvIdx()) // 배송 차수
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 올가 기타주문 header 생성
     * @param headerItem
     * @return OrgaEtcOrderHeaderDto
     * @throws
     */
    public OrgaEtcOrderHeaderDto getOrgaEtcHeader(OrgaEtcOrderListVo headerItem) {
        return OrgaEtcOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .crpCd(SourceServerTypes.ORGAOMS.getCode()) // ORGA OMS: ORGAOMS
                                    .creDat(headerItem.getCreateDt()) // 데이터 생성일자
                                    .insDat(headerItem.getCreateDt()) // 생성일
                                    .ordDat(headerItem.getCreateDt()) // 주문일시
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .ordNam(ErpApiComm.getCutDlvName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 전화번호
                                    .dlvNam(ErpApiComm.getCutDlvName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()))) // 수령자명 : 대체배송시 prefix+수령자명
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 전화번호
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 우편번호
                                    .dlvAdr1(ErpApiComm.getCutAddr(headerItem.getRecvAddr1())) // 수령자 주소
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 상세 주소
                                    .erpItmNo(headerItem.getIlItemCd()) // 품목 코드
                                    .shpItmNam(headerItem.getGoodsNm()) // 주문 상품명
                                    .ordCnt(headerItem.getOrderCancelCnt()) // 주문 수량
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordNoDtl(headerItem.getOdOrderDetlSeq()) // 통합몰 주문상세번호
                                    .shpCd(BatchConstants.ORGA_ETC_ORDER_ACCOUNT_CODE) // 매장코드
                                    .shpNam(BatchConstants.ORGA_ETC_ORDER_ACCOUNT_NAME) // 매장명
                                    .build();
    }

    /**
     * 하이톡 택배주문 header 생성
     * @param headerItem
     * @return HitokNormalDeliveryOrderListVo
     * @throws
     */
    public HitokDeliveryOrderHeaderDto getHitokNormalOrderHeader(HitokNormalDeliveryOrderListVo headerItem, List<?> lineBindList) {
        return HitokDeliveryOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 주문 1
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordHpnCd( ErpApiComm.getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                    .ordDat(headerItem.getCreateDt()) // 주문일시(YYYYMMDDHHMISS)
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 핸드폰번호
                                    .ordEml(ErpApiComm.getCutEmail(headerItem.getBuyerMail())) // 주문자 이메일
                                    .dlvNam(ErpApiComm.getCutName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()))) // 수령자명 : 대체배송시 prefix+수령자명
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvAdr1(ErpApiComm.getCutAddr(headerItem.getRecvAddr1())) // 수령자 주소 앞
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                    .bldNo(headerItem.getRecvBldNo()) // 건물번호
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .dlvErlPwd(headerItem.getDoorMsg()) // 공동현관 비밀번호
                                    .dlvErlDor(headerItem.getDoorMsgNm()) // 공동현관 출입방법
                                    .ordStuCd(headerItem.getOrdStuCnt() > 1
                                                    ? ErpApiEnums.ErpHitokOrdStuCd.RE_ORDER.getCode()
                                                    : ErpApiEnums.ErpHitokOrdStuCd.NEW_ORDER.getCode()
                                              ) // 주문상태코드 (통합몰 첫주문 여부로 판별)
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 하이톡 일배주문 header 생성
     * @param headerItem
     * @return HitokDeliveryOrderHeaderDto
     * @throws
     */
    public HitokDeliveryOrderHeaderDto getHitokkDailyOrderHeader(HitokDailyDeliveryOrderListVo headerItem, List<?> lineBindList) {
        return HitokDeliveryOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 주문 1
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordHpnCd( ErpApiComm.getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                    .ordDat(headerItem.getCreateDt()) // 주문일시(YYYYMMDDHHMISS)
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 핸드폰번호
                                    .ordEml(ErpApiComm.getCutEmail(headerItem.getBuyerMail())) // 주문자 이메일
                                    .dlvNam(ErpApiComm.getCutName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()))) // 수령자명 : 대체배송시 prefix+수령자명
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvAdr1(ErpApiComm.getCutAddr(headerItem.getRecvAddr1())) // 수령자 주소 앞
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                    .bldNo(headerItem.getRecvBldNo()) // 건물번호
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .dlvErlPwd(headerItem.getDoorMsg()) // 공동현관 비밀번호
                                    .dlvErlDor(headerItem.getDoorMsgNm()) // 공동현관 출입방법
                                    .ordStuCd(headerItem.getOrdStuCnt() > 1
                                            ? ErpApiEnums.ErpHitokOrdStuCd.RE_ORDER.getCode()
                                            : ErpApiEnums.ErpHitokOrdStuCd.NEW_ORDER.getCode()
                                    ) // 주문상태코드 (통합몰 첫주문 여부로 판별)
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 풀무원건강생활(LDS) 주문 header 생성
     * @param headerItem
     * @return LohasDirectSaleOrderHeaderDto
     * @throws
     */
    public LohasDirectSaleOrderHeaderDto getLohasDirectSaleOrderHeader(LohasDirectSaleOrderListVo headerItem, List<?> lineBindList) {
        return LohasDirectSaleOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_LOHAS_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_LOHAS_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 주문 1
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordHpnCd( ErpApiComm.getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                    .ordDat(headerItem.getCreateDt())  // 주문일시
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 휴대폰번호
                                    .dlvNam(ErpApiComm.getCutName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()))) // 수령자명 : 대체배송시 prefix+수령자명
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvAdr1(ErpApiComm.getCutAddr(headerItem.getRecvAddr1())) // 수령자 주소 앞
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvCst1(headerItem.getShippingPrice()) // 배송비
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 베이비밀 일일배송 주문 header 생성
     * @param headerItem
     * @return BabymealOrderHeaderDto
     * @throws
     */
    public BabymealOrderHeaderDto getBabymealOrderHeader(BabymealOrderListVo headerItem, List<?> lineBindList) {
        return BabymealOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_BABYMEAL_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_BABYMEAL_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 주문 1
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordHpnCd( ErpApiComm.getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                    .ordDat(headerItem.getCreateDt())  // 주문일시
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 휴대폰번호
                                    .ordEml(ErpApiComm.getCutEmail(headerItem.getBuyerMail())) // 주문자 이메일
                                    .dlvNam(ErpApiComm.getCutName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()))) // 수령자명 : 대체배송시 prefix+수령자명
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvAdr1(ErpApiComm.getCutAddr(headerItem.getRecvAddr1())) // 수령자 주소 앞
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .bldNo(headerItem.getRecvBldNo()) // 건물번호
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .dlvErlPwd(headerItem.getDoorMsg()) // 공동현관 비밀번호
                                    .dlvErlDor(headerItem.getDoorMsgNm()) // 공동현관 출입방법
                                    .creDat(headerItem.getCreateDt()) // 데이터 생성일자
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 베이비밀 택배배송 주문 header 생성
     * @param headerItem
     * @return BabymealOrderHeaderDto
     * @throws
     */
    public BabymealOrderHeaderDto getBabymealNormalOrderHeader(BabymealNormalOrderListVo headerItem, List<?> lineBindList) {
        return BabymealOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_BABYMEAL_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_BABYMEAL_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 주문 1
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordHpnCd( ErpApiComm.getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                    .ordDat(headerItem.getCreateDt())  // 주문일시
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 휴대폰번호
                                    .ordEml(ErpApiComm.getCutEmail(headerItem.getBuyerMail())) // 주문자 이메일
                                    .dlvNam(ErpApiComm.getCutName(getAlternateRecipientName(headerItem.getAlternateDeliveryType(), headerItem.getRecvNm()))) // 수령자명 : 대체배송시 prefix+수령자명
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvAdr1(ErpApiComm.getCutAddr(headerItem.getRecvAddr1())) // 수령자 주소 앞
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .bldNo(headerItem.getRecvBldNo()) // 건물번호
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .dlvErlPwd(headerItem.getDoorMsg()) // 공동현관 비밀번호
                                    .dlvErlDor(headerItem.getDoorMsgNm()) // 공동현관 출입방법
                                    .creDat(headerItem.getCreateDt()) // 데이터 생성일자
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 매출 주문 (풀무원식품) header 생성
     * @param headerItem
     * @param lineBindList
     * @return SalesOrderFoodHeaderDto
     * @throws
     */
    public SalesOrderFoodHeaderDto getSalesOrderFoodHeader(SalesOrderListVo headerItem, List<?> lineBindList) {
        return SalesOrderFoodHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getHeaderType()+"-"+headerItem.getNowDt()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getHeaderType()+"-"+headerItem.getNowDt()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE5.getCode()) // Header 유형: 매출 5
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordDat(headerItem.getNowDt())  // 오늘일자
                                    .dlvNam(ErpApiComm.getCutName(headerItem.getRecvNm())) // 수령자명
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .ordCls(ErpApiEnums.ErpOrderClass.SALES_ORDER.getCode()) // 주문유형: SALES ORDER
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 매출 주문 (올가홀푸드) header 생성
     * @param headerItem
     * @param lineBindList
     * @return SalesOrderOrgaHeaderDto
     * @throws
     */
    public SalesOrderOrgaHeaderDto getSalesOrderOrgaHeader(SalesOrderListVo headerItem, List<?> lineBindList) {
        return SalesOrderOrgaHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getNowDt()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getNowDt()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE5.getCode()) // Header 유형: 매출 5
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordDat(headerItem.getCreateDt()) // 주문일시
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 휴대폰번호
                                    .dlvNam(ErpApiComm.getCutName(headerItem.getRecvNm())) // 수령자명
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .ordCls(ErpApiEnums.ErpOrderClass.SALES_ORDER.getCode()) // 주문유형: SALES ORDER
                                    .divCod(BatchConstants.BUSINESS_CODE) // 사업부 코드 고정
                                    .dlvTyp(ErpApiEnums.ErpDlvGrpType.NORMAL_ORDER.getCode()) // 배송유형: 일반
                                    .perCod(headerItem.getUrUserId()) // 회원번호
                                    .erpPicDat(headerItem.getOrderIfDt()) // 인터페이스일자
                                    .ordNumErp(headerItem.getOdid()) // 통합몰 주문번호 ERP
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 반품매출 주문 (풀무원식품) header 생성
     * @param headerItem
     * @param lineBindList
     * @return ReturnSalesOrderHeaderDto
     * @throws
     */
    public ReturnSalesOrderHeaderDto getReturnSalesOrderFoodHeader(ReturnSalesOrderListVo headerItem, List<?> lineBindList) {
        return ReturnSalesOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_RETURN_KEY+headerItem.getGoodsType()+"-"+headerItem.getHeaderType()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_RETURN_KEY+headerItem.getGoodsType()+"-"+headerItem.getHeaderType()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE4.getCode()) // Header 유형: 반품 4
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordDat(headerItem.getFcDt()) // 반품일시
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 휴대폰번호
                                    .dlvNam(ErpApiComm.getCutName(headerItem.getRecvNm())) // 수령자명
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .ordCls(ErpApiEnums.ErpOrderClass.SALES_ORDER.getCode()) // 주문유형: SALES ORDER
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 반품매출 주문 (올가홀푸드) header 생성
     * @param headerItem
     * @param lineBindList
     * @return ReturnSalesOrderHeaderDto
     * @throws
     */
    public ReturnSalesOrderHeaderDto getReturnSalesOrderOrgaHeader(ReturnSalesOrderListVo headerItem, List<?> lineBindList) {
        return ReturnSalesOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_RETURN_KEY+headerItem.getGoodsType()+"-"+headerItem.getOdClaimId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_RETURN_KEY+headerItem.getGoodsType()+"-"+headerItem.getOdClaimId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE4.getCode()) // Header 유형: 반품 4
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordDat(headerItem.getFcDt()) // 반품일시
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 휴대폰번호
                                    .dlvNam(ErpApiComm.getCutName(headerItem.getRecvNm())) // 수령자명
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .ordCls(ErpApiEnums.ErpOrderClass.SALES_ORDER.getCode()) // 주문유형: SALES ORDER
                                    .divCod(BatchConstants.BUSINESS_CODE) // 사업부 코드 고정
                                    .dlvTyp(ErpApiEnums.ErpDlvGrpType.DELIVERY_ORDER.getCode()) // 배송유형: 택배
                                    .perCod(headerItem.getUrUserId()) // 회원번호
                                    .erpPicDat(headerItem.getFcDt()) // 인터페이스일자
                                    .ordNumErp(headerItem.getOdid()) // 통합몰 주문번호 ERP
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 하이톡 반품 주문 header 생성 (택배,일배 공통)
     * @param headerItem
     * @return HitokDeliveryReturnOrderHeaderDto
     * @throws
     */
    public HitokDeliveryReturnOrderHeaderDto getHitokReturnOrderHeader(HitokDeliveryReturnOrderListVo headerItem, List<?> lineBindList) {
        return HitokDeliveryReturnOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdClaimId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdClaimId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE4.getCode()) // Header 유형: 반품 4
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordHpnCd( ErpApiComm.getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                    .ordDat(headerItem.getFcDt()) // 반품일시(YYYYMMDDHHMISS)
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 핸드폰번호
                                    .ordEml(ErpApiComm.getCutEmail(headerItem.getBuyerMail())) // 주문자 이메일
                                    .dlvNam(ErpApiComm.getCutName(headerItem.getRecvNm())) // 수령자명
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvAdr1(ErpApiComm.getCutAddr(headerItem.getRecvAddr1())) // 수령자 주소 앞
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                    .bldNo(headerItem.getRecvBldNo()) // 건물번호
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .dlvErlPwd(headerItem.getDoorMsg()) // 공동현관 비밀번호
                                    .dlvErlDor(headerItem.getDoorMsgNm()) // 공동현관 출입방법
                                    .ordStuCd(headerItem.getOrdStuCnt() > 1
                                            ? ErpApiEnums.ErpHitokOrdStuCd.RE_ORDER.getCode()
                                            : ErpApiEnums.ErpHitokOrdStuCd.NEW_ORDER.getCode()
                                    ) // 주문상태코드 (통합몰 첫주문 여부로 판별)
                                    .rsnCd(headerItem.getClaimCd()) // 클레임사유코드
                                    .line(lineBindList)
                                    .build();
    }

    /**
     * 풀무원건강생활(LDS) 반품 주문 header 생성
     * @param headerItem
     * @return LohasDirectSaleReturnOrderHeaderDto
     * @throws
     */
    public LohasDirectSaleReturnOrderHeaderDto getLohasDirectSaleReturnOrderHeader(LohasDirectSaleReturnOrderListVo headerItem, List<?> lineBindList) {
        return LohasDirectSaleReturnOrderHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(BatchConstants.ERP_LOHAS_ORDER_KEY+ headerItem.getOdClaimId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .oriSysSeq(BatchConstants.ERP_LOHAS_ORDER_KEY+ headerItem.getOdClaimId()+"-"+headerItem.getSeqNo()) // ERP 전용 key 값
                                    .hdrTyp(HeaderTypes.TYPE4.getCode()) // Header 유형: 반품 4
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordHpnCd( ErpApiComm.getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                    .ordDat(headerItem.getCreateDt())  // 주문일시
                                    .ordNam(ErpApiComm.getCutName(headerItem.getBuyerNm())) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                    .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 휴대폰번호
                                    .dlvNam(ErpApiComm.getCutName(headerItem.getRecvNm())) // 수령자명
                                    .dlvAdr(ErpApiComm.getCutAddrAll(headerItem.getRecvAddr())) // 수령자 주소 전체
                                    .dlvAdr1(ErpApiComm.getCutAddr(headerItem.getRecvAddr1())) // 수령자 주소 앞
                                    .dlvAdr2(ErpApiComm.getCutAddr(headerItem.getRecvAddr2())) // 수령자 주소 뒤
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                    .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                    .dlvCst1(headerItem.getShippingPrice()) // 배송비
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .line(lineBindList)
                                    .build();
    }



}