package kr.co.pulmuone.batch.job.application.customer.feedback;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.customer.feedback.CustomerFeedbackBatchBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("feedbackTotalJob")
@Slf4j
public class FeedbackTotalJob implements BaseJob {

    @Autowired
    private CustomerFeedbackBatchBiz customerFeedbackBatchBiz;

    @Override
    public void run(String[] params) {
        customerFeedbackBatchBiz.runFeedbackTotal();
    }

}
