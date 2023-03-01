package kr.co.pulmuone.batch.eon.infra.mapper.email.slaveEon;

import kr.co.pulmuone.batch.eon.domain.model.email.eon.dto.vo.EmailBosVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface EmailEonMapper {

    int insertForm(EmailBosVo vo);

    int insertList(EmailBosVo vo);

    int updateForm(EmailBosVo vo);

    List<HashMap<String, String>> getConnectionPoolList();
}
