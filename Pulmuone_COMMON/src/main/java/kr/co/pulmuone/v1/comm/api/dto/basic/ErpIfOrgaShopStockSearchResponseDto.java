package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfOrgaShopStockSearchResponseDto {

    /*
     * ERP API 올가매장상품정보 조회 dto
     */

    @JsonAlias({ "shpCd" })
    private String shpCd; // 매장코드

    @JsonAlias({ "curCnt" })
    private double curCnt; // 현재고(5분단위 업데이트) // 데이터에 ".5"가 있어서 double로 지정

    @JsonAlias({ "norPrc" })
    private int norPrc; // 정상가

    @JsonAlias({ "salPrc" })
    private int salPrc; // 판매가(행사가 포함)

    @JsonAlias({ "itmNo" })
    private String itmNo; // ERP 품목코드

    @JsonAlias({ "updDat" })
    private String updDat;

    @JsonAlias({ "ifSeq" })
    private Integer ifSeq; // I/F 시퀀스 번호

    @JsonAlias({ "updFlg" })
    private String updFlg;

    @JsonAlias({ "itfFlg" })
    private String itfFlg;

    @JsonAlias({ "srcSvr" })
    private String srcSvr; // 입력 시스템(예:ERP, ORGA …)

}
