package kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 주문조회 API Request DTO")
public class EZAdminOrderInfoRequestDto{

	@ApiModelProperty(value = "API action")
    private String action;

    @ApiModelProperty(value = "조회일자타입")
    private String date_type;

    @ApiModelProperty(value = "조회시작일")
    private String start_date;

    @ApiModelProperty(value = "조회종료일")
    private String end_date;

    @ApiModelProperty(value = "조회페이지")
    private int page;

    @ApiModelProperty(value = "조회값")
    private int limit;

    @ApiModelProperty(value = "CS상태")
    private String order_cs;

    @ApiModelProperty(value = "shop id list")
    private String shop_id;

    public EZAdminOrderInfoRequestDto copy(EZAdminOrderInfoRequestDto dto){
        this.action = dto.getAction();
        this.date_type = dto.getDate_type();
        this.start_date = dto.getStart_date();
        this.end_date = dto.getEnd_date();
        this.page = dto.getPage();
        this.limit = dto.getLimit();
        this.order_cs = dto.getOrder_cs();
        this.shop_id = dto.getShop_id();
        return this;
    }
}
