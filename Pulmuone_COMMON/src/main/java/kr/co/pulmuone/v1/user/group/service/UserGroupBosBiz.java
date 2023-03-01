package kr.co.pulmuone.v1.user.group.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.group.dto.GroupMasterCommonRequestDto;
import kr.co.pulmuone.v1.user.group.dto.UserGroupMasterRequestDto;
import kr.co.pulmuone.v1.user.group.dto.UserGroupRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 회원그룹 설정
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200824    강윤경              최초작성
 * =======================================================================
 * </PRE>
 */

public interface UserGroupBosBiz {

    ApiResult<?> getUserGroupMasterList() throws Exception;

    ApiResult<?> getUserGroupMaster(UserGroupMasterRequestDto dto) throws Exception;

    ApiResult<?> addUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception;

    ApiResult<?> putUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception;

    ApiResult<?> delUserGroupMaster(UserGroupMasterRequestDto dto) throws Exception;

    ApiResult<?> getUserGroupList(UserGroupRequestDto dto) throws Exception;

    ApiResult<?> getUserGroup(UserGroupRequestDto dto) throws Exception;

    ApiResult<?> addUserGroup(UserGroupRequestDto dto) throws Exception;

    ApiResult<?> putUserGroup(UserGroupRequestDto dto) throws Exception;

    ApiResult<?> putUserGroupDefaultYn(UserGroupRequestDto dto) throws Exception;

    ApiResult<?> delUserGroup(UserGroupRequestDto dto) throws Exception;

    ApiResult<?> getUserMasterCategoryList(GroupMasterCommonRequestDto dto) throws Exception;

    ApiResult<?> getUserGroupCategoryList(UserGroupRequestDto userGroupRequestDto) throws Exception;

}

