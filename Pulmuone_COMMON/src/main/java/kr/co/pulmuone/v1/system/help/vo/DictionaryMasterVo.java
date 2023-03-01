package kr.co.pulmuone.v1.system.help.vo;

import java.time.LocalDateTime;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class DictionaryMasterVo {

    // 표준용어 id
    private Long id;
    // 표준용어
    private String baseName;
    // 표준용어 type
    private DictionaryTypes dictionaryType;

    private LocalDateTime createDt;
    private String createId;
    private LocalDateTime modifyDt;
    private String modifyId;

    @Builder
    public DictionaryMasterVo(Long id, String baseName,
        DictionaryTypes dictionaryType) {
        this.id = id;
        this.baseName = baseName;
        this.dictionaryType = dictionaryType;
        this.createDt = LocalDateTime.now();
        this.createId = getUserId();
    }

    private String getUserId() {
    	UserVo vo = SessionUtil.getBosUserVO();
        if (vo == null) {
            return null;
        }
        return vo.getUserId();
    }

    public void modifyBaseName(String baseName) {
        this.baseName = baseName;
        this.modifyId = getUserId();
    }

    public boolean isEqualsBaseName(String baseName) {
        return this.baseName.equals(baseName);
    }
}
