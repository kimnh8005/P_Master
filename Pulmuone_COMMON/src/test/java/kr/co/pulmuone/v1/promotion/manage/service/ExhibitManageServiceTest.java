package kr.co.pulmuone.v1.promotion.manage.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalExhibitRequestDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.promotion.manage.ExhibitManageMapper;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitApprovalResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.ExhibitManageResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EvUserGroupVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGroupDetlVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGroupVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitVo;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class ExhibitManageServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private ExhibitManageService exhibitManageService;

    @InjectMocks
    private ExhibitManageService mockExhibitManageService;

    @Mock
    private ExhibitManageMapper mockExhibitManageMapper;

    @BeforeEach
    void setUp() {
        preLogin();
        mockExhibitManageService = new ExhibitManageService(mockExhibitManageMapper);
    }

    /**
     * 기획전 리스트조회
     *
     * @throws BaseException
     */
    @Test
    public void test_기획전리스트조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setExhibitTp(ExhibitEnums.ExhibitTp.NORMAL.getCode());

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        Page<ExhibitVo> result = exhibitManageService.selectExhibitList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 기획전 담당자리스트(조회조건 콤보용)
     *
     * @throws BaseException
     */
    @Test
    public void test_기획전담당자리스트조회() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setExhibitTp(ExhibitEnums.ExhibitTp.GIFT.getCode());

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectExhibitManagerList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 기획전 상세조회 - 기본
     *
     * @throws BaseException
     */
    @Test
    public void test_기획전상세조회_기본() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("3");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectExhibitInfo(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 일반기획전 상세조회 - 그룹리스트
     *
     * @throws BaseException
     */
    @Test
    public void test_일반기획전상세조회_그룹리스트() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("3");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectfExhibitGroupList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 일반기획전 상세조회 - 그룹상품리스트
     *
     * @throws BaseException
     */
    @Test
    public void test_일반기획전상세조회_그룹상품리스트() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitGroupId("1");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectfExhibitGroupGoodsList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 골라담기 상세조회 - 기본
     *
     * @throws BaseException
     */
    @Test
    public void test_골라담기상세조회_기본() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("5");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectfExhibitSelectInfo(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 골라담기 상세조회 - 상품리스트
     *
     * @throws BaseException
     */
    @Test
    public void test_골라담기상세조회_상품리스트() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("5");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectExhibitSelectGoodsList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 골라담기 상세조회 - 추가상품리스트
     *
     * @throws BaseException
     */
    @Test
    public void test_골라담기상세조회_추가상품리스트() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("5");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectExhibitSelectAddGoodsList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    @Test
    public void test_증정행사상세조회_기본() throws BaseException {
        //given
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("34");

        //when
        ExhibitManageResponseDto result = exhibitManageService.selectExhibitGiftInfo(exhibitManageRequestDto);

        //then
        assertNotNull(result.getGiftDetail());
    }

    /**
     * 증정행사 상세조회 - 증정상품리스트
     *
     * @throws BaseException
     */
    @Test
    public void test_증정행사상세조회_증정상품리스트() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("8");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectExhibitGiftGoodsList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 증정행사 상세조회 - 적용상품리스트
     *
     * @throws BaseException
     */
    @Test
    public void test_증정행사상세조회_적용상품리스트() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("8");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectExhibitGiftTargetGoodsList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 증정행사 상세조회 - 적용브랜드리스트
     *
     * @throws BaseException
     */
    @Test
    public void test_증정행사상세조회_적용브랜드리스트() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("7");

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectExhibitGiftTargetBrandList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    /**
     * 상품정보리스트(엑셀용)
     *
     * @throws BaseException
     */
    @Test
    public void test_상품정보리스트() throws BaseException {

        // ------------------------------------------------------------------------
        // # Param
        // ------------------------------------------------------------------------
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        List<String> ilGoodsIdList = new ArrayList<>();
        ilGoodsIdList.add("10000241");
        ilGoodsIdList.add("15523");
        ilGoodsIdList.add("10000038");
        ilGoodsIdList.add("15522");
        ilGoodsIdList.add("15518");
        exhibitManageRequestDto.setIlGoodsIdList(ilGoodsIdList);

        // ------------------------------------------------------------------------
        // # Call
        // ------------------------------------------------------------------------
        ExhibitManageResponseDto result = exhibitManageService.selectGoodsInfoList(exhibitManageRequestDto);

        // ------------------------------------------------------------------------
        // # Result
        // ------------------------------------------------------------------------
        assertTrue(result.getTotal() > 0);
    }

    @Test
    public void test_기획전삭제() throws BaseException {
        //given
        List<String> exhibitList = new ArrayList<>();
        exhibitList.add("3");

        //when
        ExhibitManageResponseDto result = exhibitManageService.delExhibit(exhibitList);

        //then
        assertEquals(ExhibitEnums.ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode(), result.getResultCode());
    }

    @Test
    public void test_그룹상세삭제() throws BaseException {
        //given
        List<String> exhibitGroupDetlIdList = new ArrayList<>();
        exhibitGroupDetlIdList.add("172");

        //when
        ExhibitManageResponseDto result = exhibitManageService.delExhibitGroupDetl(exhibitGroupDetlIdList);

        //then
        assertEquals(ExhibitEnums.ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode(), result.getResultCode());
    }

    @Test
    public void test_골라담기상품삭제() throws BaseException {
        //given
        List<String> exhibitSelectGoodsIdList = new ArrayList<>();
        exhibitSelectGoodsIdList.add("60");

        //when
        ExhibitManageResponseDto result = exhibitManageService.delExhibitSelectGoods(exhibitSelectGoodsIdList);

        //then
        assertEquals(ExhibitEnums.ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode(), result.getResultCode());
    }

    @Test
    public void test_골라담기추가상품삭제() throws BaseException {
        //given
        List<String> exhibitSelectAddGoodsIdList = new ArrayList<>();
        exhibitSelectAddGoodsIdList.add("60");

        //when
        ExhibitManageResponseDto result = exhibitManageService.delExhibitSelectAddGoods(exhibitSelectAddGoodsIdList);

        //then
        assertEquals(ExhibitEnums.ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode(), result.getResultCode());
    }

    @Test
    public void test_증정행사상품삭제() throws BaseException {
        //given
        List<String> exhibitGiftGoodsList = new ArrayList<>();
        exhibitGiftGoodsList.add("30");

        //when
        ExhibitManageResponseDto result = exhibitManageService.delExhibitGiftGoods(exhibitGiftGoodsList);

        //then
        assertEquals(ExhibitEnums.ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode(), result.getResultCode());
    }

    @Test
    public void test_증정행사대상상품삭제() throws BaseException {
        //given
        List<String> exhibitGiftTargetGoodsList = new ArrayList<>();
        exhibitGiftTargetGoodsList.add("40");

        //when
        ExhibitManageResponseDto result = exhibitManageService.delExhibitGiftTargetGoods(exhibitGiftTargetGoodsList);

        //then
        assertEquals(ExhibitEnums.ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode(), result.getResultCode());
    }

    @Test
    public void test_증정행사대상브랜드삭제() throws BaseException {
        //given
        List<String> exhibitGiftTargetBrandList = new ArrayList<>();
        exhibitGiftTargetBrandList.add("38");

        //when
        ExhibitManageResponseDto result = exhibitManageService.delExhibitGiftTargetBrand(exhibitGiftTargetBrandList);

        //then
        assertEquals(ExhibitEnums.ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode(), result.getResultCode());
    }

    @Test
    public void test_기획전등록() throws BaseException {
        //given
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        // 기획전 기본정보
        exhibitManageRequestDto.setExhibitTp("EXHIBIT_TP.NORMAL");
        ExhibitVo exhibitVo = new ExhibitVo();
        exhibitVo.setExhibitTp("EXHIBIT_TP.NORMAL");
        exhibitVo.setMallDiv("MALL_DIV.PULMUONE");
        exhibitVo.setUseYn("Y");
        exhibitVo.setTitle("기획전제목");
        exhibitVo.setDescription("기획전설명");
        exhibitVo.setDispWebPcYn("Y");
        exhibitVo.setDispWebMobileYn("N");
        exhibitVo.setDispAppYn("Y");
        exhibitVo.setDispNonmemberYn("N");
        exhibitVo.setEvEmployeeTp("EV_EMPLOYEE_TP.NO_LIMIT");
        exhibitVo.setAlwaysYn("Y");
        exhibitVo.setStartDt("20201229000000");
        exhibitVo.setEndDt("29991231235959");
        exhibitVo.setTimeOverCloseYn("Y");
        exhibitVo.setBnrImgPath("BOS/dp/test/2020/12/29/635F4856C1B04A4CBB78.jpg");
        exhibitVo.setBnrImgOriginNm("AcadianDay_ROW6460737821_1920x1200.jpg");
        exhibitVo.setDetlHtmlPc("기획전상세-PC");
        exhibitVo.setDetlHtmlMo("기획전상세-Mobile");
        exhibitVo.setDispYn("N");
        exhibitManageRequestDto.setExhibitInfo(exhibitVo);

        // 기획전 상세정보 - 일반기획전
        ExhibitGroupVo exhibitGroupVo;
        List<ExhibitGroupVo> groupList;
        ExhibitGroupDetlVo exhibitGroupDetlVo;
        List<ExhibitGroupDetlVo> groupGoodsList;
        // 그룹1
        exhibitGroupVo = new ExhibitGroupVo();
        groupList = new ArrayList<>();
        groupGoodsList = new ArrayList<>();
        exhibitGroupVo.setGroupNm("상품그룹명1");
        exhibitGroupVo.setTextColor("#000001");
        exhibitGroupVo.setGroupUseYn("Y");
        exhibitGroupVo.setExhibitImgTp("EXHIBIT_IMG_TP.NOT_USE");
        exhibitGroupVo.setGroupDesc("상품그룹설명1");
        exhibitGroupVo.setDispCnt(11);
        exhibitGroupVo.setGroupSort(12);
        exhibitGroupDetlVo = new ExhibitGroupDetlVo();
        exhibitGroupDetlVo.setGoodsSort(1);
        exhibitGroupDetlVo.setIlGoodsId("175");
        groupGoodsList.add(exhibitGroupDetlVo);
        exhibitGroupDetlVo = new ExhibitGroupDetlVo();
        exhibitGroupDetlVo.setGoodsSort(2);
        exhibitGroupDetlVo.setIlGoodsId("177");
        groupGoodsList.add(exhibitGroupDetlVo);
        exhibitGroupVo.setGroupGoodsList(groupGoodsList);
        groupList.add(exhibitGroupVo);
        // 그룹2
        exhibitGroupVo = new ExhibitGroupVo();
        groupGoodsList = new ArrayList<>();
        exhibitGroupVo.setGroupNm("상품그룹명2");
        exhibitGroupVo.setTextColor("#000002");
        exhibitGroupVo.setGroupUseYn("Y");
        exhibitGroupVo.setExhibitImgTp("EXHIBIT_IMG_TP.NOT_USE");
        exhibitGroupVo.setGroupDesc("상품그룹설명1");
        exhibitGroupVo.setDispCnt(21);
        exhibitGroupVo.setGroupSort(22);
        exhibitGroupDetlVo = new ExhibitGroupDetlVo();
        exhibitGroupDetlVo.setGoodsSort(1);
        exhibitGroupDetlVo.setIlGoodsId("181");
        groupGoodsList.add(exhibitGroupDetlVo);
        exhibitGroupDetlVo = new ExhibitGroupDetlVo();
        exhibitGroupDetlVo.setGoodsSort(2);
        exhibitGroupDetlVo.setIlGoodsId("182");
        groupGoodsList.add(exhibitGroupDetlVo);
        exhibitGroupVo.setGroupGoodsList(groupGoodsList);
        groupList.add(exhibitGroupVo);

        exhibitManageRequestDto.setGroupList(groupList);

        //when
        ExhibitManageResponseDto result = exhibitManageService.addExhibit(exhibitManageRequestDto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    public void test_기획전수정() throws BaseException {
        //given
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();

        // 기획전 기본정보
        exhibitManageRequestDto.setExhibitTp("EXHIBIT_TP.NORMAL");
        ExhibitVo exhibitVo = new ExhibitVo();
        exhibitVo.setEvExhibitId("25");
        exhibitVo.setExhibitTp("EXHIBIT_TP.NORMAL");
        exhibitVo.setMallDiv("MALL_DIV.PULMUONE");
        exhibitVo.setUseYn("Y");
        exhibitVo.setTitle("기획전제목");
        exhibitVo.setDescription("기획전설명");
        exhibitVo.setDispWebPcYn("Y");
        exhibitVo.setDispWebMobileYn("N");
        exhibitVo.setDispAppYn("Y");
        exhibitVo.setDispNonmemberYn("N");
        exhibitVo.setEvEmployeeTp("EV_EMPLOYEE_TP.NO_LIMIT");
        exhibitVo.setAlwaysYn("Y");
        exhibitVo.setStartDt("20201229000000");
        exhibitVo.setEndDt("29991231235959");
        exhibitVo.setTimeOverCloseYn("Y");
        exhibitVo.setBnrImgPath("BOS/dp/test/2020/12/29/635F4856C1B04A4CBB78.jpg");
        exhibitVo.setBnrImgOriginNm("AcadianDay_ROW6460737821_1920x1200.jpg");
        exhibitVo.setDetlHtmlPc("기획전상세-PC");
        exhibitVo.setDetlHtmlMo("기획전상세-Mobile");
        exhibitVo.setDispYn("N");
        exhibitManageRequestDto.setExhibitInfo(exhibitVo);

        // 기획전 상세정보 - 일반기획전
        ExhibitGroupVo exhibitGroupVo;
        List<ExhibitGroupVo> groupList;
        ExhibitGroupDetlVo exhibitGroupDetlVo;
        List<ExhibitGroupDetlVo> groupGoodsList;
        // 그룹1
        exhibitGroupVo = new ExhibitGroupVo();
        groupList = new ArrayList<>();
        groupGoodsList = new ArrayList<>();
        exhibitGroupVo.setGroupNm("상품그룹명1");
        exhibitGroupVo.setTextColor("#000001");
        exhibitGroupVo.setGroupUseYn("Y");
        exhibitGroupVo.setExhibitImgTp("EXHIBIT_IMG_TP.NOT_USE");
        exhibitGroupVo.setGroupDesc("상품그룹설명1");
        exhibitGroupVo.setDispCnt(11);
        exhibitGroupVo.setGroupSort(12);
        exhibitGroupDetlVo = new ExhibitGroupDetlVo();
        exhibitGroupDetlVo.setGoodsSort(1);
        exhibitGroupDetlVo.setIlGoodsId("175");
        groupGoodsList.add(exhibitGroupDetlVo);
        exhibitGroupDetlVo = new ExhibitGroupDetlVo();
        exhibitGroupDetlVo.setGoodsSort(2);
        exhibitGroupDetlVo.setIlGoodsId("177");
        groupGoodsList.add(exhibitGroupDetlVo);
        exhibitGroupVo.setGroupGoodsList(groupGoodsList);
        groupList.add(exhibitGroupVo);
        // 그룹2
        exhibitGroupVo = new ExhibitGroupVo();
        groupGoodsList = new ArrayList<>();
        exhibitGroupVo.setGroupNm("상품그룹명2");
        exhibitGroupVo.setTextColor("#000002");
        exhibitGroupVo.setGroupUseYn("Y");
        exhibitGroupVo.setExhibitImgTp("EXHIBIT_IMG_TP.NOT_USE");
        exhibitGroupVo.setGroupDesc("상품그룹설명1");
        exhibitGroupVo.setDispCnt(21);
        exhibitGroupVo.setGroupSort(22);
        exhibitGroupDetlVo = new ExhibitGroupDetlVo();
        exhibitGroupDetlVo.setGoodsSort(1);
        exhibitGroupDetlVo.setIlGoodsId("181");
        groupGoodsList.add(exhibitGroupDetlVo);
        exhibitGroupDetlVo = new ExhibitGroupDetlVo();
        exhibitGroupDetlVo.setGoodsSort(2);
        exhibitGroupDetlVo.setIlGoodsId("182");
        groupGoodsList.add(exhibitGroupDetlVo);
        exhibitGroupVo.setGroupGoodsList(groupGoodsList);
        groupList.add(exhibitGroupVo);

        exhibitManageRequestDto.setGroupList(groupList);

        //when
        ExhibitManageResponseDto result = exhibitManageService.putExhibit(exhibitManageRequestDto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    public void test_증정행사대표상품변경() throws BaseException {
        //given
        ExhibitManageRequestDto exhibitManageRequestDto = new ExhibitManageRequestDto();
        exhibitManageRequestDto.setEvExhibitId("34");
        exhibitManageRequestDto.setEvExhibitGiftGoodsId("30");

        //when
        ExhibitManageResponseDto result = exhibitManageService.putExhibitGiftRepGoods(exhibitManageRequestDto);

        //then
        assertEquals(ExhibitEnums.ExhibitMessage.EXHIBIT_MANAGE_SUCCESS.getCode(), result.getResultCode());
    }

    @Test
    void getExhibitUserGroup_조회_성공() throws BaseException {
        //given
        String exExhibitId = "44";

        //when
        List<EvUserGroupVo> result = exhibitManageService.getExhibitUserGroup(exExhibitId);

        //then
        assertTrue(result.size() > 0);
    }


    @Test
    void test_기획전상태이력등록_성공() {
        //given
        given(mockExhibitManageMapper.addExhibitStatusHistory(any())).willReturn(1);
        // when
        int result = mockExhibitManageService.addExhibitStatusHistory(null);

        // then
        assertEquals(1, result);
    }

    @Test
    void test_기획전상태이력등록_실패() {
        //given
        given(mockExhibitManageMapper.addExhibitStatusHistory(any())).willReturn(0);
        // when
        int result = mockExhibitManageService.addExhibitStatusHistory(null);

        // then
        assertNotEquals(1, result);
    }

    @Test
    void test_기획전승인목록조회_결과있음() {
        //given
        ApprovalExhibitRequestDto requestDto = new ApprovalExhibitRequestDto();

        requestDto.setExhibitTp(ExhibitEnums.ExhibitTp.SELECT.getCode());
        // when
        ExhibitApprovalResponseDto result = exhibitManageService.getApprovalExhibitList(requestDto);

        // then
        assertNotNull(result.getRows());
    }

    @Test
    void test_기획전승인목록조회_결과없음() {
        //given
        ApprovalExhibitRequestDto requestDto = new ApprovalExhibitRequestDto();

        // when
        ExhibitApprovalResponseDto result = exhibitManageService.getApprovalExhibitList(requestDto);

        // then
        assertTrue(CollectionUtils.isEmpty(result.getRows()));
    }

    @Test
    void test_기획전승인요청철회_성공() throws Exception {
        //given
        given(mockExhibitManageMapper.putCancelRequestApprovalExhibit(any())).willReturn(1);
        given(mockExhibitManageService.addExhibitStatusHistory(any())).willReturn(1);
        // when
        MessageCommEnum result = mockExhibitManageService.putCancelRequestApprovalExhibit(null);

        // then
        assertEquals(ApiResult.success().getCode(), result.getCode());
    }

    @Test
    void test_기획전승인요청철회_실패() {
        //given
        given(mockExhibitManageMapper.putCancelRequestApprovalExhibit(any())).willReturn(0);
        given(mockExhibitManageService.addExhibitStatusHistory(any())).willReturn(0);

        // when
        BaseException myException = assertThrows(BaseException.class, () -> mockExhibitManageService.putCancelRequestApprovalExhibit(null));

        // then
        assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }

    @Test
    void test_기획전승인처리_성공() throws Exception {
        //given
        given(mockExhibitManageMapper.putApprovalProcessExhibit(any())).willReturn(1);
        given(mockExhibitManageService.addExhibitStatusHistory(any())).willReturn(1);
        // when
        MessageCommEnum result = mockExhibitManageService.putApprovalProcessExhibit(null);

        // then
        assertEquals(ApiResult.success().getCode(), result.getCode());
    }

    @Test
    void test_기획전승인처리_실패() {
        //given
        given(mockExhibitManageMapper.putApprovalProcessExhibit(any())).willReturn(0);
        given(mockExhibitManageService.addExhibitStatusHistory(any())).willReturn(0);

        // when
        BaseException myException = assertThrows(BaseException.class, () -> mockExhibitManageService.putApprovalProcessExhibit(null));

        // then
        assertEquals(BaseEnums.CommBase.PROGRAM_ERROR, myException.getMessageEnum());
    }

    @Test
    void putApprovalRequestExhibit_승인요청_성공() throws Exception {
        //given
        given(mockExhibitManageMapper.putApprovalRequestExhibit(any())).willReturn(1);
        given(mockExhibitManageService.addExhibitStatusHistory(any())).willReturn(1);

        // when
        MessageCommEnum result = mockExhibitManageService.putApprovalRequestExhibit(null, null);

        // then
        assertEquals(ApiResult.success().getCode(), result.getCode());
    }


    @Test
    void getExhibitSelectGoodsListForMaxRate_최대한인율계산() throws Exception {
        // int result = exhibitManageService.getExhibitSelectGoodsListForMaxRate("65");
        // then
        // assertEquals(5, result);
    }
}
