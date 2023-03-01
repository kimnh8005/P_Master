package kr.co.pulmuone.v1.user.certification.dto;

import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetNonMemberOrderRequestDto")
@Component
public class GetNonMemberOrderRequestDto
{
	@ApiModelProperty(value = "주문자명", required = true)
	private String buyerName;

	@ApiModelProperty(value = "휴대폰번호", required = true)
	private String buyerMobile;
}
