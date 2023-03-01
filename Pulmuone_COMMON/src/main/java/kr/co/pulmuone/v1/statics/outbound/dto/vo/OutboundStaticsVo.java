package kr.co.pulmuone.v1.statics.outbound.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OutboundStaticsVo {

    @ApiModelProperty(value = "구분1")
    private String div1;

    @ApiModelProperty(value = "구분2")
    private String div2;

    @ApiModelProperty(value = "1일")
    private int day1Cnt;

    @ApiModelProperty(value = "2일")
    private int day2Cnt;

    @ApiModelProperty(value = "3일")
    private int day3Cnt;

    @ApiModelProperty(value = "4일")
    private int day4Cnt;

    @ApiModelProperty(value = "5일")
    private int day5Cnt;

    @ApiModelProperty(value = "6일")
    private int day6Cnt;

    @ApiModelProperty(value = "7일")
    private int day7Cnt;

    @ApiModelProperty(value = "8일")
    private int day8Cnt;

    @ApiModelProperty(value = "9일")
    private int day9Cnt;

    @ApiModelProperty(value = "10일")
    private int day10Cnt;

    @ApiModelProperty(value = "11일")
    private int day11Cnt;

    @ApiModelProperty(value = "12일")
    private int day12Cnt;

    @ApiModelProperty(value = "13일")
    private int day13Cnt;

    @ApiModelProperty(value = "14일")
    private int day14Cnt;

    @ApiModelProperty(value = "15일")
    private int day15Cnt;

    @ApiModelProperty(value = "16일")
    private int day16Cnt;

    @ApiModelProperty(value = "17일")
    private int day17Cnt;

    @ApiModelProperty(value = "18일")
    private int day18Cnt;

    @ApiModelProperty(value = "19일")
    private int day19Cnt;

    @ApiModelProperty(value = "20일")
    private int day20Cnt;

    @ApiModelProperty(value = "21일")
    private int day21Cnt;

    @ApiModelProperty(value = "22일")
    private int day22Cnt;

    @ApiModelProperty(value = "23일")
    private int day23Cnt;

    @ApiModelProperty(value = "24일")
    private int day24Cnt;

    @ApiModelProperty(value = "25일")
    private int day25Cnt;

    @ApiModelProperty(value = "26일")
    private int day26Cnt;

    @ApiModelProperty(value = "27일")
    private int day27Cnt;

    @ApiModelProperty(value = "28일")
    private int day28Cnt;

    @ApiModelProperty(value = "29일")
    private int day29Cnt;

    @ApiModelProperty(value = "30일")
    private int day30Cnt;

    @ApiModelProperty(value = "31일")
    private int day31Cnt;

    @ApiModelProperty(value = "합계")
    private int sumCnt;

    @ApiModelProperty(value = "평균")
    private double avgCnt;

}
