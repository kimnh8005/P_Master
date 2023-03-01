/**-----------------------------------------------------------------------------
 * description 		 : 식단 스케쥴관리 > 식단 패턴/스케쥴 상세 등록/수정 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.10		최윤지          최초생성
 * **/
"use strict";

var paramData ;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "mealInfoDetailMgmPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();
		fnDefaultSetting();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnMealPatternDetlUpdate").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------
	function fnDefaultSetting(){
		
		// 패턴별 상세 등록/수정
		if(paramData.detlMgmType == 'PATTERN') {
			$('#schDetlMgm').hide();
			$('#patternDetlMgm').show();

			$('#patternCd').text(paramData.patternCd);
			$('#patternInfo').text(paramData.mallDivNm+'/'+paramData.patternNm);
			$('#patternStartDt').text(paramData.patternStartDt);
			$('#patternEndDt').text(paramData.patternEndDt);
			$('#patternNo').val(paramData.patternNo);
			$('#setNo').val(paramData.setNo);
			$('#setCd').val(paramData.setCd);
			$('#setNm').val(paramData.setNm);
			$('#mealContsCd').val(paramData.mealContsCd);
			$('#mealContsNm').text(fnNvl(paramData.mealNm));
			$('#allergyYn').text(fnNvl(paramData.allergyYn));
			
			//필수사항
			$('#patternNo').attr('required', true);
			$('#setNo').attr('required', true);
			$('#mealContsCd').attr('required', true);

			//베이비밀 필수사항
			if(paramData.mallDiv == 'MALL_DIV.BABYMEAL'){
				$('#setCd').attr('required', true);
			}
		// 스케쥴 상세 등록/수정
		} else if(paramData.detlMgmType == 'SCH') {
			$('#patternDetlMgm').hide();
			$('#schDetlMgm').show();
			$('#deliveryDateStr').text(paramData.deliveryDateStr);
			$('#deliveryWeekCode').text(paramData.deliveryWeekCode);
			$('#mealContsCode').val(paramData.mealContsCd);
			$('#mealName').val(fnNvl(paramData.mealNm));

			if(paramData.holidayYn == "Y"){
				$("#holidayYn").prop("checked",true);
				$('#mealContsCode').prop("disabled",true);
				$('#mealName').prop("disabled",false);
			}else{
				$("#holidayYn").prop("checked",false);
				$('#mealContsCode').prop("disabled",false);
				$('#mealName').prop("disabled",true);
			}

			//필수사항
			$('#mealContsCode').attr('required', true);

		}
	}
	//---------------Initialize Option Box Start ------------------------------------------------
	$("#holidayYn").on("click",function(){
		if($("#holidayYn").prop("checked")==true) {
			$('#mealContsCode').prop("disabled",true);
			$('#mealName').prop("disabled",false);
		}else {
			$('#mealContsCode').prop("disabled",false);
			$('#mealName').prop("disabled",true);
		}
	});

	$("#bulkUpdateYn").on("click",function(){
		fnKendoMessage({ message : '일괄변경 체크 시 기존 식단품목코드 스케쥴이 수정한 식단품목으로 일괄 변경됩니다.'});
	});
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------
	// 패턴별 상세팝업 > 변경
    function fnMealPatternDetlUpdate(){
        let mealPatternDetlData = $("#inputForm").formSerialize(true);
        mealPatternDetlData.patternCd = $('#patternCd').text();
		mealPatternDetlData.patternDetlId = paramData.patternDetlId;
        //패턴상세정보 setting
        // mealPatternDetlData.patternStartDt = paramData.patternStartDt.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
        // mealPatternDetlData.patternEndDt = paramData.patternEndDt.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
		delete mealPatternDetlData.bulkUpdateYn;
        //변경된 row grid data Setting
		let updatePatternData = new Object();
		updatePatternData.patternCd   = $('#patternCd').text();
		updatePatternData.patternDetlId   = parseInt(mealPatternDetlData.patternDetlId);
		updatePatternData.patternNo   = parseInt(mealPatternDetlData.patternNo);
		updatePatternData.setNo       = parseInt(mealPatternDetlData.setNo);
		updatePatternData.setCd       = fnNvl(mealPatternDetlData.setCd);
		updatePatternData.setNm       = fnNvl(mealPatternDetlData.setNm);
		updatePatternData.mealContsCd = mealPatternDetlData.mealContsCd;
		mealPatternDetlData.updatePatternData = updatePatternData;
		paramData.updatePatternData = updatePatternData;
		paramData.patternDetlList.push(updatePatternData);
		mealPatternDetlData.patternDetlList = paramData.patternDetlList;

		if( mealPatternDetlData.rtnValid ){
			fnClose();
        }
    };
	
	// 스케쥴별 상세팝업 > 저장
    function fnMealSchDetlUpdate(){
        let mealSchData = $("#inputForm").formSerialize(true);
        mealSchData.bulkUpdateYn = mealSchData.bulkUpdateYn != 'on' ? 'N' : 'Y';
		mealSchData.schId = paramData.schId;
		mealSchData.patternCd = paramData.patternCd;
		mealSchData.originMealContsCd = paramData.mealContsCd;

        if(mealSchData.holidayYn != 'on') {
        	mealSchData.holidayYn = 'N';
        	mealSchData.mealContsCd = $('#mealContsCode').val();
        	mealSchData.holidayNm = null;
		} else {
        	mealSchData.holidayYn = 'Y';
        	mealSchData.mealContsCd = $('#mealContsCode').val();
			mealSchData.holidayNm = mealSchData.mealName;
        }

        if( mealSchData.rtnValid ){
            fnKendoMessage({message : '저장하시겠습니까?', type : "confirm", ok : function(){
				fnAjax({
					url     : '/admin/item/meal/putMealSchRow',
                    params: mealSchData,
                    contentType: "application/json",
					success :
						function( data ){
							fnKendoMessage({message:"저장되었습니다.",ok:function(){fnClose();}});
						},
                    isAction: 'batch'
				});
			}});
        }
    };

	// 팝업 닫기
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	};





	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	$scope.fnClose = function(){ fnClose(); }; // 닫기
	$scope.fnMealPatternDetlUpdate =function(){	fnMealPatternDetlUpdate(); };
	$scope.fnMealSchDetlUpdate =function(){	fnMealSchDetlUpdate(); };

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------
	fnInputValidationForNumber('patternNo');
	fnInputValidationForNumber('setNo');
	fnInputValidationForNumber('setCd');
	fnInputValidationForHangulAlphabetNumberSpace('setNm');
	fnInputValidationForNumber('mealContsCd');
	fnInputValidationForNumber('mealContsCode');
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
