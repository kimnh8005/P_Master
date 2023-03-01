package kr.co.pulmuone.v1.policy.clause.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.mapper.policy.clause.PolicyClauseMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.policy.clause.dto.*;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetClauseResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseViewResultVo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class PolicyClauseServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private PolicyClauseService policyClauseService;

	@InjectMocks
	private PolicyClauseService mockPolicyClauseService;

	@Mock
	PolicyClauseMapper mockPolicyClauseMapper;

    @Test
    void 회원가입_최신_약관_리스트_조회() throws Exception {

        ApiResult<?> apiResult = policyClauseService.getLatestJoinClauseList();

        int cnt = Optional.ofNullable(apiResult.getData()).map(b -> ((List)b).size()).orElse(0);

        assertTrue(apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode()));
        assertTrue(cnt > 0);
    }

    @Test
    void 최신_약관_내용_정보_조회() throws Exception {

        String psClauseGrpCd = "MARKETING_RECEIPT_AGREEMENT";

        ApiResult<?> apiResult = policyClauseService.getLatestClause(psClauseGrpCd);

        String result = Optional.ofNullable(apiResult.getData()).map(b -> ((GetClauseResultVo)b).getPsClauseGrpCd()).orElse("");

        assertTrue(apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode()));
        assertEquals(result, "MARKETING_RECEIPT_AGREEMENT");
    }

    @Test
    void 약관_변경이력_리스트() throws Exception {

        String psClauseGrpCd = "MARKETING_RECEIPT_AGREEMENT";

        ApiResult<?> apiResult = policyClauseService.getClauseHistoryList(psClauseGrpCd);

        int cnt = Optional.ofNullable(apiResult.getData()).map(b -> ((List)b).size()).orElse(0);

        assertTrue(apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode()));
        assertTrue(cnt > 0);
    }

    @Test
    void 버전별_약관_내용_정보() throws Exception {

        GetClauseRequestDto getClauseRequestDto = new GetClauseRequestDto();
        getClauseRequestDto.setPsClauseGrpCd("MARKETING_RECEIPT_AGREEMENT");
        getClauseRequestDto.setExecuteDate("2020-07-30");

        ApiResult<?> apiResult = policyClauseService.getClause(getClauseRequestDto);

        assertTrue(apiResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode()));
    }
    @Test
    public void test_약관그룸관리_목록() throws Exception {

    	PolicyGetClauseGroupListRequestDto dto = new PolicyGetClauseGroupListRequestDto();
    	PolicyGetClauseGroupListResponseDto result = policyClauseService.getClauseGroupList(dto);
    	assertNotNull(result.getRows());
    }
    @Test
    public void test_약관그룸관리_저장() throws Exception {
    	String psClauseGroupCd = "test746";
    	String clauseGroupName = "test746";
    	String clauseTitle = "test746";
    	String useYn = "N";
    	String mandatoryYn = "N";
    	String sort = "1";

    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        SessionUtil.setUserVO(userVO);

    	PolicySaveClauseGroupRequestSaveDto dto = new PolicySaveClauseGroupRequestSaveDto();
    	dto.setPsClauseGroupCd(psClauseGroupCd);
    	dto.setClauseGroupName(clauseGroupName);
    	dto.setClauseTitle(clauseTitle);
    	dto.setUseYn(useYn);
    	dto.setMandatoryYn(mandatoryYn);
    	dto.setSort(sort);

    	PolicySaveClauseGroupRequestDto param = new PolicySaveClauseGroupRequestDto();

    	List<PolicySaveClauseGroupRequestSaveDto> insertRequestDtoList = new ArrayList<PolicySaveClauseGroupRequestSaveDto>();
    	insertRequestDtoList.add(dto);

    	param.setInsertRequestDtoList(insertRequestDtoList);

    	insertRequestDtoList = new ArrayList<PolicySaveClauseGroupRequestSaveDto>();

    	param.setUpdateRequestDtoList(insertRequestDtoList);

        MessageCommEnum msgEnum = policyClauseService.saveClauseGroup(param);

        assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));
    }
    @Test
    public void test_약관그룸관리_수정() throws Exception {
    	String psClauseGroupCd = "tetret";
    	String clauseGroupName = "tetetetet";
    	String clauseTitle = "et";
    	String useYn = "N";
    	String mandatoryYn = "N";
    	String sort = "1";

    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        SessionUtil.setUserVO(userVO);

    	PolicySaveClauseGroupRequestSaveDto dto = new PolicySaveClauseGroupRequestSaveDto();
    	dto.setPsClauseGroupCd(psClauseGroupCd);
    	dto.setClauseGroupName(clauseGroupName);
    	dto.setClauseTitle(clauseTitle);
    	dto.setUseYn(useYn);
    	dto.setMandatoryYn(mandatoryYn);
    	dto.setSort(sort);

    	PolicySaveClauseGroupRequestDto param = new PolicySaveClauseGroupRequestDto();

    	List<PolicySaveClauseGroupRequestSaveDto> updateRequestDtoList = new ArrayList<PolicySaveClauseGroupRequestSaveDto>();
    	updateRequestDtoList.add(dto);

    	param.setUpdateRequestDtoList(updateRequestDtoList);

    	updateRequestDtoList = new ArrayList<PolicySaveClauseGroupRequestSaveDto>();

    	param.setInsertRequestDtoList(updateRequestDtoList);

        MessageCommEnum msgEnum = policyClauseService.saveClauseGroup(param);

        assertTrue(msgEnum.getCode().equals(ApiResult.success().getCode()));
    }
    @Test
    public void test_약관관리_약관그룹_목록() throws Exception {

    	PolicyGetClauseGroupNameListRequestDto dto = new PolicyGetClauseGroupNameListRequestDto();
    	PolicyGetClauseGroupNameListResponseDto result = policyClauseService.getClauseGroupNameList(dto);
    	assertNotNull(result.getRows());
    }
    @Test
    public void test_약관관리_약관관리_목록() throws Exception {

    	String psClauseGroupCd = "28";
    	PolicyGetClauseListRequestDto dto = new PolicyGetClauseListRequestDto();
    	dto.setPsClauseGroupCd(psClauseGroupCd);
    	PolicyGetClauseListResponseDto result = policyClauseService.getClauseList(dto);
    	assertNotNull(result.getRows());
    }
    @Test
    public void test_약관관리_약관관리_보기() throws Exception {

    	String psClauseId = "1554";
    	PolicyGetClauseModifyViewRequestDto dto = new PolicyGetClauseModifyViewRequestDto();
    	dto.setPsClauseId(psClauseId);
    	PolicyGetClauseModifyViewResponseDto result = policyClauseService.getClauseModifyView(dto);
    	assertNotNull(result.getRows());
    }
    @Test
    public void test_약관관리_약관_저장() throws Exception {
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        SessionUtil.setUserVO(userVO);

    	String inputPsClauseGroupCd = "PRIVACY_POLICY_SELECT";
    	String executeDate = "20201110";
    	String clauseDescription = "테스트";
    	String content = "개정정보 처리 방침";
    	String mandatoryYn = "1";
    	String clauseInfo = "당사는 모바일 앱 서비스를 위하여 아래와 같이 고객의 이동통신단말기 내 정보 및 기능에 접근";
    	PolicyAddClauseRequestDto dto = new PolicyAddClauseRequestDto();
    	dto.setInputPsClauseGroupCd(inputPsClauseGroupCd);
    	dto.setExecuteDate(executeDate);
    	dto.setClauseDescription(clauseDescription);
    	dto.setContent(content);
    	dto.setMandatoryYn(mandatoryYn);
    	dto.setClauseInfo(clauseInfo);
    	PolicyAddClauseResponseDto result = policyClauseService.addClause(dto);
    	assertNotNull(result);
    }
    @Test
    public void test_약관관리_약관_수정() throws Exception {
    	UserVo userVO = new UserVo();
        userVO.setUserId("1");
        SessionUtil.setUserVO(userVO);

    	String executeDate = "20201113";
    	String clauseDescription = "테스트";
    	String content = "개정정보 처리 방침";
    	String mandatoryYn = "1";
    	String clauseInfo = "당사는 모바일 앱 서비스를 위하여 아래와 같이 고객의 이동통신단말기 내 정보 및 기능에 접근";
    	String psClauseId = "1556";
    	String histType = "U";
    	PolicyPutClauseRequestDto dto = new PolicyPutClauseRequestDto();

    	dto.setExecuteDate(executeDate);
    	dto.setClauseDescription(clauseDescription);
    	dto.setContent(content);
    	dto.setMandatoryYn(mandatoryYn);
    	dto.setClauseInfo(clauseInfo);
    	dto.setPsClauseId(psClauseId);
    	dto.setHistType(histType);
    	PolicyPutClauseResponseDto result = policyClauseService.putClause(dto);
    	assertNotNull(result);
    }
    @Test
    public void test_약관관리_약관_삭제() throws Exception {

    	String psClauseId = "1554";
    	String histType = "D";
    	PolicyDelClauseRequestDto dto = new PolicyDelClauseRequestDto();
    	dto.setPsClauseId(psClauseId);
    	dto.setHistType(histType);
    	PolicyDelClauseResponseDto result = policyClauseService.delClause(dto);
    	assertNotNull(result);
    }

    @Test
    void 구매_약관_리스트_조회() throws Exception {
		GetClauseRequestDto getClauseRequestDto = new GetClauseRequestDto();

		List<GetLatestJoinClauseListResultVo> rows = new ArrayList<>();
		GetLatestJoinClauseListResultVo getLatestJoinClauseListResultVo = new GetLatestJoinClauseListResultVo();
		rows.add(getLatestJoinClauseListResultVo);

		given(mockPolicyClauseMapper.getPurchaseTermsClauseList(any())).willReturn(rows);

		//when
		ApiResult<?> result = mockPolicyClauseService.getPurchaseTermsClauseList(getClauseRequestDto);

		//then
		assertEquals(ApiResult.success().getCode(), result.getCode());
    }

    @Test
    void 약관_정보_조회_getClauseView() throws Exception {
		PolicyGetClauseViewRequestDto policyGetClauseViewRequestDto = new PolicyGetClauseViewRequestDto();
		PolicyGetClauseViewResultVo policyGetClauseViewResultVo = new PolicyGetClauseViewResultVo();

		given(mockPolicyClauseMapper.getClauseView(any())).willReturn(policyGetClauseViewResultVo);

		//when
		PolicyGetClauseViewResponseDto result = mockPolicyClauseService.getClauseView(policyGetClauseViewRequestDto);

		//then
		assertNotNull(result.getRows());
    }

    @Test
    void 버전별_약관_내용_정보_조회_getClause() throws Exception {
		GetClauseRequestDto getClauseRequestDto = new GetClauseRequestDto();
		getClauseRequestDto.setPsClauseGrpCd("01");
		getClauseRequestDto.setExecuteDate("20201201");

		GetClauseResultVo getClauseResultVo = new GetClauseResultVo();


		given(mockPolicyClauseMapper.getClause(any())).willReturn(getClauseResultVo);

		//when
		ApiResult<?> result = mockPolicyClauseService.getClause(getClauseRequestDto);

		//then
		assertEquals(ApiResult.success().getCode(), result.getCode());
    }

	@Test
	void getClause() throws Exception {
		PolicyGetClauseRequestDto policyGetClauseRequestDto = new PolicyGetClauseRequestDto();
		policyGetClauseRequestDto.setPsClauseId("1556");
		PolicyGetClauseResponseDto result = policyClauseService.getClause(policyGetClauseRequestDto);
		assertTrue(result.getRows() != null);
	}
}
