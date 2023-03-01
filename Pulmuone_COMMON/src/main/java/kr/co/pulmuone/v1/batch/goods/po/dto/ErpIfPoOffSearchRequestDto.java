package kr.co.pulmuone.v1.batch.goods.po.dto;

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
@ApiModel(description = "ErpIfPoOffSearchRequestDto")
public class ErpIfPoOffSearchRequestDto {


    @JsonIgnore // JSON 직렬화에서 제외 : 하단 condition 에서 ERP API 규격에 맞게 세팅됨
    private String itmCd; // 품목 코드

    /*
     * ERP API 규격에 맞게 condition 필드 생성
     */
    @JsonProperty("condition")
    private Map<String, Object> condition() { //

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put(BatchConstants.ERP_ITEM_CD, this.itmCd);

        return conditionMap;

    }

}
