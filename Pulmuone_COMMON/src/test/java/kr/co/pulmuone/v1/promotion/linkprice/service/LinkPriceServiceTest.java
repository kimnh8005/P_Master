package kr.co.pulmuone.v1.promotion.linkprice.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.linkprice.dto.LinkPriceOrderListAPIResponseDto;
import kr.co.pulmuone.v1.promotion.manage.service.EventManageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LinkPriceServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private LinkPriceService linkPriceService;

    @Test
    public void 이미존재하는_LPINFO_GOODS쿠키에_새로운_상품번호_추가() throws Exception {}

    @Test
    public void LPINFO_GOODS쿠키_존재하지않으면_새로생성() throws Exception {}

    @Test
    public void LPINFO쿠키_없으면_LPINFO_GOODS쿠키는_null() throws Exception {}

    @Test
    public void given_succeed_api() throws Exception {}

    @Test
    public void 임직원_주문_상품제외() throws Exception {}

    @Test
    public void 정기배송주문_제외() throws Exception {}

    @Test
    public void 존재하지_않은_주문번호와_상품번호로_요청할떄() throws Exception {}

    @Test
    public void 일일배송상품_제외() throws Exception {}

    @Test
    public void null값으로_요청할_때() throws Exception {}

    @Test
    public void 요청상품개수3개_validation통과_0개() throws Exception {}

    @Test
    public void 묶음상품일경우_하위상품조회() throws Exception {}

    @Test
    public void LPINFO_쿠키값_디코딩() throws Exception {}

    @Test
    public void LINKPRICE_ORDERLIST_API_호출() throws Exception {
        String userAgent = "web-pc";
        String ymdType = "PAID";    // PAID, CONFIRMED, CANCELED
        String Ymd = "20210906";

        List<LinkPriceOrderListAPIResponseDto> list = linkPriceService.getLinkPriceOrderListAPIInfo(userAgent, ymdType, Ymd);

        System.out.println("###################################################");
        System.out.println("LinkPriceOrderListAPIResponseDto : " + list);
    }

}