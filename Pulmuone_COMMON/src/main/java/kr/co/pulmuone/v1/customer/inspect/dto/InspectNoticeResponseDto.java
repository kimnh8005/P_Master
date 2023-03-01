package kr.co.pulmuone.v1.customer.inspect.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class InspectNoticeResponseDto {

    @ApiModelProperty(value = "점검 이름")
    private String inspectName;

    @ApiModelProperty(value = "메인 타이틀")
    private String mainTitle;

    @ApiModelProperty(value = "서브 타이틀")
    private String subTitle;

    @ApiModelProperty(value = "점검 시작 일자")
    private String startDt;

    @ApiModelProperty(value = "점검 시작 시간")
    private String startHour;

    @ApiModelProperty(value = "점검 시작 분")
    private String startMin;

    @ApiModelProperty(value = "점검 종료 일자")
    private String endDt;

    @ApiModelProperty(value = "점검 종료 시간")
    private String endHour;

    @ApiModelProperty(value = "점검 종료 분")
    private String endMin;

    @ApiModelProperty(value = "접근제한 예외 IP목록")
    private List<String> exceptIpList;

    @ApiModelProperty(value = "생성 아이디")
    private String createId;

    @ApiModelProperty(value = "생성 이름")
    private String createName;

    @ApiModelProperty(value = "생성 일자")
    private String createDt;

    @ApiModelProperty(value = "수정 아이디")
    private String modifyId;

    @ApiModelProperty(value = "수정 이름")
    private String modifyName;

    @ApiModelProperty(value = "수정 일자")
    private String modifyDt;

}
