package kr.co.pulmuone.v1.display.dictionary.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SynonymDictionaryDto extends BaseRequestDto {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "동의어 관리 PK")
    private String dpSynonymDicId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "검색어(대표동의어")
    private String representSynonym;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "동의어")
    private String synonym;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "사용유무")
    private String useYn;
}
