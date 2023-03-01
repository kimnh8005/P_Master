package kr.co.pulmuone.mall.customer.stndpnt.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointMallRequestDto;
import kr.co.pulmuone.v1.customer.stndpnt.service.StandingPointBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandingPointMallServiceImpl implements StandingPointMallService {

    @Autowired
    public StandingPointBiz standingPointBiz;

    @Override
    public ApiResult<?> addStandingPointQna(StandingPointMallRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        long urUserId = 0L;
        if (buyerVo != null && !StringUtil.isEmpty(buyerVo.getUrUserId())) {
            urUserId = Long.parseLong(buyerVo.getUrUserId());
        }

        dto.setManagerUrUserId(urUserId);
        dto.setCreateId(urUserId);

        standingPointBiz.addStandingPointQna(dto);
        return ApiResult.success();
    }
}
