package kr.co.pulmuone.bos.user.member.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListResponseDto;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerDropBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDropBosServiceImpl implements UserDropBosService {
    @Autowired
    UserBuyerDropBiz userBuyerDropBiz;

    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public GetUserDropListResponseDto getUserDropList(GetUserDropListRequestDto dto) throws Exception {
        return userBuyerDropBiz.getUserDropList(dto);
    }
}
