package kr.co.pulmuone.v1.outmall.order.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CollectionMallInterfaceListVo {

    @ApiModelProperty(value = "연동 PK")
    private long ifEasyadminInfoId;

    @ApiModelProperty(value = "연동상태")
    private String syncCodeName;

    @ApiModelProperty(value = "시작일시")
    private String batchStartDateTime;

    @ApiModelProperty(value = "종료일시")
    private String batchEndDateTime;

    @ApiModelProperty(value = "소요시간")
    private String batchExecutionTime;

    @ApiModelProperty(value = "정상")
    private String successCount;

    @ApiModelProperty(value = "실패")
    private String failCount;

    @ApiModelProperty(value = "처리상태 코드")
    private String processCode;

    @ApiModelProperty(value = "처리상태 코드 명")
    private String processCodeName;

    @ApiModelProperty(value = "관리자 명")
    private String adminName;

    @ApiModelProperty(value = "관리자 ID")
    private String adminId;

    @ApiModelProperty(value = "관리자 로그인 ID")
    private String adminLoginId;

    @ApiModelProperty(value = "API 수집상태")
    private String collectCodeName;

    @ApiModelProperty(value = "주문생성 성공 주문건수")
    private String orderSuccCnt;

    @ApiModelProperty(value = "주문생성 실패 주문건수")
    private String orderFailCnt;
}
