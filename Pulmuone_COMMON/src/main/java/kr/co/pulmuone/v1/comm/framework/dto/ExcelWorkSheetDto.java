package kr.co.pulmuone.v1.comm.framework.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExcelWorkSheetDto {

    @Builder.Default
    private ObjectMapper mapper = new ObjectMapper();;

    // 파일명
    private String excelFileName;

    // 워크시트 이름
    private String workSheetName;

    // 워크시트 상단의 각 행 별 컬럼 헤더명
    @Builder.Default
    private Map<Integer, String[]> headerCollection = new HashMap<>();

    // 워크시트 컬럼별 excelDataList 에 지정된 속성
    private String[] propertyList;

    // 워크시트 컬럼별 너비
    private Integer[] widthList;

    // 워크시트 본문 데이터 컬럼별 정렬
    private String[] alignList;

    // 워크시트 컬럼별 셀 스타일 : 현재 미사용
    private String[] cellTypeList;

    // 워크시트 데이타
    private ArrayNode excelDataList;

    // 추가정보 데이터
    private List<String> addInfoList;

    // 데이터영역 로우 병합 여부
    private boolean mergeYn;

    // 로우 병합할 컬럼 index
    private int[] mergeIndexArr;

    // 로우 병합 정보
    Map<Integer, List<Map<String, Object>>> mergeInfoMap;



    /* ---------- custom 메서드 구현 ---------- */

    public void setExcelDataList(List<?> data) {

        try {

            final JsonNode arrNode = mapper.readTree(mapper.writeValueAsString(data));

            if (arrNode.isArray()) {

                excelDataList = (ArrayNode) arrNode;

            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public int getHeaderRowCount() {

        if (this.headerCollection != null) {

            return this.headerCollection.keySet().size();

        } else {

            return 0;

        }
    }

    public String[] getHeaderList(int rowIdx) {

        if (this.headerCollection != null && rowIdx >= 0 && rowIdx < getHeaderRowCount()) {

            return this.headerCollection.get(rowIdx);

        } else {

            return new String[0];

        }

    }

    public void setHeaderList(int rowIdx, String[] header) {
        this.headerCollection.put(rowIdx, header);
    }

}
