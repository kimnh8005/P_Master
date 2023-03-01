package kr.co.pulmuone.v1.company.dmmail.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DmMailGroupVo {

    @ApiModelProperty(value = "DM메일 전시그룹 PK")
    private String dmMailGroupId;

    @ApiModelProperty(value = "DM메일 PK")
    private String dmMailId;

    @ApiModelProperty(value = "전시그룹명")
    private String groupNm;

    @ApiModelProperty(value = "설명")
    private String description;

    @ApiModelProperty(value = "그룹설명")
    private String groupDesc;

    @ApiModelProperty(value = "순서")
    private int groupSort;

    @ApiModelProperty(value = "전시상품 노출가격 유형")
    private String dispPriceTp;

    // --------------------------------------------------------------------------
    // 추가항목-상품목록
    // --------------------------------------------------------------------------
    @ApiModelProperty(value = "상품리스트String")
    private String groupGoodsListJsonString;

    @ApiModelProperty(value = "상품리스트")
    private List<DmMailGroupDetlVo> groupGoodsList;
}
