package kr.co.pulmuone.v1.batch.order.ezadmin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminInfoDto;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.EZAdminEnums;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;
import kr.co.pulmuone.v1.outmall.ezadmin.service.EZAdminOrderRegistrationBiz;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EZAdminOrderBizImpl implements EZAdminOrderBiz {

    @Autowired
    private EZAdminOrderService ezAdminOrderService;

    @Autowired
    private EZAdminOrderRegistrationBiz ezadminOrderRegistrationBiz;

	@Override
	public void putEZAdminCreateOrder() throws Exception {

		LocalDateTime batchStartTime = LocalDateTime.now();	// 배치 시작시간

		// 대기중,진행중 모두 조회
		List<String> syncCdList = new ArrayList<>();
		syncCdList.add(EZAdminEnums.EZAdminSyncCd.SYNC_CD_WAIT.getCode());
		syncCdList.add(EZAdminEnums.EZAdminSyncCd.SYNC_CD_ING.getCode());

		List<EZAdminOrderDto> list = ezAdminOrderService.getEZAdminOrderTargetList(syncCdList,ApiEnums.EZAdminGetOrderInfoOrderCs.ORDER.getBatchTp());

		for(EZAdminOrderDto ezadminOrderItem : list) {

			String syncCd = createEZAdminOrder(ezadminOrderItem, batchStartTime);

			// 진행중 중단된 경우
			if(EZAdminEnums.EZAdminSyncCd.SYNC_CD_ING.getCode().equals(syncCd)){
				break;
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class}, readOnly = false)
	public String createEZAdminOrder(EZAdminOrderDto ezadminOrderItem, LocalDateTime batchStartTime) throws Exception{
		EZAdminInfoDto ezadminInfo = new EZAdminInfoDto();

		log.info("---------------------------------------- EZAdmin 상태 업데이트 처리 [" + ezadminOrderItem.getIfEasyadminInfoId() + "] ING");
		ezadminInfo.setIfEasyadminInfoId(Long.parseLong(ezadminOrderItem.getIfEasyadminInfoId()));
		ezadminInfo.setSyncCd(EZAdminEnums.EZAdminSyncCd.SYNC_CD_ING.getCode());
		LocalDateTime startTime = LocalDateTime.now();

		// 대기중일때만 시작시간 업데이트
		if(EZAdminEnums.EZAdminSyncCd.SYNC_CD_WAIT.getCode().equals(ezadminOrderItem.getSyncCd())){
			ezadminInfo.setBatchStartDt(startTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
		}

		ezAdminOrderService.putEZAdminOrderStatus(ezadminInfo);

		// 주문서 생성
		Map<String,Integer> orderCreateResult = ezadminOrderRegistrationBiz.setBindOrderOrder(Long.parseLong(ezadminOrderItem.getIfEasyadminInfoId()), batchStartTime);
		
		// 주문생성 안된건 실패처리
		ezAdminOrderService.verificationOrderCreateSuccess(Long.parseLong(ezadminOrderItem.getIfEasyadminInfoId()));
		
		// 주문생성 COUNT 조회
		int orderCreateCnt = ezAdminOrderService.getOrderCreateCount(Long.parseLong(ezadminOrderItem.getIfEasyadminInfoId()));

		// 진행중 중단된 경우
		if(orderCreateResult.containsKey(EZAdminEnums.EZAdminSyncCd.SYNC_CD_ING.getCode())){
			log.info("---------------------------------------- EZAdmin 상태 업데이트 처리 [" + ezadminOrderItem.getIfEasyadminInfoId() + "] 진행중 중단");

			LocalDateTime endTime = LocalDateTime.now();
			ezadminInfo.setBatchEndDt(endTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
			ezadminInfo.setBatchExecTime(ChronoUnit.SECONDS.between(startTime, endTime));
			ezadminInfo.setSyncCd(EZAdminEnums.EZAdminSyncCd.SYNC_CD_ING.getCode());
			//ezadminInfo.setOrderCreateCnt(orderCreateResult.get(EZAdminEnums.EZAdminSyncCd.SYNC_CD_ING.getCode()));
			ezadminInfo.setOrderCreateCnt(orderCreateCnt);

			ezAdminOrderService.putEZAdminOrderStatus(ezadminInfo);

			return EZAdminEnums.EZAdminSyncCd.SYNC_CD_ING.getCode();
		}else{
			// 완료
			log.info("---------------------------------------- EZAdmin 상태 업데이트 처리 [" + ezadminOrderItem.getIfEasyadminInfoId() + "] END");

			LocalDateTime endTime = LocalDateTime.now();
			ezadminInfo.setBatchEndDt(endTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
			ezadminInfo.setBatchExecTime(ChronoUnit.SECONDS.between(startTime, endTime));
			ezadminInfo.setSyncCd(EZAdminEnums.EZAdminSyncCd.SYNC_CD_END.getCode());
			//ezadminInfo.setOrderCreateCnt(orderCreateResult.get(EZAdminEnums.EZAdminSyncCd.SYNC_CD_END.getCode()));
			ezadminInfo.setOrderCreateCnt(orderCreateCnt);

			ezAdminOrderService.putEZAdminOrderStatus(ezadminInfo);
		}

		return EZAdminEnums.EZAdminSyncCd.SYNC_CD_END.getCode();
	}
}