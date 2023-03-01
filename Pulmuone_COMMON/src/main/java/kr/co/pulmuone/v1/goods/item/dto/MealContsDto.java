package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "식단 컨텐츠 dto")
public class MealContsDto{

    @ApiModelProperty(value = "식단컨텐츠 코드")
    private String ilGoodsDailyMealContsCd;

    @ApiModelProperty(value = "식단명")
    private String ilGoodsDailyMealNm;

    @ApiModelProperty(value = "식단분류")
    private String mallDiv;

    @ApiModelProperty(value = "알러지식단 여부")
    private String allergyYn;

    @ApiModelProperty(value = "파일정보")
    private String addFile;

    @ApiModelProperty(value = "파일 리스트")
    private List<FileVo> addFileList;

    @ApiModelProperty(value = "이미지경로")
    private String thumbnailImg;

    @ApiModelProperty(value = "1회 제공량")
    private int totalCapacity;
    
    @ApiModelProperty(value = "칼로리")
    private int calorie;

    @ApiModelProperty(value = "권장연령")
    private String recommendedAge;

    @ApiModelProperty(value = "잇슬림 지수")
    private String eatsslimIndex;

    @ApiModelProperty(value = "기본 설명")
    private String description;

    @ApiModelProperty(value = "계란 알러지유무")
    private String allergyEgg;

    @ApiModelProperty(value = "우유 알러지유무")
    private String allergyMilk;

    @ApiModelProperty(value = "새우 알러지유무")
    private String allergyShrimp;

    @ApiModelProperty(value = "고등어 알러지유무")
    private String allergyMackerel;

    @ApiModelProperty(value = "오징어 알러지유무")
    private String allergySquid;

    @ApiModelProperty(value = "게 알러지유무")
    private String allergyCrab;

    @ApiModelProperty(value = "조개류 알러지유무")
    private String allergyShellfish;

    @ApiModelProperty(value = "돼지고기 알러지유무")
    private String allergyPork;

    @ApiModelProperty(value = "쇠고기 알러지유무")
    private String allergyBeef;

    @ApiModelProperty(value = "닭고기 알러지유무")
    private String allergyChicken;

    @ApiModelProperty(value = "메밀 알러지유무")
    private String allergyBuckwheat;

    @ApiModelProperty(value = "밀 알러지유무")
    private String allergyWheat;

    @ApiModelProperty(value = "대두 알러지유무")
    private String allergySoybean;

    @ApiModelProperty(value = "땅콩 알러지유무")
    private String allergyPeanut;

    @ApiModelProperty(value = "호두 알러지유무")
    private String allergyWalnut;

    @ApiModelProperty(value = "잣 알러지유무")
    private String allergyPinenut;

    @ApiModelProperty(value = "아황산류 알러지유무")
    private String allergySulfite;

    @ApiModelProperty(value = "복숭아 알러지유무")
    private String allergyPeach;

    @ApiModelProperty(value = "토마토 알러지유무")
    private String allergyTomato;

    @ApiModelProperty(value = "탄수화물")
    private String nutritionTotalCarbohydrate;

    @ApiModelProperty(value = "식이섬유")
    private String nutritionFiber;

    @ApiModelProperty(value = "당류")
    private String nutritionSugars;

    @ApiModelProperty(value = "지방")
    private String nutritionTotalFat;

    @ApiModelProperty(value = "포화지방")
    private String nutritionSaturatedFat;

    @ApiModelProperty(value = "트랜스지방")
    private String nutritionTransFat;

    @ApiModelProperty(value = "단백질")
    private String nutritionProtein;

    @ApiModelProperty(value = "콜레스테롤")
    private String nutritionCholesterol;

    @ApiModelProperty(value = "나트륨")
    private String nutritionSodium;

    @ApiModelProperty(value = "탄수화물 1일 기준치 비율(%)")
    private String nutritionTotalCarbohydrateRate;

    @ApiModelProperty(value = "식이섬유 1일 기준치 비율(%)")
    private String nutritionFiberRate;

    @ApiModelProperty(value = "당류 1일 기준치 비율(%)")
    private String nutritionSugarsRate;

    @ApiModelProperty(value = "지방 1일 기준치 비율(%)")
    private String nutritionTotalFatRate;

    @ApiModelProperty(value = "포화지방 1일 기준치 비율(%)")
    private String nutritionSaturatedFatRate;

    @ApiModelProperty(value = "트랜스지방 1일 기준치 비율(%)")
    private String nutritionTransFatRate;

    @ApiModelProperty(value = "단백질 1일 기준치 비율(%)")
    private String nutritionProteinRate;

    @ApiModelProperty(value = "콜레스테롤 1일 기준치 비율(%)")
    private String nutritionCholesterolRate;

    @ApiModelProperty(value = "나트륨 1일 기준치 비율(%)")
    private String nutritionSodiumRate;

    @ApiModelProperty(value = "상품정보제공고시 제품명")
    private String specGoodsName;

    @ApiModelProperty(value = "상품정보제공고시 식품유형")
    private String specGoodsType;

    @ApiModelProperty(value = "상품정보제공고시 생산자 및 소재지")
    private String specProducerLocation;

    @ApiModelProperty(value = "상품정보제공고시 제조일자")
    private String specManufacturingDate;

    @ApiModelProperty(value = "상품정보제공고시 유통기한")
    private String specExpirationDate;

    @ApiModelProperty(value = "상품정보제공고시 보관방법")
    private String specStorageMethod;

    @ApiModelProperty(value = "상품정보제공고시 원재료 및 함량")
    private String specOriginalMaterial;

    @ApiModelProperty(value = "등록 시간정보")
    private String createInfo;

    @ApiModelProperty(value = "등록정보")
    private String createDt;

    @ApiModelProperty(value = "등록자")
    private String createId;

    @ApiModelProperty(value = "수정 시간정보")
    private String modifyInfo;

    @ApiModelProperty(value = "수정정보")
    private String modifyDt;

    @ApiModelProperty(value = "수정자")
    private String modifyId;

    @ApiModelProperty(value = "식단컨텐츠 일괄업로드 성공여부")
    private boolean success;

    @ApiModelProperty(value = "식단컨텐츠 일괄업로드 실패메시지")
    private String failMessage;

    @ApiModelProperty(value = "식단컨텐츠 아이콘 리스트")
    private List<FooditemIconVo> fooditemIconList;
}
