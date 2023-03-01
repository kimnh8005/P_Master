package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExhibitSelectVo {

  public ExhibitSelectVo () {}

  public ExhibitSelectVo (Object userInfo) {
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


  @ApiModelProperty(value = "기획전 골라담기 상세 PK")
  private String evExhibitSelectId;


  @ApiModelProperty(value = "기획전 PK")
  private String evExhibitId;


  @ApiModelProperty(value = "상품별 구매가능 수량")
  private int goodsBuyLimitCnt;


  @ApiModelProperty(value = "기본 구매수량")
  private int defaultBuyCnt;


  @ApiModelProperty(value = "균일가 ")
  private int selectPrice;

  @ApiModelProperty(value = "최대할인율")
  private int maxDiscountRate;

  @ApiModelProperty(value = "대표상품코드PK")
  private String ilGoodsId;


  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "상품리스트")
  private List<ExhibitSelectGoodsVo> selectGoodsList;

  @ApiModelProperty(value = "추가상품리스트")
  private List<ExhibitSelectGoodsVo> selectAddGoodsList;

  @ApiModelProperty(value = "goodsTp")
  private String goodsTp;

  @ApiModelProperty(value = "goodsTpNm")
  private String goodsTpNm;

  @ApiModelProperty(value = "goodsNm")
  private String goodsNm;

  @ApiModelProperty(value = "goodsImagePath")
  private String goodsImagePath;

  @ApiModelProperty(value = "원가")
  private int standardPrice;

  @ApiModelProperty(value = "정상가")
  private int recommendedPrice;

  @ApiModelProperty(value = "판매가")
  private int salePrice;

  @ApiModelProperty(value = "할인유형")
  private String discountTp;

  @ApiModelProperty(value = "할인유형명")
  private String discountTpNm;

  @ApiModelProperty(value = "출고처PK")
  private String urWarehouseId;

  @ApiModelProperty(value = "출고처명")
  private String warehouseNm;

  @ApiModelProperty(value = "배송정책 PK")
  private String ilShippingTmplId;

  @ApiModelProperty(value = "배송정책명")
  private String shippingTemplateName;

  @ApiModelProperty(value = "배송불가지역")
  private String undeliverableAreaTp;

  @ApiModelProperty(value = "배송불가지역명")
  private String undeliverableAreaTpNm;

  @ApiModelProperty(value = "합배송여부")
  private String bundleYn;

  @ApiModelProperty(value = "배송유형")
  private String giftShippingTp;




  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "등록자")
  private String createId                     ;


  @ApiModelProperty(value = "등록일")
  private String createDt                     ;


  @ApiModelProperty(value = "수정자")
  private String modifyId                     ;


  @ApiModelProperty(value = "수정일")
  private String modifyDt                     ;






}
