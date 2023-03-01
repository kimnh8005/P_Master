package kr.co.pulmuone.v1.api.Integratederp.shipto.dto;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErpIfShiptoSrchResponseDto {

    /*
     * ERP API 송장 dto
     */

	/* Response Header */

    @JsonAlias({ "splNam" })
    private String splNam;

    @JsonAlias({ "splId" })
    private String splId;

}



