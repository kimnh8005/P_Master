package kr.co.pulmuone.v1.policy.shoppingsetting.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.*;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.vo.GetPolicyShopSettingListResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class PolicyShopSettingServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    PolicyShopSettingService policyShopSettingService;

    @Test
    void 상점별세부정책리스트조회() {
        //given
        GetPolicyShopSettingListRequestDto getPolicyShopSettingListRequestDto = new GetPolicyShopSettingListRequestDto();
        getPolicyShopSettingListRequestDto.setStShopId("1");

        //when
        List<GetPolicyShopSettingListResultVo> rows = policyShopSettingService.getPolicyShopSettingList(getPolicyShopSettingListRequestDto);

        //then
        assertTrue(CollectionUtils.isNotEmpty(rows));
    }

    @Test
    void 상점별세부사항수정성공() {

        PutPolicyShopSettingRequestDto putPolicyShopSettingRequestDto = new PutPolicyShopSettingRequestDto();

        PutPolicyShopSettingRequestSaveDto putPolicyShopSettingRequestSaveDto = PutPolicyShopSettingRequestSaveDto.builder()
                .psKey("ST_F_TITLE")
                .psValue("test123123")
                .build();

        List<PutPolicyShopSettingRequestSaveDto> updateRequestDtoList = putPolicyShopSettingRequestDto.getUpdateRequestDtoList();

        updateRequestDtoList.add(putPolicyShopSettingRequestSaveDto);
        int count = policyShopSettingService.putPolicyShopSetting(updateRequestDtoList);
        assertTrue(count > 0);

    }

    @Test
    void 상점별세부사항저장실패() {

        PutPolicyShopSettingRequestDto putPolicyShopSettingRequestDto = new PutPolicyShopSettingRequestDto();

        PutPolicyShopSettingRequestSaveDto putPolicyShopSettingRequestSaveDto = PutPolicyShopSettingRequestSaveDto.builder()
                .psKey("ST_F_TIT")
                .psValue("")
                .build();

        List<PutPolicyShopSettingRequestSaveDto> updateRequestDtoList = putPolicyShopSettingRequestDto.getUpdateRequestDtoList();
        updateRequestDtoList.add(putPolicyShopSettingRequestSaveDto);

        int count = policyShopSettingService.putPolicyShopSetting(updateRequestDtoList);
        assertEquals(0, count);
    }

    @Test
    void 파비콘저장성공() {

        PutPolicyShopSettingRequestDto putPolicyShopSettingRequestDto = new PutPolicyShopSettingRequestDto();

        FileVo fileVo = new FileVo();

        fileVo.setFieldName("file");
        fileVo.setOriginalFileName("");
        fileVo.setContentType("");
        fileVo.setFileSize(123456);
        fileVo.setFileExt("");

        GetPolicyShopSettingFileUploadRequestDto FileUploadDtoList = GetPolicyShopSettingFileUploadRequestDto.builder()
                    .psKey("FAVICON_IMAGE_FILE_PATH")
                    .psValue("fileVo.getServerSubPath()")
                    .build();

            policyShopSettingService.putPolicyShopSettingFavicon(FileUploadDtoList);

            FileUploadDtoList = GetPolicyShopSettingFileUploadRequestDto.builder()
                    .psKey("FAVICON_IMAGE_FILE_NAME")
                    .psValue("fileVo.getPhysicalFileName()")
                    .build();

            policyShopSettingService.putPolicyShopSettingFavicon(FileUploadDtoList);

            FileUploadDtoList = GetPolicyShopSettingFileUploadRequestDto.builder()
                    .psKey("FAVICON_IMAGE_FILE_ORIG_NAME")
                    .psValue("fileVo.getOriginalFileName()")
                    .build();

        int count =  policyShopSettingService.putPolicyShopSettingFavicon(FileUploadDtoList);

        assertTrue(count > 0);
    }

    @Test
    void 파비콘저장실패() {

        FileVo fileVo = new FileVo();

        fileVo.setFieldName("");
        fileVo.setOriginalFileName("");
        fileVo.setContentType("");
        fileVo.setFileSize(123456);
        fileVo.setFileExt("");

        GetPolicyShopSettingFileUploadRequestDto FileUploadDtoList = GetPolicyShopSettingFileUploadRequestDto.builder()
                .psKey("FAVICON_IMAGE_FILE_PAT")
                .psValue("fileVo.getServerSubPath()")
                .build();

        policyShopSettingService.putPolicyShopSettingFavicon(FileUploadDtoList);

        FileUploadDtoList = GetPolicyShopSettingFileUploadRequestDto.builder()
                .psKey("FAVICON_IMAGE_FILE_NAM")
                .psValue("fileVo.getPhysicalFileName()")
                .build();

        policyShopSettingService.putPolicyShopSettingFavicon(FileUploadDtoList);

        FileUploadDtoList = GetPolicyShopSettingFileUploadRequestDto.builder()
                .psKey("FAVICON_IMAGE_FILE_ORIG_NME")
                .psValue("fileVo.getOriginalFileName()")
                .build();

        int count =  policyShopSettingService.putPolicyShopSettingFavicon(FileUploadDtoList);

        assertFalse(count > 0);
    }

}