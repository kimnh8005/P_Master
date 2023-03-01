package kr.co.pulmuone.v1.goods.discount.dto.vo;

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
@ApiModel(description = "상품할인 Vo")
public class GoodsDiscountVo {

    @ApiModelProperty(value = "상품 ID")
    private Long goodsId;

    @ApiModelProperty(value = "상품할인 ID")
    private Long goodsDiscountId;

    @ApiModelProperty(value = "상품할인코드")
    private String discountTypeCode;

    @ApiModelProperty(value = "상품할인승인상태코드")
    private String approvalStatusCode;

    @ApiModelProperty(value = "상품할인 시작일자")
    private String discountStartDate;

    @ApiModelProperty(value = "상품할인 시작시간")
    private String discountStartHour;

    @ApiModelProperty(value = "상품할인 시작분")
    private String discountStartMinute;

    @ApiModelProperty(value = "상품할인 종료일자시분초")
    private String discountEndDateTime;

    @ApiModelProperty(value = "상품할인 종료일자")
    private String discountEndDate;

    @ApiModelProperty(value = "상품할인 종료시간")
    private String discountEndHour;

    @ApiModelProperty(value = "상품할인 종료분")
    private String discountEndMinute;

    @ApiModelProperty(value = "상품할인 유형코드")
    private String discountMethodTypeCode;

    @ApiModelProperty(value = "품목 정상가")
    private long itemRecommendedPrice;

    @ApiModelProperty(value = "품목 원가")
    private long itemStandardPrice;

	@ApiModelProperty(value = "상품 원가")
	private int standardPrice;

	@ApiModelProperty(value = "상품 정상가")
	private int recommendedPrice;

    @ApiModelProperty(value = "할인율")
    private String discountRatio;

    @ApiModelProperty(value = "할인액")
    private long discountAmount;

    @ApiModelProperty(value = "할인 판매가")
    private String discountSalePrice;

    @ApiModelProperty(value = "마진율")
    private int marginRate;

    @ApiModelProperty(value = "엑셀업로드 할인값")
    private String discountVal;

    @ApiModelProperty(value = "엑셀업로드 파일이름")
    private String fileNm;

    @ApiModelProperty(value = "성공여부")
    private String successYn;

    @ApiModelProperty(value = "메시지")
    private String msg;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "상품할인 일괄 업로드 유형(GOODS_DISP(상품할인 일괄업로드), EMPLOYEE_DISC(임직원할인 일괄업로드))")
    private String uploadTp;

    @ApiModelProperty(value = "성공갯수")
    private String successCnt;

    @ApiModelProperty(value = "실패갯수")
    private String failCnt;

    @ApiModelProperty(value = "로그ID")
    private String logId;

}
