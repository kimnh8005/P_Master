package kr.co.pulmuone.v1.goods.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemReturnPeriodVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@ApiModel(description = "품목별 반품 가능 여부 / 반품 가능기간 조회 response dto")
public class ItemReturnPeriodResponseDto {

    /*
     * 품목별 반품 가능 여부 / 반품 가능기간 조회 response dto
     */

    private static final String RETURN_PERIOD_UNIT = "일";
    private static final String NOT_RETURN_ALLOWED_MESSAGE = "불가";

    private ItemReturnPeriodVo itemReturnPeriodVo; // 품목별 반품 가능 여부 / 반품 가능기간 조회 Vo

    // 화면상에 출력할 반품 가능여부 메시지
    @JsonProperty("returnPeriodMessage")
    private String returnPeriodMessage() {

        if (this.itemReturnPeriodVo.isReturnRequestAvailableYn()) {

            return this.itemReturnPeriodVo.getReturnPeriod() + " " + RETURN_PERIOD_UNIT;

        } else {

            return NOT_RETURN_ALLOWED_MESSAGE;

        }

    }

}
