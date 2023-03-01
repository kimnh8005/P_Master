package kr.co.pulmuone.v1.comm.mapper.system.itgc;

import kr.co.pulmuone.v1.system.itgc.dto.ItgcRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemItgcMapper {

    int addItgcList(@Param("insertList") List<ItgcRequestDto> insertList);

}
