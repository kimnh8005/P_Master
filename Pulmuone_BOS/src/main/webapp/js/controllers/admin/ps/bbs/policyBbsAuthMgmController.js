﻿/**-----------------------------------------------------------------------------
 * system            : 게시판권한 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.21     박승현          최초생성
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
            PG_ID  : "policyBbsAuthMgm",
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
            if( fnSaveValid() ){
                let url = "";

                if( viewModel.newCreate ){
                    url = "/admin/policy/bbs/addPolicyBbsAuth";
                }else{
                    url = "/admin/policy/bbs/putPolicyBbsAuth";
                    paramData['csBbsConfigId'] = viewModel.get("policyBbsAuthInfo").csBbsConfigId;
                }
                fnAjax({
                    url     : url,
                    params  : paramData,
                    success : function( data ){
                        if( viewModel.get("newCreate") ){
                            fnKendoMessage({  message : "등록되었습니다."
                                            , ok : function(){
                                                parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                                              }
                                           });
                        }else{
                            fnKendoMessage({  message : "수정되었습니다."
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

    function fnDel() {
    	fnKendoMessage({message:fnGetLangData({key :"102938",nullMsg :'삭제하면 데이터 복구가 불가능 합니다. 삭제 하시겠습니까?' }), type : "confirm"
    		,ok : function(){
    			var url  = "/admin/policy/bbs/delPolicyBbsAuth";
    			var data = $('#inputForm').formSerialize(true);
    			data['csBbsConfigId'] = viewModel.get("policyBbsAuthInfo").csBbsConfigId;
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
			id    :  'imageYn',
			tagId : 'imageYn',
			data : [
                { "CODE" : "Y", "NAME" : "예" },
                { "CODE" : "N", "NAME" : "아니오" }
            ],
			chkVal: 'Y',
			style : {}
		});
    	fnTagMkRadio({
    		id    :  'attachYn',
    		tagId : 'attachYn',
    		data : [
    			{ "CODE" : "Y", "NAME" : "예" },
    			{ "CODE" : "N", "NAME" : "아니오" }
    			],
    			chkVal: 'Y',
    			style : {}
    	});
    	fnTagMkRadio({
    		id    :  'replyYn',
    		tagId : 'replyYn',
    		data : [
    			{ "CODE" : "Y", "NAME" : "예" },
    			{ "CODE" : "N", "NAME" : "아니오" }
    			],
    			chkVal: 'Y',
    			style : {}
    	});
    	fnTagMkRadio({
    		id    :  'commentYn',
    		tagId : 'commentYn',
    		data : [
    			{ "CODE" : "Y", "NAME" : "예" },
    			{ "CODE" : "N", "NAME" : "아니오" }
    			],
    			chkVal: 'Y',
    			style : {}
    	});
    	fnTagMkRadio({
    		id    :  'commentSecretYn',
    		tagId : 'commentSecretYn',
    		data : [
    			{ "CODE" : "Y", "NAME" : "예" },
    			{ "CODE" : "N", "NAME" : "아니오" }
    			],
    			chkVal: 'Y',
    			style : {}
    	});
    	fnTagMkRadio({
    		id    :  'recommendYn',
    		tagId : 'recommendYn',
    		data : [
    			{ "CODE" : "Y", "NAME" : "예" },
    			{ "CODE" : "N", "NAME" : "아니오" }
    			],
    			chkVal: 'Y',
    			style : {}
    	});
        // 게시판 분류
        fnKendoDropDownList({
        	id : "csCategoryId",
        	url : "/admin/policy/bbs/getPolicyBbsAuthCategoryList",
        	tagId : "csCategoryId",
        	params : { "bbsTp" : "1"},
        	textField : "categoryNm",
            valueField : "csCategoryId",
        	blank : " ",
        	async : false
        });
        fnInputValidationByRegexp("bbsNm", /[^ㄱ-ㅎㅏ-ㅣ가-힣]/g);
    };

    //---------------Initialize Option Box End ------------------------------------------------

    //-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnViewModelInit(){
    	viewModel = new kendo.data.ObservableObject({
    		policyBbsAuthInfo : { // 게시판권한설정 템플릿
    			csBbsConfigId : null, // 게시판권한설정.SEQ
    			csCategoryId : null, // 게시판분류 SEQ
    			bbsNm : "", // 게시판명
    			imageYn : "", // 이미지 사용여부(Y:사용)
    			attachYn : "", // 첨부여부(Y:사용)
    			replyYn : "", // 답변여부(Y:사용)
    			commentYn : "", // 댓글 사용 여부(Y:사용)
    			commentSecretYn : "", // 댓글 비밀 여부(Y:사용)
    			recommendYn : "Y" // 추천 여부(Y:사용)
    		},
    		newCreate : true, // 신규 여부
    		fnGetPolicyBbsAuthInfo : function( dataItem ){ // 게시판권한설정 상세정보 조회
    			fnAjax({
    				url     : "/admin/policy/bbs/getPolicyBbsAuthInfo",
    				params  : dataItem,
    				method : "GET",
    				success : function( data ){
    					viewModel.set("policyBbsAuthInfo", data);
    					$("#csCategoryId").data("kendoDropDownList").value(data.csCategoryId);
                        $('input:radio[name="imageYn"]:input[value="' + data.imageYn + '"]').prop("checked", true);
                        $('input:radio[name="attachYn"]:input[value="' + data.attachYn + '"]').prop("checked", true);
                        $('input:radio[name="replyYn"]:input[value="' + data.replyYn + '"]').prop("checked", true);
                        $('input:radio[name="commentYn"]:input[value="' + data.commentYn + '"]').prop("checked", true);
                        $('input:radio[name="commentSecretYn"]:input[value="' + data.commentSecretYn + '"]').prop("checked", true);
                        $('input:radio[name="recommendYn"]:input[value="' + data.recommendYn + '"]').prop("checked", true);
    					$('#fnDel').kendoButton().data("kendoButton").enable(true);
    				},
    				isAction : "select"
    			});
    		}
    	});
    	kendo.bind($("#inputForm"), viewModel);
    };

    // 기본값 셋팅
    function fnDefaultValue(){
        if( !paramData.csBbsConfigId ){
            viewModel.set("newCreate", true);
        }else{
            viewModel.set("newCreate", false);
            viewModel.fnGetPolicyBbsAuthInfo( paramData );
        }
    };

    // 입력값 검증
    function fnSaveValid(){
    	if( $("#bbsNm").val().length < 1 ){
    		fnKendoMessage({ message : "게시판명 미입력",
    						ok : function(e) { $("#bbsNm").focus(); }
    		});
    		return false;
    	}
    	if( $("#csCategoryId").val().length < 1 ){
    		fnKendoMessage({ message : "분류 미선택",
    						ok : function(e) { $("#csCategoryId").focus(); }
    		});
    		return false;
    	}
    	return true;
    };

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function(){ fnSave(); }; // 저장
    $scope.fnClose = function(){ fnClose(); }; // 닫기
	$scope.fnDel = function(){	 fnDel();};

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
