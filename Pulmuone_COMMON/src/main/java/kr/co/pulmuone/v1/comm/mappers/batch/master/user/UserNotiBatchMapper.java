package kr.co.pulmuone.v1.comm.mappers.batch.master.user;

import kr.co.pulmuone.v1.batch.user.noti.dto.vo.NotiBatchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserNotiBatchMapper {

    void addNotiHistory(@Param("notiList") List<NotiBatchVo> notiList);

    List<NotiBatchVo> getNotiExhibitStart(@Param("urNotiType") String urNotiType);

    List<NotiBatchVo> getNotiEventStart(@Param("urNotiType") String urNotiType);

    List<NotiBatchVo> getNotiEventEnd(@Param("urNotiType") String urNotiType);

}
