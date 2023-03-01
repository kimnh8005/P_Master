package kr.co.pulmuone.v1.batch.system.cache.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "API Cache Result")
public class SystemCacheVo {
    @ApiModelProperty(value = "API Cache PK")
    private Long stApiCacheId;

    @ApiModelProperty(value = "API URL")
    private String apiUrl;

    @ApiModelProperty(value = "캐쉬 파일 경로")
    private String filePathName;

    @ApiModelProperty(value = "캐쉬 파일 명")
    private String cacheData = "";

    @ApiModelProperty(value = "캐쉬 시간")
    private String cacheTime;
}
