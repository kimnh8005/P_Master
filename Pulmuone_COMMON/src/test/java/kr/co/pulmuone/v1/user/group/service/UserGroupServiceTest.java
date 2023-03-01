package kr.co.pulmuone.v1.user.group.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromUserGroupVo;
import kr.co.pulmuone.v1.user.group.dto.EstimationGroupInfoResponseDto;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupBenefitVo;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupInfoByUserResultVo;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupListVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserGroupServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    UserGroupService userGroupService;

    @Test
    void getGroupByUser_조회_성공() throws Exception {
        //given
        Long urUserId = 1646893L;

        //when
        GroupInfoByUserResultVo result = userGroupService.getGroupByUser(urUserId);

        //then
        assertNotNull(result.getGroupName());
    }

    @Test
    void getEstimationGroupNameByUser_조회_성공() {
        //given
        OrderInfoFromUserGroupVo orderInfo = new OrderInfoFromUserGroupVo();
        orderInfo.setOrderCount(1);
        orderInfo.setPaidPriceSum(1000);
        List<GroupListVo> groupList = new ArrayList<>();
        GroupListVo vo1 = new GroupListVo();
        vo1.setGroupName("test");
        vo1.setTopImagePath("/test/test.jpg");
        vo1.setListImagePath("/test/test.jpg");
        vo1.setPurchaseAmountFrom(0);
        vo1.setPurchaseCountFrom(0);
        groupList.add(vo1);

        //when
        EstimationGroupInfoResponseDto result = userGroupService.getEstimationGroupNameByUser(orderInfo, groupList);

        //then
        assertNotNull(result.getGroupName());
    }

    @Test
    void getEstimationGroupNameByUser_조회_대상없음() {
        //given
        OrderInfoFromUserGroupVo orderInfo = new OrderInfoFromUserGroupVo();
        orderInfo.setOrderCount(1000);
        orderInfo.setPaidPriceSum(1000000000);
        List<GroupListVo> groupList = new ArrayList<>();
        GroupListVo vo1 = new GroupListVo();
        vo1.setGroupName("test");
        vo1.setTopImagePath("/test/test.jpg");
        vo1.setListImagePath("/test/test.jpg");
        vo1.setPurchaseAmountFrom(10000);
        vo1.setPurchaseCountFrom(10000);
        groupList.add(vo1);

        //when
        EstimationGroupInfoResponseDto result = userGroupService.getEstimationGroupNameByUser(orderInfo, groupList);

        //then
        assertNull(result.getGroupName());
    }

    @Test
    void getGroupList_조회_성공() throws Exception {
        //given, when
        List<GroupListVo> result = userGroupService.getGroupList();

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getGroupBenefit_조회_성공() throws Exception {
        //given
        Long urGroupId = 35L;

        //when
        List<GroupBenefitVo> result = userGroupService.getGroupBenefit(urGroupId);

        //then
        assertTrue(result.size() > 0);
    }

}