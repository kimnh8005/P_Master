package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ClaimUtilRefundServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    ClaimUtilRefundService claimUtilRefundService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 추가배송비_조회_및_환불처리() throws Exception {

        OrderClaimRegisterRequestDto reqDto = new OrderClaimRegisterRequestDto();

        reqDto.setOdid("21051700045604");
        reqDto.setOdOrderId(45604);
        reqDto.setPsClaimMallId(0);

        List<OrderClaimGoodsInfoDto> goodsInfoList = new ArrayList<>();

        OrderClaimGoodsInfoDto goodsInfo = new OrderClaimGoodsInfoDto();
        goodsInfo.setOdOrderDetlId(60635);
        goodsInfoList.add(goodsInfo);
        goodsInfo = new OrderClaimGoodsInfoDto();
        goodsInfo.setOdOrderDetlId(60636);
        goodsInfoList.add(goodsInfo);

        reqDto.setGoodsInfoList(goodsInfoList);

        int updateCnt = claimUtilRefundService.putOrderClaimAddShippingPrice(reqDto);
        // equals
        Assertions.assertEquals(0, updateCnt);
    }
}