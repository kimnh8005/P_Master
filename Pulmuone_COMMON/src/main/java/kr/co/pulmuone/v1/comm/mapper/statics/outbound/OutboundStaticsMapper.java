package kr.co.pulmuone.v1.comm.mapper.statics.outbound;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.vo.MissOutboundStaticsVo;
import kr.co.pulmuone.v1.statics.outbound.dto.vo.OutboundStaticsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OutboundStaticsMapper {

    List<OutboundStaticsVo> getOutboundStaticsList(OutboundStaticsRequestDto dto) throws BaseException;

    List<MissOutboundStaticsVo> getMissOutboundStaticsList(MissOutboundStaticsRequestDto dto) throws BaseException;

    List<OutboundStaticsVo> getOutboundMissStaticsList(OutboundStaticsRequestDto dto) throws BaseException;

}
