package kr.co.pulmuone.v1.customer.feedback.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.customer.feedback.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerFeedbackServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private CustomerFeedbackService customerFeedbackService;

    @Test
    void getFeedbackByUser_성공() throws Exception {

        FeedbackByUserRequestDto dto = new FeedbackByUserRequestDto();
        dto.setPage(1);
        dto.setEPage(20);
        dto.setUrUserId(1L);
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2020-12-31");

        // when
        FeedbackByUserResponseDto resultDto = customerFeedbackService.getFeedbackByUser(dto);

        assertTrue(resultDto.getTotal() > 0);

    }

    @Test
    void addFeedback_성공() throws Exception {
        // given
        FeedbackRequestDto dto = new FeedbackRequestDto();
        dto.setComment("11111");
        dto.setIlGoodsId(1L);
        dto.setIlItemCd("1");
        dto.setOdOrderDetlId("99");
        dto.setOdOrderId("99");
        dto.setSatisfactionScore("5");
        dto.setUrUserId(1L);
        dto.setUrGroupId(8L);

        //when, then
        customerFeedbackService.addFeedback(dto);
    }

    @Test
    void addFeedback_이미지_성공() throws Exception {
        // given
        FeedbackRequestDto dto = new FeedbackRequestDto();
        dto.setComment("11111");
        dto.setIlGoodsId(1L);
        dto.setIlItemCd("1");
        dto.setOdOrderDetlId("99");
        dto.setOdOrderId("99");
        dto.setSatisfactionScore("5");
        dto.setUrUserId(1L);
        dto.setUrGroupId(8L);

        List<FeedbackAttcRequestDto> imageList = new ArrayList<>();
        FeedbackAttcRequestDto imageDto = new FeedbackAttcRequestDto();
        imageDto.setImagePath("/test");
        imageDto.setImageName("1DDD.jpg");
        imageDto.setImageOriginalName("test.jpg");
        imageList.add(imageDto);
        dto.setList(imageList);

        //when, then
        customerFeedbackService.addFeedback(dto);
    }

    @Test
    void getFeedbackScorePercentList_성공() throws Exception {
        //given
        List<Long> ilGoodsId = Collections.singletonList(1L);

        //when
        List<FeedbackScorePercentDto> list = customerFeedbackService.getFeedbackScorePercentList(ilGoodsId);

        //then
        assertTrue(list.size() > 0);
    }

    @Test
    void getFeedbackEachCount_성공() throws Exception {

        List<Long> ilGoodsIdList = Collections.singletonList(1L);
        Long ilGoodsId = 1L;

        FeedbackEachCountDto resultDto = customerFeedbackService.getFeedbackEachCount(ilGoodsIdList, ilGoodsId);

        assertTrue(resultDto.getFeedbackTotalCount() > 0);
    }

    @Test
    void getFeedbackImageListByGoods_성공() throws Exception {

        FeedbackImageListByGoodsRequestDto dto = new FeedbackImageListByGoodsRequestDto();
        dto.setUrUserId(1646939L);
        List<Long> ilGoodsIdList = new ArrayList<>();
        ilGoodsIdList.add(17886L);
        dto.setIlGoodsIdList(ilGoodsIdList);

        // when
        FeedbackImageListByGoodsResponseDto resultDto = customerFeedbackService.getFeedbackImageListByGoods(dto);

        assertTrue(resultDto.getTotal() > 0);

    }

    @Test
    void getFeedbackListByGoods_성공() throws Exception {
        //given
        FeedbackListByGoodsRequestDto dto = new FeedbackListByGoodsRequestDto();
        dto.setUrUserId(1646939L);
        List<Long> ilGoodsIdList = new ArrayList<>();
        ilGoodsIdList.add(17886L);
        dto.setIlGoodsIdList(ilGoodsIdList);

        // when
        FeedbackListByGoodsResponseDto resultDto = customerFeedbackService.getFeedbackListByGoods(dto);

        //then
        assertTrue(resultDto.getTotal() > 0);
    }

    @Test
    void putFeedbackSetBestYes_성공() throws Exception {

        FeedbackSetBestRequestDto dto = new FeedbackSetBestRequestDto();
        dto.setFbFeedbackId(1L);
        dto.setUrUserId(1646939L);
        // when
        customerFeedbackService.putFeedbackSetBestYes(dto);

    }

    @Test
    void putFeedbackSetBestNo_성공() throws Exception {

        FeedbackSetBestRequestDto dto = new FeedbackSetBestRequestDto();
        dto.setFbFeedbackId(1L);
        dto.setUrUserId(1646939L);
        // when
        customerFeedbackService.putFeedbackSetBestNo(dto);

    }

    @Test
    void isExistFeedbackBest_성공() throws Exception {

        FeedbackSetBestRequestDto dto = new FeedbackSetBestRequestDto();
        dto.setFbFeedbackId(1L);
        dto.setUrUserId(1646939L);
        // when
        boolean trueornot = customerFeedbackService.isExistFeedbackBest(dto);
        assertTrue(trueornot);

    }

}
