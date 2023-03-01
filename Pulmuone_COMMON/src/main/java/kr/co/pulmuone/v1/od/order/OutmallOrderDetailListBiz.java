package kr.co.pulmuone.v1.od.order;

import kr.co.pulmuone.v1.od.order.dto.GetOutmallOrderDetailListRequestDto;
import kr.co.pulmuone.v1.od.order.dto.GetOutmallOrderDetailListResponseDto;

public interface OutmallOrderDetailListBiz {
    GetOutmallOrderDetailListResponseDto getOutmallOrderDetailList(GetOutmallOrderDetailListRequestDto dto) throws Exception;
}
