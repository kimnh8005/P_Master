package kr.co.pulmuone.v1.system.log.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@ToString
public class ExcelDownloadAsyncRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "엑셀 다운로드 비동기 PK")
    private Long stExcelDownloadAsyncId;

    @ApiModelProperty(value = "유저 PK")
    private Long urUserId;

    @ApiModelProperty(value = "파일 명")
    private String fileName;

}
