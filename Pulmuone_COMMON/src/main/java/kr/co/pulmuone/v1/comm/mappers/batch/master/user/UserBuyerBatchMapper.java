package kr.co.pulmuone.v1.comm.mappers.batch.master.user;

import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserBuyerBatchMapper {

    List<UserBuyerVo> getBuyerList(@Param("batchYearMonth") String batchYearMonth, @Param("batchUserType") String batchUserType);

    void putBuyerGroup(@Param("userBuyerList") List<UserBuyerVo> userBuyerList);

    List<UserBuyerVo> getBuyerMoveList(@Param("batchYearMonth") String batchYearMonth, @Param("batchUserType") String batchUserType);

    void putBuyerMoveGroup(@Param("userBuyerList") List<UserBuyerVo> userBuyerList);

    void addChangeLog(@Param("userBuyerList") List<UserBuyerVo> userBuyerList);
}
