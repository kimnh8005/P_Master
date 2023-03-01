package kr.co.pulmuone.v1.goods.goods.dto.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "올가매상장품정보 조회 완료 vo")
public class ErpGoodsOrgaShopStockResultVo {

	@JsonIgnore
    private String shpCd; // 매장코드

	@JsonIgnore
    private String itmNo; // ERP 품목코드

    @JsonIgnore
    private String itfFlg;


	/*
	 * condition
	 */
	@JsonProperty("condition")
    private Map<String, Object> condition() {

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("shpCd", this.shpCd);
        conditionMap.put("itmNo", this.itmNo);
        conditionMap.put("itfFlg", this.itfFlg);
        return conditionMap;
    }
}
