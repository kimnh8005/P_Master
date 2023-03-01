package kr.co.pulmuone.v1.api.hitok.service;

import kr.co.pulmuone.v1.api.hitok.dto.*;
import kr.co.pulmuone.v1.api.hitok.dto.vo.HitokDailyDeliveryCancelOrderListVo;
import kr.co.pulmuone.v1.api.hitok.dto.vo.HitokDailyDeliveryRedeliveryOrderListVo;
import kr.co.pulmuone.v1.api.hitok.dto.vo.HitokDailyDeliveryReturnOrderListVo;
import kr.co.pulmuone.v1.batch.order.comm.ErpApiComm;
import kr.co.pulmuone.v1.batch.order.dto.ErpIfRequestDto;
import kr.co.pulmuone.v1.comm.api.constant.HeaderTypes;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.api.HitokApiMapper;
import kr.co.pulmuone.v1.comm.util.PhoneUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 하이톡 API Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class HitokApiService {

    private final HitokApiMapper hitokApiMapper;

    private final ErpApiExchangeService erpApiExchangeService;

    /**
     * 하이톡 일일배송 반품 API 송신
     * @param erpApiEnums
     * @param odClaimId
     * @throws BaseException
     */
    protected boolean sendApiHitokDailyReturnOrder(long odClaimId) throws BaseException {

        boolean isSuccess = false;

        // 하이톡 반품 데이터 리스트 조회
        List<HitokDailyDeliveryReturnOrderListVo> dataList = hitokApiMapper.selectHitokDailyDeliveryReturnOrderList(odClaimId);

        // line 생성
        List<HitokDailyDeliveryReturnOrderLineDto> lineBindList = getHitokDailyDeliveryReturnOrderLineList(dataList);

        if(lineBindList != null && lineBindList.size() > 0) {

            // header 생성
            List<HitokDailyDeliveryReturnOrderHeaderDto> headerBindList = getHitokDailyDeliveryReturnOrderHeaderList(dataList.get(0), lineBindList);

            // ERP API 송신
            isSuccess = sendErpApi(headerBindList);

            // 성공 시 하이톡 일일배송 반품 API 완료 업데이트
            if(isSuccess)
                putHitokDailyDeliveryReturnOrderCompleteUpdate(dataList);

        }

        return isSuccess;

    }
    
    /**
     * 
     * @param odClaimId
     * @return
     * @throws BaseException
     */
    protected boolean saveHitokDailyDeliveryReturnOrderCompleteUpdate(long odClaimId) throws BaseException {
        boolean isSuccess = true;
        
        // 하이톡 반품 데이터 리스트 조회
        List<HitokDailyDeliveryReturnOrderListVo> dataList = hitokApiMapper.selectHitokDailyDeliveryReturnOrderList(odClaimId);
        putHitokDailyDeliveryReturnOrderCompleteUpdate(dataList);
        
        return isSuccess;
    }

    /**
     * 하이톡 일일배송 반품 line List 생성
     * @param linelist
     * @return List<HitokDailyDeliveryReturnOrderLineDto>
     * @throws
     */
    private List<HitokDailyDeliveryReturnOrderLineDto> getHitokDailyDeliveryReturnOrderLineList(List<HitokDailyDeliveryReturnOrderListVo> linelist) {

        List<HitokDailyDeliveryReturnOrderLineDto> resultList = new ArrayList<>();

        for (int i = 0; i < linelist.size(); i++) {
            HitokDailyDeliveryReturnOrderListVo lineItem = linelist.get(i);
            resultList.add(getHitokDailyDeliveryReturnOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 하이톡 일일배송 반품 line 생성
     * @param lineItem
     * @return HitokDailyDeliveryReturnOrderLineDto
     * @throws
     */
    private HitokDailyDeliveryReturnOrderLineDto getHitokDailyDeliveryReturnOrderLine(HitokDailyDeliveryReturnOrderListVo lineItem) {
        return HitokDailyDeliveryReturnOrderLineDto.builder()
                                                    .crpCd(SourceServerTypes.HITOK.getCode()) // 녹즙: HITOK
                                                    .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdClaimId()+"-"+lineItem.getSeqNo()+HeaderTypes.TYPE4.getCode()) // ERP 전용 key 값
                                                    .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdClaimId()+"-"+lineItem.getSeqNo()+HeaderTypes.TYPE4.getCode()) // ERP 전용 key 값
                                                    .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                                    .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                                    .erpItmNo(lineItem.getIlItemCd()) // 품목 Code
                                                    .ordCnt(lineItem.getClaimCnt()) // 1회배달 수량
                                                    .dlvGrp(ErpApiEnums.ErpHitokDlvGrp.DAILY_DELIVERY.getCode()) // 0 : 일배
                                                    .dlvReqDat(lineItem.getDeliveryDt()) // 배송예정일(YYYYMMDDHHMISS)
                                                    .ordAmt(GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd()) ? 0 : lineItem.getSalePrice()) // 판매가격
                                                    .totOrdCnt(lineItem.getOrderCancelCnt()) // 총수량
                                                    .ordTyp( GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd())
                                                            ? ErpApiEnums.ErpHitokOrdTypLin.PRESENTATION.getCode()
                                                            : ErpApiEnums.ErpHitokOrdTypLin.ORDER.getCode() ) // 주문일때: OT01 증정일때: OT02 (행사일때: OT03) 행사상품에 대한 구분이 따로 없음
                                                    .shiToOrgId(lineItem.getSupplierCd()) // 납품처 ID
                                                    .schLinNo(lineItem.getOdOrderDetlDailySchSeq()) // 스케줄 라인
                                                    .stoCd(lineItem.getUrStoreId()) // 가맹점 코드
                                                    .prtnChnl( GoodsEnums.StoreDeliveryType.OFFICE.getCode().equals(lineItem.getStoreDeliveryTp()) // 배달장소
                                                            ? ErpApiEnums.ErpPrtnChnl.OFFICE.getCode() // 오피스
                                                            : ErpApiEnums.ErpPrtnChnl.HOME.getCode() ) // 홈
                                                    .ordNoDtlCnl(lineItem.getOdOrderDetlSeq()) // 원주문 라인 번호
                                                    .taxYn(lineItem.getTaxYn()) // 과세구분
                                                    .refVal01(lineItem.getOdClaimDetlId()) // 주문 클레임 상세 PK
                                                    .build();
    }

    /**
     * 하이톡 일일배송 반품 Header List 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<HitokDailyDeliveryReturnOrderHeaderDto>
     * @throws
     */
    private List<HitokDailyDeliveryReturnOrderHeaderDto> getHitokDailyDeliveryReturnOrderHeaderList(HitokDailyDeliveryReturnOrderListVo headerVo, List<HitokDailyDeliveryReturnOrderLineDto> lineBindList) {

        List<HitokDailyDeliveryReturnOrderHeaderDto> headerList = new ArrayList<>();

        headerList.add(getHitokDailyDeliveryReturnOrderHeader(headerVo, lineBindList));

        return headerList;
    }

    /**
     * 하이톡 일일배송 반품 header 생성
     * @param headerItem
     * @return HitokDailyDeliveryReturnOrderHeaderDto
     * @throws
     */
    private HitokDailyDeliveryReturnOrderHeaderDto getHitokDailyDeliveryReturnOrderHeader(HitokDailyDeliveryReturnOrderListVo headerItem, List<HitokDailyDeliveryReturnOrderLineDto> lineBindList) {
        return HitokDailyDeliveryReturnOrderHeaderDto.builder()
                                                .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                                .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdClaimId()+"-"+headerItem.getSeqNo()+HeaderTypes.TYPE4.getCode()) // ERP 전용 key 값
                                                .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdClaimId()+"-"+headerItem.getSeqNo()+HeaderTypes.TYPE4.getCode()) // ERP 전용 key 값
                                                .hdrTyp(HeaderTypes.TYPE4.getCode()) // Header 유형: 4
                                                .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                                .ordHpnCd( getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                                .ordDat(headerItem.getReturnDt()) // 반품일시(YYYYMMDDHHMISS)
                                                .ordNam(headerItem.getBuyerNm()) // 주문자명
                                                .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                                .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 핸드폰번호
                                                .ordEml(headerItem.getBuyerMail()) // 주문자 이메일
                                                .dlvNam(headerItem.getRecvNm()) // 수령자명
                                                .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                                .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                                .dlvAdr(headerItem.getRecvAddr()) // 수령자 주소 전체
                                                .dlvAdr1(headerItem.getRecvAddr1()) // 수령자 주소 앞
                                                .dlvAdr2(headerItem.getRecvAddr2()) // 수령자 주소 뒤
                                                .bldNo(headerItem.getRecvBldNo()) // 건물번호
                                                .dlvZip(headerItem.getRecvZipCd()) // 수령자 주소 우편번호
                                                .dlvMsg(headerItem.getDeliveryMsg()) // 배송메시지
                                                .dlvErlPwd(headerItem.getDoorMsg()) // 공동현관 비밀번호
                                                .dlvErlDor(headerItem.getDoorMsgNm()) // 공동현관 출입방법
                                                .ordStuCd(headerItem.getOrdStuCnt() > 1
                                                        ? ErpApiEnums.ErpHitokOrdStuCd.RE_ORDER.getCode()
                                                        : ErpApiEnums.ErpHitokOrdStuCd.NEW_ORDER.getCode()
                                                ) // 주문상태코드 (통합몰 첫주문 여부로 판별)
                                                .rsnCd(headerItem.getClaimCd()) // 반품사유
                                                .line(lineBindList)
                                                .build();
    }

    /**
     * 하이톡 일일배송 반품 API 완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    private void putHitokDailyDeliveryReturnOrderCompleteUpdate(List<HitokDailyDeliveryReturnOrderListVo> linelist) throws BaseException {

        if(CollectionUtils.isNotEmpty(linelist)) {
           List<String> odClaimDetlIdList = linelist.stream().map(x -> x.getOdClaimDetlId()).distinct().collect(Collectors.toList());
           hitokApiMapper.putHitokDailyDeliveryReturnOrderCompleteUpdate(odClaimDetlIdList);
        }
//        for(int i=0; i<linelist.size(); i++) {
//            HitokDailyDeliveryReturnOrderListVo lineItem = linelist.get(i);
//            String odClaimDetlId = lineItem.getOdClaimDetlId();
//            hitokApiMapper.putHitokDailyDeliveryReturnOrderCompleteUpdate(odClaimDetlId);
//        }
    }

    /**
     * 하이톡 일일배송 원 배송일자로 재배송 시 처리
     * @param odClaimId
     * @return
     * @throws BaseException
     */
    protected boolean sendApiHitokDailyOriginalRedeliveryOrder(long odClaimId) throws BaseException {

        boolean isSuccess = true;

        // 취소 대상 리스트
        List<HitokDailyDeliveryCancelOrderListVo> cancelList = new ArrayList<>();

        // 해당 클레임으로 취소를 위한 주문 대상 리스트 조회
        List<HitokDailyDeliveryCancelOrderListVo> dataList = hitokApiMapper.selectHitokDailyOriginalRedeliveryOrderList(odClaimId);

        // 하이톡 재배송 데이터 리스트 조회
        List<HitokDailyDeliveryRedeliveryOrderListVo> redeliveryDataList = hitokApiMapper.selectHitokDailyDeliveryRedeliveryOrderList(odClaimId);

        for(HitokDailyDeliveryCancelOrderListVo hitokDailyDeliveryCancelOrderListVo : dataList) {

            // 같은 상품의 원주문 배송일자가 재배송 배송일자랑 같은 경우
            HitokDailyDeliveryRedeliveryOrderListVo hitokDailyDeliveryRedeliveryOrderListVo = redeliveryDataList.stream().filter(
                                        vo -> hitokDailyDeliveryCancelOrderListVo.getDeliveryDt().equals(vo.getDeliveryDt()) && hitokDailyDeliveryCancelOrderListVo.getIlItemCd().equals(vo.getIlItemCd())).findAny().orElse(null);
            if(hitokDailyDeliveryRedeliveryOrderListVo != null) {

                // 원 주문 취소
                hitokApiMapper.putHitokDailyOriginalOrderCancel(hitokDailyDeliveryCancelOrderListVo.getOdOrderDetlDailySchId());

                // 재배송 클레임 주문에 원주문 수량 더하기
                hitokApiMapper.putHitokDailyDeliveryReturnClaimOrderProcess(hitokDailyDeliveryRedeliveryOrderListVo.getOdOrderDetlDailySchId(), hitokDailyDeliveryCancelOrderListVo.getOrderCnt());

                // 취소대상 리스트 담기
                cancelList.add(hitokDailyDeliveryCancelOrderListVo);

            }
        }

        // 원 주문 취소 API
        if(cancelList.size() > 0)
            isSuccess = sendApiHitokDailyCancelOrder(cancelList);

        return isSuccess;
    }

    /**
     * 하이톡 일일배송 취소 API 송신
     * @param erpApiEnums
     * @param odClaimId
     */
    private boolean sendApiHitokDailyCancelOrder(List<HitokDailyDeliveryCancelOrderListVo> dataList) {

        boolean isSuccess = false;

        // line 생성
        List<HitokDailyDeliveryCancelOrderLineDto> lineBindList = getHitokDailyDeliveryCancelOrderLineList(dataList);

        if(lineBindList != null && lineBindList.size() > 0) {

            // header 생성
            List<HitokDailyDeliveryCancelOrderHeaderDto> headerBindList = getHitokDailyDeliveryCancelOrderHeaderList(dataList.get(0), lineBindList);

            // ERP API 송신
            isSuccess = sendErpApi(headerBindList);

        }

        return isSuccess;

    }

    /**
     * 하이톡 일일배송 취소 line List 생성
     * @param linelist
     * @return List<HitokDailyDeliveryCancelOrderLineDto>
     * @throws
     */
    private List<HitokDailyDeliveryCancelOrderLineDto> getHitokDailyDeliveryCancelOrderLineList(List<HitokDailyDeliveryCancelOrderListVo> linelist) {

        List<HitokDailyDeliveryCancelOrderLineDto> resultList = new ArrayList<>();

        for (int i = 0; i < linelist.size(); i++) {
            HitokDailyDeliveryCancelOrderListVo lineItem = linelist.get(i);
            resultList.add(getHitokDailyDeliveryCancelOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 하이톡 일일배송 취소 line 생성
     * @param lineItem
     * @return HitokDailyDeliveryCancelOrderLineDto
     * @throws
     */
    private HitokDailyDeliveryCancelOrderLineDto getHitokDailyDeliveryCancelOrderLine(HitokDailyDeliveryCancelOrderListVo lineItem) {
        return HitokDailyDeliveryCancelOrderLineDto.builder()
                                                .crpCd(SourceServerTypes.HITOK.getCode()) // 녹즙: HITOK
                                                .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()+HeaderTypes.TYPE2.getCode()) // ERP 전용 key 값
                                                .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()+HeaderTypes.TYPE2.getCode()) // ERP 전용 key 값
                                                .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                                .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                                .erpItmNo(lineItem.getIlItemCd()) // 품목 Code
                                                .ordCnt(lineItem.getOrderCnt()) // 1회배달 수량
                                                .dlvGrp(ErpApiEnums.ErpHitokDlvGrp.DAILY_DELIVERY.getCode()) // 0 : 일배
                                                .dlvReqDat(lineItem.getDeliveryDt()) // 배송예정일(YYYYMMDDHHMISS)
                                                .ordAmt(GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd()) ? 0 : lineItem.getSalePrice()) // 판매가격
                                                .totOrdCnt(lineItem.getOrderCancelCnt()) // 총수량
                                                .ordTyp( GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd())
                                                        ? ErpApiEnums.ErpHitokOrdTypLin.PRESENTATION.getCode()
                                                        : ErpApiEnums.ErpHitokOrdTypLin.ORDER.getCode() ) // 주문일때: OT01 증정일때: OT02 (행사일때: OT03) 행사상품에 대한 구분이 따로 없음
                                                .shiToOrgId(lineItem.getSupplierCd()) // 납품처 ID
                                                .schLinNo(lineItem.getOdOrderDetlDailySchSeq()) // 스케줄 라인
                                                .stoCd(lineItem.getUrStoreId()) // 가맹점 코드
                                                .prtnChnl( GoodsEnums.StoreDeliveryType.OFFICE.getCode().equals(lineItem.getStoreDeliveryTp()) // 배달장소
                                                        ? ErpApiEnums.ErpPrtnChnl.OFFICE.getCode() // 오피스
                                                        : ErpApiEnums.ErpPrtnChnl.HOME.getCode() ) // 홈
                                                .ordNoDtlCnl(lineItem.getOdOrderDetlSeq()) // 원주문 라인 번호
                                                .taxYn(lineItem.getTaxYn()) // 과세구분
                                                .build();
    }

    /**
     * 하이톡 일일배송 취소 Header List 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<HitokDailyDeliveryCancelOrderHeaderDto>
     * @throws
     */
    private List<HitokDailyDeliveryCancelOrderHeaderDto> getHitokDailyDeliveryCancelOrderHeaderList(HitokDailyDeliveryCancelOrderListVo headerVo, List<HitokDailyDeliveryCancelOrderLineDto> lineBindList) {

        List<HitokDailyDeliveryCancelOrderHeaderDto> headerList = new ArrayList<>();

        headerList.add(getHitokDailyDeliveryCancelOrderHeader(headerVo, lineBindList));

        return headerList;
    }

    /**
     * 하이톡 일일배송 취소 header 생성
     * @param headerItem
     * @return HitokDailyDeliveryCancelOrderHeaderDto
     * @throws
     */
    private HitokDailyDeliveryCancelOrderHeaderDto getHitokDailyDeliveryCancelOrderHeader(HitokDailyDeliveryCancelOrderListVo headerItem, List<HitokDailyDeliveryCancelOrderLineDto> lineBindList) {
        return HitokDailyDeliveryCancelOrderHeaderDto.builder()
                                                .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                                .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()+HeaderTypes.TYPE2.getCode()) // ERP 전용 key 값
                                                .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()+HeaderTypes.TYPE2.getCode()) // ERP 전용 key 값
                                                .hdrTyp(HeaderTypes.TYPE2.getCode()) // Header 유형: 2
                                                .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                                .ordHpnCd( getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                                .ordDat(headerItem.getReturnDt()) // 반품일시(YYYYMMDDHHMISS)
                                                .ordNam(headerItem.getBuyerNm()) // 주문자명
                                                .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                                .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 핸드폰번호
                                                .ordEml(headerItem.getBuyerMail()) // 주문자 이메일
                                                .dlvNam(headerItem.getRecvNm()) // 수령자명
                                                .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                                .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                                .dlvAdr(headerItem.getRecvAddr()) // 수령자 주소 전체
                                                .dlvAdr1(headerItem.getRecvAddr1()) // 수령자 주소 앞
                                                .dlvAdr2(headerItem.getRecvAddr2()) // 수령자 주소 뒤
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
     * 하이톡 일일배송 재배송 API 송신
     * @param dataList
     * @return
     * @throws BaseException
     */
    protected boolean sendApiHitokDailyRedeliveryOrder(long odClaimId) throws BaseException {

        boolean isSuccess = false;

        // 하이톡 재배송 데이터 리스트 조회
        List<HitokDailyDeliveryRedeliveryOrderListVo> dataList = hitokApiMapper.selectHitokDailyDeliveryRedeliveryOrderList(odClaimId);

        // line 생성
        List<HitokDailyDeliveryRedeliveryOrderLineDto> lineBindList = getHitokDailyDeliveryRedeliveryOrderLineList(dataList);

        if(lineBindList != null && lineBindList.size() > 0) {

            // header 생성
            List<HitokDailyDeliveryRedeliveryOrderHeaderDto> headerBindList = getHitokDailyDeliveryRedeliveryOrderHeaderList(dataList.get(0), lineBindList);

            // ERP API 송신
            isSuccess = sendErpApi(headerBindList);

            // 성공 시 주문 배치완료 업데이트
            if(isSuccess)
                putHitokDailyDeliveryRedeliveryOrderCompleteUpdate(dataList);

        }

        return isSuccess;
    }

    /**
     * 하이톡 일일배송 재배송 line List 생성
     * @param linelist
     * @return List<HitokDailyDeliveryRedeliveryOrderLineDto>
     * @throws
     */
    private List<HitokDailyDeliveryRedeliveryOrderLineDto> getHitokDailyDeliveryRedeliveryOrderLineList(List<HitokDailyDeliveryRedeliveryOrderListVo> linelist) {

        List<HitokDailyDeliveryRedeliveryOrderLineDto> resultList = new ArrayList<>();

        for (int i = 0; i < linelist.size(); i++) {
            HitokDailyDeliveryRedeliveryOrderListVo lineItem = linelist.get(i);
            resultList.add(getHitokDailyDeliveryRedeliveryOrderLine(lineItem));
        }

        return resultList;
    }

    /**
     * 하이톡 일일배송 재배송 line 생성
     * @param lineItem
     * @return HitokDailyDeliveryRedeliveryOrderLineDto
     * @throws
     */
    public HitokDailyDeliveryRedeliveryOrderLineDto getHitokDailyDeliveryRedeliveryOrderLine(HitokDailyDeliveryRedeliveryOrderListVo lineItem) {
        return HitokDailyDeliveryRedeliveryOrderLineDto.builder()
                                                .crpCd(SourceServerTypes.HITOK.getCode()) // 녹즙: HITOK
                                                .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()+HeaderTypes.TYPE1.getCode()) // ERP 전용 key 값
                                                .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+lineItem.getOdOrderId()+"-"+lineItem.getSeqNo()+HeaderTypes.TYPE1.getCode()) // ERP 전용 key 값
                                                .ordNoDtl(lineItem.getOdOrderDetlSeq()) // 주문상품 순번
                                                .ordNum(lineItem.getOdid()) // 통합몰 주문번호
                                                .erpItmNo(lineItem.getIlItemCd()) // 품목 Code
                                                .ordCnt(lineItem.getOrderCnt()) // 1회배달 수량
                                                .dlvGrp(ErpApiEnums.ErpHitokDlvGrp.DAILY_DELIVERY.getCode()) // 0 : 일배
                                                .dlvReqDat(lineItem.getDeliveryDt()) // 배송예정일(YYYYMMDDHHMISS)
                                                .ordAmt(GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd()) ? 0 : lineItem.getSalePrice()) // 판매가격
                                                .totOrdCnt(lineItem.getOrderCancelCnt()) // 총수량
                                                .ordTyp( GoodsEnums.GoodsType.GIFT.getCode().equals(lineItem.getGoodsTpCd()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(lineItem.getGoodsTpCd())
                                                        ? ErpApiEnums.ErpHitokOrdTypLin.PRESENTATION.getCode()
                                                        : ErpApiEnums.ErpHitokOrdTypLin.ORDER.getCode() ) // 주문일때: OT01 증정일때: OT02 (행사일때: OT03) 행사상품에 대한 구분이 따로 없음
                                                .shiToOrgId(lineItem.getSupplierCd()) // 납품처 ID
                                                .schLinNo(lineItem.getOdOrderDetlDailySchSeq()) // 스케줄 라인
                                                .stoCd(lineItem.getUrStoreId()) // 가맹점 코드
                                                .drnkPtrn(getGoodsCycleTp(lineItem)) // 배달주기
                                                .prtnChnl( GoodsEnums.StoreDeliveryType.OFFICE.getCode().equals(lineItem.getStoreDeliveryTp()) // 배달장소
                                                        ? ErpApiEnums.ErpPrtnChnl.OFFICE.getCode() // 오피스
                                                        : ErpApiEnums.ErpPrtnChnl.HOME.getCode() ) // 홈
                                                .taxYn(lineItem.getTaxYn()) // 과세구분
                                                .build();
    }

    /**
     * 하이톡 일일배송 재배송 Header List 생성
     * @param headerItem
     * @param  lineBindList
     * @return List<HitokDailyDeliveryRedeliveryOrderHeaderDto>
     * @throws
     */
    private List<HitokDailyDeliveryRedeliveryOrderHeaderDto> getHitokDailyDeliveryRedeliveryOrderHeaderList(HitokDailyDeliveryRedeliveryOrderListVo headerItem, List<HitokDailyDeliveryRedeliveryOrderLineDto> lineBindList) {

        List<HitokDailyDeliveryRedeliveryOrderHeaderDto> headerList = new ArrayList<>();

        headerList.add(getHitokDailyDeliveryRedeliveryOrderHeader(headerItem, lineBindList));

        return headerList;
    }

    /**
     * 하이톡 일일배송 재배송 header 생성
     * @param headerItem
     * @return HitokDailyDeliveryRedeliveryOrderHeaderDto
     * @throws
     */
    public HitokDailyDeliveryRedeliveryOrderHeaderDto getHitokDailyDeliveryRedeliveryOrderHeader(HitokDailyDeliveryRedeliveryOrderListVo headerItem, List<HitokDailyDeliveryRedeliveryOrderLineDto> lineBindList) {
        return HitokDailyDeliveryRedeliveryOrderHeaderDto.builder()
                                                .srcSvr(SourceServerTypes.ESHOP.getCode()) // 입력 시스템 코드값: ESHOP
                                                .hrdSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()+HeaderTypes.TYPE1.getCode()) // ERP 전용 key 값
                                                .oriSysSeq(BatchConstants.ERP_HITOK_ORDER_KEY+headerItem.getOdOrderId()+"-"+headerItem.getSeqNo()+HeaderTypes.TYPE1.getCode()) // ERP 전용 key 값
                                                .hdrTyp(HeaderTypes.TYPE1.getCode()) // Header 유형: 1
                                                .ordNum(headerItem.getOdid()) // 통합몰 주문번호
                                                .ordHpnCd( ErpApiComm.getOrderHpnCd(headerItem.getAgentTypeCd(), headerItem.getBuyerTypeCd()) ) // 주문경로
                                                .ordDat(headerItem.getCreateDt()) // 주문일시(YYYYMMDDHHMISS)
                                                .ordNam(headerItem.getBuyerNm()) // 주문자명
                                                .ordTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerTel())) // 주문자 전화번호
                                                .ordMobTel(PhoneUtil.makePhoneNumber(headerItem.getBuyerHp())) // 주문자 핸드폰번호
                                                .ordEml(headerItem.getBuyerMail()) // 주문자 이메일
                                                .dlvNam(headerItem.getRecvNm()) // 수령자명
                                                .dlvTel(PhoneUtil.makePhoneNumber(headerItem.getRecvTel())) // 수령자 전화번호
                                                .dlvMobTel(PhoneUtil.makePhoneNumber(headerItem.getRecvHp())) // 수령자 휴대폰번호
                                                .dlvAdr(headerItem.getRecvAddr()) // 수령자 주소 전체
                                                .dlvAdr1(headerItem.getRecvAddr1()) // 수령자 주소 앞
                                                .dlvAdr2(headerItem.getRecvAddr2()) // 수령자 주소 뒤
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
     * 하이톡 일일배송 재배송 API 완료 업데이트
     * @param linelist
     * @return void
     * @throws BaseException
     */
    private void putHitokDailyDeliveryRedeliveryOrderCompleteUpdate(List<HitokDailyDeliveryRedeliveryOrderListVo> linelist) throws BaseException {

        for(int i=0; i<linelist.size(); i++) {
            HitokDailyDeliveryRedeliveryOrderListVo lineItem = linelist.get(i);
            String odOrderDetlDailySchId = lineItem.getOdOrderDetlDailySchId();
            hitokApiMapper.putHitokDailyDeliveryRedeliveryOrderCompleteUpdate(odOrderDetlDailySchId);
        }
    }

    /**
     * ERP API 송신
     * @param headerBindList
     * @param erpInterfaceId
     * @param erpApiEnums
     * @return void
     * @throws
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

        log.info("=============baseApiResponseVo1==============: "+baseApiResponseVo);

        // 실패시 재시도 총 3회
        boolean isSuccess = erpApiFailRetry(baseApiResponseVo, requestDto, erpInterfaceId);

        return isSuccess;
    }

    /**
     * ERP API 송신 실패시 재시도 총 3회
     * @param baseApiResponseVo
     * @param custOrdRequestDto
     * @param erpInterfaceId
     * @return void
     * @throws
     */
    private boolean erpApiFailRetry(BaseApiResponseVo baseApiResponseVo, Object custOrdRequestDto, String erpInterfaceId) {

        boolean isSuccess = baseApiResponseVo.isSuccess();

        // 실패
        if(!baseApiResponseVo.isSuccess()) {

            for (int failCnt = 0; failCnt < BatchConstants.BATCH_FAIL_RETRY_COUNT; failCnt++) {

                BaseApiResponseVo retryBaseApiResponseVo = erpApiExchangeService.post(custOrdRequestDto, erpInterfaceId);

                log.info("=============baseApiResponseVo2==============: "+baseApiResponseVo);

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

    /**
     * 하이톡 일일배송 주문 배달주기
     * @param lineItem
     * @return String
     * @throws
     */
    private static String getGoodsCycleTp(HitokDailyDeliveryRedeliveryOrderListVo lineItem) {

        // 디폴트 배달 고정
        String goodsCycleTp = ErpApiEnums.ErpGoodsCycleTp.FIXING.getCode();
        // 1일,4일 배달 비고정
        if(GoodsEnums.GoodsCycleTypeByGreenJuice.DAY1_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp())
                || GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS4_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp()))
            goodsCycleTp = ErpApiEnums.ErpGoodsCycleTp.NON_FIXING.getCode();

        // 배달요일이면 1 아니면 0
        String mon = lineItem.getMonCnt() > 0
                ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
                : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String tue = lineItem.getTueCnt() > 0
                ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
                : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String wed = lineItem.getWedCnt() > 0
                ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
                : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String thu = lineItem.getThuCnt() > 0
                ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode()
                : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String fri = ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        if(lineItem.getFriCnt() > 0)
            // 주6일이면 금요일 2 이외 1
            fri = GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS6_PER_WEEK.getCode().equals(lineItem.getGoodsCycleTp())
                    ? ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.SATURDAY_DELIVERY.getCode()
                    : ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.DELIVERY.getCode();
        // 토요일/일요일은 무조건 0
        String sun = ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();
        String sat = ErpApiEnums.ErpHitokDailyDeliveryPatterntDay.NONE_DELIVERY.getCode();

        return lineItem.getOrderCnt()+"|"+sun+mon+tue+wed+thu+fri+sat+"|"+goodsCycleTp;
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

}
