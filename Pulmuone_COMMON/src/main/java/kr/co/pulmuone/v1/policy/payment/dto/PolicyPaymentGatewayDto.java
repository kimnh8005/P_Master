package kr.co.pulmuone.v1.policy.payment.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentGatewayMethodVo;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentGatewayVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PG사 설정 정보 Dto")
public class PolicyPaymentGatewayDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "PG사 이중화 설정 정보 조회 리스트")
	private	List<PolicyPaymentGatewayVo> rows;

	@ApiModelProperty(value = "PG사 설정 정보 리스트")
	private	List<PolicyPaymentGatewayVo> policyPaymentGatewayList;

	@ApiModelProperty(value = "PG 결제수단 관리 정보 리스트")
	private	List<PolicyPaymentGatewayMethodVo> policyPaymentGatewayMethodList;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;


}
