package kr.co.pulmuone.mall.user.certification.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserCertificationMallServiceImpl implements UserCertificationMallService {

    @Autowired
    UserCertificationBiz userCertificationBiz;

    @Override
    public ApiResult<?> getPasswordChangeCd() throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
        }

        return ApiResult.success(userCertificationBiz.getPasswordChangeCd(buyerVo.getUrUserId()));
    }

}