package kr.co.pulmuone.v1.policy.shippingcomp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "택배사 외부몰 정보 Vo")
public class PolicyShippingCompOutmallVo extends BaseRequestDto{

	@ApiModelProperty(value = "택배사 설정 PK")
	private long psShippingCompId;

	@ApiModelProperty(value = "택배사 외부몰 유형(E:이지어드민, S:사방넷)")
	private String outmallCode;

	@ApiModelProperty(value = "외부몰 택배사 코드")
	private String outmallShippingCompCode;

	@ApiModelProperty(value = "외부몰 택배사 명")
	private String outmallShippingCompName;

}
