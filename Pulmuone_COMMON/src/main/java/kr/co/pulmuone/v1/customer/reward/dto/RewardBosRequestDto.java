package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardTargetGoodsBosVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 고객 보상제 RequestDto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "고객 보상제 RequestDto")
public class RewardBosRequestDto extends BaseRequestPageDto{

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

    @ApiModelProperty(value = "기간 종료 후 사용 자동종료 여부")
    private String timeOverCloseYn;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "등록자")
    private String createUserNm;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

    @ApiModelProperty(value = "수정자")
    private String modifyUserNm;

    @ApiModelProperty(value = "고객보상제 적용대상 상품 리스트")
    private List<RewardTargetGoodsBosVo> rewardTargetGoodsList;

}
