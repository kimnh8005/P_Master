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
public class ClaimProcessServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    ClaimProcessService claimProcessService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }


    @Test
    void BOS_클레임사유변경() throws Exception {
        OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = new OrderClaimRegisterRequestDto();
        List<OrderClaimGoodsInfoDto> dtos = new ArrayList<OrderClaimGoodsInfoDto>();

        OrderClaimGoodsInfoDto dto = new OrderClaimGoodsInfoDto();
            dto.setPsClaimBosId(69);
            dto.setPsClaimBosSupplyId(214);
            dto.setBosClaimLargeId(145);
            dto.setBosClaimMiddleId(17);
            dto.setBosClaimSmallId(124);
            dto.setOdClaimId(1176160);
            dto.setOdClaimDetlId(15787);
        dtos.add(dto);

        orderClaimRegisterRequestDto.setGoodsInfoList(dtos);

        // result
        int updateCnt =  claimProcessService.putOrderClaimDetlBosClaimReason(orderClaimRegisterRequestDto);

        log.info("BOS_클레임사유변경 updateCnt : {}",  updateCnt);

        // equals
        Assertions.assertTrue(updateCnt > 0);
    }

    @Test
    void BOS_클레임사유변경_이력등록() throws Exception {
        OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = new OrderClaimRegisterRequestDto();
        List<OrderClaimGoodsInfoDto> dtos = new ArrayList<OrderClaimGoodsInfoDto>();

        OrderClaimGoodsInfoDto dto = new OrderClaimGoodsInfoDto();
            dto.setPsClaimBosId(69);
            dto.setPsClaimBosSupplyId(214);
            dto.setBosClaimLargeId(145);
            dto.setBosClaimMiddleId(17);
            dto.setBosClaimSmallId(124);
            dto.setOdClaimId(1176160);
            dto.setOdClaimDetlId(15787);
        dtos.add(dto);

        orderClaimRegisterRequestDto.setUrUserId("1");
        orderClaimRegisterRequestDto.setGoodsInfoList(dtos);

        // result
        int insertCnt =  claimProcessService.addOrderClaimBosReasonHist(orderClaimRegisterRequestDto);

        log.info("BOS_클레임사유변경_이력등록 insertCnt : {}",  insertCnt);

        // equals
        Assertions.assertTrue(insertCnt > 0);
    }
}