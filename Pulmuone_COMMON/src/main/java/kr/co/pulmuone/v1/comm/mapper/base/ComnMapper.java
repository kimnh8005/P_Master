package kr.co.pulmuone.v1.comm.mapper.base;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.MenuUrlResultVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;


@Mapper
public interface ComnMapper {


	/**
	 * 나의 최근 로그인 시퀀스 받기
	 * @param userVo
	 * @return
	 * @throws Exception
	 */
	UserVo hasSessionInfoByLoginId(UserVo userVo);

	MenuUrlResultVo getMenuUrlDataByUrl(@Param("systemMenuId") String systemMenuId, @Param("url") String url);

	int addMenuOperLog(MenuOperLogRequestDto menuOperLogRequestDto);

	int addPrivacyMenuOperLog(PrivacyMenuOperLogRequestDto privacyMenuOperLogRequestDto);
}
