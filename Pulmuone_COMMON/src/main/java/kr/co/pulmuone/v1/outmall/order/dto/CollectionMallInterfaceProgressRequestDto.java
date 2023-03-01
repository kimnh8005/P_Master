package kr.co.pulmuone.v1.outmall.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "수집몰 연동내역 처리상태 변경 Request Dto")
public class CollectionMallInterfaceProgressRequestDto {

    @ApiModelProperty(value = "연동내역 PK List")
    private String ifEasyadminInfoIdParam;

    @ApiModelProperty(value = "연동내역 PK List", hidden = true)
    private List<Long> ifEasyadminInfoIdList;

}

