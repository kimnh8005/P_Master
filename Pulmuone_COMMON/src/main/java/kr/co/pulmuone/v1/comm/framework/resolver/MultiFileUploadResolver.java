package kr.co.pulmuone.v1.comm.framework.resolver;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.co.pulmuone.v1.comm.framework.dto.MultiFileUploadRequestDto;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType;

@Component
public class MultiFileUploadResolver implements HandlerMethodArgumentResolver {

    private final String DOMAIN_PREFIX = StorageInfoBaseType.DOMAIN_PREFIX_KEY_NAME;
    private final String STORAGE_TYPE = StorageInfoBaseType.STORAGE_TYPE_KEY_NAME;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(MultiFileUploadRequestDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return convertMultiFileToDto(webRequest);
    }

    private MultiFileUploadRequestDto convertMultiFileToDto(NativeWebRequest webRequest) {

        MultipartHttpServletRequest multiPartRequest = webRequest.getNativeRequest(MultipartHttpServletRequest.class);

        MultiFileUploadRequestDto multiFileUploadRequestDto = MultiFileUploadRequestDto.builder().build();

        // domainPrefix : 화면에서 업로드 전송시 전달된 업무 도메인 prefix
        if (multiPartRequest.getParameterMap().get(DOMAIN_PREFIX) != null && multiPartRequest.getParameterMap().get(DOMAIN_PREFIX).length != 0) {
            multiFileUploadRequestDto.setDomainPrefix( //
                    multiPartRequest.getParameterMap().get(DOMAIN_PREFIX)[0] // domainPrefix 값
            );
        }

        // storageType : 화면에서 업로드 전송시 전달된 저장 타입
        if (multiPartRequest.getParameterMap().get(STORAGE_TYPE) != null && multiPartRequest.getParameterMap().get(STORAGE_TYPE).length != 0) {
            multiFileUploadRequestDto.setStorageType( //
                    multiPartRequest.getParameterMap().get(STORAGE_TYPE)[0] // storageType 값
            );
        }

        /*
         * multiPartRequest.getFileMap() : Map<String, MultipartFile> 타입 객체
         *
         * => Key : 업로드 태그명 : HTML <input type="file" name="fieldName"> tag 에 작성한 name 속성값
         * => Value : 화면에서 전송한 MultipartFile 객체
         */
        multiPartRequest.getFileMap().forEach((fieldName, multipartFile) -> {

            UploadFileDto uploadFileDto = UploadFileDto.builder() //
                    .fieldName(fieldName) // 업로드 태그명 : HTML <input type="file" name="fieldName"> tag 에 작성한 name 속성값
                    .originalFileName(multipartFile.getOriginalFilename()) // 원본 멀티파트 파일명
                    .contentType(multipartFile.getContentType()) // 원본 멀티파트 파일 타입
                    .fileSize(multipartFile.getSize()) // 원본 멀티파트 파일 크기
                    .fileExtension(FilenameUtils.getExtension(multipartFile.getOriginalFilename())) // 파일 확장자
                    .multipartFile(multipartFile) // MultipartFile 타입 객체
                    .build();

            multiFileUploadRequestDto.addUploadFileDto(uploadFileDto);

        });

        return multiFileUploadRequestDto;

    }

}
