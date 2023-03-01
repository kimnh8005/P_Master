package kr.co.pulmuone.v1.batch.promotion.point.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.batch.promotion.point.dto.vo.PointExpiredListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PointExpiredListResponseDto {

    @ApiModelProperty(value = "유효기간")
    private String expirationDate;

    @ApiModelProperty(value = "금액")
    private String amount;

    public PointExpiredListResponseDto(PointExpiredListVo vo) {
        this.expirationDate = vo.getExpirationDate();
        this.amount = vo.getAmount();
    }
    
}
