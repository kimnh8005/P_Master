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
@ApiModel(description = "이지어드민 송장입력 API Request DTO")
public class EZAdminTransNoRequestDto{

	@ApiModelProperty(value = "API action")
    private String action;

	@ApiModelProperty(value = "상품관리번호")
    private String prd_seq;

	@ApiModelProperty(value = "택배사코드")
    private String trans_corp;

	@ApiModelProperty(value = "송장번호")
    private String trans_no;

	@ApiModelProperty(value = "배송처리")
    private int trans_pos;

}
