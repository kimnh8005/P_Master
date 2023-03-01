package kr.co.pulmuone.v1.calculate.employee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <PRE>
 * Forbiz Korea
 * 임직원 지원금 정산 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 03.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@ApiModel(description = "임직원 지원금 정산 조회 Request Dto")
public class EmployeeListDto {

    @ApiModelProperty(value = "총할인금액")
    private int totalSalePrice;

    @ApiModelProperty(value = "임직원정산(월마감) PK")
    private long caSettleEmployeeMonthId;

    @ApiModelProperty(value = "정산월")
    private String settleMonth;

    @ApiModelProperty(value = "정산시작일자")
    private LocalDateTime startDt;

    @ApiModelProperty(value = "정산종료일자")
    private LocalDateTime endDt;

    @ApiModelProperty(value = "OU 아이디")
    private String ouId;

    @ApiModelProperty(value = "OU 명")
    private String ouNm;

    @ApiModelProperty(value = "할인금액")
    private int salePrice;

    @ApiModelProperty(value = "확정여부(N:대기,Y:확정)")
    private String confirmYn;

    @ApiModelProperty(value = "확정일자")
    private LocalDateTime confirmDt;

    @ApiModelProperty(value = "세션아이디")
    private String sessionId;

}
