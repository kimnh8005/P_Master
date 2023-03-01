package kr.co.pulmuone.batch.job.application.collectionmall.outmall;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.outmall.OutmallExcelOrderBiz;
import kr.co.pulmuone.v1.comm.enums.OutmallEnums;
import lombok.extern.slf4j.Slf4j;

/**
 * BATCH ID : 77
 */
@Component("orderExcelInfoJob")
@Slf4j
public class OrderExcelInfoJob implements BaseJob {

    @Autowired
    private OutmallExcelOrderBiz outmallExcelOrderBiz;

    @Override
    public void run(String[] params) {

        log.info("======"+OutmallEnums.OutmallBatchTypeCd.CREATE_ORDER.getCodeName()+"======");

        try {

            int maxThreadCnt = 1;      // 기본값 1개
            String targetStatusList = "";

            if(params.length == 3 && params[2] != null && params[2] != "" && StringUtils.isNumeric(params[2]) && params[2] != "0") {
                maxThreadCnt = Integer.parseInt(params[2]);
            }

            if(params.length == 4 && params[3] != null) {
                targetStatusList = params[3];
            }

             outmallExcelOrderBiz.putOutmallCreateOrder(maxThreadCnt, targetStatusList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("=========orderExcelInfoJob error============");
        }
    }

}