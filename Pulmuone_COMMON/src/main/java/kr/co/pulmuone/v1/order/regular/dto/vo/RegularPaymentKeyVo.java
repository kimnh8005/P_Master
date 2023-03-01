package kr.co.pulmuone.v1.order.regular.dto.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "정기결제 키 VO")
public class RegularPaymentKeyVo {

	/**
	 * KCP 배치키
	 */
	private String batchKey;

	/**
	 * 카드사명
	 */
	private String cardName;

	/**
	 * 카드 마스킹 정보
	 */
	private String cardMaskingNumber;

}
