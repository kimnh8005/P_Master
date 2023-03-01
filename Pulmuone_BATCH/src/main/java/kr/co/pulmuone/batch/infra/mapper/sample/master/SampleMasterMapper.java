package kr.co.pulmuone.batch.infra.mapper.sample.master;

import kr.co.pulmuone.batch.domain.model.sample.SampleMasterVo;
import kr.co.pulmuone.batch.domain.model.system.BatchJob;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SampleMasterMapper {

    List<BatchJob> getList();

    SampleMasterVo get(long no);
}
