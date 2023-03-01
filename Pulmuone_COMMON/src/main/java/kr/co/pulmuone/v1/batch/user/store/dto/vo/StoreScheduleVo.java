package kr.co.pulmuone.v1.batch.user.store.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreScheduleVo {

	@ApiModelProperty(value = "시스템 코드")
	private String srcSvr;

	@ApiModelProperty(value = "매장코드")
	private String shpCd;

    @ApiModelProperty(value = "권역코드")
    private String dlvAreCd;

    @ApiModelProperty(value = "회차")
    private int dlvSte;

    @ApiModelProperty(value = "주문마감시간")
    private String ordDeaTim;

    @ApiModelProperty(value = "주문배송 시작 시간")
    private String dlvStTim;

    @ApiModelProperty(value = "주문배송 종료 시간")
    private String dlvEndTim;

    @ApiModelProperty(value = "I/F flag")
    private String itfFlg;

    @ApiModelProperty(value = "출고한도")
    private int dlvMaxCnt;

    @ApiModelProperty(value = "매장시간")
    private List<StoreScheduleVo> storeInfo;

    @ApiModelProperty(value = "사용유무")
    private String useYn;

    @ApiModelProperty(value = "Update flag")
    private String updFlg;
}
