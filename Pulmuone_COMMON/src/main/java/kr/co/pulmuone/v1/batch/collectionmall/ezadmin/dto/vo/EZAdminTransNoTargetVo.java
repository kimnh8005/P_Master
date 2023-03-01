package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 송장입력 API 대상 주문건 VO")
public class EZAdminTransNoTargetVo {

	@ApiModelProperty(value = "관리번호")
	private String seq;

	@ApiModelProperty(value = "PRD_SEQ")
	private String prd_seq;

	@ApiModelProperty(value = "택배사코드")
	private String trans_corp;

	@ApiModelProperty(value = "송장번호")
	private String trans_no;

	@ApiModelProperty(value = "주문상세번호")
	private String order_detl_id;

	@ApiModelProperty(value = "처리상태")
	private String status;
}
