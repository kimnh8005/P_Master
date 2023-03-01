package kr.co.pulmuone.v1.batch.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpGoodsItemFlagHeaderReqeustDto")
@Builder
public class ErpGoodsItemFlagHeaderReqeustDto {

	/*
     * ERP API dto
     */
    @ApiModelProperty(value = "총 페이지")
    private Integer totalPage;

    @ApiModelProperty(value = "현재 페이지 번호")
    private Integer currentPage;

    @ApiModelProperty(value = "3PL상품정보 입력")
    private List<ErpGoodsItemFlagHeaderCondRequestDto> header;

}
