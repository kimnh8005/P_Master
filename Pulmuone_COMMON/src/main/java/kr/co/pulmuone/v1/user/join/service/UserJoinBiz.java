package kr.co.pulmuone.v1.user.join.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.AddBuyerRequestDto;
import kr.co.pulmuone.v1.user.join.dto.GetIsCheckLoginIdRequestDto;
import kr.co.pulmuone.v1.user.join.dto.GetIsCheckMailRequestDto;
import kr.co.pulmuone.v1.user.join.dto.GetIsCheckRecommendLoginIdRequestDto;
import kr.co.pulmuone.v1.user.join.dto.SaveBuyerRequestDto;
import kr.co.pulmuone.v1.user.join.dto.vo.AccountVo;
import kr.co.pulmuone.v1.user.join.dto.vo.JoinResultVo;
import kr.co.pulmuone.v1.user.join.dto.vo.UserVo;

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
 *  1.0    20200617   	 김경민            최초작성
 * =======================================================================
 * </PRE>
 */
public interface UserJoinBiz {

    // 아이디중복확인
    ApiResult<?> getIsCheckLoginId(GetIsCheckLoginIdRequestDto getIsCheckLoginIdRequestDto) throws Exception;

    ApiResult<?> getIsCheckMail(GetIsCheckMailRequestDto getIsCheckMailResponseDto) throws Exception;

    ApiResult<?> getIsCheckRecommendLoginId(GetIsCheckRecommendLoginIdRequestDto getIsCheckRecommendLoginIdRequestDto) throws Exception;

    ApiResult<?> addBuyer(AddBuyerRequestDto addBuyerRequestDto) throws Exception;

    boolean isCheckUnderAge14(String birthday) throws Exception;

    int addUrClauseAgreeLog(SaveBuyerRequestDto saveBuyerRequestDto) throws Exception;

    int putUrAccount(String urUserId) throws Exception;

    UserVo getUserInfo(String loginId);

    int addUser(UserVo userVo) throws Exception;

    int addAccount(AccountVo accountVo) throws Exception;

    int putUser(UserVo userVo) throws Exception;

    ApiResult<?> asisUserCheck(String loginId, String password, String captcha, String siteno) throws Exception;

    JoinResultVo getJoinCompletedInfo(String urUserId);

    void getSignUpCompleted(JoinResultVo infoJoinResultVo);


}
