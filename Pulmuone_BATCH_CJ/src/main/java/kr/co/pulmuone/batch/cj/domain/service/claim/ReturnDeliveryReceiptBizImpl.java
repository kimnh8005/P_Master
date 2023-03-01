package kr.co.pulmuone.batch.cj.domain.service.claim;

import kr.co.pulmuone.batch.cj.domain.model.claim.CJLogisticsOrderAcceptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * CJ 반품택배 접수 배치BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnDeliveryReceiptBizImpl implements ReturnDeliveryReceiptBiz {

	@Autowired
	private final ReturnDeliveryReceiptService returnDeliveryReceiptService;

	@Autowired
	@Qualifier("slaveCjSqlSessionTemplate")
	private final SqlSessionTemplate cjSqlSession;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Exception.class })
	public void returnDeliveryReceipt() {

		// 대상 조회
		List<CJLogisticsOrderAcceptDto> targetList = returnDeliveryReceiptService.selectReturnDeliveryReceiptTargetList();

		for (CJLogisticsOrderAcceptDto item: targetList){

			cjSqlSession.insert("kr.co.pulmuone.batch.cj.infra.mapper.claim.slaveCj.CjReturnTrackingNumberMapper.addReturnDeliveryReceipt", item);

			returnDeliveryReceiptService.putReturnDeliveryReceiptBatchExecFl(item);

		}

	}
}