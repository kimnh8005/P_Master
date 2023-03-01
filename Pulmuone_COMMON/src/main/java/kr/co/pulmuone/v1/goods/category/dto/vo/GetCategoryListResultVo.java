package kr.co.pulmuone.v1.goods.category.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GetCategoryListResultVo
{

	/**
	 * 카테고리 PK
	 */
	private int ilCategoryId;

	/**
	 * 카테고리 명
	 */
	private String categoryName;

	/**
	 * 카테고리 PC 이미지
	 */

	private String pcImage;

	/**
	 * 카테고리 MOBILE 이미지
	 */
	private String mobileImage;


	/**
	 * 하위 카테고리 리스트
	 */
	private List<GetSubCategoryListResultVo> subCategoryList;


}
