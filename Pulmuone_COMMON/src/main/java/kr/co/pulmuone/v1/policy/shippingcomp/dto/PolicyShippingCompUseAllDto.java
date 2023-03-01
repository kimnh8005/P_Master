package kr.co.pulmuone.v1.policy.shippingcomp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.shippingcomp.vo.PolicyShippingCompOutmallVo;
import kr.co.pulmuone.v1.policy.shippingcomp.vo.PolicyShippingCompVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@ToString
@SuppressWarnings("rawtypes")
@ApiModel(description = "택배사설정 전체 목록 Dto")
public class PolicyShippingCompUseAllDto extends BaseResponseDto {

	@ApiModelProperty(value = "택배사설정 조회 리스트")
	private	List<PolicyShippingCompUseAllDto> rows;

	@ApiModelProperty(value = "택배사설정 조회 카운트")
	private	int	total;

	@ApiModelProperty(value = "택배사설정.택배사설정 SEQ")
	private int psShippingCompId;

	@ApiModelProperty(value = "택배사설정.택배사명")
	private String shippingCompNm;

	@ApiModelProperty(value = "택배사설정.배송정보추척URL")
	private String trackingUrl;

	@ApiModelProperty(value = "택배사설정.HTTP 전송방법 공통코드")
	private String httpRequestTp;

	@ApiModelProperty(value = "택배사설정.송장파라미터")
	private String invoiceParam;

	@ApiModelProperty(value = "택배사 코드")
	private String shippingCompCd;

	@ApiModelProperty(value = "외부몰 유형(E:이지어드민, S:사방넷)")
	private String outmallCd;

	@ApiModelProperty(value = "외부몰 택배사 코드")
	private String outmallShippingCompCd;

	@ApiModelProperty(value = "외부몰 택배사명")
	private String outmallShippingCompNm;

}
