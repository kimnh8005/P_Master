package kr.co.pulmuone.v1.batch.customer.feedback;

import kr.co.pulmuone.v1.batch.customer.feedback.dto.FeedbackTotalBatchRequestDto;
import kr.co.pulmuone.v1.batch.goods.etc.GoodsEtcBatchBiz;
import kr.co.pulmuone.v1.batch.goods.etc.dto.vo.GoodsFeedbackVo;
import kr.co.pulmuone.v1.comm.mappers.batch.master.customer.CustomerFeedbackBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerFeedbackBatchService {

    private final CustomerFeedbackBatchMapper customerFeedbackBatchMapper;
    private final GoodsEtcBatchBiz goodsEtcBatchBiz;

    protected List<FeedbackTotalBatchRequestDto> runFeedbackTotal() {
        // 후기집계
        List<FeedbackTotalBatchRequestDto> requestDtoList = new ArrayList<>();
        // 1. 상품기준 집계 - 묶음상품 제외
        List<GoodsFeedbackVo> goodsList = goodsEtcBatchBiz.getGoodsFromFeedback();
        for (GoodsFeedbackVo vo : goodsList) {
            Double score = customerFeedbackBatchMapper.getFeedbackScore(Collections.singletonList(vo.getIlItemCd()));
            if (score == null) {
                score = 0D;
            }
            requestDtoList.add(FeedbackTotalBatchRequestDto.builder()
                    .ilGoodsId(vo.getIlGoodsId())
                    .satisfactionScore(score)
                    .build());
        }

        // 2. 상품기준 집계 - 묶음상품 만
        List<GoodsFeedbackVo> goodsPackageList = goodsEtcBatchBiz.getGoodsPackageFromFeedback();
        Map<Long, List<String>> maps = new HashMap<>();
        for (GoodsFeedbackVo vo : goodsPackageList) {
            List<String> list = maps.getOrDefault(vo.getIlGoodsId(), new ArrayList<>());
            list.add(vo.getIlItemCd());
            maps.put(vo.getIlGoodsId(), list);
        }

        for (Long ilGoodsId : maps.keySet()) {
            Double score = customerFeedbackBatchMapper.getFeedbackScore(maps.get(ilGoodsId));
            if (score == null) {
                score = 0D;
            }
            requestDtoList.add(FeedbackTotalBatchRequestDto.builder()
                    .ilGoodsId(ilGoodsId)
                    .satisfactionScore(score)
                    .build());
        }

        requestDtoList = requestDtoList.stream()
                .filter(Objects::nonNull)
                .filter(vo -> vo.getSatisfactionScore() > 0)
                .collect(Collectors.toList());

        return requestDtoList;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addFeedbackTotal(FeedbackTotalBatchRequestDto dto) {
        // 1. 삭제
        customerFeedbackBatchMapper.delFeedbackTotal(dto.getIlGoodsId());

        // 2. 저장
        customerFeedbackBatchMapper.addFeedbackTotal(dto);
    }

}
