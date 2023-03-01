package kr.co.pulmuone.v1.od.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.od.order.dto.GetOutmallOrderDetailListRequestDto;
import kr.co.pulmuone.v1.od.order.dto.GetOutmallOrderDetailListResponseDto;



@Service
public class OutmallOrderDetailListBizImpl implements OutmallOrderDetailListBiz {

    @Autowired
    private OutmallOrderDetailListService outmallOrderDetailListService;


    @Override
    public GetOutmallOrderDetailListResponseDto getOutmallOrderDetailList(GetOutmallOrderDetailListRequestDto dto) throws Exception {
        return outmallOrderDetailListService.getOutmallOrderDetailList(dto);
    }
}
