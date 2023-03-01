package kr.co.pulmuone.v1.send.push.service;

import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums.AppPushValidation;
import kr.co.pulmuone.v1.comm.mapper.send.push.SendPushMapper;
import kr.co.pulmuone.v1.send.push.dto.*;
import kr.co.pulmuone.v1.send.push.dto.vo.*;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerListResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendPushService {

    @Autowired
    private SendPushMapper sendPushMapper;

    /**
     * @Desc 모바일 푸시발송이력 조회
     * @param GetPushSendListRequestDto
     * @return GetPushSendListResponseDto
     * @throws Exception
     */
    protected GetPushSendListResponseDto getPushSendList(GetPushSendListRequestDto getPushSendListReqDto) {

    	PageMethod.startPage(getPushSendListReqDto.getPage(), getPushSendListReqDto.getPageSize());
        Page<GetPushSendListResultVo> rows = sendPushMapper.getPushSendList(getPushSendListReqDto);

        return GetPushSendListResponseDto.builder().total((int) rows.getTotal()).rows(rows.getResult()).build();
    }

    /**
     * @Desc 선택회원 PUSH 발송
     * @param addPushIssueSelectRequestDto
     * @return
     * @throws Exception
     */
    protected AddPushIssueSelectResponseDto addPushIssueSelect(AddPushIssueSelectRequestDto addPushIssueSelectRequestDto) {
        AddPushIssueSelectResponseDto result = new AddPushIssueSelectResponseDto();
        List<AddPushIssueSelectResultDto> rows = new ArrayList<AddPushIssueSelectResultDto>();
        List<AddPushManualResultDto> addPushManualResultList = new ArrayList<AddPushManualResultDto>(); // 기기타입 별 등록결과 리스트
        Map<String, List<PushSendListRequestDto>> deviceTypeGroupMap; // 기기타입 그룹

        AddPushIssueSelectResultDto row; // resultDto
        AddPushManualParamVo addPushManualParamVo; // 푸시발송정보 Vo
        AddPushIssueParamVo addPushIssueParamVo; // 회원별푸시발송정보 Vo

        String imageUrl = "";


        SendEnums.AppPushValidation appPushValidation = this.addPushIssueSelectValidation(addPushIssueSelectRequestDto);

        if( SendEnums.AppPushSendType.TEXT_IMAGE.getCode().equals(addPushIssueSelectRequestDto.getPushSendType()) ) {
            imageUrl = addPushIssueSelectRequestDto.getAddFileList().stream()
                    .findFirst()
                    .map(m -> m.getServerSubPath() + File.separator + m.getPhysicalFileName())
                    .orElseGet(String::new);
        }

        // 기기타입별 그룹핑
        deviceTypeGroupMap = addPushIssueSelectRequestDto.getBuyerDeviceList().stream().collect(Collectors.groupingBy(PushSendListRequestDto::getDeviceType));

        // 기기타입별 등록
        for(String deviceType : deviceTypeGroupMap.keySet()) {
            // 푸시발송정보 등록
            addPushManualParamVo = this.setAddPushManualParamVo(addPushIssueSelectRequestDto, deviceType, imageUrl);
            sendPushMapper.addPushManual(addPushManualParamVo);

            // 기기타입별 푸시발송정보 ID 셋팅
            addPushManualResultList.add(AddPushManualResultDto.builder()
                    .deviceType(deviceType)
                    .manualPushId(addPushManualParamVo.getManualPushId())
                    .build());
        }

        // 선택한 회원에 기기타입별 등록
        for(PushSendListRequestDto pushSendDto : addPushIssueSelectRequestDto.getBuyerDeviceList()) {

            // 회원별푸시발송정보 등록
            addPushIssueParamVo = this.setAddPushIssueParamVo(pushSendDto, addPushManualResultList);
            sendPushMapper.addPushIssue(addPushIssueParamVo);

            // 등록 ID 셋팅
            row = AddPushIssueSelectResultDto.builder()
                    .manualPushId(addPushIssueParamVo.getManualPushId())
                    .pushSendId(addPushIssueParamVo.getPushSendId())
                    .build();
            rows.add(row);
        }

        result.setRows(rows);

        return result;
    }

    /**
     * @Desc 선택회원 PUSH 발송 검증
     * @param dto
     * @throws Exception
     */
    private SendEnums.AppPushValidation addPushIssueSelectValidation(AddPushIssueSelectRequestDto dto) {
        if( CollectionUtils.isEmpty(dto.getBuyerDeviceList()) ) {
            return AppPushValidation.NO_USER_SELECTED;
        }

        if( SendEnums.AppPushSendType.TEXT_IMAGE.getCode().equals(dto.getPushSendType()) && (CollectionUtils.isEmpty(dto.getAddFileList())) ) {
            return AppPushValidation.REQUIRED_ATTACHMENT;
        }

        if( SendEnums.UseYn.Y.name().equals(dto.getSendClassification()) && StringUtils.isEmpty(dto.getReservationDate()) ) {
            return AppPushValidation.REQUIRED_RESERVATION_DATE;
        }

        return AppPushValidation.SUCCESS;
    }

    /**
     * @Desc 선택회원 푸시발송정보 등록 파라미터 셋팅
     * @param dto
     * @param deviceType
     * @param imageUrl
     * @return
     */
    private AddPushManualParamVo setAddPushManualParamVo(AddPushIssueSelectRequestDto dto, String deviceType, String imageUrl) {
        return AddPushManualParamVo.builder()
                .administratorTitle(dto.getAdministratorTitle())
                .advertAndNoticeType(dto.getAdvertAndNoticeType())
                .pushTitleAndroid(dto.getPushTitleAndroid())
                .pushSubTitleAndroid(dto.getPushSubTitleAndroid())
                .pushTitleIos(dto.getPushTitleIos())
                .reserveYn(dto.getSendClassification())
                .reservationDate(dto.getReservationDate())
                .pushSendType(dto.getPushSendType())
                .imageUrl(imageUrl)
                .content(dto.getContent())
                .pushLink(dto.getPushLink())
                .createId(dto.getUserVo().getUserId())
                .deviceType(deviceType)
                .build();
    }

    /**
     * @Desc 회원별푸시발송정보 등록 파라미터 셋팅
     * @param pushSendDto
     * @param addPushManualResultList
     * @return
     */
    private AddPushIssueParamVo setAddPushIssueParamVo(PushSendListRequestDto pushSendDto, List<AddPushManualResultDto> addPushManualResultList) {
        return AddPushIssueParamVo.builder()
                .manualPushId(addPushManualResultList.stream().filter(f -> f.getDeviceType().equals(pushSendDto.getDeviceType()))
                        .findFirst()
                        .map(a -> a.getManualPushId())
                        .orElse(0))
                .deviceInfoId(pushSendDto.getDeviceInfoId())
                .build();
    }

    /**
     * @Desc 검색회원 PUSH 발송
     * @param addPushIssueSearchRequestDto
     * @return
     * @throws Exception
     */
    protected AddPushIssueSearchResponseDto addPushIssueSearch(AddPushIssueSearchRequestDto addPushIssueSearchRequestDto) {
        AddPushIssueSearchResponseDto result = new AddPushIssueSearchResponseDto();

        List<AddPushIssueSearchResultDto> rows = new ArrayList<AddPushIssueSearchResultDto>();
        List<AddPushManualResultDto> addPushManualResultList = new ArrayList<AddPushManualResultDto>(); // 기기타입 별 등록결과 리스트

        Map<String, List<PushSendListRequestDto>> deviceTypeGroupMap; // 기기타입 그룹

        AddPushIssueSearchResultDto row; // resultDto
        AddPushManualParamVo addPushManualParamVo; // 푸시발송정보 Vo
        AddPushIssueParamVo addPushIssueParamVo; // 회원별푸시발송정보 Vo

        String imageUrl = "";

        SendEnums.AppPushValidation appPushValidation = addPushIssueSearchValidation(addPushIssueSearchRequestDto);

        if( SendEnums.AppPushSendType.TEXT_IMAGE.getCode().equals(addPushIssueSearchRequestDto.getPushSendType()) ) {
            imageUrl = addPushIssueSearchRequestDto.getAddFileList().stream()
                    .findFirst()
                    .map(m -> m.getServerSubPath() + File.separator + m.getPhysicalFileName())
                    .orElseGet(String::new);
        }

        // 기기타입별 그룹핑
        deviceTypeGroupMap = addPushIssueSearchRequestDto.getBuyerDeviceList().stream().collect(Collectors.groupingBy(PushSendListRequestDto::getDeviceType));

        // 기기타입별 등록
        for(String deviceType : deviceTypeGroupMap.keySet()) {
            // 푸시발송정보 등록
            addPushManualParamVo = this.setAddPushManualParamVo(addPushIssueSearchRequestDto, deviceType, imageUrl);
            sendPushMapper.addPushManual(addPushManualParamVo);

            // 기기타입별 푸시발송정보 ID 셋팅
            addPushManualResultList.add(AddPushManualResultDto.builder()
                    .deviceType(deviceType)
                    .manualPushId(addPushManualParamVo.getManualPushId())
                    .build());
        }

        // 선택한 회원만큼 등록
        for(PushSendListRequestDto pushSendDto : addPushIssueSearchRequestDto.getBuyerDeviceList()) {

            // 회원별푸시발송정보 등록
            addPushIssueParamVo = this.setAddPushIssueParamVo(pushSendDto, addPushManualResultList);
            sendPushMapper.addPushIssue(addPushIssueParamVo);

            // 등록 ID 셋팅
            row = AddPushIssueSearchResultDto.builder()
                    .manualPushId(addPushIssueParamVo.getManualPushId())
                    .pushSendId(addPushIssueParamVo.getPushSendId())
                    .build();

            rows.add(row);
        }

        result.setRows(rows);

        return result;
    }

    /**
     * @Desc 검색회원 PUSH 발송 검증
     * @param addPushIssueSearchRequestDto
     * @throws Exception
     */
    private SendEnums.AppPushValidation addPushIssueSearchValidation(AddPushIssueSearchRequestDto addPushIssueSearchRequestDto) {
        if( CollectionUtils.isEmpty(addPushIssueSearchRequestDto.getBuyerDeviceList()) ) {
            return SendEnums.AppPushValidation.NO_USER_SELECTED;
        }

        if( SendEnums.AppPushSendType.TEXT_IMAGE.getCode().equals(addPushIssueSearchRequestDto.getPushSendType()) && (CollectionUtils.isEmpty(addPushIssueSearchRequestDto.getAddFileList())) ) {
            return SendEnums.AppPushValidation.REQUIRED_ATTACHMENT;
        }

        if( SendEnums.UseYn.Y.name().equals(addPushIssueSearchRequestDto.getSendClassification()) && StringUtils.isEmpty(addPushIssueSearchRequestDto.getReservationDate()) ) {
            return SendEnums.AppPushValidation.REQUIRED_RESERVATION_DATE;
        }

        return SendEnums.AppPushValidation.SUCCESS;
    }

    /**
     * @Desc 검색회원 PUSH 발송 푸시발송정보 등록 파라미터 셋팅
     * @param dto
     * @param deviceType
     * @param imageUrl
     * @return
     */
    private AddPushManualParamVo setAddPushManualParamVo(AddPushIssueSearchRequestDto dto, String deviceType, String imageUrl) {
        return AddPushManualParamVo.builder()
                .administratorTitle(dto.getAdministratorTitle())
                .advertAndNoticeType(dto.getAdvertAndNoticeType())
                .pushTitleAndroid(dto.getPushTitleAndroid())
                .pushSubTitleAndroid(dto.getPushSubTitleAndroid())
                .pushTitleIos(dto.getPushTitleIos())
                .reserveYn(dto.getSendClassification())
                .reservationDate(dto.getReservationDate())
                .pushSendType(dto.getPushSendType())
                .imageUrl(imageUrl)
                .content(dto.getContent())
                .pushLink(dto.getPushLink())
                .createId(dto.getUserVo().getUserId())
                .deviceType(deviceType)
                .build();
    }

    /**
     * @Desc 모바일 푸시 발송(전체) PUSH 발송
     * @param addPushIssueAllRequestDto
     * @return
     * @throws Exception
     */
    protected AddPushIssueAllResponseDto addPushIssueAll(AddPushIssueAllRequestDto addPushIssueAllRequestDto) {
        AddPushIssueAllResponseDto result = new AddPushIssueAllResponseDto();
        List<AddPushIssueAllResultDto> rows = new ArrayList<AddPushIssueAllResultDto>();
        List<AddPushManualResultDto> addPushManualResultList = new ArrayList<AddPushManualResultDto>(); // 기기타입 별 등록결과 리스트
        List<GetSendUserDeviceListResultVo> sendUserDeviceList; // 발송회원 기기정보 리스트
        Map<String, List<GetSendUserDeviceListResultVo>> deviceTypeGroupMap; // 기기타입 그룹

        AddPushIssueAllResultDto row; // resultDto
        AddPushManualParamVo addPushManualParamVo; // 푸시발송정보 Vo
        AddPushIssueParamVo addPushIssueParamVo; // 회원별푸시발송정보 Vo

        String imageUrl = "";

        AppPushValidation appPushValidation = this.addPushIssueAllValidation(addPushIssueAllRequestDto);

        if( SendEnums.AppPushSendType.TEXT_IMAGE.getCode().equals(addPushIssueAllRequestDto.getPushSendType()) ) {
            imageUrl = addPushIssueAllRequestDto.getAddFileList().stream()
                    .findFirst()
                    .map(m -> m.getServerSubPath() + File.separator + m.getPhysicalFileName())
                    .orElseGet(String::new);
        }

        // 발송 기기정보 조회
        sendUserDeviceList = sendPushMapper.getSendUserDeviceList(GetSendUserDeviceListParamVo.builder()
                .sendGroup(addPushIssueAllRequestDto.getSendGroup())
//                .uploadUserList(addPushIssueAllRequestDto.getUploadUserList())
                .build());
        // 기기타입별 그룹핑
        deviceTypeGroupMap = sendUserDeviceList.stream().collect(Collectors.groupingBy(GetSendUserDeviceListResultVo::getDeviceType));

        // 기기타입별 등록
        for(String deviceType : deviceTypeGroupMap.keySet()) {
            // 푸시발송정보 등록
            addPushManualParamVo = this.setAddPushManualParamVo(addPushIssueAllRequestDto, deviceType, imageUrl);
            sendPushMapper.addPushManual(addPushManualParamVo);

            // 기기타입별 푸시발송정보 ID 셋팅
            addPushManualResultList.add(AddPushManualResultDto.builder()
                    .deviceType(deviceType)
                    .manualPushId(addPushManualParamVo.getManualPushId())
                    .build());
        }

        // 발송회원 대상만큼 등록
        for(GetSendUserDeviceListResultVo sendUserDeviceVo : sendUserDeviceList) {

            // 회원별푸시발송정보 등록
            addPushIssueParamVo = this.setAddPushIssueParamVo(sendUserDeviceVo, addPushManualResultList);
            sendPushMapper.addPushIssue(addPushIssueParamVo);

            // 등록 ID 셋팅
            row = AddPushIssueAllResultDto.builder()
                    .manualPushId(addPushIssueParamVo.getManualPushId())
                    .pushSendId(addPushIssueParamVo.getPushSendId())
                    .build();

            rows.add(row);
        }

        result.setRows(rows);

        return result;
    }

    /**
     * @Desc 모바일 푸시 발송(전체) PUSH 발송 검증
     * @param dto
     * @throws Exception
     */
    private SendEnums.AppPushValidation addPushIssueAllValidation(AddPushIssueAllRequestDto dto) {
        if( "UPLOAD_USER".equals(dto.getSendGroup()) && CollectionUtils.isEmpty(dto.getUploadUserList()) ) {
            return AppPushValidation.NO_USER_SELECTED;
        }

        if( SendEnums.AppPushSendType.TEXT_IMAGE.getCode().equals(dto.getPushSendType()) && (CollectionUtils.isEmpty(dto.getAddFileList())) ) {
            return AppPushValidation.REQUIRED_ATTACHMENT;
        }

        if( SendEnums.UseYn.Y.name().equals(dto.getSendClassification()) && StringUtils.isEmpty(dto.getReservationDate()) ) {
            return AppPushValidation.REQUIRED_RESERVATION_DATE;
        }
        return AppPushValidation.SUCCESS;
    }

    /**
     * @Desc  모바일 푸시 발송(전체) 푸시발송정보 등록 파라미터 셋팅
     * @param dto
     * @param deviceType
     * @param imageUrl
     * @return
     */
    private AddPushManualParamVo setAddPushManualParamVo(AddPushIssueAllRequestDto dto, String deviceType, String imageUrl) {
        return AddPushManualParamVo.builder()
                .administratorTitle(dto.getAdministratorTitle())
                .advertAndNoticeType(dto.getAdvertAndNoticeType())
                .pushTitleAndroid(dto.getPushTitleAndroid())
                .pushSubTitleAndroid(dto.getPushSubTitleAndroid())
                .pushTitleIos(dto.getPushTitleIos())
                .reserveYn(dto.getSendClassification())
                .reservationDate(dto.getReservationDate())
                .pushSendType(dto.getPushSendType())
                .imageUrl(imageUrl)
                .content(dto.getContent())
                .pushLink(dto.getPushLink())
                .createId(dto.getUserVo().getUserId())
                .deviceType(deviceType)
                .build();
    }

    /**
     * @Desc 모바일 푸시 발송(전체) 회원별푸시발송정보 등록 파라미터 셋팅
     * @param sendUserDeviceVo
     * @param addPushManualResultList
     * @return
     */
    private AddPushIssueParamVo setAddPushIssueParamVo(GetSendUserDeviceListResultVo sendUserDeviceVo, List<AddPushManualResultDto> addPushManualResultList) {
        return AddPushIssueParamVo.builder()
                .manualPushId(addPushManualResultList.stream().filter(f -> f.getDeviceType().equals(sendUserDeviceVo.getDeviceType()))
                        .findFirst()
                        .map(m -> m.getManualPushId())
                        .orElse(0))
                .deviceInfoId(sendUserDeviceVo.getDeviceInfoId())
                .build();
    }

}
