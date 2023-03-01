package kr.co.pulmuone.v1.api.storedelivery.service;

import kr.co.pulmuone.v1.api.storedelivery.dto.StoreDeliveryApiHeaderDto;
import kr.co.pulmuone.v1.api.storedelivery.dto.StoreDeliveryApiLineDto;
import kr.co.pulmuone.v1.api.storedelivery.dto.vo.StoreDeliveryApiListVo;
import kr.co.pulmuone.v1.batch.order.dto.ErpIfRequestDto;
import kr.co.pulmuone.v1.comm.api.constant.HeaderTypes;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.api.StoreDeliveryApiMapper;
import kr.co.pulmuone.v1.comm.util.PhoneUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 매장배송 API Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreDeliveryApiService {

    private final StoreDeliveryApiMapper storeDeliveryApiMapper;

    private final ErpApiExchangeService erpApiExchangeService;

    /**
     * 매장배송 취소 API 송신
     * @param odOrderId
     * @param odClaimId
     * @return boolean
     * @throws BaseException
     */
    protected boolean addStoreDeliveryCancelApiSend(long odOrderId, long odClaimId) throws BaseException {

        // 매장배송 취소 클레임 데이터 확인
        String orderStatusCd = OrderEnums.OrderStatus.DELIVERY_READY.getCode(); // 배송준비중
        String claimStatusCd = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode(); // 취소완료
        String claimStatusTp = OrderClaimEnums.ClaimStatusTp.CANCEL.getCode(); // 취소
        int checkCnt = storeDeliveryApiMapper.storeDeliveryClaimCheck(odClaimId, orderStatusCd, claimStatusCd, claimStatusTp);

        if(checkCnt == 0) return false;

        return commStoreDeliveryOrderClaimApiSend(odOrderId, odClaimId);
    }

    /**
     * 매장배송 반품 API 송신
     * @param odOrderId
     * @param odClaimId
     * @return boolean
     * @throws BaseException
     */
    protected boolean addStoreDeliveryReturnApiSend(long odOrderId, long odClaimId) throws BaseException {

        // 매장배송 반품 클레임 데이터 확인
        String orderStatusCd = "";
        String claimStatusCd = OrderEnums.OrderStatus.RETURN_COMPLETE.getCode(); // 반품완료
        String claimStatusTp = OrderClaimEnums.ClaimStatusTp.RETURN.getCode(); // 재배송
        int checkCnt = storeDeliveryApiMapper.storeDeliveryClaimCheck(odClaimId, orderStatusCd, claimStatusCd, claimStatusTp);

        if(checkCnt == 0) return false;

        return commStoreDeliveryOrderClaimApiSend(odOrderId, odClaimId);
    }

    /**
     * 매장배송 재배송 API 송신
     * @param odOrderId
     * @param odClaimId
     * @return boolean
     * @throws BaseException
     */
    protected boolean addStoreDeliveryRedeliveryApiSend(long odOrderId, long odClaimId) throws BaseException {

        // 매장배송 재배송 클레임 데이터 확인
        String orderStatusCd = "";
        String claimStatusCd = OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode(); // 재배송
        String claimStatusTp = OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY.getCode(); // 재배송
        int checkCnt = storeDeliveryApiMapper.storeDeliveryClaimCheck(odClaimId, orderStatusCd, claimStatusCd, claimStatusTp);

        if(checkCnt == 0) return false;

        boolean isSuccess = commStoreDeliveryOrderClaimApiSend(odOrderId, odClaimId);

        // 매장배송 재배송 주문 API 송신 완료
        storeDeliveryApiMapper.putOrderApiCompleteUpdate(odOrderId);

        return isSuccess;
    }

    /**
     * 공통 매장배송 주문 클레임 API 송신
     * @param odOrderId
     * @param odClaimId
     * @return
     * @throws BaseException
     */
    private boolean commStoreDeliveryOrderClaimApiSend(long odOrderId, long odClaimId) throws BaseException {

        // 매장배송 전체주문 클레임 API 송신
        boolean isSuccess = storeDeliveryAllOrderClaimApiSend(odOrderId, odClaimId);

        // 매장배송 클레임 제외 주문 API 송신
        if(isSuccess) isSuccess = storeDeliveryNotClaimOrderApiSend(odOrderId, odClaimId);

        // 매장배송 취소 API 완료 업데이트
        if(isSuccess) storeDeliveryApiMapper.putStoreDeliveryCancelCompleteUpdate(odClaimId);

        return isSuccess;
    }

    /**
     * 매장배송 전체주문 클레임 API 송신
     * @param odOrderId
     * @param odClaimId
     * @return boolean
     * @throws BaseException
     */
    private boolean storeDeliveryAllOrderClaimApiSend(long odOrderId, long odClaimId) throws BaseException {

        // 매장배송 전체주문 클레임 데이터 리스트 조회
        List<StoreDeliveryApiListVo> dataList = storeDeliveryApiMapper.selectStoreDeliveryAllOrderClaimList(odOrderId, odClaimId);

        String erpSalYn = ErpApiEnums.ErpSalYn.RETURN_ORDER.getCode(); // 반품

        boolean isSuccess = storeDeliveryApiSend(dataList, erpSalYn);

        return isSuccess;
    }

    /**
     * 매장배송 클레임 제외 주문 API 송신
     * @param odOrderId
     * @param odClaimId
     * @return boolean
     * @throws BaseException
     */
    private boolean storeDeliveryNotClaimOrderApiSend(long odOrderId, long odClaimId) throws BaseException {

        // 매장배송 클레임 제외 주문 데이터 리스트 조회
        List<StoreDeliveryApiListVo> dataList = storeDeliveryApiMapper.selectStoreDeliveryNotClaimOrderList(odOrderId, odClaimId);

        if(dataList == null || dataList.size() == 0) return true;

        String erpSalYn = ErpApiEnums.ErpSalYn.SALES_ORDER.getCode(); // 판매

        boolean isSuccess = storeDeliveryApiSend(dataList, erpSalYn);

        return isSuccess;
    }

    /**
     * 매장배송 API 송신
     * @param dataList
     * @param erpSalYn
     * @return boolean
     */
    private boolean storeDeliveryApiSend(List<StoreDeliveryApiListVo> dataList, String erpSalYn) {

        boolean isSuccess;

        // line 생성
        List<StoreDeliveryApiLineDto> lineBindList = getStoreDeliveryApiLineList(dataList, erpSalYn);

        if(lineBindList != null && lineBindList.size() > 0) {

            // header 생성
            List<StoreDeliveryApiHeaderDto> headerBindList = getStoreDeliveryApiHeaderList(dataList.get(0), lineBindList, erpSalYn);

            // ERP API 송신
            isSuccess = sendErpApi(headerBindList);

        } else {
            isSuccess = false;
        }

        return isSuccess;
    }

    /**
     * 매장배송 API line List 생성
     * @param linelist
     * @param erpSalYn
     * @return List<StoreDeliveryLineDto>
     */
    private List<StoreDeliveryApiLineDto> getStoreDeliveryApiLineList(List<StoreDeliveryApiListVo> linelist, String erpSalYn) {

        List<StoreDeliveryApiLineDto> resultList = new ArrayList<>();

        for (int i = 0; i < linelist.size(); i++) {
            StoreDeliveryApiListVo lineItem = linelist.get(i);
            resultList.add(getStoreDeliveryApiLine(lineItem, erpSalYn));
        }

        return resultList;
    }

    /**
     * 매장배송 API line 생성
     * @param lineItem
     * @param erpSalYn
     * @return StoreDeliveryLineDto
     */
    private StoreDeliveryApiLineDto getStoreDeliveryApiLine(StoreDeliveryApiListVo lineItem, String erpSalYn) {
        return StoreDeliveryApiLineDto.builder()
                                    .crpCd(SourceServerTypes.ORGAOMS.getCode()) // ORGA OMS: ORGAOMS
                                    .hrdSeq( ErpApiEnums.ErpSalYn.RETURN_ORDER.getCode().equals(erpSalYn) // ERP 전용 key 값
                                            ? BatchConstants.ERP_RETURN_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()
                                            : BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo() )
                                    .oriSysSeq( ErpApiEnums.ErpSalYn.RETURN_ORDER.getCode().equals(erpSalYn) // ERP 전용 key 값
                                            ? BatchConstants.ERP_RETURN_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()
                                            : BatchConstants.ERP_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo() )
                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                    .erpItmNo(lineItem.getIlItemCd()) // 품목코드
                                    .kanCd(lineItem.getItemBarcode()) // 바코드
                                    .ordCnt(lineItem.getOrderCancelCnt()) // BUY상품적용수량
                                    .dlvReqDat(lineItem.getCreateDt()) // 영업일자
                                    .creDat(lineItem.getCreateDt()) // 데이터 생성일자
                                    .salFg( ErpApiEnums.ErpSalYn.RETURN_ORDER.getCode().equals(erpSalYn) // 매출액표시기준
                                                ? Double.parseDouble(ErpApiEnums.ErpSaleFg.RETURN_ORDER.getCode()) // -1 반품
                                                : Double.parseDouble(ErpApiEnums.ErpSaleFg.SALES_ORDER.getCode())) // 1 판매
                                    .taxYn(lineItem.getTaxYn()) // 과세여부
                                    .selPrc(lineItem.getUnitSalePrice()) // 판매단가
                                    .stdAmt(lineItem.getSalePrice()) // 매출액
                                    .salYn(erpSalYn) // 판매여부: N 반품
                                    .vatAmt(lineItem.getVatAmt()) // 부가세
                                    .posInsDt(lineItem.getCreateDt()) // POS 등록일시
                                    .shpCd(lineItem.getShpCd()) // 매장코드
                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                    .build();
    }

    /**
     * 매장배송 API Header List 생성
     * @param headerVo
     * @param lineBindList
     * @param erpSalYn
     * @return List<StoreDeliveryApiHeaderDto>
     */
    private List<StoreDeliveryApiHeaderDto> getStoreDeliveryApiHeaderList(StoreDeliveryApiListVo headerVo, List<StoreDeliveryApiLineDto> lineBindList, String erpSalYn) {

        List<StoreDeliveryApiHeaderDto> headerList = new ArrayList<>();

        headerList.add(getStoreDeliveryApiHeader(headerVo, lineBindList, erpSalYn));

        return headerList;
    }

    /**
     * 매장배송 API header 생성
     * @param headerItem
     * @param lineBindList
     * @param erpSalYn
     * @return StoreDeliveryApiHeaderDto
     */
    private StoreDeliveryApiHeaderDto getStoreDeliveryApiHeader(StoreDeliveryApiListVo headerItem, List<StoreDeliveryApiLineDto> lineBindList, String erpSalYn) {
        return StoreDeliveryApiHeaderDto.builder()
                                    .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                    .hrdSeq(ErpApiEnums.ErpSalYn.RETURN_ORDER.getCode().equals(erpSalYn) // ERP 전용 key 값
                                            ? BatchConstants.ERP_RETURN_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()
                                            : BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo() )
                                    .oriSysSeq(ErpApiEnums.ErpSalYn.RETURN_ORDER.getCode().equals(erpSalYn) // ERP 전용 key 값
                                            ? BatchConstants.ERP_RETURN_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()
                                            : BatchConstants.ERP_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo() )
                                    .hdrTyp( ErpApiEnums.ErpSalYn.RETURN_ORDER.getCode().equals(erpSalYn) // Header 유형
                                            ? HeaderTypes.TYPE4.getCode() // 반품 4
                                            : HeaderTypes.TYPE1.getCode()) // 주문 1
                                    .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                    .ordNam(headerItem.getBuyerNm()) // 주문자명
                                    .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 전화번호
                                    .dlvNam(headerItem.getRecvNm()) // 수령자명
                                    .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                    .dlvAdr1(headerItem.getRecvAddr1()) // 수령자 주소 앞
                                    .dlvAdr2(headerItem.getRecvAddr2()) // 수령자 주소 뒤
                                    .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 전화번호
                                    .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                    .creDat(headerItem.getCreateDt()) // 영업일자
                                    .shpCd(headerItem.getShpCd()) // 매장코드
                                    .salYn(erpSalYn) // 판매여부: N 반품
                                    .salFg( ErpApiEnums.ErpSalYn.RETURN_ORDER.getCode().equals(erpSalYn) // 매출액표시기준
                                            ? Integer.parseInt(ErpApiEnums.ErpSaleFg.RETURN_ORDER.getCode()) // -1 반품
                                            : Integer.parseInt(ErpApiEnums.ErpSaleFg.SALES_ORDER.getCode())) // 1 판매
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
     * ERP API 송신
     * @param headerBindList
     * @param erpInterfaceId
     * @param erpApiEnums
     * @return boolean
     */
    private boolean sendErpApi(List<?> headerBindList) {

        // ERP INTERFACE ID
        String erpInterfaceId = ErpApiEnums.ErpInterfaceId.CUST_ORDER_INSERT_INTERFACE_ID.getCode();

        // request 생성
        ErpIfRequestDto requestDto =  ErpIfRequestDto.builder()
                                    .totalPage(1)
                                    .currentPage(1)
                                    .header(headerBindList)
                                    .build();

        // ERP API 송신
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.post(requestDto, erpInterfaceId);

        // 실패시 재시도 총 3회
        boolean isSuccess = erpApiFailRetry(baseApiResponseVo, requestDto, erpInterfaceId);

        return isSuccess;
    }

    /**
     * ERP API 송신 실패시 재시도 총 3회
     * @param baseApiResponseVo
     * @param custOrdRequestDto
     * @param erpInterfaceId
     * @return boolean
     */
    private boolean erpApiFailRetry(BaseApiResponseVo baseApiResponseVo, Object custOrdRequestDto, String erpInterfaceId) {

        boolean isSuccess = baseApiResponseVo.isSuccess();

        // 실패
        if(!baseApiResponseVo.isSuccess()) {

            for (int failCnt = 0; failCnt < BatchConstants.BATCH_FAIL_RETRY_COUNT; failCnt++) {

                BaseApiResponseVo retryBaseApiResponseVo = erpApiExchangeService.post(custOrdRequestDto, erpInterfaceId);

                isSuccess = retryBaseApiResponseVo.isSuccess();

                // 성공
                if (retryBaseApiResponseVo.isSuccess()) {
                    break;
                }
                // 3번 재시도 모두 실패
                else {
                    if(failCnt == BatchConstants.BATCH_FAIL_RETRY_COUNT-1) {
                        // 실패알람
                        // TODO SMS, Slack 작업필요
                    }
                }

            }

        }

        return isSuccess;
    }

}
