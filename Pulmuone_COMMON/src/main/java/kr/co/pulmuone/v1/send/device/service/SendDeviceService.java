package kr.co.pulmuone.v1.send.device.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import kr.co.pulmuone.v1.send.push.dto.PushSendListRequestDto;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.mapper.send.device.SendDeviceMapper;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceResponseDto;
import kr.co.pulmuone.v1.send.device.dto.vo.GetBuyerDeviceListParamVo;
import kr.co.pulmuone.v1.send.device.dto.vo.GetBuyerDeviceListResultVo;
import kr.co.pulmuone.v1.send.device.dto.vo.GetDeviceListResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("sendDeviceServiceHangaram")
@RequiredArgsConstructor
public class SendDeviceService {

    @NotNull
    private final SendDeviceMapper sendDeviceMapper;

    /**
     * @Desc  APP 설치 단말기 목록 조회
     * @param dto GetDeviceListRequestDto
     * @return GetDeviceListResponseDto
     * @throws Exception Exception
     */
    @UserMaskingRun(system="BOS")
    protected GetDeviceListResponseDto getDeviceList(GetDeviceListRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<GetDeviceListResultVo> rows = sendDeviceMapper.getDeviceList(dto);

        return GetDeviceListResponseDto.builder()
                .total((int) rows.getTotal())
                .rows(rows.getResult())
                .build();
    }

    /**
     * @Desc  푸시 가능 회원 조회
     * @param dto GetBuyerDeviceListRequestDto
     * @return GetBuyerDeviceListResponseDto
     * @throws Exception Exception
     */
    protected GetBuyerDeviceListResponseDto getBuyerDeviceList(GetBuyerDeviceListRequestDto dto) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        boolean isPage = true;
        GetBuyerDeviceListParamVo param = this.setBuyerDeviceListParamVo(dto, isPage);
        Page<GetBuyerDeviceListResultVo> rows = sendDeviceMapper.getBuyerDeviceList(param);

        return GetBuyerDeviceListResponseDto.builder()
                .total((int) rows.getTotal())
                .rows(rows.getResult())
                .build();
    }

    /**
     * @Desc 푸시 가능 회원 조회 파라미터 셋팅
     * @param dto
     * @return
     */
    protected GetBuyerDeviceListParamVo setBuyerDeviceListParamVo(GetBuyerDeviceListRequestDto dto, boolean isPage) {
        GetBuyerDeviceListParamVo reParam;

        if(!dto.getCondiValue().isEmpty()) {
            reParam = GetBuyerDeviceListParamVo.builder()
                    .condiType(dto.getCondiType())
                    .condiValueArray(Stream.of(dto.getCondiValue().split("\n|,"))
                            .map(String::trim)
                            .collect(Collectors.toList()))
                    .build();

        }else {
            reParam = GetBuyerDeviceListParamVo.builder()
                    .mobile(dto.getMobile())
                    .mail(dto.getMail())
                    .userType(dto.getUserType())
                    .userLevel(dto.getUserLevel())
                    .userStatus(dto.getUserStatus())
                    .joinDateStart(dto.getJoinDateStart())
                    .joinDateEnd(dto.getJoinDateEnd())
                    .lastVisitDateStart(dto.getLastVisitDateStart())
                    .lastVisitDateEnd(dto.getLastVisitDateEnd())
                    .deviceType(dto.getDeviceType())
                    .pushReception(dto.getPushReception())
                    .nightPushReception(dto.getNightPushReception())
                    .build();
        }

        reParam.setSPage(dto.getsPage());
        reParam.setPage(dto.getPage());
        reParam.setEPage(dto.getePage());
        reParam.setPageSize(dto.getPageSize());

        return reParam;
    }


    /**
     * @Desc  플랫폼 유형 조회
     * @param dto GetDeviceRequestDto
     * @return GetDeviceListResponseDto
     * @throws Exception Exception
     */
    protected GetDeviceResponseDto getDeviceEvnetImage(GetDeviceListRequestDto dto) throws Exception {
        List<GetDeviceListResultVo> rows = sendDeviceMapper.getDeviceEvnetImage(dto);

        GetDeviceResponseDto result = new GetDeviceResponseDto();

        result.setRows(rows);

        return result;
    }



    /**
     * 이벤트 이미지 저장
     * @param GetDeviceRequestDto
     * @return
     * @throws Exception
     */
    protected GetDeviceResponseDto setDeviceEventImage(GetDeviceRequestDto getDeviceRequestDto) throws Exception {
    	GetDeviceResponseDto result = new GetDeviceResponseDto();
    	result.setTotal(sendDeviceMapper.setDeviceEventImage(getDeviceRequestDto));
    	return result;
    }

    /**
     * @Desc  푸시 가능 회원 모두 조회
     * @param vo
     * @return List<PushSendListRequestDto>
     * @throws Exception
     */
    protected List<PushSendListRequestDto> getBuyerDeviceSearchAllList(GetBuyerDeviceListParamVo vo) throws Exception {
        return sendDeviceMapper.getBuyerDeviceSearchAllList(vo);
    }
}
