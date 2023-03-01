package kr.co.pulmuone.v1.batch.promotion.ad;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class PromotionAdJsonBizImpl implements PromotionAdJsonBiz {

    private final PromotionAdJsonBizService service;

    @Override
    public void runMakeJson() throws Exception {
        service.runMakeJson();
    }
}
