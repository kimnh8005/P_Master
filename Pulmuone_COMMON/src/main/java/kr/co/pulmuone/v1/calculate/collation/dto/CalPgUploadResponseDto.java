package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalPgUploadResponseDto {

    @ApiModelProperty(value = "전체 건수")
    private int totalCount;

    @ApiModelProperty(value = "성공 건수")
    private int successCount;

    @ApiModelProperty(value = "실패 건수")
    private int failCount;

    @ApiModelProperty(value = "실패 메세지")
    private String failMessage;

    @ApiModelProperty(value = "PG 대사 상세 조회 건수")
    private Long totalCnt;

    @ApiModelProperty(value = "결제금액합계")
    private Long totalAmt;

}
