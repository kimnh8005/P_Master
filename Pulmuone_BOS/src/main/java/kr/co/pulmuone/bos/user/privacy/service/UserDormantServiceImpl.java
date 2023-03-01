package kr.co.pulmuone.bos.user.privacy.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantHistoryListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantHistoryListResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantListResponseDto;
import kr.co.pulmuone.v1.user.dormancy.service.UserDormancyBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200612    	  천혜현           최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class UserDormantServiceImpl implements UserDormantService {

	@Autowired
	private UserDormancyBiz userDormancyBiz;

	/**
	 * 휴면회원 리스트조회
	 * @param dto
	 * @return GetUserDormantListResponseDto
	 * @throws Exception
	 */
	@UserMaskingRun(system = "MUST_MASKING")
	public GetUserDormantListResponseDto getUserDormantList(GetUserDormantListRequestDto dto) throws Exception {
		return userDormancyBiz.getUserDormantList(dto);
	}

	/**
	 * 휴면회원 이력리스트조회
	 * @param dto
	 * @return GetUserDormantHistoryListResponseDto
	 * @throws Exception
	 */
	@UserMaskingRun(system = "MUST_MASKING")
	public GetUserDormantHistoryListResponseDto getUserDormantHistoryList(GetUserDormantHistoryListRequestDto dto) throws Exception {

		return userDormancyBiz.getUserDormantHistoryList(dto);
	}

}
