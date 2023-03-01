package kr.co.pulmuone.v1.user.join.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponInfoByUserJoinVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetJoinResultVo")
public class GetJoinResultVo {

    @ApiModelProperty(value = "회원이름")
    private String userName;

    private List<CouponInfoByUserJoinVo> coupon;

}
