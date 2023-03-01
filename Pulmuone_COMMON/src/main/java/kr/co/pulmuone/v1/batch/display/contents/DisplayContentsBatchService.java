package kr.co.pulmuone.v1.batch.display.contents;

import kr.co.pulmuone.v1.batch.display.contents.dto.vo.ContentsBatchVo;
import kr.co.pulmuone.v1.batch.goods.etc.GoodsEtcBatchBiz;
import kr.co.pulmuone.v1.batch.policy.config.PolicyConfigBatchBiz;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.constants.DisplayConstants;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.display.DisplayContentsBatchMapper;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchNewGoodsRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DisplayContentsBatchService {

    private final DisplayContentsBatchMapper displayContentsBatchMapper;
    private final GoodsEtcBatchBiz goodsEtcBatchBiz;
    private final GoodsSearchBiz goodsSearchBiz;
    private final PolicyConfigBatchBiz policyConfigBatchBiz;

    protected void runDisplayContentsBrandList() {
        /*
        전시는 page - inventory - contents 순으로 등록
        contents 도 LV1 - LV2 - LV3 순으로 배치 되어있음
        브랜드관은 LV1에 전시브랜드 정보 있음
        상품정보는 LV2에 값을 넣어야함
         */

        // 대상 조회
        List<ContentsBatchVo> contentsBatchVoList = displayContentsBatchMapper.getBatchTarget(DisplayConstants.BATCH_BRAND_INVENTORY_CODE);
        if (contentsBatchVoList == null || contentsBatchVoList.size() == 0) return;

        // 중복 제거용 Map - 수동 등록 상품 예외처리
        Map<Long, List<Long>> manualGoodsMaps = new HashMap<>();
        contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL2))
                .filter(vo -> vo.getDisplayContentsType().equals(DisplayEnums.DpCondType.MANUAL.getCode()))
                .forEach(vo -> {
                    List<Long> targetList = manualGoodsMaps.getOrDefault(vo.getLevel1ContentsId(), new ArrayList<>());
                    targetList.add(vo.getContentsId());
                    manualGoodsMaps.put(vo.getLevel1ContentsId(), targetList);
                });

        // 삭제 진행 - 기 등록 자동상품
        displayContentsBatchMapper.delContentsByInventoryId(contentsBatchVoList.get(0).getDpInventoryId(), DisplayConstants.CONTENTS_LEVEL2);

        // 상품 등록
        contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL1))
                .filter(vo -> vo.getContentsId() != null)
                .forEach(level1Vo -> {
                    List<Long> goodsList = goodsEtcBatchBiz.getGoodsRankingByDpBrandId(GoodsEnums.MallDiv.PULMUONE.getCode(), level1Vo.getContentsId(), manualGoodsMaps.get(level1Vo.getDpContsId()), DisplayConstants.BATCH_BRAND_LIMIT);
                    String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
                    String endDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
                    int count = DisplayConstants.BATCH_CONTENTS_SORT_DEFAULT;
                    for (Long goodsId : goodsList) {
                        ContentsBatchVo level2Vo = new ContentsBatchVo();
                        level2Vo.setLevel2(level1Vo);
                        level2Vo.setDisplayStartDate(startDate);
                        level2Vo.setDisplayEndDate(endDate);
                        level2Vo.setContentsId(goodsId);
                        level2Vo.setSort(count);

                        displayContentsBatchMapper.addContents(level2Vo);
                        count++;
                    }
                });
    }

    protected void runDisplayContentsNowSaleGoods() {
        /*
        지금세일_추천상품 LV1에 등록
         */

        // 인벤토리 PK 조회
        Long inventoryId = displayContentsBatchMapper.getInventoryId(DisplayConstants.BATCH_NOW_SALE_INVENTORY_CODE);
        if (inventoryId == null) return;

        List<ContentsBatchVo> contentsBatchVoList = displayContentsBatchMapper.getBatchTarget(DisplayConstants.BATCH_NOW_SALE_INVENTORY_CODE);

        // 중복 제거용 Map - 수동 등록 상품 예외처리
        List<Long> manualGoodsList = contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL1))
                .filter(vo -> vo.getDisplayContentsType().equals(DisplayEnums.DpCondType.MANUAL.getCode()))
                .map(ContentsBatchVo::getContentsId)
                .collect(Collectors.toList());

        // 삭제 진행 - 기 등록 자동상품
        displayContentsBatchMapper.delContentsByInventoryId(inventoryId, DisplayConstants.CONTENTS_LEVEL1);

        // 상품 등록
        List<Long> goodsList = goodsEtcBatchBiz.getGoodsRankingFromNowSale(GoodsEnums.MallDiv.PULMUONE.getCode(), manualGoodsList, DisplayConstants.BATCH_NOW_SALE_LIMIT);
        String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
        String endDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
        int sort = DisplayConstants.BATCH_CONTENTS_SORT_DEFAULT;
        for (Long goodsId : goodsList) {
            ContentsBatchVo vo = new ContentsBatchVo();
            vo.setDpInventoryId(inventoryId);
            vo.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
            vo.setContentsLevel(DisplayConstants.CONTENTS_LEVEL1);
            vo.setParentsContentsId(0L);
            vo.setDisplayStartDate(startDate);
            vo.setDisplayEndDate(endDate);
            vo.setDisplayRangeType(DisplayEnums.DpRangeTp.ALL.getCode());
            vo.setTitleName(DisplayConstants.BATCH_TITLE_NAME);
            vo.setContentsId(goodsId);
            vo.setDisplayContentsType(DisplayEnums.DpCondType.AUTO.getCode());
            vo.setDisplaySortType(DisplayEnums.DpSortType.MANUAL.getCode());
            vo.setSort(sort);
            vo.setUseYn("Y");
            vo.setDelYn("N");
            vo.setCreateId("0");

            displayContentsBatchMapper.addContents(vo);
            displayContentsBatchMapper.putContentsLevel1(vo.getDpContsId());
            sort++;
        }
    }

    protected void runDisplayContentsLohasGoods() {
        /*
        전시는 page - inventory - contents 순으로 등록
        contents 도 LV1 - LV2 - LV3 순으로 배치 되어있음
        LV1에 전시카테고리 정보 있음
        상품정보는 LV3에 값을 넣어야함
         */

        // 대상 조회
        List<ContentsBatchVo> contentsBatchVoList = displayContentsBatchMapper.getBatchTarget(DisplayConstants.BATCH_LOHAS_INVENTORY_CODE);

        if (contentsBatchVoList == null || contentsBatchVoList.size() == 0) return;

        // LV3 생성용 map
        Map<Long, ContentsBatchVo> contentsBatchVoMap = contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL2))
                .collect(Collectors.toMap(ContentsBatchVo::getLevel1ContentsId, vo -> vo));

        // 중복 제거용 Map - 수동 등록 상품 예외처리
        Map<Long, List<Long>> manualGoodsMaps = new HashMap<>();
        contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL3))
                .filter(vo -> vo.getDisplayContentsType().equals(DisplayEnums.DpCondType.MANUAL.getCode()))
                .forEach(vo -> {
                    List<Long> targetList = manualGoodsMaps.getOrDefault(vo.getLevel1ContentsId(), new ArrayList<>());
                    targetList.add(vo.getContentsId());
                    manualGoodsMaps.put(vo.getLevel1ContentsId(), targetList);
                });

        // 삭제 진행 - 기 등록 자동상품
        displayContentsBatchMapper.delContentsByInventoryId(contentsBatchVoList.get(0).getDpInventoryId(), DisplayConstants.CONTENTS_LEVEL3);

        // 상품 등록
        contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL1))
                .filter(vo -> vo.getDpCtgryId() != 0)
                .forEach(vo -> {
                    ContentsBatchVo level2Vo = contentsBatchVoMap.get(vo.getDpContsId());
                    if (level2Vo != null) {
                        List<Long> goodsList = goodsEtcBatchBiz.getGoodsRankingByDpCtgryId(GoodsEnums.MallDiv.PULMUONE.getCode(), vo.getDpCtgryId(), manualGoodsMaps.get(vo.getDpContsId()), DisplayConstants.BATCH_LOHAS_LIMIT, null);
                        String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
                        String endDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
                        int count = DisplayConstants.BATCH_CONTENTS_SORT_DEFAULT;
                        for (Long goodsId : goodsList) {
                            ContentsBatchVo level3Vo = new ContentsBatchVo();
                            level3Vo.setLevel3(level2Vo);
                            level3Vo.setDisplayStartDate(startDate);
                            level3Vo.setDisplayEndDate(endDate);
                            level3Vo.setContentsId(goodsId);
                            level3Vo.setSort(count);

                            displayContentsBatchMapper.addContents(level3Vo);
                            displayContentsBatchMapper.putContentsLevel3(level3Vo.getDpContsId());
                            count++;
                        }
                    }
                });
    }

    protected void runDisplayContentsOrgaMainNewGoodsJob() throws Exception {
        /*
        올가 메인 - 제일먼저만나요

        전시는 page - inventory - contents 순으로 등록
        contents 는 LV1 - LV2 - LV3 순으로 배치 되어있음
        LV1 상품정보 - 3개월이내 등록 최신순
         */

        // 인벤토리 PK 조회
        Long inventoryId = displayContentsBatchMapper.getInventoryId(DisplayConstants.BATCH_ORGA_MAIN_NEW_INVENTORY_CODE);
        if (inventoryId == null) return;

        List<ContentsBatchVo> contentsBatchVoList = displayContentsBatchMapper.getBatchTarget(DisplayConstants.BATCH_ORGA_MAIN_NEW_INVENTORY_CODE);

        // 중복 제거용 Map - 수동 등록 상품 예외처리
        List<Long> manualGoodsList = contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL1))
                .filter(vo -> vo.getDisplayContentsType().equals(DisplayEnums.DpCondType.MANUAL.getCode()))
                .map(ContentsBatchVo::getContentsId)
                .collect(Collectors.toList());

        // 삭제 진행 - 기 등록 자동상품
        displayContentsBatchMapper.delContentsByInventoryId(inventoryId, DisplayConstants.CONTENTS_LEVEL1);

        // 상품 등록
        String mallDiv = GoodsEnums.MallDiv.ORGA.getCode();
        List<Long> orgaGoodsIdList = goodsSearchBiz.getNewGoods(
                GoodsSearchNewGoodsRequestDto.builder()
                        .mallDiv(mallDiv)
                        .monthSub(GoodsConstants.NEW_GOODS_MONTH_INTERVAL_3)
                        .exceptShopOnly(true)
                        .build())
                .stream()
                .limit(DisplayConstants.BATCH_ORGA_MAIN_NEW_LIMIT)
                .collect(Collectors.toList());
        if (orgaGoodsIdList == null || orgaGoodsIdList.size() == 0) return;

        String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
        String endDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
        int sort = DisplayConstants.BATCH_CONTENTS_SORT_DEFAULT;
        for (Long goodsId : orgaGoodsIdList) {
            ContentsBatchVo vo = new ContentsBatchVo();
            vo.setDpInventoryId(inventoryId);
            vo.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
            vo.setContentsLevel(DisplayConstants.CONTENTS_LEVEL1);
            vo.setParentsContentsId(0L);
            vo.setDisplayStartDate(startDate);
            vo.setDisplayEndDate(endDate);
            vo.setDisplayRangeType(DisplayEnums.DpRangeTp.ALL.getCode());
            vo.setTitleName(DisplayConstants.BATCH_TITLE_NAME);
            vo.setContentsId(goodsId);
            vo.setDisplayContentsType(DisplayEnums.DpCondType.AUTO.getCode());
            vo.setDisplaySortType(DisplayEnums.DpSortType.MANUAL.getCode());
            vo.setSort(sort);
            vo.setUseYn("Y");
            vo.setDelYn("N");
            vo.setCreateId("0");

            displayContentsBatchMapper.addContents(vo);
            displayContentsBatchMapper.putContentsLevel1(vo.getDpContsId());
            sort++;
        }

    }

    protected void runDisplayContentsOrgaMainPbGoods() throws Exception {
        /*
        올가 메인 - 올가PB

        전시는 page - inventory - contents 순으로 등록
        contents 는 LV1 - LV2 - LV3 순으로 배치 되어있음
        LV1 상품정보 - 1개월이내 등록 올가 PB상품 최신순
         */

        // 인벤토리 PK 조회
        Long inventoryId = displayContentsBatchMapper.getInventoryId(DisplayConstants.BATCH_ORGA_MAIN_PB_INVENTORY_CODE);
        if (inventoryId == null) return;

        List<ContentsBatchVo> contentsBatchVoList = displayContentsBatchMapper.getBatchTarget(DisplayConstants.BATCH_ORGA_MAIN_PB_INVENTORY_CODE);

        // 중복 제거용 Map - 수동 등록 상품 예외처리
        List<Long> manualGoodsList = contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL1))
                .filter(vo -> vo.getDisplayContentsType().equals(DisplayEnums.DpCondType.MANUAL.getCode()))
                .map(ContentsBatchVo::getContentsId)
                .collect(Collectors.toList());

        // 삭제 진행 - 기 등록 자동상품
        displayContentsBatchMapper.delContentsByInventoryId(inventoryId, DisplayConstants.CONTENTS_LEVEL1);

        // 상품 등록
        String mallDiv = GoodsEnums.MallDiv.ORGA.getCode();
        List<String> orgaPbDPBrandIdList = Arrays.stream(policyConfigBatchBiz.getConfigValue(Constants.ORGA_PB_DP_BRAND_KEY).split(",")).collect(Collectors.toList());

        List<Long> orgaGoodsIdList = goodsSearchBiz.getNewGoods(
                GoodsSearchNewGoodsRequestDto.builder()
                        .mallDiv(mallDiv)
                        .dpBrandIdList(orgaPbDPBrandIdList)
                        .exceptShopOnly(true)
                        .build())
                .stream()
                .limit(DisplayConstants.BATCH_ORGA_MAIN_PB_LIMIT)
                .collect(Collectors.toList());
        if (orgaGoodsIdList == null || orgaGoodsIdList.size() == 0) return;

        String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
        String endDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
        int sort = DisplayConstants.BATCH_CONTENTS_SORT_DEFAULT;
        for (Long goodsId : orgaGoodsIdList) {
            ContentsBatchVo vo = new ContentsBatchVo();
            vo.setDpInventoryId(inventoryId);
            vo.setContentsType(DisplayEnums.ContentsType.GOODS.getCode());
            vo.setContentsLevel(DisplayConstants.CONTENTS_LEVEL1);
            vo.setParentsContentsId(0L);
            vo.setDisplayStartDate(startDate);
            vo.setDisplayEndDate(endDate);
            vo.setDisplayRangeType(DisplayEnums.DpRangeTp.ALL.getCode());
            vo.setTitleName(DisplayConstants.BATCH_TITLE_NAME);
            vo.setContentsId(goodsId);
            vo.setDisplayContentsType(DisplayEnums.DpCondType.AUTO.getCode());
            vo.setDisplaySortType(DisplayEnums.DpSortType.MANUAL.getCode());
            vo.setSort(sort);
            vo.setUseYn("Y");
            vo.setDelYn("N");
            vo.setCreateId("0");

            displayContentsBatchMapper.addContents(vo);
            displayContentsBatchMapper.putContentsLevel1(vo.getDpContsId());
            sort++;
        }

    }

    protected void runDisplayContentsOrgaMainDirectGoods() throws Exception {
        setOrgaMainDirectGoods(DisplayConstants.BATCH_ORGA_MAIN_DIRECT_INVENTORY_CODE, DisplayConstants.BATCH_ORGA_MAIN_DIRECT_LIMIT);
    }

    private void setOrgaMainDirectGoods(String inventoryCode, int limit) throws Exception {
        /*
        올가 메인 - 산지직송

        전시는 page - inventory - contents 순으로 등록
        contents 는 LV1 - LV2 - LV3 순으로 배치 되어있음
        LV1 배너 - 배너정보 타이틀명으로 구분(과일, 채소, 수산, 정육)
        LV2 상품정보 - 특정전시카테고리 - 베스트순, 동일 판매량일 경우 최근 등록순
         */

        // 대상 조회
        List<ContentsBatchVo> contentsBatchVoList = displayContentsBatchMapper.getBatchTarget(inventoryCode);
        if (contentsBatchVoList == null || contentsBatchVoList.size() == 0) return;

        // 중복 제거용 Map - 수동 등록 상품 예외처리
        Map<Long, List<Long>> manualGoodsMaps = new HashMap<>();
        contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL2))
                .filter(vo -> vo.getDisplayContentsType().equals(DisplayEnums.DpCondType.MANUAL.getCode()))
                .forEach(vo -> {
                    List<Long> targetList = manualGoodsMaps.getOrDefault(vo.getLevel1ContentsId(), new ArrayList<>());
                    targetList.add(vo.getContentsId());
                    manualGoodsMaps.put(vo.getLevel1ContentsId(), targetList);
                });

        // 삭제 진행 - 기 등록 자동상품
        displayContentsBatchMapper.delContentsByInventoryId(contentsBatchVoList.get(0).getDpInventoryId(), DisplayConstants.CONTENTS_LEVEL2);

        // LV1 전시 중 카테고리 배치
        for (ContentsBatchVo vo : contentsBatchVoList) {
            switch (vo.getTitleName()) {
                case "과일":
                    vo.setDpCtgryIdList(Collections.singletonList(DisplayConstants.BATCH_CATEGORY_FRUIT));
                    break;
                case "채소":
                    vo.setDpCtgryIdList(Collections.singletonList(DisplayConstants.BATCH_CATEGORY_VEGETABLE));
                    break;
                case "수산":
                    vo.setDpCtgryIdList(Collections.singletonList(DisplayConstants.BATCH_CATEGORY_FISH));
                    break;
                case "정육":
                    vo.setDpCtgryIdList(Collections.singletonList(DisplayConstants.BATCH_CATEGORY_BEEF));
                    break;
            }
        }

        // 상품 등록
        contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL1))
                .filter(vo -> vo.getContentsId() != null)
                .filter(vo -> vo.getDpCtgryIdList() != null)
                .forEach(level1Vo -> {
                    List<Long> goodsList = goodsEtcBatchBiz.getGoodsRankingFromDirectGoods(GoodsEnums.MallDiv.ORGA.getCode(), level1Vo.getDpCtgryIdList(), manualGoodsMaps.get(level1Vo.getDpContsId()), limit);
                    String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
                    String endDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
                    int count = DisplayConstants.BATCH_CONTENTS_SORT_DEFAULT;
                    for (Long goodsId : goodsList) {
                        ContentsBatchVo level2Vo = new ContentsBatchVo();
                        level2Vo.setLevel2(level1Vo);
                        level2Vo.setDisplayStartDate(startDate);
                        level2Vo.setDisplayEndDate(endDate);
                        level2Vo.setContentsId(goodsId);
                        level2Vo.setSort(count);

                        displayContentsBatchMapper.addContents(level2Vo);
                        count++;
                    }
                });
    }

    protected void runDisplayContentsOrgaPbGoods() throws Exception {
        /*
        올가 메인 - 올가PB

        전시는 page - inventory - contents 순으로 등록
        contents 는 LV1 - LV2 - LV3 순으로 배치 되어있음
        LV1 카테고리
        LV2 상품정보 - 1개월이내 등록 올가 PB상품 최신순
         */

        // 대상 조회
        List<ContentsBatchVo> contentsBatchVoList = displayContentsBatchMapper.getBatchTarget(DisplayConstants.BATCH_ORGA_PB_INVENTORY_CODE);
        if (contentsBatchVoList == null || contentsBatchVoList.size() == 0) return;

        // 중복 제거용 Map - 수동 등록 상품 예외처리
        Map<Long, List<Long>> manualGoodsMaps = new HashMap<>();
        contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL2))
                .filter(vo -> vo.getDisplayContentsType().equals(DisplayEnums.DpCondType.MANUAL.getCode()))
                .forEach(vo -> {
                    List<Long> targetList = manualGoodsMaps.getOrDefault(vo.getLevel1ContentsId(), new ArrayList<>());
                    targetList.add(vo.getContentsId());
                    manualGoodsMaps.put(vo.getLevel1ContentsId(), targetList);
                });

        // 삭제 진행 - 기 등록 자동상품
        displayContentsBatchMapper.delContentsByInventoryId(contentsBatchVoList.get(0).getDpInventoryId(), DisplayConstants.CONTENTS_LEVEL2);

        // 상품 등록
        contentsBatchVoList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL1))
                .filter(vo -> vo.getContentsId() != null)
                .forEach(level1Vo -> {
                    List<String> orgaPbDPBrandIdList = Arrays.stream(policyConfigBatchBiz.getConfigValue(Constants.ORGA_PB_DP_BRAND_KEY).split(",")).collect(Collectors.toList());

                    List<Long> goodsList = goodsSearchBiz.getNewGoods(
                            GoodsSearchNewGoodsRequestDto.builder()
                                    .mallDiv(GoodsEnums.MallDiv.ORGA.getCode())
                                    .dpBrandIdList(orgaPbDPBrandIdList)
                                    .ctgryIdDepth1(String.valueOf(level1Vo.getDpCtgryId()))
                                    .exceptShopOnly(true)
                                    .build())
                            .stream()
                            .limit(DisplayConstants.BATCH_ORGA_PB_LIMIT)
                            .collect(Collectors.toList());
                    String startDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 00:00:00";
                    String endDate = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " 23:59:59";
                    int count = DisplayConstants.BATCH_CONTENTS_SORT_DEFAULT;
                    for (Long goodsId : goodsList) {
                        ContentsBatchVo level2Vo = new ContentsBatchVo();
                        level2Vo.setLevel2(level1Vo);
                        level2Vo.setDisplayStartDate(startDate);
                        level2Vo.setDisplayEndDate(endDate);
                        level2Vo.setContentsId(goodsId);
                        level2Vo.setSort(count);

                        displayContentsBatchMapper.addContents(level2Vo);
                        count++;
                    }
                });
    }

    protected void runDisplayContentsOrgaDirectGoods() throws Exception {
        setOrgaMainDirectGoods(DisplayConstants.BATCH_ORGA_DIRECT_INVENTORY_CODE, DisplayConstants.BATCH_ORGA_DIRECT_LIMIT);
    }

}
