package kr.co.pulmuone.v1.comm.mapper.user.buyer;

import java.util.List;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.user.dormancy.dto.AddUserBlackRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackHistoryListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.GetUserBlackListRequestDto;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserBlackHistoryListResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserBlackListResultVo;

@Mapper
public interface UserBlackMapper {

	int getBlackListUserListCount(GetUserBlackListRequestDto dto) throws Exception;

	Page<GetUserBlackListResultVo> getBlackListUserList(GetUserBlackListRequestDto dto) throws Exception;

	Page<GetUserBlackHistoryListResultVo> getUserBlackHistoryList(GetUserBlackHistoryListRequestDto dto) throws Exception;

	String selectLoginIdCheck(AddUserBlackRequestDto dto) throws Exception;

	void addUserBlack(AddUserBlackRequestDto dto) throws Exception;

	void putUserEventJoinYn(AddUserBlackRequestDto dto) throws Exception;

	List<GetUserBlackListResultVo> getBlackListUserListExportExcel(GetUserBlackListRequestDto dto) throws Exception;
}
