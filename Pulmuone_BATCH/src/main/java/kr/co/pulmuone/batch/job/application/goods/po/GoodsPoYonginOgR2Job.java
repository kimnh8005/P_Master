package kr.co.pulmuone.batch.job.application.goods.po;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.po.BatchGoodsPurchaseOrderBiz;
import kr.co.pulmuone.v1.comm.api.constant.TransferServerTypes;
import kr.co.pulmuone.v1.comm.enums.PoEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("goodsPoYonginOgR2Job")
@Slf4j
@RequiredArgsConstructor
public class GoodsPoYonginOgR2Job implements BaseJob {

	 @Autowired
     private BatchGoodsPurchaseOrderBiz batchGoodsPurchaseOrderBiz;

     @Override
     public void run(String[] params) {

        try {

        	batchGoodsPurchaseOrderBiz.addOghGoodsPurchaseOrderJob(PoEnums.PoBatchType.PO_BATCH_TP_YONGIN_OG_R2.getCode(), TransferServerTypes.OGH.getCode());

        } catch (BaseException e) {

            log.error(e.getMessage(), e);

        }

     }

}
