package kr.co.pulmuone.v1.outmall.sellers.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "판매처 공급처")
public class SellersSuppilerVo extends BaseRequestPageDto {

	@ApiModelProperty(value="판매처공급처 PK")
	private long omSellersSupplierId;

	@ApiModelProperty(value="판매처 PK")
	private long omSellersId;

	@ApiModelProperty(value="공급처 PK")
	private String urSupplierId;

	@ApiModelProperty(value="공급처코드")
	private String supplierCode;

	@ApiModelProperty(value="공급처명")
	private String supplierName;

	@ApiModelProperty(value="정산방식")
	private String calcType;

	@ApiModelProperty(value="수수료")
	private int fee;

}
