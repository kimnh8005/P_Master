package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfOrgaShopOrderSearchResponseDto {

    /*
     * ERP API 올가매장주문정보 조회 dto
     */

    @JsonAlias({ "shpCd" })
    private String shpCd; // 매장코드

    @JsonAlias({ "itmNo" })
    private String itmNo; // ERP 품목코드

    @JsonAlias({ "ordCnt" })
    private int ordCnt; // 주문수량


}
