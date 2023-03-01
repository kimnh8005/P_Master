package kr.co.pulmuone.v1.goods.etc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품인증정보관리상세 ResponseDto")
public class CertificationResponseDto {

	@ApiModelProperty(value = "상품인증정보관리상세")
	private	CertificationVo certificationInfo;
}
