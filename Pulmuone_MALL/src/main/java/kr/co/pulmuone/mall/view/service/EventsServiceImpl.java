package kr.co.pulmuone.mall.view.service;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kr.co.pulmuone.v1.promotion.event.service.PromotionEventBiz;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private PromotionEventBiz promotionEventBiz;

    /**
     * SPMO-1301 이벤트 리다이렉션 이벤트 아이디 조회
     *
     * @return Long
     */
    @Override
    public Long getReDirectEventId(HttpServletRequest request) throws Exception {

        String strEventId = request.getParameter("event");
        if(StringUtil.isNotEmpty(strEventId) && StringUtil.isNumeric(strEventId)) {
            Long lEventId = Long.parseLong(strEventId);
            return promotionEventBiz.getReDirectEventId(lEventId);
        }

        return null;
    }
}