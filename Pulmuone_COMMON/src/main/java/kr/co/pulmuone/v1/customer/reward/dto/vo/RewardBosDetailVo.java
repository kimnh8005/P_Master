package kr.co.pulmuone.v1.customer.reward.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 고객 보상제 등록/수정 Vo (기본정보, 지급)
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "고객 보상제 등록/수정 Vo (기본정보, 지급)")
public class RewardBosDetailVo {

	@ApiModelProperty(value = "고객보상제 관리 PK")
    private String csRewardId;

	@ApiModelProperty(value = "보상제 명")
    private String rewardNm;

	@ApiModelProperty(value = "보상지급유형")
    private String rewardPayTp;

    @ApiModelProperty(value = "보상지급유형 리스트")
    private List<String> rewardPayTpList;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "주문인정기간 (1개월~6개월)")
    private int orderApprPeriod;

    @ApiModelProperty(value = "진행상태")
    private String statusSe;

    @ApiModelProperty(value = "진행상태명")
    private String statusNm;

    @ApiModelProperty(value = "진행상태 리스트")
    private List<String> statusSeList;

    @ApiModelProperty(value = "신청기간 시작일")
    private String startDt;

    @ApiModelProperty(value = "신청기간 종료일")
    private String endDt;

	@ApiModelProperty(value = "보상제 설명")
    private String rewardContent;

    @ApiModelProperty(value = "보상제 신청 기준 (주문번호, 주문상품, 합배송, 해당없음)")
    private String rewardApplyStandard;

    @ApiModelProperty(value = "보상제 상세 PC")
    private String detlHtmlPc;

    @ApiModelProperty(value = "보상제 상세 MOBILE")
    private String detlHtmlMobile;

    @ApiModelProperty(value = "유의사항")
    private String rewardNotice;

    @ApiModelProperty(value = "적용대상상품 (ALL : 전체, TARGET_GOODS : 지정상품)")
    private String rewardGoodsTp;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "등록자 명")
    private String createUserNm;

    @ApiModelProperty(value = "등록자 계정 ID")
    private String createUserId;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

    @ApiModelProperty(value = "수정자 명")
    private String modifyUserNm;

    @ApiModelProperty(value = "수정자 계정 ID")
    private String modifyUserId;

    @ApiModelProperty(value = "사용자동종료 (Y:N)")
    private String timeOverCloseYn;
}
