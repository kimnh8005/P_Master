package kr.co.pulmuone.v1.batch.goods.po;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <PRE>
 * 발주 기본데이터 생성 배치 Biz
 * - 매일 0시 30분에 기초데이터 생성 및 업데이트.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2021-09-06          남기승         최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class BatchGoodsPoBasicDataBizImpl implements BatchGoodsPoBasicDataBiz {

    @Autowired
    private BatchGoodsPoBasidDataService batchGoodsPoBasidDataService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public void runGoodsPoBasicDataJob(String baseDt) throws BaseException {
        batchGoodsPoBasidDataService.addGoodsPoBasidDataJob(baseDt);
    }
}
