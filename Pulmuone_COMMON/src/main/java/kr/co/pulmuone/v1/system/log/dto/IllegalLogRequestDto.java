package kr.co.pulmuone.v1.system.log.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class IllegalLogRequestDto {

    @ApiModelProperty(value = "부정탐지 로그 PK")
    private Long stIllegalLogId;

    @ApiModelProperty(value = "부정거래 분류", required = true)
    private SystemEnums.IllegalType illegalType;

    @ApiModelProperty(value = "부정거래 유형", required = true)
    private SystemEnums.IllegalDetailType illegalDetailType;

    @ApiModelProperty(value = "부정거래 진행상태", hidden = true)
    private SystemEnums.IllegalStatusType illegalStatusType;

    @ApiModelProperty(value = "부정거래 감지 내용")
    private String illegalDetect;
    
    @ApiModelProperty(value = "사용자 환경 정보", required = true)
    private String urPcidCd;

    @ApiModelProperty(value = "탐지 조회 시작일자")
    private String detectStartDate;

    @ApiModelProperty(value = "탐지 조회 종료일자")
    private String detectEndDate;

    @ApiModelProperty(value = "생성자")
    private Long createId;

    @ApiModelProperty(value = "회원 PK List")
    private List<Long> userIdList;

    @ApiModelProperty(value = "주문 PK List")
    private List<Long> orderIdList;

}
