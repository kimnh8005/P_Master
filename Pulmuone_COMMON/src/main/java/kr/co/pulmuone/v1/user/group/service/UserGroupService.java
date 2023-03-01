package kr.co.pulmuone.v1.user.group.service;

import kr.co.pulmuone.v1.comm.mapper.user.group.UserGroupMapper;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromUserGroupVo;
import kr.co.pulmuone.v1.user.group.dto.EstimationGroupInfoResponseDto;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupBenefitVo;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupInfoByUserResultVo;
import kr.co.pulmuone.v1.user.group.dto.vo.GroupListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200921   	  김경민           최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class UserGroupService {
    @Autowired
    private UserGroupMapper userGroupMapper;

    /**
     * 등급혜택 메인 조회
     *
     * @param urUserId Long
     * @return GroupInfoByUserResultVo
     * @throws Exception Exception
     */
    protected GroupInfoByUserResultVo getGroupByUser(Long urUserId) throws Exception {
        return userGroupMapper.getGroupByUser(urUserId);
    }

    /**
     * 등급혜택 메인 조회
     *
     * @param orderInfo OrderInfoFromUserGroupVo
     * @param groupList List<GroupListVo>
     * @return EstimationGroupInfoResponseDto
     */
    protected EstimationGroupInfoResponseDto getEstimationGroupNameByUser(OrderInfoFromUserGroupVo orderInfo, List<GroupListVo> groupList) {
        EstimationGroupInfoResponseDto result = new EstimationGroupInfoResponseDto();
        if (orderInfo == null) {
            orderInfo = new OrderInfoFromUserGroupVo();
        }
        for (GroupListVo vo : groupList) {
            //구매건수, 구매금액
            if (orderInfo.getOrderCount() >= vo.getPurchaseCountFrom() && orderInfo.getPaidPriceSum() >= vo.getPurchaseAmountFrom()) {
                result.setGroupName(vo.getGroupName());
                result.setTopImagePath(vo.getTopImagePath());
                result.setListImagePath(vo.getListImagePath());
                return result;
            }
        }

        return result;
    }

    /**
     * 등급 정보 조회
     *
     * @return List<GroupListVo>
     * @throws Exception Exception
     */
    protected List<GroupListVo> getGroupList() throws Exception {
        List<GroupListVo> result = userGroupMapper.getGroupList();

        String defaultAmountFrom = null;
        String defaultCountFrom = null;

        for (GroupListVo vo : result) {
            vo.setPurchaseAmountTo(defaultAmountFrom);
            vo.setPurchaseCountTo(defaultCountFrom);
            defaultAmountFrom = String.valueOf(vo.getPurchaseAmountFrom());
            defaultCountFrom = String.valueOf(vo.getPurchaseCountFrom());
        }

        return result;
    }

    /**
     * 등급 혜택 조회
     *
     * @return List<GroupBenefitVo>
     * @throws Exception Exception
     */
    protected List<GroupBenefitVo> getGroupBenefit(Long urGroupId) throws Exception {
        return userGroupMapper.getGroupBenefit(urGroupId);
    }

    /**
     * 기본 등급 조회
     *
     * @return Long
     * @throws Exception Exception
     */
    protected Long getDefaultGroup() throws Exception {
        return userGroupMapper.getDefaultGroup();
    }
}
