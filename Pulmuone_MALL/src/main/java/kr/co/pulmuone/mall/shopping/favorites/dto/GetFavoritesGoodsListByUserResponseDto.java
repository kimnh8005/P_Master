package kr.co.pulmuone.mall.shopping.favorites.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetFavoritesGoodsListByUserResponseDto")
public class GetFavoritesGoodsListByUserResponseDto {
    private int total;
    private List<?> document;
}
