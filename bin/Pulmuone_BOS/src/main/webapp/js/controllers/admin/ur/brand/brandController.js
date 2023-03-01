﻿/**-----------------------------------------------------------------------------
 * description 		 : 브랜드 목록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.10     신성훈          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var itemArray = [];

$(document).ready(function() {

    fnInitialize();


    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : 'true' });

        fnPageInfo({
              PG_ID    : 'brand'
            , callback : fnUI
        });
    }

    function fnUI() {
		fnInitButton();	  // Initialize Button
		fnInitGrid()  ;	  // Initialize Grid
		fnInitOptionBox();// Initialize Option Box
		fnSearch();
	}


	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnNew,#fnSave, #fnClear, #fnClose').kendoButton();
	}

    //--- 검색  -----------------
    function fnSearch() {
        $('#inputForm').formClear(false);
        var data = $('#searchForm').formSerialize(true);
        var query = {
                       page         : 1
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
		$("span#searchUseYn input:radio").eq(0).click();
	}

    //-- 추가 팝업창
    function fnNew() {
        $('#inputForm').formClear(true);

        fnInputFormClear();
        $("#urSupplierId").data("kendoDropDownList").readonly(false);

        inputFocus();
        fnKendoInputPoup({height:"180px" ,width:"1000px", title:{ nullMsg :'표준 브랜드 등록' } });
    }

    //--- 추가/수정하기 전에 입력값 검증 -----------------
    function fnCheckBeforeSave() {
    	var urSupplierId = $("#urSupplierId").val();
        var brandName    = $.trim($("#brandName").val()).replace(/\n/g, "");
        var useYn        = $(':radio[name="useYn"]:checked').val();

        if ( urSupplierId == "") {
            $("#urSupplierId").val("");
            return fnAlertMessage("공급업체를 선택하세요.", "urSupplierId");
        }

        if ( brandName == "") {
            $("#brandName").val("");
            return fnAlertMessage("표준 브랜드명을 입력하세요.", "brandName");
        }

        if ( useYn == "") {
            return fnAlertMessage("사용여부를 선택하세요.", "useYn");
        }
        return true;
    }


	function fnClose() {
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
		fnInputFormClear();
	}
	//--------------------------------- Button End---------------------------------


	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------
    function fnInitGrid() {
        aGridDs = fnGetPagingDataSource({
              url      : "/admin/ur/brand/getBrandList"
            , pageSize : PAGE_SIZE
        });

        aGridOpt = {
              dataSource: aGridDs
            , pageable  : {
                  pageSizes   : [20, 30, 50]
                , buttonCount : 10
              }
            , navigatable : true
            , columns : [
                    {field : 'no'        , title : 'No'         , width : '50px' , attributes : {style : 'text-align:center'}, template: "<span class='row-number'></span>"}
                  , {field : 'urBrandId' , title : '표준 브랜드 코드' , width : '60px' , attributes : {style : 'text-align:center'}}
                  , {field : 'brandName'   , title : '표준 브랜드 명'   , width : '200px' , attributes : {style : 'text-align:left'  }}
                  , {field : 'supplierName', title : '공급업체'   , width : '150px' , attributes : {style : 'text-align:center'}}
                  , {field : 'useYn'       , title : '사용여부'   , width : '50px'  , attributes : {style : 'text-align:center'}}
                  , {field : 'createDate'  , title : '등록일자'   , width : '50px'  , attributes : {style : 'text-align:center'}}
                  ,{ field:'', hidden:true}
                  ,   { command: [{ text: '수정',
  									click: function(e) {

  						            e.preventDefault();
  						            var tr = $(e.target).closest("tr"); // get the current table row (tr)
  						            var data = this.dataItem(tr);

  						            fnGetBrand(data.urBrandId);
  						        }, visible: function() { return fnIsProgramAuth("SAVE")}}]
  					, title: '관리', width: "50px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly #:#' }}   // EXECUTE_TYPE 별 버튼 제어 처리 확인 필요

              ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind("dataBound", function() {
			//row number
			var row_number = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(){
				$(this).html(row_number);
				row_number--;
			});

			//total count
            $('#totalCnt').text(aGridDs._total);
        });

	}

    function fnGetBrand(urBrandId){
    	fnAjax({
            url     : '/admin/ur/brand/getBrand'
    	  , params  : {"urBrandId" : urBrandId}
          , success : function (data) {
                          fnBizCallback("select", data);
                      }
          , isAction : 'select'
      });
    }

    //==================================================================================
	//-------------------------------  Grid End  -------------------------------
    //==================================================================================



    //==================================================================================
    //--------------- Initialize Option Box Start --------------------------------------
    //==================================================================================
    function fnInitOptionBox() {
        $('#kendoPopup').kendoWindow({
              visible: false
            , modal  : true
        });

        //-----------------------------------------------------------------------
        //-- Initializing -> 검색조건 : 브랜드 콤보상자
        //-----------------------------------------------------------------------
        fnKendoDropDownList({
              id   : 'brandSearchType'
            , data : [
                        {"CODE":"BRAND_NAME", "NAME":'표준 브랜드명'   }
                      , {"CODE":"BRAND_CODE", "NAME":'표준 브랜드 코드'}
                     ]
            , textField  : "NAME"
            , valueField : "CODE"
        });

        //-----------------------------------------------------------------------
        //-- Initializing -> 검색조건 : 사용여부 라디오
        //-----------------------------------------------------------------------
        fnTagMkRadio({
              id    : 'searchUseYn'
            , tagId : 'searchUseYn'
            , data  : [
                         {"CODE" : "" , "NAME" : '전체'     }
                       , {"CODE" : "Y", "NAME" : '사용'     }
                       , {"CODE" : "N", "NAME" : '사용안함' }
                      ]
            , chkVal: ""
            , style : {}
        });

        //-----------------------------------------------------------------------
        //-- Initializing -> 검색조건 : 공급업체 콤보 목록
        //-----------------------------------------------------------------------
        // 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'searchUrSupplierId',
			url : "/admin/ur/urCompany/getSupplierCompanyList",
			params : {},
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "전체",

		});


        //-----------------------------------------------------------------------
        //-- Initializing -> 추가/수정 레이어 팝업창 : 사용여부 라디오
        //-----------------------------------------------------------------------
        fnTagMkRadio({
            id    : 'useYn'
          , tagId : 'useYn'
          , data  : [
                       {"CODE" : "Y", "NAME" : '사용'     }
                     , {"CODE" : "N", "NAME" : '사용안함' }
                    ]
          , chkVal: "Y"
          , style : {}
        });

        //-----------------------------------------------------------------------
        //-- Initializing -> 추가/수정 레이어 팝업 : 공급업체 콤보 목록
        //-----------------------------------------------------------------------
        fnKendoDropDownList({
			id    : 'urSupplierId',
			url : "/admin/ur/urCompany/getSupplierCompanyList",
			params : {},
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "선택해주세요."
		});

        // 드롭다운 리스트 높이 설정
        $('#urSupplierId').data('kendoDropDownList').setOptions({
            height: 100,
        });


	}  // the end of fnInitOptionBox()

    //==================================================================================
	//---------------Initialize Option Box End -----------------------------------------
    //==================================================================================

    function fnSave(){
    	var useYnValue = $('#useYn').getRadioVal();
        if(useYnValue == 'N' && itemArray.includes($('#urBrandId').val())){
    		fnKendoMessage({
				type : "confirm",
				message : "현재 해당 브랜드로 등록된 상품이 존재하며, 사용안함 처리 시 해당상품 노출이 제한될 수 있습니다. <br> 진행하시겠습니까?",
				ok : function() {
					fnSaveConfirm();
				},
				cancel : function() {
					return;
				}
			});
    	}else{
    		fnSaveConfirm();
    	}
    }

    // 브랜드 저장
    function fnSaveConfirm() {
        let data = $('#inputForm').formSerialize(true);

        if (!fnCheckBeforeSave()) {
            return;
        }

        var cbId = 'insert';

        if ( OPER_TP_CODE == 'U') {
            cbId= 'update';
        }

        var url = '';
        if (cbId == "insert") {
            url = '/admin/ur/brand/addBrand';
        } else if (cbId == "update") {
            url = '/admin/ur/brand/putBrand';
        }

        if( data.rtnValid ){
	        fnAjax({
	            //form : 'inputForm',
	            //fileUrl : '/fileUpload',
	            //storageType : "public", // 추가
	            //domain : "ur", // 추가
	            url : url,
	            params : data,
	            success : function(successData) {
	                fnBizCallback(cbId , successData);
	            },
	            isAction : 'batch'
	        });
        }
    }


    //==================================================================================
    //-------------------------------  Common Function start ---------------------------
    //==================================================================================
    function inputFocus() {
        $('#brandName').focus();
    };

    //-------------------------------  콜백합수 -----------------------------
    function fnBizCallback (id, data) {
        switch (id) {
            case 'select':

                //-- 화면 초기화
                //-----------------------------------------------
                fnInputFormClear();
                $('#inputForm').formClear(true);

                $("#urSupplierId").data("kendoDropDownList").readonly(true);
        		$('#inputForm').bindingForm(data, "rows", true);

        		if(data.rows.urIdList != null){
	            	for(var i=0 ; i<data.rows.urIdList.length; i++){
	            		itemArray.push(data.rows.urIdList[i].urBrandId);
	            	}
                }

                fnKendoInputPoup({height:"180px" ,width:"1000px",title:{key :"5876",nullMsg :'표준 브랜드 수정' } });
                break;

			case 'insert':
			case 'update':
                if ( id == "insert" ) {
                    aGridDs.page(1);
                }

				fnKendoMessage({
                        message:"저장되었습니다."
                        , ok : function() {
                                      fnSearch();
                                      fnClose();
                               }
				});
			    break;

            case 'delete':
                fnKendoMessage({  message : '삭제되었습니다.'
                                , ok      : function(){
                                                fnSearch();
                                                fnClose();
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
    };

    //------------------------------------------------------------------
    //-- file 버튼 내부 초기화
    //--- 입력/수정 레이어 팝업을 호출하기 전에 사용한다.
    //------------------------------------------------------------------

    //-- 입력/수정 화면 초기화
    function fnInputFormClear() {
        var urSupplierId = $("#urSupplierId").val("");
        var brandName    = $("#brandName").val("")   ;
        $("span#useYn input:radio").eq(0).click();
        //fnImageButtonClear();
    }
    //==================================================================================
    //-------------------------------  Common Function end -----------------------------
    //==================================================================================


    //==================================================================================
	//------------------------------- Html 버튼 바인딩  Start --------------------------
    //==================================================================================
    $scope.fnSearch     = function () { fnSearch(); };
    $scope.fnClear      = function () { fnClear() ; };
    $scope.fnNew        = function () { fnNew()   ; };
    $scope.fnSave       = function () { fnSave()  ; };
    $scope.fnClose      = function () { fnClose() ; };

    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End ----------------------------
    //==================================================================================

}); // document ready - END
