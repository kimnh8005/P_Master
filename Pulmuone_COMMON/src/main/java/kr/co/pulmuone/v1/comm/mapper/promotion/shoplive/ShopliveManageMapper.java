package kr.co.pulmuone.v1.comm.mapper.promotion.shoplive;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveRequestDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopliveManageMapper {
    List<ShopliveInfoVo> selectShopliveList(ShopliveRequestDto shopliveRequestDto) throws Exception;

    ShopliveInfoVo selectShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception;

    int addShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception;

    int putShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception;

    int delShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception;
}
