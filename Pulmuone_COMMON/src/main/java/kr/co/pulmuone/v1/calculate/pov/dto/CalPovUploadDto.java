package kr.co.pulmuone.v1.calculate.pov.dto;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * POV 조회 Upload Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "POV 조회 Upload Dto")
public class CalPovUploadDto {
    private String corporationCode;

    private String channelCode;

    private String skuCode;

    private String accountCode;

    private BigDecimal cost;

    private String factoryCode;
}
