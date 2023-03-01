package kr.co.pulmuone.batch.domain.service.sample;

import kr.co.pulmuone.batch.domain.model.sample.SampleMasterVo;
import kr.co.pulmuone.batch.domain.model.system.BatchJob;

import java.util.List;

public interface SampleMasterService {

    List<BatchJob> getList();

    SampleMasterVo findByNo(long no);
}
