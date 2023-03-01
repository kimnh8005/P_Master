package kr.co.pulmuone.v1.order.regular.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.pg.dto.PaymentFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "정기 결제 카드 등록 응답 Dto")
public class ApplyRegularBatchKeyResponseDto {

	private List<PaymentFormDto> pgFormDataList;

	private String exeScriptType;
}
