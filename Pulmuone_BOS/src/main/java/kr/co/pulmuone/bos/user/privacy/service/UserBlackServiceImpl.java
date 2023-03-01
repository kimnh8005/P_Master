package kr.co.pulmuone.bos.user.privacy.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBlackListBiz;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackHistoryListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackHistoryListResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackListResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 회원관리 - 블랙리스트 회원 ServiceImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200625    	  박영후           최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class UserBlackServiceImpl implements UserBlackService {

    @Autowired
    private UserBuyerBlackListBiz userBuyerBlackListBiz;


    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public GetUserBlackListResponseDto getBlackListUserList(GetUserBlackListRequestDto dto) throws Exception {
        return userBuyerBlackListBiz.getBlackListUserList(dto);
    }

    @Override
    @UserMaskingRun(system = "BOS")
    public GetUserBlackHistoryListResponseDto getUserBlackHistoryList(GetUserBlackHistoryListRequestDto dto) throws Exception {
        return userBuyerBlackListBiz.getUserBlackHistoryList(dto);
    }
}
