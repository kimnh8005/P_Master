package kr.co.pulmuone.v1.promotion.linkprice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.comm.mapper.promotion.advertising.LinkPriceAdvertisingMapper;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SystemUtil;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.promotion.linkprice.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * 풀무원
 * LinkPrice Service
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0       2021.07.19.           최용호         최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkPriceService {

    private final LinkPriceAdvertisingMapper linkPriceAdvertisingMapper;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * LinkPrice 저장
     * @return
     * @throws Exception
     */
    protected ApiResult<?> insertLinkPriceByOrder (LinkPriceDto linkPriceDto) throws Exception {

        int cnt = linkPriceAdvertisingMapper.insertLinkPriceByOrder(linkPriceDto);

        if(cnt == 0) return ApiResult.fail();

        return ApiResult.success();
    }

    /**
     * 링크프라이스 내역조회
     */
    protected Page<LinkPriceResultVo> getLinkPriceList(LinkPriceRequestDto dto) throws Exception {
        //상품입점 상담 조회 결과 페이지
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        return linkPriceAdvertisingMapper.getLinkPriceList(dto);
    }


    /**
     * 링크프라이스 내역조회 엑셀 다운로드
     */
    public List<LinkPriceExcelResultVo> getLinkPriceListExcel(LinkPriceRequestDto dto) throws Exception {
        List<LinkPriceExcelResultVo> itemList = linkPriceAdvertisingMapper.getLinkPriceListExcel(dto);
        // 화면과 동일하게 역순으로 no 지정
        for (int i = itemList.size() - 1; i >= 0; i--) {
            itemList.get(i).setRowNumber(String.valueOf(itemList.size() - i));
        }
        return itemList;
    }

    /**
     * 링크프라이스 내역조회 Total data 조회
     */
    public LinkPriceResponseDto getLinkPriceListTotal(LinkPriceRequestDto dto) throws Exception {
        LinkPriceResponseDto result = new LinkPriceResponseDto();
        LinkPriceTotalResultVo vo = linkPriceAdvertisingMapper.getLinkPriceListTotal(dto);
        String rtnTotalResult = vo.getTotOrdCnt() + "___" + vo.getTotOrdPrice() + "___" + vo.getTotCnclCnt() + "___" + vo.getTotCnclPrice();
        result.setTotalResult(rtnTotalResult);

        return result;
    }

    /**
     * 링크프라이 실적 결제정보 저장 및 전송
     */
    public void saveAndSendLinkPrice(PgApprovalOrderDataDto orderData) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        //1. 링크프라이스 쿠키 존재여부 체크
        if(!validateLinkPriceCookie(request) && StringUtils.isEmpty(orderData.getLpinfo())) {
            log.info("> 링크프라이스 LPINFO 쿠키 없음..");
            return;
        }

        //2. 링크프라이스 실적 정보 조회 및 전송 data 새팅
        LinkPriceDto dto = setLinkPriceData(request, orderData);
        if(dto == null) {
            log.info("> 링크프라이스 실적 정보 없음 : {}", orderData);
            return;
        }

        //3. 링크프라이 실적 저장 및 전송
        saveAndsendLinkPriceData(dto);
    }

    /**
     * userAgent 로 링크프라이스 전송용 userAgent 상수 가져오기
     */
    public String getByUserAgent(String userAgent) {
        if (StringUtils.isEmpty(userAgent)) { return null;  }
        String rtnAgent = PromotionConstants.LP_WEB_PC;
        if (!"PC".equalsIgnoreCase(DeviceUtil.getDeviceInfo(userAgent))) {
            return PromotionConstants.LP_WEB_MOBILE;
        }
        return rtnAgent;
    }

    /**
     * 링크프라이 쿠키 validation
     */
    private boolean validateLinkPriceCookie(HttpServletRequest request) throws Exception {
        String lpinfoCookie = CookieUtil.getCookie(request, PromotionConstants.COOKIE_AD_EXTERNAL_LP_CODE_KEY);
        if (StringUtils.isEmpty(lpinfoCookie)) {
            return false;
        }
        return true;
    }

    /**
     * 링크프라이 실적 결제정보 세팅
     */
    private LinkPriceDto setLinkPriceData(HttpServletRequest request, PgApprovalOrderDataDto orderData) throws Exception {
        String lpinfoCookie = CookieUtil.getCookie(request, PromotionConstants.COOKIE_AD_EXTERNAL_LP_CODE_KEY);
        String userAgent = request.getHeader("user-agent");
        String deviceType = getByUserAgent(userAgent);
        String customerIp = SystemUtil.getIpAddress(request);

        if(StringUtils.isEmpty(lpinfoCookie)) {
            lpinfoCookie = orderData.getLpinfo();
            userAgent = orderData.getUserAgent();
            deviceType = orderData.getDeviceType();
            customerIp = orderData.getIp();
        }

        if(StringUtils.isEmpty(lpinfoCookie)) {
            log.info("> 링크프라이스 LPINFO 쿠키 and PG 부가정도에도 LPINFO 없음.");
            return null;
        }

        List<String> categoryNames = new ArrayList<>();
        List<LinkPriceDto> linkPriceList = new ArrayList<LinkPriceDto>();
        List<LinkPriceOrderDetailVo> orderGoodsList = linkPriceAdvertisingMapper.getLinkPriceOrderDetailList(orderData.getOdOrderId());
        if(orderGoodsList == null || orderGoodsList.size() == 0) {
            return null;
        }
        String urUserName = orderGoodsList.get(0).getUrUserName();
        for (LinkPriceOrderDetailVo orderDetail : orderGoodsList) {
            LinkPriceDto subLinkPriceDto = new LinkPriceDto();
            subLinkPriceDto.setOrderId(orderData.getOdid());
            subLinkPriceDto.setLpinfo(lpinfoCookie);
            subLinkPriceDto.setIp(customerIp);
            subLinkPriceDto.setUserAgent(userAgent);
            subLinkPriceDto.setDeviceType(deviceType);
            subLinkPriceDto.setUrUserName(urUserName);
            subLinkPriceDto.setProductId(orderDetail.getIlGoodsId());
            linkPriceList.add(subLinkPriceDto);

            String[] categoryArray = orderDetail.getCategoryName().split("___");
            if(categoryArray != null) {
                categoryNames.clear();
                for(int i=0; i<categoryArray.length; i++) {
                    categoryNames.add(categoryArray[i]);    //대중소세 카테고리명 배열 형태 -> List 형태로 변환
                }
                orderDetail.setCategoryNames(categoryNames);
            }
            // 1. 미정산 브랜드 체크
            // 2. 임직원 할인 체크
            // 3. 배송유형 체크
            if(PromotionConstants.ignoreBrandList.contains(orderDetail.getUrBrandId())    // 베이비밀, 디자인밀, 잇슬림, 녹즙 제외
                    || "GOODS_DISCOUNT_TP.EMPLOYEE".equals(orderDetail.getDiscountTp())    // 임직원 할인 제외
                    || "DELIVERY_TYPE.DAILY".equals(orderDetail.getDeliveryType())         // 일일배송 제외
                    || "DELIVERY_TYPE.REGULAR".equals(orderDetail.getDeliveryType())       // 정기배송 제외
            ) {
                orderDetail.setCategoryCode("0000");
            }
        }

        LinkPriceDto dto = new LinkPriceDto();
        dto.setOrderId(orderData.getOdid());
        dto.setLpinfo(lpinfoCookie);
        dto.setIp(customerIp);
        dto.setUserAgent(userAgent);
        dto.setDeviceType(deviceType);
        dto.setUrUserName(urUserName);
        dto.setOrderGoodsList(orderGoodsList);
        dto.setLinkPriceList(linkPriceList);

        return dto;
    }

    /**
     * 링크프라이 실적 저장 및 전송
     */
    private void saveAndsendLinkPriceData(LinkPriceDto dto) throws Exception {
        LinkPriceAPIRequest linkPriceApiRequest = new LinkPriceAPIRequest(dto);
        linkPriceApiRequest.setProducts(dto.getOrderGoodsList());
        dto.setFinalPaidPrice(linkPriceApiRequest.getOrder().getFinalPaidPrice());
        if(dto.getOrderGoodsList() != null && dto.getOrderGoodsList().size() > 0) {
            dto.setPaidYmd(dto.getOrderGoodsList().get(0).getPaidDt());
        }
        if(dto.getFinalPaidPrice() == 0) {
            log.info("> 링크프라이스 getFinalPaidPrice : {}", 0);
            return;
        }

        //1. 링크프라이 실적 저장
        int cnt = linkPriceAdvertisingMapper.insertLinkPriceByOrder(dto);
        log.info("> 링크프라이스 실적 정보 저장 : {} / {}", dto, cnt);

        //2. 링크프라이 실적 전송
        sendLinkPriceOrder(dto, linkPriceApiRequest);
    }

    /**
     * 링크프라이 실적 전송
     */
    private void sendLinkPriceOrder(LinkPriceDto dto, LinkPriceAPIRequest linkPriceApiRequest) throws Exception {
        String json;
        try {
            json = objectMapper.writeValueAsString(linkPriceApiRequest).replaceAll("null", "\"\"");
        } catch (Exception e) {
            log.warn("> 링크프라이스 실적 json 생성 실패 : {} / {}", linkPriceApiRequest, e);
            throw new IllegalArgumentException();
        }

        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        log.info("> 링크프라이스 실적 전송 시도 : {}", json);
        dto.setSendYn("Y");
        dto.setSendData(json);
        int sendCnt = linkPriceAdvertisingMapper.updateLinkPrice(dto);
        log.info("> 링크프라이스 실적 정보 update SendYn : {} / {}", dto, sendCnt);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(PromotionConstants.LP_REQUEST_URL, request, String.class);
            LinkPriceAPIResponse[] linkPriceAPIResponse = objectMapper.readValue(responseEntity.getBody(), LinkPriceAPIResponse[].class);

            String successYn = "Y";
            String returnData = "";
            for (LinkPriceAPIResponse response : linkPriceAPIResponse) {
                log.info("> 링크프라이스 실적 전송 결과 : {}", response);
                if (!response.isSuccess() && "Y".equals(successYn)) {
                    log.info("> 링크프라이스 실적 전송 오류 : {}", response);
                    successYn = "N";
                }
                if("".equals(returnData)) {
                    returnData = response.toString();
                } else {
                    returnData = returnData + "," + response.toString();
                }
            }
            dto.setSuccessYn(successYn);
            dto.setReturnData(returnData);
            int successCnt = linkPriceAdvertisingMapper.updateLinkPrice(dto);
            log.info("> 링크프라이스 실적 정보 update SuccessYn : {} / {}", dto, successCnt);
        } catch (Exception e) {
            log.warn("> 링크프라이스 실적 전송 실패 : {} / {}", json, e);
        }
    }

    /**
     * 링크프라이스 내역조회 API(링크프라이스에 제공하는 용도)
     */
    public List<LinkPriceOrderListAPIResponseDto> getLinkPriceOrderListAPIInfo(String userAgent, String type, String ymd) throws Exception {

        List<LinkPriceOrderListAPIResponseDto> orderInfoList = null;

        HashMap<String, String> orderInfoSearch = new HashMap<>();
        orderInfoSearch.put("userAgent", userAgent);
        orderInfoSearch.put("type", type);
        orderInfoSearch.put("ymd", ymd);

        if(PromotionConstants.LP_SEARCH_TYPE_PAID.equals(type)) {
            orderInfoList = linkPriceAdvertisingMapper.getLinkPriceOrderListForPaid(orderInfoSearch);
        }

        // 취소는 수기정산으로 처리하기로함.
//        if(PromotionConstants.LP_SEARCH_TYPE_CANCELED.equals(type)) {
//            orderInfoList = linkPriceAdvertisingMapper.getLinkPriceOrderListForCanceled(orderInfoSearch);
//        }

        return orderInfoList;
    }

    /**
     * 가상계좌 실적 결제정보 저장
     */
    public void virtualBankSaveLinkPrice(PgApprovalOrderDataDto orderData) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        //1. 링크프라이스 쿠키 존재여부 체크
        if(!validateLinkPriceCookie(request) && StringUtils.isEmpty(orderData.getLpinfo())) {
            log.info("> 링크프라이스 LPINFO 쿠키 없음..");
            return;
        }

        //2. 링크프라이스 실적 정보 조회 및 전송 data 새팅
        LinkPriceDto dto = setLinkPriceData(request, orderData);
        if(dto == null) {
            log.info("> 링크프라이스 실적 정보 없음 : {}", orderData);
            return;
        }

        //3. 링크프라이 실적 저장
        saveLinkPriceData(dto);
    }

    /**
     * 가상계좌 링크프라이 실적 저장
     */
    private void saveLinkPriceData(LinkPriceDto dto) throws Exception {
        LinkPriceAPIRequest linkPriceApiRequest = new LinkPriceAPIRequest(dto);
        linkPriceApiRequest.setProducts(dto.getOrderGoodsList());
        dto.setFinalPaidPrice(linkPriceApiRequest.getOrder().getFinalPaidPrice());
        if(dto.getOrderGoodsList() != null && dto.getOrderGoodsList().size() > 0) {
            dto.setPaidYmd(dto.getOrderGoodsList().get(0).getPaidDt());
        }
        if(dto.getFinalPaidPrice() == 0) {
            log.info("> 링크프라이스 getFinalPaidPrice : {}", 0);
            return;
        }

        //1. 링크프라이 실적 저장
        int cnt = linkPriceAdvertisingMapper.insertLinkPriceByOrder(dto);
        log.info("> 링크프라이스 실적 정보 저장 : {} / {}", dto, cnt);
    }

    /**
     * 가상계좌 실적 결제정보 전송
     */
    public void virtualBankSendLinkPrice(PgApprovalOrderDataDto orderData) throws Exception {
        //1. 가상계좌 링크프라이스 실적 정보 조회 및 전송 data 새팅
        LinkPriceDto dto = setVirtualBankLinkPriceData(orderData);
        if(dto == null) {
            log.info("> 가상계좌 링크프라이스 실적 정보 없음 : {}", orderData);
            return;
        }

        //2. 링크프라이 실적 전송
        LinkPriceAPIRequest linkPriceApiRequest = new LinkPriceAPIRequest(dto);
        linkPriceApiRequest.setProducts(dto.getOrderGoodsList());
        dto.setFinalPaidPrice(linkPriceApiRequest.getOrder().getFinalPaidPrice());
        if(dto.getOrderGoodsList() != null && dto.getOrderGoodsList().size() > 0) {
            dto.setPaidYmd(dto.getOrderGoodsList().get(0).getPaidDt());
        }
        if(dto.getFinalPaidPrice() == 0) {
            log.info("> 링크프라이스 getFinalPaidPrice : {}", 0);
            return;
        }
        sendLinkPriceOrder(dto, linkPriceApiRequest);
    }

    /**
     * 가상계좌 링크프라이 실적 결제정보 세팅
     */
    private LinkPriceDto setVirtualBankLinkPriceData(PgApprovalOrderDataDto orderData) throws Exception {
        List<String> categoryNames = new ArrayList<>();
        List<LinkPriceDto> linkPriceList = new ArrayList<LinkPriceDto>();
        List<LinkPriceOrderDetailVo> orderGoodsList = linkPriceAdvertisingMapper.getVirtualBankLinkPriceOrderDetailList(orderData.getOdid());
        if(orderGoodsList == null || orderGoodsList.size() == 0) {
            log.info("> 가상계좌 링크프라이스 실적 정보 없음 : {}", orderData);
            return null;
        }
        String urUserName = orderGoodsList.get(0).getUrUserName();
        String lpinfoCookie = orderGoodsList.get(0).getLpinfo();
        String customerIp = orderGoodsList.get(0).getIp();
        String userAgent = orderGoodsList.get(0).getUserAgent();
        String deviceType = orderGoodsList.get(0).getDeviceType();
        for (LinkPriceOrderDetailVo orderDetail : orderGoodsList) {
            LinkPriceDto subLinkPriceDto = new LinkPriceDto();
            subLinkPriceDto.setOrderId(orderData.getOdid());
            subLinkPriceDto.setLpinfo(lpinfoCookie);
            subLinkPriceDto.setIp(customerIp);
            subLinkPriceDto.setUserAgent(userAgent);
            subLinkPriceDto.setDeviceType(deviceType);
            subLinkPriceDto.setUrUserName(urUserName);
            subLinkPriceDto.setProductId(orderDetail.getIlGoodsId());
            linkPriceList.add(subLinkPriceDto);

            String[] categoryArray = orderDetail.getCategoryName().split("___");
            if(categoryArray != null) {
                categoryNames.clear();
                for(int i=0; i<categoryArray.length; i++) {
                    categoryNames.add(categoryArray[i]);    //대중소세 카테고리명 배열 형태 -> List 형태로 변환
                }
                orderDetail.setCategoryNames(categoryNames);
            }
            // 1. 미정산 브랜드 체크
            // 2. 임직원 할인 체크
            // 3. 배송유형 체크
            if(PromotionConstants.ignoreBrandList.contains(orderDetail.getUrBrandId())    // 베이비밀, 디자인밀, 잇슬림, 녹즙 제외
                    || "GOODS_DISCOUNT_TP.EMPLOYEE".equals(orderDetail.getDiscountTp())    // 임직원 할인 제외
                    || "DELIVERY_TYPE.DAILY".equals(orderDetail.getDeliveryType())         // 일일배송 제외
                    || "DELIVERY_TYPE.REGULAR".equals(orderDetail.getDeliveryType())       // 정기배송 제외
            ) {
                orderDetail.setCategoryCode("0000");
            }
        }

        LinkPriceDto dto = new LinkPriceDto();
        dto.setOrderId(orderData.getOdid());
        dto.setLpinfo(lpinfoCookie);
        dto.setIp(customerIp);
        dto.setUserAgent(userAgent);
        dto.setDeviceType(deviceType);
        dto.setUrUserName(urUserName);
        dto.setOrderGoodsList(orderGoodsList);
        dto.setLinkPriceList(linkPriceList);

        return dto;
    }
}
