package kr.co.pulmuone.v1.comm.mapper.promotion.serialnumber;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.GetSerialNumberListRequestDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.PutSerialNumberCancelRequestSaveDto;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.vo.CommonGetSerialNumberInfoVo;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.vo.GetSerialNumberListResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SerialNumberMapper {
	Page<GetSerialNumberListResultVo> getSerialNumberList(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception;

    int getSerialNumberListCount(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception;

    //void putSerialNumberCancel(PutSerialNumberCancelRequestDto putSerialNumberCancelRequestDto) throws Exception;

    int putSerialNumberCancel(List<PutSerialNumberCancelRequestSaveDto> voList) throws Exception;

    CommonGetSerialNumberInfoVo getSerialNumberInfo(String serialNumber) throws Exception;

    void putSerialNumberSetUse(@Param("pmSerialNumberId") Long pmSerialNumberId, @Param("urUserId") Long urUserId) throws Exception;

    List<GetSerialNumberListResultVo> serialNumberListExcel(GetSerialNumberListRequestDto getSerialNumberListRequestDto) throws Exception;

    int redeemSerialNumber(@Param("urUserId") Long urUserId, @Param("serialNumber") String serialNumber);
}
