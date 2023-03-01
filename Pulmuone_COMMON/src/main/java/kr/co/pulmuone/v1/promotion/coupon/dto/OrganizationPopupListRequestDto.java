package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OrganizationPopupListRequestDto")
public class OrganizationPopupListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "검색코드 타입")
	private String codeType;

	@ApiModelProperty(value = "검색명")
	private String nameType;

	@ApiModelProperty(value = "출고처 ID")
	private String searchCodeValue;

	@ApiModelProperty(value = "출고처 ID")
	private String searchNameValue;

}
