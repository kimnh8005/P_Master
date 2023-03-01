package kr.co.pulmuone.batch.eon.domain.service.send.template;

public interface BatchSendTemplateService {

    void sendSmsBatch(Long batchNo, String content);

}
