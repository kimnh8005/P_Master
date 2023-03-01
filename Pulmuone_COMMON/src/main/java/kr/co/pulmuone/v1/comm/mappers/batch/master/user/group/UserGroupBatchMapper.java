package kr.co.pulmuone.v1.comm.mappers.batch.master.user.group;

import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;
import kr.co.pulmuone.v1.batch.user.group.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserGroupBatchMapper {

    int delUserGroupNoDetailData();

    int putUserGroupSetUp();

    UserGroupMasterVo getUserGroupMaster();

    List<UserGroupVo> getUserGroup(@Param("urGroupMasterId") Long urGroupMasterId);

    List<UserGroupBenefitVo> getUserGroupBenefit(@Param("urGroupId") Long urGroupId);

    List<UserGroupBenefitPointVo> getUserGroupBenefitPoint();

    void addGroupBatchHistory(@Param("userBuyerVoList") List<UserBuyerVo> userBuyerVoList);

}
