package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;



/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 첨부파일 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 20.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문클레임 OD_CLAIM_ATTC VO")
public class ClaimAttcVo {

	@ApiModelProperty(value = "주문클레임 파일 PK")
	private long odClaimAttcId;

    @ApiModelProperty(value = "주문클레임 PK")
    private long odClaimId;

    @ApiModelProperty(value = "업로드 원본 파일명")
    private String originNm;

    @ApiModelProperty(value = "업로드 파일명")
    private String uploadNm;

    @ApiModelProperty(value = "업로드 경로")
    private String uploadPath;
}
