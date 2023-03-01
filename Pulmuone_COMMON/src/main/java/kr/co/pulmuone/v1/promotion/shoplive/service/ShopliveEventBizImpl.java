package kr.co.pulmuone.v1.promotion.shoplive.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveInfoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopliveEventBizImpl implements ShopliveEventBiz{

    @Autowired
    ShopliveEventService shopliveEventService;

    @Override
    public ApiResult<?> getShopliveInfo(ShopliveInfoRequestDto dto) throws Exception {
        return shopliveEventService.getShopliveInfo(dto);
    }
}
