package kr.co.pulmuone.v1.customer.inspect.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InspectNoticeVo {

    @ApiModelProperty(value = "정검 시퀀스")
    private int csInspectNoticeId;

    @ApiModelProperty(value = "점검 이름")
    private String inspectName;

    @ApiModelProperty(value = "메인 타이틀")
    private String mainTitle;

    @ApiModelProperty(value = "서브 타이틀")
    private String subTitle;

    @ApiModelProperty(value = "점검 시작 일자")
    private String startDt;

    @ApiModelProperty(value = "점검 종료 일자")
    private String endDt;

    @ApiModelProperty(value = "생성 아이디")
    private Long createId;

    @ApiModelProperty(value = "생성 이름")
    private String createName;

    @ApiModelProperty(value = "생성 일자")
    private String createDt;

    @ApiModelProperty(value = "수정 아이디")
    private Long modifyId;

    @ApiModelProperty(value = "수정 이름")
    private String modifyName;

    @ApiModelProperty(value = "수정 일자")
    private String modifyDt;

}
