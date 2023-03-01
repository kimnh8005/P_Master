package kr.co.pulmuone.v1.comm.mapper.user.login;

import kr.co.pulmuone.v1.user.login.dto.EmployeeResponseDto;
import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.user.login.dto.LoginDto;
import kr.co.pulmuone.v1.user.login.dto.LoginRequestDto;
import kr.co.pulmuone.v1.user.login.dto.vo.LoginResultVo;
import kr.co.pulmuone.v1.user.login.dto.vo.RecentlyLoginResultVo;

@Mapper
public interface UserLoginBosMapper {

	public int loginInfo(LoginDto dto) throws Exception;

	public String loginUrUserId(LoginDto dto) throws Exception;

	UserVo getEmpInfo(LoginRequestDto loginRequestDto);

	UserVo hasBosLoginData(LoginRequestDto loginRequestDto);

	int putFailCountIncr(LoginRequestDto loginRequestDto);

	LoginResultVo getLoginInfoById(LoginRequestDto loginRequestDto);

	int putUserStatusTp(LoginRequestDto loginRequestDto);

	int putUrAccountLastLogin(LoginRequestDto loginRequestDto);

	int putFailCountReset(LoginRequestDto loginRequestDto);

	int addConnectionLog(LoginRequestDto loginRequestDto);

	LoginResultVo getPasswordByData(LoginRequestDto loginRequestDto);

	int putPasswordChange(LoginRequestDto loginRequestDto);

	LoginResultVo getPasswordByPassword(LoginRequestDto loginRequestDto);

	int putConnectionLogBylogOut(LoginRequestDto loginRequestDto);

	int addUrPwdChgHist(LoginResultVo loginResultVo);

	int getUrPwdChgHistByNewPwd(LoginRequestDto loginRequestDto);

	RecentlyLoginResultVo getRecentlyLoginData(String userId);

	EmployeeResponseDto getEmployeeContactInfo(String userId);
}
