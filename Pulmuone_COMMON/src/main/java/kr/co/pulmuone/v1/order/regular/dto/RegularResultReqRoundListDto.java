package kr.co.pulmuone.v1.order.regular.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 회차정보 리스트 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 09.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주문 회차정보 리스트 Dto")
public class RegularResultReqRoundListDto {

	@ApiModelProperty(value = "정기배송주문결과PK")
	private long odRegularResultId;

	@ApiModelProperty(value = "회차")
	private int reqRound;

	@ApiModelProperty(value = "도착예정일자")
	private LocalDate arriveDt;

	@ApiModelProperty(value = "신청상태코드")
	private String regularStatusCd;

	@ApiModelProperty(value = "신청상태코드명")
	private String regularStatusCdNm;

	@ApiModelProperty(value = "건너뛰기여부")
	private String regularSkipYn;

	@ApiModelProperty(value = "건너뛰기가능여부")
	private String regularSkipPsbYn;

	@ApiModelProperty(value = "회차종료여부")
	private String regularRoundEndYn;

	@ApiModelProperty(value = "주문서생성여부")
	private String orderCreateYn;

	@ApiModelProperty(value = "회차완료여부")
	private String reqRoundYn;

	@ApiModelProperty(value = "총상품금액")
	private int salePrice;

	@ApiModelProperty(value = "총할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "총배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "총결제예정금액")
	private int paidPrice;

	@ApiModelProperty(value = "결제실패건수")
	private int paymentFailCnt;

	@ApiModelProperty(value = "배송정책목록")
	List<RegularResultShippingZoneListDto> shippingZoneList;
}
