package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "거래처 검색 Vo")
public class GetClientPopupResultVo {

	@ApiModelProperty(value = "거래처 ID")
	private Long clientId;

	@ApiModelProperty(value = "거래처명")
	private String clientName;

	@ApiModelProperty(value = "거래처타입명")
	private String clientTypeName;

	@ApiModelProperty(value = "회사 ID")
	private Long companyId;

	@ApiModelProperty(value = "거래처타입")
	private String clientType;

	@ApiModelProperty(value = "공급업체 ID")
	private Long supplierId;

	@ApiModelProperty(value = "매장 ID")
	private String storeId;

	@ApiModelProperty(value = "채널 ID")
	private Long channelId;

	@ApiModelProperty(value = "벤더 납품처코드")
	private String erpCode;
}
