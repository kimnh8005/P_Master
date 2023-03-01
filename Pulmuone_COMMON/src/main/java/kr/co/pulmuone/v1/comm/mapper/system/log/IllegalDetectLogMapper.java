package kr.co.pulmuone.v1.comm.mapper.system.log;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.system.log.dto.IllegalDetectLogRequestDto;
import kr.co.pulmuone.v1.system.log.dto.vo.IllegalDetectLogResultVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IllegalDetectLogMapper {

    Page<IllegalDetectLogResultVo> getIllegalDetectLogList(IllegalDetectLogRequestDto dto);

    IllegalDetectLogResultVo getIllegalDetectLogDetail(String stIllegalLogId);

    int putIllegalDetectDetailInfo(IllegalDetectLogRequestDto dto);

    List<IllegalDetectLogResultVo> illegalDetectListExportExcel(IllegalDetectLogRequestDto dto);

    List<IllegalDetectLogResultVo> illegalDetectUserIdxportExcel(IllegalDetectLogRequestDto dto);

    List<IllegalDetectLogResultVo> illegalDetectOrderExportExcel(IllegalDetectLogRequestDto dto);
}
