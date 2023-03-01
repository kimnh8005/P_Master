package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponListByUserVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "쿠폰 정보 목록 조회 ResponseDto")
public class CouponListByUserResponseDto {

    @ApiModelProperty(value = "쿠폰 정보 목록 resultVo")
    private List<CouponListByUserVo> rows;

    @ApiModelProperty(value = "쿠폰 정보 목록 총 갯수")
    private int total;
}
