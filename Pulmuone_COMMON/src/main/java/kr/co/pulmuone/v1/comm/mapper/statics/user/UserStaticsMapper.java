package kr.co.pulmuone.v1.comm.mapper.statics.user;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.statics.user.dto.UserCountStaticsRequestDto;
import kr.co.pulmuone.v1.statics.user.dto.UserGroupStaticsRequestDto;
import kr.co.pulmuone.v1.statics.user.dto.UserTypeStaticsRequestDto;
import kr.co.pulmuone.v1.statics.user.dto.vo.UserCountStaticsVo;
import kr.co.pulmuone.v1.statics.user.dto.vo.UserGroupStaticsVo;
import kr.co.pulmuone.v1.statics.user.dto.vo.UserTypeStaticsVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserStaticsMapper {

    Page<UserTypeStaticsVo> getUserTypeStaticsList(UserTypeStaticsRequestDto dto);

    Page<UserGroupStaticsVo> getUserGroupStaticsList(UserGroupStaticsRequestDto dto);

    Page<UserCountStaticsVo> getUserCountStaticsList(UserCountStaticsRequestDto dto);

}