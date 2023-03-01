package kr.co.pulmuone.batch.job.application.order.inside;


import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.inside.ClaimExcelUploadBiz;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("claimExcelUploadJob")
@Slf4j
public class ClaimExcelUploadJob implements BaseJob {

    @Autowired
    private ClaimExcelUploadBiz claimExcelUploadBiz;

    @Override
    public void run(String[] params) throws Exception {

        log.debug("=========================== " + OrderClaimEnums.OrderClaimBatchTypeCd.CREATE_CLAIM.getCodeName() + " ===========================");
        claimExcelUploadBiz.putClaimExcelUpload();

    }

}
