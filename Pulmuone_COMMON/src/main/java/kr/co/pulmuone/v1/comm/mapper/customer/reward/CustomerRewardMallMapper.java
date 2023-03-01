package kr.co.pulmuone.v1.comm.mapper.customer.reward;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerRewardMallMapper {

    List<RewardPageInfoVo> getRewardPageInfo(@Param("deviceType") String deviceType, @Param("csRewardId") Long csRewardId);

    RewardVo getRewardInfo(Long csRewardId);

    List<Long> getRewardTargetGoods(Long csRewardId);

    int addRewardApply(RewardRequestDto dto);

    int addRewardApplyAttc(@Param("csRewardApplyId") Long csRewardApplyId, @Param("list") List<RewardAttcVo> list);

    RewardApplyInfoVo getRewardApplyInfo(RewardApplyRequestDto dto);

    Page<RewardApplyListVo> getRewardApplyList(RewardApplyRequestDto dto);

    List<RewardAttcVo> getRewardApplyAttc(Long csRewardApplyId);

    RewardApplyVo getRewardApply(Long csRewardApplyId);

    int delRewardApplyAttc(Long csRewardApplyId);

    int delRewardApply(Long csRewardApplyId);

    int putRewardApplyDelYn(Long csRewardApplyId);

    int putRewardApply(RewardRequestDto dto);

    Integer getRewardValidation(RewardRequestDto dto);

    int putRewardUserCheckYn(Long csRewardApplyId);

}