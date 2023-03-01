package kr.co.pulmuone.batch.erp.infra.mapper.sample.master;

import kr.co.pulmuone.batch.erp.domain.model.sample.SampleMasterVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SampleMasterMapper {

    List<SampleMasterVo> getList();

    SampleMasterVo get(long no);
}
