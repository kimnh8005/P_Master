﻿/**-----------------------------------------------------------------------------
 * description 		 : 프로그램관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.08		안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var psShippingPatternId;
var newPsShippingPatternId;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------
	$('#psShippingPatternId').val(parent.POP_PARAM["parameter"].psShippingPatternId);

	psShippingPatternId = $('#psShippingPatternId').val();

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'deliveryPatternMgm',
			callback : fnUI
		});

		//배송패턴 설정요일 검색조건
		$('#ng-view').on("click", "#selectWeek_0" ,function(index){
			if($("#selectWeek_0").prop("checked")==true)
				$('input[name^=selectWeek]').prop("checked",true);
			else
				$('input[name^=selectWeek]').prop("checked",false);
		});

		$('#ng-view').on("click", "#selectWeek_1" ,function(index){
			if($("#selectWeek_0").prop("checked")==false && $("input[name='selectWeek']:checked").length == 7){
				$("#selectWeek_0").prop("checked",true);
			}else if($("#selectWeek_0").prop("checked")==true &&  $("input[name='selectWeek']:checked").length < 8){
				$("#selectWeek_0").prop("checked",false);
			}
		});

		$('#ng-view').on("click", "#selectWeek_2" ,function(index){
			if($("#selectWeek_0").prop("checked")==false && $("input[name='selectWeek']:checked").length == 7){
				$("#selectWeek_0").prop("checked",true);
			}else if($("#selectWeek_0").prop("checked")==true &&  $("input[name='selectWeek']:checked").length < 8){
				$("#selectWeek_0").prop("checked",false);
			}
		});

		$('#ng-view').on("click", "#selectWeek_3" ,function(index){
			if($("#selectWeek_0").prop("checked")==false && $("input[name='selectWeek']:checked").length == 7){
				$("#selectWeek_0").prop("checked",true);
			}else if($("#selectWeek_0").prop("checked")==true &&  $("input[name='selectWeek']:checked").length < 8){
				$("#selectWeek_0").prop("checked",false);
			}
		});

		$('#ng-view').on("click", "#selectWeek_4" ,function(index){
			if($("#selectWeek_0").prop("checked")==false && $("input[name='selectWeek']:checked").length == 7){
				$("#selectWeek_0").prop("checked",true);
			}else if($("#selectWeek_0").prop("checked")==true &&  $("input[name='selectWeek']:checked").length < 8){
				$("#selectWeek_0").prop("checked",false);
			}
		});

		$('#ng-view').on("click", "#selectWeek_5" ,function(index){
			if($("#selectWeek_0").prop("checked")==false && $("input[name='selectWeek']:checked").length == 7){
				$("#selectWeek_0").prop("checked",true);
			}else if($("#selectWeek_0").prop("checked")==true &&  $("input[name='selectWeek']:checked").length < 8){
				$("#selectWeek_0").prop("checked",false);
			}
		});

		$('#ng-view').on("click", "#selectWeek_6" ,function(index){
			if($("#selectWeek_0").prop("checked")==false && $("input[name='selectWeek']:checked").length == 7){
				$("#selectWeek_0").prop("checked",true);
			}else if($("#selectWeek_0").prop("checked")==true &&  $("input[name='selectWeek']:checked").length < 8){
				$("#selectWeek_0").prop("checked",false);
			}
		});

		$('#ng-view').on("click", "#selectWeek_7" ,function(index){
			if($("#selectWeek_0").prop("checked")==false && $("input[name='selectWeek']:checked").length == 7){
				$("#selectWeek_0").prop("checked",true);
			}else if($("#selectWeek_0").prop("checked")==true &&  $("input[name='selectWeek']:checked").length < 8){
				$("#selectWeek_0").prop("checked",false);
			}
		});


		//배송패턴 등록 설정요일 설정
		$('#ng-view').on("click", "#checkAllWeek" ,function(index){
			if($("#checkAllWeek").prop("checked")==true){
				$('input[name^=check]').prop("checked",true);

				$("#warehouseMon").data("kendoDropDownList").enable(true);
				$("#warehouseTue").data("kendoDropDownList").enable(true);
				$("#warehouseWed").data("kendoDropDownList").enable(true);
				$("#warehouseThu").data("kendoDropDownList").enable(true);
				$("#warehouseFri").data("kendoDropDownList").enable(true);
				$("#warehouseSat").data("kendoDropDownList").enable(true);
				$("#warehouseSun").data("kendoDropDownList").enable(true);

				$("#arrivedMon").data("kendoDropDownList").enable(true);
				$("#arrivedTue").data("kendoDropDownList").enable(true);
				$("#arrivedWed").data("kendoDropDownList").enable(true);
				$("#arrivedThu").data("kendoDropDownList").enable(true);
				$("#arrivedFri").data("kendoDropDownList").enable(true);
				$("#arrivedSat").data("kendoDropDownList").enable(true);
				$("#arrivedSun").data("kendoDropDownList").enable(true);

			}else{
				$('input[name^=check]').prop("checked",false);

				$("#warehouseMon").data("kendoDropDownList").enable(false);
				$("#warehouseTue").data("kendoDropDownList").enable(false);
				$("#warehouseWed").data("kendoDropDownList").enable(false);
				$("#warehouseThu").data("kendoDropDownList").enable(false);
				$("#warehouseFri").data("kendoDropDownList").enable(false);
				$("#warehouseSat").data("kendoDropDownList").enable(false);
				$("#warehouseSun").data("kendoDropDownList").enable(false);

				$("#arrivedMon").data("kendoDropDownList").enable(false);
				$("#arrivedTue").data("kendoDropDownList").enable(false);
				$("#arrivedWed").data("kendoDropDownList").enable(false);
				$("#arrivedThu").data("kendoDropDownList").enable(false);
				$("#arrivedFri").data("kendoDropDownList").enable(false);
				$("#arrivedSat").data("kendoDropDownList").enable(false);
				$("#arrivedSun").data("kendoDropDownList").enable(false);
			}
		});

		$('#ng-view').on("click", "#checkMon" ,function(index){
			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkMon").prop("checked")==true){
				$("#warehouseMon").data("kendoDropDownList").enable(true);
				$("#arrivedMon").data("kendoDropDownList").enable(true);
			}else{
				$("#warehouseMon").data("kendoDropDownList").enable(false);
				$("#arrivedMon").data("kendoDropDownList").enable(false);
				$("#warehouseMon").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_0');
				$("#arrivedMon").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_1');
			}
		});

		$('#ng-view').on("click", "#checkTue" ,function(index){
			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkTue").prop("checked")==true){
				$("#warehouseTue").data("kendoDropDownList").enable(true);
				$("#arrivedTue").data("kendoDropDownList").enable(true);
			}else{
				$("#warehouseTue").data("kendoDropDownList").enable(false);
				$("#arrivedTue").data("kendoDropDownList").enable(false);
				$("#warehouseTue").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_0');
				$("#arrivedTue").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_1');
			}
		});

		$('#ng-view').on("click", "#checkWed" ,function(index){
			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkWed").prop("checked")==true){
				$("#warehouseWed").data("kendoDropDownList").enable(true);
				$("#arrivedWed").data("kendoDropDownList").enable(true);
			}else{
				$("#warehouseWed").data("kendoDropDownList").enable(false);
				$("#arrivedWed").data("kendoDropDownList").enable(false);
				$("#warehouseWed").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_0');
				$("#arrivedWed").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_1');
			}
		});

		$('#ng-view').on("click", "#checkThu" ,function(index){
			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkThu").prop("checked")==true){
				$("#warehouseThu").data("kendoDropDownList").enable(true);
				$("#arrivedThu").data("kendoDropDownList").enable(true);
			}else{
				$("#warehouseThu").data("kendoDropDownList").enable(false);
				$("#arrivedThu").data("kendoDropDownList").enable(false);
				$("#warehouseThu").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_0');
				$("#arrivedThu").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_1');
			}
		});

		$('#ng-view').on("click", "#checkFri" ,function(index){
			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkFri").prop("checked")==true){
				$("#warehouseFri").data("kendoDropDownList").enable(true);
				$("#arrivedFri").data("kendoDropDownList").enable(true);
			}else{
				$("#warehouseFri").data("kendoDropDownList").enable(false);
				$("#arrivedFri").data("kendoDropDownList").enable(false);
				$("#warehouseFri").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_0');
				$("#arrivedFri").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_1');
			}
		});

		$('#ng-view').on("click", "#checkSat" ,function(index){
			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkSat").prop("checked")==true){
				$("#warehouseSat").data("kendoDropDownList").enable(true);
				$("#arrivedSat").data("kendoDropDownList").enable(true);
			}else{
				$("#warehouseSat").data("kendoDropDownList").enable(false);
				$("#arrivedSat").data("kendoDropDownList").enable(false);
				$("##warehouseSat").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_0');
				$("#arrivedSat").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_1');
			}
		});

		$('#ng-view').on("click", "#checkSun" ,function(index){
			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkSun").prop("checked")==true){
				$("#warehouseSun").data("kendoDropDownList").enable(true);
				$("#arrivedSun").data("kendoDropDownList").enable(true);
			}else{
				$("#warehouseSun").data("kendoDropDownList").enable(false);
				$("#arrivedSun").data("kendoDropDownList").enable(false);
				$("##warehouseSun").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_0');
				$("#arrivedSun").data("kendoDropDownList").value('ARRIVED_SCHEDULE.DAY_1');
			}
		});

	}

	function checkSearchWeek(){
		$('input[name=selectWeek]').prop("checked",true);

	}

	function fnInitSelectBox(){
		$("#warehouseMon").data("kendoDropDownList").enable(false);
		$("#warehouseTue").data("kendoDropDownList").enable(false);
		$("#warehouseWed").data("kendoDropDownList").enable(false);
		$("#warehouseThu").data("kendoDropDownList").enable(false);
		$("#warehouseFri").data("kendoDropDownList").enable(false);
		$("#warehouseSat").data("kendoDropDownList").enable(false);
		$("#warehouseSun").data("kendoDropDownList").enable(false);

		$("#arrivedMon").data("kendoDropDownList").enable(false);
		$("#arrivedTue").data("kendoDropDownList").enable(false);
		$("#arrivedWed").data("kendoDropDownList").enable(false);
		$("#arrivedThu").data("kendoDropDownList").enable(false);
		$("#arrivedFri").data("kendoDropDownList").enable(false);
		$("#arrivedSat").data("kendoDropDownList").enable(false);
		$("#arrivedSun").data("kendoDropDownList").enable(false);

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnInitSelectBox();

		checkSearchWeek();

		fnSearch();
	}



	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSaveNew, #fnClear, #fnClose, #fnDeliveryPatternPopupButton,#fnShippingPatternView, #fnChangeWarehouseDate, #fnChangeArrivedDate, #fnChangeShippingPattern').kendoButton();
	}
	function fnSearch(){

		var checkCount = $("input:checkbox[name^=selectWeek]:checked").length;

		if(checkCount == 0){
			fnKendoMessage({message:'출고지시일을 선택해 주세요.'});
			return
		}

		$('#inputForm').formClear(false);
		var data;

		data = $('#searchForm').formSerialize(true);

		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );
	}
	function fnClear(){
		$('#searchForm').formClear(true);
		checkSearchWeek();
	}
	function fnNew(){
		$('#inputForm').formClear(true);

		$('#arrivedMon').data('kendoDropDownList').value('ARRIVED_SCHEDULE.DAY_1');
		$('#arrivedTue').data('kendoDropDownList').value('ARRIVED_SCHEDULE.DAY_1');
		$('#arrivedWed').data('kendoDropDownList').value('ARRIVED_SCHEDULE.DAY_1');
		$('#arrivedThu').data('kendoDropDownList').value('ARRIVED_SCHEDULE.DAY_1');
		$('#arrivedFri').data('kendoDropDownList').value('ARRIVED_SCHEDULE.DAY_1');
		$('#arrivedSat').data('kendoDropDownList').value('ARRIVED_SCHEDULE.DAY_1');
		$('#arrivedSun').data('kendoDropDownList').value('ARRIVED_SCHEDULE.DAY_1');

		fnInitSelectBox();

		$('#createInfoDiv').hide();

		fnKendoInputPoup({height:"500px" ,width:"1400px", title:{ nullMsg :'배송패턴 설정' } } );
	}

	// 선택 후 배송패턴 변경 진행
	function fnChangeShippingPattern(psShippingPattern){

		var grid = $("#aGrid").data("kendoGrid");

		var data = grid.dataSource;

		var dataCheck = data._data;
		var patternTitle;
    	for(var i=0; i<dataCheck.length; i++){
    		if(dataCheck[i].psShippingPatternId == psShippingPattern){
    			patternTitle = dataCheck[i].title;
    			break;
    		}

    	}

    	var array = [psShippingPattern, patternTitle];

		fnKendoMessage({message:'배송패턴을 변경하시겠습니까?', type : "confirm" ,ok : function(){ fnChangeClose(array) }  });
	}


	function fnChangeClose(param){
		if(param){
			parent.POP_PARAM = param;
		}
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();

	}


	function fnSaveNew(psShippingPattern){
		fnKendoMessage({message:'배송패턴을 변경하시겠습니까?', type : "confirm" ,ok : function(){ fnSave('SC') } ,cancel: function(){fnCancel('S')} });

	}

	//신규 배송패턴 저장
	function fnCancel(param){
		fnSave(param);
	}

	//신규 배송패턴 저장 후 배송패턴 변경
	function fnSaveChange(param){
		//fnSave();
		fnChangeClose(param);
	}

	function fnSave(param){

		var url  = '/admin/ur/warehouse/addDeliveryPattern';
		var cbId = 'insert';

		var checkCount = $("input:checkbox[name^=check]:checked").length;

		if(checkCount == 0){
			fnKendoMessage({message:'출고지시일을 선택해 주세요.'});
			return
		}

		if($("#checkMon").prop("checked")==true){
			$("#checkMon").val('Y');
		}

		if($("#checkTue").prop("checked")==true){
			$("#checkTue").val('Y');
		}

		if($("#checkWed").prop("checked")==true){
			$("#checkWed").val('Y');
		}

		if($("#checkThu").prop("checked")==true){
			$("#checkThu").val('Y');
		}

		if($("#checkFri").prop("checked")==true){
			$("#checkFri").val('Y');
		}

		if($("#checkSat").prop("checked")==true){
			$("#checkSat").val('Y');
		}

		if($("#checkSun").prop("checked")==true){
			$("#checkSun").val('Y');
		}

		var data = $('#inputForm').formSerialize(true);


		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);

						if(param == 'S'){
							fnClose();
							fnSearch();
						}else{
							var dataArray = [data.rows[0].psShippingPatternId, data.rows[0].title];
							fnSaveChange(dataArray);
						}
					},
					isAction : 'batch'
			});
		}else{

		}
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//출고예정일 일괄변경
	function fnChangeWarehouseDate(){

		if($("#checkMon").prop("checked")==false &&
			$("#checkTue").prop("checked")==false &&
			$("#checkWed").prop("checked")==false &&
			$("#checkThu").prop("checked")==false &&
			$("#checkFri").prop("checked")==false &&
			$("#checkSat").prop("checked")==false &&
			$("#checkSun").prop("checked")==false
			){
			fnKendoMessage({message:'출고지시 요일을 선택 후 변경바랍니다.'});
			return;
		}

		var selectWarehouseDate = $('#warehouseSchedule').val();
		if($("#checkMon").prop("checked")==true){
			$('#warehouseMon').data('kendoDropDownList').value(selectWarehouseDate);
		}
		if($("#checkTue").prop("checked")==true){
			$('#warehouseTue').data('kendoDropDownList').value(selectWarehouseDate);
		}
		if($("#checkWed").prop("checked")==true){
			$('#warehouseWed').data('kendoDropDownList').value(selectWarehouseDate);
		}
		if($("#checkThu").prop("checked")==true){
			$('#warehouseThu').data('kendoDropDownList').value(selectWarehouseDate);
		}
		if($("#checkFri").prop("checked")==true){
			$('#warehouseFri').data('kendoDropDownList').value(selectWarehouseDate);
		}
		if($("#checkSat").prop("checked")==true){
			$('#warehouseSat').data('kendoDropDownList').value(selectWarehouseDate);
		}
		if($("#checkSun").prop("checked")==true){
			$('#warehouseSun').data('kendoDropDownList').value(selectWarehouseDate);
		}

	}


	//도착예정일 일괄변경
	function fnChangeArrivedDate(){

		if($("#checkMon").prop("checked")==false &&
				$("#checkTue").prop("checked")==false &&
				$("#checkWed").prop("checked")==false &&
				$("#checkThu").prop("checked")==false &&
				$("#checkFri").prop("checked")==false &&
				$("#checkSat").prop("checked")==false &&
				$("#checkSun").prop("checked")==false
				){
				fnKendoMessage({message:'출고지시 요일을 선택 후 변경바랍니다.'});
				return;
			}

		var selectArrivedDate = $('#arrivedSchedule').val();

		if($("#checkMon").prop("checked")==true){
			$('#arrivedMon').data('kendoDropDownList').value(selectArrivedDate);
		}
		if($("#checkMon").prop("checked")==true){
			$('#arrivedMon').data('kendoDropDownList').value(selectArrivedDate);
		}
		if($("#checkTue").prop("checked")==true){
			$('#arrivedTue').data('kendoDropDownList').value(selectArrivedDate);
		}
		if($("#checkWed").prop("checked")==true){
			$('#arrivedWed').data('kendoDropDownList').value(selectArrivedDate);
		}
		if($("#checkThu").prop("checked")==true){
			$('#arrivedThu').data('kendoDropDownList').value(selectArrivedDate);
		}
		if($("#checkFri").prop("checked")==true){
			$('#arrivedFri').data('kendoDropDownList').value(selectArrivedDate);
		}
		if($("#checkSat").prop("checked")==true){
			$('#arrivedSat').data('kendoDropDownList').value(selectArrivedDate);
		}
		if($("#checkSun").prop("checked")==true){
			$('#arrivedSun').data('kendoDropDownList').value(selectArrivedDate);
		}
	}

	//--------------------------------- Button End---------------------------------





	//------------------------------- Grid Start -------------------------------$scope.fnUpdate(
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			//url      : "/admin/ur/warehouse/getWarehouseList",#=psShippingPatternId#'+'
			url      : "/admin/ur/warehouse/getDeliveryPatternList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			, height: 500
			,scrollable: true
			,rowTemplate:
				  '<tr> '+
				    '<td rowspan=2 style="text-align:center"><span class="row-number"></span></td> '+
				    '<td rowspan=2 style="text-align:center">#=title#</td> '+
				    '<td style="text-align:center"><strong>출고예정일</strong></td> '+
				    '<td style="text-align:center"><strong>#=forwardMonDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=forwardTueDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=forwardWedDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=forwardThuDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=forwardFriDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=forwardSatDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=forwardSunDay#</strong></td> '+
				    '<td style="text-align:center" rowspan=2><button class="fb-btn btn-s btn-white" onclick=$scope.fnChangeShippingPattern(#=psShippingPatternId#) >선택 </button></td> '+
				  '</tr>' +
				  '<tr>' +
				    '<td style="text-align:center"><strong>도착예정일</strong></td> '+
				    '<td style="text-align:center"><strong>#=arrivedMonDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=arrivedTueDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=arrivedWedDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=arrivedThuDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=arrivedFriDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=arrivedSatDay#</strong></td> '+
				    '<td style="text-align:center"><strong>#=arrivedSunDay#</strong></td> '+
				  '</tr>'
			,navigatable: true
			,columns   : [
				 {title : 'No'	, width:'100px', height : '50px',attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				,{title : '배송패턴명'	, width:'170px',attributes:{ style:'text-align:left' }}
				,{title : '구분'	, width:'150px',attributes:{ style:'text-align:left' }}
				,{title : "출고지시일 (I/F일자)",
				 columns: [{title : '월'	, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '화'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '수'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '목'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '금'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '토'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '일'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
					]}
				,{ title : '관리'		, width:'100px',attributes:{ style:'text-align:center' }}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
			$("#aGrid").on("click", "tbody>tr", function () {
				//fnGridClick();
		});

		aGrid.bind("dataBound", function() {

			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});


            $('#totalCnt').text(aGridDs._total);

            this.element.find(".show").bind("click", function() {
			    alert("Clicked");
            });
        });


	}

	function fnShippingPatternView(psShippingPatternId){

		$('#inputForm').formClear(true);

		fnAjax({
			url     : '/admin/ur/warehouse/getShippingPattern',
			params  : {psShippingPatternId : psShippingPatternId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	}


/*	function fnGridClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/ur/warehouse/getDeliveryPatternList',
			params  : {psShippingPatternId : aMap.psShippingPatternId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};*/



	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 설정요일
		fnTagMkChkBox({
			id    : "selectWeek",
		/*	url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "WEEK_CD", "useYn" :"Y"},*/
			data  : [
				{ "CODE" : "WEEK_CD.ALL" , "NAME":"전체"},
				{ "CODE" : "WEEK_CD.MON" , "NAME":"월"},
				{ "CODE" : "WEEK_CD.TUE" , "NAME":"화"},
				{ "CODE" : "WEEK_CD.WED" , "NAME":"수"},
				{ "CODE" : "WEEK_CD.THU" , "NAME":"목"},
				{ "CODE" : "WEEK_CD.FRI" , "NAME":"금"},
				{ "CODE" : "WEEK_CD.SAT" , "NAME":"토"},
				{ "CODE" : "WEEK_CD.SUN" , "NAME":"일"}
				],
			tagId : 'selectWeek',
            style : {}
		});

		//판매/전시 > 구매 유형 > 속성 추가
		$("input[name=selectWeek]").each(function() {
			$('input[name=selectWeek]').prop("checked",true);
		});


		fnKendoDropDownList({
			id    : "warehouseSchedule",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode',
			blank : "선택해주세요"

		});


		fnKendoDropDownList({
			id    : "warehouseMon",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'

		});

		fnKendoDropDownList({
			id    : "warehouseTue",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "warehouseWed",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "warehouseThu",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "warehouseFri",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "warehouseSat",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "warehouseSun",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});


		fnKendoDropDownList({
			id    : "arrivedSchedule",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode',
			blank : "선택해주세요"

		});

		fnKendoDropDownList({
			id    : "arrivedMon",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'

		});

		fnKendoDropDownList({
			id    : "arrivedTue",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "arrivedWed",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "arrivedThu",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "arrivedFri",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "arrivedSat",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
		});

		fnKendoDropDownList({
			id    : "arrivedSun",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			tagId : 'warehouseGroupCode'
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

				fnKendoInputPoup({height:"500px" ,width:"1400px", title:{ nullMsg :'배송패턴 설정' } } );


				$('#createInfoDiv').show();

				if(data.rows.warehouseMon != null){
					$("#checkMon").prop("checked",true);
					$("#warehouseMon").data("kendoDropDownList").enable(true);
					$("#arrivedMon").data("kendoDropDownList").enable(true);
				}else{
					$("#warehouseMon").data("kendoDropDownList").enable(false);
					$("#arrivedMon").data("kendoDropDownList").enable(false);
					data.rows.warehouseMon = 'ARRIVED_SCHEDULE.DAY_0';
					data.rows.arrivedMon = 'ARRIVED_SCHEDULE.DAY_1';
				}

				if(data.rows.warehouseTue != null){
					$("#checkTue").prop("checked",true);
					$("#warehouseTue").data("kendoDropDownList").enable(true);
					$("#arrivedTue").data("kendoDropDownList").enable(true);
				}else{
					$("#warehouseTue").data("kendoDropDownList").enable(false);
					$("#arrivedTue").data("kendoDropDownList").enable(false);
					data.rows.warehouseTue = 'ARRIVED_SCHEDULE.DAY_0';
					data.rows.arrivedTue = 'ARRIVED_SCHEDULE.DAY_1';
				}

				if(data.rows.warehouseWed != null){
					$("#checkWed").prop("checked",true);
					$("#warehouseWed").data("kendoDropDownList").enable(true);
					$("#arrivedWed").data("kendoDropDownList").enable(true);
				}else{
					$("#warehouseWed").data("kendoDropDownList").enable(false);
					$("#arrivedWed").data("kendoDropDownList").enable(false);
					data.rows.warehouseWed = 'ARRIVED_SCHEDULE.DAY_0';
					data.rows.arrivedWed = 'ARRIVED_SCHEDULE.DAY_1';
				}

				if(data.rows.warehouseThu != null){
					$("#checkThu").prop("checked",true);
					$("#warehouseThu").data("kendoDropDownList").enable(true);
					$("#arrivedThu").data("kendoDropDownList").enable(true);
				}else{
					$("#warehouseThu").data("kendoDropDownList").enable(false);
					$("#arrivedThu").data("kendoDropDownList").enable(false);
					data.rows.warehouseThu = 'ARRIVED_SCHEDULE.DAY_0';
					data.rows.arrivedThu = 'ARRIVED_SCHEDULE.DAY_1';
				}

				if(data.rows.warehouseFri != null){
					$("#checkFri").prop("checked",true);
					$("#warehouseFri").data("kendoDropDownList").enable(true);
					$("#arrivedFri").data("kendoDropDownList").enable(true);
				}else{
					$("#warehouseFri").data("kendoDropDownList").enable(false);
					$("#arrivedFri").data("kendoDropDownList").enable(false);
					data.rows.warehouseFri = 'ARRIVED_SCHEDULE.DAY_0';
					data.rows.arrivedFri = 'ARRIVED_SCHEDULE.DAY_1';
				}

				if(data.rows.warehouseSat != null){
					$("#checkSat").prop("checked",true);
					$("#warehouseSat").data("kendoDropDownList").enable(true);
					$("#arrivedSat").data("kendoDropDownList").enable(true);
				}else{
					$("#warehouseSat").data("kendoDropDownList").enable(false);
					$("#arrivedSat").data("kendoDropDownList").enable(false);
					data.rows.warehouseSat = 'ARRIVED_SCHEDULE.DAY_0';
					data.rows.arrivedSat = 'ARRIVED_SCHEDULE.DAY_1';
				}

				if(data.rows.warehouseSun != null){
					$("#checkSun").prop("checked",true);
					$("#warehouseSun").data("kendoDropDownList").enable(true);
					$("#arrivedSun").data("kendoDropDownList").enable(true);
				}else{
					$("#warehouseSun").data("kendoDropDownList").enable(false);
					$("#arrivedSun").data("kendoDropDownList").enable(false);
					data.rows.warehouseSun = 'ARRIVED_SCHEDULE.DAY_0';
					data.rows.arrivedSun = 'ARRIVED_SCHEDULE.DAY_1';
				}


				$('#inputForm').bindingForm(data, "rows", true);


				break;
			case 'insert':
				fnKendoMessage({
						message:"저장되었습니다.",
						ok:function(){
							fnSearch();
							fnClose();
						}
				});
			break;

		}
	}



	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSaveNew = function(){	 fnSaveNew();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnDeliveryPatternPopupButton = function( ){  fnDeliveryPatternPopupButton();};

	$scope.fnChangeWarehouseDate = function( ){  fnChangeWarehouseDate();};

	$scope.fnChangeArrivedDate = function( ){  fnChangeArrivedDate();};

	$scope.fnShippingPatternView = function( event){  fnShippingPatternView(event);};

	$scope.fnChangeShippingPattern = function( event){  fnChangeShippingPattern(event);};


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
