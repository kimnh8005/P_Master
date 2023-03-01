package kr.co.pulmuone.v1.comm.mapper.user.join;

import kr.co.pulmuone.v1.user.buyer.dto.AddBuyerRequestDto;
import kr.co.pulmuone.v1.user.join.dto.*;
import kr.co.pulmuone.v1.user.join.dto.vo.*;
import kr.co.pulmuone.v1.user.login.dto.LoginRequestDto;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserJoinMapper
{

	int getIsCheckLoginId(GetIsCheckLoginIdRequestDto getIsCheckLoginIdRequestDto) throws Exception;

	GetIsCheckMailUserResultVo getIsCheckMail(GetIsCheckMailRequestDto getIsCheckMailRequestDto) throws Exception;

	GetIsCheckRecommendLoginIdResultVo getIsCheckRecommendLoginId(GetIsCheckRecommendLoginIdRequestDto getIsCheckRecommendLoginIdRequestDto) throws Exception;

	int addUrUser(SaveBuyerRequestDto saveBuyerRequestDto) throws Exception;

	int addUrBuyer(SaveBuyerRequestDto saveBuyerRequestDto) throws Exception;

	int addUrCertification(SaveBuyerRequestDto saveBuyerRequestDto) throws Exception;

	int addUrClauseAgreeLog(SaveBuyerRequestDto saveBuyerRequestDto) throws Exception;

	int addUrShippingAddr(AddUrShippingAddrRequestDto addUrShippingAddrRequestDto) throws Exception;

	int putUrAccount(String urUserId) throws Exception;

	int addUrAccount(String urUserId) throws Exception;

	/**
	 * @Desc 회원기본정보 조회
	 * @param loginId
	 * @return UserVo
	 */
	UserVo getUserInfo(@Param("loginId") String loginId);

	/**
	 * @Desc 회원기본정보 등록
	 * @param userVo
	 * @return int
	 */
	int addUser(UserVo userVo) throws Exception;

	/**
	 * @Desc 회원정보활동정보 등록
	 * @param accountVo
	 * @throws Exception
	 * @return int
	 */
	int addAccount(AccountVo accountVo) throws Exception;

	/**
	 * @Desc 회원기본정보 수정
	 * @param userVo
	 * @throws Exception
	 * @return int
	 */
	int putUser(UserVo userVo) throws Exception;

	/**
	 * @Desc 회원가입완료 시 정보조회
	 * @param urUserId
	 * @return JoinResultVo
	 */
	JoinResultVo getJoinCompletedInfo(String urUserId);

}
