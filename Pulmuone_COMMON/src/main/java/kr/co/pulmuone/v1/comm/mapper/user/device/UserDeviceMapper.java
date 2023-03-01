package kr.co.pulmuone.v1.comm.mapper.user.device;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDeviceMapper
{

	int putMemberMapping(@Param("deviceId") String deviceId, @Param("urUserId") String urUserId) throws Exception;

}
