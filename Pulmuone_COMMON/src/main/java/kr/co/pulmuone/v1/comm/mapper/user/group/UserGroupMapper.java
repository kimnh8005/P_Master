package kr.co.pulmuone.v1.comm.mapper.user.group;

import kr.co.pulmuone.v1.user.group.dto.AddItemBenefitDto;
import kr.co.pulmuone.v1.user.group.dto.GroupMasterCommonRequestDto;
import kr.co.pulmuone.v1.user.group.dto.UserGroupMasterRequestDto;
import kr.co.pulmuone.v1.user.group.dto.UserGroupRequestDto;
import kr.co.pulmuone.v1.user.group.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserGroupMapper {

    //마스터 목록
    List<UserGroupMasterResultVo> getUserGroupMasterList() throws Exception;

    //마스터 조회
    UserGroupMasterResultVo getUserGroupMaster(UserGroupMasterRequestDto dto) throws Exception;

    //마스터 등록
    int addUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception;

    //마스터 수정
    int putUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception;

    void putUserMasterGroupEndDate(UserGroupMasterRequestDto dto) throws Exception;

    //그룹 혜택 삭제
    int delUserGroupBenefitByMasterId(Long urGroupMasterId) throws Exception;

    //그룹 상세 전체 삭제
    int delUserGroupListByMasterId(Long urGroupMasterId) throws Exception;

    //그룹 마스터 삭제
    int delUserGroupByMasterId(Long urGroupMasterId) throws Exception;

    UserGroupPageInfoVo getUserGroupPageInfo(Long urGroupMasterId) throws Exception;

    //그룹 상세 목록
    List<UserGroupResultVo> getUserGroupList(Long urGroupMasterId) throws Exception;

    //그룹 상세 조회
    UserGroupResultVo getUserGroup(Long urGroupId) throws Exception;

    void putUserGroupDefaultYnInit(UserGroupRequestDto dto) throws Exception;

    void putUserGroupDefaultYn(UserGroupRequestDto dto) throws Exception;

    //그룹 상세 등록
    int addUserGroup(UserGroupRequestDto dto) throws Exception;

    //그룹 상세 수정
    int putUserGroup(UserGroupRequestDto dto) throws Exception;

    //그룹 혜택 삭제
    int delUserGroupBenefitByGroupId(Long urGroupId) throws Exception;

    //그룹 상세 삭제
    int delUserGroup(Long urGroupId) throws Exception;

    List<AddItemBenefitDto> getUserBenefitList(AddItemBenefitDto dto) throws Exception;

    int addUserGroupBenefit(AddItemBenefitDto dto) throws Exception;


    int delUserGroupBenefit(Long urGroupBenefitId) throws Exception;


    GroupInfoByUserResultVo getGroupByUser(Long urUserId) throws Exception;

    List<GroupListVo> getGroupList() throws Exception;

    List<GroupBenefitVo> getGroupBenefit(Long urGroupId) throws Exception;

    Long getDefaultGroup() throws Exception;

    //회원마스터그룹
    List<UserGroupMasterResultVo> getUserMasterCategoryList(GroupMasterCommonRequestDto dto) throws Exception;

    //회원그룹
    List<UserGroupResultVo> getUserGroupCategoryList(Long urGroupMasterId) throws Exception;

}
