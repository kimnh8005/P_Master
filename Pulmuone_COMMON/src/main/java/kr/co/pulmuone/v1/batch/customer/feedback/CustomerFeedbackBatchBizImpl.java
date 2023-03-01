package kr.co.pulmuone.v1.batch.customer.feedback;

import kr.co.pulmuone.v1.batch.customer.feedback.dto.FeedbackTotalBatchRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerFeedbackBatchBizImpl implements CustomerFeedbackBatchBiz {

    @Autowired
    private CustomerFeedbackBatchService customerFeedbackBatchService;

    public void runFeedbackTotal() {
        //조회
        List<FeedbackTotalBatchRequestDto> batchList = customerFeedbackBatchService.runFeedbackTotal();
        if (batchList.isEmpty()) return;

        //저장 - 단건별 커밋
        for (FeedbackTotalBatchRequestDto dto : batchList) {
            customerFeedbackBatchService.addFeedbackTotal(dto);
        }

    }

}
