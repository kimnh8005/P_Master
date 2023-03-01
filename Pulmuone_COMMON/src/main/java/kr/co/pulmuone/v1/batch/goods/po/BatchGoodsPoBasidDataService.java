package kr.co.pulmuone.v1.batch.goods.po;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.po.BatchGoodsPoBasicDataMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchGoodsPoBasidDataService {

    @Autowired
    private BatchGoodsPoBasicDataMapper batchGoodsPoBasicDataMapper;

    // 기초데이터 생성
    protected void addGoodsPoBasidDataJob(String baseDt) {
        batchGoodsPoBasicDataMapper.addGoodsPoBasicData(baseDt);
    }
}
