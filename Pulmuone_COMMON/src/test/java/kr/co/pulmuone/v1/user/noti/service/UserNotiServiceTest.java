package kr.co.pulmuone.v1.user.noti.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.pulmuone.v1.user.noti.dto.UserNotiRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.CompanyEnums.BosBBSType;
import kr.co.pulmuone.v1.comm.enums.UserEnums.UserNotiType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotiServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	private UserNotiService userNotiService;

	@Test
	void isNotReadNoti_쿼리이상없음() throws Exception {
		userNotiService.isNotReadNoti(1L);
	}

	@Test
	void getNotiListByUser_쿼리이상없음() throws Exception {
		userNotiService.getNotiListByUser(1L);
	}

	@Test
	void putNotiRead_쿼리이상없음() throws Exception {
		List<Long> list = new ArrayList<Long>();
		list.add(1L);
		list.add(2L);
		userNotiService.putNotiRead(1L, list);
	}

	@Test
	void addNoti_쿼리이상없음() throws Exception {
		//given
		UserNotiRequestDto dto = UserNotiRequestDto.builder()
				.urUserIdList(Arrays.asList(1L, 2L))
				.userNotiType(UserNotiType.BOS_NOTI.getCode())
				.notiMessage(UserNotiType.BOS_NOTI.getMsg())
				.build();

		//when, then
		userNotiService.addNoti(dto);
	}

	@Test
	void getUserListByBBS_쿼리이상없음() throws Exception {
		userNotiService.getUserListByBBS(BosBBSType.HEADQUARTERS.getCode());
	}

	@Test
	void putNotiReadClick_정상() throws Exception {
		//given
		Long urUserId = 1L;
		Long urNotiId = 1L;

		//when, then
		userNotiService.putNotiReadClick(urUserId, urNotiId);
	}

}
