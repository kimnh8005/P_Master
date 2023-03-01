package kr.co.pulmuone.v1.batch.user.group;

import kr.co.pulmuone.v1.batch.order.front.OrderFrontBatchBiz;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromUserGroupBatchVo;
import kr.co.pulmuone.v1.batch.user.buyer.UserBuyerBatchBiz;
import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;
import kr.co.pulmuone.v1.batch.user.group.dto.vo.UserGroupBenefitPointVo;
import kr.co.pulmuone.v1.batch.user.group.dto.vo.UserGroupBenefitVo;
import kr.co.pulmuone.v1.batch.user.group.dto.vo.UserGroupMasterVo;
import kr.co.pulmuone.v1.batch.user.group.dto.vo.UserGroupVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.user.group.UserGroupBatchMapper;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserGroupBatchService {

    private final UserGroupBatchMapper userGroupBatchMapper;

    private final UserBuyerBatchBiz userBuyerBatchBiz;

    private final OrderFrontBatchBiz orderFrontBatchBiz;

    private final PromotionCouponBiz promotionCouponBiz;

    /**
     * Batch 실행
     *
     * @throws Exception Exception
     */
    public void runUserGroupSetUp() {

        //등급 생성되지 않은 정보 삭제
        deleteUserGroupNoDetailData();

        //적용종료일 설정
        updateUserGroupSetUp();

    }

    /**
     * 등급 생성되지 않은 정보 삭제
     *
     * @throws Exception Exception
     */
    private int deleteUserGroupNoDetailData() {
        return userGroupBatchMapper.delUserGroupNoDetailData();
    }

    /**
     * 적용종료일 설정
     *
     * @throws Exception Exception
     */
    private int updateUserGroupSetUp() {
        return userGroupBatchMapper.putUserGroupSetUp();
    }


    /**
     * Batch 실행 - 회원등급 갱신
     */
    public void runUserGroupUpdate() throws Exception {
        // 대상 등급마스터 선정
        UserGroupMasterVo targetMaster = userGroupBatchMapper.getUserGroupMaster();
        if (targetMaster == null) return;

        // 대상 등급 정보 조회
        List<UserGroupVo> groupList = userGroupBatchMapper.getUserGroup(targetMaster.getUrGroupMasterId());
        groupList.forEach(vo -> vo.setBenefit(userGroupBatchMapper.getUserGroupBenefit(vo.getUrGroupId())));

        // 등급 산정 기간 계산
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        YearMonth yearMonth = YearMonth.now().minusMonths(1);
        String endDate = yearMonth.atEndOfMonth().format(dateTimeFormatter);
        yearMonth = YearMonth.now().minusMonths(Long.parseLong(targetMaster.getCalculatePeriod()));
        String startDate = yearMonth.atDay(1).format(dateTimeFormatter);

        String batchYearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        // 일반회원 처리
        List<UserBuyerVo> userBuyerList = userBuyerBatchBiz.getBuyerList(batchYearMonth, UserEnums.UserGroupBatchTargetType.NORMAL.getCode());
        processUserGroupUpdate(groupList, startDate, endDate, userBuyerList, UserEnums.UserGroupBatchTargetType.NORMAL.getCode());

        // 휴면회원 처리
        List<UserBuyerVo> userBuyerMoveList = userBuyerBatchBiz.getBuyerMoveList(batchYearMonth, UserEnums.UserGroupBatchTargetType.MOVE.getCode());
        processUserGroupUpdate(groupList, startDate, endDate, userBuyerMoveList, UserEnums.UserGroupBatchTargetType.MOVE.getCode());

    }

    /**
     * 회원등급 갱신 Batch - 회원리스트 -> BuyerConstants.USER_GROUP_BATCH_USER_COUNT 에 따라 분배하여 처리
     *
     * @param groupList     그룹정보
     * @param startDate     주문조회 데이터 시작일자
     * @param endDate       주문조회 데이터 종료일자
     * @param userBuyerList 유저등급 갱신 대상 회원리스트
     * @param batchTarget   유저데이터 유형(일반회원, 휴면회원) - UserEnums.UserGroupBatchTargetType
     */
    private void processUserGroupUpdate(List<UserGroupVo> groupList, String startDate, String endDate, List<UserBuyerVo> userBuyerList, String batchTarget) throws Exception {
        int limitCount = BuyerConstants.USER_GROUP_BATCH_USER_COUNT;
        int batchCount = 0;
        List<UserBuyerVo> batchBuyerList = new ArrayList<>();
        for (UserBuyerVo vo : userBuyerList) {
            batchBuyerList.add(vo);
            batchCount++;
            if (batchCount >= limitCount) {   // Batch Count 에 따라 로직 수행
                calcUserGroup(batchBuyerList, groupList, startDate, endDate, batchTarget);
                batchCount = 0;
                batchBuyerList.clear();
            }
        }
        if (batchBuyerList.size() > 0) {
            //처리로직
            calcUserGroup(batchBuyerList, groupList, startDate, endDate, batchTarget);
        }
    }

    /**
     * 회원등급 갱신 Batch
     * - 주문정보 조회 하여
     * - 등급계산
     * - 등급반영
     * - 등급혜택 제공 - 호출
     *
     * @param buyerList   회원정보
     * @param groupList   등급정보
     * @param startDate   주문조회 데이터 시작일자
     * @param endDate     주문조회 데이터 종료일자
     * @param batchTarget 유저데이터 유형(일반회원, 휴면회원) - UserEnums.UserGroupBatchTargetType
     */
    private void calcUserGroup(List<UserBuyerVo> buyerList, List<UserGroupVo> groupList, String startDate, String endDate, String batchTarget) throws Exception {

        // 주문정보 가져오기
        List<OrderInfoFromUserGroupBatchVo> orderInfoList = orderFrontBatchBiz.getOrderCountFromUserGroup(buyerList, startDate, endDate);
        Map<Long, OrderInfoFromUserGroupBatchVo> orderInfoMap = orderInfoList.stream()
                .collect(Collectors.toMap(OrderInfoFromUserGroupBatchVo::getUrUserId, vo -> vo));

        // 등급계산
        for (UserBuyerVo buyer : buyerList) {
            OrderInfoFromUserGroupBatchVo orderInfo = orderInfoMap.get(buyer.getUrUserId());
            if (orderInfo == null) {
                orderInfo = new OrderInfoFromUserGroupBatchVo();
            }
            for (UserGroupVo vo : groupList) {
                //구매건수, 구매금액
                if (orderInfo.getOrderCount() >= vo.getPurchaseCountFrom() && orderInfo.getPaidPriceSum() >= vo.getPurchaseAmountFrom()) {
                    buyer.setUrGroupId(vo.getUrGroupId());
                    buyer.setBenefit(vo.getBenefit());
                    break;
                }
            }
        }

        // 등급 반영
        if (batchTarget.equals(UserEnums.UserGroupBatchTargetType.NORMAL.getCode())) {
            userBuyerBatchBiz.putBuyerGroup(buyerList);
        } else {
            userBuyerBatchBiz.putBuyerMoveGroup(buyerList);
        }

        // 등급이력 추가
        userBuyerBatchBiz.addChangeLog(buyerList);
        // 배치등급 이력 추가
        userGroupBatchMapper.addGroupBatchHistory(buyerList);

        // 혜택 제공
        addBenefit(buyerList);
    }

    /**
     * 회원등급 갱신 Batch - 등급 혜택 제공 진행
     *
     * @param buyerList 회원정보 - 혜택정보 포함
     */
    private void addBenefit(List<UserBuyerVo> buyerList) throws Exception {
        for (UserBuyerVo buyerVo : buyerList) {
            List<UserGroupBenefitVo> userBenefit = buyerVo.getBenefit();
            for (UserGroupBenefitVo benefitVo : userBenefit) {
                if (benefitVo.getUrGroupBenefitType().equals(UserEnums.UserGroupBenefitType.COUPON.getCode())) {
                    //쿠폰
                    promotionCouponBiz.addCouponByOne(benefitVo.getBenefitRelationId(), buyerVo.getUrUserId());
                }
                //적립금 - 신규회원 추천인 배치 에서 지급
            }
        }
    }

    protected List<UserGroupBenefitPointVo> getUserGroupBenefitPoint() {
        return userGroupBatchMapper.getUserGroupBenefitPoint();
    }

}
