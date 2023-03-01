package kr.co.pulmuone.v1.display.dictionary.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomDictionaryVo {
    private int no;

    @JsonSerialize(using = ToStringSerializer.class)
    private String dpCustomizeDicId;

    @JsonSerialize(using = ToStringSerializer.class)
    private String customizeWord;

    @JsonSerialize(using = ToStringSerializer.class)
    private String useYn;

    private String createDate;
}
