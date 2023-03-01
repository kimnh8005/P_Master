package kr.co.pulmuone.v1.comm.mapper.customer.feedback;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.customer.feedback.dto.*;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerFeedbackMapper {

    Page<FeedbackByUserResultVo> getFeedbackByUser(FeedbackByUserRequestDto paramDto) throws Exception;

    List<FeedbackAttcVo> getFeedbackAttcByFbFeedbackId(Long fbFeedbackId) throws Exception;

    int getFeedbackExistCnt(FeedbackRequestDto dto) throws Exception;

    void addFeedback(FeedbackRequestDto dto) throws Exception;

    void addFeedbackAttc(FeedbackAttcRequestDto dto) throws Exception;

    List<FeedbackScorePercentDto> getFeedbackScorePercentList(@Param("ilGoodsIdList") List<Long> ilGoodsIdList) throws Exception;

    FeedbackEachCountDto getFeedbackEachCount(@Param("ilGoodsIdList") List<Long> ilGoodsIdList, @Param("ilGoodsId") Long ilGoodsId) throws Exception;

    Page<FeedbackImageListByGoodsResultVo> getFeedbackImageListByGoods(FeedbackImageListByGoodsRequestDto dto) throws Exception;

    Page<FeedbackListByGoodsResultVo> getFeedbackListByGoods(FeedbackListByGoodsRequestDto dto) throws Exception;

    int isExistFeedbackBest(FeedbackSetBestRequestDto dto) throws Exception;

    void putFeedbackBestCntPlus(FeedbackSetBestRequestDto dto) throws Exception;

    void putFeedbackBestCntMinus(FeedbackSetBestRequestDto dto) throws Exception;

    void addFeedbackBest(FeedbackSetBestRequestDto dto) throws Exception;

    void delFeedbackBest(FeedbackSetBestRequestDto dto) throws Exception;

    int getFeedbackBestCount(Long fbFeedbackId) throws Exception;
}
