package kr.co.pulmuone.v1.user.group.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromUserGroupVo;
import kr.co.pulmuone.v1.user.group.dto.EstimationGroupInfoResponseDto;
import kr.co.pulmuone.v1.user.group.dto.GroupInfoByUserResponseDto;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupInfoByUserResultVo;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserGroupBizImpl implements UserGroupBiz {
    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    /**
     * 등급혜택 메인 조회 getGroupInfoByUser
     */
    public GroupInfoByUserResponseDto getGroupInfoByUser(Long urUserId) throws Exception {
        GroupInfoByUserResponseDto result = new GroupInfoByUserResponseDto();

        // 이달의 등급 정보 설정
        GroupInfoByUserResultVo groupVo = userGroupService.getGroupByUser(urUserId);
        result.setGroupName(groupVo.getGroupName());
        result.setTopImagePath(groupVo.getTopImagePath());
        result.setListImagePath(groupVo.getListImagePath());

        // 이달의 등급 산정 기간 계산
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        YearMonth yearMonth = YearMonth.now().minusMonths(1);
        String endDate = yearMonth.atEndOfMonth().format(dateTimeFormatter);
        yearMonth = YearMonth.now().minusMonths(groupVo.getCalculatePeriod());
        String startDate = yearMonth.atDay(1).format(dateTimeFormatter);

        result.setCalculatePeriodStart(startDate);
        result.setCalculatePeriodEnd(endDate);


        // 주문건수, 금액 조회
        startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-01";
        endDate = LocalDate.now().format(dateTimeFormatter);

        OrderInfoFromUserGroupVo orderInfo = orderFrontBiz.getOrderCountFromUserGroup(urUserId, startDate, endDate);
        if(orderInfo != null) {
            result.setOrderCount(orderInfo.getOrderCount());
            result.setOrderPrice(orderInfo.getPaidPriceSum());
        }

        // 그룹 정보 설정
        List<GroupListVo> groupList = userGroupService.getGroupList();
        for (GroupListVo vo : groupList) {
            // 그룹 혜택 설정
            vo.setBenefit(userGroupService.getGroupBenefit(vo.getUrGroupId()));
        }
        result.setGroup(groupList);

        // 예상 등급 정보 설정
        yearMonth = YearMonth.now();
        endDate = yearMonth.atEndOfMonth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        yearMonth = YearMonth.now().plusMonths(1).minusMonths(groupVo.getCalculatePeriod());
        startDate = yearMonth.atDay(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        OrderInfoFromUserGroupVo estimationOrderInfo = orderFrontBiz.getOrderCountFromUserGroup(urUserId, startDate, endDate);

        EstimationGroupInfoResponseDto estimationDto = userGroupService.getEstimationGroupNameByUser(estimationOrderInfo, groupList);
        result.setEstimationGroupName(estimationDto.getGroupName());
        result.setEstimationTopImagePath(estimationDto.getTopImagePath());
        result.setEstimationListImagePath(estimationDto.getListImagePath());

        return result;
    }

    @Override
    public GroupInfoByUserResultVo getGroupByUser(Long urUserId) throws Exception {
        return userGroupService.getGroupByUser(urUserId);
    }

    @Override
    public Long getDefaultGroup() throws Exception {
        return userGroupService.getDefaultGroup();
    }

    @Override
    public String getGroupNameByMeta() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return UserEnums.MetaType.NONE_GROUP.getCodeName();
        }

        GroupInfoByUserResultVo result = userGroupService.getGroupByUser(Long.valueOf(buyerVo.getUrUserId()));
        return result.getGroupName();
    }

}
