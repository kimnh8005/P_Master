package kr.co.pulmuone.v1.system.log.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "IllegalDetectLogRequestDto")
public class IllegalDetectLogRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "부정거래 분류")
    String searchIillegalType;

    @ApiModelProperty(value = "부정거래 유형")
    String searchIllegalDetailType;

    @ApiModelProperty(value = "상태")
    String searchIllegalStatusType;

    @ApiModelProperty(value = "상태 리스트")
    private List<String> searchIllegalStatusTypeList;

    @ApiModelProperty(value = "시작생성일")
    String startCreateDate;

    @ApiModelProperty(value = "종료생성일")
    String endCreateDate;

    @ApiModelProperty(value = "부정거래 로그 ID 목록")
    private List<String> stIllegalLogIdList;

    @ApiModelProperty(value = "주문번호 목록")
    private List<String> odidList;

    @ApiModelProperty(value = "수정 진행상태")
    String illegalStatusType;

    @ApiModelProperty(value = "부정거래 로그 PK")
    String stIllegalLogId;
    
    @ApiModelProperty(value = "메모")
    String adminMessage;

}
