package kr.co.pulmuone.v1.system.basic.dto.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetStShopListResultVo")
public class GetStShopListResultVo implements Serializable {

	private static final long serialVersionUID = 3647568578047647322L;

	@ApiModelProperty(value = "샵 아이디")
	private String shopId;

	@ApiModelProperty(value = "샵명")
	private String shopName;

	@ApiModelProperty(value = "샵 권한 아이디")
	private String shopAuthzId;

	@ApiModelProperty(value = "샵 권한 키")
	private String shopAuthzKey;

	@ApiModelProperty(value = "사이트 도메인")
	private String siteDomain;

	@ApiModelProperty(value = "사이트 모바일 도메인")
	private String siteDomainMo;

	@ApiModelProperty(value = "국가 코드")
	private String countryCode;

}
