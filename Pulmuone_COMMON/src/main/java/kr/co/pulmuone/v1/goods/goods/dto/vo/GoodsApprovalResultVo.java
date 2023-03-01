package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsApprovalResultVo")
public class GoodsApprovalResultVo {

	@ApiModelProperty(value = "상품승인 PK")
	private String ilGoodsApprId;

	@ApiModelProperty(value = "상품 ID")
	private String goodsId;

	@ApiModelProperty(value = "품목코드")
	private String itemCode;

	@ApiModelProperty(value = "품목바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "승인종류 유형 공통코드(APPR_KIND_TP.ITEM_REGIST : 품목등록, APPR_KIND_TP.ITEM_CLIENT : 거래처 품목수정)")
	private String apprKindType;

	@ApiModelProperty(value = "상품명")
	private String goodsName;

	@ApiModelProperty(value = "상품 설명")
	private String goodsDesc;

	@ApiModelProperty(value = "상품 키워드")
	private String searchKywrd;

	@ApiModelProperty(value = "전시여부")
	private String dispYn;

	@ApiModelProperty(value = "판매 시작일")
	private String saleStartDt;

	@ApiModelProperty(value = "판매 종료일")
	private String saleEndDt;

	@ApiModelProperty(value = "판매 상태")
	private String saleStatus;

	@ApiModelProperty(value = "상품 메모")
	private String goodsMemo;

	@ApiModelProperty(value = "상품유형코드")
	private String goodsTypeCode;

	@ApiModelProperty(value = "상품유형명")
	private String goodsTypeName;

	@ApiModelProperty(value = "공급업체")
	private String supplierName;

	@ApiModelProperty(value = "출고처명")
	private String warehouseName;

	@ApiModelProperty(value = "할인 시작일")
	private String discountStartDt;

	@ApiModelProperty(value = "할인 종료일")
	private String discountEndDt;

	@ApiModelProperty(value = "할인율")
    private int discountRatio;

	@ApiModelProperty(value = "할인액")
    private int discountAmount;

	@ApiModelProperty(value = "할인판매가")
	private int discountSalePrice;

	@ApiModelProperty(value = "원가")
	private int standardPrice;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "마진율")
	private int marginRate;

	@ApiModelProperty(value = "상태 처리 시점 할인율")
    private int discountRatioChg;

	@ApiModelProperty(value = "상태 처리 시점 할인액")
    private int discountAmountChg;

	@ApiModelProperty(value = "상태 처리 시점 할인판매가")
	private int discountSalePriceChg;

	@ApiModelProperty(value = "상태 처리 시점 원가")
	private int standardPriceChg;

	@ApiModelProperty(value = "상태 처리 시점 정상가")
	private int recommendedPriceChg;

	@ApiModelProperty(value = "상태 처리 시점 마진율")
	private int marginRateChg;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "승인상태 명")
	private String apprStatName;

	@ApiModelProperty(value = "할인 유형 코드")
	private String discountType;

	@ApiModelProperty(value = "할인 유형 코드명")
	private String discountTypeName;

	@ApiModelProperty(value = "상품 할인 방법")
	private String discountMethodType;

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

	@ApiModelProperty(value = "과세여부")
	private String taxYn;

	@ApiModelProperty(value = "상품 이미지 경로")
	private String goodsImagePath;

}
