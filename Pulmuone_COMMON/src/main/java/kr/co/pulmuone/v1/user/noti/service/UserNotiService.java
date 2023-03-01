package kr.co.pulmuone.v1.user.noti.service;

import java.util.List;

import kr.co.pulmuone.v1.user.noti.dto.UserNotiRequestDto;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.user.noti.UserNotiMapper;
import kr.co.pulmuone.v1.user.noti.dto.UserNotiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserNotiService {

	private final UserNotiMapper userNotiMapper;

	/**
	 * 회원 알림 안읽은 정보 여부
	 */
	protected boolean isNotReadNoti(Long urUserId) throws Exception {
		return userNotiMapper.isNotReadNoti(urUserId);
	}

	/**
	 * 회원 알림 조회
	 *
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	protected List<UserNotiDto> getNotiListByUser(Long urUserId) throws Exception {
		return userNotiMapper.getNotiListByUser(urUserId);
	}

	/**
	 * 회원 알림 읽음 처리 - 알람
	 *
	 * @param urUserId
	 * @param urNotiIds
	 * @throws Exception
	 */
	protected void putNotiRead(Long urUserId, List<Long> urNotiIds) throws Exception {
		userNotiMapper.putNotiRead(urUserId, urNotiIds);
	}

	/**
	 * 회원 알림 읽음 처리 - 내용
	 *
	 * @param urUserId
	 * @param urNotiIds
	 * @throws Exception
	 */
	protected void putNotiReadClick(Long urUserId, Long urNotiId) throws Exception {
		userNotiMapper.putNotiReadClick(urUserId, urNotiId);
	}
	
	/**
	 * 회원 알림
	 *
	 * @param dto UserNotiRequestDto
	 * @throws Exception
	 */
	protected void addNoti(UserNotiRequestDto dto) throws Exception {
		userNotiMapper.addNoti(dto);
	}

	/**
	 * 공지사항 대상 회원 조회
	 *
	 * @param bosBbsType
	 * @throws Exception
	 */
	protected List<Long> getUserListByBBS(String bosBbsType) throws Exception {
		return userNotiMapper.getUserListByBBS(bosBbsType);
	}
}
