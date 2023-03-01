package kr.co.pulmuone.v1.user.brand.dto.vo;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "푸시 가능 회원 조회 Param")
public class GetBrandListParamVo extends BaseRequestPageDto {

    @ApiModelProperty(value = "검색구분")
    private String searchClassification;

    @ApiModelProperty(value = "검색 : 휴대폰, EMAIL")
    private String searchClassificationTextValue;

    @ApiModelProperty(value = "검색 : 회원명, 회원ID")
    private List<String> searchClassificationTextareaValueList;

    @ApiModelProperty(value = "회원유형")
    private String userType;

    @ApiModelProperty(value = "회원등급")
    private String userLevel;

    @ApiModelProperty(value = "가입 시작일")
    private String joinDateStart;

    @ApiModelProperty(value = "가입 종료일")
    private String joinDateEnd;

    @ApiModelProperty(value = "최근방문일자 시작일")
    private String lastVisitDateStart;

    @ApiModelProperty(value = "최근방문일자 종료일")
    private String lastVisitDateEnd;

    @ApiModelProperty(value = "기기타입")
    private String deviceType;

    @ApiModelProperty(value = "PUSH 수신여부")
    private String pushReception;

}
