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
public enum O2oExposureTypes implements CodeCommEnum {

    /*
     * O2O 매장상품 전시구분, 매장품목유형
     *
     * - 저울코드(손질) - OD001
     *
     * - 직제조(FRM) - OD002
     *
     * - 사간거래 - OD003
     *
     * - 미대상(일반) - OD004
     */

    NONE("", "미지정"), //
    O2O_01("OD001", "저울코드(손질)"), //
    O2O_02("OD002", "직제조(FRM)"), //
    O2O_03("OD003", "사간거래"), //
    O2O_04("OD004", "미대상(일반)") //
    ;

    private final String code;

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String codeName;

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "itmO2o";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 Json 데이터 내 CODE_KEY 에 해당하는 값
     */
    @JsonCreator // @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
    public static O2oExposureTypes deserializeToEnumByCode( //
            @JsonProperty(O2oExposureTypes.CODE_KEY) String code // ERP API 에서 전송한 매장품목유형
    ) {

        if (code == null) {
            return O2oExposureTypes.NONE;
        }

        O2oExposureTypes o2oExposureType = EnumUtil.getEnum(O2oExposureTypes.class, code);

        if (o2oExposureType == null) {
            return O2oExposureTypes.NONE;
        } else {
            return o2oExposureType;
        }

    }

}
