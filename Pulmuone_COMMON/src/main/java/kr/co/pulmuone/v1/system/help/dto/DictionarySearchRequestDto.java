package kr.co.pulmuone.v1.system.help.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "DictionarySearchRequestDto")
public class DictionarySearchRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "표준용어 입력값")
    private String baseName;

    @ApiModelProperty(value = "표준용어 type")
    private String dictionaryType;

    @Builder
    public DictionarySearchRequestDto(String baseName, String dictionaryType) {
        this.baseName = baseName;
        this.dictionaryType = dictionaryType;
    }
}
