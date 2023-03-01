package kr.co.pulmuone.v1.customer.feedback.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.mapper.customer.feedback.FeedbackMapper;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosDetailVo;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosListVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {

	 private final FeedbackMapper feedbackMapper;

	    /**
	     * 후기관리 목록조회
	     *
	     * @param FeedbackBosRequestDto
	     * @return Page<FeedbackBosListVo>
	     * @throws Exception Exception
	     */
	 	@UserMaskingRun(system="BOS")
	    protected Page<FeedbackBosListVo> getFeedbackList(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception {
	    	PageMethod.startPage(feedbackBosRequestDto.getPage(), feedbackBosRequestDto.getPageSize());
	        return feedbackMapper.getFeedbackList(feedbackBosRequestDto);
	    }


	    /**
	     * @Desc 후기관리 리스트 엑셀 다운로드 목록 조회
	     * @param FeedbackBosRequestDto : 후기관리 리스트 검색 조건 request dto
	     * @return List<FeedbackBosListVo> : 후기관리 리스트 엑셀 다운로드 목록
	     */
	 	@UserMaskingRun(system="BOS")
	    public List<FeedbackBosListVo> feedbackExportExcel(FeedbackBosRequestDto feedbackBosRequestDto) {


            List<FeedbackBosListVo> itemList = feedbackMapper.feedbackExportExcel(feedbackBosRequestDto);

            // 화면과 동일하게 역순으로 no 지정
            for (int i = itemList.size() - 1; i >= 0; i--) {
                itemList.get(i).setRowNumber(String.valueOf(itemList.size() - i));
            }

            return itemList;
	    }


	    /**
	     * 후기관리 상세조회
	     * @param FeedbackBosRequestDto
	     * @return
	     * @throws Exception
	     */
	    @UserMaskingRun(system="BOS")
	    protected FeedbackBosDetailVo getDetailFeedback(FeedbackBosRequestDto feedbackBosRequestDto)throws Exception {
	    	return feedbackMapper.getDetailFeedback(feedbackBosRequestDto);
	    }


		/**
		* @Desc 후기관리 상세 첨부파일 이미지
		* @param String
		* @return List<FeedbackBosDetailVo>
		*/
		protected List<FeedbackBosDetailVo> getImageList(String feedbackId) {
			return feedbackMapper.getImageList(feedbackId);
		}


	    /**
	     * 후기관리정보 수정
	     * @param FeedbackBosRequestDto
	     * @return
	     * @throws Exception
	     */
	    protected FeedbackBosDetailResponseDto putFeedbackInfo(FeedbackBosRequestDto feedbackBosRequestDto) throws Exception{
	    	FeedbackBosDetailResponseDto result = new FeedbackBosDetailResponseDto();
	    	feedbackMapper.putFeedbackInfo(feedbackBosRequestDto);
	    	return result;
	    }


}
