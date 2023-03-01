/**-----------------------------------------------------------------------------
 * description 		 : 매장 배송권역 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.10     신성훈          최초생성
 * @ 2020.07.17     민동진          *
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
              PG_ID    : 'shopDeliveryArea'
            , callback : fnUI
        });


    }

	function fnUI(){
		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

//		fnSearch();
	}


	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnClear').kendoButton();
	}

    //--- 검색  -----------------
    function fnSearch() {
        var data = $('#searchForm').formSerialize(true);

		if((data.searchType == 'UR_STORE_ID' && data.searchValue == '') ||
				(data.searchType == 'ZIP_CD' && data.searchZip == '') ||
				(data.searchType == 'NM' && data.searchName == '') ){
			fnKendoMessage({ message : "검색어를 입력해 주세요" });
		    return;
		}

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
             url      : "/admin/ur/store/getShopDeliveryAreaList",
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
            	    {field : 'no'    , title : 'No' , width : '5%'  , attributes : {style : 'text-align:center'}, template: "<span class='row-number'></span>"}
                  , {field : 'urStoreId'    , title : '매장코드' , width : '10%'  , attributes : {style : 'text-align:center'}}
                  , {field : 'storeName'  , title : '매장명'   , width : '20%' , attributes : {style : 'text-align:center'}}
                  , {field : 'zipCode'    , title : '우편번호' , width : '15%'  , attributes : {style : 'text-align:center'}}
                  , {field : 'buildingNumber' , title : '건물번호' , width : '25%'  , attributes : {style : 'text-align:center'}}
                  , {field : 'deliveryAreaName'   , title : '권역정보' , width : '25%'  , attributes : {style : 'text-align:center'}}
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

        $("#searchName").hide();
		$("#searchValue").show();
		$("#searchZip").hide();

        //-----------------------------------------------------------------------
        //-- Initializing -> 검색조건 : 브랜드 콤보상자
        //-----------------------------------------------------------------------
        fnKendoDropDownList({
              id   : 'searchType'
            , data : [
                        {"CODE":"UR_STORE_ID"   , "NAME":'매장 코드'}
                      , {"CODE":"ZIP_CD"   , "NAME":'우편번호' }
                      , {"CODE":"NM" , "NAME":'매장명'   }
                     ]
            , textField  : "NAME"
            , valueField : "CODE"
        });

        $('#searchType').unbind('change').on('change', function(){
			var searchTypeList =$('#searchType').data('kendoDropDownList');
			var data = searchTypeList.value();
			switch(data){
			case "UR_STORE_ID" :
				$("#searchName").hide();
				$("#searchValue").show();
				$("#searchZip").hide();
				break;

			case "ZIP_CD" :
				$("#searchName").hide();
				$("#searchValue").hide();
				$("#searchZip").show();
				break;


			case "NM" :
				$("#searchName").show();
				$("#searchValue").hide();
				$("#searchZip").hide();
				break;

			}
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
	fnInputValidationByRegexp("searchValue", /[^a-z^A-Z^0-9]/g);

	//마스터코드값 입력제한 - 영문대소문자 & /
	fnInputValidationByRegexp("searchZip", /[^0-9/]/g);
    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End -------------------------------
    //==================================================================================

}); // document ready - END
