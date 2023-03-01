package kr.co.pulmuone.batch.esl.domain.service.sample;

import kr.co.pulmuone.batch.esl.domain.model.sample.SampleMasterVo;

import java.util.List;

public interface SampleMasterService {

    List<SampleMasterVo> getList();

    SampleMasterVo findByNo(long no);
}
