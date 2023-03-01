package kr.co.pulmuone.v1.shopping.recently.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "CommonGetRecentlyViewListByUserResponseDto")
public class CommonGetRecentlyViewListByUserResponseDto {
    private int total;
    private List<Long> goodsIdList;
}
