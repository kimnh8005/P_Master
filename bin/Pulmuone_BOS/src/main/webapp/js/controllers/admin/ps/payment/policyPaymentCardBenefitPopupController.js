/**-----------------------------------------------------------------------------
 * system            : 신용카드 혜택안내 미리보기 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.12     박승현          최초생성
 * @
 * **/
"use strict";

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var viewModel; // viewModel

$(document).ready(function() {

    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "policyPaymentCardBenefitPopup",
            callback : fnUI
        });
    };

    function fnUI(){

	   	fnDefaultValue();
		fnInitButton();	//Initialize Button  ---------------------------------
		fnInitOptionBox();//Initialize Option Box ------------------------------------
	}

    function fnDefaultValue(){
		fnAjax({
			url     : '/admin/policy/payment/getPolicyPaymentCardBenefitInfo',
			params  : {psCardBenefitId : paramData.psCardBenefitId},
			method : "GET",
			success :
				function( data ){
					$("#psCardBenefitId").val(data.psCardBenefitId);
					$("#pgName").html(data.pgName);
					$("#cardBenefitTpName").html(data.cardBenefitTpName);
					$("#title").html(data.title);
					$("#information").html(fnTagConvert(data.information));
					var benefitStatus = "";
					if( data.benefitStatus == "1" ){
						benefitStatus = "진행중";
                    }else if( data.benefitStatus == "2" ){
                    	benefitStatus = "진행예정";
                    }else{
                    	benefitStatus = "종료";
                    }
					$("#onBenefit").html(data.startDt + " ~ " + data.endDt + " (" + benefitStatus + ")");
					$('#fnDel').kendoButton().data("kendoButton").enable(true);
				},
			isAction : 'select'
		});
	}

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnMod").kendoButton();
        $('#fnDel').kendoButton({ enable: false });
    };

    function fnMod(){
    	parent.$scope.fnMgm(paramData.psCardBenefitId);
	}

    // 팝업 닫기
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	};

    function fnDel() {
    	fnKendoMessage({message:"삭제하시겠습니까?", type : "confirm"
    		,ok : function(){
    			var url  = "/admin/policy/payment/delPolicyPaymentCardBenefit";
    			var data = $('#inputForm').formSerialize(true);
    			data['psCardBenefitId'] = $("#psCardBenefitId").val();
				fnAjax({
					url     : url,
					params  : data,
					success :
						function(result){
							fnKendoMessage({  message : "삭제되었습니다."
								, ok : function(){
									parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
								}
							});
                          },
                      isAction : 'update'
                  });
            }
    	});
    }

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){
    };

    //---------------Initialize Option Box End ------------------------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

	/** Common Mod */
    $scope.fnMod = function( ){  fnMod();};
    /** Common Delete*/
    $scope.fnDel = function(){	 fnDel();};

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
