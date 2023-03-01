package kr.co.pulmuone.v1.comm.mapper.customer.reward;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestBosDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosDetailVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosListVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardTargetGoodsBosVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RewardMapper {

    Page<RewardBosListVo> getRewardList(RewardBosRequestDto rewardBosRequestDto) throws Exception;

    int addReward(RewardBosRequestDto rewardBosRequestDto) throws Exception;

    int addRewardTargetGoods(@Param("rewardTargetGoodsList") List<RewardTargetGoodsBosVo> rewardTargetGoodsList, @Param("csRewardId") String csRewardId) throws Exception;

    int putReward(RewardBosRequestDto rewardBosRequestDto) throws Exception;

    int delRewardTargetGoodsByCsRewardId(String csRewardId) throws Exception;

    RewardBosDetailVo getRewardInfo(RewardBosRequestDto rewardBosRequestDto) throws Exception;

    List<RewardTargetGoodsBosVo> getRewardTargetGoodsInfo(String csRewardId) throws Exception;

    Page<RewardApplyVo> getRewardApplyList(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception;

    List<RewardApplyVo> getRewardNmList(RewardApplyRequestBosDto rewardApplyRequestBosDto);

    RewardApplyVo getRewardApplyDetail(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception;

    List<RewardApplyVo> getImageList(String csRewardApplyId);

    int putRewardApplyConfirmStatus(RewardApplyRequestBosDto rewardApplyRequestBosDto);

    int putRewardApplyInfo(RewardApplyRequestBosDto rewardApplyRequestBosDto);

    List<RewardApplyVo> getRewardApplyListExportExcel(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception;
}
