package kr.co.pulmuone.v1.batch.goods.item.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.batch.goods.item.vo.ErpBaekamGoodsItemResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpGoodsItemFlagHeaderCondRequestDto")
public class ErpGoodsItemFlagHeaderCondRequestDto {

	@JsonIgnore
    private String itmNo;

	/*
	 * condition
	 */
	@JsonProperty("condition")
    private Map<String, Object> condition() {

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("itmNo", this.itmNo);
        return conditionMap;
    }

}
