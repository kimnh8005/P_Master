package kr.co.pulmuone.v1.customer.inspect.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.customer.inspect.dto.InspectNoticeRequestDto;
import kr.co.pulmuone.v1.customer.inspect.dto.InspectNoticeResponseDto;
import kr.co.pulmuone.v1.customer.inspect.dto.vo.InspectNoticeIpVo;
import kr.co.pulmuone.v1.customer.inspect.dto.vo.InspectNoticeVo;
import kr.co.pulmuone.v1.user.join.service.UserJoinBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InspectNoticeBizImpl implements InspectNoticeBiz {

    @Autowired
    UserJoinBiz userJoinBiz;

    @Autowired
    InspectNoticeService inspectNoticeService;

    @Override
    public ApiResult<?> getInspectNotice() throws Exception {

        InspectNoticeVo inspectVo = inspectNoticeService.getInspectNotice();

        if (ObjectUtils.isEmpty(inspectVo)) {
            return ApiResult.success();
        }

        String startDt = DateUtil.convertFormat(inspectVo.getStartDt(), "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
        String endDt = DateUtil.convertFormat(inspectVo.getEndDt(), "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd");
        String startTime = DateUtil.convertFormat(inspectVo.getStartDt(), "yyyy-MM-dd h24:MM:ss", "h24:MM:ss");
        String endTime = DateUtil.convertFormat(inspectVo.getEndDt(), "yyyy-MM-dd h24:MM:ss", "h24:MM:ss");

        List<String> exceptIpList = new ArrayList<>();
        List<InspectNoticeIpVo> inspectNoticeIpVoList = inspectNoticeService.getInspectNoticeIpList();
        inspectNoticeIpVoList.forEach(e -> exceptIpList.add(e.getIpAddress()));

        InspectNoticeResponseDto responseDto = InspectNoticeResponseDto.builder()
                                                    .inspectName(inspectVo.getInspectName())
                                                    .mainTitle(inspectVo.getMainTitle())
                                                    .subTitle(inspectVo.getSubTitle())
                                                    .exceptIpList(exceptIpList)
                                                    .startDt(startDt)
                                                    .startHour(startTime.substring(11, 13))
                                                    .startMin(startTime.substring(14, 16))
                                                    .endDt(endDt)
                                                    .endHour(endTime.substring(11, 13))
                                                    .endMin(endTime.substring(14, 16))
                                                    .createDt(inspectVo.getCreateDt())
                                                    .createName(inspectVo.getCreateName())
                                                    .modifyDt(inspectVo.getModifyDt())
                                                    .modifyName(inspectVo.getModifyName())
                                                    .build();

        return ApiResult.success(responseDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApiResult<?> setInspectNotice(InspectNoticeRequestDto requestDto) throws Exception {
        InspectNoticeVo remainNoticeVo = inspectNoticeService.getInspectNotice();

        if (ObjectUtils.isEmpty(remainNoticeVo) == false) {
            int inspectNoticeId = remainNoticeVo.getCsInspectNoticeId();
            inspectNoticeService.deleteInspectNotice(inspectNoticeId);
            inspectNoticeService.deleteAllInspectNoticeIp(inspectNoticeId);
        }

        UserVo userVo = SessionUtil.getBosUserVO();
        String startDate = DateUtil.convertFormat(requestDto.getStartDt(), "yyyyMMdd", "yyyy-MM-dd");
        String endDate   = DateUtil.convertFormat(requestDto.getEndDt(), "yyyyMMdd", "yyyy-MM-dd");
        String startTime = String.join(":", requestDto.getStartHour(), requestDto.getStartMin(), "00");
        String endTime   = String.join(":", requestDto.getEndHour(), requestDto.getEndMin(), "00");
        String startTimestamp = String.join(" ", startDate, startTime);
        String endTimestamp = String.join(" ", endDate, endTime);

        InspectNoticeVo inspectNoticeVo = InspectNoticeVo.builder()
                                                        .inspectName(requestDto.getInspectName())
                                                        .mainTitle(requestDto.getMainTitle())
                                                        .subTitle(requestDto.getSubTitle())
                                                        .createId(Long.parseLong(userVo.getUserId()))
                                                        .modifyId(Long.parseLong(userVo.getUserId()))
                                                        .startDt(startTimestamp)
                                                        .endDt(endTimestamp)
                                                        .build();

        inspectNoticeService.addInspectNotice(inspectNoticeVo);
        InspectNoticeVo newNoticeVo = inspectNoticeService.getInspectNotice();
        
        //ip 저장 하기
        List<InspectNoticeIpVo> inspectNoticeIpVoList = new ArrayList<>();

        if (ObjectUtils.isEmpty(requestDto.getExceptIpList())) {
            return ApiResult.success();
        }

        requestDto.getExceptIpList().forEach(ip -> {
            InspectNoticeIpVo vo = InspectNoticeIpVo.builder()
                                        .ipAddress(ip)
                                        .createId(Long.parseLong(userVo.getUserId()))
                                        .csInspectNoticeId(newNoticeVo.getCsInspectNoticeId())
                                        .build();
            inspectNoticeIpVoList.add(vo);
        });

        inspectNoticeService.addAllInspectNoticeIp(inspectNoticeIpVoList);

        return ApiResult.success();
    }

    @Override
    public boolean checkInspectAllowed(String ipAddress) throws Exception {
        List<InspectNoticeIpVo> allIpVoList = inspectNoticeService.getInspectNoticeIpList();
        boolean hasExceptIpAddress = allIpVoList.stream().anyMatch(e -> ipAddress.equals(e.getIpAddress()));
        Boolean isBetweenServerTime = inspectNoticeService.getIsBetweenServerTime(LocalDateTime.now());

        if (isBetweenServerTime == null) {
            return true;
        }

        if (isBetweenServerTime == true && hasExceptIpAddress == true) {
            return true;
        } else if (isBetweenServerTime == true && hasExceptIpAddress == false) {
            return false;
        } else {
            return true;
        }
    }
}
