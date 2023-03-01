package kr.co.pulmuone.batch.job.application.goods.po;

import kr.co.pulmuone.v1.comm.api.constant.TransferServerTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.batch.goods.po.BatchGoodsPurchaseOrderBiz;
import kr.co.pulmuone.v1.comm.enums.PoEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("goodsPoYonginPf1440Job")
@Slf4j
@RequiredArgsConstructor
public class GoodsPoYonginPf1440Job implements BaseJob {

	 @Autowired
     private BatchGoodsPurchaseOrderBiz batchGoodsPurchaseOrderBiz;

     @Override
     public void run(String[] params) {

        try {

        	batchGoodsPurchaseOrderBiz.addPffGoodsPurchaseOrderJob(PoEnums.PoBatchType.PO_BATCH_TP_YONGIN_PF_1440.getCode(), TransferServerTypes.PFF.getCode());

        } catch (BaseException e) {

            log.error(e.getMessage(), e);

        }

     }

}
