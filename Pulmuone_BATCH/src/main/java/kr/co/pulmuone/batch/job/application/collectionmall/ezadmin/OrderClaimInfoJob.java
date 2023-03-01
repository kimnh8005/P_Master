package kr.co.pulmuone.batch.job.application.collectionmall.ezadmin;

import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.CollectionMallEZAdminBatchBiz;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminRunOrderInfoRequestDto;
import kr.co.pulmuone.v1.comm.enums.EZAdminEnums;
import kr.co.pulmuone.v1.comm.enums.ApiEnums.EZAdminGetOrderInfoOrderCs;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("orderClaimInfoJob")
@Slf4j
public class OrderClaimInfoJob implements BaseJob {

    @Autowired
    private CollectionMallEZAdminBatchBiz collectionMallEZAdminBatchBiz;

    @Override
    public void run(String[] params) {


//  이지어드민 클레임 주문조회 API -> OrderInfoJob 하나로 사용하므로 해당 job 사용안함!

/*        log.info("======"+EZAdminEnums.EZAdminBatchTypeCd.CLAIM_SEARCH.getCodeName()+"======");

        int paramsLength = params.length;
        long paramIfEasyadminInfoId = 0;
        int paramThreadMaxCount = 0;

        // TODO 수동배치 실행
        if(paramsLength == 3){
            paramIfEasyadminInfoId = Long.parseLong(params[2]);
        }
//        else if(paramsLength == 4){
//            paramIfEasyadminInfoId = Long.parseLong(params[2]);
//            paramThreadMaxCount = Integer.parseInt(params[3]);
//        }

    	try {
        	// 클레임
            EZAdminRunOrderInfoRequestDto batchDateTime = collectionMallEZAdminBatchBiz.calcBatchDateTime();

			collectionMallEZAdminBatchBiz.runOrderInfo(EZAdminRunOrderInfoRequestDto.builder()
                    .orderCsEnum(EZAdminGetOrderInfoOrderCs.CLAIM)
                    .paramIfEasyadminInfoId(paramIfEasyadminInfoId)
                    .paramThreadMaxCount(paramThreadMaxCount)
                    .startDateTime(batchDateTime.getStartDateTime())
                    .endDateTime(batchDateTime.getEndDateTime())
                    .build());

		} catch (Exception e) {
			log.error("=========OrderClaimInfoJob error============");
		}*/
    }

}
