package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo;

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
@ApiModel(description = "이지어드민 API 호출 정보 vo")
public class EZAdminApiInfoVo {

	@ApiModelProperty(value = "이지어드민 API 호출 정보 PK")
    private Long ifEasyadminApiInfoId;

	@ApiModelProperty(value = "API 종류")
	private String actionNm;

	@ApiModelProperty(value = "주문조회 API CS 상태값")
	private String orderCs;

	@ApiModelProperty(value = "요청 데이터")
	private String reqData;

	@ApiModelProperty(value = "요청 일자")
	private String reqDt;

	@ApiModelProperty(value = "응답 일자")
	private String resDt;

	@ApiModelProperty(value = "에러여부")
	private String resError;

	@ApiModelProperty(value = "응답 메세지")
	private String resMsg;

	@ApiModelProperty(value = "API 호출 성공여부")
	private String successYn;

}
