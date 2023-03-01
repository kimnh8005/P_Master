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
public enum OrganizationTypes implements CodeCommEnum { // ( ERP API ) 온라인 통합몰 구분값 Enum

    NONE("", "미지정"), //
    LOHAS("228", "건강생활"), //
    FOOD("165", "풀무원식품"), //
    ORGA("3836", "올가"), //
    FOODMERCE("3810", "푸드머스");

    @JsonValue // 해당 속성의 값을 Json 직렬화시 값으로 지정
    private final String code;

    private final String codeName;

    // ERP API 에서 사용하는 해당 코드의 키 값 => Json 데이터 내에서 해당 키 값 사용
    public static final String CODE_KEY = "orgId";

    /*
     * Json => Java Object 역직렬화시 @JsonProperty 로 지정된 해당 코드에 해당하는 Enum 반환 메서드
     *
     * @Param String code : ERP API 에서 전송한 Json 데이터 내 CODE_KEY 에 해당하는 값
     */
    @JsonCreator // @JsonCreator로 지정된 static 메서드를 Jackson ObjectMapper 가 역직렬화시 사용함
    public static OrganizationTypes deserializeToEnumByCode( //
            @JsonProperty(OrganizationTypes.CODE_KEY) String code // ERP API 에서 전송한 온라인 통합몰 구분값
    ) {

        if (code == null) {
            return OrganizationTypes.NONE;
        }

        return EnumUtil.getEnum(OrganizationTypes.class, code);

    }

}
