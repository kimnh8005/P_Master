/**-----------------------------------------------------------------------------
 * description 		 : 매장 목록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.11     안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {

    fnInitialize();

    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : 'true' });

        fnPageInfo({
              PG_ID    : 'shopList'
            , callback : fnUI
        });


    }

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();
	}


	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnClear').kendoButton();
	}

    //--- 검색  -----------------
    function fnSearch() {
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
	}
	//--------------------------------- Button End---------------------------------


	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------
    function fnInitGrid() {
        aGridDs = fnGetPagingDataSource({
             url      : "/admin/ur/store/getStoreList",
             pageSize : PAGE_SIZE
        });

        aGridOpt = {
              dataSource: aGridDs
            , pageable  : {
                pageSizes   : [20, 30, 50],
                buttonCount : 10
              }
            , navigatable: true
            , columns : [
            	    {field : 'no'             , title : 'No' 	, width : '50px'  , attributes : {style : 'text-align:center'}, template: "<span class='row-number'></span>"}
                  , {field : 'urStoreId'      , title : '매장코드' , width : '150px'  , attributes : {style : 'text-align:center'}}
                  , {field : 'storeCategoryName' , title : '매장유형' , width : '150px'  , attributes : {style : 'text-align:center'}}
                  , {field : 'storeName'      , title : '매장명'  , width : '300px' , attributes : {style : 'text-align:center'}}
                  , {field : 'address'        , title : '매장주소' , width : '350px'  , attributes : {style : 'text-align:center'}}
                  , {field : 'tel' 			  , title : '대표번호' , width : '150px'  , attributes : {style : 'text-align:center'}}
                  , {field : 'deliveryTypeName', title : '배송방법' , width : '150px'  , attributes : {style : 'text-align:center'}}
                  , {field : 'useYn' 	  , title : '노출여부' , width : '80px'  , attributes : {style : 'text-align:center'}}
                  , {field : 'statusName' 		  , title : '운영상태' , width : '100px'  , attributes : {style : 'text-align:center'}}
                  , {field : 'onlineDivYn' 	  , title : 'O2O사용여부' , width : '100px'  , attributes : {style : 'text-align:center'}}
                  ,{ title:'관리', width: "200px", attributes:{ style:'text-align:center;', class:'forbiz-cell-readonly' }
					,command: [ { text: '수정',  className: "btn-gray btn-s",
								   click: function(e) {  e.preventDefault();
												            var tr = $(e.target).closest("tr");
												            var data = this.dataItem(tr);
												            fnStoreDetail(data.urStoreId);}
								}
					]
				}
                  , {field : 'modifyDate'   , hidden:true}
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
            if(aGrid.dataSource.data()[0] != undefined){
            	$('#modifyDate').text(aGrid.dataSource.data()[0].modifyDate);
            }
        });

	}


    function fnStoreDetail(urStoreId){
    	var map = aGrid.dataItem(aGrid.select());
//		var sData = $('#searchForm').formSerialize(true);
		var option = new Object();
		option.url = "#/shopMgm";
		option.menuId = 200;
		option.data = {
				urStoreId		:urStoreId
//				companyBbsType	:sData.companyBbsType,
//				useYn			:sData.useYn,
//				popupYn			:sData.popupYn,
//				conditionType	:sData.conditionType,
//				conditionValue	:sData.conditionValue,
//				startCreateDate	:sData.startCreateDate,
//				endCreateDate	:sData.endCreateDate
				};
		$scope.goPage(option);
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


        //  검색어
		fnKendoDropDownList({
			id    : 'searchType',
			data  : [{"CODE":"NM" ,"NAME":'매장명'}
					,{"CODE":"STORE_ID"		,"NAME":'매장코드'}
					]
		});

		// 노출여부
		fnTagMkRadio({
			id : 'searchUseYn',
			tagId : 'searchUseYn',
			async : false,
			style : {},
			data  : [
				{ "CODE" : "ALL"	, "NAME":'전체' },
				{ "CODE" : "Y"		, "NAME":'예' },
				{ "CODE" : "N"		, "NAME":'아니오' }
			],
			chkVal: "ALL"

		});

		// 매장유형
		fnTagMkRadio({
			id : 'searchStoreType',
			tagId : 'searchStoreType',
			async : false,
			style : {},
			data  : [
				{ "CODE" : "STORE_CATEGORY.ALL"		, "NAME":'전체' },
				{ "CODE" : "STORE_CATEGORY.DIRECT"	, "NAME":'직영' },
				{ "CODE" : "STORE_CATEGORY.SIS"		, "NAME":'Shop in Shop' },
				{ "CODE" : "STORE_CATEGORY.BY_ORGA"	, "NAME":'By ORGA' }
			],
			chkVal: "STORE_CATEGORY.ALL"

		});

		// O2O 매장여부
		fnTagMkRadio({
			id : 'searchOnlineDivYn',
			tagId : 'searchOnlineDivYn',
			async : false,
			style : {},
			data  : [
				{ "CODE" : "ALL"	, "NAME":'전체' },
				{ "CODE" : "Y"		, "NAME":'예' },
				{ "CODE" : "N"		, "NAME":'아니오' },
			],
			chkVal: "ALL"

		});


	}  // the end of fnInitOptionBox()
    //==================================================================================
	//---------------Initialize Option Box End ------------------------------------------------
    //==================================================================================

    //==================================================================================
	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    //==================================================================================
    $scope.fnSearch     = function () { fnSearch(); };
    $scope.fnClear      = function () { fnClear() ; };

	//마스터코드값 입력제한 - 영문대소문자 & 숫자
//	fnInputValidationByRegexp("searchValue", /[^a-z^A-Z^0-9]/g);

	//마스터코드값 입력제한 - 영문대소문자 & /
//	fnInputValidationByRegexp("searchZip", /[^0-9/]/g);
    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End -------------------------------
    //==================================================================================

}); // document ready - END
