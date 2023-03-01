package kr.co.pulmuone.v1.comm.mapper.system.basic;

import java.util.List;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestSaveDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;

@Mapper
public interface SystemBasicEnvironmentMapper {

	int getEnvironmentListCount(GetEnvironmentListRequestDto vo);

	Page<GetEnvironmentListResultVo> getEnvironmentList(GetEnvironmentListRequestDto vo);

	void addEnvironment(List<SaveEnvironmentRequestSaveDto> voList);

	void putEnvironment(List<SaveEnvironmentRequestSaveDto> voList);

	void delEnvironment(List<SaveEnvironmentRequestSaveDto> voList);

	int checkEnvironmentDuplicate(List<String> voList);

	GetEnvironmentListResultVo getEnvironment(GetEnvironmentListRequestDto vo);

	int putEnvironmentEnvVal(SaveEnvironmentRequestSaveDto saveEnvironmentRequestSaveDto);

}
