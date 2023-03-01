/**-----------------------------------------------------------------------------
 * description 		 : 식단컨텐츠 등록/수정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.07		안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var paramData ;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	var publicStorageUrl = null;  // image url path

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'mealContsMgm',
			callback : fnUI
		});

	}


	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnInitEventBind();

		fnSearch();

		fnInitPublicStorageUrl();  // get public storage url ---------------------------------

	}




	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSave, #fnClose, #fnDel').kendoButton();
	}


	function fnSave(){
		var url  = '/admin/il/meal/addMealConts';
		var cbId = 'insert';

		if (OPER_TP_CODE == 'U') {
			url = '/admin/il/meal/putMealConts';
			cbId = 'update';
		}

		var data = $('#inputForm').formSerialize(true);

		var fooditemIconList = new Array();
		$("tbody#iconSelectTbody tr").each(function(i){
			var fooditemIconVo = new Object();
			fooditemIconVo.ilFooditemIconId = $(this).attr('data-id');
			fooditemIconList.push(fooditemIconVo);
		});
		data.fooditemIconList = fooditemIconList;

		// 섬네일 이미지 체크
		if($("#basicImage").attr('src') == '/contents/images/noimg.png'){
			fnKendoMessage({message : "섬네일 이미지는 필수입니다."});
			return false;
		}

		let convertData = fnConvertFormData(data);

		if(data.rtnValid){
			fnAjaxSubmit({
				form    : 'inputForm',
				fileUrl : '/fileUpload',
				url     : url,
				params  : convertData,
				storageType : "public",
				domain : "il",
				contentType: "application/json",
				success : function(data){
					fnBizCallback(cbId, data);
				},
				isAction : 'batch'
			});
		}
	}

    function fnClose(){
        parent.POP_PARAM = 'SAVE';
        parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
    }

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnSearch(){

    	if(paramData != undefined && paramData.type == 'put'){
			fnAjax({
				url : '/admin/il/meal/getMealConts',
				params : {
					ilGoodsDailyMealContsCd : paramData.ilGoodsDailyMealContsCd
				},
				success : function(data) {

					$('#inputForm').bindingForm({ data : data }, "data");

					// 식단분류
					$("#mallDiv").data("kendoDropDownList").enable(false);
					
					// 베이비밀
					if($("#mallDiv").val() == 'MALL_DIV.BABYMEAL'){
						fnSetDisabledForAllergyCheckbox(false);
						if(data.allergyYn == 'Y') $("#allergyCheckbox").prop("checked",true);
						
						// 알러지 유발성분 유무 정보
						if(data.allergyEgg == 'Y') $("#allergyEgg").prop("checked",true);
						if(data.allergyMilk == 'Y') $("#allergyMilk").prop("checked",true);
						if(data.allergyShrimp == 'Y') $("#allergyShrimp").prop("checked",true);
						if(data.allergyMackerel == 'Y') $("#allergyMackerel").prop("checked",true);
						if(data.allergySquid == 'Y') $("#allergySquid").prop("checked",true);
						if(data.allergyCrab == 'Y') $("#allergyCrab").prop("checked",true);
						if(data.allergyShellfish == 'Y') $("#allergyShellfish").prop("checked",true);
						if(data.allergyPork == 'Y') $("#allergyPork").prop("checked",true);
						if(data.allergyBeef == 'Y') $("#allergyBeef").prop("checked",true);
						if(data.allergyChicken == 'Y') $("#allergyChicken").prop("checked",true);
						if(data.allergyBuckwheat == 'Y') $("#allergyBuckwheat").prop("checked",true);
						if(data.allergyWheat == 'Y') $("#allergyWheat").prop("checked",true);
						if(data.allergySoybean == 'Y') $("#allergySoybean").prop("checked",true);
						if(data.allergyPeanut == 'Y') $("#allergyPeanut").prop("checked",true);
						if(data.allergyWalnut == 'Y') $("#allergyWalnut").prop("checked",true);
						if(data.allergyPinenut == 'Y') $("#allergyPinenut").prop("checked",true);
						if(data.allergySulfite == 'Y') $("#allergySulfite").prop("checked",true);
						if(data.allergyPeach == 'Y') $("#allergyPeach").prop("checked",true);
						if(data.allergyTomato == 'Y') $("#allergyTomato").prop("checked",true);
						
						// 잇슬림
					}else if($("#mallDiv").val() == 'MALL_DIV.EATSLIM'){
						fnSetDisabledForAllergyCheckbox(true);
					}
					// 등록일/최근수정일
					let modifyInfo = data.modifyDt != '' ? ' /'+"<BR>" + data.modifyDt + ' (' +data.modifyInfo+ ')' : '';
					let createInfo = data.createDt +' ('+data.createInfo+')' + modifyInfo;
					$('#createInfo').html(createInfo);
					$("#ilGoodsDailyMealContsCd").prop("disabled",true);

					// 섬네일 이미지
					if(data.thumbnailImg != ''){
						$("#basicImage").attr("src",fnGetPublicStorageUrl() + data.thumbnailImg);
					}

					// 식단 아이콘 html 등록
					if(data.fooditemIconList && data.fooditemIconList.length){
						data.fooditemIconList.forEach(function(fooditemIconVo){
							fnAddIconHtml(fooditemIconVo);
						});
					}
				},
				isAction : 'select'
			});
		}
	}




	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
    		
		// 식단분류
        var mallDropDownList = fnKendoDropDownList({
            id : "mallDiv",
            tagId : "mallDiv",
            data : [ 
                     { "CODE" : "MALL_DIV.BABYMEAL", "NAME" : "베이비밀" },
                     { "CODE" : "MALL_DIV.EATSLIM", "NAME" : "잇슬림" }
                   ],
			blank : "선택"
        });

		mallDropDownList.bind('change', function(e) {
			let mallDiv = $("#mallDiv").val();
			if(mallDiv == 'MALL_DIV.BABYMEAL'){
				fnSetDisabledForAllergyCheckbox(false);
			}else{
				fnSetDisabledForAllergyCheckbox(true);
			}
		});

		// 이미지 파일첨부
		fnKendoUpload({
			id     : "imageFile",
			select : function(e){
				let f = e.files[0];
				let ext = f.extension.substring(1, f.extension.length);

				if($.inArray(ext.toLowerCase(), ["jpg","jpeg","png"]) == -1){
					fnKendoMessage({message : "jpg / jpeg / png 파일만 첨부가능합니다."});
					e.preventDefault();
				}else{
					if (typeof(window.FileReader) == "undefined"){
						$("#basicImage").attr("src", e.sender.element.val());
					} else {
						if(f){
							var reader = new FileReader();
							reader.readAsDataURL(f.rawFile);

							reader.onload = function (ele) {
								var img = new Image();
								img.src = ele.target.result;
								img.onload = function() {
									if(img.width < 300 || img.height < 300) {
										fnKendoMessage({message : "이미지 가로세로 최소 300px이상만 첨부가능합니다."});
										$("input[id=imageFile]").val("");
										$("#basicImage").attr("src", "/contents/images/noimg.png");
									}else{
										$("#basicImage").attr("src", ele.target.result);
										$("#imageOriginName").val(f.name);

									}
								}
							};

						}
					}
				}
			},
			localization : "파일첨부"
		});

		// 식단 분류별 아이콘 선택
		fnKendoDropDownList({
			id  : "iconSelect",
			url : "/admin/goods/fooditem/getFooditemIconDropDownList",
			params: { "searchUseYn" : "Y"},
			valueField : "ilFooditemIconId",
			textField :"fooditemIconName",
			blank : "아이콘 선택"
		});

		// 알러지 식단여부, 알러지 유발성분 유무정보 기본값 -> 비활성화
		fnSetDisabledForAllergyCheckbox(true);
	}

	// 이미지 파일첨부 버튼 클릭
	function fnImageUpload(){
		$("#imageFile").trigger("click");
	};

	function fnInitEventBind(){

		// 식단 분류별 아이콘 등록
		$('#ng-view').on("click", "#fnAdd", function(e) {
			e.preventDefault();
			let iconSelectVal = $("#iconSelect").val();
			let isOverlapIcon = false;

			if(iconSelectVal == ''){
				fnKendoMessage({ message : '아이콘을 선택해주세요.', ok      : function(){ }});
			}else{
				
				// 아이콘 중복체크
				$("tbody#iconSelectTbody tr").each(function(i){
					let rowIconId = $(this).attr('data-id');
					if(rowIconId == iconSelectVal){
						isOverlapIcon = true;
						fnKendoMessage({ message : '이미 추가되어 있습니다.', ok      : function(){ }});
						return;
					}
				});

				if(!isOverlapIcon){
					// 아이콘 추가
					fnAddIcon(iconSelectVal);
				}
			}


		});

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	function fnAddIcon(iconId){

		fnAjax({
			url : '/admin/goods/fooditem/getFooditemIcon',
			method  : 'GET',
			params : { ilFooditemIconId : iconId },
			success : function(data) {
				
				// 식단 아이콘 html 등록
				fnAddIconHtml(data.fooditemIconVo)

			},
			isAction : 'select'
		});

	}

	function fnAddIconHtml(fooditemIconVo){
		let defaultDesc = fooditemIconVo.defaultDesc.replaceAll('\n','<BR>');

		// 아이콘 조회 후 데이터 바인딩
		let html = document.querySelector("#iconTemplate").innerHTML;
		let resultHtml = '';

		resultHtml += html.replaceAll("{iconId}", fooditemIconVo.ilFooditemIconId)
			.replaceAll("{iconImage}",publicStorageUrl + fooditemIconVo.imagePath + fooditemIconVo.imageName)
			.replaceAll("{iconTitle}",fooditemIconVo.titleNm)
			.replaceAll("{iconDesc}",defaultDesc)
		;

		document.querySelector("#iconSelectTbody").innerHTML += resultHtml;
	}

	function fnDel(iconId){
		$("#iconId_"+iconId).remove();
	}


	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'insert':
                fnKendoMessage({
                        message : '저장되었습니다.',
                        ok      : function(){ fnClose();}
                    });

                    break;

			case 'update':
				fnKendoMessage({
						message:"저장되었습니다.",
						ok:function(){
							fnSearch();
							fnClose();
						}
				});
			break;

		}
	}


	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	$scope.fnDel = function(iconId){  fnDel(iconId);};
	$scope.fnImageUpload =function(){ fnImageUpload(); }; // 이미지 파일첨부 버튼


	//입력제한 - 숫자
	fnInputValidationByRegexp("ilGoodsDailyMealContsCd", /[^0-9]/g);
	fnInputValidationByRegexp("totalCapacity", /[^0-9]/g);
	fnInputValidationByRegexp("calorie", /[^0-9]/g);
	fnInputValidationByRegexp("recommendedAge", /[^ㄱ-ㅎㅏ-ㅣ가-힣0-9,~]/g);
	fnInputValidationByRegexp("eatsslimIndex", /[^0-9]/g);
	fnInputDecimalValidationByRegexp("nutritionTotalCarbohydrate", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionFiber", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionSugars", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionTotalFat", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionSaturatedFat", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionTransFat", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionProtein", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionCholesterol", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionSodium", /[^0-9.]/g);

	fnInputDecimalValidationByRegexp("nutritionTotalCarbohydrateRate", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionFiberRate", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionSugarsRate", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionTotalFatRate", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionSaturatedFatRate", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionTransFatRate", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionProteinRate", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionCholesterolRate", /[^0-9.]/g);
	fnInputDecimalValidationByRegexp("nutritionSodiumRate", /[^0-9.]/g);

	function fnNvlForNumber( argv1 ){
		if( argv1 != null && argv1 != "" && argv1 != undefined ){
			return argv1;
		}else{
			return 0;
		}
	}

	function fnConvertCheckBox( argv1 ){
		if( argv1 != null && argv1 != "" && argv1 != undefined && argv1 == 'on'){
			return "Y";
		}else{
			return "N";
		}
	}

	// 마지막 입력값 소수점 입력불가
	function fnCheckDecimalPoint(argv1){
		var rexexp = new RegExp(/\.$/);
		if(rexexp.test(argv1)){
			return argv1.slice(0,-1);
		}

		return argv1;
	}

	function fnConvertFormData(data){

		data.allergyYn 					= fnConvertCheckBox(data.allergyCheckbox);
		data.totalCapacity 				= fnNvlForNumber(data.totalCapacity);
		data.calorie 					= fnNvlForNumber(data.calorie);
		data.allergyEgg 				= fnConvertCheckBox(data.allergyEgg);
		data.allergyMilk 				= fnConvertCheckBox(data.allergyMilk);
		data.allergyShrimp 				= fnConvertCheckBox(data.allergyShrimp);
		data.allergyMackerel 			= fnConvertCheckBox(data.allergyMackerel);
		data.allergySquid 				= fnConvertCheckBox(data.allergySquid);
		data.allergyCrab 				= fnConvertCheckBox(data.allergyCrab);
		data.allergyShellfish 			= fnConvertCheckBox(data.allergyShellfish);
		data.allergyPork 				= fnConvertCheckBox(data.allergyPork);
		data.allergyBeef 				= fnConvertCheckBox(data.allergyBeef);
		data.allergyChicken 			= fnConvertCheckBox(data.allergyChicken);
		data.allergyBuckwheat 			= fnConvertCheckBox(data.allergyBuckwheat);
		data.allergyWheat 				= fnConvertCheckBox(data.allergyWheat);
		data.allergySoybean 			= fnConvertCheckBox(data.allergySoybean);
		data.allergyPeanut 				= fnConvertCheckBox(data.allergyPeanut);
		data.allergyWalnut 				= fnConvertCheckBox(data.allergyWalnut);
		data.allergyPinenut 			= fnConvertCheckBox(data.allergyPinenut);
		data.allergySulfite 			= fnConvertCheckBox(data.allergySulfite);
		data.allergyPeach 				= fnConvertCheckBox(data.allergyPeach);
		data.allergyTomato 				= fnConvertCheckBox(data.allergyTomato);
		data.nutritionTotalCarbohydrate = fnCheckDecimalPoint(data.nutritionTotalCarbohydrate);
		data.nutritionFiber 			= fnCheckDecimalPoint(data.nutritionFiber);
		data.nutritionSugars 			= fnCheckDecimalPoint(data.nutritionSugars);
		data.nutritionTotalFat 			= fnCheckDecimalPoint(data.nutritionTotalFat);
		data.nutritionSaturatedFat 		= fnCheckDecimalPoint(data.nutritionSaturatedFat);
		data.nutritionTransFat 			= fnCheckDecimalPoint(data.nutritionTransFat);
		data.nutritionProtein 			= fnCheckDecimalPoint(data.nutritionProtein);
		data.nutritionCholesterol 		= fnCheckDecimalPoint(data.nutritionCholesterol);
		data.nutritionSodium 			= fnCheckDecimalPoint(data.nutritionSodium);
		data.nutritionTotalCarbohydrateRate = fnCheckDecimalPoint(data.nutritionTotalCarbohydrateRate);
		data.nutritionFiberRate 			= fnCheckDecimalPoint(data.nutritionFiberRate);
		data.nutritionSugarsRate 			= fnCheckDecimalPoint(data.nutritionSugarsRate);
		data.nutritionTotalFatRate 			= fnCheckDecimalPoint(data.nutritionTotalFatRate);
		data.nutritionSaturatedFatRate 		= fnCheckDecimalPoint(data.nutritionSaturatedFatRate);
		data.nutritionTransFatRate 			= fnCheckDecimalPoint(data.nutritionTransFatRate);
		data.nutritionProteinRate 			= fnCheckDecimalPoint(data.nutritionProteinRate);
		data.nutritionCholesterolRate 		= fnCheckDecimalPoint(data.nutritionCholesterolRate);
		data.nutritionSodiumRate 			= fnCheckDecimalPoint(data.nutritionSodiumRate);


		return data;
	}

	function fnSetDisabledForAllergyCheckbox(isDisabled){

		$("#allergyCheckbox").prop("disabled",isDisabled);
		$("#allergyEgg").prop("disabled",isDisabled);
		$("#allergyMilk").prop("disabled",isDisabled);
		$("#allergyShrimp").prop("disabled",isDisabled);
		$("#allergyMackerel").prop("disabled",isDisabled);
		$("#allergySquid").prop("disabled",isDisabled);
		$("#allergyCrab").prop("disabled",isDisabled);
		$("#allergyShellfish").prop("disabled",isDisabled);
		$("#allergyPork").prop("disabled",isDisabled);
		$("#allergyBeef").prop("disabled",isDisabled);
		$("#allergyChicken").prop("disabled",isDisabled);
		$("#allergyBuckwheat").prop("disabled",isDisabled);
		$("#allergyWheat").prop("disabled",isDisabled);
		$("#allergySoybean").prop("disabled",isDisabled);
		$("#allergyPeanut").prop("disabled",isDisabled);
		$("#allergyWalnut").prop("disabled",isDisabled);
		$("#allergyPinenut").prop("disabled",isDisabled);
		$("#allergySulfite").prop("disabled",isDisabled);
		$("#allergyPeach").prop("disabled",isDisabled);
		$("#allergyTomato").prop("disabled",isDisabled);

		// 체크박스 비활성화일 경우 체크박스 해제
		if(isDisabled){
			$("#allergyCheckbox").prop("checked",false);
			$("#allergyEgg").prop("checked",false);
			$("#allergyMilk").prop("checked",false);
			$("#allergyShrimp").prop("checked",false);
			$("#allergyMackerel").prop("checked",false);
			$("#allergySquid").prop("checked",false);
			$("#allergyCrab").prop("checked",false);
			$("#allergyShellfish").prop("checked",false);
			$("#allergyPork").prop("checked",false);
			$("#allergyBeef").prop("checked",false);
			$("#allergyChicken").prop("checked",false);
			$("#allergyBuckwheat").prop("checked",false);
			$("#allergyWheat").prop("checked",false);
			$("#allergySoybean").prop("checked",false);
			$("#allergyPeanut").prop("checked",false);
			$("#allergyWalnut").prop("checked",false);
			$("#allergyPinenut").prop("checked",false);
			$("#allergySulfite").prop("checked",false);
			$("#allergyPeach").prop("checked",false);
			$("#allergyTomato").prop("checked",false);
		}
	}

	// 이미지 URL 조회
	function fnInitPublicStorageUrl() {
		publicStorageUrl = fnGetPublicStorageUrl();
	};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
