package kr.co.pulmuone.v1.batch.customer.qna;

import kr.co.pulmuone.v1.batch.policy.config.PolicyConfigBatchBiz;
import kr.co.pulmuone.v1.comm.mappers.batch.master.customer.CustomerQnaBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerQnaBatchService {

    private final CustomerQnaBatchMapper customerQnaBatchMapper;
    private final PolicyConfigBatchBiz policyConfigBatchBiz;

    protected void runQnaDelay() {
        // 토, 일요일은 Batch 종료
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfWeek().getValue() == 6 || localDate.getDayOfWeek().getValue() == 7) return;

        // 공휴일이면 Batch 종료
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String holidayYn = policyConfigBatchBiz.getHolidayYn(localDate.format(dateTimeFormatter));
        if(holidayYn.equals("Y")){
            return ;
        }

        // 지연 대상 추출 및 지연 처리
        List<Long> csQnaIdList = customerQnaBatchMapper.getCustomerQna();
        csQnaIdList.forEach(customerQnaBatchMapper::putCustomerQnaDelay);
    }
}
