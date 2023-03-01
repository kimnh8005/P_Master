package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 컨텐츠 상세 Dto")
public class GoodsDailyMealContentsDto {
	/** 식단 컨텐츠 코드 */
	private String ilGoodsDailyMealContsCd;
	/** 식단명 */
	private String mealName;
	/** 식단 분류 (MALL_DIV.BABYMEAL, MALL_DIV.EATSLIM) */
	private String mallDiv;
	/** 알러지 유발유무 */
	private String allergyYn;
	/** 썸네일 이미지 */
	private String thumbnailImg;
	/** 1회 제공량 */
	private int totalCapacity;
	/** 칼로리 */
	private int calorie;
	/** 권장연령 */
	private String recommendedAge;
	/** 잇슬림 지수 */
	private String eatsslimIndex;
	/** 기본설명 */
	private String description;
	/** 주요재료정보 */
	private String material;
	/** 상세 이미지 */
	private String detlImg;
	/** 난류 알러지유무 */
	private String allergyEgg;
	/** 우유 알러지유무 */
	private String allergyMilk;
	/** 새우 알러지유무 */
	private String allergyShrimp;
	/** 고등어 알러지유무 */
	private String allergyMackerel;
	/** 오징어 알러지유무 */
	private String allergySquid;
	/** 게 알러지유무 */
	private String allergyCrab;
	/** 조개류 알러지유무 */
	private String allergyShellfish;
	/** 돼지고기 알러지유무 */
	private String allergyPork;
	/** 쇠고기 알러지유무 */
	private String allergyBeef;
	/** 닭고기 알러지유무 */
	private String allergyChicken;
	/** 메밀 알러지유무 */
	private String allergyBuckwheat;
	/** 밀 알러지유무 */
	private String allergyWheat;
	/** 대두 알러지유무 */
	private String allergySoybean;
	/** 땅콩 알러지유무 */
	private String allergyPeanut;
	/** 호두 알러지유무 */
	private String allergyWalnut;
	/** 잣 알러지유무 */
	private String allergyPinenut;
	/** 아황산류 알러지유무 */
	private String allergySulfite;
	/** 복숭아 알러지유무 */
	private String allergyPeach;
	/** 토마토 알러지유무 */
	private String allergyTomato;
	/** 탄수화물 */
	private String nutritionTotalCarbohydrate;
	/** 식이섬유 */
	private String nutritionFiber;
	/** 당류 */
	private String nutritionSugars;
	/** 지방 */
	private String nutritionTotalFat;
	/** 포화지방 */
	private String nutritionSaturatedFat;
	/** 트랜스지방 */
	private String nutritionTransFat;
	/** 단백질 */
	private String nutritionProtein;
	/** 콜레스테롤 */
	private String nutritionCholesterol;
	/** 나트륨 */
	private String nutritionSodium;
	/** 탄수화물 1일 기준치 비율(%) */
	private String nutritionTotalCarbohydrateRate;
	/** 식이섬유 1일 기준치 비율(%) */
	private String nutritionFiberRate;
	/** 당류 1일 기준치 비율(%) */
	private String nutritionSugarsRate;
	/** 지방 1일 기준치 비율(%) */
	private String nutritionTotalFatRate;
	/** 포화지방 1일 기준치 비율(%) */
	private String nutritionSaturatedFatRate;
	/** 트랜스지방 1일 기준치 비율(%) */
	private String nutritionTransFatRate;
	/** 단백질 1일 기준치 비율(%) */
	private String nutritionProteinRate;
	/** 콜레스테롤 1일 기준치 비율(%) */
	private String nutritionCholesterolRate;
	/** 나트륨 1일 기준치 비율(%) */
	private String nutritionSodiumRate;
	/** 상품정보제공고시 제품명 */
	private String specGoodsName;
	/** 상품정보제공고시 식품유형 */
	private String specGoodsType;
	/** 상품정보제공고시 생산자및소재지 */
	private String specProducerLocation;
	/** 상품정보제공고시 제조일자 */
	private String specManufacturingDate;
	/** 상품정보제공고시 유통기한 */
	private String specExpirationDate;
	/** 상품정보제공고시 보관방법 */
	private String specStorageMethod;
	/** 상품정보제공고시 원재료 및 함량 */
	private String specOriginalMaterial;
	/** 식단컨텐츠 아이콘 리스트 */
	private List<FooditemIconVo> fooditemIconList;
}
