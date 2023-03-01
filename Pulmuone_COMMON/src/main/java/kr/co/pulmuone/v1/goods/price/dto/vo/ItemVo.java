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
public class ItemVo {

  public ItemVo () {}

  public ItemVo (Object userInfo) {
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
  @ApiModelProperty(value = "품목PK")
  private String ilItemCd                     ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "품목유형(공통,매장전용,무형,렌탈)")
  private String itemTp                       ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "품목명")
  private String itemNm                       ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "품목바코드")
  private String itemBarcode                  ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "ERP연동여부")
  private String erpIfYn                      ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "ERP재고연동여부")
  private String erpStockIfYn                 ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "표준카테고리PK")
  private String ilCtgryStdId                 ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "공급업체PK")
  private String urSupplierId                 ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "브랜드PK")
  private String urBrandId                    ;

  @JsonSerialize(using = ToStringSerializer.class)
  @ApiModelProperty(value = "상품군")
  private String itemGrp                      ;

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


}
