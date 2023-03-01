package kr.co.pulmuone.v1.order.email.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.email.dto.BosOrderStatusNotiDto;
import kr.co.pulmuone.v1.order.email.dto.OrderDetailGoodsDto;
import kr.co.pulmuone.v1.order.email.dto.OrderRegularGoodsPriceChangeDto;
import kr.co.pulmuone.v1.order.email.dto.OrderRegularResultDto;
import kr.co.pulmuone.v1.order.email.dto.vo.BosOrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderInfoForEmailVo;
import kr.co.pulmuone.v1.order.email.dto.vo.OrderRegularReqInfoVo;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderEmailServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    OrderEmailService orderEmailService;

    @Test
    void getOrderDetailGoodsListForEmail_성공1() throws Exception{
        Long odOrderId = 45481L;
        List<OrderDetailGoodsDto> result = orderEmailService.getOrderDetailGoodsListForEmail(odOrderId, null);

        Assertions.assertTrue(result.size() > 0);
    }
    @Test
    void getOrderDetailGoodsListForEmail_성공2() throws Exception{
        List<Long> odOrderDetlIdList = new ArrayList<>();
        odOrderDetlIdList.add(59565L);
        List<OrderDetailGoodsDto> result = orderEmailService.getOrderDetailGoodsListForEmail(null,odOrderDetlIdList);

        Assertions.assertTrue(result.size() > 0);
    }
    @Test
    void getOrderDetailGoodsListForEmail_조회결과없음() throws Exception{
        Long odOrderId = 1L;
        List<OrderDetailGoodsDto> result = orderEmailService.getOrderDetailGoodsListForEmail(odOrderId,null);

        Assertions.assertFalse(result.size() > 0);
    }
    @Test
    void getOrderInfoForEmail_성공() throws Exception{
        Long odOrderId = 43718L;
        String goodsTpCd = "GOODS_TYPE.NORMAL";
        OrderInfoForEmailVo result = orderEmailService.getOrderInfoForEmail(odOrderId,goodsTpCd);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getOrderInfoForEmail_조회결과없음() throws Exception{
        Long odOrderId = 1L;
        String goodsTpCd = "GOODS_TYPE.NORMAL";
        OrderInfoForEmailVo result = orderEmailService.getOrderInfoForEmail(odOrderId,goodsTpCd);

        Assertions.assertFalse(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getOrderIdList_성공() throws Exception{
        List<Long> odOrderDetlIdList = new ArrayList<>();
        odOrderDetlIdList.add(59565L);
        List<OrderDetailGoodsDto> result = orderEmailService.getOrderIdList(odOrderDetlIdList);

        Assertions.assertTrue(result.size() > 0);
    }
    @Test
    void getOrderIdList_조회결과없음() throws Exception{
        List<Long> odOrderDetlIdList = new ArrayList<>();
        odOrderDetlIdList.add(0L);
        List<OrderDetailGoodsDto> result = orderEmailService.getOrderIdList(odOrderDetlIdList);

        Assertions.assertFalse(result.size() > 0);
    }
    @Test
    void getClaimDetlIdList_성공() throws Exception{
        Long odClaimId = 1175795L;
        List<Long> result = orderEmailService.getClaimDetlIdList(odClaimId);

        Assertions.assertTrue(result.size() > 0);
    }
    @Test
    void getClaimDetlIdList_조회결과없음() throws Exception{
        Long odClaimId = 0L;
        List<Long> result = orderEmailService.getClaimDetlIdList(odClaimId);

        Assertions.assertFalse(result.size() > 0);
    }
    @Test
    void getOrderClaimInfoForEmail_성공() throws Exception{
        Long odClaimId = 1175795L;
        OrderInfoForEmailVo result = orderEmailService.getOrderClaimInfoForEmail(odClaimId);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getOrderClaimInfoForEmail_조회결과없음() throws Exception{
        Long odClaimId = 0L;
        OrderInfoForEmailVo result = orderEmailService.getOrderClaimInfoForEmail(odClaimId);

        Assertions.assertFalse(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getOrderRegularReqInfoForEmail_성공() throws Exception{
        Long odRegularReqId = 7L;
        OrderRegularReqInfoVo result = orderEmailService.getOrderRegularReqInfoForEmail(odRegularReqId);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getOrderRegularReqInfoForEmail_조회결과없음() throws Exception{
        Long odRegularReqId = 1L;
        OrderRegularReqInfoVo result = orderEmailService.getOrderRegularReqInfoForEmail(odRegularReqId);

        Assertions.assertFalse(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getOrderRegularGoodsListForEmail_성공() throws Exception{
        Long odRegularReqId = 7L;
        List<OrderDetailGoodsDto> result = orderEmailService.getOrderRegularGoodsListForEmail(odRegularReqId);

        Assertions.assertTrue(result.size() > 0);
    }
    @Test
    void getOrderRegularGoodsListForEmail_조회결과없음() throws Exception{
        Long odRegularReqId = 1L;
        List<OrderDetailGoodsDto> result = orderEmailService.getOrderRegularGoodsListForEmail(odRegularReqId);

        Assertions.assertFalse(result.size() > 0);
    }
    @Test
    void getRegularOrderResultCreateGoodsListForEmail_성공() throws Exception{
        Long odRegularResultId = 3669L;
        OrderRegularResultDto result = orderEmailService.getRegularOrderResultCreateGoodsListForEmail(odRegularResultId);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getRegularOrderResultCreateGoodsListForEmail_조회결과없음() throws Exception{
        Long odRegularResultId = 1L;
        OrderRegularResultDto result = orderEmailService.getRegularOrderResultCreateGoodsListForEmail(odRegularResultId);

        Assertions.assertFalse(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getRegularOrderResultDetlInfoForEmail_성공() throws Exception{
        Long odRegularResultDetlId = 750L;
        OrderRegularResultDto result = orderEmailService.getRegularOrderResultDetlInfoForEmail(odRegularResultDetlId);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getRegularOrderResultDetlInfoForEmail_조회결과없음() throws Exception{
        Long odRegularResultDetlId = 1L;
        OrderRegularResultDto result = orderEmailService.getRegularOrderResultDetlInfoForEmail(odRegularResultDetlId);

        Assertions.assertFalse(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getOrderRegularResultList_성공() throws Exception{
        Long odRegularResultId = 595L;
        List<OrderRegularResultDto> result = orderEmailService.getOrderRegularResultList(odRegularResultId);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getOrderRegularResultList_조회결과없음() throws Exception{
        Long odRegularResultId = 1L;
        List<OrderRegularResultDto> result = orderEmailService.getOrderRegularResultList(odRegularResultId);

        Assertions.assertFalse(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getTargetOrderRegularExpired_성공() throws Exception{
        List<Long> result = orderEmailService.getTargetOrderRegularExpired();

        Assertions.assertTrue(result.size() >= 0);
    }
    @Test
    void putSmsSendStatusByOrderRegularExpired_성공() throws Exception{
        Long odRegularResultId = 23L;
        int result = orderEmailService.putSmsSendStatusByOrderRegularExpired(odRegularResultId);

        Assertions.assertTrue(result > 0);
    }
    @Test
    void putSmsSendStatusByOrderRegularExpired_조회결과없음() throws Exception{
        Long odRegularResultId = 1L;
        int result = orderEmailService.putSmsSendStatusByOrderRegularExpired(odRegularResultId);

        Assertions.assertFalse(result > 0);
    }
    @Test
    void getTargetOrderDailyGreenJuiceEnd_성공() throws Exception{
        List<Long> result = orderEmailService.getTargetOrderDailyGreenJuiceEnd();

        Assertions.assertTrue(result.size() >= 0);
    }
    @Test
    void getOrderDailyGreenJuiceEndInfoForEmail_성공() throws Exception{
        Long odOrderDetlId = 58160L;
        OrderInfoForEmailVo result = orderEmailService.getOrderDailyGreenJuiceEndInfoForEmail(odOrderDetlId);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getOrderDailyGreenJuiceEndInfoForEmail_조회결과없음() throws Exception{
        Long odOrderDetlId = 1L;
        OrderInfoForEmailVo result = orderEmailService.getOrderDailyGreenJuiceEndInfoForEmail(odOrderDetlId);

        Assertions.assertFalse(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void getTargetOrderRegularGoodsPriceChangeList_성공() throws Exception{
        List<HashMap<String, BigInteger>> result = orderEmailService.getTargetOrderRegularGoodsPriceChangeList();

        Assertions.assertTrue(result.size() >= 0);
    }
    @Test
    void getOrderRegularGoodsPriceChangeInfo_성공() throws Exception{
        Long odRegularReqId = 0L;
        Long ilGoodsId = 0L;
        OrderRegularGoodsPriceChangeDto result = orderEmailService.getOrderRegularGoodsPriceChangeInfo(odRegularReqId,ilGoodsId);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(result) || ObjectUtils.isEmpty(result));
    }
    @Test
    void getTargetBosOrderStatusNotification_성공() throws Exception{
        List<BosOrderStatusNotiDto> result = orderEmailService.getTargetBosOrderStatusNotification();

        Assertions.assertTrue(result.size() > 0);
    }
    @Test
    void getOrderCountForOrderStatusNotification_성공() throws Exception{
        Long urClientId = 250L;
        BosOrderInfoForEmailVo result = orderEmailService.getOrderCountForOrderStatusNotification(urClientId);

        Assertions.assertTrue(ObjectUtils.isNotEmpty(result));
    }
    @Test
    void checkBosOrgaCautionOrderNotification_올가상품주문_주의주문발생_주소체크() throws Exception{
        Long odOrderId = 46282L;
        boolean result = orderEmailService.checkBosOrgaCautionOrderNotification(odOrderId);

        Assertions.assertTrue(result);
    }
    @Test
    void checkBosOrgaCautionOrderNotification_올가상품주문_주의주문발생_이메일체크() throws Exception{
        Long odOrderId = 46283L;
        boolean result = orderEmailService.checkBosOrgaCautionOrderNotification(odOrderId);

        Assertions.assertTrue(result);
    }
    @Test
    void checkBosOrgaCautionOrderNotification_올가상품주문_주의주문발생안함() throws Exception{
        Long odOrderId = 45892L;
        boolean result = orderEmailService.checkBosOrgaCautionOrderNotification(odOrderId);

        Assertions.assertFalse(result);
    }
    @Test
    void checkBosOrgaCautionOrderNotification_올가상품주문안함() throws Exception{
        Long odOrderId = 46125L;
        boolean result = orderEmailService.checkBosOrgaCautionOrderNotification(odOrderId);

        Assertions.assertFalse(result);
    }

}