package kr.co.pulmuone.v1.goods.discount.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsDiscountUploadListVo {

	@ApiModelProperty(value = "상품할인 일괄 업로드 로그 아이디(SEQ)")
	private String ilGoodsDiscountUploadLogId;

	@ApiModelProperty(value = "No")
	private int rownum;

	@ApiModelProperty(value = "No")
	private String rownumStr;

	@ApiModelProperty(value = "상품할인 일괄 업로드 유형(GOODS_DISP(상품할인 일괄업로드), EMPLOYEE_DISC(임직원할인 일괄업로드))")
	private String ilDiscUploadTp;

	@ApiModelProperty(value = "파일명")
	private String fileNm;

	@ApiModelProperty(value = "정상건수")
	private int successCnt;

	@ApiModelProperty(value = "실패건수")
	private int failCnt;

	@ApiModelProperty(value = "등록자")
	private String createId;

	@ApiModelProperty(value = "등록일")
	private String createDt;

	@ApiModelProperty(value = "수정자")
	private String modifyId;

	@ApiModelProperty(value = "아이템 CD")
	private String itemCd;

	@ApiModelProperty(value = "바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "상품코드")
	private String goodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "성공여부")
	private String successYn;

	@ApiModelProperty(value = "사유")
	private String msg;

	@ApiModelProperty(value = "할인구분")
	private String discountTp;

	@ApiModelProperty(value = "할인구분코드값")
	private String discountTpCode;

	@ApiModelProperty(value = "할인구분코드값명")
	private String discountTpCodeNm;

	@ApiModelProperty(value = "할인유형")
	private String discountMethodTp;

	@ApiModelProperty(value = "할인유형코드값")
	private String discountMethodTpCode;

	@ApiModelProperty(value = "할인유형코드값명")
	private String discountMethodTpCodeNm;

	@ApiModelProperty(value = "할인시작일")
	private String discountStartDt;

	@ApiModelProperty(value = "할인종료일")
	private String discountEndDt;

	@ApiModelProperty(value = "할인율(=입력값)")
	private String discountSalePrice;

	@ApiModelProperty(value = "원가")
	private int standardPrice;

	@ApiModelProperty(value = "정가")
	private int recommendedPrice;

	@ApiModelProperty(value = "할인율(=입력받은 할인유형에따라 분기처리)")
	private int discountRatio;

	@ApiModelProperty(value = "할인가(=입력받은 할인유형에따라 분기처리)")
	private int discountPrice;

	@ApiModelProperty(value = "할인율(=입력값)")
	private String discountSalePriceStr;

	@ApiModelProperty(value = "원가")
	private String standardPriceStr;

	@ApiModelProperty(value = "정가")
	private String recommendedPriceStr;

	@ApiModelProperty(value = "할인율(=입력받은 할인유형에따라 분기처리)")
	private String discountRatioStr;

	@ApiModelProperty(value = "할인가(=입력받은 할인유형에따라 분기처리)")
	private String discountPriceStr;

	@ApiModelProperty(value = "공급업체")
	private String compNm;
}
