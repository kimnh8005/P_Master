package kr.co.pulmuone.v1.comm.mappers.batch.master.send;

import kr.co.pulmuone.v1.batch.send.template.service.dto.vo.SmsSendBatchResultVo;
import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.batch.send.template.service.dto.AddEmailIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddEmailManualBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddSmsIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddSmsManualBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.vo.GetEmailSendBatchResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SendTemplateBatchMapper {

	int addEmailManual(AddEmailManualBatchRequestDto addEmailManualBatchRequestDto);

	int addEmailIssue(AddEmailIssueSelectBatchRequestDto addEmailIssueSelectBatchRequestDto);

	int addSmsManual(AddSmsManualBatchRequestDto addSmsManualBatchRequestDto);

	int addSmsIssue(AddSmsIssueSelectBatchRequestDto addSmsIssueSelectBatchRequestDto);

	GetEmailSendBatchResultVo getSendTemplateByCode(String templateCode);

	String getPsValue(String psKey);

	List<SmsSendBatchResultVo> getSmsSend();

	void putPushSend(@Param("smsSendList") List<SmsSendBatchResultVo> smsSendList, @Param("senderTelephone") String senderTelephone);

}
