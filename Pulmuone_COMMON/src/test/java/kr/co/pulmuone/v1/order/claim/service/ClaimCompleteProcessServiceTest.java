package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import kr.co.pulmuone.v1.order.claim.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalCsRefundRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalStatus;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.order.claim.dto.ApprovalCsRefundListResponseDto;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ClaimCompleteProcessServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    ClaimCompleteProcessService claimCompleteProcessService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void getApprovalCsRefundList_조회() throws Exception {

    	ApprovalCsRefundRequestDto requestDto =  new ApprovalCsRefundRequestDto();
    	requestDto.setDateSearchType("CLAIM_REQUEST_DATE");
    	requestDto.setDateSearchStart("2000-01-01");
    	requestDto.setDateSearchEnd("2000-01-01");

        // result
    	ApprovalCsRefundListResponseDto responseDto = claimCompleteProcessService.getApprovalCsRefundList(requestDto);

        // equals
        Assertions.assertTrue(responseDto.getTotal() == 0);
    }

    @Test
    void putCancelRequestApprovalCsRefund_요청철회() throws Exception {
    	ApprovalStatusVo approvalVo = new ApprovalStatusVo();
    	approvalVo.setTaskPk("0");
    	approvalVo.setApprStat(ApprovalStatus.CANCEL.getCode());

        // result
    	MessageCommEnum result = claimCompleteProcessService.putCancelRequestApprovalCsRefund(approvalVo);

        // equals
        Assertions.assertTrue(BaseEnums.Default.SUCCESS.equals(result));
    }

    @Test
    void putApprovalProcessCsRefund_요청() throws Exception {
    	ApprovalStatusVo approvalVo = new ApprovalStatusVo();
    	approvalVo.setTaskPk("0");
    	approvalVo.setApprStat(ApprovalStatus.APPROVED.getCode());
    	approvalVo.setApprStep(ApprovalStatus.APPROVED.getCode());

        // result
    	MessageCommEnum result = claimCompleteProcessService.putApprovalProcessCsRefund(approvalVo);

        // equals
        Assertions.assertTrue(BaseEnums.Default.SUCCESS.equals(result));
    }

    @Test
    void addClaimStatusHistory_등록() throws Exception {
    	ApprovalStatusVo approvalVo = new ApprovalStatusVo();
    	approvalVo.setTaskPk("0");
    	approvalVo.setApprStat(ApprovalStatus.APPROVED.getCode());
    	approvalVo.setApprStep(ApprovalStatus.APPROVED.getCode());

        // result
    	int result = claimCompleteProcessService.addClaimStatusHistory(approvalVo);

        // equals
        Assertions.assertTrue(result > 0);
    }

    @Test
    void 반품_클레임_정보_업데이트() {

        OrderClaimRegisterRequestDto reqDto = new OrderClaimRegisterRequestDto();
        reqDto.setOdClaimId(1);
        reqDto.setTargetTp(OrderClaimEnums.ClaimTargetTp.TARGET_BUYER.getCode());
        reqDto.setReturnsYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());
        reqDto.setDirectPaymentYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());
        reqDto.setRefundPrice(0);
        reqDto.setRefundPointPrice(0);

        int updateCnt = claimCompleteProcessService.putOrderClaimTargetInfo(reqDto);

        // equals
        Assertions.assertTrue(updateCnt > 0);
    }
}