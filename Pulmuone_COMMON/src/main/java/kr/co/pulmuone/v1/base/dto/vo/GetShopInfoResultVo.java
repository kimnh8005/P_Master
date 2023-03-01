package kr.co.pulmuone.v1.base.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetShopInfoResultVo")
public class GetShopInfoResultVo {

	@JsonProperty("SHOP_ID")
	@ApiModelProperty(value = "")
	private String id;

	@JsonProperty("SHOP_NAME")
	@ApiModelProperty(value = "")
	private String shopName;

	@JsonProperty("SHOP_AUTHORIZATION_ID")
	@ApiModelProperty(value = "")
	private String shopAuthorizationId;

	@JsonProperty("SHOP_AUTHORIZATION_KEY")
	@ApiModelProperty(value = "")
	private String shopAuthorizationKey;

	@JsonProperty("SITE_DOMAIN")
	@ApiModelProperty(value = "")
	private String siteDomain;

	@JsonProperty("SITE_DOMAIN_MOBILE")
	@ApiModelProperty(value = "")
	private String siteDomainMobile;

	@JsonProperty("COUNTRY_CODE")
	@ApiModelProperty(value = "")
	private String countryCode;

}
