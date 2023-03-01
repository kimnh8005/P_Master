package kr.co.pulmuone.v1.system.log.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "IllegalDetectLogResultVo")
public class IllegalDetectLogResultVo {

    @ApiModelProperty(value = "부정거래 로그 PK")
    String stIllegalLogId;

    @ApiModelProperty(value = "진행상태")
    String illegalStatusType;

    @ApiModelProperty(value = "진행상태 명")
    String illegalStatusTypeName;

    @ApiModelProperty(value = "부정거래 분류코드")
    String illegalType;

    @ApiModelProperty(value = "부정거래 분류코드 명")
    String illegalTypeName;

    @ApiModelProperty(value = "부정거래 유형 코드")
    String illegalDetailType;

    @ApiModelProperty(value = "부정거래 유형 코드 명")
    String illegalDetailTypeName;

    @ApiModelProperty(value = "부정거래 탐지 내용")
    String illegalDetectCmt;

    @ApiModelProperty(value = "Device Id")
    String urPcidCd;

    @ApiModelProperty(value = "회원 PK ID")
    String urUserId;

    @ApiModelProperty(value = "회원 ID")
    String loginId;

    @ApiModelProperty(value = "회원 ID 그룹")
    String loginIdGroup;

    @ApiModelProperty(value = "주문번호")
    String odid;

    @ApiModelProperty(value = "주문번호 그룹")
    String odidGroup;

    @ApiModelProperty(value = "탐지일시")
    String createDt;

    @ApiModelProperty(value = "부정거래 감지")
    String illegalDetect;

    @ApiModelProperty(value = "처리정보")
    String modifyInfo;

    @ApiModelProperty(value = "회원여부")
    String userYn;

    @ApiModelProperty(value = "탐지일시")
    String detectDate;

    @ApiModelProperty(value = "회원ID수")
    String userCnt;

    @ApiModelProperty(value = "주문횟수")
    String orderCnt;

    @ApiModelProperty(value = "메모")
    String adminMessage;

    @ApiModelProperty(value = "부정거래 탐지내용 Front")
    String illegalDetectFrontCmt;

    @ApiModelProperty(value = "부정거래 탐지내용 End")
    String illegalDetectEndCmt;

}
