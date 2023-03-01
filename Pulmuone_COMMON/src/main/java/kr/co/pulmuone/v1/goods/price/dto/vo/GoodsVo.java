package kr.co.pulmuone.v1.goods.price.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
//import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsVo {

  public GoodsVo () {}

  public GoodsVo (Object userInfo) {
    // 등록자/수정자 Set
    if (userInfo!= null) {
      if (((UserVo)userInfo).getUserId() != null) {
        this.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        this.setCreateId((SessionUtil.getBosUserVO()).getUserId());
      }
      else {
        this.setModifyId("0");
        this.setCreateId("0");
      }
    }
    else {
      this.setModifyId("0");
      this.setCreateId("0");
    }
  }

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품PK")
  private String ilGoodsId                    ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "품목PK")
  private String ilItemCd                     ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "출고처 관리 PK")
  private String urWarehouseId                ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품유형 공통코드(GOODS_TYPE)")
  private String goodsTp                      ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "판매유형 공통코드(SALE_TYPE)")
  private String saleTp                       ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품명")
  private String goodsNm                      ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "표장용량 구성정보 노출여부(Y:노출)")
  private String packageUnitDispYn            ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "프로모션명")
  private String promotionNm                  ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "프로모션 시작일")
  private String promotionNmStartDt           ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "프로모션 종료일")
  private String promotionNmEndDt             ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품설명")
  private String goodsDesc                    ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "검색키워드")
  private String searchKywrd                  ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품 구매유형 공통코드(PURCHASE_TARGET_TP)")
  private String purchaseTargetTp             ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "회원 구매여부(Y:회원 구매가능)")
  private String purchaseMemberYn             ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "임직원 구매여부(Y:구매가능)")
  private String purchaseEmployeeYn           ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "비회원 구매여부(Y:비회원 구매가능)")
  private String purchaseNonmemberYn          ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "WEB PC 전시여부(Y:전시)")
  private String dispWebPcYn                  ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "WEB MOBILE 전시여부(Y:전시)")
  private String dispWebMobileYn              ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "APP 전시여부(Y:전시)")
  private String dispAppYn                    ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "판매 시작일")
  private String saleStartDt                  ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "판매 종료일")
  private String saleEndDt                    ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "전시여부(Y:전시)")
  private String dispYn                       ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "판매상태 공통코드(SALE_STATUS)")
  private String saleStatus                   ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "단위별 용량정보 자동 표기(Y:자동표기)")
  private String autoDispSizeYn               ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "단위별 용량정보(자동 표기안함 일때 사용)")
  private String sizeEtc                      ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "매장판매 여부(Y:매장판매)")
  private String saleShopYn                   ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "개별 쿠폰 적용여부(Y:적용)")
  private String couponUseYn                  ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "최소구매수량")
  private String limitMinCnt                  ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "최대구매수량유형 공통코드(PURCHASE_LIMIT_MAX_TP)")
  private String limitMaxTp                   ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "최대구매수량 산정기간")
  private String limitMaxDuration             ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "최대구매수량")
  private String limitMaxCnt                  ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "MD추천(Y:추천)")
  private String mdRecommendYn                ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품메모")
  private String goodsMemo                    ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "삭제여부(Y:삭제)")
  private String delYn                        ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상세 하단공지 1 이미지 URL")
  private String noticeBelow1ImgUrl           ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상세 하단공지 1 시작일")
  private String noticeBelow1StartDt          ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상세 하단공지 2 시작일")
  private String noticeBelow2StartDt          ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상세 하단공지 2 이미지 URL")
  private String noticeBelow2ImgUrl           ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상세 하단공지 1 종료일")
  private String noticeBelow1EndDt            ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상세 하단공지 2 종료일")
  private String noticeBelow2EndDt            ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "(묶음상품)상품이미지형식(묶음상품 전용, 묶음/개별상품 조합/개별상품 전용)")
  private String goodsPackageImgTp            ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "(묶음상품)상품상세 기본정보 직접등록 여부(Y:직접등록)")
  private String goodsPackageBasicDescYn      ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "(묶음상품)상품상세 기본정보")
  private String goodsPackageBasicDesc        ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "(묶음상품)동영상 URL")
  private String goodsPackageVideoUrl         ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "(묶음상품)비디오 자동재생 유무(Y:자동재생)")
  private String goodsPackageVideoAutoplayYn  ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "일일상품 유형(GOODS_DAILY_TP : GREENJUICE/BABYMEAL/EATSSLIM )")
  private String goodsDailyTp                 ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "(일일상품)알러지 식단 포함여부(Y:포함)")
  private String goodsDailyAllergyYn          ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "(일일상품)일괄배달 허용여부(Y:허용)")
  private String goodsDailyBulkYn             ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "배치에의한가격,할인변경시간")
  private String batchPriceChangeDt           ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "등록자")
  private String createId                     ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "등록일")
  private String createDt                     ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "수정자")
  private String modifyId                     ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "수정일")
  private String modifyDt                     ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "선물하기 허용 여부")
  private String presentYn                     ;

  // --------------------------------------------------------------------------
  // IL_GOODS_PACKAGE_GOODS_MAPPING
  // --------------------------------------------------------------------------
  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "묶음상품PK")
  private String ilGoodsPackageGoodsMappingId;


}
