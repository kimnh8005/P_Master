package kr.co.pulmuone.v1.system.help.vo;

import java.util.stream.Stream;
import kr.co.pulmuone.v1.comm.enums.CodeCommEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@RequiredArgsConstructor
public enum DictionaryTypes implements CodeCommEnum {

    NONE("", "-"),
    WORD("DIC_TP.1", "단어"),
    TIP("DIC_TP.4", "도움말"),
    ;

    private final String code;
    private final String codeName;

    public static DictionaryTypes to(String code) {
        return Stream.of(values())
            .filter(type -> type.getCode().equalsIgnoreCase(code))
            .findAny()
            .orElse(NONE);
    }
}
