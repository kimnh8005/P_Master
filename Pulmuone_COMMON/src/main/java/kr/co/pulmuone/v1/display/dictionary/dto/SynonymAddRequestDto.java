package kr.co.pulmuone.v1.display.dictionary.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SynonymAddRequestDto extends BaseRequestDto {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "동의어 관리 PK")
    String dpSynonymDicId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "대표여부")
    String representYn;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "동의어")
    String synonym;

}
