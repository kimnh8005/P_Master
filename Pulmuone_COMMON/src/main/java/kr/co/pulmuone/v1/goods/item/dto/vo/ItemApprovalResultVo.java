package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemApprovalResultVo")
public class ItemApprovalResultVo {

	@ApiModelProperty(value = "품목승인 PK")
	private String ilItemApprId;

	@ApiModelProperty(value = "품목가격 승인 PK")
	private String ilItemPriceApprId;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCode;

	@ApiModelProperty(value = "승인종류 유형 공통코드(APPR_KIND_TP.ITEM_REGIST : 품목등록, APPR_KIND_TP.ITEM_CLIENT : 거래처 품목수정)")
	private String apprKindType;

	@ApiModelProperty(value = "적용시작일")
	private String startDt;

	@ApiModelProperty(value = "품목바코드")
	private String ilItemBarcode;

	@ApiModelProperty(value = "마스터 품목명")
	private String itemName;

	@ApiModelProperty(value = "마스터 품목유형")
	private String itemType;

	@ApiModelProperty(value = "마스터 품목유형명")
	private String itemTypeName;

	@ApiModelProperty(value = "ERP 연동여부")
	private String erpIfYn;

	@ApiModelProperty(value = "공급업체")
	private String supplierName;

	@ApiModelProperty(value = "출고처명")
	private String warehouseName;

	@ApiModelProperty(value = "원가")
	private int standardPrice;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "마진율")
	private int marginRate;

	@ApiModelProperty(value = "처리시점원가")
	private int standardPriceChg;

	@ApiModelProperty(value = "처리시점정상가")
	private int recommendedPriceChg;

	@ApiModelProperty(value = "처리시점마진율")
	private int marginRateChg;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "승인상태 명")
	private String apprStatName;

	@ApiModelProperty(value = "승인요청일")
	private String approvalRequestDt;

	@ApiModelProperty(value = "승인요청자명")
	private String approvalRequestUserName;

	@ApiModelProperty(value = "승인요청자ID")
	private String approvalRequestUserId;

	@ApiModelProperty(value = "1차 승인처리일")
	private String approvalSubChangeDt;

	@ApiModelProperty(value = "1차 승인처리자명")
	private String approvalSubChangeUserName;

	@ApiModelProperty(value = "1차 승인처리자ID")
	private String approvalSubChangeUserId;

	@ApiModelProperty(value = "1차 승인담당자명")
	private String approvalSubUserName;

	@ApiModelProperty(value = "1차 승인담당자ID")
	private String approvalSubUserId;

	@ApiModelProperty(value = "승인처리일")
	private String approvalChangeDt;

	@ApiModelProperty(value = "승인처리자명")
	private String approvalChangeUserName;

	@ApiModelProperty(value = "승인처리자ID")
	private String approvalChangeUserId;

	@ApiModelProperty(value = "승인담당자명")
	private String approvalUserName;

	@ApiModelProperty(value = "승인담당자ID")
	private String approvalUserId;

	@ApiModelProperty(value = "1차 승인권한위임자ID")
	private String approvalSubGrantUserId;

	@ApiModelProperty(value = "1차 승인권한위임자명")
	private String approvalSubGrantUserName;

	@ApiModelProperty(value = "승인권한위임자ID")
	private String approvalGrantUserId;

	@ApiModelProperty(value = "승인권한위임자명")
	private String approvalGrantUserName;

	@ApiModelProperty(value = "승인상태변경 메시지")
	private String apprStatCmnt;

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
