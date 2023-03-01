/**-----------------------------------------------------------------------------
 * system 			 : 주문상태실행관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.18		최윤지          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'orderStatusActionMgm',
			callback : fnUI
		});
	}
	// 화면 UI 초기화
	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------
		fnInitGrid();	//Initialize Grid ------------------------------------
		fnInitOptionBox();//Initialize Option Box ----------------------------
		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnNew, #fnSave,  #fnClearInput').kendoButton();
	}
	// 조회
    function fnSearch(){

        var query = {
                    page         : 1,
                    pageSize     : PAGE_SIZE,
        };
        aGridDs.query( query );
    }

	 // 팝업 초기화
    function fnClearInput() {
        var data = $('#inputForm').data("data");
        if (data)
            $('#inputForm').bindingForm(data, 'rows', true);
        else
            $('#inputForm').formClear(true);
    }

    // 신규
    function fnNew(){
        $('#inputForm').formClear(true);
        $('#inputForm').data("data", "");
        $("#fnClearInput").show();
        $("#actionTarget").data("kendoDropDownList").enable( true );
        fnKendoInputPoup({height:"500px" ,width:"500px", title:{ nullMsg :'주문상태실행 등록' } });
    }

    // 저장
	function fnSave(){
		var url  = '/admin/order/statusMgr/addOrderStatusAction'; // 주문상태실행 등록 url
        var cbId = 'insert';

        if( OPER_TP_CODE == 'U' ){
            url  = '/admin/order/statusMgr/putOrderStatusAction';
            cbId= 'update';
        }

        var data = $('#inputForm').formSerialize(true);

        if( data.rtnValid ){
            fnAjax({
                url     : url,
                params  : data,
                success :
                    function( data ){
                        fnBizCallback(cbId, data);
                    },
                    isAction : 'batch'
            });
        }

	}

	// 닫기
    function fnClose(){
        var kendoWindow =$('#kendoPopup').data('kendoWindow');
        kendoWindow.close();
    }

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){
        aGridDs = fnGetPagingDataSource({
            url      : '/admin/order/statusMgr/getOrderStatusActionList', //조회 URL
            pageSize : PAGE_SIZE
        });
        aGridOpt = {
            dataSource: aGridDs
            ,  pageable  : {
                pageSizes: [20, 30, 50],
                buttonCount : 10
            }
            ,navigatable: true
            ,columns   : [
                 { field:'actionId'            ,title : '주문상태실행ID'       , width:'80px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'actionType'           ,title : '실행구분'       , width:'50px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'actionExecId'           ,title : '실행ID'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'actionExecNm'           ,title : '실행구분명'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'actionTarget'           ,title : '실행타겟'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'actionTargetUrl'         ,title : '실행URL'       , width:'200px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'actionConfirm'           ,title : 'Confirm메세지'       , width:'200px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'actionAttr1'           ,title : '실행보조1'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'actionAttr2'           ,title : '실행보조2'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'actionAttr3'           ,title : '실행보조3'       , width:'100px' ,attributes:{ style:'text-align:center' }}
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        $("#aGrid").on("click", "tbody>tr", function () {
            fnGridClick();
        });

        // 총 건수
        aGrid.bind("dataBound", function() {
            //total count
            $('#countTotalSpan').text(aGridDs._total);
        });
    }
	//그리드 클릭
	function fnGridClick(){
		$("#fnClearInput").hide();
	    var aMap = aGrid.dataItem(aGrid.select());
	    fnAjax({
	        url     : '/admin/order/statusMgr/getOrderStatusAction',
	        params  : {actionId : aMap.actionId},
	        success :
	            function( data ){
	                fnBizCallback("select",data);
	            },
	        isAction : 'select'
	    });
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
        $('#kendoPopup').kendoWindow({
            visible: false,
            modal: true
        });

		// 실행타겟 리스트 조회
		fnKendoDropDownList({
			id  : 'actionTarget',
            data  :
                [{"CODE":"submit","NAME":'Submit'}
                ,{"CODE":"selfLink","NAME":'링크'}
                ,{"CODE":"popupLink","NAME":'새창링크'}
                ,{"CODE":"popup","NAME":'팝업'}
                ,{"CODE":"layer","NAME":'레이어'}
                ,{"CODE":"ajax","NAME":'Ajax'}
                ],
			blank : "선택해주세요"
		});

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
	            //form data binding
	            $('#inputForm').data("data", data);
	            $('#inputForm').bindingForm(data, 'rows', true);
	            fnKendoInputPoup({height:"500px" ,width:"500px", title:{nullMsg :'주문상태실행 수정'} });
	            break;
	        case 'insert':
	        	if(data.messageEnum =="DUPLICATE_DATA"){
                    fnKendoMessage({
                        message : '중복된 데이터가 존재합니다.'
                    });
				} else {
		            fnSearch();
		            $('#kendoPopup').data('kendoWindow').close();
		            fnKendoMessage({message : '저장되었습니다.'});
				}
	            break;
	        case 'update':
	            aGridDs.query();
	            fnKendoMessage({message : '수정되었습니다.'});
	            fnClose();
	            break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	/** input Clear */
    $scope.fnClearInput = function( ){  fnClearInput();}; // 팝업 초기화
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
