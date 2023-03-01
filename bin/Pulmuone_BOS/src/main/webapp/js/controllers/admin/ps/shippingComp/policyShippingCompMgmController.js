/**-----------------------------------------------------------------------------
 * system            : 배송정책 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.08     박승현          최초생성
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
            PG_ID  : "policyShippingCompMgm",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();	// Initialize Button  ---------------------------------
        fnInitOptionBox(); // Initialize Option Box ------------------------------------
        fnViewModelInit();
        fnInitGrid();
        fnDefaultValue();
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSave").kendoButton();
        $("#fnFindTransInfo").kendoButton();
        $("#fnAdd").kendoButton();
        $('#fnDel').kendoButton({ enable: false });
    };

    // 팝업 닫기
    function fnClose(){
        if( viewModel.newCreate ){
            parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
        }else{
            fnKendoMessage({
                type    : "confirm",
                message : "저장 후 변경 된 정보가 반영 됩니다.",
                ok      : function(){
                    parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                },
                cancel  : function(){
                }
            });
        }
    };

    // 저장
    function fnSave(){
        let paramData = $("#inputForm").formSerialize(true);
        if( paramData.rtnValid ){
            let url = "";

            // 택배사 코드
            var shippingCompCode = "";
            var shippingCompCodeList = [];
            var shippingCompCodeMap = new Object();
            var regExp = /[^a-zA-Z0-9]/;
            $.each($("#shippingCompCdList").find("li"), function (i, item){
            	var that = $(this);
            	shippingCompCode = $.trim(that.find("input[name='shippingCompCds']").val());

            	if(regExp.test(shippingCompCode)) {
            		fnKendoMessage({ message: "택배사 코드는 영어 대소문자, 숫자만 입력 가능합니다."}); return false;
            	}

            	if (shippingCompCode == "") {
                    fnKendoMessage({message: "택배사 코드를 입력해주세요."}); return false;
                }

            	shippingCompCodeMap = new Object();
            	shippingCompCodeMap.shippingCompCode = shippingCompCode;
            	shippingCompCodeList.push(shippingCompCodeMap);
            });

            paramData.shippingCompCodeList = shippingCompCodeList;

            if( viewModel.newCreate ){
                url = "/admin/policy/shippingcomp/addPolicyShippingComp";
            }else{
                url = "/admin/policy/shippingcomp/putPolicyShippingComp";
                paramData['psShippingCompId'] = viewModel.get("policyShippingCompInfo").psShippingCompId;
                paramData['shippingCompTel'] = fnIsEmpty(paramData.shippingCompTel) == true ? null : paramData.shippingCompTel;
            }

            fnAjax({
                url     : url,
                params  : paramData,
                success : function( data ){
                    if( viewModel.get("newCreate") ){
                        fnKendoMessage({  message : "등록되었습니다."
                        				, ok : function(){ parent.LAYER_POPUP_OBJECT.data("kendoWindow").close(); }});
                    }else{
                        fnKendoMessage({  message : "수정되었습니다."
                                        , ok : function(){ parent.LAYER_POPUP_OBJECT.data("kendoWindow").close(); }});
                    }
                },
                error : function(xhr, status, strError){
                    fnKendoMessage({ message : xhr.responseText });
                },
                isAction : "insert"
            });
        }
    };

    function fnDel() {
    	fnKendoMessage({message:fnGetLangData({key :"102938",nullMsg :'삭제하면 데이터 복구가 불가능 합니다. 삭제 하시겠습니까?' }), type : "confirm"
    		,ok : function(){
    			var url  = "/admin/policy/shippingcomp/delPolicyShippingComp";
    			var data = $('#inputForm').formSerialize(true);
    			data['psShippingCompId'] = viewModel.get("policyShippingCompInfo").psShippingCompId;
    			if( data.rtnValid ){
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
            }
    	});
    }

    function fnAdd(shippingCompCd){

    	var code = shippingCompCd == null ? "" : shippingCompCd;

    	var str = "";
		str += "<li class='marginT5'>";
		str += '<input type="text" name="shippingCompCds" class="comm-input marginR5" style="width: 200px;" maxlength="10" value="'+ code + '" required>';
		str += "<button type='button' class='k-button k-button-icontext btn-red btn-s k-grid-remove marginT5' onclick='$scope.fnShippingCompCdDel(event)'>삭제</button>";
		str += "</li>";

    	$("#shippingCompCdList").append(str);
    }

    function fnShippingCompCdDel(e){
    	var event = e || window.event;
    	var target = event.target;

    	target.parentElement.remove();
    }

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    };
    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){
    	fnTagMkRadio({
			id    :  'useYn',
			tagId : 'useYn',
			data : [
                { "CODE" : "Y", "NAME" : "예" },
                { "CODE" : "N", "NAME" : "아니오" }
            ],
			chkVal: 'Y',
			style : {}
		});
    	fnTagMkRadio({
    		id    :  'httpRequestTp',
    		tagId : 'httpRequestTp',
    		data : [
    			{ "CODE" : "G", "NAME" : "GET" },
    			{ "CODE" : "P", "NAME" : "POST" }
    			],
    			chkVal: 'G',
    			style : {}
    	});

    	fnInputValidationForAlphabetHangul('shippingCompNm');
    	fnInputValidationForNumberBar('shippingCompTel');
    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------
    // viewModel 초기화
    function fnViewModelInit(){
        viewModel = new kendo.data.ObservableObject({
        	policyShippingCompInfo : { // 택배사설정 탬플릿
            	psShippingCompId : null, // 택배사설정 ID
                originalPsShippingCompId : 0, // 원본 택배사설정 ID
                shippingCompCd : "", // 택배사코드
                shippingCompNm : "", // 택배사명
                trackingUrl : "", // 배송정보추척URL
                httpRequestTp : "G", // HTTP 전송방법 공통코드
                invoiceParam :"", // 송장파라미터
                useYn : "Y", // 사용여부 (기본값 : 합배송)
            },
            newCreate : true, // 신규 여부
            fnGetPolicyShippingCompInfo : function( dataItem ){ // 배송정책 상세정보 조회
                fnAjax({
                    url     : "/admin/policy/shippingcomp/getPolicyShippingCompInfo",
                    params  : dataItem,
                    method : "GET",
                    success : function( data ){
                        viewModel.set("policyShippingCompInfo", data);
                        $('input:radio[name="useYn"]:input[value="' + data.useYn + '"]').prop("checked", true);
                        $('input:radio[name="httpRequestTp"]:input[value="' + data.httpRequestTp + '"]').prop("checked", true);
                        $('#fnDel').kendoButton().data("kendoButton").enable(true);

                        // 수집몰 코드
                        for(var i = 0 ; i < data.shippingCompOutmallList.length ; i++){
                    		if(data.shippingCompOutmallList[i].outmallCode == 'E' ){
                    			if( !fnIsEmpty(data.shippingCompOutmallList[i].outmallShippingCompCode) && !fnIsEmpty(data.shippingCompOutmallList[i].outmallShippingCompName)){
                    				$('#ezadminShippingCompCd').val(data.shippingCompOutmallList[i].outmallShippingCompCode);
                    				$('#ezadminShippingCompNm').val(data.shippingCompOutmallList[i].outmallShippingCompName);
                    				$('#ezadminShippingCompView').val(data.shippingCompOutmallList[i].outmallShippingCompName + " (" + data.shippingCompOutmallList[i].outmallShippingCompCode + ")");
                    			}
                    		}else{
                    			if( !fnIsEmpty(data.shippingCompOutmallList[i].outmallShippingCompCode)){
                    				$('#sabangnetShippingCompCd').val(data.shippingCompOutmallList[i].outmallShippingCompCode);
                    			}
                    		}
                    	}

                        // 택배사 코드
                        for(var i = 1 ; i < data.shippingCompCodeList.length ; i++){
                    		fnAdd(data.shippingCompCodeList[i].shippingCompCd);
                    	}

                        //$("input[name=shippingCompCds]").attr("readonly", true).addClass("forbiz-cell-readonly");
                        //$("#fnAdd").css("display","none");
                    },
                    isAction : "select"
                });
            }
        });
        kendo.bind($("#inputForm"), viewModel);
    };
    function fnFindTransInfo(){
    	fnKendoPopup({
			id     : 'ezAdminEtcTransInfo',
			title  : '외부몰 택배사 코드 조회',
			src    : '#/ezAdminEtcTransInfo',
			width  : '1100px',
			height : '800px',
            scrollable : "yes",
			success: function( stMenuId, data ){
				if(data.code != undefined){
					$('#ezadminShippingCompCd').val(data.code);
					$('#ezadminShippingCompNm').val(data.name);
					$('#ezadminShippingCompView').val(data.name + " (" + data.code + ")");
				}
			}
		});
    }

    // 기본값 셋팅
    function fnDefaultValue(){
        if( !paramData.psShippingCompId ){
            viewModel.set("newCreate", true);
        }else{
            viewModel.set("newCreate", false);
            viewModel.fnGetPolicyShippingCompInfo( paramData );
        }
    };

    // 입력값 검증
    function fnSaveValid(){

        return true;
    };

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function(){ 				fnSave();}; // 저장
    $scope.fnClose = function(){ 				fnClose();};// 닫기
	$scope.fnDel = function(){	 				fnDel();}; 	// 삭제
	$scope.fnFindTransInfo = function(){		fnFindTransInfo();}; //수집몰코드 찾기
	$scope.fnAdd = function(shippingCompCd){	fnAdd(shippingCompCd);}; // 택배사 코드 추가
	$scope.fnShippingCompCdDel = function(){	fnShippingCompCdDel();}; // 택배사 코드 삭제


    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
