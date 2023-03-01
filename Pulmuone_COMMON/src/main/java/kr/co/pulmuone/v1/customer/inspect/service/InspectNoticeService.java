package kr.co.pulmuone.v1.customer.inspect.service;

import kr.co.pulmuone.v1.comm.mapper.customer.inspect.InspectNoticeMapper;
import kr.co.pulmuone.v1.customer.inspect.dto.vo.InspectNoticeIpVo;
import kr.co.pulmuone.v1.customer.inspect.dto.vo.InspectNoticeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InspectNoticeService {

    private final InspectNoticeMapper inspectNoticeMapper;

    protected InspectNoticeVo getInspectNotice() throws Exception {
        return inspectNoticeMapper.getInspectNotice();
    }

    protected List<InspectNoticeIpVo> getInspectNoticeIpList() throws Exception {
        return inspectNoticeMapper.getAllInspectNoticeIpList();
    }

    protected int addInspectNotice(InspectNoticeVo inspectNoticeVo) throws Exception {
        return inspectNoticeMapper.addInspectNotice(inspectNoticeVo);
    }

    protected int addAllInspectNoticeIp(List<InspectNoticeIpVo> inspectNoticeIpVoList) throws Exception {
        return inspectNoticeMapper.addAllInspectNoticeIp(inspectNoticeIpVoList);
    }

    protected int deleteInspectNotice(int inspectNoticeId) throws Exception {
        return inspectNoticeMapper.deleteInspectNotice(inspectNoticeId);
    }

    protected int deleteAllInspectNoticeIp(int inspectNoticeId) throws Exception {
        return inspectNoticeMapper.deleteAllInspectNoticeIp(inspectNoticeId);
    }

    protected Boolean getIsBetweenServerTime(LocalDateTime serverLocalTime) {
        return inspectNoticeMapper.getIsBetweenServerTime(serverLocalTime);
    }

}
