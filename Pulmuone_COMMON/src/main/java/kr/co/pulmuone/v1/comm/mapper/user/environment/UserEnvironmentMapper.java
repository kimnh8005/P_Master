package kr.co.pulmuone.v1.comm.mapper.user.environment;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserEnvironmentMapper {

	/**
	 * PCID 추가
	 *
	 */
	void addPCID(@Param("urPcidCd")String urPcidCd, @Param("agent")String agent) throws Exception;

}
