/**-----------------------------------------------------------------------------
 * description 		 : 부정거래 탐지 조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.06.23		안치열			최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

var pageParam = fnGetPageParam();
//var pmCouponId;
//var displayCouponName;

$(document).ready(function() {
	fnInitialize();
	//Initialize Page Call ---------------------------------


	//Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : true
		});

		fnPageInfo({
			PG_ID : 'illegalDetectLog',
			callback : fnUI
		});

	}

	function fnUI() {

		fnInitButton();
		fnInitGrid();
		fnInitOptionBox();
//		fnSearch();
        fnDefaultSet();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton() {
		$('#fnSearch, #fnClear, #fnList, #fnNormalComplete, #fnAbnormalComplete, #fnExcelExport').kendoButton();

	}

	// 기본 설정
    function fnDefaultSet(){
        $("input[name=searchIllegalStatusType]").eq(0).prop("checked", true).trigger("change");
        setDefaultDatePicker();
         $("#searchIllegalDetailType").data("kendoDropDownList").enable(false);
    };

    function setDefaultDatePicker() {
        $(".date-controller button").each(function() {
            $(this).attr("fb-btn-active", false);
        })

        $("button[data-id='fnDateBtn2']").attr("fb-btn-active", true);

        var today = fnGetToday();

        $("#startCreateDate").val(fnGetDayMinus(today, 1));
        $("#endCreateDate").val(today);
    }

	function fnSearch() {

		var data;
		data = $('#searchForm').formSerialize(true);
		//console.log(data);
		var query = {
			page : 1,
			pageSize : PAGE_SIZE,
			filterLength : fnSearchData(data).length,
			filter : {
				filters : fnSearchData(data)
			}
		};
		aGridDs.query(query);
	}

	function fnClear() {
		$('#searchForm').formClear(true);
		$('#aGrid').gridClear(true);
		fnDefaultSet();
	}


	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid() {
		var data = $('#searchForm').formSerialize(true);

		aGridDs = fnGetPagingDataSource({
			url: '/admin/st/log/getIllegalDetectLogList',
			pageSize : PAGE_SIZE
		});


		aGridOpt = {
				dataSource : aGridDs
				,  pageable  : {
					pageSizes: [5, 20, 30, 50],
					buttonCount : 5
				}
				,navigatable: true
				//,height:550
				,columns : [
					{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />"
					    , template : function(dataItem) {
					            let returnValue = '';
					            return returnValue = '<input type="checkbox" name="illegalDetectChk" class="illegalDetectChk" />';
					    }, width:'60px', attributes : { style : "text-align:center;" }}
					,{ field : 'stIllegalLogId'         , title : 'FDS CODE'        , width:'80px', attributes : { style : "text-align:center;" }, template:"<span class='row-number'></span>"}
					,{ field : 'illegalStatusTypeName'  , title : '상태'             , width:'100px', attributes : { style : "text-align:center;" }}
					,{ field : 'illegalTypeName'        , title : '부정거래 분류'      , width:'150px', format: "{0:n0}", attributes : {  style : "text-align:center;" }}
					,{ field : 'illegalDetailTypeName'  , title : '부정거래 유형'      , width:'250px', attributes : { style : "text-align:center;" }}
					,{ field : 'illegalDetectCmt'       , title : '부정거래 탐지 내용'  , width:'250px', attributes : {  style : "text-align:center; text-decoration: underline;"},
                        template : function(dataItem){
                        	    let returnValue;
                                if(dataItem.illegalDetailType == 'ILLEGAL_DETAIL_TYPE.STOLEN_LOST_CARD' || dataItem.illegalDetailType == 'ILLEGAL_DETAIL_TYPE.TRANSACTION_NOT_CARD'){
                                    returnValue = '<span style="color:red;">' + dataItem.illegalDetectFrontCmt + '</span>' + dataItem.illegalDetectEndCmt;
                                }else if(dataItem.illegalDetailType == 'ILLEGAL_DETAIL_TYPE.ORDER_PRICE'){
                                    returnValue = dataItem.illegalDetectFrontCmt + '<span style="color:red;">'+ priceToString(dataItem.illegalDetect) + '</span>' +dataItem.illegalDetectEndCmt;
                                }else{
                        	        returnValue = dataItem.illegalDetectFrontCmt + '<span style="color:red;">'+dataItem.illegalDetect + '</span>' +dataItem.illegalDetectEndCmt;
                                }

                                return returnValue;
                              }
                     }
					,{ field : 'urPcidCd'               , title : 'Device ID'       , width:'120px', attributes : { style : "text-align:center;"}}
					,{ field : 'loginId'                , title : '회원 ID'          , width:'150px', attributes : {  style : "text-align:center;text-decoration: underline;color:blue;"}}
					,{ field : 'odid'                   , title : '주문번호'          , width:'200px', attributes : {  style : "text-align:center;text-decoration: underline;color:blue;"}}
					,{ field : 'createDt'          , title : '탐지일시'          , width:'120px', attributes : {  style : "text-align:center;"}}
					,{ field : 'urUserId', hidden:true }
					,{ field : 'logindIdGroup', hidden:true }
					,{ field : 'odidGroup', hidden:true }
					]
				};

		aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();

		aGrid.bind("dataBound", function(){
	        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
				$("#aGrid tbody > tr .row-number").each(function(index){
					$(this).html(row_num);
					row_num--;
				});

	        	$('#totalCnt').text(aGridDs._total);
	        });

        $("#checkBoxAll").prop("checked", false);
        // 그리드 전체선택 클릭
		$("#checkBoxAll").on("click",function(index){
			if($("#checkBoxAll").prop("checked")==true){
				$('INPUT[name=illegalDetectChk]').prop("checked",true);
			}else{
				$('INPUT[name=illegalDetectChk]').prop("checked",false);
			}
		});


		// 그리드 체크박스 클릭
        aGrid.element.on("click", "[name=illegalDetectChk]" , function(e){
            if( e.target.checked ){
                if( $("[name=illegalDetectChk]").length == $("[name=illegalDetectChk]:checked").length ){
                    $("#checkBoxAll").prop("checked", true);
                }
            }else{
                $("#checkBoxAll").prop("checked", false);
            }
        });


		$('#aGrid').on('change', ':checkbox', function(e) {
			var data = aGridDs.getByUid($(e.target).closest('tr').data('uid'));
			if ($(this).is(':checked')) {
				data.CHECK_YN = 'Y';
			} else {
				data.CHECK_YN = 'N';
			}
			data.dirty = true;
			//변경여부
		});

		$(aGrid.tbody).on("click", "td", function (e) {
            var dataItem = aGrid.dataItem($(e.target).closest('tr'));
            var row = $(this).closest("tr");
            var rowIdx = $("tr", aGrid.tbody).index(row);
            var colIdx = $("td", row).index(this);
            if(colIdx == 5){
                fnGridClick(e);
            }else if(dataItem.loginId !='-' && colIdx == 7){
                fnUserIdClick(e);
            }else if(dataItem.odid !='-' && colIdx == 8){
                fnOrderClick(e);
            }
        });

	}


    // 정상완료 처리
    function fnNormalComplete(){
        let selectRows  = $("#aGrid").find("input[name=illegalDetectChk]:checked").closest("tr");
        let params = {};
        params.stIllegalLogIdList = [];
        params.illegalStatusType = 'ILLEGAL_STATUS_TYPE.CLEAR';
        if( selectRows.length == 0 ){
            fnKendoMessage({ message : "선택된 부정거래 탐지 항목이 없습니다." });
            return;
        }
        for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
            let dataItem = aGrid.dataItem($(selectRows[i]));
            params.stIllegalLogIdList[i] = dataItem.stIllegalLogId;
        }

        fnKendoMessage({message:'정상완료 상태로 변경하시겠습니까?', type : "confirm" ,ok : function(){ fnCompleteRequest(params) } });

    };


    // 완료처리
    function fnCompleteRequest(params){
        if( fnIsEmpty(params) || fnIsEmpty(params.stIllegalLogIdList) || params.stIllegalLogIdList.length < 1){
            fnKendoMessage({ message : "선택된 부정거래 탐지 항목이 없습니다." });
            return;
        }
        fnAjax({
            url     : "/admin/st/log/putCompleteRequest",
            params  : params,
            contentType : "application/json",
            success : function( data ){
                fnKendoMessage({  message : "저장이 완료되었습니다."
                    , ok : function(){
                        fnSearch();
                    }
                });
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "update"
        });
    };


    // 비정상완료 처리
    function fnAbnormalComplete(){
        let selectRows  = $("#aGrid").find("input[name=illegalDetectChk]:checked").closest("tr");
        let params = {};
        params.stIllegalLogIdList = [];
        params.illegalStatusType = 'ILLEGAL_STATUS_TYPE.ILLEGAL';
        if( selectRows.length == 0 ){
            fnKendoMessage({ message : "선택된 부정거래 탐지 항목이 없습니다." });
            return;
        }
        for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
            let dataItem = aGrid.dataItem($(selectRows[i]));
            params.stIllegalLogIdList[i] = dataItem.stIllegalLogId;
        }

        fnKendoMessage({message:'비정상완료 상태로 변경하시겠습니까?', type : "confirm" ,ok : function(){ fnCompleteRequest(params) } });

    };

    function priceToString(price) {
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

    // ==========================================================================
    // # 엑셀다운로드 로그 등록
    // ==========================================================================
    function fnExcelExport() {
        var formData = $('#searchForm').formSerialize(true);
        var url = '/admin/st/log/illegalDetectListExportExcel';
        fnKendoPopup({
            id      : 'excelDownloadReasonPopup'
          , title   : '엑셀 다운로드 사유'
          , src     : '#/excelDownloadReasonPopup'
          , param   : {excelDownloadType : 'EXCEL_DOWN_TP.ILLEGAL_DETECT'}
          , width   : '700px'
          , height  : '300px'
          , success : function(id, data){
                        if(data == 'EXCEL_DOWN_TP.ILLEGAL_DETECT'){
                          fnExcelDownload(url, formData);
                        }
                      }
        });
    }

    // 부정거래 상세 팝업 호출
	function fnGridClick(e) {
        var dataItem = aGrid.dataItem($(e.target).closest('tr'));
		var sData = $('#searchForm').formSerialize(true);

		fnKendoPopup({
			id     : 'illegalDetectLogPopup',
			title  : '부정거래탐지 상세정보',
			src    : '#/illegalDetectLogPopup',
			param  : {stIllegalLogId :dataItem.stIllegalLogId },
			width  : '800px',
			height : '1000px',
			success: function( id, data ){
					fnSearch();
			}
		});
	};

	// 회원ID 클릭 : 회원검색 새창 호출
	function fnUserIdClick(e){
	    var dataItem = aGrid.dataItem($(e.target).closest('tr'));
		window.open("#/buyer?condiValue="+ dataItem.loginIdGroup +"&condiType=loginId");
	}

	// 주문번호 클릭 : 주문리스트 새창 호출
	function fnOrderClick(e){
	    var dataItem = aGrid.dataItem($(e.target).closest('tr'));
        window.open("#/orderList?codeSearch="+ dataItem.odidGroup +"&selectConditionType=singleSection&searchSingleType=ORDER_ID");
    }
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox() {

        // 부정거래 분류
		fnKendoDropDownList({
            id  : 'searchIillegalType',
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "ILLEGAL_TYPE", "useYn" :"Y"},
            blank : "전체",
            chkVal: "",
        });

        $('#searchIillegalType').unbind('change').on('change', function(){
            var dropDownList =$('#searchIillegalType').data('kendoDropDownList');
            var data = dropDownList.value();
            switch(data){
            case "" :
                $("#searchIllegalDetailType").data("kendoDropDownList").enable(false);
                break;


            case "ILLEGAL_TYPE.USER" :
                $("#searchIllegalDetailType").data("kendoDropDownList").enable(true);
                fnKendoDropDownList({
                            id  : 'searchIllegalDetailType',
                            url : "/admin/comn/getCodeList",
                            params : {"stCommonCodeMasterCode" : "ILLEGAL_DETAIL_TYPE", "useYn" :"Y"},
                            blank : "전체"
                        });
                break;

            case "ILLEGAL_TYPE.ORDER" :
                $("#searchIllegalDetailType").data("kendoDropDownList").enable(true);
                fnKendoDropDownList({
                        id  : 'searchIllegalDetailType',
                        url : "/admin/comn/getCodeList",
                        params : {"stCommonCodeMasterCode" : "ILLEGAL_DETAIL_TYPE_ORDER", "useYn" :"Y"},
                        blank : "전체"
                    });
                break;

            }
		});

        // 부정거래 유형
        fnKendoDropDownList({
            id  : 'searchIllegalDetailType',
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "ILLEGAL_DETAIL_TYPE", "useYn" :"Y"},
            blank : "전체"
        });

        fbCheckboxChange();

        // 진행상태
        fnTagMkChkBox({
            id : "searchIllegalStatusType",
            url : "/admin/comn/getCodeList",
            tagId : "searchIllegalStatusType",
            async : false,
            style : {},
            params : {"stCommonCodeMasterCode" : "ILLEGAL_STATUS_TYPE", "useYn" :"Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

        // 탐지 시작일
		fnKendoDatePicker({
			id: "startCreateDate",
            format: "yyyy-MM-dd",
            btnStartId: "startCreateDate",
            defVal: fnGetDayMinus(fnGetToday(),1),
            btnEndId: "endCreateDate",
            defType : 'yesterday'
		});

		// 탐지 종료일
		fnKendoDatePicker({
			id: "endCreateDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "startCreateDate",
            defVal: fnGetDayMinus(fnGetToday(),1),
            btnEndId: "endCreateDate",
            defType : 'yesterday'
		});

	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	 * 콜백함수
	 */
	function fnBizCallback(id, data) {
		switch(id) {
		case 'update':

			fnKendoMessage({
				message : '저장되었습니다.',
				ok      : function(){ fnSearch();}
			});

			break;

		}
	}

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	$scope.fnSearch = function() {fnSearch();};
	$scope.fnClear = function() { fnClear();};
	$scope.fnNormalComplete = function() {fnNormalComplete(); };
	$scope.fnAbnormalComplete = function() {fnAbnormalComplete(); };
	$scope.fnExcelExport = function() {fnExcelExport();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

});
// document ready - END

