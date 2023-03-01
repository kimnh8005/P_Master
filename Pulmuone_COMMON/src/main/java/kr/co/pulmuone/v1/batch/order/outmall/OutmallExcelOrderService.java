package kr.co.pulmuone.v1.batch.order.outmall;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mappers.batch.master.order.outmall.OutmallExcelOrderMapper;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutmallExcelOrderService {

    private final OutmallExcelOrderMapper outmallExcelOrderMapper;

    protected List<OutMallOrderDto> getOutmallExcelOrderTargetList(List<String> batchStatusList) throws Exception {
    	return outmallExcelOrderMapper.getOutmallExcelOrderTargetList(batchStatusList);
    }

    protected int putOutmallExcelInfo(OutMallExcelInfoVo outMallExcelInfoVo) throws Exception {
    	return outmallExcelOrderMapper.putOutmallExcelInfo(outMallExcelInfoVo);
    }
}
