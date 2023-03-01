package kr.co.pulmuone.v1.comm.api.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;
import kr.co.pulmuone.v1.comm.util.EnumUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum StockWarehouseTypes implements CodeCommEnum { // ( ERP API ) 재고수량 조회 API 의 shiFroOrgId 출고처 ID 구분 Enum

    PFF_YONGIN("802", "식품 용인물류") //
    , PFF_BAEGAM("803", "식품 백암물류") //
    , ORGA_YONGIN("O10", "올가 용인물류(재고)") //
    , OGH_YONGIN("O13", "올가 용인물류(주문)")
    , OGH_SALSE("O20", "올가 (매출)")
    ;

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code; // ERP 재고수량 조회 API 의 shiFroOrgId ( 출고처 ID ) 코드값

    private final String codeName; // 출고처 이름

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "shiFroOrgId";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 Json 데이터 내 CODE_KEY 에 해당하는 값
     */
    @JsonCreator // @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
    public static StockWarehouseTypes deserializeToEnumByCode( //
            @JsonProperty(StockWarehouseTypes.CODE_KEY) String code // ERP 재고수량 조회 API 에서 전송한 shiFroOrgId ( 출고처 ID ) 코드값
    ) {

        if (code == null) {
            return null;
        }

        StockWarehouseTypes stockWarehouseType = EnumUtil.getEnum(StockWarehouseTypes.class, code);

        if (stockWarehouseType == null) {
            return null;
        } else {
            return stockWarehouseType;
        }

    }

}
