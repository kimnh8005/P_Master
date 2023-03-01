package kr.co.pulmuone.v1.policy.shippingcomp.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.policy.shippingcomp.vo.PolicyShippingCompVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "택배사설정 Dto")
public class PolicyShippingCompRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "택배사설정 조회 리스트")
	private	List<PolicyShippingCompVo> rows;

    @ApiModelProperty(value = "택배사설정 조회 카운트")
	private	int	total;

	@ApiModelProperty(value = "택배사설정.택배사설정 SEQ")
	private long psShippingCompId;

	@ApiModelProperty(value = "택배사설정.택배코드")
	private List<HashMap<String,String>> shippingCompCodeList;

	@ApiModelProperty(value = "택배사설정.택배사명")
	private String shippingCompNm;

	@ApiModelProperty(value = "택배사설정.배송정보추척URL")
	private String trackingUrl;

	@ApiModelProperty(value = "택배사설정.HTTP 전송방법 공통코드")
	private String httpRequestTp;

	@ApiModelProperty(value = "택배사설정.송장파라미터")
	private String invoiceParam;

	@ApiModelProperty(value = "택배사설정.사용여부(Y:사용)")
	private String useYn;

	@ApiModelProperty(value = "택배사설정.외부몰 이지어드민 택배사 코드")
	private String ezadminShippingCompCd;

	@ApiModelProperty(value = "택배사설정.외부몰 이지어드민 택배사명")
	private String ezadminShippingCompNm;

	@ApiModelProperty(value = "택배사설정.외부몰 사방넷 택배사 코드")
	private String sabangnetShippingCompCd;
	
	@ApiModelProperty(value = "택배사설정.전화번호")
	private String shippingCompTel;
}
