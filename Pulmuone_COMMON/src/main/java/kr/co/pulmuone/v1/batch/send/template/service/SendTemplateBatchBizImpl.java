package kr.co.pulmuone.v1.batch.send.template.service;

import java.io.IOException;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.send.template.service.dto.AddEmailIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddSmsIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.GetEmailSendBatchResponseDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.vo.GetEmailSendBatchResultVo;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;

@Service
public class SendTemplateBatchBizImpl implements SendTemplateBatchBiz{

	@Value("${resource.server.url.bos}")
	private String serverUrlBos;

	@Autowired
    private SendTemplateBatchService sendTemplateBatchService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void addEmailIssueSelect(AddEmailIssueSelectBatchRequestDto dto) {
		sendTemplateBatchService.addEmailIssueSelect(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void addSmsIssueSelect(AddSmsIssueSelectBatchRequestDto addSmsIssueSelectBatchRequestDto) {
    	sendTemplateBatchService.addSmsIssueSelect(addSmsIssueSelectBatchRequestDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public GetEmailSendBatchResponseDto getSendTemplateByCode(String templateCode) {
        return sendTemplateBatchService.getSendTemplateByCode(templateCode);
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public String getPsValue(String psKey) {
		return sendTemplateBatchService.getPsValue(psKey);
	}


    /**
     * @Desc SMS 내용 받기 --> 확정 후 개발 필요 임시
     * @param getEmailSendResultVo
     * @param vo
     * @return String
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public String getSMSTmplt(GetEmailSendBatchResultVo getEmailSendBatchResultVo, Object vo) {
    	String smsBody = getTmpltContext(getEmailSendBatchResultVo.getSmsBody(), vo);

    	//정책 확인 후 SMS 발송 로직 개발 필요
        return smsBody;
    }

    /**
     * @Desc 이메일sms 치환 데이터 조회
     * @param Context
     * @param vo
     * @return
     */
    public String getTmpltContext(String Context, Object vo) {

		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(Velocity.RESOURCE_LOADER, "string");
	    engine.addProperty("string.resource.loader.class", StringResourceLoader.class.getName());
	    engine.addProperty("string.resource.loader.repository.static", "false");
	    engine.init();

	    // Initialize my template repository. You can replace the "Hello $w" with your String.
	    StringResourceRepository repo = (StringResourceRepository) engine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
	    repo.putStringResource("template", Context);

	    // Get and merge the template with my parameters.(치환)
	    Template template = engine.getTemplate("template");

	    VelocityContext context = new VelocityContext();
	    context.put("data", vo);	//변경하려는 데이터릉 vo 에 담아 넘긴다 (사용시 ex> userId = data.getUserId)
		context.put("number", new NumberTool());

	    StringWriter writer = new StringWriter();
	    template.merge(context, writer);

        return writer.toString();
    }

    public String getDomainManagement() {
    	return serverUrlBos.replace("https://", "http://");
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void runSendSms() throws Exception {
		sendTemplateBatchService.runSendSms();
	}

}
