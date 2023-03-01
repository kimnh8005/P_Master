package kr.co.pulmuone.v1.customer.reward.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.constants.CustomerConstants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CustomerEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.customer.reward.CustomerRewardMallMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.reward.dto.*;
import kr.co.pulmuone.v1.customer.reward.dto.vo.*;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromRewardVo;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerRewardMallService {

    @Autowired
    private CustomerRewardMallMapper customerRewardMallMapper;

    @Autowired
    private GoodsSearchBiz goodsSearchBiz;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    /**
     * MALL - 고객보상제 페이지 정보 조회
     *
     * @return List<RewardPageInfoVo>
     */
    protected List<RewardPageInfoVo> getRewardPageInfo(String deviceType) throws Exception {
        List<RewardPageInfoVo> list = customerRewardMallMapper.getRewardPageInfo(deviceType, null);
        for (RewardPageInfoVo vo : list) {
            if (CustomerConstants.REWARD_ALWAYS.equals(vo.getEndDate())) {
                vo.setAlwaysYn("Y");
            } else {
                vo.setAlwaysYn("N");
            }
        }
        return list;
    }

    /**
     * MALL - 고객보상제 상품 정보 조회
     *
     * @return List<RewardPageInfoVo>
     */
    protected RewardGoodsResponseDto getRewardGoods(RewardGoodsRequestDto dto) throws Exception {
        List<Long> goodsIdList = customerRewardMallMapper.getRewardTargetGoods(dto.getCsRewardId());
        if (goodsIdList.isEmpty()) RewardGoodsResponseDto.builder().total(0).build();

        //조회된 상품ID로 상품검색
        GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                .goodsIdList(goodsIdList)
                .page(dto.getPage())
                .limit(dto.getLimit())
                .build();
        List<GoodsSearchResultDto> goodsList = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);

        return RewardGoodsResponseDto.builder()
                .total(goodsList.size())
                .goods(goodsList)
                .build();
    }

    protected List<Long> getRewardTargetGoods(Long csRewardId) {
        return customerRewardMallMapper.getRewardTargetGoods(csRewardId);
    }

    /**
     * MALL - 고객보상제 정보 조회
     *
     * @return RewardInfoResponseDto
     */
    protected RewardInfoResponseDto getRewardInfo(String deviceType, Long csRewardId) throws Exception {
        List<RewardPageInfoVo> rewardList = customerRewardMallMapper.getRewardPageInfo(deviceType, csRewardId);
        if (rewardList == null || rewardList.isEmpty()) return null;

        return RewardInfoResponseDto.builder()
                .rewardName(rewardList.get(0).getRewardName())
                .rewardNotice(rewardList.get(0).getRewardNotice())
                .rewardApplyStandard(rewardList.get(0).getRewardApplyStandard())
                .build();
    }

    /**
     * MALL - 고객보상제 validation 정보 조회
     *
     * @param dto RewardRequestDto
     */
    protected Integer getRewardValidation(RewardRequestDto dto) {
        return customerRewardMallMapper.getRewardValidation(dto);
    }

    /**
     * MALL - 고객보상제 신청내역 저장
     *
     * @param dto RewardRequestDto
     */
    protected void addRewardApply(RewardRequestDto dto) {
        int result = customerRewardMallMapper.addRewardApply(dto);
        if (result > 0 && dto.getFile() != null && !dto.getFile().isEmpty()) {
            customerRewardMallMapper.addRewardApplyAttc(dto.getCsRewardApplyId(), dto.getFile());
        }
    }

    /**
     * MALL - 고객보상제 신청내역 validation
     *
     * @param validationInfo Integer
     * @return MessageCommEnum
     */
    protected MessageCommEnum addRewardValidation(Integer validationInfo) {
        if (validationInfo == null) return CustomerEnums.RewardApply.NO_CS_REWARD;

        if (validationInfo > 0) {
            return CustomerEnums.RewardApply.ALREADY_APPLY;
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * MALL - 고객보상제 신청내역 정보 조회
     *
     * @param dto RewardApplyRequestDto
     * @return RewardApplyInfoVo
     */
    protected RewardApplyInfoVo getRewardApplyInfo(RewardApplyRequestDto dto) {
        return customerRewardMallMapper.getRewardApplyInfo(dto);
    }

    /**
     * MALL - 고객보상제 신청내역 조회
     *
     * @param dto RewardApplyRequestDto
     * @return RewardApplyInfoVo
     */
    protected RewardApplyResponseDto getRewardApplyList(RewardApplyRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getLimit());

        Page<RewardApplyListVo> responseList = customerRewardMallMapper.getRewardApplyList(dto);
        List<RewardApplyListVo> result = responseList.getResult();
        for (RewardApplyListVo vo : result) {
            vo.setFile(customerRewardMallMapper.getRewardApplyAttc(vo.getCsRewardApplyId()));
        }

        return RewardApplyResponseDto.builder()
                .total(responseList.getTotal())
                .reward(result)
                .build();
    }

    /**
     * MALL - 고객보상제 신청내역 상세 조회
     *
     * @param csRewardApplyId Long
     * @return RewardApplyVo
     */
    protected RewardApplyVo getRewardApply(Long csRewardApplyId) {
        RewardApplyVo result = customerRewardMallMapper.getRewardApply(csRewardApplyId);
        if (result == null) return new RewardApplyVo();
        if (result.getOrderGoodsCount() != null && result.getOrderGoodsCount() > 0) {
            result.setOrderGoodsCount(result.getOrderGoodsCount() - 1);
        }
        result.setFile(customerRewardMallMapper.getRewardApplyAttc(csRewardApplyId));

        return result;
    }

    /**
     * MALL - 고객보상제 신청내역 Validation
     *
     * @param csRewardApplyId Long
     * @return MessageCommEnum
     */
    protected MessageCommEnum rewardApplyValidation(RewardApplyVo rewardApplyInfo, CustomerEnums.RewardValidationType rewardValidationType) {
        // 고객보상제 없음
        if (rewardApplyInfo == null || StringUtil.isEmpty(rewardApplyInfo.getRewardName())) {
            return CustomerEnums.RewardApply.NO_CS_REWARD;
        }

        //처리상태 확인
        if (CustomerEnums.RewardApplyStatus.CONFIRM.getCode().equals(rewardApplyInfo.getRewardApplyStatus())) {
            return CustomerEnums.RewardApply.APPLY_CONFIRM;
        }

        if (CustomerEnums.RewardValidationType.PUT_DEL_YN.equals(rewardValidationType)) {
            if (CustomerEnums.RewardApplyStatus.ACCEPT.getCode().equals(rewardApplyInfo.getRewardApplyStatus())) {
                return CustomerEnums.RewardApply.APPLY_CONFIRM;
            }
        } else {
            if (CustomerEnums.RewardApplyStatus.COMPLETE.getCode().equals(rewardApplyInfo.getRewardApplyStatus()) ||
                    CustomerEnums.RewardApplyStatus.IMPOSSIBLE.getCode().equals(rewardApplyInfo.getRewardApplyStatus())) {
                return CustomerEnums.RewardApply.APPLY_DONE;
            }
        }


        //유저 확인
        if (!rewardApplyInfo.getRequestUrUserId().equals(rewardApplyInfo.getUrUserId())) {
            return CustomerEnums.RewardApply.NOT_USER;
        }

        return BaseEnums.Default.SUCCESS;
    }

    /**
     * MALL - 고객보상제 신청철회
     *
     * @param csRewardApplyId Long
     */
    protected void delRewardApply(Long csRewardApplyId) {
        customerRewardMallMapper.delRewardApplyAttc(csRewardApplyId);
        customerRewardMallMapper.delRewardApply(csRewardApplyId);
    }

    /**
     * MALL - 고객보상제 신청내역 삭제
     *
     * @param csRewardApplyId Long
     */
    protected void putRewardApplyDelYn(Long csRewardApplyId) {
        customerRewardMallMapper.putRewardApplyDelYn(csRewardApplyId);
    }

    /**
     * MALL - 고객보상제 신청내역 수정
     *
     * @param dto RewardRequestDto
     */
    protected void putRewardApply(RewardRequestDto dto) {
        if (dto.getFile() != null && dto.getFile() != null && !dto.getFile().isEmpty()) {
            customerRewardMallMapper.delRewardApplyAttc(dto.getCsRewardApplyId());
            customerRewardMallMapper.addRewardApplyAttc(dto.getCsRewardApplyId(), dto.getFile());
        }
        customerRewardMallMapper.putRewardApply(dto);
    }

    /**
     * MALL - 고객보상제 주문정보 조회
     *
     * @param csRewardId Long
     * @param urUserId   Long
     */
    protected RewardOrderResponseDto getRewardOrderInfo(Long csRewardId, Long urUserId) {
        // 고객보상제 정보 조회
        RewardVo rewardInfo = customerRewardMallMapper.getRewardInfo(csRewardId);
        if (rewardInfo == null) return RewardOrderResponseDto.builder().build();

        //주문정보 조회
        List<Long> goodsIdList = null;
        if (CustomerEnums.RewardApplyStandard.ORDER_GOODS.getCode().equals(rewardInfo.getRewardApplyStandard()) &&
                CustomerEnums.RewardGoodsType.TARGET_GOODS.getCode().equals(rewardInfo.getRewardGoodsType())) {
            goodsIdList = customerRewardMallMapper.getRewardTargetGoods(csRewardId);
        }

        LocalDate startLocalDate = LocalDate.parse(rewardInfo.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String startDate = startLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDate = startLocalDate.plusMonths(rewardInfo.getOrderApprPeriod()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        OrderInfoFromRewardRequestDto orderRequestDto = OrderInfoFromRewardRequestDto.builder()
                .urUserId(urUserId)
                .startDate(startDate)
                .endDate(endDate)
                .csRewardId(csRewardId)
                .goodsIdList(goodsIdList)
                .build();
        List<OrderInfoFromRewardVo> orderInfoResult = orderFrontBiz.getOrderInfoFromReward(orderRequestDto);

        // 결과 값
        List<RewardOrderDto> responseOrderList = new ArrayList<>();

        if (CustomerEnums.RewardApplyStandard.ORDER_GOODS.getCode().equals(rewardInfo.getRewardApplyStandard())
                || CustomerEnums.RewardApplyStandard.ORDER_NUMBER.getCode().equals(rewardInfo.getRewardApplyStandard())) {
            // 주문번호기준 집계
            Map<Long, List<OrderInfoFromRewardVo>> orderMap = orderInfoResult.stream()
                    .collect(Collectors.groupingBy(OrderInfoFromRewardVo::getOdOrderId));
            orderMap.forEach((key, value) -> {
                RewardOrderDto orderDto = new RewardOrderDto(value.get(0));
                orderDto.setOrder(value);
                responseOrderList.add(orderDto);
            });
        } else if (CustomerEnums.RewardApplyStandard.PACK_DELIVERY.getCode().equals(rewardInfo.getRewardApplyStandard())) {
            // 주문번호기준 집계
            Map<Long, List<OrderInfoFromRewardVo>> orderMap = orderInfoResult.stream()
                    .collect(Collectors.groupingBy(OrderInfoFromRewardVo::getOdOrderId));
            orderMap.forEach((key, value) -> {
                // 배송일 기준 집계
                Map<String, List<OrderInfoFromRewardVo>> deliveryMap = value.stream()
                        .collect(Collectors.groupingBy(OrderInfoFromRewardVo::getDeliveryKey));
                deliveryMap.forEach((key2, value2) -> {
                    RewardOrderDto orderDto = new RewardOrderDto(value2.get(0));
                    orderDto.setOrder(value2);
                    responseOrderList.add(orderDto);
                });
            });
        }

        return RewardOrderResponseDto.builder()
                .startDate(rewardInfo.getStartDate())
                .endDate(rewardInfo.getEndDate())
                .rewardApplyStandard(rewardInfo.getRewardApplyStandard())
                .order(responseOrderList)
                .build();
    }

    /**
     * MALL - 고객보상제 - 답변 확인
     *
     * @param csRewardApplyId Long
     */
    protected void putRewardUserCheckYn(Long csRewardApplyId) {
        customerRewardMallMapper.putRewardUserCheckYn(csRewardApplyId);
    }

}
