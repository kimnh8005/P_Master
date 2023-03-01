/**-----------------------------------------------------------------------------
 * description 		 : 외주몰 주문상세 리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.16		안지영          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var viewModel , aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	importScript("/js/service/admin/od/order/outmallOrderDetailListGridColumns.js", function (){
		importScript("/js/service/admin/od/order/outmallSearchCommItem.js", function (){
			importScript("/js/service/admin/od/order/outmallOrderDetailListFunctions.js", function (){
				fnInitialize();
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'ursDormHist',
			callback : fnUI
		});

	}

	function fnUI(){
		// Initialize
		fnTranslate();		// 다국어 변환
		fnInitButton();		// Button
		fnInitGrid();	   	// Grid
		fnInitOptionBox();	// Option Box
		fnSearch();
		//fnViewModelInit();
		mutilSearchCommonUtil.default(); // 복수,단수 조건 TAB
		searchCommonUtil.getOptionCheck();
		searchCommonUtil.dateSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave, #fnClear').kendoButton();
	}
	function fnSearch(){

		// TODO : validation 정리

		if (($('#condiType').val() == "USER_NAME" || $('#condiType').val() == "LOGIN_ID") && $("#condiTextareaValue").val() == "") {
            return valueCheck("", "조회 하시고자 하는 검색조건을 선택 후 검색해 주세요.", 'condiType');
        }

        if (($('#condiType').val() == "MOBILE" || $('#condiType').val() == "MAIL") && $("#condiValue").val() == "") {
            return valueCheck("", "조회 하시고자 하는 검색조건을 선택 후 검색해 주세요.", 'condiType');
        }

		 if ($('#startCreateDate').val() == "" && $('#endCreateDate').val() != "") {
            return valueCheck("6495", "시작일 또는 종료일을 입력해주세요.", 'startCreateDate');
        }

        if ($('#startCreateDate').val() != "" && $('#endCreateDate').val() == "") {
            return valueCheck("6495", "시작일 또는 종료일을 입력해주세요.", 'endCreateDate');
        }



		var data;
		data = $('#searchForm').formSerialize(true);
		$('#inputForm').formClear(false);
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

	function valueCheck(key, nullMsg, ID){
		fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
			$('#'+ID).focus();
		}});

		return false;
	}

	function fnClear(){
		$('#searchForm').formClear(true);

		$(".set-btn-type6").attr("fb-btn-active" , false );
        //fnSearchControl();
	}
	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/od/order/getUserDormantHistoryList'
			,pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : outmallOrderGridUtil.outmallOrderDetailList()
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function(){
			let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});

			$("#countTotalSpan").text(aGridDs._total);
		});

		$("#aGrid").on("click", "tbody>tr>td", function () {
			//$('#inputForm').formClear(true);
			var index = $(this).index();
			var type = "";

			if($(this).closest('table').find('th').eq(index).text() == '회원명' || $(this).closest('table').find('th').eq(index).text() == '회원ID'){
				var map = aGrid.dataItem(aGrid.select());

				//정상전환일이 없으면 휴면회원, 있으면 정상회원
				if(map.modifyDate == "-"){
					type = "userDorm";
				}

				fnKendoPopup({
		            id: 'buyerPopup',
		            title: fnGetLangData({ nullMsg: '회원상세' }),
		            param: { "urUserId": map.urUserId,"type": type },
		            src: '#/buyerPopup',
		            width: '1200px',
		            height: '700px',
		            success: function(id, data) {
		                //fnSearch();
		            }
		        });
			}
		});
	}
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		$('#inputForm').bindingForm( {'rows':map},'rows', true);
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	function fnInitOptionBox(){
		searchCommonUtil.getOptionCheck();
	}

	// 검색제어
    function fnSearchControl(){
        let condiValueYn = $("#condiType").val() != "" ? true : false;

        fnSearchConditionTypeControl( $("#condiType").val() );
        //fnSearchControl( condiValueYn );
    };

    //검색구분 제어
    function fnSearchConditionTypeControl(conditionTypeValue){

        // 검색구분이 전체일 경우
        if(conditionTypeValue == ""){
            $("#condiValue, #condiTextareaValue").val("");
            $("#condiValue").css("display", "");
            $("#condiTextareaValue").css("display", "none");
            $("#condiValue, #condiTextareaValue").attr("disabled", true);
        } // 검색구분이 회원ID, 회원명 일 경우
        else if(conditionTypeValue == "USER_NAME" || conditionTypeValue == "LOGIN_ID"){
            $("#condiValue").val("");
            $("#condiValue").css("display", "none");
            $("#condiValue").attr("disabled", true);
            $("#condiTextareaValue").css("display", "");
            $("#condiTextareaValue").attr("disabled", false);
        } // 검색구분이 모바일, 이메일 일 경우

    };


	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'insert':
				aGridDs.insert(data.rows);;
				fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'입력되었습니다.' }) ,ok :fnNew});
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({key :"368",nullMsg :'저장되었습니다.' })});
				break;
			case 'update':
				fnUpdateGrid(data,$("#aGrid"),"rows");
				fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }) });
				break;
			case 'delete':
				aGridDs.remove(data);
				//aGridDs.total = aGridDs.total-1;
				fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' })});
				break;

		}
	}
	//엑셀 다운로드
	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);

        // 표준 카테고리 : 현재 선택된 표준 카테고리 Select 값 중 가장 마지막 값을 조회조건에 추가
        data['ilCategoryStandardId'] = checkCategoryStandardId();

		fnExcelDownload('/admin/item/master/itemExportExcel', data);
	}

//	function fnViewModelInit(){
//        viewModel = new kendo.data.ObservableObject({
//        	fnExcelDownload : function(e){ // 엑셀다운로드
//        		alert("!");
//                e.preventDefault();
//
//                fnKendoMessage({ message : "엑셀 양식화면 개발되야 개발가능." });
//                /*
//                let data = $("#searchForm").formSerialize(true);
//                let searchData = fnSearchData(data);
//
//                fnExcelDownload("/admin/il/goods/goodsListExportExcel", searchData);
//                */
//            },
//        });
//
//        kendo.bind($("#searchForm"), viewModel);
//	}
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
