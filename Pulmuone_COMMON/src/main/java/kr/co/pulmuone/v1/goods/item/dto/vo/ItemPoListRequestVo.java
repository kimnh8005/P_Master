package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemPoListRequestVo {

	@ApiModelProperty(value = "낱개발주수량")
    private String piecePoQty;

	@ApiModelProperty(value = "발주SEQ")
    private String ilPoId;

	@ApiModelProperty(value = "메모")
    private String memo;

	@ApiModelProperty(value = "등록자")
    private String createId;

}
