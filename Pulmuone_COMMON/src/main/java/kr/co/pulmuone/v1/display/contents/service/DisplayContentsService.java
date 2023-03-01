package kr.co.pulmuone.v1.display.contents.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.constants.DisplayConstants;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mapper.display.DisplayContentsMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.display.contents.dto.*;
import kr.co.pulmuone.v1.display.contents.dto.vo.ContentsDetailVo;
import kr.co.pulmuone.v1.display.contents.dto.vo.InventoryInfoVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRankingRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsEtcBiz;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.pulmuone.v1.system.code.service.SystemCodeBiz;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200928   	 이원호            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class DisplayContentsService {
    @Autowired
    private DisplayContentsMapper displayContentsMapper;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    @Autowired
    private GoodsEtcBiz goodsEtcBiz;

    @Autowired
    private PolicyConfigBiz policyConfigBiz;

    private final GoodsSearchBiz goodsSearchBiz;

    @Autowired
    SystemCodeBiz systemCodeBiz;

    /**
     * 인벤토리별 컨텐츠 조회 by pageCd
     *
     * @param pageCd     String
     * @param deviceType String
     * @param userType   String
     * @return List<InventoryContentsInfoResponseDto>
     * @throws Exception Exception
     */
    protected List<InventoryContentsInfoResponseDto> getInventoryContentsInfoList(String pageCd, String deviceType, String userType) throws Exception {
        List<InventoryContentsInfoResponseDto> resultList = new ArrayList<>();

        // 인벤토리 정보 조회
        List<InventoryInfoVo> inventoryList = displayContentsMapper.getInventoryInfoList(pageCd, deviceType);
        // 컨텐츠 정보 조회
        List<ContentsDetailVo> contentsList = displayContentsMapper.getContentsDetailByPageCd(pageCd, deviceType);

        /*
        예외처리 추가
        올가 GNB 일경우 샵풀무원 GNB 키워드 추가
         */
        if (DisplayConstants.PAGE_CODE_ORGA_GNB.equals(pageCd)) {
            List<InventoryInfoVo> gnbInventoryList = displayContentsMapper.getInventoryInfoList(DisplayConstants.PAGE_CODE_PULMUONE_GNB, deviceType);
            inventoryList.addAll(
                    gnbInventoryList.stream()
                            .filter(vo -> DisplayConstants.INVENTORY_CODE_KEYWORD.equals(vo.getInventoryCode()))
                            .collect(Collectors.toList())
            );
            List<ContentsDetailVo> gnbContentsList = displayContentsMapper.getContentsDetailByPageCd(DisplayConstants.PAGE_CODE_PULMUONE_GNB, deviceType);
            contentsList.addAll(
                    gnbContentsList.stream()
                            .filter(vo -> DisplayConstants.INVENTORY_CODE_KEYWORD.equals(vo.getInventoryCode()))
                            .collect(Collectors.toList())
            );
        }

        // LV1, LV2, LV3 정렬 용
        Map<Long, List<ContentsDetailVo>> level1Map = new HashMap<>();
        Map<Long, List<ContentsDetailVo>> level2Map = new HashMap<>();
        Map<Long, List<ContentsDetailVo>> level3Map = new HashMap<>();

        contentsList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL1))
                .forEach(vo -> {
                    List<ContentsDetailVo> contentsDetailVoList = level1Map.getOrDefault(vo.getDpInventoryId(), new ArrayList<>());
                    contentsDetailVoList.add(vo);
                    level1Map.put(vo.getDpInventoryId(), contentsDetailVoList);
                });

        contentsList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL2))
                .forEach(vo -> {
                    List<ContentsDetailVo> contentsDetailVoList = level2Map.getOrDefault(vo.getLevel1ContentsId(), new ArrayList<>());
                    contentsDetailVoList.add(vo);
                    level2Map.put(vo.getLevel1ContentsId(), contentsDetailVoList);
                });

        contentsList.stream()
                .filter(vo -> vo.getContentsLevel().equals(DisplayConstants.CONTENTS_LEVEL3))
                .forEach(vo -> {
                    List<ContentsDetailVo> contentsDetailVoList = level3Map.getOrDefault(vo.getLevel2ContentsId(), new ArrayList<>());
                    contentsDetailVoList.add(vo);
                    level3Map.put(vo.getLevel2ContentsId(), contentsDetailVoList);
                });

        // 상위 인벤토리 -> LV1 -> LV2 -> LV3 배치 및 LV 별 정렬
        for (InventoryInfoVo inventoryInfoVo : inventoryList) {
            List<ContentsDetailVo> level1List = level1Map.get(inventoryInfoVo.getDpInventoryId());
            if (level1List == null || level1List.size() == 0) continue;
            level1List = level1List.stream()
                    .sorted(Comparator.comparing(ContentsDetailVo::getSort))
                    .collect(Collectors.toList());

            //LEVEL 1
            for (ContentsDetailVo level1 : level1List) {
                List<ContentsDetailVo> level2List = level2Map.get(level1.getDpContsId());
                if (level2List == null || level2List.size() == 0) continue;
                level2List = level2List.stream()
                        .sorted(Comparator.comparing(ContentsDetailVo::getSort))
                        .collect(Collectors.toList());

                //LEVEL 2
                for (ContentsDetailVo level2 : level2List) {
                    List<ContentsDetailVo> level3List = level3Map.get(level2.getDpContsId());
                    if (level3List == null || level3List.size() == 0) continue;
                    level3List = level3List.stream()
                            .sorted(Comparator.comparing(ContentsDetailVo::getSort))
                            .collect(Collectors.toList());

                    // 상품일 경우 상품정보 조회
                    if (DisplayEnums.ContentsType.GOODS.getCode().equals(inventoryInfoVo.getContentsLevel3Type())) {
                        level3List = searchGoodsInfo(level3List, deviceType, userType, DisplayEnums.GoodsSortCode.DEFAULT.getCode(), pageCd);
                    }
                    level2.setContentsLowerList(level3List);
                }

                // 상품일 경우 상품정보 조회
                if (DisplayEnums.ContentsType.GOODS.getCode().equals(inventoryInfoVo.getContentsLevel2Type())) {
                    level2List = searchGoodsInfo(level2List, deviceType, userType, DisplayEnums.GoodsSortCode.DEFAULT.getCode(), pageCd);
                }

                //예외처리 - LV3 없는 케이스 -> LV2 제외처리
                if (!DisplayEnums.ContentsType.NONE.getCode().equals(inventoryInfoVo.getContentsLevel3Type())) {
                    level2List = level2List.stream()
                            .filter(vo -> vo.getContentsLowerList() != null && vo.getContentsLowerList().size() > 0)
                            .collect(Collectors.toList());
                }
                level1.setContentsLowerList(level2List);
            }

            // 상품일 경우 상품정보 조회
            if (DisplayEnums.ContentsType.GOODS.getCode().equals(inventoryInfoVo.getContentsLevel1Type())) {
                level1List = searchGoodsInfo(level1List, deviceType, userType, DisplayEnums.GoodsSortCode.DEFAULT.getCode(), pageCd);
            }

            //예외처리 - LV2 없는 케이스 -> LV1 제외처리
            if (!DisplayEnums.ContentsType.NONE.getCode().equals(inventoryInfoVo.getContentsLevel2Type())) {
                level1List = level1List.stream()
                        .filter(vo -> vo.getContentsLowerList() != null && vo.getContentsLowerList().size() > 0)
                        .collect(Collectors.toList());
            }
            inventoryInfoVo.setContentsLevel1List(level1List);
        }

        //올가 전단행서 상품 전체브랜드 추가
        if (pageCd.equals(DisplayConstants.PAGE_CODE_ORGA_FLYER)) {
            setOrgaFlyerList(inventoryList);
        }

        // 화면 노출용 데이터로 변환
        for (InventoryInfoVo inventoryInfoVo : inventoryList) {
            InventoryContentsInfoResponseDto resultDto = new InventoryContentsInfoResponseDto();
            resultDto.setDpInventoryId(inventoryInfoVo.getDpInventoryId());
            resultDto.setInventoryCode(inventoryInfoVo.getInventoryCode());
            resultDto.setInventoryName(inventoryInfoVo.getInventoryName());

            // level1 설정
            List<ContentsDetailVo> level1List = inventoryInfoVo.getContentsLevel1List();
            if (level1List != null && level1List.size() > 0) {
                resultDto.setLevel1TotalCount(inventoryInfoVo.getContentsLevel1List().size());
                resultDto.setLevel1(contentsDetailVoToResponseDto(inventoryInfoVo.getContentsLevel1List(), deviceType));
                resultDto.setLevel1ContentsType(level1List.get(0).getContentsType());

                // level2 설정
                List<ContentsDetailVo> level2List = level1List.get(0).getContentsLowerList();
                if (level2List != null && level2List.size() > 0) {
                    List<?> mappingLv2Result = contentsDetailVoToResponseDto(level2List, deviceType);
                    resultDto.setLevel2TotalCount(mappingLv2Result.size());
                    resultDto.setLevel2(mappingLv2Result);
                    resultDto.setLevel2ContentsType(level2List.get(0).getContentsType());

                    List<ContentsDetailVo> level3List = level2List.get(0).getContentsLowerList();
                    if (level3List != null && level3List.size() > 0) {
                        List<?> mappingLv3Result = contentsDetailVoToResponseDto(level3List, deviceType);
                        resultDto.setLevel3TotalCount(mappingLv3Result.size());
                        resultDto.setLevel3(mappingLv3Result);
                        resultDto.setLevel3ContentsType(level3List.get(0).getContentsType());
                    }
                }
            }

            resultList.add(resultDto);
        }

        return resultList;
    }

    /**
     * 올가 전단행서 상품 전체브랜드 추가
     */
    private void setOrgaFlyerList(List<InventoryInfoVo> inventoryList) throws Exception {
        try {
            for (InventoryInfoVo inventoryInfoVo : inventoryList) {
                if(inventoryInfoVo.getInventoryCode().equals(DisplayConstants.PAGE_CODE_ORGA_FLYER_GDS)) {
                    // level1 설정
                    List<ContentsDetailVo> level1List = inventoryInfoVo.getContentsLevel1List();
                    if (level1List != null && level1List.size() > 1) {
                        ArrayList<ContentsDetailVo> newlevel1List = new ArrayList<ContentsDetailVo>();
                        int totalGoodsCnt = 0;
                        for (int i = 0; i < level1List.size(); i++) {
                            totalGoodsCnt = totalGoodsCnt + level1List.get(i).getContentsLowerList().size();
                            newlevel1List.add(level1List.get(i));
                            newlevel1List.get(i).setTmpContentsLowerList(new ArrayList<ContentsDetailVo>());
                            List<ContentsDetailVo> tmpLevel2List = newlevel1List.get(i).getContentsLowerList();
                            if (tmpLevel2List != null && tmpLevel2List.size() > 0) {
                                for (int j = 0; j < tmpLevel2List.size(); j++) {
                                    newlevel1List.get(i).getTmpContentsLowerList().add((ContentsDetailVo)tmpLevel2List.get(j).clone());
                                }
                            }
                        }
                        ContentsDetailVo orgaFlyerAll = new ContentsDetailVo();
                        orgaFlyerAll.setTitleName(DisplayConstants.PAGE_CODE_ORGA_FLYER_ALL_TITLE);
                        orgaFlyerAll.setSort(0);
                        orgaFlyerAll.setContentsType(newlevel1List.get(0).getContentsType());
                        orgaFlyerAll.setDpRangeTypeName(newlevel1List.get(0).getDpRangeTypeName());
                        orgaFlyerAll.setDpContsId(Long.valueOf("1"));
                        orgaFlyerAll.setDpStartDate(newlevel1List.get(0).getDpStartDate());
                        orgaFlyerAll.setDpEndDate(newlevel1List.get(0).getDpEndDate());

                        int index = 0;
                        List<ContentsDetailVo> goodsAllList = new ArrayList<ContentsDetailVo>();
                        for (int i = index; i < totalGoodsCnt; i++) {
                            log.info(">>> setOrgaFlyerList : " + i + " / " + index + " / " + goodsAllList.size());
                            if(goodsAllList.size() == totalGoodsCnt) { break; }
                            for (int j = 0; j < newlevel1List.size(); j++) {
                                List<ContentsDetailVo> level2List = newlevel1List.get(j).getTmpContentsLowerList();
                                if (level2List != null && level2List.size() > 0) {
                                    for (int k = 0; k < level2List.size(); k++) {
                                        goodsAllList.add(level2List.get(k));
                                        level2List.remove(level2List.get(k));
                                        index++;
                                        break;
                                    }
                                }
                            }
                        }

                        orgaFlyerAll.setContentsLowerList(goodsAllList);
                        log.info(">>> goodsAllList : " + goodsAllList);
                        level1List.add(0, orgaFlyerAll);
                        inventoryInfoVo.setContentsLevel1List(level1List);
                    }
                }
            }
        }catch(Exception e){
            log.warn("", e);
        }
    }

    /**
     * 올가 매장 전용관 배너정보 조회
     *
     * @param deviceType String
     * @return List<ContentsDetailBannerResponseDto>
     * @throws Exception Exception
     */
    protected List<ContentsDetailBannerResponseDto> getOrgaStoreBanner(String deviceType) throws Exception {
        // 컨텐츠 정보 조회
        List<ContentsDetailVo> contentsList = displayContentsMapper.getContentsDetailByInventoryCd(DisplayConstants.ORGA_STORE_INVENTORY_CODE, deviceType);
        if (contentsList == null || contentsList.size() == 0) return new ArrayList<>();

        // 화면 노출용 데이터로 변환
        // level1 설정
        return (List<ContentsDetailBannerResponseDto>) contentsDetailVoToResponseDto(contentsList, deviceType);
    }

    /**
     * 카테고리 컨텐츠 정보 조회 by 인벤토리 코드
     *
     * @param inventoryCd String
     * @param deviceType  String
     * @param userType    String
     * @return List<InventoryCategoryResponseDto>
     * @throws Exception Exception
     */
    protected List<InventoryCategoryResponseDto> getCategoryInfo(String inventoryCd, String deviceType, String userType) throws Exception {
        List<InventoryCategoryResponseDto> result = new ArrayList<>();

        //inventory 조회
        InventoryInfoVo inventoryInfoVo = displayContentsMapper.getInventoryInfo(inventoryCd, deviceType);

        if (inventoryInfoVo == null) return result;

        // LV1 배너조회
        Page<ContentsDetailVo> level1Rows = getContentsDetail(inventoryInfoVo.getDpInventoryId(), deviceType, DisplayConstants.CONTENTS_LEVEL1);
        if (level1Rows.getResult().size() > 0 && DisplayEnums.ContentsType.BANNER.getCode().equals(level1Rows.getResult().get(0).getContentsType())) {
            Map<String, List<ContentsDetailBannerResponseDto>> maps = new HashMap<>();
            List<ContentsDetailBannerResponseDto> level1 = (List<ContentsDetailBannerResponseDto>) contentsDetailVoToResponseDto(level1Rows.getResult(), deviceType);
            for (ContentsDetailBannerResponseDto dto : level1) {
                List<ContentsDetailBannerResponseDto> list = maps.getOrDefault(dto.getIlCtgryId(), new ArrayList<>());
                list.add(dto);
                maps.put(dto.getIlCtgryId(), list);
            }

            for (String key : maps.keySet()) {
                result.add(InventoryCategoryResponseDto.builder()
                        .ilCtgryId(key)
                        .level1(maps.get(key))
                        .build());
            }
        }

        return result;
    }

    /**
     * 컨텐츠 LV1 조회 by 인벤토리 코드
     *
     * @param inventoryCd String
     * @param deviceType  String
     * @return List<ContentsDetailVo>
     * @throws Exception Exception
     */
    protected List<ContentsDetailVo> getContentsLevel1ByInventoryCd(String inventoryCd, String deviceType) throws Exception {
        //inventory 조회
        Long inventoryId = displayContentsMapper.getInventoryId(inventoryCd, deviceType);

        // LV1 조회
        Page<ContentsDetailVo> level1Rows = getContentsDetail(inventoryId, deviceType, DisplayConstants.CONTENTS_LEVEL1);

        return level1Rows.getResult();
    }

    /**
     * 컨텐츠 상세 조회
     *
     * @param dpContsId     Long
     * @param deviceType    String
     * @param userType      String
     * @param goodsSortCode String
     * @return GetContentsInfoResponseDto
     * @throws Exception Exception
     */
    protected ContentsInfoResponseDto getContentsInfo(Long dpContsId, String deviceType, String userType, String goodsSortCode) throws Exception {
        ContentsInfoResponseDto result = new ContentsInfoResponseDto();
        // level2 조회
        Page<ContentsDetailVo> level2Rows = getContentsDetail(dpContsId, deviceType, DisplayConstants.CONTENTS_LEVEL2);
        List<ContentsDetailVo> level2RowsList = level2Rows.getResult();
        if (level2RowsList.size() > 0) {
            // level2 결과 셋팅
            if (level2RowsList.get(0).getContentsType() != null && DisplayEnums.ContentsType.GOODS.getCode().equals(level2RowsList.get(0).getContentsType())) {
                // 상품일 경우 상품정보 조회
                level2RowsList = searchGoodsInfo(level2RowsList, deviceType, userType, goodsSortCode, null);
                List<?> contentList = contentsDetailVoToResponseDto(level2RowsList, deviceType);
                result.setLevel2TotalCount(contentList.size());
                result.setLevel2(contentList);
            } else {
                result.setLevel2TotalCount((int) level2Rows.getTotal());
                result.setLevel2(contentsDetailVoToResponseDto(level2Rows.getResult(), deviceType));
            }
        }

        if (level2Rows.getTotal() > 0) {
            result.setLevel2ContentsType(level2Rows.getResult().get(0).getContentsType());

            // level3 조회
            Page<ContentsDetailVo> level3Rows = getContentsDetail(level2Rows.getResult().get(0).getDpContsId(), deviceType, DisplayConstants.CONTENTS_LEVEL3);
            List<ContentsDetailVo> level3RowsList = level3Rows.getResult();
            // level3 결과 셋팅
            if (level3RowsList.size() > 0 && level3RowsList.get(0).getContentsType() != null && DisplayEnums.ContentsType.GOODS.getCode().equals(level3RowsList.get(0).getContentsType())) {
                // 상품일 경우 상품정보 조회
                level3RowsList = searchGoodsInfo(level3RowsList, deviceType, userType, goodsSortCode, null);
                List<?> contentList = contentsDetailVoToResponseDto(level3RowsList, deviceType);
                result.setLevel3TotalCount(contentList.size());
                result.setLevel3(contentList);
            } else {
                result.setLevel3TotalCount((int) level3Rows.getTotal());
                result.setLevel3(contentsDetailVoToResponseDto(level3RowsList, deviceType));
            }

            if (level3Rows.getTotal() > 0) {
                result.setLevel3ContentsType(level3Rows.getResult().get(0).getContentsType());
            }
        }

        return result;
    }

    protected Page<ContentsDetailVo> getContentsDetail(Long dpContsId, String deviceType, String contentsLevel) throws Exception {
        PageMethod.startPage(0, 999);
        return displayContentsMapper.getContentsDetail(dpContsId, deviceType, contentsLevel);
    }

    protected List<ContentsDetailVo> searchGoodsInfo(List<ContentsDetailVo> list, String deviceType, String userType, String goodsSortCode, String pageCd) throws Exception {
        if (list == null) return null;
        if (list.size() == 0) return list;
        List<ContentsDetailVo> responseDtos = new ArrayList<>();

        //상품
        if (list.get(0).getContentsType().equals(DisplayEnums.ContentsType.GOODS.getCode())) {

            Map<Long, ContentsDetailVo> listMaps = new HashMap<>();
            List<Long> goodsList = new ArrayList<>();
            list.forEach(vo -> {
                goodsList.add(vo.getContentsId());
                listMaps.put(vo.getContentsId(), vo);
            });

            // 정렬값 DEFAULT 의 경우 BOS 전시 정렬 기준 에 따라 설정
            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto;
            if (goodsSortCode.equals(DisplayEnums.GoodsSortCode.DEFAULT.getCode())) {
                goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                        .isPurchaseSearch(true)
                        .goodsIdList(goodsList)
                        .build();
            } else {
                goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                        .isPurchaseSearch(true)
                        .goodsIdList(goodsList)
                        .goodsSortCode(goodsSortCode)
                        .build();
            }

            // 올가메인 매장상품 제외
            if (StringUtil.isNotEmpty(pageCd) && DisplayConstants.PAGE_CODE_ORGA_MAIN.equals(pageCd)) {
                goodsSearchByGoodsIdReqDto.setExceptShopOnly(true);
                List<String> orgaDpBrandIdList = Arrays.stream(policyConfigBiz.getConfigValue(Constants.ORGA_DP_BRAND_KEY).split(",")).collect(Collectors.toList());
                goodsSearchByGoodsIdReqDto.setDpBrandIdList(orgaDpBrandIdList);
            }

            // '상품 통합검색'
            List<GoodsSearchResultDto> searchResultLit = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);

            // 조회 결과 값이 있는 경우
            if (searchResultLit.size() > 0) {
                for (GoodsSearchResultDto goodsResult : searchResultLit) {
                    if (isFalseSearchType(deviceType, userType, goodsResult)) continue;

                    ContentsDetailVo responseDto = listMaps.get(goodsResult.getGoodsId());
                    responseDto.setGoodsInfo(goodsResult);

                    responseDtos.add(responseDto);
                }
            }

            return responseDtos;
        }

        return list;
    }

    protected List<?> contentsDetailVoToResponseDto(List<ContentsDetailVo> list, String deviceType) throws Exception {
        if (list == null) return null;
        if (list.size() == 0) return list;

        // TEXT LV2 포함
        if (list.get(0).getContentsType().equals(DisplayEnums.ContentsType.TEXT.getCode())) {
            List<ContentsDetailTextResponseDto> responseDtos = new ArrayList<>();
            for (ContentsDetailVo vo : list) {
                ContentsDetailTextResponseDto result = new ContentsDetailTextResponseDto(vo);

                // level2 설정
                List<ContentsDetailVo> level2List = vo.getContentsLowerList();
                if (level2List != null && level2List.size() > 0) {
                    List<?> mappingLv2Result = contentsDetailVoToResponseDto(level2List, deviceType);
                    result.setLevel2TotalCount(mappingLv2Result.size());
                    result.setLevel2(mappingLv2Result);
                    result.setLevel2ContentsType(level2List.get(0).getContentsType());
                }

                responseDtos.add(result);
            }
            return responseDtos;
        }

        if (list.get(0).getContentsType().equals(DisplayEnums.ContentsType.HTML.getCode())) {
            return list.stream()
                    .map(ContentsDetailHtmlResponseDto::new)
                    .collect(Collectors.toList());
        }

        // Banner 의 경우 Lv2, 3 정보 포함
        if (list.get(0).getContentsType().equals(DisplayEnums.ContentsType.BANNER.getCode())) {
            List<ContentsDetailBannerResponseDto> responseDtos = new ArrayList<>();

            // 예외처리 : 메인 빌보드 이미지 여부 확인
            if (GoodsEnums.DeviceType.PC.getCode().equals(deviceType)) {
                list = list.stream()
                        .filter(vo -> !vo.getInventoryCode().equals(DisplayConstants.MAIN_BILLBOARD) || !vo.getImagePathPc().equals(""))
                        .collect(Collectors.toList());
            } else {
                list = list.stream()
                        .filter(vo -> !vo.getInventoryCode().equals(DisplayConstants.MAIN_BILLBOARD) || !vo.getImagePathMobile().equals(""))
                        .collect(Collectors.toList());
            }

            for (ContentsDetailVo vo : list) {
                ContentsDetailBannerResponseDto result = new ContentsDetailBannerResponseDto(vo);

                if (DisplayConstants.CONTENTS_LEVEL1.equals(vo.getContentsLevel())) {
                    // level2 설정
                    List<ContentsDetailVo> level2List = vo.getContentsLowerList();
                    if (level2List != null && level2List.size() > 0) {
                        List<?> mappingLv2Result = contentsDetailVoToResponseDto(level2List, deviceType);
                        result.setLevel2TotalCount(mappingLv2Result.size());
                        result.setLevel2(mappingLv2Result);
                        result.setLevel2ContentsType(level2List.get(0).getContentsType());

                        // level3 설정
                        List<ContentsDetailVo> level3List = level2List.get(0).getContentsLowerList();
                        if (level3List != null && level3List.size() > 0) {
                            List<?> mappingLv3Result = contentsDetailVoToResponseDto(level3List, deviceType);
                            result.setLevel3TotalCount(mappingLv3Result.size());
                            result.setLevel3(mappingLv3Result);
                            result.setLevel3ContentsType(level3List.get(0).getContentsType());
                        }
                    }
                }
                if (DisplayConstants.CONTENTS_LEVEL2.equals(vo.getContentsLevel())) {
                    // level3 설정
                    List<ContentsDetailVo> level3List = vo.getContentsLowerList();
                    if (level3List != null && level3List.size() > 0) {
                        List<?> mappingLv3Result = contentsDetailVoToResponseDto(level3List, deviceType);
                        result.setLevel3TotalCount(mappingLv3Result.size());
                        result.setLevel3(mappingLv3Result);
                        result.setLevel3ContentsType(level3List.get(0).getContentsType());
                    }
                }

                responseDtos.add(result);
            }

            return responseDtos;
        }

        // CATEGORY 의 경우 Lv2 정보 포함
        if (list.get(0).getContentsType().equals(DisplayEnums.ContentsType.CATEGORY.getCode())) {
            List<ContentsDetailCategoryResponseDto> responseDtos = new ArrayList<>();
            for (ContentsDetailVo vo : list) {
                ContentsDetailCategoryResponseDto result = new ContentsDetailCategoryResponseDto(vo);

                // level2 설정
                List<ContentsDetailVo> level2List = vo.getContentsLowerList();
                if (level2List != null && level2List.size() > 0) {
                    List<?> mappingLv2Result = contentsDetailVoToResponseDto(level2List, deviceType);
                    result.setLevel2TotalCount(mappingLv2Result.size());
                    result.setLevel2(mappingLv2Result);
                    result.setLevel2ContentsType(level2List.get(0).getContentsType());
                }

                responseDtos.add(result);
            }

            return responseDtos;
        }

        if (list.get(0).getContentsType().equals(DisplayEnums.ContentsType.BRAND.getCode())) {
            if (list.get(0).getInventoryCode().equals(DisplayConstants.BRAND_INVENTORY_CODE)) {
                // 메인 - 브랜드 리스트
                // LV1 - 브랜드
                // LV2 - 상품
                List<ContentsDetailBrandResponseDto> responseDtos = new ArrayList<>();
                for (ContentsDetailVo vo : list) {
                    if ("N".equals(vo.getDpBrandUseYn())) continue;
                    ContentsDetailBrandResponseDto result = new ContentsDetailBrandResponseDto(vo);

                    // level2 설정
                    if (vo.getContentsLowerList() != null && vo.getContentsLowerList().size() > 0) {
                        List<?> mappingResult = contentsDetailVoToResponseDto(vo.getContentsLowerList(), deviceType);
                        result.setLevel2TotalCount(mappingResult.size());
                        result.setLevel2(mappingResult);
                        result.setLevel2ContentsType(vo.getContentsLowerList().get(0).getContentsType());
                    }
                    responseDtos.add(result);
                }
                return responseDtos;
            }

            return list.stream()
                    .filter(vo -> vo.getDpBrandUseYn().equals("Y"))
                    .map(ContentsDetailBrandResponseDto::new)
                    .collect(Collectors.toList());
        }

        //상품
        if (list.get(0).getContentsType().equals(DisplayEnums.ContentsType.GOODS.getCode())) {
            String startDate = "";
            String endDate = "";
            if (list.get(0).getInventoryCode().equals(DisplayConstants.NOW_SALE_CODE)) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime flagTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0));
                LocalDateTime localDateTime = LocalDateTime.now();
                if (localDateTime.isBefore(flagTime)) {
                    startDate = flagTime.minusDays(1).format(dateTimeFormatter);
                } else {
                    startDate = flagTime.format(dateTimeFormatter);
                }
                endDate = localDateTime.format(dateTimeFormatter);
            }

            List<ContentsDetailGoodsResponseDto> responseDtos = new ArrayList<>();
            for (ContentsDetailVo vo : list) {
                ContentsDetailGoodsResponseDto responseDto = new ContentsDetailGoodsResponseDto(vo);
                responseDto.setGoodsInfo(vo.getGoodsInfo());

                //구매개수 조회
                if (list.get(0).getInventoryCode().equals(DisplayConstants.NOW_SALE_CODE)) {
                    responseDto.setOrderCount(0);
                    GetCodeListResultVo codeResult = systemCodeBiz.getCode(DisplayConstants.NOW_SALE_ORDER_CNT_HIDDEN);
                    if(codeResult == null) {
                        responseDto.setOrderCount(orderFrontBiz.getOrderInfoFromMain(vo.getContentsId(), startDate, endDate));
                    }
                }
                responseDtos.add(responseDto);
            }

            return responseDtos;
        }

        return list;
    }

    private boolean isFalseSearchType(String deviceType, String userType, GoodsSearchResultDto goodsSearchResultDto) {
        if (deviceType.equals(DisplayEnums.DeviceType.PC.getCode()) && goodsSearchResultDto.getDisplayPcYn().equals("N")) {
            return true;
        }
        if (deviceType.equals(DisplayEnums.DeviceType.MOBILE.getCode()) && goodsSearchResultDto.getDisplayMobileYn().equals("N")) {
            return true;
        }
        if (deviceType.equals(DisplayEnums.DeviceType.APP.getCode()) && goodsSearchResultDto.getDisplayAppYn().equals("N")) {
            return true;
        }
        // 비회원 상품 노출처리
//        if (userType.equals(DisplayEnums.UserType.NONE.getCode()) && goodsSearchResultDto.getPurchaseNonMemberYn().equals("N")) {
//            return true;
//        }
        if (userType.equals(DisplayEnums.UserType.NORMAL.getCode()) && goodsSearchResultDto.getPurchaseMemberYn().equals("N")) {
            return true;
        }
        return userType.equals(DisplayEnums.UserType.EMPLOYEE.getCode()) && goodsSearchResultDto.getPurchaseEmployeeYn().equals("N");
    }

    protected LohasBannerResponseDto getLohasBanner(LohasBannerRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        Page<ContentsDetailVo> result = displayContentsMapper.getLohasBanner(dto);

        List<ContentsDetailBannerResponseDto> list = result.getResult().stream()
                .map(ContentsDetailBannerResponseDto::new)
                .collect(Collectors.toList());

        // 예외처리 : 이미지 여부 확인
        if (GoodsEnums.DeviceType.PC.getCode().equals(dto.getDeviceType())) {
            list = list.stream()
                    .filter(vo -> !vo.getImagePathPc().equals(""))
                    .collect(Collectors.toList());
        } else {
            list = list.stream()
                    .filter(vo -> !vo.getImagePathMobile().equals(""))
                    .collect(Collectors.toList());
        }

        return LohasBannerResponseDto.builder()
                .total((int) result.getTotal())
                .banner(list)
                .build();
    }

    boolean isDealGoods(Long ilGoodsId) throws Exception {
        return displayContentsMapper.isDealGoods(GoodsConstants.DEAL_GOODS_DISPLAY_INVENTORY_CODE, ilGoodsId);
    }

    protected List<GoodsSearchResultDto> getBestGoods(BestGoodsRequestDto dto) throws Exception {
        GoodsRankingRequestDto requestDto = GoodsRankingRequestDto.builder()
                .mallDiv(dto.getMallDiv())
                .categoryIdDepth1(dto.getIlCtgryId())
                .total(dto.getTotal())
                .build();

        // 인기상품 올가 - 매장상품 제외
        if (GoodsEnums.MallDiv.ORGA.getCode().equals(dto.getMallDiv())) {
            requestDto.setExceptShopOnly(true);
            List<String> orgaDpBrandIdList = Arrays.stream(policyConfigBiz.getConfigValue(Constants.ORGA_DP_BRAND_KEY).split(",")).collect(Collectors.toList());
            requestDto.setDpBrandIdList(orgaDpBrandIdList);
        }

        List<Long> goodsIdList = goodsEtcBiz.getGoodsRankingByCategoryIdDepth1(requestDto);

        List<GoodsSearchResultDto> result = new ArrayList<>();
        if (goodsIdList.size() > 0) {
            // 상품 ID로 상품검색
            GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                    .deviceInfo(dto.getDeviceType())
                    .isMember(dto.getUserType() != null && (dto.getUserType().equals(DisplayEnums.UserType.NORMAL.getCode()) || dto.getUserType().equals(DisplayEnums.UserType.EMPLOYEE.getCode())))
                    .isEmployee(dto.getUserType() != null && (dto.getUserType().equals(DisplayEnums.UserType.EMPLOYEE.getCode())))
                    .goodsIdList(goodsIdList)
                    .isPurchaseSearch(true)
                    .build();

            result = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);
        }

        return result;
    }
}