package kr.co.pulmuone.batch.eon.domain.service.sample;

import kr.co.pulmuone.batch.eon.domain.model.sample.SampleMasterVo;

import java.util.List;

public interface SampleMasterService {

    List<SampleMasterVo> getList();

    SampleMasterVo findByNo(long no);
}
