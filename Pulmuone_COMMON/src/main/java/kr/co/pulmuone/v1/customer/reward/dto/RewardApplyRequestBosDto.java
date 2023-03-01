package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RewardApplyRequestBosDto  extends BaseRequestPageDto {

    @ApiModelProperty(value = "보상제명")
    private String searchRewardApplyName;

    @ApiModelProperty(value = "처리상태")
    private String rewardStatus;

    @ApiModelProperty(value = "처리담당자 검색조건")
    private String searchApplySelect;

    @ApiModelProperty(value = "처리담당자 검색키워드")
    private String findApplyKeyword;

    @ApiModelProperty(value = "처리담당자 검색조건")
    private String searchRequestSelect;

    @ApiModelProperty(value = "신청자 검색키워드")
    private String findRequestKeyword;

    @ApiModelProperty(value = "신청일 시작")
    private String createDateStart;

    @ApiModelProperty(value = "고객보상제 신청관리 ID")
    private String csRewardApplyId;

    @ApiModelProperty(value = "신청일 종료")
    private String createDateEnd;

    @ApiModelProperty(value = "처리상태태 리스")
    private List<String> rewardStatusList;

    @ApiModelProperty(value = "처리지연 구분")
    private String applyDelayView;

    @ApiModelProperty(value = "고객보상제 ID")
    private String csRewardId;

    @ApiModelProperty(value = "고객보상제 ID")
    private String rewardNm;

    @ApiModelProperty(value = "사용자 ID")
    private String urUserId;

    @ApiModelProperty(value = "지급내역 상세")
    private String rewardPayDetl;

    @ApiModelProperty(value = "처리결과")
    private String rewardApplyResult;

    @ApiModelProperty(value = "처리사유")
    private String answer;

    @ApiModelProperty(value = "관리자 메모")
    private String adminCmt;

    @ApiModelProperty(value = "답변방법 문자")
    private String answerSmsYn;

    @ApiModelProperty(value = "답변방법 메일")
    private String answerMailYn;

    @ApiModelProperty(value = "휴대폰번호")
    private String mobileSend;

    @ApiModelProperty(value = "email")
    private String mailSend;

    @ApiModelProperty(value = "보상제명")
    private String rewardName;

    @ApiModelProperty(value = "고객명")
    private String userName;

    @ApiModelProperty(value = "고객명")
    private String noMaskUserName;
    

}
