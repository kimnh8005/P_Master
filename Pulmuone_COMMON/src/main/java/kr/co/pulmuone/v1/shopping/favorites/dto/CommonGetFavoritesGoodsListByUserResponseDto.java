package kr.co.pulmuone.v1.shopping.favorites.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "CommonGetFavoritesGoodsListByUserResponseDto")
public class CommonGetFavoritesGoodsListByUserResponseDto {
    private int total;
    private List<Long> goodsIdList;
}
