package kr.co.pulmuone.v1.goods.etc.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.etc.dto.vo.CertificationVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품인증정보관리리스트 ResponseDto")
public class CertificationListResponseDto {

	@ApiModelProperty(value = "상품인증정보관리리스트")
	private	List<CertificationVo> rows;

	@ApiModelProperty(value = "총 갯수")
	private long total;
}
