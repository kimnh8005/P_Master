package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.*;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordClearRequestDto;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserBuyerBosServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserBuyerBosService userBuyerBosService;

    @Test
    void 회원명으로_검색시_다른조건_무시하고_조회_결과없음() throws Exception{
        // given
        GetBuyerListRequestDto searchDto = new GetBuyerListRequestDto();
        searchDto.setCondiValue("존재하지않는이름");
        searchDto.setCondiType("userName");
        searchDto.setStartCreateDate("20200617");
        searchDto.setEndCreateDate("20200918");
        searchDto.setMailYn("N");
        searchDto.setPageSize(20);
        searchDto.setPage(1);

        // when
        GetBuyerListResponseDto data = userBuyerBosService.getBuyerList(searchDto);

        // then
        assertTrue(data.getRows().size() == 0);
    }

    @Test
    void 전체회원검색_결과_있음() throws Exception {
        // given
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        List<String> listRoleId = new ArrayList<>();
        listRoleId.add("1");
        userVO.setListRoleId(listRoleId);
        SessionUtil.setUserVO(userVO);

        GetBuyerListRequestDto searchDto = new GetBuyerListRequestDto();
        ArrayList<String> condiValueArray = new ArrayList<>();
        condiValueArray.add("문상필");
        searchDto.setCondiValueArray(condiValueArray);
        //searchDto.setCondiValue("문상필");
        searchDto.setCondiType("userName");
        searchDto.setStartCreateDate("20200617");
        searchDto.setEndCreateDate("20200918");
        searchDto.setMailYn("N");
        searchDto.setPageSize(20);
        searchDto.setPage(1);

        // when
        GetBuyerListResponseDto result = userBuyerBosService.getBuyerList(searchDto);

        // then
        assertNotNull(result.getRows());
        SessionUtil.setUserVO(null);
    }

    @Test
    void 전체엑회원검색_엑셀다운로드_조회_결과_있음() throws Exception {
        // given
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        GetBuyerListRequestDto searchDto = new GetBuyerListRequestDto();
        searchDto.setCondiValue("");
        searchDto.setCondiType("userName");
        searchDto.setStartCreateDate("20200617");
        searchDto.setEndCreateDate("20200918");
        searchDto.setMailYn("N");
        searchDto.setPageSize(20);
        searchDto.setPage(1);

        // when
        List<GetBuyerListResultVo> result = userBuyerBosService.getBuyerListExcel(searchDto);

        // then
        assertTrue(result.size() > 1);
        SessionUtil.setUserVO(null);
    }

    @Test
    void 회원정보_단건조회_성공() throws Exception{
        // given
        GetBuyerRequestDto searchDto = new GetBuyerRequestDto();
        searchDto.setUrUserId("20");

        // when
        GetBuyerResultVo buyerVo = userBuyerBosService.getBuyer(searchDto);

        // then
        assertEquals("admin", buyerVo.getLoginId());
    }

    @Test
    void 회원그룹변경이력_조회_성공() throws Exception{
        // given
        GetBuyerGroupChangeHistoryListRequestDto searchDto = new GetBuyerGroupChangeHistoryListRequestDto();
        searchDto.setUrUserId("659357");

        // when
        List<GetBuyerGroupChangeHistoryListResultVo> result = userBuyerBosService.getBuyerGroupChangeHistoryList(searchDto);

        // then
        assertNotNull(result);

    }

    @Test
    void 추천인리스트_조회_성공() throws Exception {
        // given
        GetBuyerRecommendListRequestDto searchDto = new GetBuyerRecommendListRequestDto();
        searchDto.setUrUserId("659357");

        // when
        List<GetBuyerRecommendListResultVo> result = userBuyerBosService.getBuyerRecommendList(searchDto);

        // then
        assertTrue(result.size() > 0);
    }

    @Test
    void 회원정보_수정_성공() throws Exception {
        // given
        PutBuyerRequestDto putDto = new PutBuyerRequestDto();
        putDto.setGender("M");
        putDto.setSmsYn("Y");
        putDto.setMailYn("Y");
        putDto.setUrUserId("20");

        // when
        Map<String, Object> result = userBuyerBosService.putBuyer(putDto);

        // then
        assertTrue(Integer.parseInt(result.get("cnt").toString()) > 0 );
    }

    @Test
    void 휴면회원_단건조회() throws Exception {
        // given
        GetBuyerRequestDto searchDto = new GetBuyerRequestDto();
        searchDto.setUrUserId("421702");

        // when
        GetBuyerResultVo buyerVo = userBuyerBosService.getBuyerMove(searchDto);

        // then
        assertEquals("421702", buyerVo.getUrUserId());
    }

    @Test
    void 전체회원_엑셀다운로드_조회_성공() throws Exception {
        // given
        GetBuyerListRequestDto searchDto = new GetBuyerListRequestDto();
        searchDto.setCondiValue("홍길동");
        searchDto.setCondiType("userName");

        // when
        ExcelDownloadDto result = userBuyerBosService.getBuyerListExcelDownload(searchDto);

        // then
        assertNotNull(result.getExcelWorkSheetList());
    }

    @Test
    void 사용자_패스워드_초기화_성공() throws Exception {
        // given
        PutPasswordClearRequestDto putDto = new PutPasswordClearRequestDto();
        putDto.setUrUserId("1");
        putDto.setMail("moonkoon@kakao.com");
        putDto.setMobile("01085684533");
        putDto.setPassword(RandomStringUtils.randomAlphanumeric(5));
        putDto.setPutPasswordType("∀");

        // when
        int result = userBuyerBosService.putPasswordClear(putDto);

        // then
        assertTrue(result > 0);
    }


    @Test
    void 개인정보_변경이력_조회_데이터있음() throws Exception {
        // given
        GetUserChangeHistoryListRequestDto searchDto = new GetUserChangeHistoryListRequestDto();
        searchDto.setCondiValue("홍길동");
        searchDto.setCondiType("userName");

        // when
        GetUserChangeHistoryListResponseDto result = userBuyerBosService.getUserChangeHistoryList(searchDto);

        // then
        assertNotNull(result.getRows());
    }

    @Test
    void 악성클레임회원_조회_데이터있음() throws Exception {
        // given
        GetUserMaliciousClaimHistoryListRequestDto searchDto = new GetUserMaliciousClaimHistoryListRequestDto();
        searchDto.setUrUserId("659357");

        // when
        List<GetUserMaliciousClaimHistoryListResultVo> result = userBuyerBosService.getUserMaliciousClaimHistoryList(searchDto);

        // then
        assertNotNull(result);
    }

}