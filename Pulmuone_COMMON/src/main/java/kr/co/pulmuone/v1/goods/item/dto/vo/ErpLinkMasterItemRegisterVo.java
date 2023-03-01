package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpLinkMasterItemRegisterVo {

	 /*
     * ERP 품목검색시 이미 등록된 품목 Vo
     */
	@ApiModelProperty(value = "마스터 품목코드")
    private String itemCd;

	@ApiModelProperty(value = "마스터 품목바코드")
    private String itemBarcode;

	@ApiModelProperty(value = "전시 브랜드 ID")
    private String dpBrandId;

	@ApiModelProperty(value = "전시 브랜드 명")
    private String dpBrandNm;
}
