package kr.co.pulmuone.v1.display.popup.dto;


import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AddDisplayPopupRequestDto {

    @ApiModelProperty(value = "노출 시작 기간")
    String displayStartDate;

    @ApiModelProperty(value = "노출 종료 기간")
    String displayEndDate;

    @ApiModelProperty(value = "팝업노출채널")
    String displayRangeType;

    @ApiModelProperty(value = "팝업 노출대상")
    String displayTargetType;

    @ApiModelProperty(value = "팝업내용")
    String html = "";

    @ApiModelProperty(value = "링크")
    String linkUrl = "";

    @ApiModelProperty(value = "순번")
    int sort;

    @ApiModelProperty(value = "팝업유형")
    String popupType;

    @ApiModelProperty(value = "팝업제목")
    String popupSubject;

    @ApiModelProperty(value = "오늘 그만보기 노출")
    String todayStopYn;

    @ApiModelProperty(value = "사용여부")
    String useYn;

    @ApiModelProperty(value = "파일정보")
    String addFile;

    @ApiModelProperty(value = "파일 리스트")
    List<FileVo> addFileList;

    @ApiModelProperty(value = "팝업이미지경로")
    String popupImagePath = "";

    @ApiModelProperty(value = "팝업이미지원본파일명")
    String popupImageOriginName = "";

    @ApiModelProperty(value = "등록자")
    String createId;

}
