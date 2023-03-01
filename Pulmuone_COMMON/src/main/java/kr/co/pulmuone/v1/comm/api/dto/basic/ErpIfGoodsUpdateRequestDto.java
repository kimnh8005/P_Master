package kr.co.pulmuone.v1.comm.api.dto.basic;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfGoodsUpdateRequestDto {

    /*
     * ERP API 상품정보 조회 완료 업데이트 dto
     */

    // ERP API 로 json 데이터 송신시 품목코드의 키 값
    private static final String ERP_ITEM_NO_KEY = "itmNo";

    @JsonIgnore // JSON 직렬화에서 제외 : 하단 condition 에서 ERP API 규격에 맞게 세팅됨
    private String ilItemCode; // 품목 코드

    @JsonProperty("useOshYn")
    private String useOnlineShopYn = "Y"; // 온라인 통합몰 취급 상품 여부 (온라인 통합몰 사용 시 SHOP)

    /*
     * ERP API 규격에 맞게 condition 필드 생성
     */
    @JsonProperty("condition")
    private Map<String, Object> condition() { //

        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put(ERP_ITEM_NO_KEY, this.ilItemCode);

        return conditionMap;

    }

}
