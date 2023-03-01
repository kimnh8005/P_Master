package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderExcelServerInfoResponseDto {

    @ApiModelProperty(value = "PROD 서버 여부")
    private String isProdServerYn;
}
