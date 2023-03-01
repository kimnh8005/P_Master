package kr.co.pulmuone.v1.customer.reward.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* <PRE>
* Forbiz Korea
* 고객 보상제 리스트 VO
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021-06-17              최윤지         최초작성
* =======================================================================
* </PRE>
*/

@Getter
@Setter
@ApiModel(description = "고객 보상제 리스트 VO")
public class RewardBosListVo {

	@ApiModelProperty(value = "고객보상제 관리 PK")
    private String csRewardId;

	@ApiModelProperty(value = "보상제 명")
    private String rewardNm;

	@ApiModelProperty(value = "보상제 설명")
    private String rewardContent;

    @ApiModelProperty(value = "보상제 신청 기준 (주문번호, 주문상품, 합배송, 해당없음)")
    private String rewardApplyStandard;

    @ApiModelProperty(value = "보상지급유형(REWARD_PAY_TP.COUPON : 쿠폰, REWARD_PAY_TP.POINT  : 적립금, REWARD_PAY_TP.ETC : 기타)")
    private String rewardPayTp;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "진행상태")
    private String statusSe;

    @ApiModelProperty(value = "진행상태명")
    private String statusNm;

    @ApiModelProperty(value = "보상제 상세 PC")
    private String detlHtmlPc;

    @ApiModelProperty(value = "보상제 상세 MOBILE")
    private String detlHtmlMobile;

    @ApiModelProperty(value = "유의사항")
    private String rewardNotice;

    @ApiModelProperty(value = "신청기간 시작일")
    private String startDt;

    @ApiModelProperty(value = "신청기간 종료일")
    private String endDt;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "등록자 명")
    private String createUserNm;

    @ApiModelProperty(value = "등록자 아이디")
    private String createUserId;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

    @ApiModelProperty(value = "수정자 명")
    private String modifyUserNm;

    @ApiModelProperty(value = "수정자 아이디")
    private String modifyUserId;

}
