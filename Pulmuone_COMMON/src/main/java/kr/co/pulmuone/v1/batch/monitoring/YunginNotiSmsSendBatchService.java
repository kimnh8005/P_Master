package kr.co.pulmuone.v1.batch.monitoring;

import kr.co.pulmuone.v1.batch.monitoring.dto.vo.YunginNotiSmsSendVo;
import kr.co.pulmuone.v1.batch.send.template.service.SendTemplateBatchBiz;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddSmsIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.comm.mappers.batch.master.monitoring.YunginNotiSmsSendBatchMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.system.code.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.system.code.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.system.code.service.SystemCodeBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class YunginNotiSmsSendBatchService {

    private final YunginNotiSmsSendBatchMapper yunginNotiSmsSendBatchMapper;

    @Autowired
    SystemCodeBiz systemCodeBiz;

    @Autowired
    private SendTemplateBatchBiz sendTemplateBatchBiz;

    protected void execYunginNotiSmsSend(int orderIfDate, String stCommonCodeMasterId) throws Exception {
        String content = smsSendContent(orderIfDate);

        //SMS발송 주문 데이터가 존재하면 발송
        if (StringUtil.isNotEmpty(content)) {
            String senderTelephone = sendTemplateBatchBiz.getPsValue("SEND_SMS_NUMBER");
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

            //SMS수신자 대상으로 문자 발송
            GetCodeListRequestDto getCodeListRequestDto = GetCodeListRequestDto.builder()
                    .stCommonCodeMasterId(stCommonCodeMasterId)
                    .useYn("Y")
                    .build();

            GetCodeListResponseDto getCodeListResponseDto = systemCodeBiz.getCodeList(getCodeListRequestDto);

            List<GetCodeListResultVo> codeListResultVoList = getCodeListResponseDto.getRows();

            if (CollectionUtils.isNotEmpty(codeListResultVoList)) {
                for (kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo target : codeListResultVoList) {
                    // 모니터링 대상자 SMS 발송
                    AddSmsIssueSelectBatchRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectBatchRequestDto.builder()
                            .content(content)
                            .mobile(target.getAttribute2())
                            .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                            .reserveYn(reserveYn)
                            .build();

                    sendTemplateBatchBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);
                }
            }
        }
    }

    protected String smsSendContent(int orderIfDate) {
        String tempContent = "";    //일자별 내용
        String returnContent = "";  //최종합산 내용

        // 기준일자 값만큼 loop 실행 (SMS로 발송할 주문 데이터)
        for (int ifDt = 1; orderIfDate >= ifDt; ifDt++) {

            //SMS로 발송할 주문 데이터
            YunginNotiSmsSendVo resultVo = yunginNotiSmsSendBatchMapper.getYunginNotiOrderInfo(ifDt);

            if (ifDt == 1) {
                tempContent = "#풀무원\r\n"
                        + "[D" + ifDt + "]" + resultVo.getIfDt() + " 00시"
                        + "\r\n■ 택배 : " + resultVo.getNormalPffOrdCnt() + "건 (냉동 " + resultVo.getNormalPffFrozenCnt() + "건 포함) / " + resultVo.getNormalPffCnt() + "ea"
                        + "\r\n■ 새벽 : " + resultVo.getDawnPffOrdCnt() + "건 (냉동 " + resultVo.getDawnPffFrozenCnt() + "건 포함) / " + resultVo.getDawnPffCnt() + "ea";
            } else {
                tempContent = "\r\n[D" + ifDt + "]" + resultVo.getIfDt() + " 00시"
                        + "\r\n■ 택배 : " + resultVo.getNormalPffOrdCnt() + "건 (냉동 " + resultVo.getNormalPffFrozenCnt() + "건 포함) / " + resultVo.getNormalPffCnt() + "ea"
                        + "\r\n■ 새벽 : " + resultVo.getDawnPffOrdCnt() + "건 (냉동 " + resultVo.getDawnPffFrozenCnt() + "건 포함) / " + resultVo.getDawnPffCnt() + "ea";
            }

            returnContent += tempContent;
        }

        return returnContent;
    }
}
