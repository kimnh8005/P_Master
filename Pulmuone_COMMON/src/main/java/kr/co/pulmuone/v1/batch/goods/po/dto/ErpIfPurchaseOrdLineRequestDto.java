package kr.co.pulmuone.v1.batch.goods.po.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfPurchaseOrdLineRequestDto")
public class ErpIfPurchaseOrdLineRequestDto implements Cloneable {

	/*
     * ERP API 구매발주 입력 Line dto
     */
	@ApiModelProperty(value = "전송 대상 시스템 구분 코드값")
    private String crpCd;

	@ApiModelProperty(value = "발주 구분자")
    private String ordGub;

	@ApiModelProperty(value = "ERP 전용 key 값")
	private String oriSysSeq;

	@ApiModelProperty(value = "식품 - FD 올가 - R1, R2 푸드머스 - FM")
	private String ordTyp;

	@ApiModelProperty(value = "ERP품목코드 (CHNN_GOODS_NO)")
	private String itmNo;

	@ApiModelProperty(value = "품목명")
	private String itmNam;

	@ApiModelProperty(value = "주문수량")
	private long ordCnt;

	@ApiModelProperty(value = "이동발주수량")
    private long reqCnt;

	@ApiModelProperty(value = "발주일자 (이동발주시 SYSDATE+1) (14자리 년월일시분초)")
	private String reqDat;

    @ApiModelProperty(value = "PO발주일자(14자리 년월일시분초)")
    private String poReqDat;

    @ApiModelProperty(value = "이동일자 (14자리 년월일시분초)")
    private String moveReqDat;

    @ApiModelProperty(value = "조직코드")
	private String orgCd;

	@ApiModelProperty(value = "납품처")
	private long shiToOrgId;

    @ApiModelProperty(value = "Header와 Line의 join key")
    private String hdrSeq;

    @ApiModelProperty(value = "운영조직")
    private String orgId;

    @ApiModelProperty(value = "KAN 코드")
    private String kanCd;

    @ApiModelProperty(value = "ERP 품목코드")
    private String invItmId;

    @ApiModelProperty(value = "(ERP 필요 정보)_단위")
    private String ordCntUom;

    @ApiModelProperty(value = "ERP 전용 Order Line Key 값")
    private String ordNoDtl;

    @ApiModelProperty(value = "(ERP 필요 정보)_Validation Status(Y/N)")
    private String vdtSta;

    @ApiModelProperty(value = "(ERP 필요 정보)_DRP에서 정보를 읽었는지 여부(Y/N)")
    private String drpFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문/발주 연계 유형")
    private String ordPoTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문출처(ERS/BABS/ECOS/ECMS/ERF)")
    private String ordSrc;

    @ApiModelProperty(value = "(ERP 필요 정보)_Order Merge Flag")
    private String ordMrgFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_Validation 시 : 프로모션 라인일 경우 'Y', Legacy에서 Insert 시 : 'N'으로 입력")
    private String pmtLinFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_라인 유형 카테고리 코드")
    private String linCatCd;

    @ApiModelProperty(value = "(ERP 필요 정보)_단가계산플래그")
    private String cclPrcFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_CONTEXT")
    private String ctx;

    @ApiModelProperty(value = "고객 배송 요청일")
    private String dvlReqDat;

    @ApiModelProperty(value = "(ERP 필요 정보)_고객요청일자 입력(반품시에는 반품에 대한 회수 받기를 원하는 요청일)")
    private String prmDat;

    @ApiModelProperty(value = "(ERP 필요 정보)_(10: 물류회수처리 20: 물류회수택배처리 30: 미회수환품 40: 자체폐기)")
    private String rtnTrcTyp;

    @ApiModelProperty(value = "주문마감 여부")
    private String clsSta;

    @ApiModelProperty(value = "(ERP 필요 정보)_Receipt Org ID(물류)")
    private long recOrgId;

    @ApiModelProperty(value = "(CJ물류 필요 정보)_입고처코드")
    private String subInvCd;

    @ApiModelProperty(value = "(CJ물류 필요 정보)_입고처명")
    private String subInvNam;

    @ApiModelProperty(value = "배치유형")
    private String poBatchTp;

    @ApiModelProperty(value = "시스템발주수량")
    private String poSystemQty;

    @ApiModelProperty(value = "i/f성공여부")
    private String poIfYn;

    @ApiModelProperty(value = "발주연동수량")
    private long poIfQty;

    @ApiModelProperty(value = "발주SEQ")
    private String ilPoId;

    @ApiModelProperty(value = "발주구분")
    private String purGubCd;

    @ApiModelProperty(value = "사업부")
    private String divCd;

    @ApiModelProperty(value = "발주시간마감그룹")
    private String ordCloGrpCd;

    @ApiModelProperty(value = "일배구분")
    private String dlvGubCd;

    @ApiModelProperty(value = "IF flag")
    private String itfFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_Order Interface Table로 반영 여부")
    private String itfFlgErp;

    @ApiModelProperty(value = "품목 출고처 PK")
    private long ilItemWarehouseId;

	@ApiModelProperty(value = "입고예정일")
    private String stockScheduledDt;

	@ApiModelProperty(value = "이동수량")
    private long trnCnt;

	@ApiModelProperty(value = "조직코드")
    private String froOrgId;

	@ApiModelProperty(value = "조직코드")
    private String toOrgId;

	@ApiModelProperty(value = "이전될 조직코드")
    private String toSubInvCd;

    @ApiModelProperty(value = "(ERP 필요정보) 판매가격 0")
    private String selPrc;

    @ApiModelProperty(value = "(ERP 필요정보) 라인유형 ID 0")
    private String linTypId;

    @ApiModelProperty(value = "(ERP 필요정보) 요청일")
    private String dlvReqDat;

    @ApiModelProperty(value = "발주일자")
    private String baseDt;

    @ApiModelProperty(value = "PO발주일자+1")
    private String poPrmDat;

    @ApiModelProperty(value = "PO발주일자+1")
    private String movePrmDat;

    @ApiModelProperty(value = "온도값")
    private String tmpVal;

    @ApiModelProperty(value = "발주전체조회여부")
    private String getPoAllYn;

    @Override
    public ErpIfPurchaseOrdLineRequestDto clone(){
        ErpIfPurchaseOrdLineRequestDto clone = null;
        try {
            clone = (ErpIfPurchaseOrdLineRequestDto) super.clone();
        } catch(CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
        return clone;
    }
}
