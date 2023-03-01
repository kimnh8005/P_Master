package kr.co.pulmuone.batch.job.application.order.email;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("orderRegularGoodsPriceChangeJob")
@Slf4j
@RequiredArgsConstructor
public class OrderRegularGoodsPriceChangeJob implements BaseJob {

    @Autowired
    private OrderEmailBiz orderEmailBiz;

    @Autowired
    private OrderEmailSendBiz orderEmailSendBiz;

    @Override
    public void run(String[] params) {

    	log.info("====== 정기배송 상품금액 변동안내 자동메일  발송  ======");

    	try {

    		// 1. 금일 가격변동 대상 상품 중 정기배송중인 상품 목록 조회
    		List<HashMap<String,BigInteger>> targetRegularList = orderEmailBiz.getTargetOrderRegularGoodsPriceChangeList();

    		if(CollectionUtils.isNotEmpty(targetRegularList)) {

    			for(HashMap<String,BigInteger> targetMap : targetRegularList) {
    				Long ilGoodsId = targetMap.get("IL_GOODS_ID").longValue();
    				Long odRegularReqId = targetMap.get("OD_REGULAR_REQ_ID").longValue();

    				// 2. 자동메일 발송
    				try {
    					OrderInfoForEmailResultDto orderRegularGoodsPriceChangeDto
    										= orderEmailBiz.getOrderRegularGoodsPriceChangeInfoForEmail(odRegularReqId, ilGoodsId);
    					orderEmailSendBiz.orderRegularGoodsPriceChange(orderRegularGoodsPriceChangeDto);
					} catch (Exception e) {
						log.error("ERROR ====== 정기배송 상품금액 변동안내 자동메일  발송 오류 ======= ilGoodsId :: " + ilGoodsId + ", odRegularReqId :: " + odRegularReqId);
						log.error(e.getMessage());
					}
    			}
    		}

		} catch (Exception e) {
			log.error("ERROR ====== 정기배송 상품금액 변동안내 자동메일  발송 오류 orderEmailBiz.getTargetOrderRegularGoodsPriceChangeList ERROR =======");
			log.error(e.getMessage());
		}

    }
}

