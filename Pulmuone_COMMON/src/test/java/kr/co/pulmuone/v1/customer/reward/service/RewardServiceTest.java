package kr.co.pulmuone.v1.customer.reward.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestBosDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardTargetGoodsResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosListVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RewardServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private RewardService rewardService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void getRewardList_정상() throws Exception {
        RewardBosRequestDto dto = new RewardBosRequestDto();
        Page<RewardBosListVo> voList = rewardService.getRewardList(dto);

        assertNotNull(voList.getResult());
    }

    @Test
    void addReward_정상() throws Exception {
        RewardBosRequestDto dto = new RewardBosRequestDto();
        RewardBosResponseDto rewardBosResponseDto = new RewardBosResponseDto();

        dto.setRewardNm("고객보상제명 test");
        dto.setRewardContent("고객보상제 설명 test");
        dto.setRewardApplyStandard("REWARD_PAY_TP.ETC");
        dto.setUseYn("N");
        dto.setOrderApprPeriod(1);
        dto.setDetlHtmlPc("고객보상제 html pc test");
        dto.setDetlHtmlMobile("고객보상제 html mobile test");
        dto.setRewardNotice("고객보상제 유의사항 test");
        dto.setRewardGoodsTp("ALL");
        dto.setStartDt("20210606030300");
        dto.setEndDt("20210608235959");
        dto.setTimeOverCloseYn("N");

        rewardBosResponseDto = rewardService.addReward(dto);

        assertTrue(rewardBosResponseDto != null);

    }

    @Test
    void putReward_정상() throws Exception {
        RewardBosRequestDto dto = new RewardBosRequestDto();
        RewardBosResponseDto rewardBosResponseDto = new RewardBosResponseDto();

        dto.setCsRewardId("3");
        dto.setRewardNm("고객보상제명 test");
        dto.setRewardContent("고객보상제 설명 test");
        dto.setRewardApplyStandard("REWARD_PAY_TP.ETC");
        dto.setUseYn("N");
        dto.setOrderApprPeriod(1);
        dto.setDetlHtmlPc("고객보상제 html pc test");
        dto.setDetlHtmlMobile("고객보상제 html mobile test");
        dto.setRewardNotice("고객보상제 유의사항 test");
        dto.setRewardGoodsTp("ALL");
        dto.setStartDt("20210606030300");
        dto.setEndDt("20210608235959");
        dto.setTimeOverCloseYn("N");

        rewardBosResponseDto = rewardService.putReward(dto);

        assertTrue(rewardBosResponseDto != null);

    }

    @Test
    void getRewardInfo_정상() throws Exception {

        RewardBosRequestDto dto = new RewardBosRequestDto();
        RewardBosResponseDto rewardBosResponseDto = new RewardBosResponseDto();
        dto.setCsRewardId("1");
        rewardBosResponseDto = rewardService.getRewardInfo(dto);

        assertTrue( rewardBosResponseDto != null);

    }

    @Test
    void getRewardTargetGoodsInfo_정상() throws Exception {

        String csRewardId = "1";
        RewardTargetGoodsResponseDto rewardTargetGoodsResponseDto = new RewardTargetGoodsResponseDto();

        rewardTargetGoodsResponseDto = rewardService.getRewardTargetGoodsInfo(csRewardId);

        assertTrue(rewardTargetGoodsResponseDto != null);
    }


    @Test
    void getRewardApplyList_정상() throws Exception {
        RewardApplyRequestBosDto dto = new RewardApplyRequestBosDto();

        Page<RewardApplyVo> voList = rewardService.getRewardApplyList(dto);

        assertTrue(voList.size()>0);

    }

    @Test
    void getRewardNmList_정상() throws Exception {
        RewardApplyRequestBosDto dto = new RewardApplyRequestBosDto();

        List<RewardApplyVo> voList = rewardService.getRewardNmList(dto);

        assertTrue(voList.size()>0);

    }

    @Test
    void getRewardApplyDetail_정상() throws Exception {

        RewardApplyRequestBosDto dto = new RewardApplyRequestBosDto();
        dto.setCsRewardApplyId("-1");

        RewardApplyVo vo = rewardService.getRewardApplyDetail(dto);

        Assertions.assertNull(vo);
    }

    @Test
    void putRewardApplyConfirmStatus_정상() throws Exception {

        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        RewardApplyRequestBosDto dto = new RewardApplyRequestBosDto();
        dto.setCsRewardApplyId("1");

        ApiResult<?> result = rewardService.putRewardApplyConfirmStatus(dto);

        // then
        assertTrue(result.getCode().equals(ApiResult.success().getCode()));

    }

    @Test
    void putRewardApplyInfo_정상() throws Exception {
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        RewardApplyRequestBosDto dto = new RewardApplyRequestBosDto();
        dto.setCsRewardApplyId("1");
        dto.setRewardStatus("REWARD_APPLY_STATUS.COMPLETE");
        dto.setRewardApplyResult("REWARD_APPLY_STATUS.COMPLETE");
        dto.setAnswer("TEST");
        dto.setAdminCmt("TESt");
        dto.setRewardPayDetl("TESTE");


        ApiResult<?> result = rewardService.putRewardApplyInfo(dto);

        // then
        assertTrue(result.getCode().equals(ApiResult.success().getCode()));

    }

}
