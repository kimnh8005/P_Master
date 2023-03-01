package kr.co.pulmuone.v1.outmall.order.dto;

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
@ApiModel(description = "수집몰 연동내역 리스트 검색조건 Request Dto")
public class CollectionMallInterfaceListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "연동상태")
    private String syncCode;

    @ApiModelProperty(value = "처리상태")
    private String processCodeFilter;

    @ApiModelProperty(value = "처리상태 리스트")
    private List<String> processCodeList;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    // CLAIM / ORDER
    @ApiModelProperty(value = "이지어드민배치타입")
    private String easyadminBatchTp;

    // get_order_info(주문/클레임) / set_trans_no(송장연동)
    @ApiModelProperty(value = "연동명")
    private String actionNm;
}

