package kr.co.pulmuone.v1.batch.goods.item.vo;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "ErpGoodsPriceTermResultVo")
public class ErpGoodsPriceTermResultVo {

	@JsonIgnore
    private Integer ifSeq;

	@JsonIgnore
    private String itmNo;

	/*
	 * condition
	 */
	@JsonProperty("condition")
    private Map<String, Object> condition() {

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("ifSeq", this.ifSeq);
        conditionMap.put("itmNo", this.itmNo);
        return conditionMap;
    }
}
