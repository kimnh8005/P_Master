package kr.co.pulmuone.v1.batch.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.batch.goods.item.vo.ErpGoodsOrgaShopStockResultVo;
import kr.co.pulmuone.v1.batch.goods.item.vo.ErpGoodsPriceTermResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "올가매장상품정보 조회 완료 요청 HEADER DTO")
public class ErpGoodsOrgaShopStockHeaderRequestDto {

	/*
     * ERP API dto
     */
    @ApiModelProperty(value = "올가매상장품정보 vo")
    private List<ErpGoodsOrgaShopStockResultVo> header;
}
