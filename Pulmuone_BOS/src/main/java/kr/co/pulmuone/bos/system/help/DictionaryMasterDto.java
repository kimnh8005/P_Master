package kr.co.pulmuone.bos.system.help;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.system.help.vo.DictionaryMasterVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
@ApiModel(description = "DictionaryMasterDto")
public class DictionaryMasterDto {

    @ApiModelProperty(value = "표준용어 id")
    private Long id;

    @ApiModelProperty(value = "표준용어 type")
    private String dictionaryType;

    @ApiModelProperty(value = "표준용어")
    private String baseName;

    public DictionaryMasterDto(DictionaryMasterVo vo) {
        this.id = vo.getId();
        this.dictionaryType = vo.getDictionaryType().getCodeName();
        this.baseName = vo.getBaseName();
    }
}
