package kr.co.pulmuone.v1.approval.auth.service;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthByTaskDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthByTaskInfoDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalAuthManagerHistoryByTaskDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalStatusHistoryDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthInfoVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ExhibitEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.approval.auth.ApprovalAuthMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
class ApprovalAuthServiceTest extends CommonServiceTestBaseForJunit5 {


	@Autowired
    private ApprovalAuthService approvalAuthService;

	@InjectMocks
	private ApprovalAuthService mockApprovalAuthService;

	@Mock
	private ApprovalAuthMapper mockApprovalAuthMapper;

	@BeforeEach
	void setUp() {
		mockApprovalAuthService = new ApprovalAuthService(mockApprovalAuthMapper);
	}

	public void test_업무별승인권한관리_목록조회_결과있음()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();

		List<GetCodeListResultVo> taskList = new ArrayList<GetCodeListResultVo>();
		GetCodeListResultVo vo = new GetCodeListResultVo();
		vo.setCode("APPR_KIND_TP.ITEM_REGIST");
		vo.setName("품목등록 승인관리");
		taskList.add(vo);
		vo = new GetCodeListResultVo();
		vo.setCode("APPR_KIND_TP.GOODS_REGIST");
		vo.setName("상품등록 승인관리");
		taskList.add(vo);
		vo = new GetCodeListResultVo();
		vo.setCode("APPR_KIND_TP.ITEM_PRICE");
		vo.setName("품목가격  승인관리");
		taskList.add(vo);
		// when
		ApprovalAuthByTaskDto result = approvalAuthService.getApprovalAuthByTaskList(taskList);

		// then
		assertNotNull(result);
	}
	@Test
	public void test_업무별승인권한관리_목록조회_결과없음()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();

		List<GetCodeListResultVo> taskList = new ArrayList<GetCodeListResultVo>();
		// when
		ApprovalAuthByTaskDto result = approvalAuthService.getApprovalAuthByTaskList(taskList);

		// then
		assertTrue(CollectionUtils.isEmpty(result.getRows()));
	}
	@Test
	public void test_업무별승인권한_승인관리자조회_결과있음()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();

		// when
		ApprovalAuthByTaskInfoDto result = approvalAuthService.getApprovalAuthByTaskInfo(taskCode);

		// then
		assertNotNull(result);
	}
	@Test
	public void test_업무별승인권한_승인관리자조회_결과없음()  {
		// given
		String taskCode = "";

		// when
		ApprovalAuthByTaskInfoDto result = approvalAuthService.getApprovalAuthByTaskInfo(taskCode);

		// then
		assertTrue(CollectionUtils.isEmpty(result.getAuthManager1stList()) );
	}

	@Test
	public void test_업무별직전_승인관리자이력조회_결과있음_쿠폰()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();
		apprManagerHistory.setApprovalSubUserId("1");
		apprManagerHistory.setApprovalUserId("1");

		given(mockApprovalAuthMapper.getApprovalAuthManagerHistoryCoupon(any())).willReturn(apprManagerHistory);

    	// when
		ApprovalAuthManagerHistoryByTaskDto result = mockApprovalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNotNull(result.getApprovalUserId());
	}

	@Test
	public void test_업무별직전_승인관리자이력조회_결과있음_적립금()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_POINT.getCode();
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();
		apprManagerHistory.setApprovalSubUserId("1");
		apprManagerHistory.setApprovalUserId("1");

		given(mockApprovalAuthMapper.getApprovalAuthManagerHistoryPoint(any())).willReturn(apprManagerHistory);

		// when
		ApprovalAuthManagerHistoryByTaskDto result = mockApprovalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNotNull(result.getApprovalUserId());
	}

	@Test
	public void test_업무별직전_승인관리자이력조회_결과있음_골라담기()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_SELECT.getCode();
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();
		apprManagerHistory.setApprovalSubUserId("1");
		apprManagerHistory.setApprovalUserId("1");

		given(mockApprovalAuthMapper.getApprovalAuthManagerHistoryExhibit(any(), ExhibitEnums.ExhibitTp.SELECT.getCode())).willReturn(apprManagerHistory);

		// when
		ApprovalAuthManagerHistoryByTaskDto result = mockApprovalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNotNull(result.getApprovalUserId());
	}
	@Test
	public void test_업무별직전_승인관리자이력조회_결과있음_증정행사()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_GIFT.getCode();
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();
		apprManagerHistory.setApprovalSubUserId("1");
		apprManagerHistory.setApprovalUserId("1");

		given(mockApprovalAuthMapper.getApprovalAuthManagerHistoryExhibit(any(), ExhibitEnums.ExhibitTp.GIFT.getCode())).willReturn(apprManagerHistory);

		// when
		ApprovalAuthManagerHistoryByTaskDto result = mockApprovalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNotNull(result.getApprovalUserId());
	}

	@Test
	public void test_업무별직전_승인관리자이력조회_결과있음_품목등록()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode();
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();
		apprManagerHistory.setApprovalSubUserId("1");
		apprManagerHistory.setApprovalUserId("1");

		given(mockApprovalAuthMapper.getApprovalAuthManagerHistoryItem(any(), any())).willReturn(apprManagerHistory);

		// when
		ApprovalAuthManagerHistoryByTaskDto result = mockApprovalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNotNull(result.getApprovalUserId());
	}

	@Test
	public void test_업무별직전_승인관리자이력조회_결과있음_품목수정()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode();
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();
		apprManagerHistory.setApprovalSubUserId("1");
		apprManagerHistory.setApprovalUserId("1");

		given(mockApprovalAuthMapper.getApprovalAuthManagerHistoryItem(any(), any())).willReturn(apprManagerHistory);

		// when
		ApprovalAuthManagerHistoryByTaskDto result = mockApprovalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNotNull(result.getApprovalUserId());
	}

	@Test
	public void test_업무별직전_승인관리자이력조회_결과있음_상품등록()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode();
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();
		apprManagerHistory.setApprovalSubUserId("1");
		apprManagerHistory.setApprovalUserId("1");

		given(mockApprovalAuthMapper.getApprovalAuthManagerHistoryGoods(any(), any())).willReturn(apprManagerHistory);

		// when
		ApprovalAuthManagerHistoryByTaskDto result = mockApprovalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNotNull(result.getApprovalUserId());
	}

	@Test
	public void test_업무별직전_승인관리자이력조회_결과있음_상품수정()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode();
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();
		apprManagerHistory.setApprovalSubUserId("1");
		apprManagerHistory.setApprovalUserId("1");

		given(mockApprovalAuthMapper.getApprovalAuthManagerHistoryGoods(any(), any())).willReturn(apprManagerHistory);

		// when
		ApprovalAuthManagerHistoryByTaskDto result = mockApprovalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNotNull(result.getApprovalUserId());
	}

	@Test
	public void test_업무별직전_승인관리자이력조회_결과있음_품목가격()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode();
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		ApprovalAuthManagerHistoryByTaskDto apprManagerHistory = new ApprovalAuthManagerHistoryByTaskDto();
		apprManagerHistory.setApprovalSubUserId("1");
		apprManagerHistory.setApprovalUserId("1");

		given(mockApprovalAuthMapper.getApprovalAuthManagerHistoryItemPrice(any())).willReturn(apprManagerHistory);

		// when
		ApprovalAuthManagerHistoryByTaskDto result = mockApprovalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNotNull(result.getApprovalUserId());
	}

	@Test
	public void test_업무별직전_승인관리자이력조회_결과없음()  {

		// given
		String taskCode = "TEST";

		// when
		ApprovalAuthManagerHistoryByTaskDto result = approvalAuthService.getApprovalAuthManagerHistoryByTask(taskCode);

		// then
		assertNull(result.getApprovalUserId());
	}

	@Test
	public void test_업무별_승인처리_정보_조회_쿠폰()  {
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();
		String taskPk = "1";

		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();
		approvalAuthInfoVo.setTaskPk(taskPk);

		given(mockApprovalAuthMapper.getApprovalProcessCouponInfo(any())).willReturn(approvalAuthInfoVo);

        // when
		ApprovalAuthInfoVo result = mockApprovalAuthService.getApprovalProcessInfo(taskCode, taskPk);

        // then
		assertNotNull(result.getTaskPk());
	}

	@Test
	public void test_업무별_승인처리_정보_조회_적립금()  {
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_POINT.getCode();
		String taskPk = "1";

		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();
		approvalAuthInfoVo.setTaskPk(taskPk);

		given(mockApprovalAuthMapper.getApprovalProcessPointInfo(any())).willReturn(approvalAuthInfoVo);

		// when
		ApprovalAuthInfoVo result = mockApprovalAuthService.getApprovalProcessInfo(taskCode, taskPk);

		// then
		assertNotNull(result.getTaskPk());
	}
	@Test
	public void test_업무별_승인처리_정보_조회_골라담기()  {
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_SELECT.getCode();
		String taskPk = "1";

		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();
		approvalAuthInfoVo.setTaskPk(taskPk);

		given(mockApprovalAuthMapper.getApprovalProcessExhibitInfo(any())).willReturn(approvalAuthInfoVo);

		// when
		ApprovalAuthInfoVo result = mockApprovalAuthService.getApprovalProcessInfo(taskCode, taskPk);

		// then
		assertNotNull(result.getTaskPk());
	}
	@Test
	public void test_업무별_승인처리_정보_조회_증정행사()  {
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_EXHIBIT_GIFT.getCode();
		String taskPk = "1";

		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();
		approvalAuthInfoVo.setTaskPk(taskPk);

		given(mockApprovalAuthMapper.getApprovalProcessExhibitInfo(any())).willReturn(approvalAuthInfoVo);

		// when
		ApprovalAuthInfoVo result = mockApprovalAuthService.getApprovalProcessInfo(taskCode, taskPk);

		// then
		assertNotNull(result.getTaskPk());
	}
	@Test
	public void test_업무별_승인처리_정보_조회_품목등록()  {
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode();
		String taskPk = "1";

		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();
		approvalAuthInfoVo.setTaskPk(taskPk);

		given(mockApprovalAuthMapper.getApprovalProcessItemRegistInfo(any())).willReturn(approvalAuthInfoVo);

		// when
		ApprovalAuthInfoVo result = mockApprovalAuthService.getApprovalProcessInfo(taskCode, taskPk);

		// then
		assertNotNull(result.getTaskPk());
	}
	public void test_업무별_승인처리_정보_조회_상품등록()  {
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode();
		String taskPk = "1";

		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();
		approvalAuthInfoVo.setTaskPk(taskPk);

		given(mockApprovalAuthMapper.getApprovalProcessGoodsRegistInfo(any())).willReturn(approvalAuthInfoVo);

		// when
		ApprovalAuthInfoVo result = mockApprovalAuthService.getApprovalProcessInfo(taskCode, taskPk);

		// then
		assertNotNull(result.getTaskPk());
	}

	@Test
	public void test_업무별_승인처리_정보_조회_품목가격()  {
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_PRICE.getCode();
		String taskPk = "1";

		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();
		approvalAuthInfoVo.setTaskPk(taskPk);

		given(mockApprovalAuthMapper.getApprovalProcessItemPriceInfo(any())).willReturn(approvalAuthInfoVo);

		// when
		ApprovalAuthInfoVo result = mockApprovalAuthService.getApprovalProcessInfo(taskCode, taskPk);

		// then
		assertNotNull(result.getTaskPk());
	}

	@Test
	public void test_승인철회_가능한_상태_체크_가능()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();
		String taskPk = "1";

		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();
		approvalAuthInfoVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
		approvalAuthInfoVo.setApprReqUserId("1");

		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		given(mockApprovalAuthMapper.getApprovalProcessCouponInfo(any())).willReturn(approvalAuthInfoVo);

        // when
		ApiResult<?> result = approvalAuthService.checkCancelable(approvalAuthInfoVo);

		// then
		assertTrue(result.getMessageEnum().equals(ApprovalEnums.ApprovalValidation.CANCELABLE));
    }

	@Test
	public void test_승인철회_가능한_상태_체크_불가()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();
		String taskPk = "1";

		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();
		approvalAuthInfoVo.setApprStat(ApprovalEnums.ApprovalStatus.APPROVED.getCode());
		approvalAuthInfoVo.setApprReqUserId("2");

		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		given(mockApprovalAuthMapper.getApprovalProcessCouponInfo(any())).willReturn(approvalAuthInfoVo);

        // when
		ApiResult<?> result = approvalAuthService.checkCancelable(approvalAuthInfoVo);

		// then
		assertTrue(!result.getMessageEnum().equals(ApprovalEnums.ApprovalValidation.CANCELABLE));
	}

	@Test
	public void test_승인가능_상태_체크_가능()  {
		// given
		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();

		approvalAuthInfoVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
		approvalAuthInfoVo.setApprSubUserId("1");
		approvalAuthInfoVo.setApprSubUserStatus(UserEnums.EmployeeStatus.NORMAL.getCode());

		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		// when
		ApiResult<?> result = approvalAuthService.checkApprovable(approvalAuthInfoVo);

		// then
		assertTrue(result.getMessageEnum().equals(ApprovalEnums.ApprovalValidation.APPROVABLE));
	}

	@Test
	public void test_승인가능_상태_체크_가능_불가()  {
		// given
		ApprovalAuthInfoVo approvalAuthInfoVo = new ApprovalAuthInfoVo();

		approvalAuthInfoVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
		approvalAuthInfoVo.setApprReqUserId("1");

		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);

		// when
		ApiResult<?> result = approvalAuthService.checkApprovable(approvalAuthInfoVo);

		// then
		assertTrue(!result.getMessageEnum().equals(ApprovalEnums.ApprovalValidation.APPROVABLE));
	}

	@Test
	public void test_업무별승인관리자_정보저장_성공() throws Exception {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();

		ApprovalAuthByTaskInfoDto reqData = approvalAuthService.getApprovalAuthByTaskInfo(taskCode);
		UserVo userVO = new UserVo();
		userVO.setUserId("1");
		userVO.setLoginId("forbiz");
		userVO.setLangCode("1");
		SessionUtil.setUserVO(userVO);
		// when
		MessageCommEnum result = approvalAuthService.putApprovalAuthByTaskInfo(reqData);

		// then
        assertTrue(result.getCode().equals(ApiResult.success().getCode()));
	}
	@Test
	public void test_업무별승인관리자_정보저장_실패1() throws Exception {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();

		ApprovalAuthByTaskInfoDto reqData = new ApprovalAuthByTaskInfoDto();

		given(mockApprovalAuthMapper.addApprovalAuthManager(any())).willReturn(0);


		// when
		MessageCommEnum result = mockApprovalAuthService.putApprovalAuthByTaskInfo(reqData);

		// then
		assertTrue(result.getCode().equals(ApiResult.fail().getCode()));
	}
	@Test
	public void test_업무별승인관리자_정보저장_실패2() throws Exception {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();

		ApprovalAuthByTaskInfoDto reqData = approvalAuthService.getApprovalAuthByTaskInfo(taskCode);

		given(mockApprovalAuthMapper.addApprovalAuthManager(any())).willReturn(0);


    	// when
    	BaseException myException = assertThrows(BaseException.class, () -> {
    		mockApprovalAuthService.putApprovalAuthByTaskInfo(reqData);
    	});

    	// then
    	assertEquals(BaseEnums.CommBase.VALID_ERROR, myException.getMessageEnum());
	}
	public void test_업무별승인관리자_관리자검증_성공() throws Exception {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();

		ApprovalAuthByTaskInfoDto reqData = approvalAuthService.getApprovalAuthByTaskInfo(taskCode);

		// when
		MessageCommEnum result = approvalAuthService.validationApprovalAuthManager(reqData.getAuthManager1stList());

		// then
        assertTrue(result.getCode().equals(ApiResult.success().getCode()));
	}

	public void test_업무별승인관리자_관리자검증_실패() throws Exception {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();

		ApprovalAuthByTaskInfoDto reqData = approvalAuthService.getApprovalAuthByTaskInfo(taskCode);

		// when
		MessageCommEnum result = approvalAuthService.validationApprovalAuthManager(reqData.getAuthManager1stList());

		// then
		assertTrue(!result.getCode().equals(ApiResult.success().getCode()));
	}

	@Test
	public void test_업무별승인내역_이력조회_결과있음_쿠폰()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();
		String taskPk = "1";

		List<ApprovalStatusVo> rows = new ArrayList<ApprovalStatusVo>();
		rows.add(new ApprovalStatusVo());

		given(mockApprovalAuthMapper.getApprovalHistoryCoupon(any())).willReturn(rows);

    	// when
		ApprovalStatusHistoryDto result = mockApprovalAuthService.getApprovalHistory(taskCode, taskPk);

		// then
		assertTrue(CollectionUtils.isNotEmpty(result.getRows()));
	}
	@Test
	public void test_업무별승인내역_이력조회_결과있음_적립금()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_POINT.getCode();
		String taskPk = "1";

		List<ApprovalStatusVo> rows = new ArrayList<ApprovalStatusVo>();
		rows.add(new ApprovalStatusVo());

		given(mockApprovalAuthMapper.getApprovalHistoryPoint(any())).willReturn(rows);

		// when
		ApprovalStatusHistoryDto result = mockApprovalAuthService.getApprovalHistory(taskCode, taskPk);

		// then
		assertTrue(CollectionUtils.isNotEmpty(result.getRows()));
	}
	@Test
	public void test_업무별승인내역_이력조회_결과없음()  {
		// given
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode();

		given(mockApprovalAuthMapper.getApprovalHistoryCoupon(any())).willReturn(null);

		// when
		ApprovalStatusHistoryDto result = mockApprovalAuthService.getApprovalHistory(taskCode, null);

		// then
		assertTrue(CollectionUtils.isEmpty(result.getRows()));
	}

}
