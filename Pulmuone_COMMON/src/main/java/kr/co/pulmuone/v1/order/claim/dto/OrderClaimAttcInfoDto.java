package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 파일첨부 정보 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 파일첨부 정보 조회 결과 Dto")
public class OrderClaimAttcInfoDto {
	@ApiModelProperty(value = "주주문클레임파일 PK")
	private long odClaimAttcId;

	@ApiModelProperty(value = "주문클레임 환불계좌 PK")
	private long odClaimId;

	@ApiModelProperty(value = "업로드 원본 파일명")
	private String originNm;

	@ApiModelProperty(value = "업로드 파일명")
	private String uploadNm;

	@ApiModelProperty(value = "업로드 경로")
	private String uploadPath;
}
