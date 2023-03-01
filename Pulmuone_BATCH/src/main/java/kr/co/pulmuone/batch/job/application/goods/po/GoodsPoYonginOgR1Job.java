package kr.co.pulmuone.batch.job.application.goods.po;

import kr.co.pulmuone.v1.comm.api.constant.TransferServerTypes;
import kr.co.pulmuone.v1.comm.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.batch.goods.po.BatchGoodsPurchaseOrderBiz;
import kr.co.pulmuone.v1.comm.enums.PoEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("goodsPoYonginOgR1Job")
@Slf4j
@RequiredArgsConstructor
public class GoodsPoYonginOgR1Job implements BaseJob {

	 @Autowired
     private BatchGoodsPurchaseOrderBiz batchGoodsPurchaseOrderBiz;

     @Override
     public void run(String[] params) {

        try {

        	batchGoodsPurchaseOrderBiz.addOghGoodsPurchaseOrderJob(PoEnums.PoBatchType.PO_BATCH_TP_YONGIN_OG_R1.getCode(), TransferServerTypes.OGH.getCode());

        } catch (BaseException e) {

            log.error(e.getMessage(), e);

        }

     }

}
