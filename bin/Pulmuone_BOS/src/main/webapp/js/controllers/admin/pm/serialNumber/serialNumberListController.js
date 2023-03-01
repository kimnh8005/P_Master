﻿'use strict';

//var pageParam = fnGetPageParam();

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;


$(document).ready(function() {

    fnInitialize();

    parent.POP_PARAM["parameter"].pmPointId;

    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : false });

        fnPageInfo({
              PG_ID    : 'serialNumberList'
            , callback : fnUI
        });
    }

	function fnUI(){
//		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}


	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnSave, #fnSelectCancel, #fnExcel').kendoButton();
	}

    //--- 검색  -----------------
    function fnSearch() {
        $('#inputForm').formClear(false);
        var data = $('#searchForm').formSerialize(true);

//        data.parentId = pageParam.parentId;
//        data.useType = pageParam.useType;
        data.parentId = parent.POP_PARAM["parameter"].parentId;
        data.useType = parent.POP_PARAM["parameter"].useType;
        data.serialNumberTp = parent.POP_PARAM["parameter"].serialNumberTp;

        var query = {
                       page         : aGridDs.page()
                     , pageSize     : PAGE_SIZE
                     , filterLength : fnSearchData(data).length
                     , filter :  {
                           filters : fnSearchData(data)
                       }
        };
        aGridDs.query( query );
    }

    //-- 초기화 버튼 -----------------
	function fnClear() {
		$('#searchForm').formClear(true);
	}

	function fnSelectCancel() {

		var selectRows 	= aGrid.tbody.find('input[name=itemGridChk]:checked').closest('tr');
		var cancelArray = new Array();

		if(selectRows.length == 0) {
			fnKendoMessage({message:'이용권을 선택해주세요.'});
			return false;
		}

		for(var i =0; i< selectRows.length;i++){
			var dataRow = aGrid.dataItem($(selectRows[i]));
			cancelArray.push(dataRow);
		}
		fnKendoMessage({message:'선택하신 이용권을 중지 처리 하시겠습니까?', type : "confirm" ,ok : function(){ fnSelectCancelApply(cancelArray) }  });
	}

	function fnSelectCancelApply(cancelArray){
		var url  = '/admin/promotion/serialNumber/putSerialNumberCancel';

		fnAjax({
			url     : url,
			params  : {updateData :kendo.stringify(cancelArray)},
			success :
				function( data ){
					fnBizCallback("update", data);
				},
			isAction : 'update'
		});
	}

	function fnExcel(){
		var data = $('#searchForm').formSerialize(true);
//      data.parentId = pageParam.parentId;
//      data.useType = pageParam.useType;
      data.parentId = parent.POP_PARAM["parameter"].parentId;
      data.useType = parent.POP_PARAM["parameter"].useType;
      data.serialNumberTp = parent.POP_PARAM["parameter"].serialNumberTp;
		fnExcelDownload('/admin/promotion/serialNumber/serialNumberListExportExcel', data);

	}

	//--------------------------------- Button End---------------------------------


	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------
    function fnInitGrid() {

    	var displayName;

    	if(parent.POP_PARAM["parameter"].useType == 'SERIAL_NUMBER_USE_TYPE.COUPON'){
    		displayName = '이용권 내역 | 쿠폰명 : ' + parent.POP_PARAM["parameter"].displayName + '(' + parent.POP_PARAM["parameter"].parentId +')';
    	}else{
    		displayName = '이용권 내역 | 적립금명 : ' + parent.POP_PARAM["parameter"].displayName;
    	}

//    	$('.title').text(displayName);
    	$("#ticketTitle").text(displayName);
        aGridDs = fnGetPagingDataSource({
             url      : "/admin/promotion/serialNumber/getSerialNumberList"
             , pageSize : PAGE_SIZE
             , requestEnd: function(e) {
                // $('#listCount').text(kendo.toString(e.response.total, "n0"));
             }
        });

        aGridOpt = {
              dataSource: aGridDs
            , pageable  : {
                pageSizes   : [20, 30, 50],
                buttonCount : 10
              }
            , navigatable: true
            , columns : [
            	{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />",
            		//template : '<input type="checkbox" name="itemGridChk" class="itemGridChk" />',
            		template : function(dataItem){
            			let returnValue;
            			if(dataItem.status =='SERIAL_NUMBER_STATUS.ISSUED'){
            				returnValue = '<input type="checkbox" name="itemGridChk" class="itemGridChk" />';
            			}else{
            				returnValue = '';
            			}
            			return returnValue;
            		},
            		width:'50px', attributes : { style : "text-align:center;" }}
                    , {field : 'no'        , title : 'No'   , width : '60px' , attributes : {style : 'text-align:center'}, type:"number", template:"<span class='row-number'></span>", type:"number",format: "{0:n0}"}
                    , {field : 'serialNumber' , title : '이용권 번호' , width : '100px' , attributes : {style : 'text-align:center'}
                    	,template : function(dataItem){
                			let returnValue;
                			let serialBackChar = '';
//                			for(var i=0; i < dataItem.serialSize ;i++){
//                				serialBackChar = serialBackChar + '*';
//                			}
//                			returnValue = dataItem.serialFront + serialBackChar +dataItem.serialBack;
                			returnValue = dataItem.serialNumber;
                			return returnValue;
                    	}
                	}
                    , {field : 'createDate' , title : '등록일자' , width : '100px' , attributes : {style : 'text-align:center'}}
                    , {field : 'issuePeriod' , title : '등록가능기간' , width : '220px' , attributes : {style : 'text-align:center'}
                    	, template: kendo.template($("#issuePeriod").html()) }
                    , {field : 'statusName' , title : '사용여부' , width : '80px' , attributes : {style : 'text-align:center'}}
                    , {field : 'loginId' , title : '사용 ID' , width : '100px' , attributes : {style : 'text-align:center'}}
                    , {field : 'userName' , title : '사용 회원명' , width : '100px' , attributes : {style : 'text-align:center'}}
                    , {field : 'useDate' , title : '이용권 등록일시' , width : '100px' , attributes : {style : 'text-align:center'}}
                    ,{ field : 'pmSerialNumberId', hidden:true }
                    , {field : "button"    , title : "관리"       , width : "120px"  , attributes : { style : "text-align:center" }
                    , command : [{ name: "사용중지",
                    	click: function(e) {
                    		e.preventDefault();

                            var tr = $(e.target).closest("tr");
                            var data = this.dataItem(tr);

                        	var cancelArray = new Array();
                        	cancelArray.push(data);

                            fnAjax({
                                  url     : '/admin/promotion/serialNumber/putSerialNumberCancel'
                                //, params  : {"inputPmSerialNumberIdList" : data.pmSerialNumberId}
                        		, params  : {updateData :kendo.stringify(cancelArray)}
                                , success : function (data) {
                                                fnBizCallback("update", data);
                                            }
                                , isAction : 'update'
                            });
                    	}
                    	, visible: function(dataItem) { return dataItem.status =="SERIAL_NUMBER_STATUS.ISSUED"}
                    }] }
              ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind("dataBound", function(){
        	var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#countTotalSpan').text(aGridDs._total);
        });

        $("#checkBoxAll").on("click",function(index){
			if($("#checkBoxAll").prop("checked")==true){
				$('INPUT[name=itemGridChk]').prop("checked",true);

			}else{
				$('INPUT[name=itemGridChk]').prop("checked",false);
			}
		});
	}


    //==================================================================================
    //--------------- Initialize Option Box Start --------------------------------------
    //==================================================================================
    function fnInitOptionBox() {

    	$('#kendoPopup').kendoWindow({
			visible: false
			, modal: true
		});

        //-----------------------------------------------------------------------
        //-- 검색조건 : 사용여부 라디오
        //-----------------------------------------------------------------------
        fnTagMkRadio({
              id    : 'serialNumberUseCondition'
            , tagId : 'serialNumberUseCondition'
            , chkVal: ""
            , data  : [
                         {"CODE" : "" , "NAME" : '전체'     }
                       , {"CODE" : "Y", "NAME" : '사용'     }
                       , {"CODE" : "N", "NAME" : '미사용' }
                      ]
            , style : {}
        });

		//================데이트 피커===============
        fnKendoDatePicker({
			id    : 'startUseDate',
			format: 'yyyy-MM-dd'
		});
		fnKendoDatePicker({
			id    : 'endUseDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startUseDate',
			btnEndId : 'endUseDate'
		});

		// 검색조건 선택
		fnKendoDropDownList({
			id    : 'serialNumberSearchCondition',
			data  : [{"CODE":"SEARCH_SELECT.SERIALNUMBER","NAME":'이용권번호'}
					,{"CODE":"SEARCH_SELECT.USER_ID","NAME":'사용자 ID'}
					,{"CODE":"SEARCH_SELECT.USER_NAME","NAME":'사용 회원명'}
					],
			value : "",
			blank : "선택해주세요"
		});

	}
    //==================================================================================
	//---------------Initialize Option Box End ------------------------------------------------
    //==================================================================================



    //==================================================================================
    //-------------------------------  Common Function start -------------------------------
    //==================================================================================
    function inputFocus() {
        $('#brandName').focus();
    };

    //-------------------------------  콜백합수 -----------------------------
    function fnBizCallback (id, data) {

        switch (id) {
            case 'select':
            	/* popup 오픈 시 데이터 초기화 */
                break;
			case 'insert':
			case 'update':
				fnKendoMessage({
                    message:"저장되었습니다."
                    , ok : function() {
                                  fnSearch();
//                                  fnClose();
                           }
				});
			    break;

            case 'delete':
                fnKendoMessage({  message : '삭제되었습니다.'
                	, ok      : function(){
                		fnSearch();
//                		fnClose();
                	}
                });
                break;
        }
    }

    //-- Alert 메세지
	function fnAlertMessage(msg, id) {
		fnKendoMessage(
			{  message : msg
				, ok      : function focusValue() { $("#" + id).focus(); }
			}
		);
        return false;
    }
    //==================================================================================
    //-------------------------------  Common Function end -----------------------------
    //==================================================================================


    //==================================================================================
	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    //==================================================================================
    $scope.fnSearch     = function () { fnSearch(); };
    $scope.fnClear      = function () { fnClear() ; };
    $scope.fnNew        = function () { fnNew()   ; };
    $scope.fnSave       = function () { fnSave()  ; };
    $scope.fnClose      = function () { fnClose() ; };

    $scope.fnSelectCancel       = function () { fnSelectCancel()  ; };
    $scope.fnExcel      = function () { fnExcel() ; };
    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End ----------------------------
    //==================================================================================

}); // document ready - END
