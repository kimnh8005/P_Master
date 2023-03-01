package kr.co.pulmuone.v1.order.ifday.dto.vo;

import java.time.LocalDate;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IfDayExcelSuccessVo {



    @ApiModelProperty(value = "I/F 성공 PK")
    private String ifIfDayExcelSuccId;

    @ApiModelProperty(value = "주문상세순번")
    private int odOrderDetlSeq;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "I/F 일자")
    private String ifDay;

    @ApiModelProperty(value = "주무PK")
    private Long odOrderId;

    @ApiModelProperty(value = "주문번호상세PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "출고처ID(출고처PK)")
	private Long urWarehouseId;

	@ApiModelProperty(value = "상품ID(상품PK)")
	private Long ilGoodsId;

	@ApiModelProperty(value = "새벽배송여부")
	private Boolean isDawnDelivery;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsCycleTp;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "주문 I/F 일자")
    private LocalDate orderIfDt;

    @ApiModelProperty(value = "출고예정일일자")
    private LocalDate shippingDt;

    @ApiModelProperty(value = "도착예정일일자")
    private LocalDate deliveryDt;

    @ApiModelProperty(value = "성공여부")
    private boolean success;

    @ApiModelProperty(value = "실패구분 U:업로드 B:배치")
    private String failType;

    @ApiModelProperty(value = "주문상태")
    private String orderStatusCd;

    @ApiModelProperty(value = "출고처 주문변경타입")
    private String orderChangeTp;

    public IfDayExcelSuccessVo(){}

    public IfDayExcelSuccessVo(IfDayChangeDto dto){
        this.odOrderDetlSeq = dto.getOdOrderDetlSeq();
        this.odid           = dto.getOdid();
        this.ifDay          = dto.getIfDay();
    }
}
