package kr.co.pulmuone.bos.user.member.service;

import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListResponseDto;

public interface UserDropBosService {
    GetUserDropListResponseDto getUserDropList(GetUserDropListRequestDto dto) throws Exception;
}
