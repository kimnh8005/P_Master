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
public enum TaxInvoiceTypes implements CodeCommEnum { // ( ERP API ) 세금계산서 발행구분 Enum

    NONE("", ""), //
    POS("POS/홈쇼핑", "POS/홈쇼핑"), //
    UNDEFINED("미정의", "미정의"), //
    TASTING("시식/시음/기타", "시식/시음/기타"), //
    BABY_FOOD("이유식", "이유식"), //
    GREEN_JUICE("녹즙정품", "녹즙정품"), //
    CAF("월합", "월합"), //
    GREENCHE("그린체정품", "그린체정품");

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code; // ERP API 에서 전송하는 세금계산서 발행구분

    private final String codeName;

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "recFlg";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 Json 데이터 내 CODE_KEY 에 해당하는 값
     */
    @JsonCreator // @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
    public static TaxInvoiceTypes deserializeToEnumByCode( //
            @JsonProperty(TaxInvoiceTypes.CODE_KEY) String code // ERP API 에서 전송한 세금계산서 발행구분
    ) {

        if (code == null) {
            return TaxInvoiceTypes.NONE;
        }

        return EnumUtil.getEnum(TaxInvoiceTypes.class, code);

    }

}
