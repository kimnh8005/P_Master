package kr.co.pulmuone.batch.cj.infra.mapper.sample.master;

import kr.co.pulmuone.batch.cj.domain.model.sample.SampleMasterVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SampleMasterMapper {

    List<SampleMasterVo> getList();

    SampleMasterVo get(long no);
}
