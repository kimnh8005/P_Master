package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.order.claim.dto.MallOrderClaimAddPaymentRequest;
import kr.co.pulmuone.v1.order.claim.dto.MallOrderClaimAddPaymentValidResult;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimSearchGoodsDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimViewRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ClaimRequestServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    ClaimRequestService claimRequestService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void BOS_클레임사유_조회() throws Exception {
        OrderClaimViewRequestDto orderClaimViewRequestDto= new OrderClaimViewRequestDto();
        List<OrderClaimSearchGoodsDto> dtos = new ArrayList<OrderClaimSearchGoodsDto>();

        OrderClaimSearchGoodsDto dto = new OrderClaimSearchGoodsDto();
            dto.setOdClaimDetlId(16359);
            dto.setOdOrderDetlId(60662);
        dtos.add(dto);

        orderClaimViewRequestDto.setGoodSearchList(dtos);
        orderClaimViewRequestDto.setOdClaimId(1176572);
        orderClaimViewRequestDto.setOdOrderId(45622);

        // result
        List<OrderClaimGoodsInfoDto> result = claimRequestService.getBosCalimReasonGoodsInfoList(orderClaimViewRequestDto);

        // equals
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void 배송비_추가결제정보조회_및_유효성체크() throws Exception {

        MallOrderClaimAddPaymentRequest mallOrderClaimAddPaymentRequest = new MallOrderClaimAddPaymentRequest();
        mallOrderClaimAddPaymentRequest.setOdOrderId(45490);
        mallOrderClaimAddPaymentRequest.setOdClaimId(1176499);

        OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = claimRequestService.setOrderClaimRegisterRequest(mallOrderClaimAddPaymentRequest);

        // result
        MallOrderClaimAddPaymentValidResult validResult = claimRequestService.validOrderClaimShippingPrice(orderClaimRegisterRequestDto);

        // equals
        Assertions.assertTrue(OrderClaimEnums.AddPaymentShippingPriceError.SUCCESS.getCode().equals(validResult.getValidResult().getCode()));
    }
}