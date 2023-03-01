package kr.co.pulmuone.v1.approval.auth.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseVo;
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
public class ApprovalDuplicateRequestVo extends BaseVo{

	@ApiModelProperty(value = "품목/상품 승인 아이디")
	private String apprId;

	@ApiModelProperty(value = "품목/상품 승인 요청 타입")
	private String apprKindTp;

	@ApiModelProperty(value = "품목/상품 승인 상태")
	private String apprStat;

}
