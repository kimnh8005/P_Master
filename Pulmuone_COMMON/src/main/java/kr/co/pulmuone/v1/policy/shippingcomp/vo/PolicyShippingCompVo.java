package kr.co.pulmuone.v1.policy.shippingcomp.vo;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@SuppressWarnings("rawtypes")
@ApiModel(description = "배송비 탬플릿 Vo")
public class PolicyShippingCompVo extends BaseRequestDto{

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

	@ApiModelProperty(value = "택배사설정.사용여부(Y:사용)")
	private String useYn;

    @ApiModelProperty(value = "등록자ID")
    private String createId;

    @ApiModelProperty(value = "등록일시")
    private String createDt;

    @ApiModelProperty(value = "수정자ID")
    private String modifyId;

    @ApiModelProperty(value = "수정일시")
    private String modifyDt;

	@ApiModelProperty(value = "택배사 코드 리스트")
    private List<HashMap> shippingCompCodeList;

	@ApiModelProperty(value = "택배사 외부몰 코드 리스트")
    private List<PolicyShippingCompOutmallVo> shippingCompOutmallList;
}
