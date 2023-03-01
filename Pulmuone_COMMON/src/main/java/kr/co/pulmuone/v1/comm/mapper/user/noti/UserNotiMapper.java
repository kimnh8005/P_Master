package kr.co.pulmuone.v1.comm.mapper.user.noti;

import java.util.List;

import kr.co.pulmuone.v1.user.noti.dto.UserNotiRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.user.noti.dto.UserNotiDto;

@Mapper
@SuppressWarnings("rawtypes")
public interface UserNotiMapper {
	boolean isNotReadNoti(long urUserId) throws Exception;

	List<UserNotiDto> getNotiListByUser(Long urUserId) throws Exception;

	void putNotiRead(@Param("urUserId") Long urUserId, @Param("urNotiIds") List<Long> urNotiIds) throws Exception;

	void putNotiReadClick(@Param("urUserId") Long urUserId, @Param("urNotiId") Long urNotiId) throws Exception;

	void addNoti(UserNotiRequestDto dto) throws Exception;

	List<Long> getUserListByBBS(String bosBbsType) throws Exception;
}
