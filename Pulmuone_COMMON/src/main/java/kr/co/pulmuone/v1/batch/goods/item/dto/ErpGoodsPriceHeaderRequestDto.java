package kr.co.pulmuone.v1.batch.goods.item.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.batch.goods.item.vo.ErpGoodsPriceTermResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpGoodsPriceHeaderRequestDto")
@Builder
public class ErpGoodsPriceHeaderRequestDto {

	/*
     * ERP API dto
     */
    @ApiModelProperty(value = "총 페이지")
    private Integer totalPage;

    @ApiModelProperty(value = "현재 페이지 번호")
    private Integer currentPage;

    @ApiModelProperty(value = "상품가격정보 입력")
    private List<ErpGoodsPriceTermResultVo> header;
}
