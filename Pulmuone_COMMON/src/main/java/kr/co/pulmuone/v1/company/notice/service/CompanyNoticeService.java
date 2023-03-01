package kr.co.pulmuone.v1.company.notice.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums.EnumSiteType;
import kr.co.pulmuone.v1.comm.mapper.company.notice.CompanyNoticeMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.company.notice.dto.*;
import kr.co.pulmuone.v1.company.notice.dto.vo.*;
import kr.co.pulmuone.v1.policy.bbs.service.PolicyBbsBannedWordBiz;
import kr.co.pulmuone.v1.user.login.dto.vo.RecentlyLoginResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyNoticeService {

    @Autowired
    private CompanyNoticeMapper companyNoticeMapper;

    @Autowired
    PolicyBbsBannedWordBiz policyBbsBannedWordBiz;


    protected ApiResult<?> addNotice(AddNoticeRequestDto dto) throws Exception {
        AddNoticeResponseDto result = new AddNoticeResponseDto();

        // 제목 내용 필수 값 validate
        if(dto.getTitle().isEmpty()
                || dto.getContent().isEmpty()
                || dto.getContent().equals(" ")) {

            return ApiResult.result(BaseEnums.CommBase.MANDATORY_MISSING);
        }

        // 공지사항 팝업 사용 시 필수 값 validate
        if(dto.getPopupYn().equals("Y")) {
            if(dto.getPopupCoordinateX().isEmpty()
                    || dto.getPopupCoordinateY().isEmpty()
                    || dto.getPopupDisplayStartDate().isEmpty()
                    || dto.getPopupDisplayEndDate().isEmpty()) {

                return ApiResult.result(BaseEnums.CommBase.MANDATORY_MISSING);
            }
        }

        // 금칙어 마스킹 처리
        dto.setContent(policyBbsBannedWordBiz.filterSpamWord(dto.getContent(), EnumSiteType.BOS));

        companyNoticeMapper.addNotice(dto);

        // Add After Key
        String csCompanyBbsId = dto.getCsCompanyBbsId();

        if(!dto.getAddFileList().isEmpty()) {
            for(FileVo fileVo : dto.getAddFileList()) {
                AddNoticeAttachParamDto addNoticeAttachParamDto = new AddNoticeAttachParamDto();

                // File Vo Get -> AddNoticeAttachParamDto Set
                addNoticeAttachParamDto.setPhysicalAttachment(StringUtil.nvl(fileVo.getOriginalFileName()));
                addNoticeAttachParamDto.setRealAttachment(StringUtil.nvl(fileVo.getPhysicalFileName()));
                addNoticeAttachParamDto.setFilePath(StringUtil.nvl(fileVo.getServerSubPath()));

                // csCompBbsId -> AddNoticeAttachParamDto Set
                addNoticeAttachParamDto.setCsCompanyBbsId(csCompanyBbsId);

                this.addNoticeAttach(addNoticeAttachParamDto);
            }
        }

        AddNoticeResultVo rows = new AddNoticeResultVo();
        rows.setCsCompanyBbsId(csCompanyBbsId);
        result.setRows(rows);

        return ApiResult.success(result);
    }

    private int addNoticeAttach(AddNoticeAttachParamDto dto) {

        return companyNoticeMapper.addNoticeAttach(dto);
    }

    protected ApiResult<?> putNotice(PutNoticeRequestDto dto) throws Exception {
        PutNoticeResponseDto result = new PutNoticeResponseDto();

        // 제목 내용 필수 값 validate
        if(dto.getTitle().isEmpty()
                || dto.getContent().isEmpty()
                || dto.getContent().equals(" ")) {

            return ApiResult.result(BaseEnums.CommBase.MANDATORY_MISSING);
        }

        // 공지사항 팝업 사용 시 필수 값 validate
        if(dto.getPopupYn().equals("Y")) {
            if(dto.getPopupCoordinateX().isEmpty()
                    || dto.getPopupCoordinateY().isEmpty()
                    || dto.getPopupDisplayStartDate().isEmpty()
                    || dto.getPopupDisplayEndDate().isEmpty()) {
                return ApiResult.result(BaseEnums.CommBase.MANDATORY_MISSING);
            }
        }

        // 금칙어 마스킹 처리
        dto.setContent(policyBbsBannedWordBiz.filterSpamWord(dto.getContent(), EnumSiteType.BOS));

        companyNoticeMapper.putNotice(dto);

        NoticeAttachParamDto noticeAttachParamDto = NoticeAttachParamDto.builder()
                .csCompanyBbsId(StringUtil.nvl(dto.getCsCompanyBbsId()))
                .build();

        if(!dto.getAddFileList().isEmpty()) {
            for(FileVo fileVo : dto.getAddFileList()) {
                companyNoticeMapper.delNoticeAttach(noticeAttachParamDto);

                AddNoticeAttachParamDto addNoticeAttachParamDto = new AddNoticeAttachParamDto();

                // File Vo Get -> AddNoticeAttachParamDto Set
                addNoticeAttachParamDto.setPhysicalAttachment(StringUtil.nvl(fileVo.getOriginalFileName()));
                addNoticeAttachParamDto.setRealAttachment(StringUtil.nvl(fileVo.getPhysicalFileName()));
                addNoticeAttachParamDto.setFilePath(StringUtil.nvl(fileVo.getServerSubPath()));

                // csCompBbsId -> AddNoticeAttachParamDto Set
                addNoticeAttachParamDto.setCsCompanyBbsId(StringUtil.nvl(dto.getCsCompanyBbsId()));

                this.addNoticeAttach(addNoticeAttachParamDto);
            }
        }

        PutNoticeResultVo rows = new PutNoticeResultVo();
        rows.setCsCompanyBbsId(StringUtil.nvl(dto.getCsCompanyBbsId()));
        result.setRows(rows);

        return ApiResult.success(result);
    }

    protected GetNoticeResponseDto getNotice(GetNoticeRequestDto dto) throws Exception {
        GetNoticeResponseDto result = new GetNoticeResponseDto();

        if(dto.getViewMode().equals("Y")){
            this.addNoticeViewCount(dto);
        }

        GetNoticeResultVo rows = companyNoticeMapper.getNotice(dto);
        GetNoticeAttachResultVo rowsFile = companyNoticeMapper.getNoticeAttach(dto);

        result.setRows(rows);
        result.setRowsFile(rowsFile);

        return result;
    }

    private int addNoticeViewCount(GetNoticeRequestDto dto) {
        return companyNoticeMapper.addNoticeViewCount(dto);
    }

    protected GetNoticeListResponseDto getNoticeList(GetNoticeListRequestDto dto) throws Exception {
        GetNoticeListResponseDto result = new GetNoticeListResponseDto();

        if (dto.isDashboardCallYn() == true) {
          // ------------------------------------------------------------------
          // 대시보드에서 호출
          // ------------------------------------------------------------------
          int searchCnt = 5;

          if (StringUtil.isEmpty(dto.getSearchCnt()) || dto.getSearchCnt() <= 0) {
            // 화면에서 개수정보 미입력 시 기본값 5 설정
            dto.setSearchCnt(searchCnt);
          }

          List<GetNoticeListResultVo> rows = companyNoticeMapper.getDashboardNoticeList(dto);  // rows

          result.setRows(rows);

          if (rows != null && rows.size() > 0) {
            result.setTotal(rows.size());
          }
          else {
            result.setTotal(0);
          }
        }
        else {
          // ------------------------------------------------------------------
          // 일반 호출
          // ------------------------------------------------------------------
          int total = companyNoticeMapper.getNoticeListCount(dto);    // total
          List<GetNoticeListResultVo> rows = companyNoticeMapper.getNoticeList(dto);  // rows

          result.setTotal(total);
          result.setRows(rows);
        }

        return result;
    }

    protected int delNotice(DelNoticeRequestDto dto) throws Exception {

        List<DelNoticeRequestSaveDto> deleteRequestDtoList = dto.getDeleteRequestDtoList();

        int cnt = 0;

        //데이터 삭제
        if(!deleteRequestDtoList.isEmpty()){
            for(DelNoticeRequestSaveDto vo : deleteRequestDtoList) {
                NoticeAttachParamDto noticeAttachParamDto = NoticeAttachParamDto.builder()
                        .csCompanyBbsId(vo.getCsCompanyBbsId()).build();
                cnt += companyNoticeMapper.delNoticeAttach(noticeAttachParamDto);
                cnt += companyNoticeMapper.delNotice(vo);
            }
        }

        return cnt;
    }

    protected int putNoticeSet(PutNoticeSetRequestDto dto) throws Exception {

        List<PutNoticeSetRequestSaveDto> updateRequestDtoList = dto.getUpdateRequestDtoList();

        int cnt = 0;

        if(!updateRequestDtoList.isEmpty()){
            NoticeParamDto noticeParamDto = new NoticeParamDto();
            for(PutNoticeSetRequestSaveDto vo : updateRequestDtoList) {

                noticeParamDto.setCsCompanyBbsId(vo.getCsCompanyBbsId());
                noticeParamDto.setNotificationYn(dto.getNotificationYn());
                cnt += companyNoticeMapper.putNoticeSet(noticeParamDto);

            }
        }

        return cnt;
    }

    protected GetNoticePreNextListResponseDto getNoticePreNextList(GetNoticePreNextListRequestDto dto) throws Exception {
        GetNoticePreNextListResponseDto result = new GetNoticePreNextListResponseDto();

        List<GetNoticePreNextListResultVo> rows = companyNoticeMapper.getNoticePreNextList(dto);

        result.setRows(rows);

        return result;
    }

    protected int delAttc(NoticeAttachParamDto noticeAttachParamDto) throws Exception {

        int cnt = companyNoticeMapper.delNoticeAttach(noticeAttachParamDto);

        return cnt;
    }

    protected ApiResult<?> getNoticePopupList() throws Exception {

        GetNoticePopupListResponseDto getNoticePopupListResponseDto = new GetNoticePopupListResponseDto();

        //세션에서 UserVo 받기
        UserVo userVo = SessionUtil.getBosUserVO();
        String userId = userVo.getUserId();

        //세션에서 UserVo 안 userId의 정보 중 COMP_TP(회사구분코드) 가져오기
        //최근 접속 로그인 정보
        RecentlyLoginResultVo recentlyLoginResultVo = companyNoticeMapper.getNoticeLoginData(userId);

        //공지사항 정보
        List<GetNoticePopupListResultVo> getNoticePopupListResultVo = companyNoticeMapper.getNoticePopupList(recentlyLoginResultVo);
        getNoticePopupListResponseDto.setGetNoticePopupListResultVo(getNoticePopupListResultVo);

        return ApiResult.success(getNoticePopupListResponseDto);
    }

    protected ApiResult<?> getNoticePopup(int csCompanyBbsId){

        GetNoticePopupResponseDto getNoticePopupResponseDto = new GetNoticePopupResponseDto();

        //상세내용
        GetNoticePopupResultVo getNoticePopupResultVo = companyNoticeMapper.getNoticePopup(csCompanyBbsId);
        getNoticePopupResponseDto.setGetNoticePopupResultVo(getNoticePopupResultVo);

        return ApiResult.success(getNoticePopupResponseDto);
    }
}
