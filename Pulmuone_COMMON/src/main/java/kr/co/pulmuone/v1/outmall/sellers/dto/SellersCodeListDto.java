package kr.co.pulmuone.v1.outmall.sellers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "판매처 목록 응답 Response Dto")
public class SellersCodeListDto extends BaseResponseDto {

	@JsonProperty("CODE")
	@ApiModelProperty(value="판매처 PK")
	private long code;

	@JsonProperty("NAME")
	@ApiModelProperty(value="판매처명")
	private String name;
}
