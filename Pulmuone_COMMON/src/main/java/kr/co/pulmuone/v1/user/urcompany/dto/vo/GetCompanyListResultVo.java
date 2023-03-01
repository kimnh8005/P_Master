package kr.co.pulmuone.v1.user.urcompany.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCompanyListResultVo")
public class GetCompanyListResultVo {


	@ApiModelProperty(value = "No")
	private int no;

	@ApiModelProperty(value = "거래처 ID")
	private String urClientId;

	@ApiModelProperty(value = "거래처 타입")
	private String clientTp;

	@ApiModelProperty(value = "거래처 타입 명")
	private String clientTypeName;

	@ApiModelProperty(value = "거래처 명")
	private String clientName;

	@ApiModelProperty(value = "공급업체 명")
	private String supplyCompName;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "등록일자")
	private String createDate;

	@ApiModelProperty(value = "회사정보 고유ID")
	private String urCompanyId;
}
