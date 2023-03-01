package kr.co.pulmuone.v1.batch.goods.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpGoods3PLHeaderRequestDto")
@Builder
public class ErpGoods3PLUpdateHeaderRequestDto {

    @ApiModelProperty(value = "총 페이지")
    private Integer totalPage;

    @ApiModelProperty(value = "현재 페이지 번호")
    private Integer currentPage;

    @ApiModelProperty(value = "3PL상품정보 변경")
    private List<ErpGoods3PLUpdateHeaderCondRequestDto> header;
}
