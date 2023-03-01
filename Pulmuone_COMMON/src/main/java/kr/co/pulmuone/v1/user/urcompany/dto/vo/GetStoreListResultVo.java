package kr.co.pulmuone.v1.user.urcompany.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetStorListResultVo")
public class GetStoreListResultVo {

	@ApiModelProperty(value = "매장 ID + 매장 전화번호")
	private String storeId;

	@ApiModelProperty(value = "매장명")
	private String storeName;

	@ApiModelProperty(value = "매정전화번호1")
	private String companyTelephone1;

	@ApiModelProperty(value = "매정전화번호2")
	private String companyTelephone2;

	@ApiModelProperty(value = "매정전화번호3")
	private String companyTelephone3;

	@ApiModelProperty(value = "매장 ID")
	private String urStoreId;
}
