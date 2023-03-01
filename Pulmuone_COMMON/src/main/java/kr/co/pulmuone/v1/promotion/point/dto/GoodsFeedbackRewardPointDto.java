package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.FeedbackEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.promotion.point.dto.vo.GoodsFeedbackPointRewardSettingVo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@ApiModel(description = "상품 후기 적립금 적립 Dto")
public class GoodsFeedbackRewardPointDto {

    @ApiModelProperty(value = "적립금 회원등급 고유값")
    private Long pmPointUserGradeId;

    @ApiModelProperty(value = "적립금 고유값")
    private Long pmPointId;

    @ApiModelProperty(value = "적립금 처리 유형")
    private PointEnums.PointProcessType pointProcessTp;

    @ApiModelProperty(value = "적립금 정산 유형")
    private PointEnums.PointSettlementType pointSettlementTp;

    @ApiModelProperty(value = "적립금 정산 법인 코드")
    private String issueDeptCd;

    @ApiModelProperty(value = "일반 후기 적립금")
    private Long nomalAmount;

    @ApiModelProperty(value = "포토 후기 적립금")
    private Long photoAmount;

    @ApiModelProperty(value = "프리미엄 후기 적립금")
    private Long premiumAmount;

    @ApiModelProperty(value = "일반 후기 유효일")
    private Long nomalValidityDay;

    @ApiModelProperty(value = "포토 후기 유효일")
    private Long photoValidityDay;

    @ApiModelProperty(value = "프리미엄 후기 유효일")
    private Long premiumValidityDay;

    @ApiModelProperty(value = "지급 기준일")
    private int paymentStandardDetlVal;

    @ApiModelProperty(value = "즉시 지급 여부")
    private boolean isDepositPointsImmediately;

    @ApiModelProperty(value = "적립/차감액")
    private Long amount;

    @ApiModelProperty(value = "포인트 적립일")
    private String depositDt;

    @ApiModelProperty(value = "포인트 만료일")
    private String expirationDt;

    @Builder
    public GoodsFeedbackRewardPointDto(GoodsFeedbackPointRewardSettingVo goodsFeedbackPointRewardSettingVo, FeedbackEnums.FeedbackType feedbackType){
        this.pmPointUserGradeId = goodsFeedbackPointRewardSettingVo.getPmPointUserGradeId();
        this.pmPointId = goodsFeedbackPointRewardSettingVo.getPmPointId();
        this.pointProcessTp = goodsFeedbackPointRewardSettingVo.getPointProcessTp();
        this.pointSettlementTp = goodsFeedbackPointRewardSettingVo.getPointSettlementTp();
        this.issueDeptCd = goodsFeedbackPointRewardSettingVo.getIssueDeptCd();
        this.nomalAmount = goodsFeedbackPointRewardSettingVo.getNomalAmount();
        this.photoAmount = goodsFeedbackPointRewardSettingVo.getPhotoAmount();
        this.premiumAmount = goodsFeedbackPointRewardSettingVo.getPremiumAmount();
        this.nomalValidityDay = goodsFeedbackPointRewardSettingVo.getNomalValidityDay();
        this.photoValidityDay = goodsFeedbackPointRewardSettingVo.getPhotoValidityDay();
        this.premiumValidityDay = goodsFeedbackPointRewardSettingVo.getPremiumValidityDay();
        this.paymentStandardDetlVal = goodsFeedbackPointRewardSettingVo.getPaymentStandardDetlVal();
        this.isDepositPointsImmediately = this.paymentStandardDetlVal==0;
        this.amount = 0L;
        if(FeedbackEnums.FeedbackType.NORMAL == feedbackType) this.amount = this.nomalAmount;
        if(FeedbackEnums.FeedbackType.PHOTO == feedbackType) this.amount = this.photoAmount;
        if(FeedbackEnums.FeedbackType.PREMIUM == feedbackType) this.amount = this.premiumAmount;
        this.depositDt = LocalDate.now().plusDays(this.paymentStandardDetlVal).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(FeedbackEnums.FeedbackType.NORMAL == feedbackType) this.expirationDt = LocalDate.now().plusDays(this.nomalValidityDay).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(FeedbackEnums.FeedbackType.PHOTO == feedbackType) this.expirationDt = LocalDate.now().plusDays(this.photoValidityDay).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(FeedbackEnums.FeedbackType.PREMIUM == feedbackType) this.expirationDt = LocalDate.now().plusDays(this.premiumValidityDay).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
