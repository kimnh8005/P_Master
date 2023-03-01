package kr.co.pulmuone.batch.esl.domain.service.send.template;

public interface BatchSendTemplateService {

    void sendSmsBatch(Long batchNo, String content);

}
