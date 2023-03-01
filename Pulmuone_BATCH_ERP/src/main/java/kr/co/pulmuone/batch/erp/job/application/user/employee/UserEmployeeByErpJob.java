package kr.co.pulmuone.batch.erp.job.application.user.employee;

import kr.co.pulmuone.batch.erp.domain.service.user.employee.UserEmployeeByErpBatchBiz;
import kr.co.pulmuone.batch.erp.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userEmployeeByErpJob")
@Slf4j
@RequiredArgsConstructor
public class UserEmployeeByErpJob implements BaseJob {

    @Autowired
    private UserEmployeeByErpBatchBiz userEmployeeByErpBatchBiz;

    @Override
    public void run(String[] params) {
    	userEmployeeByErpBatchBiz.synchEmployeeByErp();
    }
}
