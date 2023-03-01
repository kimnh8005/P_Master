package kr.co.pulmuone.mall.shopping.recently.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetRecentlyViewListByUserResponseDto")
public class GetRecentlyViewListByUserResponseDto {
    private int total;
    private List<?> document;
}
