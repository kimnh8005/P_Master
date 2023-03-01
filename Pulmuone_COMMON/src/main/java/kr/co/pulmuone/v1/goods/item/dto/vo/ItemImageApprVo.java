package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemImageApprVo {

	private String ilItemImgId;

	private String ilItemApprId;

	private String ilItemCd;

	private boolean basicYn;

	private String imgNm;

	private String imgOriginNm;

	private String bImg;

	private String mImg;

	private String msImg;

	private String sImg;

	private String cImg;

	private int sort;

    private Long createId; // 등록자

    private Long modifyId; // 수정자
}
