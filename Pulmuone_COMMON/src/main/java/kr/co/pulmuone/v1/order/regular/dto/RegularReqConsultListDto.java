package kr.co.pulmuone.v1.order.regular.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 신청 상담내역 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 08.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 신청 상담내역 Dto")
public class RegularReqConsultListDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "정기배송주문신청메모 PK")
	private long odRegularReqMemoId;

	@ApiModelProperty(value = "정기배송주문신청 PK")
	private long odRegularReqId;

	@ApiModelProperty(value = "메모내용")
	private String memo;

	@ApiModelProperty(value = "등록자")
	private long createId;

	@ApiModelProperty(value = "등록자ID")
	private String loginId;

	@ApiModelProperty(value = "등록자명")
	private String userNm;

	@ApiModelProperty(value = "등록일")
	private LocalDate createDt;
}
