package kr.co.pulmuone.v1.customer.feedback.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.feedback.dto.*;

import java.util.List;

public interface CustomerFeedbackBiz {

    FeedbackInfoByUserResponseDto getFeedbackInfoByUser(Long urUserId, Long urGroupId) throws Exception;

    FeedbackTargetListByUserResponseDto getFeedbackTargetListByUser(FeedbackTargetListByUserRequestDto paramDto) throws Exception;

    /**
     * 나의상품구매후기 목록조회
     */
    FeedbackByUserResponseDto getFeedbackByUser(FeedbackByUserRequestDto paramDto) throws Exception;

    /**
     * 상품구매후기 등록
     */
    ApiResult<?> addFeedback(FeedbackRequestDto dto) throws Exception;

    /**
     * 상품구매 후기 별점 점수 목록
     */
    List<FeedbackScorePercentDto> getFeedbackScorePercentList(List<Long> ilGoodsIdList) throws Exception;

    /**
     * 상품후기 각각의 총갯수 가져오기
     */
    FeedbackEachCountDto getFeedbackEachCount(List<Long> ilGoodsIdList, Long ilGoodsId) throws Exception;

    /**
     * 상품 후기 이미지 목록 조회
     */
    FeedbackImageListByGoodsResponseDto getFeedbackImageListByGoods(FeedbackImageListByGoodsRequestDto dto) throws Exception;

    /**
     * 상품 후기 목록 조회
     */
    FeedbackListByGoodsResponseDto getFeedbackListByGoods(FeedbackListByGoodsRequestDto dto) throws Exception;

    /**
     * 상품 상세 후기 추천
     */
    ApiResult<?> putFeedbackSetBestYes(FeedbackSetBestRequestDto dto) throws Exception;

    /**
     * 상품 상세 후기 추천 취소
     */
    ApiResult<?> putFeedbackSetBestNo(FeedbackSetBestRequestDto dto) throws Exception;
}
