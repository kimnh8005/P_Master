package kr.co.pulmuone.v1.display.popup.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.display.popup.dto.vo.GetDisplayPopupListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetDisplayPopupListResponseDto")
public class GetDisplayPopupListResponseDto  extends BaseResponseDto {

    private List<GetDisplayPopupListResultVo> rows = new ArrayList<GetDisplayPopupListResultVo>();
    private long total;
}
