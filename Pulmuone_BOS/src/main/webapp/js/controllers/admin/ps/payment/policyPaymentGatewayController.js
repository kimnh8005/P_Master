/**-----------------------------------------------------------------------------
 * system           : PG 설정정보 관리
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.19     박승현          최초생성
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
            PG_ID  : "policyPaymentGateway",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){
        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
	   	fnDefaultValue();
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

    // 조회
    function fnSearch(){
        aGridDs.read();
    };

    // 신규
    function fnSave() {
    	var flag = true;
    	var allData = aGrid.dataSource.data();
		for (var i=0; i < allData.length; i++) {
			let useRatioKcp = allData[i].useRatioKcp;
			let useRatioInicis = allData[i].useRatioInicis;
			if((parseInt(useRatioKcp)+parseInt(useRatioInicis)) != 100){
				fnKendoMessage({message : "["+allData[i].psPayCdName +"] PG사별 이중화비율 총합이 100%가 되어야 합니다." });
				flag = false;
				break;
			}
		};
		let paramData = {};
		var policyPaymentGatewayList = new Array() ;

		//PG사 증가시 PG사 length로 처리하면됨
		for (var i=0; i < 2; i++) {
			let policyPaymentGateway = new Object();
			policyPaymentGateway.psPgCd = $("#psPgCd"+i).val();
			policyPaymentGateway.depositScheduled = $("#depositScheduled"+i).val();
			policyPaymentGateway.automaticDepositUrl = $("#automaticDepositUrl"+i).val();
			if(!fnSaveValid(policyPaymentGateway)){
				flag = false;
				break;
			}
			policyPaymentGatewayList.push(policyPaymentGateway);
		}
		if(flag) {
			paramData.rows = allData;
			paramData.policyPaymentGatewayList = policyPaymentGatewayList;

	        fnAjax({
	            url     : "/admin/policy/payment/putPolicyPaymentGateway",
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
    function fnDefaultValue(){
		fnAjax({
			url     : '/admin/policy/payment/getPolicyPaymentGatewayList',
			method : "GET",
			success :
				function( data ){
					for(let i=0; i<data.rows.length; i++){
						$("#psPgCd"+i).val(data.rows[i].psPgCd);
						$("#pgName"+i).html(data.rows[i].pgName);
						$("#pgLogo"+i).html(data.rows[i].pgLogo);
						$("#pgSiteCode"+i).html(data.rows[i].pgSiteCode);
						$("#pgSiteKey"+i).html(data.rows[i].pgSiteKey);
						$("#pgBatchPayInfo"+i).html(data.rows[i].pgBatchPayInfo);
						$("#depositScheduled"+i).val(data.rows[i].depositScheduled);
						$("#automaticDepositUrl"+i).val(data.rows[i].automaticDepositUrl);
					}
				},
			isAction : 'select'
		});
	}

    // 입력값 검증
    function fnSaveValid(policyPaymentGateway){
    	if( policyPaymentGateway.depositScheduled.length < 1 ){
    		fnKendoMessage({ message : "무통장(가상계좌)결제 입금예정 기한설정일을 입력해주세요"});
    		return false;
    	}
    	if( policyPaymentGateway.automaticDepositUrl.length < 1 ){
    		fnKendoMessage({ message : "무통장(가상계좌)결제 자동입금확인 URL 을 입력해주세요"});
    		return false;
    	}
    	return true;
    };
    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	var basicRatioRt = [];

		for (var i = 0; i <= 100; i += 10) {
			var rate = i;
			basicRatioRt.push({"NAME":rate, "CODE":rate});
		}

    	aGridDs = fnGetDataSource({
            url      : "/admin/policy/payment/getPolicyPaymentGatewayRatioList"
        });

    	aGridOpt = {
            dataSource : aGridDs,
            navigatable: true,
            editable  : true,
            columns   : [
		                { field : "psPayCdName", title: "결제수단", width: "200px", attributes : {style : "text-align:center"}, editable:function (dataItem) {return false; }}
		                ,{ field:'useRatioKcp'	    ,title : 'KCP'	, width:'200px',attributes:{ style:'text-align:center' }
						  ,editor: function(container, options) {
								var input = $("<input id='useRatioKcp' value='"+options.model.useRatioKcp+"'   selected='selected' />");
								input.appendTo(container);
								fnKendoDropDownList({
									id    : 'useRatioKcp',
									data  : basicRatioRt,
									value :	options.model.useRatioKcp
								});
								$('#useRatioKcp').unbind('change').on('change', function(){
									var dataItem = aGrid.dataItem($(this).closest('tr'));
									var useRatioDropDownList =$('#useRatioKcp').data('kendoDropDownList');
									dataItem.set('useRatioKcp', useRatioDropDownList.value());
								});

							}
						}
		                ,{ field:'useRatioInicis'	    ,title : '이니시스'	, width:'200px',attributes:{ style:'text-align:center' }
		                	,editor: function(container, options) {
			                	var input = $("<input id='useRatioInicis' value='"+options.model.useRatioInicis+"'   selected='selected' />");
								input.appendTo(container);
								fnKendoDropDownList({
									id    : 'useRatioInicis',
									data  : basicRatioRt,
									value :	options.model.useRatioInicis
								});
								$('#useRatioInicis').unbind('change').on('change', function(){
									var dataItem = aGrid.dataItem($(this).closest('tr'));
									var useRatioDropDownList =$('#useRatioInicis').data('kendoDropDownList');
									dataItem.set('useRatioInicis', useRatioDropDownList.value());
								});
		                	}
		                }
		                , { field:'psPayCd'		,hidden:true}
            ],
            selectable : false,
            noRecordMsg : '결제수단이 없습니다'
        };

    	aGrid = $("#aGrid").initializeKendoGrid( aGridOpt ).cKendoGrid();

    	aGridDs.read();
    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {
		fnInputValidationByRegexp("depositScheduled0", /[^0-9]/g);
		fnInputValidationByRegexp("depositScheduled1", /[^0-9]/g);
    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function() { fnSave(); }; // 저장
    $scope.fnClear = function() { fnClear(); }; // 초기화

    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
