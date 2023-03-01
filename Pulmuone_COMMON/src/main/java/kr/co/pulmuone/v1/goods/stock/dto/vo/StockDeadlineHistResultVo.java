package kr.co.pulmuone.v1.goods.stock.dto.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "재고 기한관리 이력관리 Result Vo")
public class StockDeadlineHistResultVo {

	@ApiModelProperty(value = "NO")
	private long no;

	@ApiModelProperty(value = "SEQ")
	private long ilStockDeadlineHistId;

	@ApiModelProperty(value = "타입")
	private String histTp;

	@ApiModelProperty(value = "재고 기한관리 SEQ")
	private long ilStockDeadlineId;

	@ApiModelProperty(value = "공급처 명")
	private String compName;

	@ApiModelProperty(value = "출고처 명")
	private String warehouseName;

	@ApiModelProperty(value = "유통기간")
	private long distributionPeriod;

	@ApiModelProperty(value = "임박기준(일)")
	private long imminent;

	@ApiModelProperty(value = "출고기준(일)")
    private long delivery;

	@ApiModelProperty(value = "목표재고(%)")
	private long targetStockRatio;

	@ApiModelProperty(value = "등록자")
	private int createId;

	@ApiModelProperty(value = "등록자 이름")
	private String createName;

	@ApiModelProperty(value = "등록일자")
	private String createDate;

	@ApiModelProperty(value = "등록자 아이디")
	private String loginId;

	@ApiModelProperty(value = "기본설정")
	private String basicYn;

}
