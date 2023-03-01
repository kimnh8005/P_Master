package kr.co.pulmuone.v1.user.group.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.user.group.dto.AddItemBenefitDto;
import kr.co.pulmuone.v1.user.group.dto.GroupMasterCommonRequestDto;
import kr.co.pulmuone.v1.user.group.dto.UserGroupMasterRequestDto;
import kr.co.pulmuone.v1.user.group.dto.UserGroupRequestDto;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupMasterResultVo;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupPageInfoVo;
import kr.co.pulmuone.v1.user.group.dto.vo.UserGroupResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Slf4j
class UserGroupBosServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserGroupBosService userGroupBosService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void 회원그룹설정_목록조회_성공() throws Exception {
        // given, when
        List<UserGroupMasterResultVo> rows = userGroupBosService.getUserGroupMasterList();

        // then
        assertTrue(CollectionUtils.isNotEmpty(rows));
    }

    @Test
    void 회원그룹_설정_상세조회_성공() throws Exception {
        //given
        UserGroupMasterRequestDto userGroupRequestDto = new UserGroupMasterRequestDto();
        userGroupRequestDto.setUrGroupMasterId(1L);

        //when
        UserGroupMasterResultVo resultVo = userGroupBosService.getUserGroupMaster(userGroupRequestDto);

        //then
        assertEquals(1, resultVo.getUrGroupMasterId());
    }

    @Test
    void 회원그룹_중복체크_성공() {
        //given
        UserGroupMasterRequestDto dto = new UserGroupMasterRequestDto();
        dto.setGroupMasterName("동해물과백두산이마르고닳도록");
        dto.setStartDate("2020-08-01");

        List<UserGroupMasterResultVo> userGroupMasterList = new ArrayList<>();
        UserGroupMasterResultVo vo1 = new UserGroupMasterResultVo();
        userGroupMasterList.add(vo1);

        UserEnums.DatabaseAction databaseAction = UserEnums.DatabaseAction.INSERT;

        //when
        MessageCommEnum result = userGroupBosService.validationUserGroupMaster(dto, userGroupMasterList, databaseAction);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

    @Test
    void 회원그룹_중복체크_실패_중복이름() {
        //given
        UserGroupMasterRequestDto dto = new UserGroupMasterRequestDto();
        dto.setGroupMasterName("test");
        dto.setStartDate("2020-08-01");

        List<UserGroupMasterResultVo> userGroupMasterList = new ArrayList<>();
        UserGroupMasterResultVo vo1 = new UserGroupMasterResultVo();
        vo1.setGroupMasterName("test");
        userGroupMasterList.add(vo1);

        UserEnums.DatabaseAction databaseAction = UserEnums.DatabaseAction.INSERT;

        //when
        MessageCommEnum result = userGroupBosService.validationUserGroupMaster(dto, userGroupMasterList, databaseAction);

        //then
        assertEquals(UserEnums.UserGroup.USER_GROUP_DUP_NAME, result);
    }

    @Test
    void 회원그룹_중복체크_실패_중복기간() {
        //given
        UserGroupMasterRequestDto dto = new UserGroupMasterRequestDto();
        dto.setGroupMasterName("test");
        dto.setStartDate("2020-08-01");

        List<UserGroupMasterResultVo> userGroupMasterList = new ArrayList<>();
        UserGroupMasterResultVo vo1 = new UserGroupMasterResultVo();
        vo1.setGroupMasterName("test1");
        vo1.setStartDate("2020-08-01");
        userGroupMasterList.add(vo1);

        UserEnums.DatabaseAction databaseAction = UserEnums.DatabaseAction.INSERT;

        //when
        MessageCommEnum result = userGroupBosService.validationUserGroupMaster(dto, userGroupMasterList, databaseAction);

        //then
        assertEquals(UserEnums.UserGroup.USER_GROUP_DUP_DATE, result);
    }

    @Test
    void 회원그룹_신규등록_성공() throws Exception {
        //given
        UserGroupMasterRequestDto userGroupRequestDto = new UserGroupMasterRequestDto();
        userGroupRequestDto.setGroupMasterName("신규신규신규");
        userGroupRequestDto.setCalculatePeriod("1");
        userGroupRequestDto.setStartDate("2029-08-01");

        //when
        int result = userGroupBosService.addUserMasterGroup(userGroupRequestDto);

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹_수정_성공() throws Exception {
        //given
        UserGroupMasterRequestDto dto = new UserGroupMasterRequestDto();
        dto.setGroupMasterName("신규신규신규");
        dto.setCalculatePeriod("1");
        dto.setStartDate("2029-08-01");
        dto.setUrGroupMasterId(1L);

        //when
        int result = userGroupBosService.putUserMasterGroup(dto);

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹_수정_종료일자_성공() throws Exception {
        //given
        UserGroupMasterRequestDto userGroupRequestDto = new UserGroupMasterRequestDto();
        userGroupRequestDto.setUrGroupMasterId(1L);
        userGroupRequestDto.setGroupMasterName("신규신규신규");
        userGroupRequestDto.setStartDate("2029-08-01");

        List<UserGroupMasterResultVo> userGroupMasterList = new ArrayList<>();
        UserGroupMasterResultVo vo1 = new UserGroupMasterResultVo();
        vo1.setUrGroupMasterId(2L);
        vo1.setStartDate("2020-01-01");
        userGroupMasterList.add(vo1);
        UserEnums.DatabaseAction databaseAction = UserEnums.DatabaseAction.INSERT;

        //when, then
        userGroupBosService.putUserGroupMasterEndDate(userGroupRequestDto, userGroupMasterList, databaseAction);
    }

    @Test
    void 회원그룹_혜택삭제_마스터ID로_성공() throws Exception {
        //given
        Long urGroupMasterId = 1L;

        //when
        int result = userGroupBosService.delUserGroupBenefitByMasterId(urGroupMasterId);

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹_등급상세_삭제_성공() throws Exception {
        //given
        Long urGroupMasterId = 1L;

        //when
        //fk 오류로 인해 하위 정보 선 삭제
        //그룹 혜택 삭제
        userGroupBosService.delUserGroupBenefitByMasterId(urGroupMasterId);
        //fk 오류로 인해 하위 정보 선 삭제
        int result = userGroupBosService.delUserGroupListByMasterId(urGroupMasterId);

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹_마스터_삭제_성공() throws Exception {
        //given
        Long urGroupMasterId = 1L;

        //when
        //fk 오류 로 하위 정보 일괄 삭제
        //그룹 혜택 삭제
        userGroupBosService.delUserGroupBenefitByMasterId(urGroupMasterId);

        //등급 상세 삭제
        userGroupBosService.delUserGroupListByMasterId(urGroupMasterId);
        //fk 오류 로 하위 정보 일괄 삭제

        int result = userGroupBosService.delUserGroupByMasterId(urGroupMasterId);

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹설정_상세_목록조회_성공() throws Exception {
        //given
        Long urGroupMasterId = 1L;

        //when
        List<UserGroupResultVo> rows = userGroupBosService.getUserGroupList(urGroupMasterId);

        //then
        assertTrue(rows.size() > 0);
    }

    @Test
    void 회원그룹_상세정보조회_성공() throws Exception {
        //given
        Long urGroupId = 8L;

        //when
        UserGroupResultVo resultVo = userGroupBosService.getUserGroup(urGroupId);

        //then
        assertEquals(8L, resultVo.getUrGroupId());
    }

    @Test
    void 회원등급_중복체크_성공() {
        //given
        UserGroupRequestDto userGroupRequestDto = new UserGroupRequestDto();
        userGroupRequestDto.setGroupName("테스트");
        userGroupRequestDto.setGroupLevelType(UserEnums.GroupLevelType.LEVEL02.getCode());

        List<UserGroupResultVo> userGroupList = new ArrayList<>();
        UserGroupResultVo vo1 = new UserGroupResultVo();
        vo1.setUrGroupId(8L);
        vo1.setGroupName("test");
        vo1.setGroupLevelType(UserEnums.GroupLevelType.LEVEL01.getCode());
        userGroupList.add(vo1);

        UserEnums.DatabaseAction databaseAction = UserEnums.DatabaseAction.INSERT;

        //when
        MessageCommEnum result = userGroupBosService.validationUserGroup(userGroupRequestDto, userGroupList, databaseAction);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

    @Test
    void 회원등급_중복체크_실패_그룹명() {
        //given
        UserGroupRequestDto userGroupRequestDto = new UserGroupRequestDto();
        userGroupRequestDto.setGroupName("테스트");
        userGroupRequestDto.setGroupLevelType(UserEnums.GroupLevelType.LEVEL02.getCode());

        List<UserGroupResultVo> userGroupList = new ArrayList<>();
        UserGroupResultVo vo1 = new UserGroupResultVo();
        vo1.setUrGroupId(8L);
        vo1.setGroupName("테스트");
        vo1.setGroupLevelType(UserEnums.GroupLevelType.LEVEL01.getCode());
        userGroupList.add(vo1);

        UserEnums.DatabaseAction databaseAction = UserEnums.DatabaseAction.INSERT;

        //when
        MessageCommEnum result = userGroupBosService.validationUserGroup(userGroupRequestDto, userGroupList, databaseAction);

        //then
        assertEquals(UserEnums.UserGroup.USER_GROUP_DUP_GROUP, result);
    }

    @Test
    void 회원등급_중복체크_실패_등급레벨() {
        //given
        UserGroupRequestDto userGroupRequestDto = new UserGroupRequestDto();
        userGroupRequestDto.setGroupName("test");
        userGroupRequestDto.setGroupLevelType(UserEnums.GroupLevelType.LEVEL01.getCode());

        List<UserGroupResultVo> userGroupList = new ArrayList<>();
        UserGroupResultVo vo1 = new UserGroupResultVo();
        vo1.setUrGroupId(8L);
        vo1.setGroupName("테스트");
        vo1.setGroupLevelType(UserEnums.GroupLevelType.LEVEL01.getCode());
        userGroupList.add(vo1);

        UserEnums.DatabaseAction databaseAction = UserEnums.DatabaseAction.INSERT;

        //when
        MessageCommEnum result = userGroupBosService.validationUserGroup(userGroupRequestDto, userGroupList, databaseAction);

        //then
        assertEquals(UserEnums.UserGroup.USER_GROUP_DUP_LEVEL, result);
    }

    @Test
    void 회원그룹_등급상세_등록_성공() throws Exception {
        //given
        UserGroupRequestDto dto = new UserGroupRequestDto();
        dto.setUrGroupMasterId(1L);
        dto.setGroupLevelType(UserEnums.GroupLevelType.LEVEL01.getCode());
        dto.setGroupName("테스트");
        dto.setPurchaseAmountFrom("1");
        dto.setPurchaseCountFrom("2");
        dto.setTopImageOriginalName("상단테스트이미지");
        dto.setTopImageName("상단테스트저장이미지이름");
        dto.setTopImagePath("/test/abc/");
        dto.setListImageOriginalName("리스트테스트이미지");
        dto.setListImageName("리스트테스트저장이미지이름");
        dto.setListImagePath("/test/abc/");

        //when
        int result = userGroupBosService.addUserGroup(dto);    //등급 상세 등록

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹_등급상세_수정_성공() throws Exception {
        //given
        UserGroupRequestDto dto = new UserGroupRequestDto();
        dto.setUrGroupId(8L);
        dto.setGroupLevelType(UserEnums.GroupLevelType.LEVEL01.getCode());
        dto.setGroupName("test");
        dto.setPurchaseAmountFrom("9");
        dto.setPurchaseCountFrom("8");

        //when
        int result = userGroupBosService.putUserGroup(dto);

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹_혜택_삭제_성공() throws Exception {
        //given
        Long urGroupId = 8L;

        //when
        int result = userGroupBosService.delUserGroupBenefitByGroupId(urGroupId);

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹_등급상세_개별삭제_성공() throws Exception {
        //given
        Long urGroupId = 8L;

        //when
        int result = userGroupBosService.delUserGroup(urGroupId);

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹설정_등급혜택_정보조회_성공() throws Exception {
        //given
        AddItemBenefitDto addItemBenefitDto = new AddItemBenefitDto();
        addItemBenefitDto.setUrGroupId(8L);
        addItemBenefitDto.setUserGroupBenefitType(UserEnums.UserGroupBenefitType.COUPON.getCode());

        //when
        List<AddItemBenefitDto> rows = userGroupBosService.getUserBenefitList(addItemBenefitDto);

        //then
        assertTrue(CollectionUtils.isNotEmpty(rows));
    }

    @Test
    void 회원그룹_혜택_등록_성공() throws Exception {
        //given
        AddItemBenefitDto addItemBenefitDto = new AddItemBenefitDto();
        addItemBenefitDto.setUrGroupId(25L);
        addItemBenefitDto.setUserGroupBenefitType(UserEnums.UserGroupBenefitType.COUPON.getCode());
        addItemBenefitDto.setBenefitRelationId("68");

        //when
        int result = userGroupBosService.addUserGroupBenefit(addItemBenefitDto);

        //then
        assertTrue(result > 0);
    }

    @Test
    void 회원그룹_혜택삭제_개별삭제_성공() throws Exception {
        //given
        Long urGroupBenefitId = 1L;

        //when
        int result = userGroupBosService.delUserGroupBenefit(urGroupBenefitId);

        //then
        assertTrue(result > 0);
    }


    @Test
    void getUserMasterCategoryList_조회_성공() throws Exception {
        //given
        GroupMasterCommonRequestDto dto = new GroupMasterCommonRequestDto();

        // when
        ApiResult<?> result = userGroupBosService.getUserMasterCategoryList(dto);

        //then
        assertEquals(result.getCode(), BaseEnums.Default.SUCCESS.getCode());
    }

    @Test
    void getUserGroupCategoryList_조회_성공() throws Exception {
        //given
        Long urGroupMasterId = 1L;

        //when
        ApiResult<?> result = userGroupBosService.getUserGroupCategoryList(urGroupMasterId);

        //then
        assertEquals(result.getCode(), BaseEnums.Default.SUCCESS.getCode());
    }

    @Test
    void getUserGroupPageInfo_조회_성공() throws Exception {
        //given
        Long urGroupMasterId = 1L;

        //when
        UserGroupPageInfoVo result = userGroupBosService.getUserGroupPageInfo(urGroupMasterId);

        //then
        assertTrue(result.getGroupMasterName().length() > 0);
    }
    
}