package kr.co.pulmuone.v1.comm.mappers.batch.master.customer;

import kr.co.pulmuone.v1.batch.customer.feedback.dto.FeedbackTotalBatchRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerFeedbackBatchMapper {

    Double getFeedbackScore(@Param("list") List<String> ilItemCdList);

    void delFeedbackTotal(Long ilGoodsId);

    void addFeedbackTotal(FeedbackTotalBatchRequestDto dto);

}
