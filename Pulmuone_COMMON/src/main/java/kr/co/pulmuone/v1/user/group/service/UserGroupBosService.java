package kr.co.pulmuone.v1.user.group.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mapper.user.group.UserGroupMapper;
import kr.co.pulmuone.v1.user.group.dto.*;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupMasterResultVo;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupPageInfoVo;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupResultVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
 * 1.1    20210208    이원호              수정사항 반영 - 회원그룹 기준 변경
 * =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class UserGroupBosService {

    @Autowired
    UserGroupMapper userGroupMapper;

    /**
     * 회원그룹 설정 목록 조회
     *
     * @return List<UserGroupMasterResultVo>
     * @throws Exception Exception
     */
    protected List<UserGroupMasterResultVo> getUserGroupMasterList() throws Exception {
        return userGroupMapper.getUserGroupMasterList();
    }

    /**
     * 회원그룹 설정 상세조회
     *
     * @param dto UserGroupMasterRequestDto
     * @return UserGroupMasterResultVo
     * @throws Exception Exception
     */
    protected UserGroupMasterResultVo getUserGroupMaster(UserGroupMasterRequestDto dto) throws Exception {
        return userGroupMapper.getUserGroupMaster(dto);
    }

    /**
     * 회원그룹 중복 체크
     *
     * @param dto                 UserGroupMasterRequestDto
     * @param userGroupMasterList List<UserGroupMasterResultVo>
     * @param databaseAction      UserEnums.DatabaseAction
     * @return MessageCommEnum
     */
    protected MessageCommEnum validationUserGroupMaster(UserGroupMasterRequestDto dto, List<UserGroupMasterResultVo> userGroupMasterList, UserEnums.DatabaseAction databaseAction) {
        for (UserGroupMasterResultVo vo : userGroupMasterList) {
            // 수정의 경우
            if (databaseAction.equals(UserEnums.DatabaseAction.UPDATE)) {
                if (dto.getUrGroupMasterId().equals(vo.getUrGroupMasterId())) {
                    continue;
                }
            }

            // 그룹명 중복 시
            if (dto.getGroupMasterName().equals(vo.getGroupMasterName())) {
                return UserEnums.UserGroup.USER_GROUP_DUP_NAME;
            }

            // 적용기간 중복 시
            if (dto.getStartDate().equals(vo.getStartDate())) {
                return UserEnums.UserGroup.USER_GROUP_DUP_DATE;
            }
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * 회원그룹 신규 등록
     *
     * @param dto UserGroupMasterRequestDto
     * @return int
     * @throws Exception Exception
     */
    protected int addUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception {
        return userGroupMapper.addUserMasterGroup(dto);
    }

    /**
     * 회원그룹 수정
     *
     * @param dto UserGroupMasterRequestDto
     * @return int
     * @throws Exception Exception
     */
    protected int putUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception {
        return userGroupMapper.putUserMasterGroup(dto);
    }

    /**
     * 회원그룹 중복 체크
     *
     * @param dto                 UserGroupMasterRequestDto
     * @param userGroupMasterList List<UserGroupMasterResultVo>
     * @param databaseAction      UserEnums.DatabaseAction
     * @return MessageCommEnum
     * @throws Exception Exception
     */
    protected void putUserGroupMasterEndDate(UserGroupMasterRequestDto dto, List<UserGroupMasterResultVo> userGroupMasterList, UserEnums.DatabaseAction databaseAction) throws Exception {
        if (userGroupMasterList == null || userGroupMasterList.isEmpty()) return;   // 방어코드

        // 정렬
        List<UserGroupMasterResultVo> sortedGroupMasterList = userGroupMasterList.stream()
                .filter(vo -> !vo.getUrGroupMasterId().equals(dto.getUrGroupMasterId()))
                .sorted(Comparator.comparing(UserGroupMasterResultVo::getStartDate).reversed())
                .collect(Collectors.toList());

        String endDate = null;  // 삭제의 경우 Null

        // 등록, 수정의 경우 종료일자 설정
        if (databaseAction.equals(UserEnums.DatabaseAction.INSERT) || databaseAction.equals(UserEnums.DatabaseAction.UPDATE)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dto.getStartDate(), dateTimeFormatter);
            endDate = localDate.minusDays(1).format(dateTimeFormatter) + " 23:59:59";
        }

        UserGroupMasterRequestDto requestDto = new UserGroupMasterRequestDto();
        requestDto.setUrGroupMasterId(sortedGroupMasterList.get(0).getUrGroupMasterId());
        requestDto.setEndDate(endDate);
        userGroupMapper.putUserMasterGroupEndDate(requestDto);
    }


    /**
     * 회원그룹 혜택 삭제
     *
     * @param urGroupMasterId Long
     * @return int
     * @throws Exception Exception
     */
    protected int delUserGroupBenefitByMasterId(Long urGroupMasterId) throws Exception {
        return userGroupMapper.delUserGroupBenefitByMasterId(urGroupMasterId);
    }


    /**
     * 회원그룹 등급 상세 삭제
     *
     * @param urGroupMasterId Long
     * @return int
     * @throws Exception Exception
     */
    protected int delUserGroupListByMasterId(Long urGroupMasterId) throws Exception {
        return userGroupMapper.delUserGroupListByMasterId(urGroupMasterId);
    }

    /**
     * 회원그룹 마스터 삭제
     *
     * @param urGroupMasterId Long
     * @return int
     * @throws Exception Exception
     */
    protected int delUserGroupByMasterId(Long urGroupMasterId) throws Exception {
        return userGroupMapper.delUserGroupByMasterId(urGroupMasterId);
    }

    /**
     * 회원그룹 설정 페이지 정보
     *
     * @param urGroupMasterId Long
     * @return UserGroupPageInfoVo
     * @throws Exception Exception
     */
    protected UserGroupPageInfoVo getUserGroupPageInfo(Long urGroupMasterId) throws Exception {
        return userGroupMapper.getUserGroupPageInfo(urGroupMasterId);
    }

    /**
     * 회원그룹 설정 상세 목록조회
     *
     * @param urGroupMasterId Long
     * @return List<UserGroupResultVo>
     * @throws Exception Exception
     */
    protected List<UserGroupResultVo> getUserGroupList(Long urGroupMasterId) throws Exception {
        List<UserGroupResultVo> result = userGroupMapper.getUserGroupList(urGroupMasterId);

        Long defaultAmountFrom = 999999999L;
        int defaultCountFrom = 999999999;
        for (UserGroupResultVo vo : result) {
            vo.setPurchaseAmountTo(defaultAmountFrom);
            vo.setPurchaseCountTo(defaultCountFrom);
            defaultAmountFrom = vo.getPurchaseAmountFrom();
            defaultCountFrom = vo.getPurchaseCountFrom();
        }

        return result;
    }

    /**
     * 그룹 설정 정보 조회
     *
     * @param urGroupId Long
     * @return UserGroupResultVo
     * @throws Exception Exception
     */
    protected UserGroupResultVo getUserGroup(Long urGroupId) throws Exception {
        return userGroupMapper.getUserGroup(urGroupId);
    }

    /**
     * 회원등급 validation
     *
     * @param dto            UserGroupRequestDto
     * @param userGroupList  List<UserGroupResultVo>
     * @param databaseAction UserEnums.DatabaseAction
     * @return MessageCommEnum
     * @throws Exception Exception
     */
    protected MessageCommEnum validationUserGroup(UserGroupRequestDto dto, List<UserGroupResultVo> userGroupList, UserEnums.DatabaseAction databaseAction) {
        for (UserGroupResultVo vo : userGroupList) {
            // 수정의 경우
            if (databaseAction.equals(UserEnums.DatabaseAction.UPDATE)) {
                if (dto.getUrGroupId().equals(vo.getUrGroupId())) {
                    continue;
                }
            }

            // 그룹명 중복 시
            if (dto.getGroupName().equals(vo.getGroupName())) {
                return UserEnums.UserGroup.USER_GROUP_DUP_GROUP;
            }

            // 등급레벨 중복 시
            if (dto.getGroupLevelType().equals(vo.getGroupLevelType())) {
                return UserEnums.UserGroup.USER_GROUP_DUP_LEVEL;
            }
        }

        return BaseEnums.Default.SUCCESS;
    }


    /**
     * 회원그룹 등급 상세 등록
     *
     * @param dto UserGroupRequestDto
     * @return int
     * @throws Exception Exception
     */
    protected int addUserGroup(UserGroupRequestDto dto) throws Exception {
        return userGroupMapper.addUserGroup(dto);
    }

    /**
     * 회원그룹 등급 상세 수정
     *
     * @param dto UserGroupRequestDto
     * @return int
     * @throws Exception Exception
     */
    protected int putUserGroup(UserGroupRequestDto dto) throws Exception {
        return userGroupMapper.putUserGroup(dto);
    }

    /**
     * 회원그룹 등급 - 기본값 초기화
     *
     * @param dto UserGroupRequestDto
     * @throws Exception Exception
     */
    protected void putUserGroupDefaultYnInit(UserGroupRequestDto dto) throws Exception {
        userGroupMapper.putUserGroupDefaultYnInit(dto);
    }

    /**
     * 회원그룹 등급 - 기본값 수정
     *
     * @param dto UserGroupRequestDto
     * @throws Exception Exception
     */
    protected void putUserGroupDefaultYn(UserGroupRequestDto dto) throws Exception {
        userGroupMapper.putUserGroupDefaultYn(dto);
    }

    /**
     * 그룹 혜택 삭제
     *
     * @param urGroupId Long
     * @return int
     * @throws Exception Exception
     */
    protected int delUserGroupBenefitByGroupId(Long urGroupId) throws Exception {
        return userGroupMapper.delUserGroupBenefitByGroupId(urGroupId);
    }

    /**
     * 등급 상세 삭제
     *
     * @param urGroupId Long
     * @return int
     * @throws Exception Exception
     */
    protected int delUserGroup(Long urGroupId) throws Exception {
        return userGroupMapper.delUserGroup(urGroupId);
    }

    /**
     * 등급 혜택 정보 조회
     *
     * @param dto AddItemBenefitDto
     * @return List<AddItemBenefitDto>
     * @throws Exception Exception
     */
    protected List<AddItemBenefitDto> getUserBenefitList(AddItemBenefitDto dto) throws Exception {
        return userGroupMapper.getUserBenefitList(dto);
    }

    /**
     * 회원그룹 헤택 저장
     *
     * @param dto AddItemBenefitDto
     * @return int
     * @throws Exception Exception
     */
    protected int addUserGroupBenefit(AddItemBenefitDto dto) throws Exception {
        return userGroupMapper.addUserGroupBenefit(dto);
    }

    /**
     * 회원그룹 설정 상세 혜택 개별 삭제
     *
     * @param urGroupBenefitId Long
     * @return int
     * @throws Exception Exception
     */
    protected int delUserGroupBenefit(Long urGroupBenefitId) throws Exception {
        return userGroupMapper.delUserGroupBenefit(urGroupBenefitId);
    }

    /**
     * 회원마스터그룹 카테고리 조회 DropDown
     *
     * @return ApiResult<?>
     * @throws Exception Exception
     */
    protected ApiResult<?> getUserMasterCategoryList(GroupMasterCommonRequestDto dto) throws Exception {
        UserGroupMasterListResponseDto result = new UserGroupMasterListResponseDto();
        result.setRows(userGroupMapper.getUserMasterCategoryList(dto));
        return ApiResult.success(result);
    }

    /**
     * 회원그룹 카테고리 조회 DropDown
     *
     * @param dto UserGroupRequestDto
     * @return ApiResult<?>
     * @throws Exception Exception
     */
    protected ApiResult<?> getUserGroupCategoryList(Long urGroupMasterId) throws Exception {
        UserGroupListResponseDto result = new UserGroupListResponseDto();
        result.setRows(userGroupMapper.getUserGroupCategoryList(urGroupMasterId));
        return ApiResult.success(result);
    }

}