package kr.co.pulmuone.v1.order.ifday.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class IfDayExcelFailVo {

    @ApiModelProperty(value = "I/F 일자 변경 엑셀 정보 PK")
    private long ifIfDayExcelInfoId;

    @ApiModelProperty(value = "I/F 성공 PK")
    private String ifIfDayExcelSuccId;

    @ApiModelProperty(value = "주문상세순번")
    private long odOrderDetlSeq;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "I/F 일자")
    private String ifDay;

    @ApiModelProperty(value = "실패사유")
    private String failMessage;

    @ApiModelProperty(value = "실패구분 U:업로드 B:배치")
    private String failType;

    public IfDayExcelFailVo(){}

    public IfDayExcelFailVo(IfDayChangeDto dto){
        this.ifIfDayExcelSuccId     = dto.getIfIfDayExcelSuccId();
        this.odOrderDetlSeq         = dto.getOdOrderDetlSeq();
        this.odid                   = dto.getOdid();
        this.ifDay                  = dto.getIfDay();
        this.failMessage            = dto.getFailMessage();
        this.failType               = dto.getFailType();
    }

    public IfDayExcelFailVo(IfDayExcelSuccessVo dto){
        this.ifIfDayExcelSuccId     = dto.getIfIfDayExcelSuccId();
        this.odOrderDetlSeq         = dto.getOdOrderDetlSeq();
        this.odid                   = dto.getOdid();
        this.ifDay                  = dto.getIfDay();
        this.failMessage            = dto.getFailMessage();
    }

}
