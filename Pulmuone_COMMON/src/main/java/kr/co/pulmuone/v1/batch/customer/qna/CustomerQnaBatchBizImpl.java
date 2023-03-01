package kr.co.pulmuone.v1.batch.customer.qna;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CustomerQnaBatchBizImpl implements CustomerQnaBatchBiz {

    @Autowired
    private CustomerQnaBatchService customerQnaBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runQnaDelay() {
        customerQnaBatchService.runQnaDelay();
    }

}
