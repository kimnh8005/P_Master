package kr.co.pulmuone.batch.job.application.collectionmall.ezadmin;

import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.CollectionMallEZAdminBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("transNoJob")
@Slf4j
public class TransNoJob implements BaseJob {

    @Autowired
    private CollectionMallEZAdminBatchBiz collectionMallEZAdminBatchBiz;

    @Override
    public void run(String[] params) {
    	try {
            collectionMallEZAdminBatchBiz.runTransNo();
		} catch (Exception e) {
            e.printStackTrace();
			log.error("=========TransNoJob error============");
		}
    }

}
