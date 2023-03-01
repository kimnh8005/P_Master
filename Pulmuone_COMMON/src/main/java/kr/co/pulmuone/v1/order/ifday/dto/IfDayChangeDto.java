package kr.co.pulmuone.v1.order.ifday.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IfDayChangeDto {

    @ApiModelProperty(value = "외부몰 엑셀 정보 PK")
    private long ifIfDayExcelInfoId;

    @ApiModelProperty(value = "엑셀 업로드 성공 정보 PK")
    private String ifIfDayExcelSuccId;

    @ApiModelProperty(value = "주문상세순번")
    private int odOrderDetlSeq;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "I/F 일자")
    private String ifDay;

    @ApiModelProperty(value = "성공여부")
    private boolean success;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "실패구분 U:업로드 B:배치")
    private String failType;
}
