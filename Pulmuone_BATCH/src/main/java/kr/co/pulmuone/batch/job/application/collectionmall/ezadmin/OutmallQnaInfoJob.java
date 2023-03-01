package kr.co.pulmuone.batch.job.application.collectionmall.ezadmin;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.CollectionMallEZAdminBatchBiz;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminQnaInfoRequestDto;
import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.EZAdminRunOrderInfoRequestDto;
import kr.co.pulmuone.v1.comm.enums.ApiEnums.EZAdminGetOrderInfoOrderCs;
import kr.co.pulmuone.v1.comm.enums.EZAdminEnums;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component("OutmallQnaInfoJob") // Batch Id : 142
@Slf4j
public class OutmallQnaInfoJob implements BaseJob {

    @Autowired
    private CollectionMallEZAdminBatchBiz collectionMallEZAdminBatchBiz;

    @Override
    public void run(String[] params) {

        log.info("======"+EZAdminEnums.EZAdminBatchTypeCd.QNA_SEARCH.getCodeName()+"======");
		int failCount = 0; // API 호출 실패여부
		String ezadminBatchTypeCd = EZAdminEnums.EZAdminBatchTypeCd.QNA_SEARCH.getCode();

		EZAdminRunOrderInfoRequestDto batchDateTime = collectionMallEZAdminBatchBiz.calcQnaBatchDateTime(ezadminBatchTypeCd);
		EZAdminQnaInfoRequestDto reqDto = new EZAdminQnaInfoRequestDto();
		reqDto.setStart_date(batchDateTime.getStartDateTime().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));
		reqDto.setEnd_date(batchDateTime.getEndDateTime().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss")));

    	try {

    		// 정상
			failCount += collectionMallEZAdminBatchBiz.runOutmallQnaInfo(reqDto);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("=========outmallQnaInfoJob error============");
		}

        log.info("======"+EZAdminEnums.EZAdminBatchTypeCd.QNA_SEARCH.getCodeName()+"======");
    }
}
