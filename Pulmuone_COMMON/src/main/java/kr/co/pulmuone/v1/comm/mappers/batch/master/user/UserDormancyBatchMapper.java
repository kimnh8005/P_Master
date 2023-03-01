package kr.co.pulmuone.v1.comm.mappers.batch.master.user;

import kr.co.pulmuone.v1.batch.user.dormancy.dto.ActiveUserDormantRequestDto;
import kr.co.pulmuone.v1.batch.user.dormancy.dto.vo.UserDormancyBatchResultVo;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDormancyBatchMapper {

    void addUrUserMove(ActiveUserDormantRequestDto dto);

    Long selUrUserMoveId(Long urUserId);

    void addUrUserMoveLog(ActiveUserDormantRequestDto dto);

    void addUrBuyerMove(ActiveUserDormantRequestDto dto);

    void delUserBuyer(ActiveUserDormantRequestDto dto);

    void putUserMove(ActiveUserDormantRequestDto dto);

    List<Long> getTargetDormant(int day);

	UserDormancyBatchResultVo getUserDormancyBatchInfo(Long urUserId);

	UserDormancyBatchResultVo getUserDormancyExpected(Long urUserId);


}
