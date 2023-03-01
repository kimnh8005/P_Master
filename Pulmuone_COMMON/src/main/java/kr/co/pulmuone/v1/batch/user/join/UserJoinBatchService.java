package kr.co.pulmuone.v1.batch.user.join;

import kr.co.pulmuone.v1.batch.order.front.OrderFrontBatchBiz;
import kr.co.pulmuone.v1.batch.user.group.UserGroupBatchBiz;
import kr.co.pulmuone.v1.batch.user.group.dto.vo.UserGroupBenefitPointVo;
import kr.co.pulmuone.v1.batch.user.join.dto.vo.JoinBatchVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.mappers.batch.master.user.UserJoinBatchMapper;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserJoinBatchService {

    private final UserJoinBatchMapper userJoinBatchMapper;
    private final UserGroupBatchBiz userGroupBatchBiz;
    private final OrderFrontBatchBiz orderFrontBatchBiz;
    private final PointBiz pointBiz;

    /**
     * MALL 추천 받은사람 적립금 지급
     */
    public void runUserJoinDepositPoint() throws Exception {
        // 1. 대상정보 조회
        // 대상자 조회
        List<JoinBatchVo> joinList = userJoinBatchMapper.getUserJoin(BuyerConstants.USER_JOIN_DEPOSIT_DAY);
        if (joinList == null || joinList.size() == 0) return;
        Map<Long, JoinBatchVo> joinMap = new HashMap<>();
        joinList.forEach(vo -> joinMap.put(vo.getUrUserId(), vo));

        // 그룹혜택 정보 조회
        List<UserGroupBenefitPointVo> groupBenefitList = userGroupBatchBiz.getUserGroupBenefitPoint();
        Map<Long, List<Long>> groupBenefitMap = new HashMap<>();
        for (UserGroupBenefitPointVo dto : groupBenefitList) {
            List<Long> list = groupBenefitMap.getOrDefault(dto.getUrGroupId(), new ArrayList<>());
            list.add(dto.getBenefitRelationId());
            groupBenefitMap.put(dto.getUrGroupId(), list);
        }

        // 2.주문정보 조회
        List<Long> orderList = orderFrontBatchBiz.getOrderInfoFromUserJoin(joinList.stream()
                .map(JoinBatchVo::getUrUserId)
                .collect(Collectors.toList()), BuyerConstants.USER_JOIN_DEPOSIT_DAY);
        if (orderList == null || orderList.size() == 0) return;

        // 3. 적립금 지급
        List<Long> resultUserIdList = new ArrayList<>();
        for (Long urUserId : orderList) {
            JoinBatchVo joinBatchVo = joinMap.get(urUserId);
            List<Long> pmPointIdList = groupBenefitMap.get(joinBatchVo.getRecommUrGroupId());
            for (Long pmPointId : pmPointIdList) {
                ApiResult apiResult = pointBiz.depositRecommendationPoint(joinBatchVo.getRecommUserId(), pmPointId, urUserId, String.valueOf(joinBatchVo.getRecommUrGroupId()));
            }
            resultUserIdList.add(urUserId);
        }

        // 4. 정리작업
        // 4-1. 회원 추천인 적립금 지급 처리
        if (resultUserIdList.size() == 0) return;
        userJoinBatchMapper.putUserJoinRecommGiveYn(resultUserIdList);
    }

}
