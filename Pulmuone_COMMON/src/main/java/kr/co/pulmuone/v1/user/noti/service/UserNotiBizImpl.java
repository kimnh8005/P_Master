package kr.co.pulmuone.v1.user.noti.service;

import java.util.List;

import kr.co.pulmuone.v1.user.noti.dto.UserNotiRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.user.noti.dto.UserNotiDto;

@Service
public class UserNotiBizImpl implements UserNotiBiz {

	@Autowired
	UserNotiService userNotiService;

	@Override
	public boolean isNotReadNoti(Long urUserId) throws Exception {
		return userNotiService.isNotReadNoti(urUserId);
	}

	@Override
	public List<UserNotiDto> getNotiListByUser(Long urUserId) throws Exception {
		return userNotiService.getNotiListByUser(urUserId);
	}

	@Override
	public void putNotiRead(Long urUserId, List<Long> urNotiIds) throws Exception {
		userNotiService.putNotiRead(urUserId, urNotiIds);
	}

	@Override
	public void putNotiReadClick(Long urUserId, Long urNotiId) throws Exception {
		userNotiService.putNotiReadClick(urUserId, urNotiId);
	}

	@Override
	public void addNoti(UserNotiRequestDto dto) throws Exception {
		userNotiService.addNoti(dto);
	}

	@Override
	public List<Long> getUserListByBBS(String bosBbsType) throws Exception {
		return userNotiService.getUserListByBBS(bosBbsType);
	}
}
