package kr.co.pulmuone.v1.policy.shoppingsetting.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.*;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.vo.GetPolicyShopSettingListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyShopSettingBizImpl implements PolicyShopSettingBiz {

    @Autowired
    PolicyShopSettingService policyShopSettingService;

    /**
     * @Desc 상점별 세부정책 리스트 조회
     * @param getPolicyShopSettingListRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getPolicyShopSettingList(GetPolicyShopSettingListRequestDto getPolicyShopSettingListRequestDto) {
        GetPolicyShopSettingListResponseDto result = new GetPolicyShopSettingListResponseDto();

        List<GetPolicyShopSettingListResultVo> rows = policyShopSettingService.getPolicyShopSettingList(getPolicyShopSettingListRequestDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }

    /**
     * @Desc 상점별 세부정책 저장
     * @param putPolicyShopSettingRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> putPolicyShopSetting(PutPolicyShopSettingRequestDto putPolicyShopSettingRequestDto) {

        PutPolicyShopSettingResponseDto result = new PutPolicyShopSettingResponseDto();

        List<PutPolicyShopSettingRequestSaveDto> updateRequestDtoList = putPolicyShopSettingRequestDto.getUpdateRequestDtoList();

        for (FileVo fileVo : putPolicyShopSettingRequestDto.getAddFileList()) {
        	PutPolicyShopSettingRequestSaveDto FileUploadDtoList = PutPolicyShopSettingRequestSaveDto.builder()
                    .psKey(fileVo.getFieldName()+"_PATH")
                    .psValue(fileVo.getServerSubPath())
                    .build();
            updateRequestDtoList.add(FileUploadDtoList);

            FileUploadDtoList = PutPolicyShopSettingRequestSaveDto.builder()
                    .psKey(fileVo.getFieldName()+"_NAME")
                    .psValue(fileVo.getPhysicalFileName())
                    .build();
            updateRequestDtoList.add(FileUploadDtoList);

            FileUploadDtoList = PutPolicyShopSettingRequestSaveDto.builder()
                    .psKey(fileVo.getFieldName()+"_ORIG_NAME")
                    .psValue(fileVo.getOriginalFileName())
                    .build();
            updateRequestDtoList.add(FileUploadDtoList);

        }

      /*
        if (!fileList.isEmpty()) {
            FileVo fileVo = fileList.get(0);
            GetPolicyShopSettingFileUploadRequestDto FileUploadDtoList = GetPolicyShopSettingFileUploadRequestDto.builder()
                    .psKey("FAVICON_IMAGE_FILE_PATH")
                    .psValue(fileVo.getServerSubPath())
                    .build();

            policyShopSettingService.putPolicyShopSettingFavicon(FileUploadDtoList);

            FileUploadDtoList = GetPolicyShopSettingFileUploadRequestDto.builder()
                    .psKey("FAVICON_IMAGE_FILE_NAME")
                    .psValue(fileVo.getPhysicalFileName())
                    .build();

            policyShopSettingService.putPolicyShopSettingFavicon(FileUploadDtoList);

            FileUploadDtoList = GetPolicyShopSettingFileUploadRequestDto.builder()
                    .psKey("FAVICON_IMAGE_FILE_ORIG_NAME")
                    .psValue(fileVo.getOriginalFileName())
                    .build();

            policyShopSettingService.putPolicyShopSettingFavicon(FileUploadDtoList);
        }

*/
        //데이터 수정
        if (!updateRequestDtoList.isEmpty()) {
            policyShopSettingService.putPolicyShopSetting(updateRequestDtoList);
        }

        return ApiResult.success(result);
    }

}
