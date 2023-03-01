package kr.co.pulmuone.v1.batch.goods.etc;

import kr.co.pulmuone.v1.batch.goods.etc.dto.vo.GoodsFeedbackVo;
import kr.co.pulmuone.v1.batch.goods.etc.dto.vo.GoodsRankingVo;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderCountVo;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.GoodsEtcBatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodsEtcBatchService {
    private final GoodsEtcBatchMapper goodsEtcBatchMapper;

    /**
     * 상품 랭킹 등록 - 풀무원 대카
     *
     * @param orderCountList List<OrderCountVo>
     */
    protected void addGoodsRankingPulmuone(List<OrderCountVo> orderCountList, boolean addAll) {
        // 대카테고리별 수집
        Map<Long, List<OrderCountVo>> maps = new HashMap<>();
        List<OrderCountVo> targetList = orderCountList.stream()
                .filter(vo -> vo.getPulmuoneCategoryId() != 0L) // 미사용 카테고리(0) - 제거
                .collect(Collectors.toList());

        targetList.forEach(vo -> {
                    List<OrderCountVo> orderCountVoList = maps.getOrDefault(vo.getPulmuoneCategoryId(), new ArrayList<>());
                    orderCountVoList.add(vo);
                    maps.put(vo.getPulmuoneCategoryId(), orderCountVoList);
                }
        );

        if (addAll) {
            addGoodsRankingALL(targetList, GoodsEnums.MallDiv.PULMUONE.getCode());
        }
        addGoodsRankingCategory(maps, GoodsEnums.MallDiv.PULMUONE.getCode());
    }

    /**
     * 상품 랭킹 등록 - 올가 대카
     *
     * @param orderCountList List<OrderCountVo>
     */
    protected void addGoodsRankingOrga(List<OrderCountVo> orderCountList) {
        // 대카테고리별 수집
        Map<Long, List<OrderCountVo>> maps = new HashMap<>();
        List<OrderCountVo> targetList = orderCountList.stream()
                .filter(vo -> vo.getOrgaCategoryId() != 0L) // 미사용 카테고리(0) - 제거
                .collect(Collectors.toList());

        targetList.forEach(vo -> {
                    List<OrderCountVo> orderCountVoList = maps.getOrDefault(vo.getOrgaCategoryId(), new ArrayList<>());
                    orderCountVoList.add(vo);
                    maps.put(vo.getOrgaCategoryId(), orderCountVoList);
                }
        );

        addGoodsRankingALL(targetList, GoodsEnums.MallDiv.ORGA.getCode());
        addGoodsRankingCategory(maps, GoodsEnums.MallDiv.ORGA.getCode());
    }

    /**
     * 상품 랭킹 등록 - 잇슬림 대카
     *
     * @param orderCountList List<OrderCountVo>
     */
    protected void addGoodsRankingEatslim(List<OrderCountVo> orderCountList) {
        // 대카테고리별 수집
        Map<Long, List<OrderCountVo>> maps = new HashMap<>();
        List<OrderCountVo> targetList = orderCountList.stream()
                .filter(vo -> vo.getEatslimCategoryId() != 0L) // 미사용 카테고리(0) - 제거
                .collect(Collectors.toList());

        targetList.forEach(vo -> {
                    List<OrderCountVo> orderCountVoList = maps.getOrDefault(vo.getEatslimCategoryId(), new ArrayList<>());
                    orderCountVoList.add(vo);
                    maps.put(vo.getEatslimCategoryId(), orderCountVoList);
                }
        );

        addGoodsRankingALL(targetList, GoodsEnums.MallDiv.EATSLIM.getCode());
        addGoodsRankingCategory(maps, GoodsEnums.MallDiv.EATSLIM.getCode());
    }

    /**
     * 상품 랭킹 등록 - 베이비밀 대카
     *
     * @param orderCountList List<OrderCountVo>
     */
    protected void addGoodsRankingBabymeal(List<OrderCountVo> orderCountList) {
        // 대카테고리별 수집
        Map<Long, List<OrderCountVo>> maps = new HashMap<>();
        List<OrderCountVo> targetList = orderCountList.stream()
                .filter(vo -> vo.getBabymealCategoryId() != 0L) // 미사용 카테고리(0) - 제거
                .collect(Collectors.toList());

        targetList.forEach(vo -> {
                    List<OrderCountVo> orderCountVoList = maps.getOrDefault(vo.getBabymealCategoryId(), new ArrayList<>());
                    orderCountVoList.add(vo);
                    maps.put(vo.getBabymealCategoryId(), orderCountVoList);
                }
        );

        addGoodsRankingALL(targetList, GoodsEnums.MallDiv.BABYMEAL.getCode());
        addGoodsRankingCategory(maps, GoodsEnums.MallDiv.BABYMEAL.getCode());
    }

    /**
     * 상품 랭킹 등록 - 전체
     *
     * @param orderCountList List<OrderCountVo>
     * @param mallDivCode    String
     */
    private void addGoodsRankingALL(List<OrderCountVo> orderCountList, String mallDivCode) {
        int RANKING_BEST = GoodsConstants.GOODS_RANKING_ALL_BEST_FLAG;
        int ranking = 1;
        List<GoodsRankingVo> requestList = new ArrayList<>();
        for (OrderCountVo vo : orderCountList) {
            requestList.add(GoodsRankingVo.builder()
                    .mallDiv(mallDivCode)
                    .ilGoodsId(vo.getIlGoodsId())
                    .ilCtgryId(0L)
                    .ranking(ranking)
                    .bestYn((ranking <= RANKING_BEST) ? "Y" : "N")
                    .build());
            ranking++;
        }

        if (!requestList.isEmpty()) {
            goodsEtcBatchMapper.addGoodsRankingList(requestList);
        }
    }

    /**
     * 상품 랭킹 등록 - 대카
     *
     * @param orderCountList List<OrderCountVo>
     * @param mallDivCode    String
     */
    private void addGoodsRankingCategory(Map<Long, List<OrderCountVo>> maps, String mallDivCode) {
        int RANKING_BEST = GoodsConstants.GOODS_RANKING_CATEGORY_BEST_FLAG;

        // 대카테고리별 저장
        List<GoodsRankingVo> requestList = new ArrayList<>();
        for (Long ilCtgryId : maps.keySet()) {
            int ranking = 1;
            List<OrderCountVo> categoryList = maps.get(ilCtgryId);
            for (OrderCountVo vo : categoryList) {
                requestList.add(GoodsRankingVo.builder()
                        .mallDiv(mallDivCode)
                        .ilGoodsId(vo.getIlGoodsId())
                        .ilCtgryId(ilCtgryId)
                        .ranking(ranking)
                        .bestYn((ranking <= RANKING_BEST) ? "Y" : "N")
                        .build());
                ranking++;
            }
        }

        if (!requestList.isEmpty()) {
            goodsEtcBatchMapper.addGoodsRankingList(requestList);
        }
    }

    /**
     * 상품 랭킹 삭제
     */
    protected void delAllGoodsRanking() {
        goodsEtcBatchMapper.delAllGoodsRanking();
    }

    /**
     * 상품 랭킹 조회 - brandId
     */
    protected List<Long> getGoodsRankingByDpBrandId(String mallDiv, Long dpBrandId, List<Long> goodsList, int limit) {
        return goodsEtcBatchMapper.getGoodsRankingByDpBrandId(mallDiv, dpBrandId, goodsList, limit);
    }

    /**
     * 상품 랭킹 조회 - 지금세일
     */
    protected List<Long> getGoodsRankingFromNowSale(String mallDiv, List<Long> goodsList, int limit) {
        return goodsEtcBatchMapper.getGoodsRankingFromNowSale(mallDiv, goodsList, limit);
    }

    /**
     * 상품 랭킹 조회 - 중카테고리
     */
    protected List<Long> getGoodsRankingByDpCtgryId(String mallDiv, Long dpCtgryId, List<Long> goodsList, int limit, String basicYn) {
        return goodsEtcBatchMapper.getGoodsRankingByDpCtgryId(mallDiv, dpCtgryId, goodsList, limit, basicYn);
    }

    /**
     * 상품 랭킹 조회 - 산지직송
     */
    protected List<Long> getGoodsRankingFromDirectGoods(String mallDiv, List<Long> dpCtgryIdList, List<Long> goodsList, int limit) {
        return goodsEtcBatchMapper.getGoodsRankingFromDirectGoods(mallDiv, dpCtgryIdList, goodsList, limit);
    }

    /**
     * 상품 조회 - 묶음상품 제외 - 후기 집계용
     */
    protected List<GoodsFeedbackVo> getGoodsFromFeedback() {
        return goodsEtcBatchMapper.getGoodsFromFeedback();
    }

    /**
     * 상품 랭킹 조회 - 묶음상품 포함 - 후기 집계용
     */
    protected List<GoodsFeedbackVo> getGoodsPackageFromFeedback() {
        return goodsEtcBatchMapper.getGoodsPackageFromFeedback();
    }

}
