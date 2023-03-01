package kr.co.pulmuone.batch.erp.domain.service.send.template;

public interface BatchSendTemplateService {

    void sendSmsBatch(Long batchNo, String content);

}
