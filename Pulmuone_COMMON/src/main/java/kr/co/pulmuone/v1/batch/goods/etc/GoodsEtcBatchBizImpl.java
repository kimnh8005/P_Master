package kr.co.pulmuone.v1.batch.goods.etc;

import kr.co.pulmuone.v1.batch.goods.etc.dto.vo.GoodsFeedbackVo;
import kr.co.pulmuone.v1.batch.order.front.OrderFrontBatchBiz;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderCountVo;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GoodsEtcBatchBizImpl implements GoodsEtcBatchBiz {

    private final GoodsEtcBatchService goodsEtcBatchService;
    private final OrderFrontBatchBiz orderFrontBatchBiz;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runGoodsRanking() {
        // 집계
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String startDateTime = localDate.minusDays(8).format(dateTimeFormatter) + " 00:00";
        String endDateTime = localDate.minusDays(1).format(dateTimeFormatter) + " 23:59";
        List<OrderCountVo> orderCountList = orderFrontBatchBiz.getOrderCountGoodsList(startDateTime, endDateTime, GoodsConstants.LOHAS_IL_CTGRY_ID);

        if (orderCountList.size() > 0) {
            // 삭제
            goodsEtcBatchService.delAllGoodsRanking();

            // 몰구분 별로 등록
            goodsEtcBatchService.addGoodsRankingPulmuone(orderCountList, true);
            goodsEtcBatchService.addGoodsRankingOrga(orderCountList);
            goodsEtcBatchService.addGoodsRankingEatslim(orderCountList);
            goodsEtcBatchService.addGoodsRankingBabymeal(orderCountList);
        }

        // 통합몰 - Lohas 추가 등록
        List<OrderCountVo> orderCountLohasList = orderFrontBatchBiz.getOrderCountLohasGoodsList(startDateTime, endDateTime, GoodsConstants.LOHAS_IL_CTGRY_ID);
        goodsEtcBatchService.addGoodsRankingPulmuone(orderCountLohasList, false);
    }

    @Override
    public List<Long> getGoodsRankingByDpBrandId(String mallDiv, Long dpBrandId, List<Long> goodsList, int limit) {
        return goodsEtcBatchService.getGoodsRankingByDpBrandId(mallDiv, dpBrandId, goodsList, limit);
    }

    @Override
    public List<Long> getGoodsRankingFromNowSale(String mallDiv, List<Long> goodsList, int limit) {
        return goodsEtcBatchService.getGoodsRankingFromNowSale(mallDiv, goodsList, limit);
    }

    @Override
    public List<Long> getGoodsRankingByDpCtgryId(String mallDiv, Long dpCtgryId, List<Long> goodsList, int limit, String basicYn) {
        return goodsEtcBatchService.getGoodsRankingByDpCtgryId(mallDiv, dpCtgryId, goodsList, limit, basicYn);
    }

    @Override
    public List<Long> getGoodsRankingFromDirectGoods(String mallDiv, List<Long> dpCtgryIdList, List<Long> goodsList, int limit) {
        return goodsEtcBatchService.getGoodsRankingFromDirectGoods(mallDiv, dpCtgryIdList, goodsList, limit);
    }

    @Override
    public List<GoodsFeedbackVo> getGoodsFromFeedback() {
        return goodsEtcBatchService.getGoodsFromFeedback();
    }

    @Override
    public List<GoodsFeedbackVo> getGoodsPackageFromFeedback() {
        return goodsEtcBatchService.getGoodsPackageFromFeedback();
    }

}
