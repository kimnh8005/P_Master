package kr.co.pulmuone.bos.promotion.advertising.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.promotion.advertising.dto.AddAdvertisingExternalRequestDto;
import kr.co.pulmuone.v1.promotion.advertising.service.PromotionAdvertisingBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionAdvertisingBosServiceImpl implements PromotionAdvertisingBosService {

    @Autowired
    private PromotionAdvertisingBiz promotionAdvertisingBiz;

    @Override
    public ApiResult<?> addAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception {
        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null) {
            dto.setUserId(userVo.getUserId());
        }
        return promotionAdvertisingBiz.addAdvertisingExternal(dto);
    }

    @Override
    public void putAdvertisingExternal(AddAdvertisingExternalRequestDto dto) throws Exception {
        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null) {
            dto.setUserId(userVo.getUserId());
        }
        promotionAdvertisingBiz.putAdvertisingExternal(dto);
    }

}
