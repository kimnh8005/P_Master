package kr.co.pulmuone.v1.api.lotteglogis.service;

import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderRequestDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderResponseDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisTrackingResponseDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.ErpEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class LotteGlogisServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private LotteGlogisService lotteGlogisService;

    @Test
    void 롯데_송장번호_트래킹_API_성공() {
        String waybillNumber = "236671200743";
        log.info("롯데_송장번호_트래킹_API_성공 start!!!");
        LotteGlogisTrackingResponseDto lotteGlogisTrackingDto = lotteGlogisService.getLotteGlogisTrackingList(waybillNumber, "N");
        log.info("lotteGlogisTrackingDto result !!! \n {}", lotteGlogisTrackingDto.getTracking() );
        assertTrue( CollectionUtils.isNotEmpty(lotteGlogisTrackingDto.getTracking()) );
        log.info("롯데_송장번호_트래킹_API_성공 End!!!");
    }

    @Test
    void 롯데_송장번호_트래킹_API_실패() {
        String waybillNumber = "306641893245";
        log.info("롯데_송장번호_트래킹_API_실패 start!!!");
        LotteGlogisTrackingResponseDto lotteGlogisTrackingDto = lotteGlogisService.getLotteGlogisTrackingList(waybillNumber, "N");
        log.info("lotteGlogisTrackingDto reuslt ~~~ \n {}", lotteGlogisTrackingDto);
        assertFalse( CollectionUtils.isNotEmpty(lotteGlogisTrackingDto.getTracking()) );
        log.info("롯데_송장번호_트래킹_API_실패 End!!!");
    }

    @Test
    void 롯데_거래처_주문_API_성공() {
    	log.info("롯데_거래처_주문_API_성공 start!!!");
        List<LotteGlogisClientOrderRequestDto> lotteGlogisClientOrderList = new ArrayList<LotteGlogisClientOrderRequestDto>();
        LotteGlogisClientOrderRequestDto lotteGlogisClientOrderRequestDto = new LotteGlogisClientOrderRequestDto();
        lotteGlogisClientOrderRequestDto.setJobCustCd(ErpEnums.ParcelServiceCustId.LOTTE_CUST_CD.getCode());
        lotteGlogisClientOrderRequestDto.setUstRtgSctCd("02");
        lotteGlogisClientOrderRequestDto.setOrdSct("1");
        lotteGlogisClientOrderRequestDto.setFareSctCd("03");
        lotteGlogisClientOrderRequestDto.setOrdNo("ord123456789");
        lotteGlogisClientOrderRequestDto.setInvNo("inv123456789");
        lotteGlogisClientOrderRequestDto.setSnperNm("(주)제이앤우");
        lotteGlogisClientOrderRequestDto.setSnperTel("02-6925-0180");
        lotteGlogisClientOrderRequestDto.setSnperZipcd("12801");
        lotteGlogisClientOrderRequestDto.setSnperAdr("경기도 광주시 곤지암읍 신대길 156 176-10 knb물류");
        lotteGlogisClientOrderRequestDto.setAcperNm("최*운");
        lotteGlogisClientOrderRequestDto.setAcperTel("050710257620");
        lotteGlogisClientOrderRequestDto.setAcperCpno("050710257620");
        lotteGlogisClientOrderRequestDto.setAcperZipcd("210112");
        lotteGlogisClientOrderRequestDto.setAcperAdr("강원 강릉시 포남2동 삼호아파트 3동405호");
        lotteGlogisClientOrderRequestDto.setBoxTypCd("C");
        lotteGlogisClientOrderRequestDto.setGdsNm("상품DATA");
        lotteGlogisClientOrderRequestDto.setDlvMsgCont("테스트");
        lotteGlogisClientOrderRequestDto.setBdpkSctCd("N");

        lotteGlogisClientOrderList.add(lotteGlogisClientOrderRequestDto);

        LotteGlogisClientOrderResponseDto lotteGlogisClientOrderResponseDto = lotteGlogisService.convertLotteGlogisClientOrderList(lotteGlogisClientOrderList);

        log.error("lotteGlogisClientOrderResponseDto returnList :::: \n <{}>", lotteGlogisClientOrderResponseDto.getReturnList().toString());

        assertEquals("S", lotteGlogisClientOrderResponseDto.getReturnList().get(0).getReturnCode());

        log.info("롯데_거래처_주문_API_성공 End!!!");
    }

    @Test
    void 롯데_거래처_주문_API_실패() {
    	log.info("롯데_거래처_주문_API_실패 start!!!");
        List<LotteGlogisClientOrderRequestDto> lotteGlogisClientOrderList = new ArrayList<LotteGlogisClientOrderRequestDto>();
        LotteGlogisClientOrderRequestDto lotteGlogisClientOrderRequestDto = new LotteGlogisClientOrderRequestDto();
        lotteGlogisClientOrderRequestDto.setJobCustCd("121974");

        lotteGlogisClientOrderList.add(lotteGlogisClientOrderRequestDto);

        LotteGlogisClientOrderResponseDto lotteGlogisClientOrderResponseDto = lotteGlogisService.convertLotteGlogisClientOrderList(lotteGlogisClientOrderList);

        log.error("lotteGlogisClientOrderResponseDto returnList ~~~: \n {}", lotteGlogisClientOrderResponseDto.getReturnList().toString());

        assertEquals("E", lotteGlogisClientOrderResponseDto.getReturnList().get(0).getReturnCode());

        log.info("롯데_거래처_주문_API_실패 End!!!");
    }
}
