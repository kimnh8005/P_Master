package kr.co.pulmuone.v1.order.schedule.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import kr.co.pulmuone.v1.comm.api.constant.SourceServerTypes;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErpIfCustordSearchResponseDto {

    /*
     * ERP API 주문|취소 정보 조회 dto
     */

    @JsonAlias({ "hrdSeq" })
    private String hrdSeq; // Header와 Line의 join key. 통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함

    @JsonAlias({ "hdrTyp" })
    private String hrdType; // Header와 Line의 join key. 통합몰에서 주문번호와 별도로 구분하기 위한 고유값으로 생성함

    @JsonAlias({ "oriSysSeq" })
    private String originalSystemSeq; // ERP 전용 key 값.온라인 order key값

    @JsonAlias({ SourceServerTypes.CODE_KEY })
    private SourceServerTypes soureServer; //입력 시스템 코드값

    @JsonAlias({ "ordNum" })
    private String orderNumber; // 통합몰 주문번호

    @JsonAlias({ "rn" })
    private Integer rowNumber; // 순번

    /* Response line */
    @JsonAlias({ "line" })
    private List<ErpIfCustordSerachLineResponseDto> line;

}
