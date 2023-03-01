package kr.co.pulmuone.v1.customer.feedback.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.CustomerConstants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.FeedbackEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.enums.SerialEnums;
import kr.co.pulmuone.v1.comm.mapper.customer.feedback.CustomerFeedbackMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.feedback.dto.*;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackByUserResultVo;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackImageListByGoodsResultVo;
import kr.co.pulmuone.v1.customer.feedback.dto.vo.FeedbackListByGoodsResultVo;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200824   	 kkm            최초작성
 * =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class CustomerFeedbackService {
    private final CustomerFeedbackMapper customerFeedbackMapper;

    @Autowired
    private PointBiz pointBiz;

    /**
     * 나의상품구매후기 목록조회
     *
     * @param dto FeedbackByUserRequestDto
     * @return FeedbackByUserResponseDto
     * @throws Exception Exception
     */
    protected FeedbackByUserResponseDto getFeedbackByUser(FeedbackByUserRequestDto dto) throws Exception {
        dto.setBestCount(CustomerConstants.BEST_COUNT);
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<FeedbackByUserResultVo> rows = customerFeedbackMapper.getFeedbackByUser(dto);

        List<FeedbackByUserResultVo> list = rows.getResult();
        for (FeedbackByUserResultVo vo : list) {
            vo.setImage(customerFeedbackMapper.getFeedbackAttcByFbFeedbackId(vo.getFbFeedbackId()));
        }

        return FeedbackByUserResponseDto.builder()
                .total((int) rows.getTotal())
                .dataList(list)
                .build();
    }

    /**
     * 상품구매후기 등록
     *
     * @param dto AddFeedbackRequestDto
     * @throws Exception Exception
     */
    protected ApiResult<?> addFeedback(FeedbackRequestDto dto) throws Exception {
        dto.setDisplayYn("Y");
        dto.setBestCount("0");
        dto.setAdminBestYn("N");
        dto.setAdminExcellentYn("N");

        // validation
        int feedbackCount = customerFeedbackMapper.getFeedbackExistCnt(dto);
        if (feedbackCount > 0) {
            return ApiResult.result(FeedbackEnums.AddFeedbackMessage.FEEDBACK_EXIST);
        }

        // 체험단 후기 여부
        if (dto.getEvEventId() != null && dto.getEvEventId() > 0) {
            dto.setFeedbackProductType(FeedbackEnums.FeedbackProductType.TESTER.getCode());
        } else {
            dto.setFeedbackProductType(FeedbackEnums.FeedbackProductType.NORMAL.getCode());
        }

        // 후기 유형 설정
        FeedbackEnums.FeedbackType feedbackType = getFeedbackType(dto);
        if (feedbackType.equals(FeedbackEnums.FeedbackType.NONE)) {
            return ApiResult.result(FeedbackEnums.AddFeedbackMessage.FEEDBACK_TYPE_ERROR);
        }
        dto.setFeedbackType(feedbackType.getCode());

        // 후기 저장 프로세스 진행
        customerFeedbackMapper.addFeedback(dto);

        // 후기 이미지 저장
        List<FeedbackAttcRequestDto> imageList = dto.getList();
        if (imageList != null) {
            for (FeedbackAttcRequestDto imageDto : imageList) {
                imageDto.setFbFeedbackId(dto.getFbFeedbackId());
                customerFeedbackMapper.addFeedbackAttc(imageDto);
            }
        }

        // 적립금 발급
        ApiResult<?> pointResponse = processPoint(dto, feedbackType);
        if (!BaseEnums.Default.SUCCESS.equals(pointResponse.getMessageEnum())) {
            return pointResponse;
        }

        return ApiResult.success();
    }

    /**
     * 후기유형 계산
     *
     * @param dto FeedbackRequestDto
     * @return String
     */
    private FeedbackEnums.FeedbackType getFeedbackType(FeedbackRequestDto dto) {
        FeedbackEnums.FeedbackType feedbackType = FeedbackEnums.FeedbackType.NORMAL;
        List<FeedbackAttcRequestDto> imageList = dto.getList();
        int imageCount = 0;
        if (imageList != null) {
            imageCount = imageList.size();
        }
        int textCount = 0;
        if (StringUtil.isNotEmpty(dto.getComment())) {
            textCount = dto.getComment().length();
        }

        if (imageCount >= CustomerConstants.PREMIUM_IMAGE_N && textCount >= CustomerConstants.PREMIUM_TEXT_N) {         // 프리미엄 후기
            feedbackType = FeedbackEnums.FeedbackType.PREMIUM;
        } else if (imageCount >= CustomerConstants.PHOTO_IMAGE_N && textCount >= CustomerConstants.PHOTO_TEXT_N) {      // 포토후기
            feedbackType = FeedbackEnums.FeedbackType.PHOTO;
        }

        return feedbackType;
    }

    /**
     * 적립금 발급
     *
     * @param dto FeedbackRequestDto
     * @return String
     */
    private ApiResult<?> processPoint(FeedbackRequestDto dto, FeedbackEnums.FeedbackType feedbackType) throws Exception {
        ApiResult<?> pointResult = pointBiz.goodsFeedbackPointReward(dto.getUrUserId(), dto.getUrGroupId(), feedbackType);

        if (BaseEnums.Default.SUCCESS.equals(pointResult.getMessageEnum())) {
            return ApiResult.success();
        } else if (PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT.equals(pointResult.getMessageEnum())) {
            Object object = pointResult.getData();
            int partialPoint = 0;
            if (object.getClass().isAssignableFrom(Integer.class)) {
                partialPoint = (Integer) pointResult.getData();
            } else if (object.getClass().isAssignableFrom(Long.class)) {
                Long temp = (Long) pointResult.getData();
                partialPoint = (Math.toIntExact(temp));
            }
            return ApiResult.result(partialPoint, SerialEnums.AddPromotion.SUCCESS_ADD_POINT_PARTIAL);
        } else if (PointEnums.PointMessage.MAXIMUM_DEPOSIT_POINT_EXCEEDED.equals(pointResult.getMessageEnum())) {
            return ApiResult.result(SerialEnums.AddPromotion.MAXIMUM_DEPOSIT_POINT_EXCEEDED);
        } else if (PointEnums.PointUseMessage.SUCCESS_DEPOSIT_POINT.equals(pointResult.getMessageEnum())) {
            Object object = pointResult.getData();
            int depositPoint = 0;
            if (object.getClass().isAssignableFrom(Integer.class)) {
                depositPoint = (Integer) pointResult.getData();
            } else if (object.getClass().isAssignableFrom(Long.class)) {
                Long temp = (Long) pointResult.getData();
                depositPoint = (Math.toIntExact(temp));
            }
            return ApiResult.result(depositPoint, PointEnums.PointUseMessage.SUCCESS_DEPOSIT_POINT);
        } else {
//            return ApiResult.result(FeedbackEnums.AddFeedbackMessage.DEPOSIT_POINT_ERROR);    // 기타 적립금 오류 예외처리
            return ApiResult.success();
        }
    }

    /**
     * 상품구매 후기 별점 점수 목록
     *
     * @param ilGoodsId Long
     * @return List<GetFeedbackScorePercentDto>
     * @throws Exception Exception
     */
    protected List<FeedbackScorePercentDto> getFeedbackScorePercentList(List<Long> ilGoodsIdList) throws Exception {
        return customerFeedbackMapper.getFeedbackScorePercentList(ilGoodsIdList);
    }

    /**
     * 상품후기 각각의 총갯수 가져오기
     *
     * @param ilGoodsId Long
     * @return GetFeedbackEachCountDto
     * @throws Exception Exception
     */
    protected FeedbackEachCountDto getFeedbackEachCount(List<Long> ilGoodsIdList, Long ilGoodsId) throws Exception {
        return customerFeedbackMapper.getFeedbackEachCount(ilGoodsIdList, ilGoodsId);
    }

    /**
     * 상품 후기 이미지 목록 조회
     *
     * @param dto GetFeedbackImageListByGoodsRequestDto
     * @return GetFeedbackImageListByGoodsResponseDto
     * @throws Exception Exception
     */
    protected FeedbackImageListByGoodsResponseDto getFeedbackImageListByGoods(FeedbackImageListByGoodsRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<FeedbackImageListByGoodsResultVo> rows = customerFeedbackMapper.getFeedbackImageListByGoods(dto);

        List<FeedbackImageListByGoodsResultVo> list = rows.getResult();
        for (FeedbackImageListByGoodsResultVo vo : list) {
            vo.setImage(customerFeedbackMapper.getFeedbackAttcByFbFeedbackId(vo.getFbFeedbackId()));
        }

        return FeedbackImageListByGoodsResponseDto.builder()
                .total((int) rows.getTotal())
                .dataList(list)
                .build();
    }

    /**
     * 상품 후기 목록 조회
     *
     * @param dto GetFeedbackListByGoodsRequestDto
     * @return GetFeedbackListByGoodsResponseDto
     * @throws Exception Exception
     */
    protected FeedbackListByGoodsResponseDto getFeedbackListByGoods(FeedbackListByGoodsRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<FeedbackListByGoodsResultVo> rows = customerFeedbackMapper.getFeedbackListByGoods(dto);

        List<FeedbackListByGoodsResultVo> list = rows.getResult();
        for (FeedbackListByGoodsResultVo vo : list) {
            vo.setImage(customerFeedbackMapper.getFeedbackAttcByFbFeedbackId(vo.getFbFeedbackId()));
        }

        return FeedbackListByGoodsResponseDto.builder()
                .total((int) rows.getTotal())
                .dataList(list)
                .build();
    }

    /**
     * 상품 상세 후기 추천
     *
     * @param dto PutFeedbackSetBestRequestDto
     * @throws Exception Exception
     */
    protected int putFeedbackSetBestYes(FeedbackSetBestRequestDto dto) throws Exception {
        customerFeedbackMapper.putFeedbackBestCntPlus(dto);
        customerFeedbackMapper.addFeedbackBest(dto);

        return customerFeedbackMapper.getFeedbackBestCount(dto.getFbFeedbackId());
    }

    /**
     * -상품 상세 후기 추천
     *
     * @param dto PutFeedbackSetBestRequestDto
     * @throws Exception Exception
     */
    protected int putFeedbackSetBestNo(FeedbackSetBestRequestDto dto) throws Exception {
        customerFeedbackMapper.putFeedbackBestCntMinus(dto);
        customerFeedbackMapper.delFeedbackBest(dto);

        return customerFeedbackMapper.getFeedbackBestCount(dto.getFbFeedbackId());
    }

    /**
     * 상품 상세 후기 추천
     *
     * @param dto PutFeedbackSetBestRequestDto
     * @return boolean
     * @throws Exception Exception
     */
    protected boolean isExistFeedbackBest(FeedbackSetBestRequestDto dto) throws Exception {
        int count = customerFeedbackMapper.isExistFeedbackBest(dto);
        return count != 0;
    }
}
