package kr.co.pulmuone.v1.batch.order.inside.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 배송추적 택배사  Vo
 * </PRE>
 */

@Getter
@Setter
@ToString
public class TrackingShippingCompVo {

	@ApiModelProperty(value = "택배사 PK")
    private Long psShippingCompId;

    @ApiModelProperty(value = "택배사 Key(CJ/LOTTE)")
    private String psShippingCompVal;

    @ApiModelProperty(value = "택배사명")
    private String psShippingCompNm;
}