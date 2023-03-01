package kr.co.pulmuone.mall.promotion.shoplive.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveInfoRequestDto;
import kr.co.pulmuone.v1.promotion.shoplive.service.ShopliveEventBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShopliveEventMallServiceImpl implements ShopliveEventMallService {

    @Autowired
    ShopliveEventBiz shopliveEventBiz;

    @Override
    public ApiResult<?> getShopliveInfo(ShopliveInfoRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        dto.setUrUserId(getUrUserId(buyerVo));
        // 샵라이브 채팅 사용자명 = 로그인 아이디 3자리 + 나머지 * 마스킹 처리
        dto.setUserNm(StringUtil.isNotEmpty(buyerVo.getLoginId())? buyerVo.getLoginId().replaceAll("(?<=.{3}).", "*"): "");
        dto.setUrGroupId(buyerVo.getUrGroupId());
        dto.setLoginId(StringUtil.isNotEmpty(buyerVo.getLoginId()) ? buyerVo.getLoginId() : "");
        return shopliveEventBiz.getShopliveInfo(dto);
    }

    private Long getUrUserId(BuyerVo buyerVo) {
        long urUserIdL = 0L;
        String urUserId = "";
        if (buyerVo != null) urUserId = StringUtil.nvl(buyerVo.getUrUserId());
        if (StringUtil.isNotEmpty(urUserId)) urUserIdL = Long.parseLong(urUserId);
        return urUserIdL;
    }
}
