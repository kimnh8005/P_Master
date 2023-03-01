package kr.co.pulmuone.v1.calculate.employee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 부문 구분 관리 VO
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
@Getter
@Setter
@ToString
@ApiModel(description = "부문 구분 CA_SETTLE_OU_MNG VO")
public class SettleOuMngVo {

    @ApiModelProperty(value = "OU 아이디")
    private String ouId;

    @ApiModelProperty(value = "OU 명")
    private String ouNm;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "등록일")
    private String careateDt;


}
