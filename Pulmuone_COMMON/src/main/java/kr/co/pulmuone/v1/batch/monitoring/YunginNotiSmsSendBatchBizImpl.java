package kr.co.pulmuone.v1.batch.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class YunginNotiSmsSendBatchBizImpl implements YunginNotiSmsSendBatchBiz {

    @Autowired
    private YunginNotiSmsSendBatchService yunginNotiSmsSendBatchService;

    /**
     * 용인물류 D1, D2출고 주문 택배, 새벽을 기준으로 주문건수(냉동건수), 상품수량으로 SMS발송
     * @param stCommonCodeMasterId
     * @return void
     * @throws Exception
     */
    @Override
    public void yunginNotiSmsSend(String stCommonCodeMasterId) throws Exception {
        int orderIfDate = 2;  //주문 IF일자 기준값 (ex: 해당값이 2면 D1, D2 주문을 조회한다)

        yunginNotiSmsSendBatchService.execYunginNotiSmsSend(orderIfDate, stCommonCodeMasterId);
    }
}
