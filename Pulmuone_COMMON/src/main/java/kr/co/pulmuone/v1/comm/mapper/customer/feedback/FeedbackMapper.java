package kr.co.pulmuone.v1.comm.mapper.customer.feedback;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosDetailVo;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosListVo;

@Mapper
public interface FeedbackMapper {

	Page<FeedbackBosListVo> getFeedbackList(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception;

    List<FeedbackBosListVo> feedbackExportExcel(FeedbackBosRequestDto feedbackBosRequestDto);

    FeedbackBosDetailVo getDetailFeedback (FeedbackBosRequestDto feedbackBosRequestDto) throws Exception;

    List<FeedbackBosDetailVo> getImageList(String feedbackId);

    int putFeedbackInfo(FeedbackBosRequestDto feedbackBosRequestDto);
}
