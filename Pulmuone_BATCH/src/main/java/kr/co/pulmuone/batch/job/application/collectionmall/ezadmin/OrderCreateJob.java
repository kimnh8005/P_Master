package kr.co.pulmuone.batch.job.application.collectionmall.ezadmin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.ezadmin.EZAdminOrderBiz;
import kr.co.pulmuone.v1.comm.enums.EZAdminEnums;
import lombok.extern.slf4j.Slf4j;

@Component("orderCreateJob") // Batch Id : 73
@Slf4j
public class OrderCreateJob implements BaseJob {

    @Autowired
    private EZAdminOrderBiz ezadminOrderBiz;

    @Override
    public void run(String[] params) {

        log.info("======"+EZAdminEnums.EZAdminBatchTypeCd.CREATE_ORDER.getCodeName()+"======");

    	try {

    		ezadminOrderBiz.putEZAdminCreateOrder();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("=========OrderInfoJob error============");
		}
    }

}
