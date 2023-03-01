package kr.co.pulmuone.v1.goods.goods.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsCertificationListResultVo
{

	/**
	 * 인증정보 타이틀
	 */
	private String certificationName;


	/**
	 * 인증정보 이미지
	 */
	private String image;


	/**
	 * 인증정보 상세정보
	 */
	private String certificationDescription;


}
