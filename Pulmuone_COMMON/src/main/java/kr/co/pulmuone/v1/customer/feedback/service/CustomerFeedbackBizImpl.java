package kr.co.pulmuone.v1.customer.feedback.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.CustomerConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.customer.feedback.dto.*;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackTargetListByUserResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.PackageGoodsListDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromFeedbackVo;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.policy.bbs.service.PolicyBbsBannedWordBiz;
import kr.co.pulmuone.v1.promotion.point.dto.vo.GoodsFeedbackPointRewardSettingVo;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerFeedbackBizImpl implements CustomerFeedbackBiz {

    @Autowired
    private CustomerFeedbackService customerFeedbackService;

    @Autowired
    private PolicyBbsBannedWordBiz policyBbsBannedWordBiz;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    @Autowired
    private GoodsGoodsBiz goodsGoodsBiz;

    @Autowired
    private PromotionPointBiz promotionPointBiz;

    @Override
    public FeedbackInfoByUserResponseDto getFeedbackInfoByUser(Long urUserId, Long urGroupId) throws Exception {
        FeedbackTargetListByUserRequestDto dto = new FeedbackTargetListByUserRequestDto();
        dto.setUrUserId(urUserId);
        dto.setPage(1);
        dto.setLimit(9999);

        FeedbackInfoByUserResponseDto responseDto = new FeedbackInfoByUserResponseDto();
        responseDto.setFeedbackCount((int) orderFrontBiz.getOrderInfoFromFeedback(dto, CustomerConstants.FEEDBACK_DAY).getTotal());

        //적립금정보 설정
        List<GoodsFeedbackPointRewardSettingVo> pointInfo = promotionPointBiz.getGoodsFeedbackPointRewardSettingList(urGroupId);
        if(pointInfo != null && !pointInfo.isEmpty()){
            responseDto.setExistPointYn("Y");
            responseDto.setNormalAmount(pointInfo.get(0).getNomalAmount());
            responseDto.setPhotoAmount(pointInfo.get(0).getPhotoAmount());
            responseDto.setPremiumAmount(pointInfo.get(0).getPremiumAmount());
        }else{
            responseDto.setExistPointYn("N");
        }

        return responseDto;
    }

    @Override
    public FeedbackTargetListByUserResponseDto getFeedbackTargetListByUser(FeedbackTargetListByUserRequestDto dto) throws Exception {
        Page<OrderInfoFromFeedbackVo> orderInfoList = orderFrontBiz.getOrderInfoFromFeedback(dto, CustomerConstants.FEEDBACK_DAY);
        List<GoodsFeedbackPointRewardSettingVo> pointInfo = promotionPointBiz.getGoodsFeedbackPointRewardSettingList(dto.getUrGroupId());
        String existPointYn = "N";
        if(pointInfo != null && !pointInfo.isEmpty()){
            existPointYn = "Y";
        }

        List<FeedbackTargetListByUserResultVo> resultList = new ArrayList<>();
        for (OrderInfoFromFeedbackVo vo : orderInfoList) {
            FeedbackTargetListByUserResultVo feedbackVo = new FeedbackTargetListByUserResultVo(vo);
            if (vo.getEvEventId() != null && vo.getEvEventId() != 0L && vo.getPackType().equals(FeedbackEnums.PackType.NORMAL.getCode())) {
                feedbackVo.setExperienceYn("Y");
            }
            feedbackVo.setDday(DateUtil.getBetweenDays(vo.getDiDate()) + CustomerConstants.FEEDBACK_DAY);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            feedbackVo.setFeedbackEndDate(LocalDate.parse(vo.getDiDate(), dateTimeFormatter).plusDays(CustomerConstants.FEEDBACK_DAY).format(dateTimeFormatter));

            //적립금정보 설정
            feedbackVo.setExistPointYn(existPointYn);
            if("Y".equals(existPointYn)){
                feedbackVo.setNormalAmount(pointInfo.get(0).getNomalAmount());
                feedbackVo.setPhotoAmount(pointInfo.get(0).getPhotoAmount());
                feedbackVo.setPremiumAmount(pointInfo.get(0).getPremiumAmount());
            }
            resultList.add(feedbackVo);
        }

        return FeedbackTargetListByUserResponseDto
                .builder()
                .total((int) orderInfoList.getTotal())
                .list(resultList)
                .build();
    }

    /**
     * 나의상품구매후기 목록조회
     *
     * @throws Exception
     */
    @Override
    public FeedbackByUserResponseDto getFeedbackByUser(FeedbackByUserRequestDto dto) throws Exception {
        return customerFeedbackService.getFeedbackByUser(dto);
    }

    /**
     * 상품구매후기 등록
     */
    @Override
    public ApiResult<?> addFeedback(FeedbackRequestDto dto) throws Exception {
        //금칙어 - 마스킹 하여 저장
        dto.setComment(policyBbsBannedWordBiz.filterSpamWord(dto.getComment(), BaseEnums.EnumSiteType.MALL));
        return customerFeedbackService.addFeedback(dto);
    }

    /**
     * 상품구매 후기 별점 점수 목록
     */
    @Override
    public List<FeedbackScorePercentDto> getFeedbackScorePercentList(List<Long> ilGoodsIdList) throws Exception {

        return customerFeedbackService.getFeedbackScorePercentList(ilGoodsIdList);
    }

    /**
     * 상품후기 각각의 총갯수 가져오기
     */
    @Override
    public FeedbackEachCountDto getFeedbackEachCount(List<Long> ilGoodsIdList, Long ilGoodsId) throws Exception {

        return customerFeedbackService.getFeedbackEachCount(ilGoodsIdList, ilGoodsId);
    }

    /**
     * 상품 후기 이미지 목록 조회
     */
    @Override
    public FeedbackImageListByGoodsResponseDto getFeedbackImageListByGoods(FeedbackImageListByGoodsRequestDto dto) throws Exception {
        GoodsRequestDto goodsRequestDto = new GoodsRequestDto();
        goodsRequestDto.setIlGoodsId(dto.getIlGoodsId());
        BasicSelectGoodsVo goodsVo = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);
        List<Long> ilGoodsIdList = new ArrayList<>();
        ilGoodsIdList.add(dto.getIlGoodsId());

        if (goodsVo.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
            List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(dto.getIlGoodsId(), dto.isMember(), dto.isEmployee(), false, null, 1);
            ilGoodsIdList.addAll(goodsPackageList.stream()
                    .map(PackageGoodsListDto::getIlGoodsId)
                    .collect(Collectors.toList()));
        }

        dto.setIlGoodsIdList(ilGoodsIdList);
        return customerFeedbackService.getFeedbackImageListByGoods(dto);
    }

    /**
     * 상품 후기 목록 조회
     */
    @Override
    public FeedbackListByGoodsResponseDto getFeedbackListByGoods(FeedbackListByGoodsRequestDto dto) throws Exception {
        GoodsRequestDto goodsRequestDto = new GoodsRequestDto();
        goodsRequestDto.setIlGoodsId(dto.getIlGoodsId());
        BasicSelectGoodsVo goodsVo = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);
        List<Long> ilGoodsIdList = new ArrayList<>();
        ilGoodsIdList.add(dto.getIlGoodsId());

        if (goodsVo.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
            List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(dto.getIlGoodsId(), dto.isMember(), dto.isEmployee(), false, null, 1);
            ilGoodsIdList.addAll(goodsPackageList.stream()
                    .map(PackageGoodsListDto::getIlGoodsId)
                    .collect(Collectors.toList()));
        }

        dto.setIlGoodsIdList(ilGoodsIdList);

        return customerFeedbackService.getFeedbackListByGoods(dto);
    }

    /**
     * 상품 상세 후기 추천
     */
    @Override
    public ApiResult<?> putFeedbackSetBestYes(FeedbackSetBestRequestDto dto) throws Exception {
        if (customerFeedbackService.isExistFeedbackBest(dto)) {
            return ApiResult.result(UserEnums.Feedback.ALREADY_BEST);
        }
        return ApiResult.success(customerFeedbackService.putFeedbackSetBestYes(dto));
    }

    /**
     * 상품 상세 후기 추천 취소
     */
    @Override
    public ApiResult<?> putFeedbackSetBestNo(FeedbackSetBestRequestDto dto) throws Exception {
        if (!customerFeedbackService.isExistFeedbackBest(dto)) {
            return ApiResult.result(UserEnums.Feedback.CANCEL_BEST);
        }
        return ApiResult.success(customerFeedbackService.putFeedbackSetBestNo(dto));
    }
}
