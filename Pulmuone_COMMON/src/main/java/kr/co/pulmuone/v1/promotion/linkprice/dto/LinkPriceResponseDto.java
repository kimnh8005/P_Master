package kr.co.pulmuone.v1.promotion.linkprice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class LinkPriceResponseDto {

	private long total;

	private List<LinkPriceResultVo> rows;

	@ApiModelProperty(value = "링크프라이스 내역조회")
	private LinkPriceResultVo row;

	@ApiModelProperty(value = "Total data 조회 결과값")
	private String totalResult;
}
