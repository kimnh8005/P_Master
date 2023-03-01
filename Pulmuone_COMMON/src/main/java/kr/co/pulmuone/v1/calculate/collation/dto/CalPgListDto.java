package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <PRE>
 * Forbiz Korea
 * PG 거래 내역 대사 조회 Request Dto
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
@ApiModel(description = "PG 거래 내역 대사 조회 Request Dto")
public class CalPgListDto {

    @ApiModelProperty(value = "구분명")
    private String pgServiceName;

    @ApiModelProperty(value = "성공건수")
    private int successCnt;

    @ApiModelProperty(value = "실패건수")
    private int failCnt;

    @ApiModelProperty(value = "상세 대사 건수")
    private int pgTotalCnt;

    @ApiModelProperty(value = "등록일자")
    private String createDt;

    @ApiModelProperty(value = "관리자 아이디")
    private String createId;

    @ApiModelProperty(value = "관리자 명")
    private String userNm;

    @ApiModelProperty(value = "세션아이디")
    private Long odPgCompareUploadInfoId;

}
