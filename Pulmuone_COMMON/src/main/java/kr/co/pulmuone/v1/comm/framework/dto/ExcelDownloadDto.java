package kr.co.pulmuone.v1.comm.framework.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExcelDownloadDto {

    // 엑셀 파일명
    private String excelFileName;

    // 엑셀 워크시트 목록
    @Builder.Default
    private List<ExcelWorkSheetDto> excelWorkSheetList = new ArrayList<>();

    /* ---------- custom 메서드 구현 ---------- */

    public void addExcelWorkSheet(ExcelWorkSheetDto workSheetDto) {
        this.excelWorkSheetList.add(workSheetDto);
    }

}
