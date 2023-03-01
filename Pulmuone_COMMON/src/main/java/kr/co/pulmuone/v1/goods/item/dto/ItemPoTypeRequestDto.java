package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ItemPoTypeRequestDto")
public class ItemPoTypeRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "발주유형 PK")
	private String ilPoTpId;

	@ApiModelProperty(value = "공급업체 PK")
	private String urSupplierId;

	@ApiModelProperty(value = "발주유형명")
	private String poTpNm;

	@ApiModelProperty(value = "발주유형공통코드(PO_TYPE.MOVING:이동발주, PO_TYPE.PRODUCTION:생산발주)")
	private String poTp;

	@ApiModelProperty(value = "물류 입고예정일")
	private String stockPlannedDays;

	@ApiModelProperty(value = "ERP 발주유형")
	private String erpPoTp;

	@ApiModelProperty(value = "발주일")
	private String poChkDates[];

	@ApiModelProperty(value = "품목별 상이 여부(Y:품목별 상이)")
	private String poPerItemYn;

	@ApiModelProperty(value = "발주마감시간(시)")
	private String poDeadlineHour;

	@ApiModelProperty(value = "발주마감시간(분)")
	private String poDeadlineMin;

	@ApiModelProperty(value = "발주일(월)")
	private String checkMon;

	@ApiModelProperty(value = "발주일(화)")
	private String checkTue;

	@ApiModelProperty(value = "발주일(수)")
	private String checkWed;

	@ApiModelProperty(value = "발주일(목)")
	private String checkThu;

	@ApiModelProperty(value = "발주일(금)")
	private String checkFri;

	@ApiModelProperty(value = "발주일(토)")
	private String checkSat;

	@ApiModelProperty(value = "발주일(일)")
	private String checkSun;

	@ApiModelProperty(value = "입고예정일(월)")
	private String scheduledMon;

	@ApiModelProperty(value = "입고예정일(월)")
	private String scheduledTue;

	@ApiModelProperty(value = "입고예정일(월)")
	private String scheduledWed;

	@ApiModelProperty(value = "입고예정일(월)")
	private String scheduledThu;

	@ApiModelProperty(value = "입고예정일(월)")
	private String scheduledFri;

	@ApiModelProperty(value = "입고예정일(월)")
	private String scheduledSat;

	@ApiModelProperty(value = "입고예정일(월)")
	private String scheduledSun;

	@ApiModelProperty(value = "이동요청일(월)")
	private String moveReqMon;

	@ApiModelProperty(value = "이동요청일(화)")
	private String moveReqTue;

	@ApiModelProperty(value = "이동요청일(수)")
	private String moveReqWed;

	@ApiModelProperty(value = "이동요청일(목)")
	private String moveReqThu;

	@ApiModelProperty(value = "이동요청일(금)")
	private String moveReqFri;

	@ApiModelProperty(value = "이동요청일(토)")
	private String moveReqSat;

	@ApiModelProperty(value = "이동요청일(일)")
	private String moveReqSun;

	@ApiModelProperty(value = "PO요청일(월)")
	private String poReqMon;

	@ApiModelProperty(value = "PO요청일(화)")
	private String poReqTue;

	@ApiModelProperty(value = "PO요청일(수)")
	private String poReqWed;

	@ApiModelProperty(value = "PO요청일(목)")
	private String poReqThu;

	@ApiModelProperty(value = "PO요청일(금)")
	private String poReqFri;

	@ApiModelProperty(value = "PO요청일(토)")
	private String poReqSat;

	@ApiModelProperty(value = "PO요청일(일)")
	private String poReqSun;


}
