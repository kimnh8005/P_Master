package kr.co.pulmuone.v1.promotion.advertising.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class AddAdvertisingExternalRequestDto {

    @ApiModelProperty(value = "외부광고코드 PK")
    private String pmAdExternalCd;

    @ApiModelProperty(value = "광고명")
    private String advertisingName;

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

    @ApiModelProperty(value = "매체")
    private String newSource;

    @ApiModelProperty(value = "구좌")
    private String newMedium;

    @ApiModelProperty(value = "캠페인")
    private String newCampaign;

    @ApiModelProperty(value = "콘텐츠")
    private String newContent;

    @ApiModelProperty(value = "키워드")
    private String newTerm;

    @ApiModelProperty(value = "Redirect URL")
    private String redirectUrl;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "발급회원코드")
    private String userId;

    @ApiModelProperty(value = "외부광고코드 유형")
    private String searchType;

}