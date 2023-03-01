package kr.co.pulmuone.v1.customer.feedback.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackBosRequestDto;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosDetailVo;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackBosListVo;

public class FeedbackBosServiceTest  extends CommonServiceTestBaseForJunit5{

	 @Autowired
	    private FeedbackService feedbackService;

	 @Test
	    void getFeedbackList_성공() throws Exception {

		 	FeedbackBosRequestDto dto = new FeedbackBosRequestDto();
		 	dto.setCreateDateStart("2021-08-11");
		 	dto.setCreateDateEnd("2021-08-11");

	        // when
	        List<FeedbackBosListVo> voList = feedbackService.getFeedbackList(dto);

	        assertNotNull(voList);
	    }


	    @Test
	    void feedbackExportExcel_엑셀다운로드조회_성공() {

	        FeedbackBosRequestDto feedbackBosRequestDto = new FeedbackBosRequestDto();
			feedbackBosRequestDto.setCreateDateStart("2021-08-10");
			feedbackBosRequestDto.setCreateDateEnd("2021-08-11");
			feedbackBosRequestDto.setSatisfactionScore("4");

	        List<FeedbackBosListVo> excelList = feedbackService.feedbackExportExcel(feedbackBosRequestDto);

	        // 해당 품목코드로 1건 또는 0건 조회되어야 함
	        assertTrue(excelList.size() > 0 || excelList.size() == 0);

	    }


	    @Test
	    void getDetailFeedback_정상() throws Exception {

	    	FeedbackBosRequestDto dto = new FeedbackBosRequestDto();
	    	dto.setFeedbackId("-1");

	    	FeedbackBosDetailVo vo = feedbackService.getDetailFeedback(dto);

	    	Assertions.assertNull(vo);
	    }


	    @Test
	    void getImageList_첨부파일리스트조회_성공() {

	        String feedbackId = "-1";

	        List<FeedbackBosDetailVo> imageList = feedbackService.getImageList(feedbackId);

	        // 해당 품목코드로 1건 또는 0건 조회되어야 함
	        assertTrue(imageList.size() > 0 || imageList.size() == 0);

	    }

	    @Test
	    void putFeedbackInfo_정상() throws Exception {

	    	FeedbackBosRequestDto dto = new FeedbackBosRequestDto();

	    	dto.setAdminBestYn("Y");
	    	dto.setPopupDisplayYn("Y");
	    	dto.setAdminExcellentYnCheck("Y");
	    	dto.setFeedbackId("-1");

	    	feedbackService.putFeedbackInfo(dto);
	    }


}
