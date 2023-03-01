package kr.co.pulmuone.v1.display.dictionary.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomDictionaryDto extends BaseRequestDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private String dpCustomizeDicId;

    @JsonSerialize(using = ToStringSerializer.class)
    private String customizeWord;

    @JsonSerialize(using = ToStringSerializer.class)
    private String useYn;

}
