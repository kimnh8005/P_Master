package kr.co.pulmuone.v1.batch.order.inside;

import kr.co.pulmuone.v1.batch.order.ifday.IfDayChangeExcelBiz;
import kr.co.pulmuone.v1.batch.order.ifday.IfDayChangeExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ClaimExcelUploadBizImpl implements ClaimExcelUploadBiz {

    @Autowired
    private ClaimExcelUploadService claimExcelUploadService;



	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void putClaimExcelUpload() throws Exception {
		claimExcelUploadService.putClaimExcelUpload();
	}
}