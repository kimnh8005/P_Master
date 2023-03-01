package kr.co.pulmuone.batch.esl.domain.service.order.order;

import kr.co.pulmuone.batch.esl.common.enums.ErpApiEnums;
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
 * 주문 API 배치 BizImpl
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderErpBizImpl implements OrderErpBiz {

	@Autowired
	private final OrderErpService orderErpService;

	@Autowired
	@Qualifier("slaveEslSqlSessionTemplate")
	private final SqlSessionTemplate eslSqlSession;

	/**
	 * 잇슬림 주문 입력 ERP API 송신
	 * @param erpApiEnums
	 * @return void
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Exception.class })
	public void addEatsslimOrderIfInpByErp(ErpApiEnums.ErpServiceType erpApiEnums) throws Exception {

		// 주문 데이터 리스트 조회
		List<?> headerList = orderErpService.getErpOrderList(erpApiEnums);
		log.info("=====headerList: " + headerList);

		// 주문별 LOOP
		for(int i=0; i<headerList.size(); i++) {

			// header 생성
			List<?> headerBindList = orderErpService.getErpApiHeaderList(erpApiEnums, headerList.get(i));
			log.info("=====headerBindList: " + headerBindList);

			// DB Connection
			eslSqlSession.insert("kr.co.pulmuone.batch.esl.infra.mapper.order.slaveEsl.EslOrderBatchMapper.addEatsslimOrder", headerBindList.get(0));

		}

		// 주문 배치완료 업데이트
		orderErpService.putErpOrderBatchCompleteUpdate(erpApiEnums, headerList);

	}

}