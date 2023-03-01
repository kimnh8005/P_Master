package kr.co.pulmuone.v1.api.lotteglogis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderRequestDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderResponseDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisTrackingResponseDto;


/**
* <PRE>
* Forbiz Korea
* 롯데택배 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 18.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class LotteGlogisBizImpl implements LotteGlogisBiz {

    @Autowired
    LotteGlogisService lotteGlogisService;

    /**
     * @Desc 롯데 송장번호 트래킹 API
     * @param waybillNumber
     * @return LotteGlogisTrackingResponseDto
     */
    @Override
    public LotteGlogisTrackingResponseDto getLotteGlogisTrackingList(String waybillNumber, String returnsYn){
        return lotteGlogisService.getLotteGlogisTrackingList(waybillNumber, returnsYn);
    }

    /**
     * @Desc 롯데 거래처 주문 API 단건
     * @param lotteGlogisClientOrderRequestDto
     * @return LotteGlogisClientOrderResponseDto
     */
    @Override
    public LotteGlogisClientOrderResponseDto convertLotteGlogisClientOrder(LotteGlogisClientOrderRequestDto lotteGlogisClientOrderRequestDto) {
        List<LotteGlogisClientOrderRequestDto> lotteGlogisClientOrderList = new ArrayList<LotteGlogisClientOrderRequestDto>();
        lotteGlogisClientOrderList.add(lotteGlogisClientOrderRequestDto);

        return lotteGlogisService.convertLotteGlogisClientOrderList(lotteGlogisClientOrderList);
    }

    /**
     * @Desc 롯데 거래처 주문 API 복수건
     * @param lotteGlogisClientOrderList
     * @return LotteGlogisClientOrderResponseDto
     */
    @Override
    public LotteGlogisClientOrderResponseDto convertLotteGlogisClientOrderList(List<LotteGlogisClientOrderRequestDto> lotteGlogisClientOrderList){
        return lotteGlogisService.convertLotteGlogisClientOrderList(lotteGlogisClientOrderList);
    }
}
