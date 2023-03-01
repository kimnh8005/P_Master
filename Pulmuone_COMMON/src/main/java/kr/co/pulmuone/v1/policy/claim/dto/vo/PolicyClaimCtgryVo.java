package kr.co.pulmuone.v1.policy.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BOS 클레임 사유 카테고리 VO")
public class PolicyClaimCtgryVo {

	@ApiModelProperty(value = "BOS 클레임 사유 카테고리 PK")
	private Long psClaimCtgryId;

	@ApiModelProperty(value = "클레임 사유 카테고리 10: 대, 20: 중, 30: 귀책처")
	private String categoryCode;

	@ApiModelProperty(value = "클레임 사유명")
	private String claimName;

	@ApiModelProperty(value = "귀책 유형명(S:판매자 귀책 B:구매자 귀책)")
	private String targetTypeName;

	@ApiModelProperty(value = "BOS 클레임 PK")
	private Long psClaimBosId;

	//주문한 공급업처에 따른 클레임사유
	@ApiModelProperty(value = "공급업체명")
	private String supplierName;

	@ApiModelProperty(value = "반품회수-공급업체 코드")
	private String ySupplierCode;

	@ApiModelProperty(value = "반품회수-클레임 사유 코드")
	private String yClaimCode;

	@ApiModelProperty(value = "반품회수 클레임사유")
	private String yClaimName;

	@ApiModelProperty(value = "반품미회수-공급업체 코드")
	private String nSupplierCode;

	@ApiModelProperty(value = "반품미회수-클레임 사유 코드")
	private String nClaimCode;

	@ApiModelProperty(value = "반품미회수 클레임사유")
	private String nClaimName;

	@ApiModelProperty(value = "BOS 클레임 사유 공급업체 PK")
	private Long psClaimBosSupplyId;

	@ApiModelProperty(value = "주문 상세 pk")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "클레임 사유 코드")
	private String claimCode;

	@ApiModelProperty(value = "귀책 유형")
	private String targetType;

	@ApiModelProperty(value = "depth2")
	private String depth2;

	@ApiModelProperty(value = "depth3")
	private String depth3;

}
