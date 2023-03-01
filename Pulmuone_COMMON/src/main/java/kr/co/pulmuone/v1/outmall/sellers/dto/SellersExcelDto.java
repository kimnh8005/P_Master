package kr.co.pulmuone.v1.outmall.sellers.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "판매처 목록 응답 Response Dto")
public class SellersExcelDto extends BaseResponseDto {

	@ApiModelProperty(value="판매처 PK")
	private long omSellersId;

	@ApiModelProperty(value="판매처명")
	private String sellersNm;

	@ApiModelProperty(value="물류구분")
	private String logisticsGubun;

	@ApiModelProperty(value="판매처그룹명")
	private String sellersGroupNm;

	@ApiModelProperty(value="공급처코드")
	private String supplierCd;

	@ApiModelProperty(value="공급처명")
	private String supplierNm;

	@ApiModelProperty(value="정산방식")
	private String calcType;

	@ApiModelProperty(value="수수료")
	private String fee;

	@ApiModelProperty(value="판매처 URL")
	private String sellersUrl;

	@ApiModelProperty(value="판매처관리자 URL")
	private String sellersAdminUrl;

	@ApiModelProperty(value="연동여부")
	private String interfaceYn;

	@ApiModelProperty(value="물류IF 연동여부")
	private String erpInterfaceYn;

	@ApiModelProperty(value="사용여부")
	private String useYn;

	@ApiModelProperty(value="등록일")
	private String createDt;

}
