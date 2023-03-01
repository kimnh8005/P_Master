package kr.co.pulmuone.batch.esl.domain.service.sample;

import kr.co.pulmuone.batch.esl.domain.model.sample.SampleMasterVo;
import kr.co.pulmuone.batch.esl.infra.mapper.sample.master.SampleMasterMapper;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SampleMasterServiceImpl implements SampleMasterService {

    @Autowired
    private final SampleMasterMapper mapper;

    @Override
    public List<SampleMasterVo> getList() {
        return mapper.getList();
    }

    @Override
    public SampleMasterVo findByNo(long no) {
        return mapper.get(no);
    }
}