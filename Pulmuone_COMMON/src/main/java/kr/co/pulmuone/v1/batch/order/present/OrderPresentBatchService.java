package kr.co.pulmuone.v1.batch.order.present;

import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.order.present.dto.OrderPresentExpiredResponseDto;
import kr.co.pulmuone.v1.order.present.service.OrderPresentBiz;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderPresentBatchService {

    private final OrderPresentBiz orderPresentBiz;
    private final SendTemplateBiz sendTemplateBiz;

    @Autowired
    private ComnBizImpl comnBizImpl;

    protected void runOrderPresentExpiredList() throws Exception {
        // Biz 호출
        List<OrderPresentExpiredResponseDto> expiredList = orderPresentBiz.getExpiredCancelOrderList();
        if (!expiredList.isEmpty()) {
            ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.ORDER_PRESENT_EXPIRED.getCode());
            GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

            for (OrderPresentExpiredResponseDto expiredDto : expiredList) {
                OrderEnums.OrderPresentErrorCode expiredResult = orderPresentBiz.expiredCancel(expiredDto.getOdOrderId());
                
                // 취소성공시 SMS 발송
                if (OrderEnums.OrderPresentErrorCode.SUCCESS.equals(expiredResult)) {
                    String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, expiredDto);
                    String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
                    String reserveYn = "Y"; //즉시 발송여부(N:즉시발송, Y:예약발송)
                    LocalDate localDate = LocalDate.now();
                    AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                            .content(content)
                            .urUserId(String.valueOf(expiredDto.getUrUserId()))
                            .mobile(expiredDto.getPresentReceiveHp())
                            .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                            .reserveYn(reserveYn)
                            .reserveDate(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 08:00:00")))
                            .build();
    
                    sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);
                }
            }
        }
    }

}
