package kr.co.pulmuone.v1.batch.user.store.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.constants.BatchConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfDeliveryZoneRequestDto")
public class ErpIfDeliveryZoneRequestDto {


	@JsonIgnore // JSON 직렬화에서 제외 : 하단 condition 에서 ERP API 규격에 맞게 세팅됨
	private String itfFlg; // api 업데이트 flag

	@JsonIgnore // JSON 직렬화에서 제외 : 하단 condition 에서 ERP API 규격에 맞게 세팅됨
	private String srcSvr; // 올가/하이톡

    /*
     * ERP API 규격에 맞게 condition 필드 생성
     */
    @JsonProperty("condition")
    private Map<String, Object> condition() { //

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put(BatchConstants.ERP_FLAG_KEY, this.itfFlg);
        conditionMap.put(BatchConstants.ERP_STORE_TYPE_KEY, this.srcSvr);

        return conditionMap;

    }
}
