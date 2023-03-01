package kr.co.pulmuone.v1.batch.user.store.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreInfoVo {

	@ApiModelProperty(value = "배송권역 중계시스템 고유코드")
    private String ifSeq;

    @ApiModelProperty(value = "배송권역 코드")
    private String urDeliveryAreaId;

    @ApiModelProperty(value = "입력 시스템 코드값")
    private String srcSvr;

    @ApiModelProperty(value = "매장유형")
    private String shpTyp;

    @ApiModelProperty(value = "매장 카테고리1")
    private String shpCtg;

    @ApiModelProperty(value = "매장 카테고리2")
    private String shyCtg;

    @ApiModelProperty(value = "O2O매장여부")
    private String o2oTyp;

    @ApiModelProperty(value = "매장상태")
    private String shpSta;

    @ApiModelProperty(value = "매장코드")
    private String shpCd;

    @ApiModelProperty(value = "매장명")
    private String shpNam;

    @ApiModelProperty(value = "대표번호")
    private String tel;

    @ApiModelProperty(value = "우편번호")
    private String shpZip;

    @ApiModelProperty(value = "매장 주소")
    private String shpAdr;

    @ApiModelProperty(value = "매장 위치")
    private String shpLoc;

    @ApiModelProperty(value = "매장영업 시작 시간")
    private String shpOprStDat;

    @ApiModelProperty(value = "매장영업 종료 시간")
    private String shpOprEndDat;

    @ApiModelProperty(value = "매장소개")
    private String shpDes;

    @ApiModelProperty(value = "매장이미지")
    private String shpImg;

    @ApiModelProperty(value = "사용여부")
    private String useYn;

    @ApiModelProperty(value = "공급업체코드")
    private String urSupplierId;

    @ApiModelProperty(value = "등록자 ID")
    private Long createId;

    @ApiModelProperty(value = "매장주문 설정코드")
    private String shpSalSet;

    @ApiModelProperty(value = "배송권역")
    private List<StoreInfoVo> storeInfo;

    @ApiModelProperty(value = "지사코드")
    private String shpCdDm;

    @ApiModelProperty(value = "I/F flag")
    private String itfFlg;

    @ApiModelProperty(value = "Update flag")
    private String updFlg;

}
