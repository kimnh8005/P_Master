package kr.co.pulmuone.v1.user.noti.service;

import java.util.List;

import kr.co.pulmuone.v1.user.noti.dto.UserNotiDto;
import kr.co.pulmuone.v1.user.noti.dto.UserNotiRequestDto;

public interface UserNotiBiz {

	/**
	 * 회원 알림 안읽은 정보 여부
	 */
	boolean isNotReadNoti(Long urUserId) throws Exception;

	List<UserNotiDto> getNotiListByUser(Long urUserId) throws Exception;

	void putNotiRead(Long urUserId, List<Long> urNotiIds) throws Exception;

	void putNotiReadClick(Long urUserId, Long urNotiId) throws Exception;

	void addNoti(UserNotiRequestDto dto) throws Exception;

	List<Long> getUserListByBBS(String bosBbsType) throws Exception;
}
