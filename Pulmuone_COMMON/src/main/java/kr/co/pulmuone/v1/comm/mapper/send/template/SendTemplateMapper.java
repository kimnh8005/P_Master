package kr.co.pulmuone.v1.comm.mapper.send.template;

import kr.co.pulmuone.v1.send.template.dto.*;
import kr.co.pulmuone.v1.send.template.dto.vo.BatchSmsTargetVo;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendListResultVo;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SendTemplateMapper {

    int getEmailSendListCount(GetEmailSendListRequestDto vo);

    List<GetEmailSendListResultVo> getEmailSendList(GetEmailSendListRequestDto vo);

    GetEmailSendResultVo getEmailSend(Long snAutoSendId);

    int checkEmailSendDuplicate(Map<String, Object> parameterMap);

    int addEmailSend(AddEmailSendRequestDto dto);

    int putEmailSend(PutEmailSendRequestDto dto);

    void delEmailSend(Long snAutoSendId);

    int addEmailManual(AddEmailManualRequestDto dto);

    int addEmailIssue(AddEmailIssueSelectRequestDto dto);

    int addSmsManual(AddSmsManualRequestDto dto);

    int addSmsIssue(AddSmsIssueSelectRequestDto dto);

    GetEmailSendResultVo getSendTemplateByCode(String templateCode);

	String getPsValue(String psKey);

	List<BatchSmsTargetVo> getBatchSmsTargetList(Long batchNo);

	int isOverlapSendMailForGoodsDelivery(AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto);

    int isOverlapSendSmsForGoodsDelivery(AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto);

}
