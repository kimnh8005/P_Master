package kr.co.pulmuone.bos.user.noti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.noti.dto.UserNotiDto;
import kr.co.pulmuone.v1.user.noti.service.UserNotiBiz;

@Service
public class UserNotiBosServiceImpl implements UserNotiBosService {

	@Autowired
	private UserNotiBiz userNotiBiz;

	/**
	 * 회원 알림 안읽은 정보 여부
	 */
	@Override
	public ApiResult<?> isNotReadNoti() throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		boolean check = false;
		if (userVo != null) {
			check = userNotiBiz.isNotReadNoti(new Long(userVo.getUserId()));
		}
		return ApiResult.success(check);
	}

	@Override
	public ApiResult<?> getNotiListByUser() throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		List<UserNotiDto> list = null;
		if (userVo != null) {
			list = userNotiBiz.getNotiListByUser(new Long(userVo.getUserId()));
		}
		return ApiResult.success(list);
	}

	@Override
	public ApiResult<?> putNotiRead(List<Long> urNotiIds) throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		if (userVo != null) {
			userNotiBiz.putNotiRead(new Long(userVo.getUserId()), urNotiIds);
		}
		return ApiResult.success();
	}

	@Override
	public ApiResult<?> putNotiReadClick(Long urNotiId) throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		if (userVo != null) {
			userNotiBiz.putNotiReadClick(new Long(userVo.getUserId()), urNotiId);
		}
		return ApiResult.success();
	}

}
