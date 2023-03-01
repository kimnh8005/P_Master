package kr.co.pulmuone.bos.system.help;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.system.help.vo.DictionaryMasterVo;
import kr.co.pulmuone.v1.system.help.vo.DictionaryTypes;
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
@ApiModel(description = "DictionarySaveRequestDto")
public class DictionarySaveRequestDto {

    @ApiModelProperty(value = "표준용어 id")
    private Long id;

    @ApiModelProperty(value = "표준용어 type")
    private String inputDictionaryType;

    @ApiModelProperty(value = "표준용어")
    private String baseName;

    public DictionaryMasterVo convert() {
        return DictionaryMasterVo.builder()
            .id(id)
            .baseName(baseName)
            .dictionaryType(DictionaryTypes.to(inputDictionaryType))
            .build();
    }

    public DictionarySaveRequestDto(DictionaryMasterVo vo) {
        this.id = vo.getId();
        this.inputDictionaryType = vo.getDictionaryType().getCode();
        this.baseName = vo.getBaseName();
    }
}
