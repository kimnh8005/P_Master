package kr.co.pulmuone.bos.user.group;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.group.dto.*;
import kr.co.pulmuone.v1.user.group.service.UserGroupBosBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
@RestController
@RequiredArgsConstructor
public class UserGroupController {

    @Autowired(required = true)
    private HttpServletRequest request;

    private final UserGroupBosBiz userGroupBosBiz;


    @ApiOperation(value = "회원그룹 설정 목록 조회")
    @PostMapping(value = "/admin/ur/userGroup/getUserGroupMasterList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserGroupMasterListResponseDto.class)
    })
    public ApiResult<?> getUserGroupMasterList() throws Exception {
        return userGroupBosBiz.getUserGroupMasterList();
    }

    @ApiOperation(value = "회원그룹 설정 조회")
    @PostMapping(value = "/admin/ur/userGroup/getUserGroupMaster")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserGroupMasterResponseDto.class)
    })
    public ApiResult<?> getUserGroupMaster(UserGroupMasterRequestDto dto) throws Exception {
        return userGroupBosBiz.getUserGroupMaster(dto);
    }

    @ApiOperation(value = "회원그룹 신규 등록")
    @PostMapping(value = "/admin/ur/userGroup/addUserMasterGroup")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data"),
            @ApiResponse(code = 901, message = "" +
                    "USER_GROUP_DUP_NAME - 그룹명 중복 데이터가 존재합니다\n"
                    + "USER_GROUP_DUP_DATE - 시작 적용일 중복 데이터가 존재합니다. \n"
            )
    })
    public ApiResult<?> addUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception {
        return userGroupBosBiz.addUserMasterGroup(dto);
    }

    @ApiOperation(value = "회원그룹  설정  수정")
    @PostMapping(value = "/admin/ur/userGroup/putUserMasterGroup")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data"),
            @ApiResponse(code = 901, message = "" +
                    "USER_GROUP_DUP_NAME - 그룹명 중복 데이터가 존재합니다\n"
                    + "USER_GROUP_DUP_DATE - 시작 적용일 중복 데이터가 존재합니다. \n"
            )
    })
    public ApiResult<?> putUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception {
        return userGroupBosBiz.putUserMasterGroup(dto);
    }

    @ApiOperation(value = "회원그룹  설정  삭제")
    @PostMapping(value = "/admin/ur/userGroup/delUserGroupMaster")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data")
    })
    public ApiResult<?> delUserGroupMaster(UserGroupMasterRequestDto dto) throws Exception {
        return userGroupBosBiz.delUserGroupMaster(dto);
    }


    @ApiOperation(value = "회원그룹  설정 상세 목록 조회")
    @PostMapping(value = "/admin/ur/userGroup/getUserGroupList")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserGroupListResponseDto.class)
    })
    public ApiResult<?> getUserGroupList(UserGroupRequestDto dto) throws Exception {
        return userGroupBosBiz.getUserGroupList(dto);
    }

    @ApiOperation(value = "회원그룹  설정 상세 목록 조회")
    @PostMapping(value = "/admin/ur/userGroup/getUserGroup")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserGroupResponseDto.class)
    })
    public ApiResult<?> getUserGroup(UserGroupRequestDto dto) throws Exception {
        return userGroupBosBiz.getUserGroup(dto);
    }

    @ApiOperation(value = "회원그룹  설정 상세 저장")
    @PostMapping(value = "/admin/ur/userGroup/addUserGroup")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data"),
            @ApiResponse(code = 901, message = "" +
                    "USER_GROUP_DUP_LEVEL - 동일한 레벨이 존재합니다\n"
                    + "USER_GROUP_DUP_GROUP - 동일한 등급명이 존재합니다. \n"
            )
    })
    public ApiResult<?> addUserGroup(@RequestBody UserGroupRequestDto dto) throws Exception {
        return userGroupBosBiz.addUserGroup(dto);
    }

    @ApiOperation(value = "회원그룹  설정 상세 수정")
    @PostMapping(value = "/admin/ur/userGroup/putUserGroup")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data"),
            @ApiResponse(code = 901, message = "" +
                    "USER_GROUP_DUP_LEVEL - 동일한 레벨이 존재합니다\n"
                    + "USER_GROUP_DUP_GROUP - 동일한 등급명이 존재합니다. \n"
            )
    })
    public ApiResult<?> putUserGroup(@RequestBody UserGroupRequestDto dto) throws Exception {
        return userGroupBosBiz.putUserGroup(dto);
    }

    @ApiOperation(value = "회원그룹 설정 상세 수정 - 기본값 설정")
    @PostMapping(value = "/admin/ur/userGroup/putUserGroupDefaultYn")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data")
    })
    public ApiResult<?> putUserGroupDefaultYn(UserGroupRequestDto dto) throws Exception {
        return userGroupBosBiz.putUserGroupDefaultYn(dto);
    }

    @ApiOperation(value = "회원그룹  설정 상세 삭제")
    @PostMapping(value = "/admin/ur/userGroup/delUserGroup")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data")
    })
    public ApiResult<?> delUserGroup(UserGroupRequestDto dto) throws Exception {
        return userGroupBosBiz.delUserGroup(dto);
    }

    @GetMapping(value = "/admin/comn/getUserMasterCategoryList")
    @ApiOperation(value = "회원마스터그룹 카테고리 조회 DropDown")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserGroupMasterListResponseDto.class)
    })
    public ApiResult<?> getUserMasterCategoryList(GroupMasterCommonRequestDto dto) throws Exception {
        return userGroupBosBiz.getUserMasterCategoryList(dto);
    }

    @GetMapping(value = "/admin/comn/getUserGroupCategoryList")
    @ApiOperation(value = "회원그룹 카테고리 조회 DropDown")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = UserGroupResponseDto.class)
    })
    public ApiResult<?> getUserGroupCategoryList(UserGroupRequestDto dto) throws Exception {
        return userGroupBosBiz.getUserGroupCategoryList(dto);
    }
}

