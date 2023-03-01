package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApprovalUserVo {

    @ApiModelProperty(value = "유저 PK")
    private String urUserId;

    @ApiModelProperty(value = "계정유형")
    private String adminTypeName;

    @ApiModelProperty(value = "이름")
    private String approvalUserName;

    @ApiModelProperty(value = "로그인 아이디")
    private String approvalLoginId;

    @ApiModelProperty(value = "조직 / 거래처 정보")
    private String organizationName;

    @ApiModelProperty(value = "조직장 여부")
    private String teamLeaderYn;

    @ApiModelProperty(value = "BOS 계정상태")
    private String userStatusName;

    @ApiModelProperty(value = "권한 위임자 로그인 아이디")
    private String grantLoginId;

    @ApiModelProperty(value = "권한 위임자 이름")
    private String grantUserName;

    @ApiModelProperty(value = "권한위임기간 시작일자")
    private String grantAuthStartDate;

    @ApiModelProperty(value = "권한위임기간 종료일자")
    private String grantAuthEndDate;

    @ApiModelProperty(value = "권한위임자 BOS 계정상태")
    private String grantUserStatusName;

}