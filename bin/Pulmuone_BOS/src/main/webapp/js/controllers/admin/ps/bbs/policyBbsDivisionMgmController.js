﻿/**-----------------------------------------------------------------------------
 * system            : 게시판분류 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.18     박승현          최초생성
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
            PG_ID  : "policyBbsDivisionMgm",
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
                    url = "/admin/policy/bbs/addPolicyBbsDivision";
                }else{
                    url = "/admin/policy/bbs/putPolicyBbsDivision";
                    paramData['csCategoryId'] = viewModel.get("policyBbsDivisionInfo").csCategoryId;
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
    			var url  = "/admin/policy/bbs/delPolicyBbsDivision";
    			var data = $('#inputForm').formSerialize(true);
    			data['csCategoryId'] = viewModel.get("policyBbsDivisionInfo").csCategoryId;
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
			id    :  'useYn',
			tagId : 'useYn',
			data : [
                { "CODE" : "Y", "NAME" : "예" },
                { "CODE" : "N", "NAME" : "아니오" }
            ],
			chkVal: 'Y',
			style : {}
		});
    	// 게시판 분류 타입
        fnKendoDropDownList({
            id : "bbsTp",
            url : "/admin/comn/getCodeList",
            tagId : "bbsTp",
            params : { "stCommonCodeMasterCode" : "BBS_TP", "useYn" : "Y" },
            chkVal: "",
            blank : "전체",
            async : false
        });
        // 게시판 분류 상위 분류
        fnKendoDropDownList({
        	id : "parentCategoryId",
        	url : "/admin/policy/bbs/getPolicyBbsDivisionParentCategoryList",
        	tagId : "parentCategoryId",
        	textField : "categoryNm",
            valueField : "csCategoryId",
        	blank : "전체",
        	async : false,
            cscdId : "bbsTp",
            cscdField : "bbsTp"
        });
        fnInputValidationByRegexp("categoryNm", /[^ㄱ-ㅎㅏ-ㅣ가-힣]/g);
    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnViewModelInit(){
        viewModel = new kendo.data.ObservableObject({
        	policyBbsDivisionInfo : { // 게시판분류 설정 템플릿
        		csCategoryId : null, // 게시판분류 ID
        		bbsTp : "", // 게시판분류 타입 공통코드(BBS_TP)
        		parentCategoryId : "", // 게시판분류설정.상위 PK
        		categoryNm : "", // 게시판 분류명
                useYn : "Y" // 사용여부
            },
            newCreate : true, // 신규 여부
            fnGetPolicyBbsDivisionInfo : function( dataItem ){ // 게시판분류 상세정보 조회
                fnAjax({
                    url     : "/admin/policy/bbs/getPolicyBbsDivisionInfo",
                    params  : dataItem,
                    method : "GET",
                    success : function( data ){
                        viewModel.set("policyBbsDivisionInfo", data);
                        $("#bbsTp").data("kendoDropDownList").value(data.bbsTp);
                        $('input:radio[name="useYn"]:input[value="' + data.useYn + '"]').prop("checked", true);
                        $("#parentCategoryId").data("kendoDropDownList").value(data.parentCategoryId);
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
        if( !paramData.csCategoryId ){
            viewModel.set("newCreate", true);
        }else{
            viewModel.set("newCreate", false);
            viewModel.fnGetPolicyBbsDivisionInfo( paramData );
        }
    };

 // 입력값 검증
    function fnSaveValid(){
    	if( $("#bbsTp").val().length < 1 ){
    		fnKendoMessage({ message : "타입 미선택",
    			ok : function(e) { $("#bbsTp").focus(); }
    		});
    		return false;
    	}
    	if( $("#categoryNm").val().length < 1 ){
    		fnKendoMessage({ message : "분류명 미입력",
    						ok : function(e) { $("#categoryNm").focus(); }
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
