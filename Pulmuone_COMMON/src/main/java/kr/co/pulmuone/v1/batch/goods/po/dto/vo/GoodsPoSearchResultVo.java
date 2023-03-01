package kr.co.pulmuone.v1.batch.goods.po.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "발주유형 Result Vo")
public class GoodsPoSearchResultVo extends BaseRequestDto {

	@ApiModelProperty(value = "품목별 출고처 PK")
	private int urSupplierId;

	@ApiModelProperty(value = "발주유형명")
	private String poTpNm;

	@ApiModelProperty(value = "발주유형")
	private String poTp;

	@ApiModelProperty(value = "템플릿 유무(Y: 템플릿용, N: 품목별 상이 데이터)")
	private String templateYn;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "발주요일(일)")
	private String poSunYn;

	@ApiModelProperty(value = "발주요일(월)")
	private String poMonYn;

	@ApiModelProperty(value = "발주요일(화)")
	private String poTueYn;

	@ApiModelProperty(value = "발주요일(수)")
	private String poWedYn;

	@ApiModelProperty(value = "발주요일(목)")
	private String poThuYn;

	@ApiModelProperty(value = "발주요일(금)")
	private String poFriYn;

	@ApiModelProperty(value = "발주요일(토)")
	private String poSatYn;

	@ApiModelProperty(value = "입고예정일(일)")
	private int scheduledSun;

	@ApiModelProperty(value = "입고예정일(월)")
	private int scheduledMon;

	@ApiModelProperty(value = "입고예정일(화)")
	private int scheduledTue;

	@ApiModelProperty(value = "입고예정일(수)")
	private int scheduledWed;

	@ApiModelProperty(value = "입고예정일(목)")
	private int scheduledThu;

	@ApiModelProperty(value = "입고예정일(금)")
	private int scheduledFri;

	@ApiModelProperty(value = "입고예정일(토)")
	private int scheduledSat;

	@ApiModelProperty(value = "이동요청일(일)")
	private int moveReqSun;

	@ApiModelProperty(value = "이동요청일(월)")
	private int moveReqMon;

	@ApiModelProperty(value = "이동요청일(화)")
	private int moveReqTue;

	@ApiModelProperty(value = "이동요청일(수)")
	private int moveReqWed;

	@ApiModelProperty(value = "이동요청일(목)")
	private int moveReqThu;

	@ApiModelProperty(value = "이동요청일(금)")
	private int moveReqFri;

	@ApiModelProperty(value = "이동요청일(토)")
	private int moveReqSat;

	@ApiModelProperty(value = "PO요청일(일)")
	private int poReqSun;

	@ApiModelProperty(value = "PO요청일(월)")
	private int poReqMon;

	@ApiModelProperty(value = "PO요청일(화)")
	private int poReqTue;

	@ApiModelProperty(value = "PO요청일(수)")
	private int poReqWed;

	@ApiModelProperty(value = "PO요청일(목)")
	private int poReqThu;

	@ApiModelProperty(value = "PO요청일(금)")
	private int poReqFri;

	@ApiModelProperty(value = "PO요청일(토)")
	private int poReqSat;

	@ApiModelProperty(value = "품목별 상이 여부")
	private String poPerItemYn;

	@ApiModelProperty(value = "발주마감시간(시)")
	private String poDeadlineHour;

	@ApiModelProperty(value = "발주마감시간(분)")
	private String poDeadlineMin ;

	@ApiModelProperty(value = "등록자")
	private String createId;

	@ApiModelProperty(value = "등록일")
	private String createDt;



}
