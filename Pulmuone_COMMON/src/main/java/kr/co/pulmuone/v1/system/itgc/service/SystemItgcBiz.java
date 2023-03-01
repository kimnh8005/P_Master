package kr.co.pulmuone.v1.system.itgc.service;

import kr.co.pulmuone.v1.system.itgc.dto.ItgcRequestDto;

import java.util.List;

public interface SystemItgcBiz {

    int addItgc(ItgcRequestDto dto);

    int addItgcList(List<ItgcRequestDto> insertList);

}
