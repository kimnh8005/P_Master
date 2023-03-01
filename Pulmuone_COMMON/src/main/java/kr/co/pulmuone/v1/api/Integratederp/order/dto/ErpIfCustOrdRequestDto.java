package kr.co.pulmuone.v1.api.Integratederp.order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ErpIfCustOrdRequestDto")
@Builder
public class ErpIfCustOrdRequestDto  {

    /*
     * ERP API 주문|취소 입력 dto
     */

    @ApiModelProperty(value = "전송 대상 시스템 구분 코드값")
    private String crpCd;

    @ApiModelProperty(value = "Header와 Line의 join key")
    private String hrdSeq;

    @ApiModelProperty(value = "ERP 전용 key")
    private String oriSysSeq;

    @ApiModelProperty(value = "ERP 전용 Order Line Key 값 (주문상품 순번)")
    private String ordNoDtl;

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;

    @ApiModelProperty(value = "품목 Code")
    private String erpItmNo;

    @ApiModelProperty(value = "KAN 코드")
    private String kanCd;

    @ApiModelProperty(value = "상품코드(이샵 제품제드) 예>채널상품코드(베이비밀 상품코드)")
    private String malItmNo;

    @ApiModelProperty(value = "주문수량")
    private Integer ordCnt;

    @ApiModelProperty(value = "ERP는 직송(U), 배송(S) LDS인 경우 배송구분 [ 0 : 가맹점배송, 1 : 택배 ]")
    private String dlvGrp;

    @ApiModelProperty(value = "물류 배송 출발 요청일 (Header 에 필요한 데이터임)(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvStrDat;

    @ApiModelProperty(value = "고객 배송 요청일.입고처 입고 요청일(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvReqDat;

    @ApiModelProperty(value = "출고처리일(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvDat;

    @ApiModelProperty(value = "표준판매 단가")
    private double stdPrc;

    @ApiModelProperty(value = "판매단가")
    private double selPrc;

    @ApiModelProperty(value = "과세적용 후 판매금액")
    private double ordAmt;

    @ApiModelProperty(value = "쇼핑몰에서 회원에게 제공하는 품목명")
    private String shpItmNam;

    @ApiModelProperty(value = "(ERP 필요 정보)_회사 id(고정값 165) (Header 에 필요해서 일단 표기함)")
    private String oprUni;

    @ApiModelProperty(value = "(ERP 필요 정보)_샵구분")
    private String shpCd;

    @ApiModelProperty(value = "배송그룹유형")
    private String dlvGrpTyp;

    @ApiModelProperty(value = "총주문수량 (통합몰 제공가능)")
    private Integer totOrdCnt;

    @ApiModelProperty(value = "쇼핑몰 총금액(부가세포함가)")
    private Integer totOrdAmt;

    @ApiModelProperty(value = "쇼핑몰 세액 (부가세 * dtl_cnt수량)")
    private Integer totOrdTax;

    @ApiModelProperty(value = "(ERP 필요 정보)_매장에서 추가할인 (직원할인 등) 경우는 FLAG를 'Y'로 입력(attribute7)")
    private String addDisFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_Order Source 별 용도다름(쇼핑몰:고객 축하메세지.)")
    private String pacDes;

    @ApiModelProperty(value = "(ERP 필요 정보)_조직 Code. 출고처 창고코드값(802:용인물류, 803:CJ통합물류)")
    private String wahCd;

    @ApiModelProperty(value = "데이터 생성일자(YYYYMMDDHHMISS)")
    private String creDat;

    @ApiModelProperty(value = "배송 요청 여부(Y/N) : 배송을 포함해서 매출처리 또는 배송 없이 매출처리만")
    private String dlvReqYn;

    @ApiModelProperty(value = "정기 배송 상품의 배송시작일 (YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvStDat;

    @ApiModelProperty(value = "정기 배송 상품의 배송종료일(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dlvEndDat;

    @ApiModelProperty(value = "배송지사 전화번호")
    private String dlvTel;

    @ApiModelProperty(value = "고객 나이")
    private Integer age;

    @ApiModelProperty(value = "배송형태 ONCEDLVCD ")
    private String onDlvCd;

    @ApiModelProperty(value = "배달방법 DLVMT ")
    private String dlvMt;

    @ApiModelProperty(value = "1회배달 수량ONCNT")
    private Integer onCnt;

    @ApiModelProperty(value = "주차WEEKSID")
    private Integer wekId;

    @ApiModelProperty(value = "알러지 대체식단 여부")
    private String argYn;

    @ApiModelProperty(value = "취소 또는 일부 반품 시 원주문라인 번호")
    private String ordNoDtlCnl;

    @ApiModelProperty(value = "주문구분 [ 1 : 주문, 2 : 취소 ] ")
    private String ordCnlFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_단가계산플래그.ERP에 매출을 반영할 단가 기준(예 : Y / N)")
    private String cclPrcFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_CONTEXT.고정값 (NO)")
    private String ctx;

    @ApiModelProperty(value = "(ERP 필요 정보)_DRP에서 정보를 읽었는지 여부(Y/N)")
    private String drpFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_라인 유형 카테고리 코드(예 : Return / order)")
    private String linCatCd;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문 Line 유형")
    private String linTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_Order Merge Flag.주문을 합침할지에 대한 flag")
    private String ordMrgFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문/발주 연계 유형.주문/발주 유형으로 각 유형에 따라 재고만이동 되거나 PO 만 발생하거나 주문이 생기거나 함")
    private String ordPoTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문/발주 연계 유형 Description")
    private String ordPoTypDes;

    @ApiModelProperty(value = "(ERP 필요 정보)_단위.입수량 단위로 법인별 마스터를 따르지 않는다면 필수로 받아야함")
    private String ordCntUom;

    @ApiModelProperty(value = "(ERP 필요 정보)_OU.법인 구분을 위한 ID 값으로 필요")
    private Integer orgId;

    @ApiModelProperty(value = "(ERP 필요 정보)_단가적용일자(YYYYMMDDHHMISS)")
    private String prcDat;

    @ApiModelProperty(value = "(ERP 필요 정보)_고객요청일자 입력(반품시에는 반품에 대한 회수 받기를 원하는 요청일)(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String prmDat;

    @ApiModelProperty(value = "(ERP 필요 정보)_Validation 시 : 프로모션 라인일 경우 'Y', Legacy에서 Insert 시 : 'N'으로 입력")
    private String pmtLinFlg;

    @ApiModelProperty(value = "(ERP 필요 정보)_푸드머스 증정용 : Parent ORIG_SYS_LINE_REF")
    private String pmtOriSysLinRef;

    @ApiModelProperty(value = "(ERP 필요 정보)_Receipt Org ID(물류).물류 입고구분을 위한 데이터로 Dblink 로 되어 넘겨줄것으로 보이나 실제 푸드머스 직매입까지 연결되여 관리되야 하는 항목")
    private Integer recOrgId;

    @ApiModelProperty(value = "(ERP 필요 정보)_Return Order Header Key 값(반품시 입력).반품시 원주문의 orig sys document ref  값으로 보이며 어떤 판매건에 대한 반품인지 관리하기 위한 데이터")
    private String rtnOriSysDocRef;

    @ApiModelProperty(value = "(ERP 필요 정보)_Return Order Line Key 값(반품시 입력).반품시 원주문의 orig sys document ref  값으로 보이며 어떤 판매건에 대한 반품인지 관리하기 위한 데이터")
    private String rtnOriSysLinRef;

    @ApiModelProperty(value = "(ERP 필요 정보)_반품주문 사유(반품시 입력) Code. ERP 에서 생성될 반품사유코드로 필수값으로 받아야 반품주문 생성할수있음")
    private String rtnRsnDes;

    @ApiModelProperty(value = "(ERP 필요 정보)반품 처리에 따른 구분값(10: 물류회수처리 20: 물류회수택배처리 30: 미회수환품 40: 자체폐기)")
    private String rtnTrcTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_정산유형")
    private String salCnlTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_Shipping Instruction.고객 요청사항 입력란(문앞에 놔줘/ 경비실 나줘/ 님이 드세요 등등)")
    private String shiIns;

    @ApiModelProperty(value = "(CJ물류 필요 정보) 출고수량 출고확정후 CJ에서 업데이트")
    private Integer shiCnt;

    @ApiModelProperty(value = "(CJ물류 필요 정보) 재고(주문)유형 'NN':정상, 'AC':임박,행사")
    private String stcTyp;

    @ApiModelProperty(value = "(ERP 필요 정보)_Validation Status(Y/N) VALIDATION 에 대한 검증 flag 로 보임(필수값 같음)")
    private String vdtSta;

    @ApiModelProperty(value = "주문 Line 유형 ID")
    private Integer linTypId;

    @ApiModelProperty(value = "주문유형(주문/증정)(택배정산용)통합몰에서 재정의 후 legacy 에 전달 주문의 매출 TRX 가 라인별로 구분되기에 ERP 에서 어떤 주문타입으로 매출인지 본 컬럼에 수신되야함")
    private String ordTypLin;

    @ApiModelProperty(value = "송신일자 (YYYYMMDDHHMISS)")
    private String sndDat;

    @ApiModelProperty(value = "지사채널(ex.홈,오피스)")
    private String prtnChnl;

    @ApiModelProperty(value = "음용패턴")
    private String drnkPtrn;

    @ApiModelProperty(value = "주문일자(YYYYMMDDHHMISS) (예시 : 20200912000000)")
    private String ordDat;

    @ApiModelProperty(value = "매출액표시기준 1:판매 -1:반품")
    private double salFg;

    @ApiModelProperty(value = "과세여부")
    private String taxYn;

    @ApiModelProperty(value = "매출액(부가세가 포함된 판매단가 * 수량)")
    private double stdAmt;

    @ApiModelProperty(value = "판매여부 Y:판매 N:반품")
    private String salYn;

    @ApiModelProperty(value = "할인액(상품별 총할인액)")
    private double dcAmt;

    @ApiModelProperty(value = "부가세(과세상품인 경우에 한하여 실매출액에 부가세)")
    private double vatAmt;

    @ApiModelProperty(value = "할인액-쿠폰")
    private double dcCpnAmt;

    @ApiModelProperty(value = "POS 등록일시(주문일시)(YYYYMMDDHHMISS)")
    private String posInsDt;

    @ApiModelProperty(value = "배송 처리 코드")
    private String dlvStaCd;

    @ApiModelProperty(value = "배송완료여부")
    private String dlvCnf;

    @ApiModelProperty(value = "송장번호(여러 개인 경우 \",\" 로 구분하여 기록함)")
    private String dlvNo;

    @ApiModelProperty(value = "배송 상태 수정일자(YYYYMMDDHHMISS)")
    private String dlvUpdDat;

    @ApiModelProperty(value = "미출 여부")
    private String misYn;

    @ApiModelProperty(value = "미출 수량")
    private Integer misCnt;

    @ApiModelProperty(value = "미출사유(기존 방식과 동일)")
    private String misRsn;

    @ApiModelProperty(value = "미출정보 등록일시(YYYYMMDDHHMISS)")
    private String misUpdDat;

    @ApiModelProperty(value = "정산처리 여부")
    private String setYn;

    @ApiModelProperty(value = "정산처리 금액")
    private double setAmt;

    @ApiModelProperty(value = "정산정보 등록 일시(YYYYMMDDHHMISI)")
    private String setUpdDat;

    @ApiModelProperty(value = "(ERP 필요 정보)_Line 번호")
    private String linNum;

    @ApiModelProperty(value = "(ERP 필요 정보)SHI_CNT")
    private Integer shiToOrgId;

    @ApiModelProperty(value = "고객 배송 요청일(YYYYMMDDHHMISS)(예시 : 20200912000000)")
    private String dvlReqDat;

    @ApiModelProperty(value = "가맹점코드")
    private String stoCd;

    @ApiModelProperty(value = "(ERP 필요 정보)_주문출처(ERS/BABS/ECOS/ECMS/ERF)")
    private String ordSrc;

    @ApiModelProperty(value = "주문 인터페이스 결과 메시지")
    private String itfOrdMsg;

    @ApiModelProperty(value = "주문 인터페이스 수신일자(YYYYMMDDHHMISS)")
    private String itfOrdDat;




}
