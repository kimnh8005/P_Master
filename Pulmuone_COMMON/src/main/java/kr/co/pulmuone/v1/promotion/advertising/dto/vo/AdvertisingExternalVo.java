package kr.co.pulmuone.v1.promotion.advertising.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdvertisingExternalVo {

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

    @ApiModelProperty(value = "Redirect URL")
    private String redirectUrl;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "등록자 ID")
    private String createLoginId;

    @ApiModelProperty(value = "등록자 명")
    private String createName;

    @ApiModelProperty(value = "등록일시")
    private String createDateTime;

    @ApiModelProperty(value = "수정자 ID")
    private String modifyLoginId;

    @ApiModelProperty(value = "수정자 명")
    private String modifyName;

    @ApiModelProperty(value = "수정일시")
    private String modifyDateTime;

    @ApiModelProperty(value = "제휴광고 URL")
    private String advertisingUrl;

    @ApiModelProperty(value = "사용여부")
    private String useYnName;

    @ApiModelProperty(value = "담당자")
    private String userInfo;

}
