package kr.co.pulmuone.v1.promotion.advertising.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AdvertisingExternalListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "광고명")
    private String advertisingName;

    @ApiModelProperty(value = "외부광고코드 PK")
    private String pmAdExternalCd;

    @ApiModelProperty(value = "매체")
    private String source;

    @ApiModelProperty(value = "구좌")
    private String medium;

    @ApiModelProperty(value = "캠페인")
    private String campaign;

    @ApiModelProperty(value = "콘텐츠")
    private String content;

    @ApiModelProperty(value = "키워드")
    private String term;

    @ApiModelProperty(value = "등록일시")
    private String createStartDate;

    @ApiModelProperty(value = "등록일시")
    private String createEndDate;

    @ApiModelProperty(value = "수정일시")
    private String modifyStartDate;

    @ApiModelProperty(value = "수정일시")
    private String modifyEndDate;

    @ApiModelProperty(value = "사용여부")
    private String useYnFilter;

    @ApiModelProperty(value = "사용여부")
    private List<String> useYnList;

    @ApiModelProperty(value = "담당자조회구분")
    private String searchType;

    @ApiModelProperty(value = "담당자조회값")
    private String searchText;

}
