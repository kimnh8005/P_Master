package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpIfOrgaShopGoodsSearchResponseDto {

	  @JsonAlias({ "ifSeq" })
	  private Integer ifSeq; // 중계서버 DB 테이블의 unique 키

	  @JsonAlias({ "srcSvr" })
	  private String sourceServer; // 입력 시스템(예:ERP, ORGA …)

	  @JsonAlias({ "shpCd" })
	  private String shopCode;			// 매장코드

	  @JsonAlias({ "curCnt" })
	  private String currnetCnt;		 // 현재고(5분단위 업데이트/ 주기는 테스트 후 확정)

	  @JsonAlias({ "norPrc" })
	  private String normalPrc;		 // 정상가

	  @JsonAlias({ "salPrc" })
	  private String salePrc;		 // 판매가(행사가 포함)

	  @JsonAlias({ "updFlg" })
	  private Boolean updateFlag; // 정보 업데이트 여부(Y / N)

	  @JsonAlias({ "updDat" })
	  private String updateDate; // 정보 업데이트 일시

	  @JsonAlias({ "itmNo" })
	  private String erpItemNo; // ERP 품목번호

	  @JsonAlias({ "itfFlg" })
	  private Boolean interfaceReceivingFlag; // I/F 수신 여부 (Y / N)

	  @JsonAlias({ "itfDat" })
	  private String interfaceReceivingDate; // I/F 수신 일시 (14자리 년월일시분초)
}
