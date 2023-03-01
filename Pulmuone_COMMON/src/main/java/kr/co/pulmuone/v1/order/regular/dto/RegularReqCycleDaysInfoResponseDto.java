package kr.co.pulmuone.v1.order.regular.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주기 요일 변경 정보 조회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 10.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주기 요일 변경 정보 조회 Response Dto")
public class RegularReqCycleDaysInfoResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "회차")
	private int reqRound;

	@ApiModelProperty(value = "총회차")
	private int totCnt;

	@ApiModelProperty(value = "기간연장횟수")
	private int termExtensionCnt;

	@ApiModelProperty(value = "첫배송예정일자")
	private LocalDate deliveryDt;

	@ApiModelProperty(value = "마지막배송도착예정일자")
	private LocalDate lastArriveDt;

	@ApiModelProperty(value = "기간코드")
	private String goodsCycleTermTp;

	@ApiModelProperty(value = "기간코드명")
	private String goodsCycleTermTpNm;

	@ApiModelProperty(value = "주기코드")
	private String goodsCycleTp;

	@ApiModelProperty(value = "주기코드명")
	private String goodsCycleTpNm;

	@ApiModelProperty(value = "변경주기코드")
	private String changeGoodsCycleTp;

	@ApiModelProperty(value = "변경주기코드명")
	private String changeGoodsCycleTpNm;

	@ApiModelProperty(value = "요일코드")
	private String weekCd;

	@ApiModelProperty(value = "요일코드명")
	private String weekCdNm;

	@ApiModelProperty(value = "변경요일코드")
	private String changeWeekCd;

	@ApiModelProperty(value = "변경요일코드명")
	private String changeWeekCdNm;

	@ApiModelProperty(value = "회차완료여부")
	private String reqRoundYn;

	@ApiModelProperty(value = "다음배송일자")
	private LocalDate nextDeliveryDt;

	@ApiModelProperty(value = "기존건너뛰기여부")
	private String existingSkipYn;

	@ApiModelProperty(value = "휴일목록")
	List<GetHolidayListResultVo> holidayList;

	@ApiModelProperty(value = "도착예정일목록")
	List<RegularReqArriveDtListDto> arriveDtList;
}
