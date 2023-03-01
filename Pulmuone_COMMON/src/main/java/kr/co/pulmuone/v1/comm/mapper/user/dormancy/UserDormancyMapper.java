package kr.co.pulmuone.v1.comm.mapper.user.dormancy;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.user.dormancy.dto.CommonPutUserDormantRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantHistoryListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserDormantListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetBuyerMoveInfoByCiVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetIsCheckUserMoveResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserDormantHistoryListResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserDormantListResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.UserDormancyResultVo;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDormancyMapper {

	void putUserActive(CommonPutUserDormantRequestDto dto);

	void addUserBuyerActive(CommonPutUserDormantRequestDto dto);

	void putUrUserMoveLog(CommonPutUserDormantRequestDto dto);

	void delUrBuyerMove(CommonPutUserDormantRequestDto dto);

	void delUrUserMove(CommonPutUserDormantRequestDto dto);

	GetIsCheckUserMoveResultVo isCheckUserMove(String urUserId) throws Exception;

	GetBuyerMoveInfoByCiVo getBuyerMoveInfoByCI(String ciCd) throws Exception;

	int getUserDormantListCount(GetUserDormantListRequestDto dto) throws Exception;

	Page<GetUserDormantListResultVo> getUserDormantList(GetUserDormantListRequestDto dto) throws Exception;

	int getUserDormantHistoryListCount(GetUserDormantHistoryListRequestDto dto) throws Exception;

	Page<GetUserDormantHistoryListResultVo> getUserDormantHistoryList(GetUserDormantHistoryListRequestDto dto) throws Exception;

	UserDormancyResultVo getUserDormancyInfo(Long urUserId);

	UserDormancyResultVo getUserDormancyExpected(Long urUserId);

}
