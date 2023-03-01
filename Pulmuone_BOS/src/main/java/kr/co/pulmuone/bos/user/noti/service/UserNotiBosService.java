package kr.co.pulmuone.bos.user.noti.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;

public interface UserNotiBosService {

	ApiResult<?> isNotReadNoti() throws Exception;

	ApiResult<?> getNotiListByUser() throws Exception;

	ApiResult<?> putNotiRead(List<Long> urNotiIds) throws Exception;

	ApiResult<?> putNotiReadClick(Long urNotiId) throws Exception;

}
