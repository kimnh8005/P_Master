package kr.co.pulmuone.batch.cj.domain.service.send.template;

public interface BatchSendTemplateService {

    void sendSmsBatch(Long batchNo, String content);

}
