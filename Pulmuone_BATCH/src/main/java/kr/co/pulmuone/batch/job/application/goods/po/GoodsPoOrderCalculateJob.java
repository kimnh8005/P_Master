package kr.co.pulmuone.batch.job.application.goods.po;

import kr.co.pulmuone.v1.comm.api.constant.TransferServerTypes;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.batch.goods.po.BatchGoodsPurchaseOrderBiz;
import kr.co.pulmuone.v1.comm.enums.PoEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("GoodsPoOrderCalculate")
@Slf4j
@RequiredArgsConstructor
public class GoodsPoOrderCalculateJob implements BaseJob {

	 @Autowired
     private BatchGoodsPurchaseOrderBiz batchGoodsPurchaseOrderBiz;

     @Override
     public void run(String[] params) {
        try {

    		LocalDate now = LocalDate.now();//현재일짜

            String baseDt = now.toString().replaceAll("-", "");//조회기간
        	batchGoodsPurchaseOrderBiz.addGoodsPoOrderCalculate(baseDt);

        } catch (BaseException e) {

            log.error(e.getMessage(), e);

        }
     }

}