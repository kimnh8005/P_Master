package kr.co.pulmuone.v1.display.manage.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.display.manage.dto.DisplayContsResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayInventoryResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayManageRequestDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayPageResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.vo.ContsVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.InventoryVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class DisplayManageServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private DisplayManageService displayManageService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    // ##########################################################################
    // 페이지
    // ##########################################################################

    @Test
    public void test_전시페이지리스트조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        String dpPageId = "0";     // 페이지ID
        String useAllYn = "Y";    // 전체여부

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayPageResponseDto result = displayManageService.selectDpPageList(dpPageId, useAllYn);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 전시카테고리리스트조회
     *
     * @throws BaseException
     */
    @Test
    public void test_전시카테고리리스트조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        DisplayManageRequestDto displayManageRequestDto = new DisplayManageRequestDto();
        displayManageRequestDto.setPageTp("PAGE_TP.CATEGORY");
        displayManageRequestDto.setDpPageId("55");
        displayManageRequestDto.setUseAllYn("Y");
        displayManageRequestDto.setMallDiv("MALL_DIV.PULMUONE");
        displayManageRequestDto.setContsListYn("Y");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayPageResponseDto result = displayManageService.selectDpCategoryList(displayManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 페이지 수정
     *
     * @throws BaseException
     */
    @Test
    public void test_페이지수정() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        PageVo pageVo = new PageVo();
        pageVo.setDpPageId("5075");
        pageVo.setPageCd("JUNIT-" + DateUtil.getCurrentDate("yyyyMMddHHmmdd"));
        pageVo.setPageNm("JUNIT-페이지명-001");
        pageVo.setUseYn("Y");
        pageVo.setCreateId("9999999991");
        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayPageResponseDto result = displayManageService.putPage(pageVo);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 페이지 삭제
     *
     * @throws BaseException
     */
    @Test
    public void test_페이지삭제() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        PageVo pageVo = new PageVo();
        pageVo.setDpPageId("5075");
        pageVo.setCreateId("9999999991");

        List<PageVo> pageVoList = new ArrayList<>();
        pageVoList.add(pageVo);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayPageResponseDto result = displayManageService.delPage(pageVoList);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 페이지 등록
     *
     * @throws BaseException
     */
    @Test
    public void test_페이지등록() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        PageVo pageVo = new PageVo();
        pageVo.setPageCd("JUNIT-" + DateUtil.getCurrentDate("yyyyMMddHHmmdd"));
        pageVo.setPageNm(pageVo.getPageCd() + "명");
        pageVo.setDepth(2);
        pageVo.setPrntsPageId("0");
        pageVo.setSort(991);
        pageVo.setUseYn("Y");
        pageVo.setCreateId("9999999991");
        pageVo.setModifyId("9999999991");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayPageResponseDto result = displayManageService.addPage(pageVo);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    // ##########################################################################
    // 인벤토리
    // ##########################################################################

    /**
     * 인벤토리리스트조회
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리리스트조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        DisplayManageRequestDto displayManageRequestDto = new DisplayManageRequestDto();
        displayManageRequestDto.setPageTp("PAGE_TP.PAGE");
        displayManageRequestDto.setDpPageId("4868");
        displayManageRequestDto.setMallDiv("MALL_DIV.PULMUONE");
        //displayManageRequestDto.setDepth(depth);
        displayManageRequestDto.setUseYn("Y");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayInventoryResponseDto result = displayManageService.selectInventoryList(displayManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리수정
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리수정() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        InventoryVo inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryId("4918");
        inventoryVo.setInventoryNm("인벤토리명수정-" + DateUtil.getCurrentDate("yyyyMMddHHmmdd"));
        inventoryVo.setDpRangeTp("DP_RANGE_TP.ALL");
        inventoryVo.setSort(998);
        inventoryVo.setUseYn("Y");
        inventoryVo.setModifyId("9999999991");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayInventoryResponseDto result = displayManageService.putInventory(inventoryVo);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리순서변경
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리순서변경() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        List<InventoryVo> inventoryList = new ArrayList<InventoryVo>();

        InventoryVo unitInventoryVo = new InventoryVo();
        unitInventoryVo.setDpInventoryId("4896");
        unitInventoryVo.setSort(1);
        inventoryList.add(unitInventoryVo);
        unitInventoryVo = new InventoryVo();
        unitInventoryVo.setDpInventoryId("4904");
        unitInventoryVo.setSort(2);
        inventoryList.add(unitInventoryVo);
        unitInventoryVo = new InventoryVo();
        unitInventoryVo.setDpInventoryId("4895");
        unitInventoryVo.setSort(3);
        inventoryList.add(unitInventoryVo);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayInventoryResponseDto result = displayManageService.putInventorySort(inventoryList);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리삭제
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리삭제() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        InventoryVo inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryId("4916");
        inventoryVo.setDelYn("Y");

        List<InventoryVo> inventoryVoList = new ArrayList<InventoryVo>();
        inventoryVoList.add(inventoryVo);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayInventoryResponseDto result = displayManageService.delInventory(inventoryVoList);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리등록
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리등록() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        InventoryVo inventoryVo = new InventoryVo();
        inventoryVo.setPageTp("PAGE_TP.PAGE");
        inventoryVo.setInventoryCd("JUNIT-INV-" + DateUtil.getCurrentDate("yyyyMMddHHmmdd"));
        inventoryVo.setInventoryNm(inventoryVo.getInventoryCd() + "명");
        inventoryVo.setDpPageId("4917");
        inventoryVo.setMallDiv("MALL_DIV.PULMUONE");
        inventoryVo.setCtgryDepth(0);
        inventoryVo.setDpRangeTp("DP_RANGE_TP.ALL");
        inventoryVo.setContsLevel1Tp("HTML");
        inventoryVo.setContsLevel1Desc("Dep1 설명");
        inventoryVo.setSort(99998);
        inventoryVo.setUseYn("Y");
        inventoryVo.setCreateId("9999999991");
        inventoryVo.setModifyId("9999999991");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayInventoryResponseDto result = displayManageService.addInventory(inventoryVo);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    // ##########################################################################
    // 콘텐츠
    // ##########################################################################

    /**
     * 콘텐츠리스트조회
     *
     * @throws BaseException
     */
    @Test
    public void test_콘텐츠리스트조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        DisplayManageRequestDto displayManageRequestDto = new DisplayManageRequestDto();
        displayManageRequestDto.setDpInventoryId("4848");
        displayManageRequestDto.setPrntsContsId("0");
        displayManageRequestDto.setContsLevel(1);
        displayManageRequestDto.setDpRangeTp("DP_RANGE_TP.ALL");
        displayManageRequestDto.setStatus("");
        displayManageRequestDto.setDpStartDt("");
        displayManageRequestDto.setDpEndDt("");
        displayManageRequestDto.setDpCondTp("");
        displayManageRequestDto.setDpSortTp("");
        displayManageRequestDto.setIlCtgryId("");
        displayManageRequestDto.setHostUrl("https://dev0shop.pulmuone.online");
        displayManageRequestDto.setGoodsDetailUrl("/shop/goodsView?item=");
        displayManageRequestDto.setUseYn("");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayContsResponseDto result = displayManageService.selectDpContsList(displayManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 콘텐츠리스트상세조회
     *
     * @throws BaseException
     */
    @Test
    public void test_콘텐츠상세조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        DisplayManageRequestDto displayManageRequestDto = new DisplayManageRequestDto();
        displayManageRequestDto.setDpContsId("4884");
        displayManageRequestDto.setHostUrl("https://dev0shop.pulmuone.online");
        displayManageRequestDto.setGoodsDetailUrl("/shop/goodsView?item=");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayContsResponseDto result = displayManageService.selectDpContsInfo(displayManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 콘텐츠수정
     *
     * @throws BaseException
     */
    @Test
    public void test_콘텐츠수정() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ContsVo contsVo = new ContsVo();
        contsVo.setDpContsId("4884");
        contsVo.setDpInventoryId("4851");
        contsVo.setContsTp("DP_CONTENTS_TP.GOODS");
        contsVo.setContsLevel(2);
        contsVo.setPrntsContsId("4880");
        contsVo.setLevel1ContsId("4880");
        contsVo.setLevel2ContsId("4884");
        contsVo.setDpStartDt("2020-11-17 00:00:00.0");
        contsVo.setDpEndDt("2020-12-31 23:59:59.0");
        contsVo.setDpRangeTp("DP_RANGE_TP.ALL");
        contsVo.setTitleNm("단호박 스낵");
        contsVo.setText1("튀기지 않고 구웠다");
        contsVo.setContsId("15364");
        contsVo.setDpCondTp("DP_COND_TP.MANUAL");
        contsVo.setDpSortTp("DP_SORT_TP.MANUAL");
        contsVo.setSort(99);
        contsVo.setUseYn("Y");
        contsVo.setDelYn("N");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayContsResponseDto result = displayManageService.putConts(contsVo);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 콘텐츠순서변경
     *
     * @throws BaseException
     */
    @Test
    public void test_콘텐츠순서변경() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        List<ContsVo> contsList = new ArrayList<ContsVo>();

        ContsVo unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("4871");
        unitContsVo.setSort(1);
        contsList.add(unitContsVo);
        unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("4872");
        unitContsVo.setSort(2);
        contsList.add(unitContsVo);
        unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("4874");
        unitContsVo.setSort(3);
        contsList.add(unitContsVo);
        unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("5044");
        unitContsVo.setSort(4);
        contsList.add(unitContsVo);
        unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("5049");
        unitContsVo.setSort(5);
        contsList.add(unitContsVo);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayContsResponseDto result = displayManageService.putContsSort(contsList);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 콘텐츠삭제
     *
     * @throws BaseException
     */
    @Test
    public void test_콘텐츠삭제() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        List<ContsVo> contsList = new ArrayList<ContsVo>();

        ContsVo unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("4871");
        unitContsVo.setDelYn("Y");
        contsList.add(unitContsVo);
        unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("4872");
        unitContsVo.setDelYn("Y");
        contsList.add(unitContsVo);
        unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("4874");
        unitContsVo.setDelYn("Y");
        contsList.add(unitContsVo);
        unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("5044");
        unitContsVo.setDelYn("Y");
        contsList.add(unitContsVo);
        unitContsVo = new ContsVo();
        unitContsVo.setDpContsId("5049");
        unitContsVo.setDelYn("Y");
        contsList.add(unitContsVo);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayContsResponseDto result = displayManageService.delConts(contsList);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 상품콘텐츠 중복체크
     *
     * @throws BaseException
     */
    @Test
    public void test_상품콘텐츠_중복체크() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ContsVo contsVo = new ContsVo();
        contsVo.setDpInventoryId("4851");
        contsVo.setContsTp("DP_CONTENTS_TP.GOODS");
        contsVo.setContsLevel(2);
        contsVo.setPrntsContsId("4880");
        contsVo.setContsId("15364");
        contsVo.setDpStartDt("2020-11-17 00:00:00.0");
        contsVo.setDpEndDt("2020-12-31 23:59:59.0");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        List<ContsVo> result = displayManageService.selectDpGoodsContsDupList(contsVo);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.size() <= 0);   // 조회건이 없으면 성공
    }

    /**
     * 콘텐츠등록
     *
     * @throws BaseException
     */
    @Test
    public void test_콘텐츠등록() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ContsVo contsVo = new ContsVo();
        contsVo.setDpInventoryId("4851");
        contsVo.setContsTp("DP_CONTENTS_TP.GOODS");
        contsVo.setContsLevel(2);
        contsVo.setPrntsContsId("4880");
        contsVo.setLevel1ContsId("4880");
        contsVo.setLevel2ContsId("4884");
        contsVo.setDpStartDt("2020-11-17 00:00:00.0");
        contsVo.setDpEndDt("2020-12-31 23:59:59.0");
        contsVo.setDpRangeTp("DP_RANGE_TP.ALL");
        contsVo.setTitleNm("단호박 스낵");
        contsVo.setText1("튀기지 않고 구웠다");
        contsVo.setContsId("15364");
        contsVo.setDpCondTp("DP_COND_TP.MANUAL");
        contsVo.setDpSortTp("DP_SORT_TP.MANUAL");
        contsVo.setSort(99);
        contsVo.setUseYn("Y");
        contsVo.setDelYn("N");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayContsResponseDto result = displayManageService.addConts(contsVo);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }


    // ##########################################################################
    // 인벤토리그룹관리
    // ##########################################################################

    @Test
    public void test_그룹리스트조회() throws BaseException {
        log.info("# ######################################");
        log.info("# DisplayManageServiceTest.test_그룹리스트조회 Start");
        log.info("# ######################################");

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        DisplayManageRequestDto displayManageRequestDto = new DisplayManageRequestDto();
        displayManageRequestDto.setUseYn("Y");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayPageResponseDto result = displayManageService.selectDpInventoryGroupList(displayManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리그룹 인벤토리 리스트조회
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리그룹_인벤토리리스트조회() throws BaseException {
        log.info("# ######################################");
        log.info("# DisplayManageServiceTest.test_인벤토리그룹_인벤토리리스트조회 Start");
        log.info("# ######################################");

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        DisplayManageRequestDto displayManageRequestDto = new DisplayManageRequestDto();
        displayManageRequestDto.setDpInventoryGrpId("4820");
        displayManageRequestDto.setUseYn("Y");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayInventoryResponseDto result = displayManageService.selectDpGroupInventoryList(displayManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리그룹 구성 리스트조회
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리그룹_구성리스트조회() throws BaseException {
        log.info("# ######################################");
        log.info("# DisplayManageServiceTest.test_인벤토리그룹_구성리스트조회 Start");
        log.info("# ######################################");

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        DisplayManageRequestDto displayManageRequestDto = new DisplayManageRequestDto();
        displayManageRequestDto.setDpInventoryGrpId("4799");
        displayManageRequestDto.setUseYn("Y");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayInventoryResponseDto result = displayManageService.selectDpGroupInventoryMappingList(displayManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리그룹 등록
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리그룹_등록() throws BaseException {
        log.info("# ######################################");
        log.info("# DisplayManageServiceTest.test_인벤토리그룹_등록 Start");
        log.info("# ######################################");

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        InventoryVo inventoryVo = new InventoryVo();
        inventoryVo.setInventoryGrpNm("인벤토리그룹명");
        inventoryVo.setUseYn("Y");
        inventoryVo.setSort(10);
        inventoryVo.setGroupDesc("상세입니다.");
        inventoryVo.setInventoryCdsString("21-Main-Billboard-Bn");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayPageResponseDto result = displayManageService.addInventoryGroup(inventoryVo);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리그룹 수정
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리그룹_수정() throws BaseException {
        log.info("# ######################################");
        log.info("# DisplayManageServiceTest.test_인벤토리그룹_수정 Start");
        log.info("# ######################################");

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        InventoryVo inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryGrpId("4817");
        inventoryVo.setInventoryGrpNm("인벤토리그룹명2");
        inventoryVo.setUseYn("Y");
        inventoryVo.setSort(10);
        inventoryVo.setGroupDesc("상세입니다.2");
        inventoryVo.setInventoryCdsString("21-Main-Billboard-Bn");


        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayPageResponseDto result = displayManageService.addInventoryGroup(inventoryVo);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리그룹 삭제
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리그룹_삭제() throws BaseException {
        log.info("# ######################################");
        log.info("# DisplayManageServiceTest.test_인벤토리그룹_삭제 Start");
        log.info("# ######################################");

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        List<InventoryVo> inventoryVoList = new ArrayList<InventoryVo>();

        InventoryVo inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryGrpId("4815");
        inventoryVo.setDelYn("Y");
        inventoryVoList.add(inventoryVo);
        inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryGrpId("4819");
        inventoryVo.setDelYn("Y");
        inventoryVoList.add(inventoryVo);
        inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryGrpId("4820");
        inventoryVo.setDelYn("Y");
        inventoryVoList.add(inventoryVo);
        inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryGrpId("4821");
        inventoryVo.setDelYn("Y");
        inventoryVoList.add(inventoryVo);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayInventoryResponseDto result = displayManageService.delInventoryGroup(inventoryVoList);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 인벤토리그룹 순서변경
     *
     * @throws BaseException
     */
    @Test
    public void test_인벤토리그룹_순서변경() throws BaseException {
        log.info("# ######################################");
        log.info("# DisplayManageServiceTest.test_인벤토리그룹_순서변경 Start");
        log.info("# ######################################");

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        List<InventoryVo> inventoryVoList = new ArrayList<InventoryVo>();

        InventoryVo inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryGrpId("4815");
        inventoryVo.setSort(11);
        inventoryVoList.add(inventoryVo);
        inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryGrpId("4819");
        inventoryVo.setSort(12);
        inventoryVoList.add(inventoryVo);
        inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryGrpId("4820");
        inventoryVo.setSort(13);
        inventoryVoList.add(inventoryVo);
        inventoryVo = new InventoryVo();
        inventoryVo.setDpInventoryGrpId("4821");
        inventoryVo.setSort(14);
        inventoryVoList.add(inventoryVo);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        DisplayInventoryResponseDto result = displayManageService.putInventoryGroupSort(inventoryVoList);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }


}
