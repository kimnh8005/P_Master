package kr.co.pulmuone.v1.order.schedule.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfCustordConditionRequestDto  {

    /*
     * ERP API 주문|취소 조회 완료 dto
     */
	@JsonIgnore //Header와 Line의 join key. 통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함
    private String hrdSeq;

	@JsonIgnore	//주문번호
    private String ordNum;

	/*
	 * condition
	 */
	@JsonProperty("condition")
    private Map<String, Object> condition() {


        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("hrdSeq", this.hrdSeq);
        conditionMap.put("ordNum", this.ordNum);
        return conditionMap;
    }

}
