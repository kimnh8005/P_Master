package kr.co.pulmuone.v1.comm.util.asis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "통합몰 회원가입시 AS-IS 풀무원 적립금 조회 API 응답 DTO")
public class SearchCustomerRsrvTotalResponseDto {

	@ApiModelProperty(value = "풀무원샵 적립금")
    private int pulmuoneShopPoint;

	@ApiModelProperty(value = "올가 적립금")
	private int orgaPoint;

	@ApiModelProperty(value = "성공여부")
    private boolean success;

	@ApiModelProperty(value = "상태")
	private int status;

	@ApiModelProperty(value = "결과 코드")
	private int code;

	@ApiModelProperty(value = "결과 메시지")
	private String message;

}
