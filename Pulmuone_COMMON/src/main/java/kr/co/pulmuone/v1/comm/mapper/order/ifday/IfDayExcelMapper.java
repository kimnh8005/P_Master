package kr.co.pulmuone.v1.comm.mapper.order.ifday;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeOrderCntDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelFailRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelListRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelFailVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelInfoVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelSuccessVo;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelFailRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallExcelListRequestDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelFailVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelSuccessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IfDayExcelMapper {

    IfDayChangeOrderCntDto getOrderDetlCount(@Param("odid") String odid, @Param("odOrderDetlSeq") int odOrderDetlSeq);

    void addIfDayExcelInfo(IfDayExcelInfoVo vo);

    void putIfDayExcelInfo(IfDayExcelInfoVo vo);

    void addIfDayExcelSuccess(@Param("ifIfDayExcelInfoId") Long ifIfDayExcelInfoId, @Param("voList") List<IfDayExcelSuccessVo> voList);

    void addIfDayExcelFail(@Param("ifIfDayExcelInfoId") Long ifIfDayExcelInfoId, @Param("voList") List<IfDayExcelFailVo> voList);

    IfDayExcelInfoVo getIfDayExcelInfo(IfDayExcelFailRequestDto ifDayExcelFailRequestDto);

    Page<IfDayExcelInfoVo> getIfDayExcelInfoList(IfDayExcelListRequestDto dto);

    List<IfDayExcelFailVo> getIfDayFailExcelDownload(IfDayExcelFailRequestDto dto);

    Boolean isSameOrderIfDay(IfDayChangeDto dto);

    IfDayChangeOrderCntDto getOrderDetlCountByClaim(@Param("odid") String odid, @Param("odOrderDetlSeq") int odOrderDetlSeq);

}
