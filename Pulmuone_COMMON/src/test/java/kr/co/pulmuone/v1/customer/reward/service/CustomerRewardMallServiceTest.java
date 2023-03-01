package kr.co.pulmuone.v1.customer.reward.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CustomerEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.customer.reward.dto.*;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyInfoVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardPageInfoVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRewardMallServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private CustomerRewardMallService customerRewardMallService;

    @Test
    void getRewardPageInfo_조회_성공() throws Exception {
        //given
        String deviceType = "PC";

        // when
        List<RewardPageInfoVo> result = customerRewardMallService.getRewardPageInfo(deviceType);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void addReward_등록_성공() {
        //given
        RewardRequestDto dto = new RewardRequestDto();
        dto.setCsRewardId(1L);
        dto.setUrUserId(1L);
        dto.setOdOrderId(1L);
        dto.setOdOrderDetlId(1L);
        dto.setRewardApplyContent("test");
        dto.setRewardApplyStatus(CustomerEnums.RewardApplyStatus.ACCEPT.getCode());
        dto.setAnswerMailYn("N");
        dto.setAnswerMailYn("N");

        //when, then
        customerRewardMallService.addRewardApply(dto);
    }

    @Test
    void getRewardApplyInfo_조회_성공() {
        //given
        RewardApplyRequestDto dto = new RewardApplyRequestDto();
        dto.setStartDate("2021-06-01");
        dto.setEndDate("2021-06-30");
        dto.setUrUserId(1647366L);

        //when
        RewardApplyInfoVo result = customerRewardMallService.getRewardApplyInfo(dto);

        //then
        assertTrue(result.getTotalCount() > 0);
    }

    @Test
    void getRewardApplyList_조회_성공() {
        //given
        RewardApplyRequestDto dto = new RewardApplyRequestDto();
        dto.setStartDate("2021-06-01");
        dto.setEndDate("2021-06-30");
        dto.setUrUserId(1647366L);
        dto.setPage(1);
        dto.setLimit(10);

        //when
        RewardApplyResponseDto result = customerRewardMallService.getRewardApplyList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getRewardApply_조회_성공() {
        //given
        Long csRewardApplyId = 1L;

        //when
        RewardApplyVo result = customerRewardMallService.getRewardApply(csRewardApplyId);

        //then
        assertNotNull(result);
    }

    @Test
    void rewardApplyValidation_validation_실패_관리자확인중() {
        //given
        RewardApplyVo rewardApplyInfo = new RewardApplyVo();
        rewardApplyInfo.setRewardName("test");
        rewardApplyInfo.setRewardApplyStatus(CustomerEnums.RewardApplyStatus.CONFIRM.getCode());

        //when
        MessageCommEnum result = customerRewardMallService.rewardApplyValidation(rewardApplyInfo, CustomerEnums.RewardValidationType.NORMAL);

        //then
        assertEquals(CustomerEnums.RewardApply.APPLY_CONFIRM, result);
    }

    @Test
    void rewardApplyValidation_validation_실패_관리자처리완료() {
        //given
        RewardApplyVo rewardApplyInfo = new RewardApplyVo();
        rewardApplyInfo.setRewardName("test");
        rewardApplyInfo.setRewardApplyStatus(CustomerEnums.RewardApplyStatus.COMPLETE.getCode());

        //when
        MessageCommEnum result = customerRewardMallService.rewardApplyValidation(rewardApplyInfo, CustomerEnums.RewardValidationType.NORMAL);

        //then
        assertEquals(CustomerEnums.RewardApply.APPLY_DONE, result);
    }

    @Test
    void rewardApplyValidation_validation_실패_유저다름() {
        //given
        RewardApplyVo rewardApplyInfo = new RewardApplyVo();
        rewardApplyInfo.setRewardName("test");
        rewardApplyInfo.setRewardApplyStatus(CustomerEnums.RewardApplyStatus.ACCEPT.getCode());
        rewardApplyInfo.setUrUserId(1L);
        rewardApplyInfo.setRequestUrUserId(2L);

        //when
        MessageCommEnum result = customerRewardMallService.rewardApplyValidation(rewardApplyInfo, CustomerEnums.RewardValidationType.NORMAL);

        //then
        assertEquals(CustomerEnums.RewardApply.NOT_USER, result);
    }

    @Test
    void rewardApplyValidation_validation_성공() {
        //given
        RewardApplyVo rewardApplyInfo = new RewardApplyVo();
        rewardApplyInfo.setRewardName("test");
        rewardApplyInfo.setRewardApplyStatus(CustomerEnums.RewardApplyStatus.ACCEPT.getCode());
        rewardApplyInfo.setUrUserId(1L);
        rewardApplyInfo.setRequestUrUserId(1L);

        //when
        MessageCommEnum result = customerRewardMallService.rewardApplyValidation(rewardApplyInfo, CustomerEnums.RewardValidationType.NORMAL);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

    @Test
    void delRewardApply_삭제_성공() {
        //given
        Long csRewardApplyId = 1L;

        //when, then
        customerRewardMallService.delRewardApply(csRewardApplyId);
    }

    @Test
    void putRewardApply_수정_성공() {
        //given
        RewardRequestDto dto = new RewardRequestDto();
        dto.setCsRewardApplyId(1L);
        dto.setUrUserId(1L);
        dto.setRewardApplyContent("test");
        dto.setAnswerSmsYn("Y");
        dto.setAnswerMailYn("N");
        dto.setOdOrderId(1L);
        dto.setDeliveryDate("2021-08-12");
        dto.setGoodsDeliveryType("test");
        dto.setOdShippingPriceId("1");

        //when, then
        customerRewardMallService.putRewardApply(dto);
    }

    @Test
    void getRewardOrderInfo_조회_성공() {
        //given
        Long csRewardId = 1L;
        Long urUserId = 1647363L;

        //when
        RewardOrderResponseDto result = customerRewardMallService.getRewardOrderInfo(csRewardId, urUserId);

        //then
        assertTrue(result.getOrder().size() > 0);
    }

    @Test
    void getRewardGoods_조회_성공() throws Exception {
        //given
        RewardGoodsRequestDto dto = new RewardGoodsRequestDto();
        dto.setCsRewardId(1L);
        dto.setPage(0);
        dto.setLimit(20);

        //when
        RewardGoodsResponseDto result = customerRewardMallService.getRewardGoods(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getRewardInfo_조회_성공() throws Exception {
        //given
        String deviceType = "PC";
        Long csRewardId = 27L;

        //when
        RewardInfoResponseDto result = customerRewardMallService.getRewardInfo(deviceType, csRewardId);

        //then
        assertNotNull(result);
    }

    @Test
    void putRewardUserCheckYn_수정_성공() {
        //given
        Long csRewardApplyId = 1L;

        //when, then
        customerRewardMallService.putRewardUserCheckYn(csRewardApplyId);
    }

    @Test
    void putRewardApplyDelYn_수정_성공() {
        //given
        Long csRewardApplyId = 1L;

        //when, then
        customerRewardMallService.putRewardApplyDelYn(csRewardApplyId);
    }

    @Test
    void getRewardTargetGoods() {
        //given
        Long csRewardId = 38L;

        //when
        List<Long> result = customerRewardMallService.getRewardTargetGoods(csRewardId);

        //then
        assertTrue(result.size() > 0);
    }

}