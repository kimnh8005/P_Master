package kr.co.pulmuone.v1.user.group.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.user.group.dto.*;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupMasterResultVo;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

@Service
public class UserGroupBosBizImpl implements UserGroupBosBiz {

    @Autowired
    UserGroupBosService userGroupBosService;

    @Override
    public ApiResult<?> getUserGroupMasterList() throws Exception {
        UserGroupMasterListResponseDto responseDto = new UserGroupMasterListResponseDto();
        responseDto.setRows(userGroupBosService.getUserGroupMasterList());
        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> getUserGroupMaster(UserGroupMasterRequestDto dto) throws Exception {
        UserGroupMasterResponseDto responseDto = new UserGroupMasterResponseDto();
        responseDto.setRows(userGroupBosService.getUserGroupMaster(dto));
        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> addUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception {
        // 시작일자 set
        LocalDate localDate = LocalDate.of(dto.getStartDateYear(), dto.getStartDateMonth(), 1);
        dto.setStartDate(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 그룹마스터 기존 저장내역 조회
        List<UserGroupMasterResultVo> userGroupMasterList = userGroupBosService.getUserGroupMasterList();

        // Validation Check
        MessageCommEnum validation = userGroupBosService.validationUserGroupMaster(dto, userGroupMasterList, UserEnums.DatabaseAction.INSERT);
        if (!validation.equals(BaseEnums.Default.SUCCESS)) {
            return ApiResult.result(validation);
        }

        // 기존 저장내역 종료일 처리
        userGroupBosService.putUserGroupMasterEndDate(dto, userGroupMasterList, UserEnums.DatabaseAction.INSERT);

        return ApiResult.success(userGroupBosService.addUserMasterGroup(dto));
    }

    @Override
    public ApiResult<?> putUserMasterGroup(UserGroupMasterRequestDto dto) throws Exception {
        // 시작일자 set
        LocalDate localDate = LocalDate.of(dto.getStartDateYear(), dto.getStartDateMonth(), 1);
        dto.setStartDate(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // 그룹마스터 기존 저장내역 조회
        List<UserGroupMasterResultVo> userGroupMasterList = userGroupBosService.getUserGroupMasterList();

        // Validation Check
        MessageCommEnum validation = userGroupBosService.validationUserGroupMaster(dto, userGroupMasterList, UserEnums.DatabaseAction.UPDATE);
        if (!validation.equals(BaseEnums.Default.SUCCESS)) {
            return ApiResult.result(validation);
        }

        // 기존 저장내역 종료일 처리
        userGroupBosService.putUserGroupMasterEndDate(dto, userGroupMasterList, UserEnums.DatabaseAction.UPDATE);

        return ApiResult.success(userGroupBosService.putUserMasterGroup(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> delUserGroupMaster(UserGroupMasterRequestDto dto) throws Exception {
        // 그룹마스터 기존 저장내역 조회
        List<UserGroupMasterResultVo> userGroupMasterList = userGroupBosService.getUserGroupMasterList();

        // 기존 저장내역 종료일 처리
        userGroupBosService.putUserGroupMasterEndDate(dto, userGroupMasterList, UserEnums.DatabaseAction.DELETE);

        //그룹 혜택 삭제
        userGroupBosService.delUserGroupBenefitByMasterId(dto.getUrGroupMasterId());

        //등급 상세 삭제
        userGroupBosService.delUserGroupListByMasterId(dto.getUrGroupMasterId());

        //마스터 삭제
        userGroupBosService.delUserGroupByMasterId(dto.getUrGroupMasterId());

        return ApiResult.success();
    }

    @Override
    public ApiResult<?> getUserGroupList(UserGroupRequestDto dto) throws Exception {
        UserGroupListResponseDto responseDto = new UserGroupListResponseDto();
        responseDto.setUserGroupPageInfo(userGroupBosService.getUserGroupPageInfo(dto.getUrGroupMasterId()));
        responseDto.setRows(userGroupBosService.getUserGroupList(dto.getUrGroupMasterId()));
        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> getUserGroup(UserGroupRequestDto dto) throws Exception {
        UserGroupResponseDto responseDto = new UserGroupResponseDto();

        UserGroupResultVo resultVo = userGroupBosService.getUserGroup(dto.getUrGroupId());
        //쿠폰 정보
        AddItemBenefitDto benefitCouponDto = new AddItemBenefitDto();
        benefitCouponDto.setUserGroupBenefitType(UserEnums.UserGroupBenefitType.COUPON.getCode());
        benefitCouponDto.setUrGroupId(dto.getUrGroupId());
        resultVo.setAddItemCouponList(userGroupBosService.getUserBenefitList(benefitCouponDto));

        //적립금 정보
        AddItemBenefitDto benefitPointDto = new AddItemBenefitDto();
        benefitPointDto.setUserGroupBenefitType(UserEnums.UserGroupBenefitType.POINT.getCode());
        benefitPointDto.setUrGroupId(dto.getUrGroupId());
        resultVo.setAddItemPointList(userGroupBosService.getUserBenefitList(benefitPointDto));
        responseDto.setRows(resultVo);

        return ApiResult.success(responseDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> addUserGroup(UserGroupRequestDto dto) throws Exception {
        // 기존 저장내역 조회
        List<UserGroupResultVo> userGroupList = userGroupBosService.getUserGroupList(dto.getUrGroupMasterId());

        // Validation Check
        MessageCommEnum validation = userGroupBosService.validationUserGroup(dto, userGroupList, UserEnums.DatabaseAction.INSERT);
        if (!validation.equals(BaseEnums.Default.SUCCESS)) {
            return ApiResult.result(validation);
        }

        setFileList(dto);                           //파일정보 set
        userGroupBosService.addUserGroup(dto);      //등급 상세 등록
        addUserGroupBenefit(dto);                   //그룹 혜택 등록

        return ApiResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> putUserGroup(UserGroupRequestDto dto) throws Exception {
        // 기존 저장내역 조회
        List<UserGroupResultVo> userGroupList = userGroupBosService.getUserGroupList(dto.getUrGroupMasterId());

        // Validation Check
        MessageCommEnum validation = userGroupBosService.validationUserGroup(dto, userGroupList, UserEnums.DatabaseAction.UPDATE);
        if (!validation.equals(BaseEnums.Default.SUCCESS)) {
            return ApiResult.result(validation);
        }

        setFileList(dto);            //파일정보 set
        userGroupBosService.putUserGroup(dto);  //등급 상세 수정
        addUserGroupBenefit(dto);   //그룹 혜택 등록

        return ApiResult.success();
    }

    @Override
    public ApiResult<?> putUserGroupDefaultYn(UserGroupRequestDto dto) throws Exception {
        userGroupBosService.putUserGroupDefaultYnInit(dto);
        userGroupBosService.putUserGroupDefaultYn(dto);
        return ApiResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> delUserGroup(UserGroupRequestDto dto) throws Exception {
        //그룹 혜택 삭제
        userGroupBosService.delUserGroupBenefitByGroupId(dto.getUrGroupId());

        //등급 상세 삭제
        userGroupBosService.delUserGroup(dto.getUrGroupId());

        return ApiResult.success();
    }

    @Override
    public ApiResult<?> getUserMasterCategoryList(GroupMasterCommonRequestDto dto) throws Exception {
        return userGroupBosService.getUserMasterCategoryList(dto);
    }

    @Override
    public ApiResult<?> getUserGroupCategoryList(UserGroupRequestDto dto) throws Exception {
        return userGroupBosService.getUserGroupCategoryList(dto.getUrGroupMasterId());
    }

    /**
     * 파일정보 set
     *
     * @param dto UserGroupRequestDto
     * @throws Exception Exception
     */
    private void setFileList(UserGroupRequestDto dto) throws Exception {
        dto.setAddFileList(BindUtil.convertJsonArrayToDtoList(dto.getAddFile(), FileVo.class));

        for (FileVo fileVo : dto.getAddFileList()) {
            if (fileVo.getFieldName().equals(UserEnums.UserGroupImageType.TOP_IMAGE_FILE.getCode())) { //상단 등급아이콘
                dto.setTopImageOriginalName(fileVo.getOriginalFileName());
                dto.setTopImageName(fileVo.getPhysicalFileName());
                dto.setTopImagePath(fileVo.getServerSubPath());
            } else if (fileVo.getFieldName().equals(UserEnums.UserGroupImageType.LIST_IMAGE_FILE.getCode())) { //상단 등급아이콘
                dto.setListImageOriginalName(fileVo.getOriginalFileName());
                dto.setListImageName(fileVo.getPhysicalFileName());
                dto.setListImagePath(fileVo.getServerSubPath());
            }
        }
    }

    /**
     * 그룹 혜택 등록
     *
     * @param dto
     * @return
     * @throws Exception
     */
    private void addUserGroupBenefit(UserGroupRequestDto dto) throws Exception {

        //쿠폰 삭제
        for (AddItemBenefitDto benefitDto : dto.getDelItemCouponList()) {
            userGroupBosService.delUserGroupBenefit(benefitDto.getUrGroupBenefitId());
        }

        //쿠폰 저장
        for (AddItemBenefitDto benefitDto : dto.getAddItemCouponList()) {
            benefitDto.setUrGroupId(dto.getUrGroupId());
            benefitDto.setUserGroupBenefitType(UserEnums.UserGroupBenefitType.COUPON.getCode());
            if(benefitDto.getUrGroupBenefitId() == 0){
                userGroupBosService.addUserGroupBenefit(benefitDto);
            }
        }

        //적립금 삭제
        for (AddItemBenefitDto benefitDto : dto.getDelItemPointList()) {
            userGroupBosService.delUserGroupBenefit(benefitDto.getUrGroupBenefitId());
        }

		//적립금 저장
		for (AddItemBenefitDto benefitDto : dto.getAddItemPointList()) {
			benefitDto.setUrGroupId(dto.getUrGroupId());
			benefitDto.setUserGroupBenefitType(UserEnums.UserGroupBenefitType.POINT.getCode());
            if(benefitDto.getUrGroupBenefitId() == 0){
                userGroupBosService.addUserGroupBenefit(benefitDto);
            }
		}

    }

}

