package kr.co.pulmuone.v1.system.basic.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = " GetClassificationListParamDto")
public class GetClassificationListParamDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "아이디", required = false)
    String id;

    @ApiModelProperty(value = "분류PK", required = false)
    String stClassificationId;

    @ApiModelProperty(value = "사용여부", required = false)
    String useYn;

    @ApiModelProperty(value = "타입", required = false)
    String type;

    @ApiModelProperty(value = "타입명", required = false)
    String typeName;

    @ApiModelProperty(value = "샵명", required = false)
    String shopName;

    @ApiModelProperty(value = "깊이", required = false)
    String depth;

    @ApiModelProperty(value = "시작페이지", required = false)
    int sPage;

    @ApiModelProperty(value = "종료페이지", required = false)
    int ePage;
}
