package kr.co.pulmuone.v1.comm.mappers.batch.master.system;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemAuthBatchMapper {

    List<Long> getSystemProgramUserList(String pgId);

}
