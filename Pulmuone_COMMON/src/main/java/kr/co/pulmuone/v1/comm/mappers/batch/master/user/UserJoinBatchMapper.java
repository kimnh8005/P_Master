package kr.co.pulmuone.v1.comm.mappers.batch.master.user;

import kr.co.pulmuone.v1.batch.user.join.dto.vo.JoinBatchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserJoinBatchMapper {

    List<JoinBatchVo> getUserJoin(int userJoinDepositDay);

    void putUserJoinRecommGiveYn(@Param("userIdList") List<Long> userIdList);

}
