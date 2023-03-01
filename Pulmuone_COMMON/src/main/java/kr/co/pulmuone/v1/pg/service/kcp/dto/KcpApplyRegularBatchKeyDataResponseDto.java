package kr.co.pulmuone.v1.pg.service.kcp.dto;

import java.util.List;

import kr.co.pulmuone.v1.pg.dto.PaymentFormDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KcpApplyRegularBatchKeyDataResponseDto {
	private List<PaymentFormDto> pgFormDataList;

	private String exeScriptType;
}
