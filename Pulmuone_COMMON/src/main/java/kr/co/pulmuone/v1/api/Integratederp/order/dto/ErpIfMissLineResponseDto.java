package kr.co.pulmuone.v1.api.Integratederp.order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErpIfMissLineResponseDto {

    /*
     * ERP API 송장 dto
     */

	/* Response line */
    @JsonAlias({ "crpCd" })
    private String crpCode; //전송 대상 시스템 구분 코드값

    //default
    @JsonAlias({ "hrdSeq" })
    private String hrdSeq; // Header와 Line의 join key. 통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함

    @JsonAlias({ "oriSysSeq" })
    private String originalSystemSeq; // ERP 전용 key 값.온라인 order key값

    @JsonAlias({ "ordNum" })
    private String orderNumber; // 통합몰 주문번호

    @JsonAlias({ "ordNoDtl" })
    private String ordNoDtl; // 주문상품 순번
    //default

    @JsonAlias({ "dlvNo" })
    private String deliveryNo; // 송장번호

    //condition


}
