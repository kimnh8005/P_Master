package kr.co.pulmuone.bos.user.privacy.service;

import kr.co.pulmuone.v1.user.dormancy.dto.AddUserBlackRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.AddUserBlackResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackHistoryListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackHistoryListResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackListResponseDto;

public interface UserBlackService {

	GetUserBlackListResponseDto getBlackListUserList(GetUserBlackListRequestDto dto) throws Exception;

	GetUserBlackHistoryListResponseDto getUserBlackHistoryList(GetUserBlackHistoryListRequestDto dto) throws Exception;

}
