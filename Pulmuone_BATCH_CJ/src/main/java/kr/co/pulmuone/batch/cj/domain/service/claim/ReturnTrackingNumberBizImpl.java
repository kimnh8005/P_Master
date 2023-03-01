package kr.co.pulmuone.batch.cj.domain.service.claim;


import kr.co.pulmuone.batch.cj.common.enums.OrderEnums;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 API 배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnTrackingNumberBizImpl implements ReturnTrackingNumberBiz {

	@Autowired
	private final ReturnTrackingNumberService returnTrackingNumberService;

	@Autowired
	@Qualifier("slaveCjSqlSessionTemplate")
	private final SqlSessionTemplate cjSqlSession;

	@Override
	public void execute() {
		// 대상 조회
		List<String> targetList = returnTrackingNumberService.selectTargetList();

		for (String item: targetList){

			System.out.println("item : " + item);


			// CJ 데이터 조회
			HashMap<String, String> cjItem = cjSqlSession.selectOne("kr.co.pulmuone.batch.cj.infra.mapper.claim.slaveCj.CjReturnTrackingNumberMapper.selectTracePulmuoneInfo", item);

			if (cjItem != null){
				cjItem.put("odClaimDetlId",	item);
				cjItem.put("logisticsCd",	OrderEnums.LogisticsCd.CJ.getLogisticsCode());
				returnTrackingNumberService.addReturnTrackingNumber(cjItem);
			}
		}
	}
}