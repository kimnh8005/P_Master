/**-----------------------------------------------------------------------------
 * system           : PG 결제수단 관리
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.21     박승현          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "policyPaymentGatewayMethod",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){
        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
        fnInitGrid();   //Initialize Grid ------------------------------------
    };
	//--------------------------------- Button Start---------------------------------
    // 버튼 초기화
	function fnInitButton(){
		$("#fnSave, #fnClear").kendoButton();
	};

    // 초기화
    function fnClear() {
    	fnUI();
    };

    // 신규
    function fnSave() {
    	var flag = true;

    	let paramData = {};
		var policyPaymentGatewayMethodList = new Array() ;

    	$("input:checkbox[name=useYn]").each(function(){
    		let policyPaymentGatewayMethod = new Object();
    		policyPaymentGatewayMethod.psPaymentMethodId = $(this).val();
    		if($(this).is(":checked")) policyPaymentGatewayMethod.useYn = "Y";
    		else policyPaymentGatewayMethod.useYn = "N";
    		policyPaymentGatewayMethodList.push(policyPaymentGatewayMethod);
    	});

		if(flag) {
			paramData.policyPaymentGatewayMethodList = policyPaymentGatewayMethodList;

	        fnAjax({
	            url     : "/admin/policy/payment/putPolicyPaymentGatewayMethod",
	            params  : paramData,
	            contentType : "application/json",
	            success : function( data ){
	                    fnKendoMessage({  message : "저장이 완료되었습니다."});
	            },
	            error : function(xhr, status, strError){
	                fnKendoMessage({ message : xhr.responseText });
	            },
	            isAction : "update"
	        });
		}
    };

    //--------------------------------- Button End---------------------------------

    // 입력값 검증
    function fnSaveValid(){
    };
    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	aGridDs = new kendo.data.DataSource({
	        transport: {
	        	read : {
	                dataType : 'json',
	                type     : 'POST',
	                url: "/admin/policy/payment/getPolicyPaymentGatewayMethodList",
                    beforeSend: function(req) {
                        req.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                    }
	            }, parameterMap: function(data) {
	            	data["psPayCd"]   = 'PAY_TP.CARD';
	            	return data;
	            }
	        },
	        schema: {
	        	data  : function(response) {
        			return response.data.policyPaymentGatewayMethodList
	            }
	        }
		});

    	aGridOpt = {
    			dataSource: aGridDs
    			,navigatable: true
    			,columns   : [
    				{ title : 'KCP', attributes:{ style:'text-align:center' },
    					columns: [
    						 		{field : "bankNmCdNm", title: "카드사명", width: 50	, attributes:{ style:'text-align:center' }}
    					 			,{ field : 'useYn', title : "허용여부"
    				                	, template : function(dataItem) {
    				                		let checked = "";
    				                		if( dataItem.useYnKcp == "Y" ){
    				                			checked = "checked";
    		                                }
    				                		return '<input type="checkbox" name="useYn" value="' + dataItem.psPaymentMethodIdKcp + '" class="itemGridChk" '+ checked + ' />';
    				                	}
    					 				, width:'50px', attributes : {style : "text-align:center;"}
    					 			 }
    					 		]
    				}
    				,{ title : '이니시스', attributes:{ style:'text-align:center' },
    					columns: [
					 				{field : "bankNmCdNm", title: "카드사명", width: 50	, attributes:{ style:'text-align:center' }}
					 				,{ field : 'useYn', title : "허용여부"
    				                	, template : function(dataItem) {
    				                		let checked = "";
    				                		if( dataItem.useYnInicis == "Y" ){
    				                			checked = "checked";
    		                                }
    				                		return '<input type="checkbox" name="useYn" value="' + dataItem.psPaymentMethodIdInicis + '" class="itemGridChk" '+ checked + ' />';
    				                	}
    					 				, width:'50px', attributes : {style : "text-align:center;"}
    					 			 }
					 			]
    				}
    				,{ title : '', attributes:{ style:'text-align:center' },
    					columns: [
    						{field : "", title: "", width: 50	, attributes:{ style:'text-align:center' }}
    						,{field : "", title: "", width: "50px"	, attributes:{ style:'text-align:center' }}
    						]
    				}
    				,{ title : '', attributes:{ style:'text-align:center' },
    					columns: [
    						{field : "", title: "", width: 50	, attributes:{ style:'text-align:center' }}
    						,{field : "", title: "", width: "50px"	, attributes:{ style:'text-align:center' }}
    						]
    				}
    			]
    			,selectable : false
    			,noRecordMsg : '결제수단이 없습니다'
    		};
    	aGrid = $("#aGrid").initializeKendoGrid( aGridOpt ).cKendoGrid();
    	aGridDs.read();
    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {
    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function() { fnSave(); }; // 저장
    $scope.fnClear = function() { fnClear(); }; // 초기화

    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
