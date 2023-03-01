/**-----------------------------------------------------------------------------
 * system 			 : 시스템 환경설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.18		최윤지			최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'orderStatusMgm',
			callback : fnUI
		});


	}
	// 화면 UI 초기화
	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------
		fnInitGrid();	//Initialize Grid ------------------------------------
		fnInitOptionBox();//Initialize Option Box ----------------------------
		fnInitMaskTextBox();
		fnSearchAGrid();
		fnSearchBGrid();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnNewAGrid, #fnSaveAGrid, #fnNewBGrid, #fnSaveBGrid').kendoButton();
	}
	// 조회
    function fnSearchAGrid(){

        aGridDs.query();
    }

    function fnSearchBGrid(){

    	bGridDs.query();
    }

    // 신규fnNewAGrid
    function fnNewAGrid(){
    	$('#inputFormAGrid').show(); // 주문상태목록 show
    	$('#inputFormBGrid').hide(); // 주문상태유형목록 hide
    	$('#jsonFormAGrid').hide();
        $('#inputFormAGrid').formClear(true);
        $('#inputFormAGrid').data("data", "");
        $('#statusCd').attr("disabled", false);
        $('#ifDayChangeYn_1').prop("checked", true);
        $('#deliverySearchYn_1').prop("checked", true);
        fnKendoInputPoup({height:"500px" ,width:"500px", title:{ nullMsg :'주문상태 등록' } });
    }

    // 신규 fnNewBGrid
    function fnNewBGrid(){
    	$('#inputFormBGrid').show(); //주문상태유형목록 show
    	$('#inputFormAGrid').hide(); //주문상태 목록 hide
    	$('#jsonFormAGrid').hide();
    	$('#inputFormBGrid').formClear(true);
    	$('#inputFormBGrid').data("data", "");
    	$('#useType').attr("disabled", false);
    	$('#typeCd').attr("disabled", false);
    	fnKendoInputPoup({height:"250px" ,width:"500px", title:{ nullMsg :'주문상태유형 등록' } });
    }

    // 저장 fnSaveAGrid
	function fnSaveAGrid(){
		var url  = '/admin/order/statusMgr/addOrderStatus'; // 주문상태 등록 url
        var cbId = 'insertAGrid';

        if( OPER_TP_CODE == 'U' ){
            url  = '/admin/order/statusMgr/putOrderStatus';
            cbId= 'updateAGrid';
        }

        var data = $('#inputFormAGrid').formSerialize(true);

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

	// 저장 fnSaveBGrid
	function fnSaveBGrid(){
		var url  = '/admin/order/statusMgr/addOrderStatusGoodsType'; // 주문상태유형 등록 url
		var cbId = 'insertBGrid';

		if( OPER_TP_CODE == 'U' ){
            url  = '/admin/order/statusMgr/putOrderStatusGoodsType';
            cbId= 'updateBGrid';
        }

		var data = $('#inputFormBGrid').formSerialize(true);

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

    // Json 정렬
    function sortResults(prop, asc) {
        people.sort(function(a, b) {
            if (asc) {
                return (a[prop] > b[prop]) ? 1 : ((a[prop] < b[prop]) ? -1 : 0);
            } else {
                return (b[prop] > a[prop]) ? 1 : ((b[prop] < a[prop]) ? -1 : 0);
            }
        });
        renderResults();
    }

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){
        aGridDs = fnGetPagingDataSource({
            url      : '/admin/order/statusMgr/getOrderStatusList', //조회 URL
            pageSize : PAGE_SIZE
        });

        aGridOpt = {
            dataSource: aGridDs
            ,navigatable: true
            ,columns   : [
                 { field:'statusSort'           ,title : '정렬순서'       , width:'20px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'statusCd'            ,title : '주문상태ID'       , width:'30px' ,attributes:{ style:'text-align:center; text-decoration: underline; color:blue;' }}
                ,{ field:'statusNm'           ,title : '주문상태명'       , width:'40px' ,attributes:{ style:'text-align:center; text-decoration: underline; color:blue;' }}
                ,{ field:'searchGrp'           ,title : '주문상태구분'       , width:'20px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'ifDayChangeYn'           ,title : 'I/F일자변경가능여부'       , width:'20px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'deliverySearchYn'           ,title : '배송추적가능여부'       , width:'20px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'orderStatusSort'           ,title : '주문통합상태우선순위'       , width:'20px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'claimStatusSort'           ,title : '주문클레임통합상태우선순위'       , width:'20px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'useYn'           ,title : '사용여부'       , width:'20px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'frontJson'           ,title : 'FrontJson'       , width:'30px' ,attributes:{ style:'text-align:center' },
	                template:
						function(dataItem){
							return "<button class='frontJson btn-s'>FrontJson</button>";
					}
                }
                ,{ field:'bosJson'           ,title : 'BosJson'       , width:'30px' ,attributes:{ style:'text-align:center' },
                	template:
                		function(dataItem){
                		return "<button class='bosJson btn-s'>BosJson</button>";
                	}
                }
                ,{ field:'actionJson'           ,title : 'ActionJson'       , width:'30px' ,attributes:{ style:'text-align:center' },
                	template:
                		function(dataItem){
                		return "<button class='actionJson btn-s'>ActionJson</button>";
                	}
                }
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        // 주문상태ID or 주문상태명 클릭 시
        $("#aGrid").on("click", "tbody>tr>td", function () {
			var index = $(this).index();
			if($(this).closest('table').find('th').eq(index).text() == '주문상태ID' || $(this).closest('table').find('th').eq(index).text() == '주문상태명'){

				$("#statusCd").attr("disabled", true);
				var aMap = aGrid.dataItem(aGrid.select());
		        fnAjax({
		            url     : '/admin/order/statusMgr/getOrderStatus',
		            params  : {statusCd : aMap.statusCd},
		            success :
		                function( data ){
		            		fnBizCallback("selectAGrid",data);
		                },
		           isAction : 'select'
		        });
			}
        });

       //frontJson 버튼 클릭
    	$("#aGrid").on("click", ".frontJson", function(){
    		var map = aGrid.dataItem($(this).closest('tr'));
    		$('#inputFormAGrid').hide();
    		$('#inputFormBGrid').hide();
    		$('#jsonFormAGrid').show();

    		$("#frontJsonSelect").show();
    		$("#bosJsonSelect").hide();
    		$("#actionJsonSelect").hide();

    		$("#frontJson").val(JSON.stringify(JSON.parse(map.frontJson), null, 4)).attr("disabled",true);

    		fnKendoInputPoup({height:"250px" ,width:"500px",title:{nullMsg :'FrontJson'} });
    	});

    	//bosJson 버튼 클릭
    	$("#aGrid").on("click", ".bosJson", function(){
    		var map = aGrid.dataItem($(this).closest('tr'));
    		$('#inputFormAGrid').hide();
    		$('#inputFormBGrid').hide();
    		$('#jsonFormAGrid').show();

    		$("#frontJsonSelect").hide();
    		$("#bosJsonSelect").show();
    		$("#actionJsonSelect").hide();

    		$("#bosJson").val(JSON.stringify(JSON.parse(map.bosJson), null, 4)).attr("disabled",true);

    		fnKendoInputPoup({height:"250px" ,width:"500px",title:{nullMsg :'BosJson'} });
    	});

    	//actionJson 버튼 클릭
    	$("#aGrid").on("click", ".actionJson", function(){
    		var map = aGrid.dataItem($(this).closest('tr'));
    		$('#inputFormAGrid').hide();
    		$('#inputFormBGrid').hide();
    		$('#jsonFormAGrid').show();

    		$("#frontJsonSelect").hide();
    		$("#bosJsonSelect").hide();
    		$("#actionJsonSelect").show();

    		$("#actionJson").val(JSON.stringify(JSON.parse(map.actionJson), null, 4)).attr("disabled",true);

    		fnKendoInputPoup({height:"250px" ,width:"500px",title:{nullMsg :'ActionJson'} });
    	});


        //bGrid
        bGridDs = fnGetPagingDataSource({
            url      : '/admin/order/statusMgr/getOrderStatusGoodsTypeList', //조회 URL
        });
        bGridOpt = {
            dataSource: bGridDs
            ,navigatable: true
            ,columns   : [
                { field:'useType'            ,title : '사용구분'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'typeCd'           ,title : '유형코드'       , width:'200px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'typeNm'           ,title : '유형명'       , width:'200px' ,attributes:{ style:'text-align:center' }}
            ]
        };

        bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();
        $("#bGrid").on("click", "tbody>tr", function () {
        	fnBGridClick();
        });

        // 총 건수 aGrid
        aGrid.bind("dataBound", function() {
            //total count
            $('#countTotalSpanAGrid').text(aGridDs._total);
        });

        // 총 건수 bGrid
        bGrid.bind("dataBound", function() {
            //total count
            $('#countTotalSpanBGrid').text(bGridDs._total);
        });

    }
	 // BGrid 클릭 fnBGridClick
	    function fnBGridClick(){
	    	$("#useType").attr("disabled", true);
	    	$("#typeCd").attr("disabled", true);
	        var aMap = bGrid.dataItem(bGrid.select());
	        fnAjax({
	            url     : '/admin/order/statusMgr/getOrderStatusGoodsType',
	            params  : {typeCd : aMap.typeCd},
	            success :
	                function( data ){
	            		fnBizCallback("selectBGrid",data);
	                },
	           isAction : 'select'
	        });
	    };

	//-------------------------------  Grid End  ------------------------------

    function fnInitMaskTextBox() {
        fnInputValidationForNumber("statusSort");
        fnInputValidationForNumberBar("orderStatusSort");
        fnInputValidationForNumberBar("claimStatusSort");

    }

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
        $('#kendoPopup').kendoWindow({
            visible: false,
            modal: true
        });

        // 주문상태구분 체크박스 - 하드코딩
        fnTagMkChkBox({
            id    : "searchGrp",
            tagId : 'searchGrp',
            async : false,
            chkVal: '',
            style : {},
            data  : [	{ "CODE" : "O" , "NAME":"주문"}
        	, 	{ "CODE" : "C" , "NAME":"클레임"}
            , 	{ "CODE" : "S" , "NAME":"클레임2"}
            , 	{ "CODE" : "R" , "NAME":"환불"}
            , 	{ "CODE" : "P" , "NAME":"프로모션"}
            , 	{ "CODE" : "D" , "NAME":"반품"}
            ]

        });

        // I/F일자 변경가능여부 라디오버튼
        fnTagMkRadio({
        	id    :  'ifDayChangeYn',
        	tagId : 'ifDayChangeYn',
        	chkVal: 'N',
        	data  : [	{ "CODE" : "Y" , "NAME":"예"}
        	, 	{ "CODE" : "N" , "NAME":"아니요"}],
        	style : {},
        	async : false
        });

        // 배송추적 가능여부 라디오버튼
        fnTagMkRadio({
        	id    :  'deliverySearchYn',
        	tagId : 'deliverySearchYn',
        	chkVal: 'N',
        	data  : [	{ "CODE" : "Y" , "NAME":"예"}
        	, 	{ "CODE" : "N" , "NAME":"아니요"}],
        	style : {},
        	async : false

        });
        // 사용여부 라디오버튼
		fnTagMkRadio({
			id    :  'useYn',
			tagId : 'useYn',
			data  : [	{ "CODE" : "Y" , "NAME":"예"}
					, 	{ "CODE" : "N" , "NAME":"아니요"}],
			chkVal: 'Y',
			style : {},
            async : false

		});

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'selectAGrid':
	            //form data binding
        		$('#inputFormAGrid').show();
        		$('#inputFormBGrid').hide();
        		$('#jsonFormAGrid').hide();
	            $('#inputFormAGrid').bindingForm(data, 'rows', true);
	            $("#searchGrp input[type=checkbox][name=searchGrp]").attr("checked", false);

	           	 var searchGrp = data.rows.searchGrp;

	           	 for(var i = 0; i < searchGrp.length; i++) {
	           		 console.log("searchGrp: "+searchGrp.charAt(i));
	           		 if(searchGrp.charAt(i) == "O"){
	           			 $("#searchGrp_0").prop("checked" ,true);
	           		 } else if(searchGrp.charAt(i) == "C"){
	           			$("#searchGrp_1").prop("checked" ,true);
	           		 } else if(searchGrp.charAt(i) == "S"){
	           			$("#searchGrp_2").prop("checked" ,true);
	           		 } else { // "R"
	           			$("#searchGrp_3").prop("checked" ,true);
	           		 }
	           	 }

                fnKendoInputPoup({height:"500px" ,width:"500px",title:{nullMsg :'주문상태 수정'} });
	            break;
			case 'selectBGrid':
				//form data binding
				$('#inputFormBGrid').show();
				$('#inputFormAGrid').hide();
				$('#jsonFormAGrid').hide();
				$('#inputFormBGrid').data("data", data);
				$('#inputFormBGrid').bindingForm(data, 'rows', true);
				fnKendoInputPoup({height:"250px" ,width:"500px",title:{nullMsg :'주문상태유형 수정'} });
				break;
			case 'insertAGrid':
				if(data.messageEnum =="DUPLICATE_DATA"){
                    fnKendoMessage({
                        message : '중복된 데이터가 존재합니다.'
                    });
				} else {
					fnSearchAGrid();
					$('#kendoPopup').data('kendoWindow').close();
	                fnKendoMessage({message : '저장되었습니다.'});
				}
				break;
			case 'insertBGrid':
				if(data.messageEnum =="DUPLICATE_DATA"){
					fnKendoMessage({
						message : '중복된 데이터가 존재합니다.'
					});
				} else {
					fnSearchBGrid();
					$('#kendoPopup').data('kendoWindow').close();
					fnKendoMessage({message : '저장되었습니다.'});
				}
				break;
			case 'updateAGrid':
	            aGridDs.query();
	            fnKendoMessage({message : '수정되었습니다.'});
	            fnClose();
	            break;
			case 'updateBGrid':
				bGridDs.query();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	/** Common New*/
	$scope.fnNewAGrid = function( ){	fnNewAGrid();};
	$scope.fnNewBGrid = function( ){	fnNewBGrid();};
	/** Common Save*/
	$scope.fnSaveAGrid = function(){	 fnSaveAGrid();};
	$scope.fnSaveBGrid = function(){	 fnSaveBGrid();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
