package kr.co.pulmuone.batch.job.application.collectionmall.ezadmin;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.CollectionMallEZAdminBatchBiz;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminRunOrderInfoRequestDto;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.enums.ApiEnums.EZAdminGetOrderInfoOrderCs;
import kr.co.pulmuone.v1.comm.enums.EZAdminEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("orderInfoJob") // Batch Id : 32
@Slf4j
public class OrderInfoJob implements BaseJob {

    @Autowired
    private CollectionMallEZAdminBatchBiz collectionMallEZAdminBatchBiz;


	/**
	 * params[0] - 배치번호
	 * params[1] - 실행ID
	 * params[2] - 스래드갯수
	 * params[3] - 샵ID List  ex)10007,10009,10011,10054,10055,10067,10080,10081,10082,10083
	 * params[4] - 배치구분(주문수집 / 클레입수집)    ex) 0-전체, 1-주문, 2-클레임
	 */
	@Override
    public void run(String[] params) {

        log.info("======"+EZAdminEnums.EZAdminBatchTypeCd.ORDER_SEARCH.getCodeName()+"======");

		int paramsLength = params.length;
		long paramIfEasyadminInfoId = 0;
		int paramThreadMaxCount = 0;
		String paramShopIdList = "";
		int paramActionType = 0;

		String action = ApiEnums.EZAdminApiAction.GET_ORDER_INFO.getCode(); // EZ Admin API 종류

		// params[2] - 스래드갯수
		if(paramsLength >= 3 && StringUtils.isNotEmpty(params[2]) && StringUtils.isNumeric(params[2])){
			paramThreadMaxCount = Integer.parseInt(params[2]);		// 최대 스래드개수
		}

		// params[3] - 판매처 리스트
		if(paramsLength >= 4 && StringUtils.isNotEmpty(params[3])){
			paramShopIdList = params[3];
		}

		// params[4] - 수집 유형
		if(paramsLength >= 5 && StringUtils.isNotEmpty(params[4]) && StringUtils.isNumeric(params[4])){
			paramActionType = Integer.parseInt(params[4]);
		}

		if(paramActionType == 0 || paramActionType == 1) {

			// [주문] 주문 조회 시작시간/종료시간 지정(기존 종료시간부터 현재시간에서 -5분한 시간)
			EZAdminRunOrderInfoRequestDto batchDateTime = collectionMallEZAdminBatchBiz.calcBatchDateTime(action, EZAdminGetOrderInfoOrderCs.ORDER.getBatchTp());

			// TODO EZADMIN TEST : 시간 지정
//			batchDateTime.setStartDateTime(LocalDateTime.of(2021, 9, 13, 8, 0));
//			batchDateTime.setEndDateTime(LocalDateTime.of(2021, 9, 13, 8, 10));

			try {
				// 정상
				collectionMallEZAdminBatchBiz.runOrderInfo(EZAdminRunOrderInfoRequestDto.builder()
								.orderCsEnum(EZAdminGetOrderInfoOrderCs.ORDER)
								.paramIfEasyadminInfoId(paramIfEasyadminInfoId)
								.paramThreadMaxCount(paramThreadMaxCount)
								.startDateTime(batchDateTime.getStartDateTime())
								.endDateTime(batchDateTime.getEndDateTime())
								.shopIdList(paramShopIdList)
						.build());

			} catch (Exception e) {
				e.printStackTrace();
				log.error("=========OrderInfoJob error============");
			}
		}

		if(paramActionType == 0 || paramActionType == 2) {

			// [클레임] 주문 조회 시작시간/종료시간 지정(기존 종료시간부터 현재시간에서 -5분한 시간)
			EZAdminRunOrderInfoRequestDto batchDateTime = collectionMallEZAdminBatchBiz.calcBatchDateTime(action, EZAdminGetOrderInfoOrderCs.CLAIM.getBatchTp());

			// TODO EZADMIN TEST : 시간 지정
//			batchDateTime.setStartDateTime(LocalDateTime.of(2021, 12, 1, 1, 0));
//			batchDateTime.setEndDateTime(LocalDateTime.of(2021, 12, 7, 23, 10));

			log.info("======" + EZAdminEnums.EZAdminBatchTypeCd.CLAIM_SEARCH.getCodeName() + "======");
			try {
				// 클레임
				collectionMallEZAdminBatchBiz.runOrderInfo(EZAdminRunOrderInfoRequestDto.builder()
						.orderCsEnum(EZAdminGetOrderInfoOrderCs.CLAIM)
						.paramIfEasyadminInfoId(paramIfEasyadminInfoId)
						.paramThreadMaxCount(paramThreadMaxCount)
						.startDateTime(batchDateTime.getStartDateTime())
						.endDateTime(batchDateTime.getEndDateTime())
						.shopIdList(paramShopIdList)
						.build());

			} catch (Exception e) {
				e.printStackTrace();
				log.error("=========OrderClaimInfoJob error============");
			}
		}

    	//API 연속 호출 3회 실패시 -> BOS 알림메일 발송
		try{
			collectionMallEZAdminBatchBiz.checkBosCollectionMallInterfaceFail();
		}catch (Exception e) {
			log.error("ERROR====== BOS 수집몰 연동 실패 알림 자동메일 오류 ==========");
			log.error(e.getMessage(), e);
		}

    }

}
