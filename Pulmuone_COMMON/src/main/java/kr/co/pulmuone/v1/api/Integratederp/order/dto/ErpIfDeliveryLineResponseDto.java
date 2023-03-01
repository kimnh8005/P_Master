package kr.co.pulmuone.v1.api.Integratederp.order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErpIfDeliveryLineResponseDto {

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

    @JsonAlias({ "firDlvDat" })
    private String firstDeliveryDate; // 첫배송완료일자



    /*
    @JsonAlias({ "dlvSta" })
    private String deliveryState;  // 배송상태
								   //	※ 해당 컬럼 업데이트시 0003 (배송전) , 0004 (배송완료) - SYSDATE 기준으로 배송날짜와 비교결과값)


    @JsonAlias({ "dlvComNm" })
    private String deliveryCompanyName; // 택배사/FD,DM의 배송지사 명

    @JsonAlias({ "dlvComCd" })
    private String deliveryCompanyCode; // 택배사코드 FD,DM의 배송지사 코드

    @JsonAlias({ "dlvTel" })
    private String deliveryTelephone; // 배송지사 전화번호

    @JsonAlias({ "dlvTakCd" })
    private String deliveryTakCode; // (DM 필요정보)택배사코드

    @JsonAlias({ "dlvStaCd" })
    private String deliveryStateCode; // 배송 처리 코드

    @JsonAlias({ "dlvCnf" })
    private String deliveryCnf; // 배송완료여부

    @JsonAlias({ "shiCnt" })
    private Integer shipCount; // (CJ물류 필요 정보) 출고수량

    @JsonAlias({ "dlvSchFirDat" })
    private String deliveryScheduleFirstDate; // 첫 배송 예정일  (14자리 년월일시분초)

    @JsonAlias({ "dlvDat" })
    private String deliveryDate; // 출고처리일

    @JsonAlias({ "dlvEndDat" })
    private String deliveryEndDate; // 정기 배송 상품의 배송종료일
	*/



    //condition


}
