package kr.co.pulmuone.batch.cj.domain.service.send.template;


import kr.co.pulmuone.batch.cj.domain.service.send.template.dto.BatchSmsIssueRequestDto;
import kr.co.pulmuone.batch.cj.domain.service.send.template.dto.vo.BatchSmsTargetVo;
import kr.co.pulmuone.batch.cj.infra.mapper.send.BatchSendTemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchSendTemplateServiceImpl implements BatchSendTemplateService {

    private final BatchSendTemplateMapper batchSendTemplateMapper;

    @Override
    public void sendSmsBatch(Long batchNo, String content) {
        // SMS 전송 대상자 조회
        List<BatchSmsTargetVo> targetList = batchSendTemplateMapper.getSmsTargetList(batchNo);
        if (targetList == null || targetList.isEmpty()) return;

        // SMS 전송 등록
        String senderTelephone = batchSendTemplateMapper.getPsValue("SEND_SMS_NUMBER");
        List<BatchSmsIssueRequestDto> requestDtoList = targetList.stream()
                .map(vo -> BatchSmsIssueRequestDto.builder()
                        .urUserId(vo.getUrUserId())
                        .mobile(vo.getMobile())
                        .content(content)
                        .senderTelephone(senderTelephone)
                        .build())
                .collect(Collectors.toList());
        batchSendTemplateMapper.addSmsIssue(requestDtoList);
    }

}
