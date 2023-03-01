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
public class SellersListDto extends BaseResponseDto {

	@ApiModelProperty(value="판매처 PK")
	private long omSellersId;

	@ApiModelProperty(value="판매처코드")
	private String sellersCd;

	@ApiModelProperty(value="판매처명")
	private String sellersNm;

	@ApiModelProperty(value="물류구분")
	private String logisticsGubun;

	@ApiModelProperty(value="판매처그룹명")
	private String sellersGroupNm;

	@ApiModelProperty(value="공급처 갯수")
	private int sellersSupplierCnt;

	@ApiModelProperty(value="정산방식")
	private String calcTypeNm;

	@ApiModelProperty(value="판매처 URL")
	private String sellersUrl;

	@ApiModelProperty(value="판매처관리자 URL")
	private String sellersAdminUrl;

	@ApiModelProperty(value="연동여부")
	private String interfaceYnNm;

	@ApiModelProperty(value="등록일")
	private String createDt;

}
