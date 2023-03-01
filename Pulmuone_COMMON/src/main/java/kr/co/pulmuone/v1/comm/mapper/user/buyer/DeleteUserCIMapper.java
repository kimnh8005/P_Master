package kr.co.pulmuone.v1.comm.mapper.user.buyer;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeleteUserCIMapper {

    int resetUserCi(String urUserId) throws Exception;
}
