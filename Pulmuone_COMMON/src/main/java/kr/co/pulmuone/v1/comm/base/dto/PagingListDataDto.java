package kr.co.pulmuone.v1.comm.base.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PagingListDataDto<T> {

    /** 조회 데이터 목록 */
    private final List<T> rows;

    /** 페이지번호 */
    private int page;

    /** 총 페이지 개수 */
    private int pageSize;

    /** 총 레코드 개수 */
    private int total;

    public PagingListDataDto(List<T> rows) {
        this.rows = rows;
    }

    public PagingListDataDto(int pageNum, int pageTotalCnt, int recordTotalCnt, List<T> rows) {
        this.page = pageNum;
        this.pageSize = pageTotalCnt;
        this.total = recordTotalCnt;
        this.rows = rows;
    }
}
