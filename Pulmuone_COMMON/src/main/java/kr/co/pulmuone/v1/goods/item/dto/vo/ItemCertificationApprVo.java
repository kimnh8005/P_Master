package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "품목인증정보 Request")
public class ItemCertificationApprVo extends BaseRequestDto{

	@ApiModelProperty(value = "SEQ", required = true)
	private String ilItemCertificationId;

	@ApiModelProperty(value = "품목승인코드", required = true)
	private String ilItemApprId;

	@ApiModelProperty(value = "품목코드", required = true)
	private String ilItemCd;

	@ApiModelProperty(value = "상품 인증정보 PK", required = true)
	private String ilCertificationId;

	@ApiModelProperty(value = "상세정보", required = true)
	private String certificationDesc;

	@ApiModelProperty(value = "등록자", required = true)
	private String createId;

	@ApiModelProperty(value = "등록일", required = true)
	private String createDt;

	@ApiModelProperty(value = "수정자", required = true)
	private String modifyId;

	@ApiModelProperty(value = "수정일", required = true)
	private String modifyDt;




}
