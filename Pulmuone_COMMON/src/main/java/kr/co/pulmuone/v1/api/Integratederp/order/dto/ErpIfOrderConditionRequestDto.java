package kr.co.pulmuone.v1.api.Integratederp.order.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfOrderConditionRequestDto  {

    /*
     * ERP API 송장/미출/매출 완료 dto
     */
	@JsonIgnore //Header와 Line의 join key. 통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함
    private String hrdSeq;

	@JsonIgnore // ERP 전용 key 값.온라인 order key값
    private String oriSysSeq;

	@JsonIgnore	//주문번호
    private String ordNum;

	/*
	 * condition
	 */
	@JsonProperty("condition")
    private Map<String, Object> condition() {

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("hrdSeq", this.hrdSeq);
        conditionMap.put("oriSysSeq", this.oriSysSeq);
        conditionMap.put("ordNum", this.ordNum);

        return conditionMap;
    }

}
