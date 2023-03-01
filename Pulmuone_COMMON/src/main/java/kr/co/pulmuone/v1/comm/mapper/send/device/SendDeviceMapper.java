package kr.co.pulmuone.v1.comm.mapper.send.device;

import java.util.List;

import kr.co.pulmuone.v1.send.push.dto.PushSendListRequestDto;
import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.send.device.dto.GetDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceRequestDto;
import kr.co.pulmuone.v1.send.device.dto.vo.GetBuyerDeviceListParamVo;
import kr.co.pulmuone.v1.send.device.dto.vo.GetBuyerDeviceListResultVo;
import kr.co.pulmuone.v1.send.device.dto.vo.GetDeviceListResultVo;

@Mapper
public interface SendDeviceMapper {

    /**
     * APP 설치 단말기 목록 조회
     * @param vo GetDeviceListRequestDto
     * @return Page<GetDeviceListResultVo>
     */
    Page<GetDeviceListResultVo> getDeviceList(GetDeviceListRequestDto vo);

    /**
     * 푸시 가능 회원 조회
     * @param vo GetBuyerDeviceListParamVo
     * @return Page<GetBuyerDeviceListResultVo>
     */
    Page<GetBuyerDeviceListResultVo> getBuyerDeviceList(GetBuyerDeviceListParamVo vo);

    List<GetDeviceListResultVo> getDeviceEvnetImage(GetDeviceListRequestDto vo);

    int setDeviceEventImage(GetDeviceRequestDto dto) throws Exception;

    List<PushSendListRequestDto> getBuyerDeviceSearchAllList(GetBuyerDeviceListParamVo vo);
}
