package kr.co.pulmuone.batch.cj.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class MultiValueMapConverter {
    public static MultiValueMap<String, String> convert(ObjectMapper objectMapper, Object vo) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            Map<String, String> map = objectMapper.convertValue(vo, new TypeReference<Map<String, String>>() {
            });
            params.setAll(map);

            return params;
        } catch (Exception e) {
            log.error("URI Parameter 변환 중 오류가 발생했습니다. param={}", vo, e);
            throw new IllegalStateException("URI Parameter 변환 중 오류가 발생했습니다.");
        }
    }
}
