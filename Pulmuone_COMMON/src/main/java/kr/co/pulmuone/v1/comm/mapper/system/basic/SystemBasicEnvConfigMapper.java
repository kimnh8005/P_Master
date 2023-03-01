package kr.co.pulmuone.v1.comm.mapper.system.basic;

import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetLangListResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemBasicEnvConfigMapper {
    List<GetEnvListResultVo> getEnvList();

    List<GetLangListResultVo> getLangList();
}
