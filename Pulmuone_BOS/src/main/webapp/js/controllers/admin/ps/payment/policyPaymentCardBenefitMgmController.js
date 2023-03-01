/**-----------------------------------------------------------------------------
 * system            : 신용카드 혜택안내 등록/수정 팝업
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
            PG_ID  : "policyPaymentCardBenefitMgm",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();	// Initialize Button  ---------------------------------
        fnInitOptionBox(); // Initialize Option Box ------------------------------------
        fnViewModelInit();
        fnDefaultValue();
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSave").kendoButton();
    };

    // 팝업 닫기
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	};

    // 저장
    function fnSave(){
        let paramData = $("#inputForm").formSerialize(true);
        if( paramData.rtnValid ){
            if( fnSaveValid() ){
                let url = "";

                if( viewModel.newCreate ){
                    url = "/admin/policy/payment/addPolicyPaymentCardBenefit";
                }else{
                    url = "/admin/policy/payment/putPolicyPaymentCardBenefit";
                    paramData['psCardBenefitId'] = viewModel.get("policyPaymentCardBenefitInfo").psCardBenefitId;
                }
                fnAjax({
                    url     : url,
                    params  : paramData,
                    success : function( data ){
                        if( viewModel.get("newCreate") ){
                            fnKendoMessage({  message : "저장이 완료되었습니다."
                                            , ok : function(){
                                                parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                                              }
                                           });
                        }else{
                            fnKendoMessage({  message : "저장이 완료되었습니다."
                                            , ok : function(){
                                                  parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                                              }
                                           });
                        }
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "insert"
                });
            }
        }
    };
    //--------------------------------- Button End---------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){
    	fnKendoDropDownList({
            id : "psPgCd",
            url : "/admin/comn/getCodeList",
            tagId : "psPgCd",
            params : { "stCommonCodeMasterCode" : "PG_SERVICE", "useYn" : "Y" },
            chkVal: "",
            blank : "PG 선택",
            async : false
        });
    	fnKendoDropDownList({
    		id : "cardBenefitTp",
    		url : "/admin/comn/getCodeList",
    		tagId : "cardBenefitTp",
    		params : { "stCommonCodeMasterCode" : "CARD_BENEFIT_INFO_TP", "useYn" : "Y" },
    		chkVal: "",
    		blank : "안내유형 선택",
    		async : false
    	});
    	// 등록일 시작
        fnKendoDatePicker({
            id: "startDt",
            format: "yyyy-MM-dd",
            btnStartId: "startDt",
            btnEndId: "endDt",
            change : function(e) {
            	fnStartCalChange("startDt", "endDt");
            }
        });

        // 등록일 종료
        fnKendoDatePicker({
            id: "endDt",
            format: "yyyy-MM-dd",
            btnStartId: "startDt",
            btnEndId: "endDt",
            change : function(e) {
            	fnEndCalChange("startDt", "endDt");
            }
        });
    	fnKendoEditor( {id : 'information'} );

    	$('#startDt').prop('readonly', true);
    	$('#endDt').prop('readonly', true);
    };

    //---------------Initialize Option Box End ------------------------------------------------

    //-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnViewModelInit(){
    	viewModel = new kendo.data.ObservableObject({
    		policyPaymentCardBenefitInfo : { // 카드혜택 템플릿
    			psCardBenefitId : null, // 카드혜택.SEQ
    			psPgCd : null, // PG사 SEQ
    			cardBenefitTp : "", // 카드혜택타입
    			startDt : "", // 적용시작일
    			endDt : "", // 적용종료일
    			title : "", // 혜택제목
    			information : "" // 혜택내용
    		},
    		newCreate : true, // 신규 여부
    		fnGetPolicyPaymentCardBenefitInfo : function( dataItem ){ // 카드혜택 상세정보 조회
    			fnAjax({
    				url     : "/admin/policy/payment/getPolicyPaymentCardBenefitInfo",
    				params  : dataItem,
    				method : "GET",
    				success : function( data ){
    					viewModel.set("policyPaymentCardBenefitInfo", data);
						$('#inputForm').bindingForm( {'rows':data},'rows', true);
		                fnStartCalChange("startDt", "endDt");
		            	fnEndCalChange("startDt", "endDt");
    				},
    				isAction : "select"
    			});
    		}
    	});
    	kendo.bind($("#inputForm"), viewModel);
    };

    // 기본값 셋팅
    function fnDefaultValue(){
        if( !paramData.psCardBenefitId ){
            viewModel.set("newCreate", true);
        }else{
            viewModel.set("newCreate", false);
            viewModel.fnGetPolicyPaymentCardBenefitInfo( paramData );
        }
    };

    // 입력값 검증
    function fnSaveValid(){
    	return true;
    };

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function(){ fnSave(); }; // 저장
    $scope.fnClose = function(){ fnClose(); }; // 닫기

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
