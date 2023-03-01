package kr.co.pulmuone.v1.api.cjlogistics.service;

import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsTrackingResponseDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class CJLogisticsServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private CJLogisticsService cJLogisticsService;

    @Test
    void CJ송장번호_트래킹_API_성공() {
        String waybillNumber = "389797696812";

        log.info("CJ송장번호_트래킹_API_성공 Start!!!");

        CJLogisticsTrackingResponseDto cJLogisticsTrackingDto = cJLogisticsService.getCJLogisticsTrackingList(waybillNumber);

        log.info("Tracking : [{}]", cJLogisticsTrackingDto.getMaster().getTracking());

        assertTrue( CollectionUtils.isNotEmpty(cJLogisticsTrackingDto.getMaster().getTracking()) );

        log.info("CJ송장번호_트래킹_API_성공 End!!!");
    }

    @Test
    void CJ송장번호_트래킹_API_실패() {
        String waybillNumber = "385132516853";

        log.info("CJ송장번호_트래킹_API_실패 Start!!!");

        CJLogisticsTrackingResponseDto cJLogisticsTrackingDto = cJLogisticsService.getCJLogisticsTrackingList(waybillNumber);

        log.info("Tracking 결과코드, 결과메세지 ::: <{}> <{}>", cJLogisticsTrackingDto.getResponseCode(), cJLogisticsTrackingDto.getResponseMessage());
        boolean result = false;
        if (!"-1".equals(cJLogisticsTrackingDto.getResponseCode())) { result = true; }

        assertFalse(result);

        log.info("CJ송장번호_트래킹_API_실패 End!!!");
    }

    // 테스트케이스 제외처리
    // 테스트이후 삭제 안됨
    // 외부 DB로 권한 제한적으로 보임 -> 검토필요
//    @Test
//    void CJ택배접수_처리_API_성공() throws Exception {
//
//        log.info("CJ택배접수_처리_API_성공 Start!!!");
//        CJLogisticsOrderAcceptDto dto = new CJLogisticsOrderAcceptDto();
//        dto.setCustId(ErpEnums.ParcelServiceCustId.CJ_CUST_CD.getCode());			    /**고객ID*/
//        dto.setRcptYmd("20201204");             /**접수일자YYYMMDD*/
//        dto.setCustUseNo("고객사용번호2");           /**고객사용번호	 기업고객이관리하는주문번호/영수번호등내부관리번호*/
//        dto.setRcptDv("02");              /**접수구분	01:일반,02:반품*/
//        dto.setWorkDvCd("01");            /**작업구분코드	01:일반,02:교환,03:A/S*/
//        dto.setReqDvCd("01");             /**요청구분코드	01:요청,02:취소*/
//        dto.setMpckKey("20201204_skyangel_고객사용번호");             /**합포장키	다수데이터를한송장에출력할경우처리(합포없는경우YYYYMMDD_고객ID_고객사용번호orYYYYMMDD_고객ID_운송장번호);*/
//        dto.setMpckSeq(1);              /**합포장순번	합포장처리건수가다수일경우SEQ처리를수행한다.(합포없는경우무조건1);*/
//        dto.setCalDvCd("01");             /**정산구분코드	01:계약운임,02:자료운임(계약운임인지업체에서넣어주는운임으로할지);*/
//        dto.setFrtDvCd("03");             /**운임구분코드	01:선불,02:착불,03:신용*/
//        dto.setCntrItemCd("01");          /**계약품목코드01:일반품목*/
//        dto.setBoxTypeCd("04");           /**박스타입코드	01:극소,02:소,03:중,04:대,05:특대*/
//        dto.setBoxQty(1);               /**택배박스수량	*/
//        dto.setFrt(0);                 	/**운임적용구분이 자료 운임일 경우 등록처리*/
//        dto.setCustMgmtDlcmCd("고객관리거래처코드");      /**고객관리거래처코드주관사관리협력업체코드혹은택배사관리업체코드*/
//        dto.setSendrNm("송화인명");             /**송화인명			*/
//        dto.setSendrTelNo1("010");         /**송화인전화번호1*/
//        dto.setSendrTelNo2("2252");         /**송화인전화번호2*/
//        dto.setSendrTelNo3("2345");         /**송화인전화번호3*/
//        dto.setSendrCellNo1("");        /**송화인휴대폰번호1*/
//        dto.setSendrCellNo2("");        /**송화인휴대폰번호2*/
//        dto.setSendrCellNo3("");        /**송화인휴대폰번호3*/
//        dto.setSendrSafeNo1("");        /**송화인안심번호1*/
//        dto.setSendrSafeNo2("");        /**송화인안심번호2*/
//        dto.setSendrSafeNo3("");        /**송화인안심번호3*/
//        dto.setSendrZipNo("18102");          /**송화인우편번호*/
//        dto.setSendrAddr("서울시 노원구 노원로");           /**송화인주소*/
//        dto.setSendrDetailAddr("공릉역 옆집");     /**송화인상세주소*/
//        dto.setRcvrNm("수화인명");              /**수화인명*/
//        dto.setRcvrTelNo1("010");          /**수화인전화번호1*/
//        dto.setRcvrTelNo2("1100");          /**수화인전화번호2*/
//        dto.setRcvrTelNo3("2000");          /**수화인전화번호3*/
//        dto.setRcvrCellNo1("");         /**수화인휴대폰번호1*/
//        dto.setRcvrCellNo2("");         /**수화인휴대폰번호2*/
//        dto.setRcvrCellNo3("");         /**수화인휴대폰번호3*/
//        dto.setRcvrSafeNo1("");         /**수화인안심번호1*/
//        dto.setRcvrSafeNo2("");         /**수화인안심번호2*/
//        dto.setRcvrSafeNo3("");         /**수화인안심번호3*/
//        dto.setRcvrZipNo("01022");           /**수화인우편번호*/
//        dto.setRcvrAddr("서울시 노원구 화랑로");            /**수화인주소*/
//        dto.setRcvrDetailAddr("화랑대역 앞집");      /**수화인상세주소*/
//        dto.setOrdrrNm("");             /**주문자명*/
//        dto.setOrdrrTelNo1("");         /**주문자전화번호1*/
//        dto.setOrdrrTelNo2("");         /**주문자전화번호2*/
//        dto.setOrdrrTelNo3("");         /**주문자전화번호3*/
//        dto.setOrdrrCellNo1("");        /**주문자휴대폰번호1*/
//        dto.setOrdrrCellNo2("");        /**주문자휴대폰번호2*/
//        dto.setOrdrrCellNo3("");        /**주문자휴대폰번호3*/
//        dto.setOrdrrSafeNo1("");        /**주문자안심번호1*/
//        dto.setOrdrrSafeNo2("");        /**주문자안심번호2*/
//        dto.setOrdrrSafeNo3("");        /**주문자안심번호3*/
//        dto.setOrdrrZipNo("");          /**주문자우편번호*/
//        dto.setOrdrrAddr("");           /**주문자주소*/
//        dto.setOrdrrDetailAddr("");     /**주문자상세주소*/
//        dto.setInvcNo("");              /**운송장번호(12자리);*/
//        dto.setOriInvcNo("");           /**원운송장번호*/
//        dto.setOriOrdNo("");            /**원주문번호*/
//        dto.setColctExpctYmd("");       /**집화예정일자*/
//        dto.setColctExpctHour("");      /**집화예정시간*/
//        dto.setShipExpctYmd("");        /**배송예정일자*/
//        dto.setShipExpctHour("");       /**배송예정시간*/
//        dto.setPrtSt("02");               /**출력상태	01:미출력,02:선출력,03:선발번(반품은선발번이없음);*/
//        dto.setArticleAmt(0);          	/**물품가액			*/
//        dto.setRemark1("");             /**배송메세지1(비고);*/
//        dto.setRemark2("");             /**배송메세지2(송화인비고);*/
//        dto.setRemark3("");             /**배송메세지3(수화인비고);*/
//        dto.setCodYn("");               /**COD여부대면결제서비스업체의경우대면결제발생시Y로셋팅*/
//        dto.setGdsCd("");               /**상품코드	*/
//        dto.setGdsNm("테스트 단품");               /**상품명*/
//        dto.setGdsQty(0);              	/**상품수량*/
//        dto.setUnitCd("");              /**단품코드*/
//        dto.setUnitNm("");              /**단품명*/
//        dto.setGdsAmt(0);              	/**상품가액*/
//        dto.setEtc1("");                /**기타1*/
//        dto.setEtc2("");                /**기타2*/
//        dto.setEtc3("");                /**기타3*/
//        dto.setEtc4("");                /**기타4*/
//        dto.setEtc5("");                /**기타5*/
//        dto.setDlvDv("01");               /**택배구분	택배:'01',중량물(설치물류);:'02',중량물(비설치물류);:'03'*/
//        dto.setRcptErrYn("N");           /**접수에러여부	DEFAULT:'N'	*/
//        dto.setRcptErrMsg("");          /**접수에러메세지					*/
//        dto.setEaiPrgsSt("01");           /**EAI전송상태		DEFAULT:'01'*/
//        dto.setEaiErrMsg("");           /**에러메세지*/
//        dto.setRegEmpId("Skyangel");            /**등록사원ID*/
//        dto.setModiEmpId("Skyangel");           /**수정사원ID*/
//        int resultCnt = cJLogisticsService.addCJLogisticsOrderAccept(dto);
//
//        log.info("resultCnt : [{}]", resultCnt);
//        boolean result = false;
//        if (resultCnt > 0) result = true;
//        assertTrue( result );
//
//        log.info("CJ택배접수_처리_API_성공 End!!!");
//    }

}
