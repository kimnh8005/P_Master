package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "상품목록 Vo")
public class ItemChangeLogListVo {
	@ApiModelProperty(value = "업데이트 일자")
	private String createDate;

	@ApiModelProperty(value = "품목 ID")
	private String ilItemCode;

	@ApiModelProperty(value = "ERP연동 여부")
	private String erpLinkIfYn;

	@ApiModelProperty(value = "품목 바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "품목유형")
	private String itemType;

	@ApiModelProperty(value = "품목유형명")
	private String itemTypeName;

	@ApiModelProperty(value = "품목명")
	private String itemName;

	@ApiModelProperty(value = "수정담당자")
	private String chargeName;

	@ApiModelProperty(value = "수정담당자 LOGIN ID")
	private String chargeId;

	/* 업데이트 상세 내역 */
	@ApiModelProperty(value = "컬럼명")
	private String columnName;

	@ApiModelProperty(value = "컬럼항목명")
	private String columnLabel;

	@ApiModelProperty(value = "변경전값")
	private String beforeData;

	@ApiModelProperty(value = "변경후값")
	private String afterData;
}
