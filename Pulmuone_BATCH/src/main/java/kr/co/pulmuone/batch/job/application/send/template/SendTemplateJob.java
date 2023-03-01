package kr.co.pulmuone.batch.job.application.send.template;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.send.template.service.SendTemplateBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component("sendTemplateJob")
@Slf4j
public class SendTemplateJob implements BaseJob {

    @Autowired
    private SendTemplateBatchBiz sendTemplateBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {
        try {
            sendTemplateBatchBiz.runSendSms();
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }
}
