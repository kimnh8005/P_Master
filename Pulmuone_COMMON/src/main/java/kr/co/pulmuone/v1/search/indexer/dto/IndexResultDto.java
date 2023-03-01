package kr.co.pulmuone.v1.search.indexer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class IndexResultDto {
    /**
     * 색인 document 건 수
     */
    private int count;

    /**
     * 색인 생성 상태 코드
     */
    private int indexStatusCode;

    /**
     *  색인 별칭 스위칭 여부
     */
    private boolean switching;

    /**
     * 색인 과정 전체 성공 여부
     */
    private boolean success = false;


    @Builder
    public IndexResultDto(int count, int indexStatusCode, boolean wasAliasSwitched){
        this.count = count;
        this.indexStatusCode = indexStatusCode;
        this.switching = wasAliasSwitched;
        this.success = indexStatusCode == 200 && wasAliasSwitched;
    }
}
