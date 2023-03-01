package kr.co.pulmuone.v1.system.itgc.service;

import kr.co.pulmuone.v1.system.itgc.dto.ItgcRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemItgcBizImpl implements SystemItgcBiz {

    private final SystemItgcService systemItgcService;

    @Override
    public int addItgc(ItgcRequestDto dto) {
        return systemItgcService.addItgcList(Collections.singletonList(dto));
    }

    @Override
    public int addItgcList(List<ItgcRequestDto> insertList) {
        return systemItgcService.addItgcList(insertList);
    }

}