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
@ApiModel(description = "ErpIfRequestDto")
public class ErpIfStoreRequestDto {


    @JsonIgnore // JSON 직렬화에서 제외 : 하단 condition 에서 ERP API 규격에 맞게 세팅됨
    private String shpCd; // 품목 코드

    @JsonIgnore // JSON 직렬화에서 제외 : 하단 condition 에서 ERP API 규격에 맞게 세팅됨
    private String itfFlg; // I/F flag

    @JsonIgnore // JSON 직렬화에서 제외 : 하단 condition 에서 ERP API 규격에 맞게 세팅됨
    private String dlvAreCd; // 권역코드

    @JsonIgnore // JSON 직렬화에서 제외 : 하단 condition 에서 ERP API 규격에 맞게 세팅됨
    private int dlvSte; // 회차

    @JsonIgnore // JSON 직렬화에서 제외 : 하단 condition 에서 ERP API 규격에 맞게 세팅됨
    private String schShiDat; // 휴무일


    /*
     * ERP API 규격에 맞게 condition 필드 생성
     */
    @JsonProperty("condition")
    private Map<String, Object> condition() { //

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put(BatchConstants.ERP_ITEM_NO_KEY, this.shpCd);
        conditionMap.put(BatchConstants.ERP_FLAG_KEY, this.itfFlg);
        conditionMap.put(BatchConstants.ERP_DELIVERY_AREA_KEY, this.dlvAreCd);
        conditionMap.put(BatchConstants.ERP_SCHEDULE_NO_KEY, this.dlvSte);
        conditionMap.put(BatchConstants.ERP_UNDELIVER_DT_KEY, this.schShiDat);

        return conditionMap;

    }

}
