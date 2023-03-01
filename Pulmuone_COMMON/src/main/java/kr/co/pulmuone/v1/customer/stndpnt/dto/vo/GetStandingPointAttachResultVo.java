package kr.co.pulmuone.v1.customer.stndpnt.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetStandingPointAttachResultVo")
public class GetStandingPointAttachResultVo {

	@ApiModelProperty(value = "상품입점문의 첨부파일 ID")
	String csStandPntAttcId;

	@ApiModelProperty(value = "파일경로")
	String filePath;

	@ApiModelProperty(value = "상품입점문의 ID")
	String csStandPntId;

	@ApiModelProperty(value = "물리파일명")
	String fileNm;

	@ApiModelProperty(value = "파일명")
	String realFileNm;

}
