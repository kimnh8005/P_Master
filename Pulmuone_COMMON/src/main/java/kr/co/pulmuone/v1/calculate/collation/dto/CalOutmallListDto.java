package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 외부몰 주문 대사 조회 Request Dto
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
@ApiModel(description = "외부몰 주문 대사 조회 Request Dto")
public class CalOutmallListDto {

    @ApiModelProperty(value = "성공건수")
    private int successCnt;

    @ApiModelProperty(value = "실패건수")
    private int failCnt;

    @ApiModelProperty(value = "총건수")
    private int totalCnt;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "관리자ID")
    @UserMaskingLoginId
    private String createId;

    @ApiModelProperty(value = "관리자 명")
    @UserMaskingUserName
    private String userNm;

    @ApiModelProperty(value = "외부몰 주문 업로드 Pk")
    private Long odOutMallCompareUploadInfoId;

}
