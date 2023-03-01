package kr.co.pulmuone.v1.statics.pm.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 통계 정보 List Result")
public class PromotionStaticsVo {

    @ApiModelProperty(value = "통계지표")
    private String statics;

    @ApiModelProperty(value = "적립금 합계")
    private String amount;

    @ApiModelProperty(value = "적립금 유형")
    private String pointType;

    @ApiModelProperty(value = "분담조직")
    private String issueDeptName;

    @ApiModelProperty(value = "쿠폰번호")
    private String pmCouponId;

    @ApiModelProperty(value = "쿠폰종류")
    private String couponTp;

    @ApiModelProperty(value = "발급목적")
    private String issuePurpose;

    @ApiModelProperty(value = "전시쿠폰명")
    private String displayCouponNm;

    @ApiModelProperty(value = "관리자쿠폰명")
    private String bosCouponNm;

    @ApiModelProperty(value = "사용가능범위PC")
    private String usePcYn;

    @ApiModelProperty(value = "생성수량")
    private String issueQty;

    @ApiModelProperty(value = "발급수량")
    private long issueCnt;

    @ApiModelProperty(value = "발급금액")
    private long issuePrice;

    @ApiModelProperty(value = "주문건수")
    private long orderCnt;

    @ApiModelProperty(value = "쿠폰할인금액")
    private String discountPrice;

    @ApiModelProperty(value = "매출기여")
    private long paidPrice;

    @ApiModelProperty(value = "구매자수")
    private long userCnt;

    @ApiModelProperty(value = "쿠폰상태")
    private String groupMasterNm;

    @ApiModelProperty(value = "그룹명")
    private String groupNm;

    @ApiModelProperty(value = "사용수량")
    private long useCnt;

    @ApiModelProperty(value = "사용금액")
    private long usePrice;

    @ApiModelProperty(value = "소멸수량")
    private long expirationCnt;

    @ApiModelProperty(value = "소멸금액")
    private long expirationPrice;

    @ApiModelProperty(value = "LEVEL1_페이지")
    private String pageNm;

    @ApiModelProperty(value = "LEVEL2_영역/키워드")
    private String contentNm;

    @ApiModelProperty(value = "주문단가")
    private long orderUnitPrice;
    
    @ApiModelProperty(value = "인단가")
    private long userUnitPrice;

    @ApiModelProperty(value = "대분류(매체)")
    private String source;

    @ApiModelProperty(value = "중분류(구좌)")
    private String medium;

    @ApiModelProperty(value = "소분류(캠페인)")
    private String campaign;

    @ApiModelProperty(value = "세분류(콘텐츠)")
    private String content;

    @JsonProperty("CODE")
    @ApiModelProperty(value = "")
    private String code;

    @JsonProperty("NAME")
    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "고객매출 엑셀표시")
    private String paidPriceFm;

    @ApiModelProperty(value = "주문단가 엑셀표시")
    private String orderUnitPriceFm;

    @ApiModelProperty(value = "인단가 엑셀표시")
    private String userUnitPriceFm;

    @ApiModelProperty(value = "주문건수 엑셀표시")
    private String orderCntFm;

    @ApiModelProperty(value = "구매고객수 엑셀표시")
    private String userCntFm;

    @ApiModelProperty(value = "발급수량 엑셀표시")
    private String issueCntFm;

    @ApiModelProperty(value = "발급금액 엑셀표시")
    private String issuePriceFm;

    @ApiModelProperty(value = "사용수량 엑셀표시")
    private String useCntFm;

    @ApiModelProperty(value = "사용금액 엑셀표시")
    private String usePriceFm;    

    @ApiModelProperty(value = "소멸수량 엑셀표시")
    private String expirationCntFm;

    @ApiModelProperty(value = "소멸금액 엑셀표시")
    private String expirationPriceFm;    

    @ApiModelProperty(value = "합계 엑셀표시")
    private String amountFm;

    @ApiModelProperty(value = "쿠폰할인금액 엑셀표시")
    private String discountPriceFm;

    @ApiModelProperty(value = "생성수량 엑셀표시")
    private String issueQtyFm;

    @ApiModelProperty(value = "사용범위")
    private String useStr;

    @ApiModelProperty(value = "사용범위Mobile")
    private String useMoWebYn;

    @ApiModelProperty(value = "사용범위App")
    private String useMoAppYn;

    @ApiModelProperty(value = "상품아이디")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "묶음상품아이디")
    private String pakageGoodsId;

    @ApiModelProperty(value = "묶음상품명")
    private String pakageGoodsNm;
}
