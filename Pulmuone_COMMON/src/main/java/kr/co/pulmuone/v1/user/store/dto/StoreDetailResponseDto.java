package kr.co.pulmuone.v1.user.store.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponCoverageDto;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDeliveryAreaVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDetailVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "매장 상세정보 Response")
public class StoreDetailResponseDto extends BaseResponseDto {

    @ApiModelProperty(value = "리스트")
	private	 StoreDetailVo row;

    @ApiModelProperty(value = "매장권역")
    private List<StoreDeliveryAreaVo> storeDeliveryArea;


}
