package kr.co.pulmuone.v1.api.lotteglogis.service;

import java.util.List;

import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderRequestDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisClientOrderResponseDto;
import kr.co.pulmuone.v1.api.lotteglogis.dto.LotteGlogisTrackingResponseDto;

public interface LotteGlogisBiz {
    LotteGlogisTrackingResponseDto getLotteGlogisTrackingList(String waybillNumber, String returnsYn);
    LotteGlogisClientOrderResponseDto convertLotteGlogisClientOrder(LotteGlogisClientOrderRequestDto lotteGlogisClientOrderRequestDto);
    LotteGlogisClientOrderResponseDto convertLotteGlogisClientOrderList(List<LotteGlogisClientOrderRequestDto> lotteGlogisClientOrderList);
}
