package kr.co.pulmuone.v1.pg.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentFormDto {

	private String name;

	private String value;
}
