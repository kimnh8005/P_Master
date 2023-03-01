package kr.co.pulmuone.v1.goods.etc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품인증정보관리리스트 RequestDto")
public class CertificationListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "인증정보명")
	private String searchCertificationName;

	@ApiModelProperty(value = "사용여부")
	private String searchUseYn;

}
