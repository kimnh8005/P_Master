package kr.co.pulmuone.v1.comm.api.dto.basic;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.text.DecimalFormat;

@Getter
@ToString
public class ErpIfNutritionSearchResponseDto extends BaseResponseDto {

    /*
     * ERP API 품목 영양 정보 조회 dto
     */

    @JsonAlias({ "srcSvr" })
    private String soureServer; // 입력 시스템(예:ERP, ORGA …)

    @JsonAlias({ "itmNo" })
    private String erpItemNo; // ERP 품목코드

    @JsonAlias({ "srvSizPgs" })
    private String erpNutritionQuantityPerOnceWithUnit; // 1회 제공량 PGS (단위 포함)

    @JsonAlias({ "srvConPgs" })
    private String erpNutritionQuantityTotalWithUnit; // 총 제공량 PGS (단위 포함)

    @JsonAlias({ "srvSiz" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double erpNutritionQuantityPerOnce; // 1회 제공량

    @JsonAlias({ "srvCon" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double erpNutritionQuantityTotal; // 총 제공량

    @JsonAlias({ "cal" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double calories; // 열량 ( 단위 : kcal )

    @JsonAlias({ "carRat" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double carbohydrate; // 탄수화물 ( 단위 : g )

    @JsonAlias({ "carPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double carbohydratePercent; // 탄수화물 ( % )

    @JsonAlias({ "dieFib" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double dietaryFiber; // 식이섬유 ( 단위 : g )

    @JsonAlias({ "dieFibPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double dietaryFiberPercent; // 식이섬유 ( % )

    @JsonAlias({ "sug" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double sugars; // 당류 ( 단위 : g )

    @JsonAlias({ "pro" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double protein; // 단백질 ( 단위 : g )

    @JsonAlias({ "proPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double proteinPercent; // 단백질 ( % )

    @JsonAlias({ "fat" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double fat; // 지방 ( 단위 : g )

    @JsonAlias({ "fatPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double fatPercent; // 지방 ( % )

    @JsonAlias({ "satFat" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double saturatedFat; // 포화지방산 ( 단위 : g )

    @JsonAlias({ "satFatPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double saturatedFatPercent; // 포화지방산 ( % )

    @JsonAlias({ "trnFat" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double transFat; // 트랜스지방산 ( 단위 : g )

    @JsonAlias({ "cho" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double cholesterol; // 콜레스테롤 ( 단위 : mg )

    @JsonAlias({ "choPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double cholesterolPercent; // 콜레스테롤 ( % )

    @JsonAlias({ "sod" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double sodium; // 나트륨 ( 단위 : mg )

    @JsonAlias({ "sodPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double sodiumPercent; // 나트륨 ( % )

    @JsonAlias({ "vitA" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double vitaminA; // 비타민A ( 단위 : R.E. )

    @JsonAlias({ "vitAPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double vitaminAPercent; // 비타민A ( % )

    @JsonAlias({ "vitC" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double vitaminC; // 비타민C (단위 : mg )

    @JsonAlias({ "vitCPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double vitaminCPercent; // 비타민C ( % )

    @JsonAlias({ "calc" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double calcium; // 칼슘 (단위 : mg )

    @JsonAlias({ "calcPer" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double calciumPercent; // 칼슘 ( % )

    @JsonAlias({ "iro" })
    @JsonSerialize(using = ErpIfNutritionSearchResponseDto.DoubleSerializer.class)
    private Double iron; // 철 ( 단위 : mg )

    @JsonAlias({ "iroPer" })
    @JsonSerialize(using = DoubleSerializer.class)
    private Double ironPercent; // 철 ( % )

    @JsonAlias({ "nut" })
    private String erpNutritionEtc; // 기타정보

    @JsonAlias({ "updFlg" })
    private Boolean updateFlag; // 정보 업데이트 여부(Y / N)

    @JsonAlias({ "updDat" })
    private String updateDate; // 정보 업데이트 일시

    @JsonAlias({ "itfFlg" })
    private Boolean interfaceFlag; // I/F 수신 여부 (Y / N)

    /*
     * ERP API 수신 데이터 외 정보
     */

    // ERP 영양분석단위 (1회 분량) : 마스터 품목 등록 화면에 출력되고 BOS 상에 저장되는 값
    @JsonProperty("erpServingsize")
    public String getServingsize() {

        if (this.erpNutritionQuantityPerOnceWithUnit != null) { // 1회 제공량 (String, 단위 포함) 값 조회시

            return this.erpNutritionQuantityPerOnceWithUnit;

        } else if (this.erpNutritionQuantityPerOnce != null) { // 1회 제공량 (Double 타입) 값 조회시

            return String.valueOf(this.erpNutritionQuantityPerOnce);

        } else {

            return ""; // 값 미조회시 "" 반환

        }

    }

    // ERP 영양분석단위 (총분량) : 마스터 품목 등록 화면에 출력되고 BOS 상에 저장되는 값
    @JsonProperty("erpServingContainer")
    public String getServingContainer() {

        if (this.erpNutritionQuantityTotalWithUnit != null) { // 총 제공량 (String, 단위 포함) 값 조회시

            return this.erpNutritionQuantityTotalWithUnit;

        } else if (this.erpNutritionQuantityTotal != null) { // 총 제공량 (Double 타입) 값 조회시

            return String.valueOf(this.erpNutritionQuantityTotal);

        } else {

            return ""; // 값 미조회시 "" 반환

        }

    }

    /*
     * 현재 클래스에서 사용할 Double Type Serializer
     *
     * => DecimalFormat 으로 소수점 넷째 자리에서 반올림 ( 셋째자리까지 표현 ), 소수점 마지막 0 은 출력값에서 제외
     *
     */
    private static class DoubleSerializer extends JsonSerializer<Double> {

        private DecimalFormat df = new DecimalFormat("#.###"); // DecimalFormat : 소수점 이하 셋째자리까지 표현

        @Override
        public void serialize(Double originalValue, JsonGenerator jgen, SerializerProvider provider) throws IOException {

            jgen.writeString(df.format(originalValue));

        }
    }

}
