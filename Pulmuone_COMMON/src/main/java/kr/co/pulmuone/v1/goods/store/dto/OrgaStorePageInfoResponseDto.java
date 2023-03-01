package kr.co.pulmuone.v1.goods.store.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.display.contents.dto.ContentsDetailBannerResponseDto;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrgaStorePageInfoResponseDto {

    @ApiModelProperty(value = "배송지 정보")
    private GetSessionShippingResponseDto shippingAddress;

    @ApiModelProperty(value = "배너 정보")
    private List<ContentsDetailBannerResponseDto> banner;

    @ApiModelProperty(value = "매장 정보")
    private ShopVo store;

    @ApiModelProperty(value = "카테고리 정보")
    private List<OrgaStoreCategoryDto> category;

}
