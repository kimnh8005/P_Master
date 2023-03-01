package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "상품할인승인 Vo")
public class GoodsDiscountApprVo extends BaseRequestPageDto {
	
	@ApiModelProperty(value = "상품할인승인 ID", required = true)
	private String ilGoodsDiscountApprId;

	@ApiModelProperty(value = "상품할인 ID", required = true)
	private String ilGoodsDiscountId;
	
	@ApiModelProperty(value = "상품 ID", required = true)
	private String ilGoodsId;
	
	@ApiModelProperty(value = "상품할인유형 공통코드", required = true)
	private String discountTp;
	
	@ApiModelProperty(value = "할인 시작일", required = true)
	private String discountStartDt;
	
	@ApiModelProperty(value = "할인 종료일", required = true)
	private String discountEndDt;
	
	@ApiModelProperty(value = "상품할인 방법 유형 공통코드", required = true)
	private String discountMethodTp;
	
	@ApiModelProperty(value = "상태 처리 시점 원가", required = true)
	private long standardPriceChg;
	
	@ApiModelProperty(value = "할인율(정률할인 시)", required = true)
	private double discountRatio;
	
	@ApiModelProperty(value = "할인판매가(고정가 할인 시)", required = true)
	private int discountSalePrice;
	
	@ApiModelProperty(value = "상품 승인상태(APPR_STAT)", required = true)
	private String apprStat;
	
	@ApiModelProperty(value = "승인요청일", required = true)
	private String apprReqDt;
	
	@ApiModelProperty(value = "승인요청자", required = true)
	private String apprReqUserId;
	
	@ApiModelProperty(value = "승인 1차 담당자", required = true)
	private String apprSubUserId;
	
	@ApiModelProperty(value = "승인 1차 처리자", required = true)
	private String apprSubChgUserId;
	
	@ApiModelProperty(value = "승인 1차 처리일", required = true)
	private String apprSubChgDt;
	
	@ApiModelProperty(value = "승인 2차 담당자", required = true)
	private String apprUserId;
	
	@ApiModelProperty(value = "승인 2차 처리자", required = true)
	private String apprChgUserId;
	
	@ApiModelProperty(value = "승인 2차 처리일", required = true)
	private String apprChgDt;
	
	@ApiModelProperty(value = "자동승인여부", required = true)
	private String apprAutoYn;
	
	@ApiModelProperty(value = "폐기여부", required = true)
	private String apprDisposalYn;
	
	@ApiModelProperty(value = "등록자", required = true)
	private String createId;
	
	@ApiModelProperty(value = "등록일", required = true)
	private String createDt;
	
	@ApiModelProperty(value = "수정자", required = true)
	private String modifyId;
	
	@ApiModelProperty(value = "수정일", required = true)
	private String modifyDt;
	
	@ApiModelProperty(value = "상태 처리 시점 정상가", required = true)
	private long recommendedPriceChg;
	
	@ApiModelProperty(value = "원가", required = true)
	private long standardPrice;
	
	@ApiModelProperty(value = "정상가", required = true)
	private long recommendedPrice;
	
	@ApiModelProperty(value = "이전승인상태", required = true)
	private String prevApprStat;
	
	@ApiModelProperty(value = "상태변경메세지", required = true)
	private String statusCmnt;
}
