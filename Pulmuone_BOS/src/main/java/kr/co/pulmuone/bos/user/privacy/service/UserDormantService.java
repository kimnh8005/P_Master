package kr.co.pulmuone.bos.user.privacy.service;

import kr.co.pulmuone.v1.user.dormancy.dto.*;

public interface UserDormantService {

	GetUserDormantListResponseDto getUserDormantList(GetUserDormantListRequestDto dto) throws Exception;

	GetUserDormantHistoryListResponseDto getUserDormantHistoryList(GetUserDormantHistoryListRequestDto dto) throws Exception;

}
