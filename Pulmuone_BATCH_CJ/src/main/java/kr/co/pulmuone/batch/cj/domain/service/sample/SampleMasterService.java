package kr.co.pulmuone.batch.cj.domain.service.sample;

import kr.co.pulmuone.batch.cj.domain.model.sample.SampleMasterVo;

import java.util.List;

public interface SampleMasterService {

    List<SampleMasterVo> getList();

    SampleMasterVo findByNo(long no);
}
