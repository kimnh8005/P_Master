package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@ApiModel(description = "배송불가지역 response Dto")
public class UndeliverableAreaResponseDto {

    /*
     * 배송불가지역 response Dto
     *
     * 배송불가지역 Enum UndeliverableAreaTypes, 공통코드 UNDELIVERABLE_AREA_TP 에 대응됨
     *
     */

    private boolean islandShippingYn; // 도서산간지역 (1권역) 배송여부 ( true : 배송가능 )

    private boolean jejuShippingYn; // 제주지역 (2권역) 배송여부 ( true : 배송가능 )

    // 배송불가지역 Enum 으로 생성
    public UndeliverableAreaResponseDto(String undeliverableAreaTypeCode) {

        for (final ItemEnums.UndeliverableAreaTypes undeliverableAreaType : ItemEnums.UndeliverableAreaTypes.values()) {

            if (undeliverableAreaType.getCode().equals(undeliverableAreaTypeCode)) {

                switch (undeliverableAreaType) {

                case NONE: // 1권역, 2권역 모두 배송 가능시

                    islandShippingYn = true;
                    jejuShippingYn = true;

                    break;

                case A1: // 1권역 배송 불가, 2권역 배송 가능

                    islandShippingYn = false;
                    jejuShippingYn = true;

                    break;

                case A2: // 1권역 배송 가능, 2권역 배송 불가

                    islandShippingYn = true;
                    jejuShippingYn = false;

                    break;

                case A1_A2: // 1권역, 2권역 모두 배송 불가

                    islandShippingYn = false;
                    jejuShippingYn = false;

                    break;

                default:

                }

                break;

            }

        }

    }

}
