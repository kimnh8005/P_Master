package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 API 판매처 정보")
public class EZAdminSellersInfoDto{

	@ApiModelProperty(value = "판매처PK")
	private long omSellersId;

	@ApiModelProperty(value = "판매처그룹코드 공통코드(SELLERS_GROUP_CD")
	private String sellersGroupCd;

	@ApiModelProperty(value = "외부몰코드")
	private String outmallCd;

	@ApiModelProperty(value = "판매처에 등록된 공급업체 리스트")
	private List<OmBasicFeeListDto> supplierList;
}
