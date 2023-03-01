package kr.co.pulmuone.v1.comm.mappers.batch.master.order.ifday;

import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelFailVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelInfoVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelSuccessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface IfDayChangeExcelOrderMapper {
    List<IfDayChangeDto> getIfDayChangeExcelTargetList(String batchStatusCd) throws Exception;

    List<IfDayExcelSuccessVo> getIfDayChangeSuccExcelTargetList(long ifIfDayExcelInfoId) throws Exception;

    int putIfDayChangeExcelInfo(IfDayExcelInfoVo ifDayExcelInfoVo) throws Exception;

    int putIfDayChange(@Param("odid") String odid, @Param("odOrderDetlSeq") int odOrderDetlSeq, @Param("orderIfDt") LocalDate orderIfDt, @Param("shippingDt") LocalDate shippingDt, @Param("deliveryDt") LocalDate deliveryDt);

    void addIfDayExcelFail(IfDayExcelFailVo ifDayExcelFailVo);
}
