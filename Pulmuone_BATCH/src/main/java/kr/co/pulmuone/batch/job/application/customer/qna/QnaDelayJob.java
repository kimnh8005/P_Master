package kr.co.pulmuone.batch.job.application.customer.qna;

import kr.co.pulmuone.v1.batch.customer.qna.CustomerQnaBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("qnaDelayJob")
@Slf4j
public class QnaDelayJob implements BaseJob {

    @Autowired
    private CustomerQnaBatchBiz customerQnaBatchBiz;

    @Override
    public void run(String[] params) {
        customerQnaBatchBiz.runQnaDelay();
    }

}
