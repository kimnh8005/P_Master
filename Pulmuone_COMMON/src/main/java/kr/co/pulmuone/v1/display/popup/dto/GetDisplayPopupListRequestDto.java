package kr.co.pulmuone.v1.display.popup.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class GetDisplayPopupListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "전시대상대상", required = false)
    String displayTargetType;

    @ApiModelProperty(value = "팝업노출대상 리스트", required = false)
    List<String> displayTargetTypeList;

    @ApiModelProperty(value = "팝업유형", required = false)
    String popupType;

    @ApiModelProperty(value = "노출상태", required = false)
    String exposureState;

    @ApiModelProperty(value = "노출상태 리스트", required = false)
    List<String> exposureStateList;

    @ApiModelProperty(value = "사용여부", required = false)
    String useYn;

    @ApiModelProperty(value = "팝업 제목", required = false)
    String popupSubject;

    @ApiModelProperty(value = "노출 채널", required = false)
    String displayRangeType;

    @ApiModelProperty(value = "노출채널 리스트", required = false)
    List<String> displayRangeTypeList;

}
