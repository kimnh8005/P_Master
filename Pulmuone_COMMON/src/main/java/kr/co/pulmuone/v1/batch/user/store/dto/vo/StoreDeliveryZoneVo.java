package kr.co.pulmuone.v1.batch.user.store.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreDeliveryZoneVo {

	@ApiModelProperty(value = "배송권역 중계시스템 고유코드")
    private String ifSeq;

    @ApiModelProperty(value = "입력 시스템 코드값")
    private String srcSvr;

    @ApiModelProperty(value = "매장코드")
    private String shpCd;

    @ApiModelProperty(value = "배송권역코드")
    private String dlvAreCd;

    @ApiModelProperty(value = "배송권역명")
    private String dlvAreNm;

    @ApiModelProperty(value = "매장배송/매장픽업")
    private String dlvTypCd;

    @ApiModelProperty(value = "매일/격일 타입")
    private String dlvMthTyp;

    @ApiModelProperty(value = "아이템 타입")
    private String dlvItmTyp;

    @ApiModelProperty(value = "사용유무")
    private String useYn;

    @ApiModelProperty(value = "I/F flag")
    private String itfFlg;

    @ApiModelProperty(value = "등록자 ID")
    private long createId;

    @ApiModelProperty(value = "배송권역")
    private List<StoreDeliveryZoneVo> deliveryZone;

    @ApiModelProperty(value = "Update flag")
    private String updFlg;


}
