package kr.co.pulmuone.v1.batch.user.store.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreDeliveryAreaVo {

	@ApiModelProperty(value = "시스템 코드")
    private String srcSvr;

	@ApiModelProperty(value = "매장 코드")
    private String shpCd;

	@ApiModelProperty(value = "우편번호")
    private String zip;

	@ApiModelProperty(value = "건물번호")
    private String bldNum;

	@ApiModelProperty(value = "권역정보")
    private String dlvAreCd;

	@ApiModelProperty(value = "사용여부")
    private String useYn;

	@ApiModelProperty(value = "등록자 PK")
    private Long createId;

	@ApiModelProperty(value = "Update flag")
	private String updFlg;


}
