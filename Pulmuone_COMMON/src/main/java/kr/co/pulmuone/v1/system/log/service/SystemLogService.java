package kr.co.pulmuone.v1.system.log.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.base.dto.ExcelDownLogRequestDto;
import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.mapper.system.log.SystemLogMapper;
import kr.co.pulmuone.v1.system.log.dto.*;
import kr.co.pulmuone.v1.system.log.dto.vo.DeviceLogVo;
import kr.co.pulmuone.v1.system.log.dto.vo.ExcelDownloadLogResultVo;
import kr.co.pulmuone.v1.system.log.dto.vo.MenuOperLogResultVo;
import kr.co.pulmuone.v1.system.log.dto.vo.PrivacyMenuOperLogResultVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class SystemLogService {

    private final SystemLogMapper mapper;

    /**
     * @param dto
     * @return
     * @return GetConnectLogListResponseDto
     * @Desc 접속로그 목록
     */
    protected GetConnectLogListResponseDto getConnectLogList(GetConnectLogListRequestDto dto) {
        GetConnectLogListResponseDto result = new GetConnectLogListResponseDto();

        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<GetConnectLogListResultVo> rows = mapper.getConnectLogList(dto);

        result.setTotal((int) rows.getTotal());
        result.setRows(rows.getResult());

        return result;
    }

    /**
     * @param dto
     * @return
     * @return GetBatchLogListResponseDto
     * @Desc 배치로그 목록
     */
    protected GetBatchLogListResponseDto getBatchLogList(GetBatchLogListRequestDto dto) {
        GetBatchLogListResponseDto result = new GetBatchLogListResponseDto();

        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<GetBatchLogListResultVo> rows = mapper.getBatchLogList(dto);

        result.setTotal((int) rows.getTotal());
        result.setRows(rows.getResult());

        return result;
    }


    /**
     * @param excelDownLogRequestDto
     * @return
     * @return Page<ExcelDownloadLogResultVo>
     * @Desc 엑셀다운로드 로그 리스트 조회
     */
    protected Page<ExcelDownloadLogResultVo> getExcelDownloadLogList(ExcelDownLogRequestDto excelDownLogRequestDto) {
        PageMethod.startPage(excelDownLogRequestDto.getPage(), excelDownLogRequestDto.getPageSize());
        return mapper.getExcelDownloadLogList(excelDownLogRequestDto);
    }

    /**
     * 비동기 엑셀다운로드 정보 저장
     *
     * @param requestDto ExcelDownloadAsyncRequestDto
     */
    protected void addExcelDownloadAsync(ExcelDownloadAsyncRequestDto requestDto) {
        mapper.addExcelDownloadAsync(requestDto);
    }

    /**
     * 비동기 엑셀다운로드 정보 수정
     *
     * @param stExcelDownloadAsyncId Long
     */
    protected void putExcelDownloadAsyncSetUse(Long stExcelDownloadAsyncId) {
        mapper.putExcelDownloadAsyncSetUse(stExcelDownloadAsyncId);
    }

    /**
     * 비동기 엑셀다운로드 정보 수정 - 에러처리
     *
     * @param stExcelDownloadAsyncId Long
     */
    protected void putExcelDownloadAsyncSetError(Long stExcelDownloadAsyncId) {
        mapper.putExcelDownloadAsyncSetError(stExcelDownloadAsyncId);
    }

    /**
     * 비동기 엑셀다운로드 정보 조회
     *
     * @param stExcelDownloadAsyncId Long
     * @return String 사용여부
     */
    protected String getExcelDownloadAsyncUseYn(Long stExcelDownloadAsyncId) {
        return mapper.getExcelDownloadAsyncUseYn(stExcelDownloadAsyncId);
    }
    
    /**
     * @param dto
     * @return
     * @return Page<MenuOperLogResultVo>
     * @Desc 메뉴사용이력 리스트 조회
     */
    protected Page<MenuOperLogResultVo> getMenuOperLogList(MenuOperLogRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        return mapper.getMenuOperLogList(dto);
    }


    /**
     * @param dto
     * @return
     * @return Page<PrivacyMenuOperLogResultVo>
     * @Desc 개인정보 처리 이력  목록
     */
    protected Page<PrivacyMenuOperLogResultVo> getPrivacyMenuOperLogList(PrivacyMenuOperLogRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());

        if (StringUtils.isNotEmpty(dto.getCrudType())) {
            dto.setCrudTypeList(Stream.of(dto.getCrudType().split("∀"))
                    .map(String::trim)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toList()));
        }

        return mapper.getPrivacyMenuOperLogList(dto);
    }

    /**
     * 디바이스 로그 저장 - 회원가입
     *
     * @param urPcidCd String
     * @param urUserId Long
     * @return Boolean
     */
    protected Boolean addDeviceLogUserJoin(String urPcidCd, Long urUserId) {
        int result = mapper.addDeviceLog(DeviceLogRequestDto.builder()
                .deviceLogType(SystemEnums.DeviceLogType.USER_JOIN)
                .urPcidCd(urPcidCd)
                .urUserId(urUserId)
                .build());

        return result > 0;
    }

    /**
     * 디바이스 로그 저장 - 로그인 실패
     *
     * @param urPcidCd String
     * @param urUserId Long
     * @return Boolean
     */
    protected Boolean addDeviceLogLoginFail(String urPcidCd, Long urUserId) {
        int result = mapper.addDeviceLog(DeviceLogRequestDto.builder()
                .deviceLogType(SystemEnums.DeviceLogType.LOGIN_FAIL)
                .urPcidCd(urPcidCd)
                .urUserId(urUserId)
                .build());

        return result > 0;
    }

    /**
     * 부정탐지 로그 저장 - 도난분실카드
     *
     * @param urPcidCd String
     * @param urUserId Long
     * @return Boolean
     * @Desc 비회원의 경우 urUserId : null
     */
    protected Boolean addIllegalLogStolenLostCard(String urPcidCd, Long urUserId) {
        IllegalLogRequestDto requestDto = IllegalLogRequestDto.builder()
                .illegalType(SystemEnums.IllegalType.ORDER)
                .illegalDetailType(SystemEnums.IllegalDetailType.STOLEN_LOST_CARD)
                .illegalStatusType(SystemEnums.IllegalStatusType.DETECT)
                .urPcidCd(urPcidCd)
                .createId(urUserId)
                .build();
        int result = mapper.addIllegalLog(requestDto);

        if (null != urUserId) {
            mapper.addIllegalLogUser(requestDto.getStIllegalLogId(), urUserId);
        }

        return result > 0;
    }

    /**
     * 부정탐지 로그 저장 - 거래불가카드
     *
     * @param urPcidCd String
     * @param urUserId Long
     * @return Boolean
     * @Desc 비회원의 경우 urUserId : null
     */
    protected Boolean addIllegalLogTransactionNotCard(String urPcidCd, Long urUserId) {
        IllegalLogRequestDto requestDto = IllegalLogRequestDto.builder()
                .illegalType(SystemEnums.IllegalType.ORDER)
                .illegalDetailType(SystemEnums.IllegalDetailType.TRANSACTION_NOT_CARD)
                .illegalStatusType(SystemEnums.IllegalStatusType.DETECT)
                .urPcidCd(urPcidCd)
                .createId(urUserId)
                .build();

        int result = mapper.addIllegalLog(requestDto);

        if (null != urUserId) {
            mapper.addIllegalLogUser(requestDto.getStIllegalLogId(), urUserId);
        }

        return result > 0;
    }

    /**
     * 부정탐지 로그 저장 - List
     *
     * @param dtoList List<IllegalLogRequestDto>
     */
    protected void addIllegalLogList(List<IllegalLogRequestDto> dtoList) {
        for (IllegalLogRequestDto dto : dtoList) {
            mapper.addIllegalLog(dto);
            if (dto.getUserIdList() != null && !dto.getUserIdList().isEmpty()) {
                mapper.addIllegalLogUserList(dto.getStIllegalLogId(), dto.getUserIdList());
            }
            if (dto.getOrderIdList() != null && !dto.getOrderIdList().isEmpty()) {
                mapper.addIllegalLogOrderList(dto.getStIllegalLogId(), dto.getOrderIdList());
            }
        }
    }

    /**
     * 부정탐지 - 회원가입, 로그인실패 케이스
     *
     * @param dto DetectDeviceLogRequestDto
     * @return List<DeviceLogVo>
     */
    protected List<DeviceLogVo> getDeviceLogDetect(DetectDeviceLogRequestDto dto) {
        List<DeviceLogVo> resultList = null;
        if (SystemEnums.DeviceLogType.USER_JOIN.equals(dto.getDeviceLogType())) {
            resultList = mapper.getDeviceLogDetect(dto);
            for (DeviceLogVo result : resultList) {
                result.setUserIdList(mapper.getDeviceLogDetectUser(dto, result.getUrPcidCd()));
            }
        } else if (SystemEnums.DeviceLogType.LOGIN_FAIL.equals(dto.getDeviceLogType())) {
            resultList = mapper.getLoginFailDetect(dto);
        }

        return resultList;
    }

}

