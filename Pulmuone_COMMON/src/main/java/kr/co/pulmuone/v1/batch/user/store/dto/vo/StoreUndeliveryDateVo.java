package kr.co.pulmuone.v1.batch.user.store.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreUndeliveryDateVo {

	@ApiModelProperty(value = "시스템 코드")
	private String srcSvr;

	@ApiModelProperty(value = "매장 코드")
    private String shpCd;

	@ApiModelProperty(value = "휴무일자(출고기준)")
    private String schShiDat;

	@ApiModelProperty(value = "사용유무")
    private String useYn;

	@ApiModelProperty(value = "등록자 ID")
    private long createId;

	@ApiModelProperty(value = "매장휴무일")
    private List<StoreUndeliveryDateVo> storeInfo;

	@ApiModelProperty(value = "Update flag")
	private String updFlg;

}
