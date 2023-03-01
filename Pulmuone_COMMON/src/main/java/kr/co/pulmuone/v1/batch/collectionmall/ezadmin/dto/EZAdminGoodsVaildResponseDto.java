package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 API 상품 유효성 체크 응답 정보")
public class EZAdminGoodsVaildResponseDto {

	@ApiModelProperty(value = "상태")
	private String status;

	@ApiModelProperty(value = "판매처PK")
	private String message;
}
