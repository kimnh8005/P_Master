package kr.co.pulmuone.v1.goods.goods.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsSpecListResultVo
{

	/**
	 * 상품정보제공고시 항목명
	 */
	private String specFieldName;


	/**
	 * 상품정보제공고시 항목값
	 */
	private String specFieldValue;


}
