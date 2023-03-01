/**-----------------------------------------------------------------------------
 * description 		 : 프로모션관리 > 외부광고 코드 관리 > 외부광고 코드 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.05.04		이원호          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'pmAdvertising',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitGrid();	// Initialize Grid ------------------------------------
		fnInitOptionBox();// Initialize Option Box
		fnDefaultSet();
		fnSearch();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch', '#fnClear', '#fnNew', '#fnExcelDownload').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);

		if(data.createStartDate === "" || data.createEndDate === ""){
            fnKendoMessage({message : '등록일 시작일 또는 종료일을 입력해주세요.'});
            return;
        }
        if(data.modifyStartDate === "" || data.modifyEndDate === ""){
            fnKendoMessage({message : '수정일 시작일 또는 종료일을 입력해주세요.'});
            return;
        }

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
		fnDefaultSet();
	}

    // 옵션 초기화
	function fnInitOptionBox(){

	    // 분류값 선택 - 매체
        fnKendoDropDownList({
            id : "source",
            url : "/admin/promotion/advertising/getAdvertisingType",
            tagId : "source",
            params : { "searchType" : "SOURCE" },
            valueField: 'CODE',
            textField: 'NAME',
            blank : "전체",
            async : false
        });

        // 분류값 선택 - 구좌
        fnKendoDropDownList({
            id : "medium",
            url : "/admin/promotion/advertising/getAdvertisingType",
            tagId : "medium",
            params : { "searchType" : "MEDIUM" },
            chkVal: "",
            blank : "전체",
            async : false,
            cscdId : "source",
            cscdField : "source"
        });

        // 분류값 선택 - 캠페인
        fnKendoDropDownList({
            id : "campaign",
            url : "/admin/promotion/advertising/getAdvertisingType",
            tagId : "campaign",
            params : { "searchType" : "CAMPAIGN"},
            chkVal: "",
            blank : "전체",
            async : false,
            cscdId : "medium",
            cscdField : "medium"
        });

        // 등록일 - 조회 시작 일자
        fnKendoDatePicker({
            id: "createStartDate",
            format: "yyyy-MM-dd",
            btnStartId: "createStartDate",
            btnEndId: "createEndDate",
            defType : 'oneMonth'
        });

        // 등록일 - 조회 종료 일자
        fnKendoDatePicker({
            id: "createEndDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "createStartDate",
            btnEndId: "createEndDate",
            defType : 'oneMonth'
        });

        // 수정일 - 조회 시작 일자
        fnKendoDatePicker({
            id: "modifyStartDate",
            format: "yyyy-MM-dd",
            btnStartId: "modifyStartDate",
            btnEndId: "modifyEndDate",
            defType : 'oneMonth'
        });

        // 수정일 - 조회 종료 일자
        fnKendoDatePicker({
            id: "modifyEndDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "modifyStartDate",
            btnEndId: "modifyEndDate",
            defType : 'oneMonth'
        });

        // 처리 상태
	    fnTagMkChkBox({
            id: 'useYnFilter',
            tagId: 'useYnFilter',
            data: [
                {CODE: "ALL", NAME: "전체"},
                {CODE: "Y", NAME: "사용"},
                {CODE: "N", NAME: "미사용"}
                ]
        });

        // 담당자
        fnKendoDropDownList({
            id : "searchType",
            data : [
                {"CODE" : "USER_NAME", "NAME" : "담당자명"},
                {"CODE" : "USER_ID", "NAME" : "담당자ID"}
            ]
        });

        fbCheckboxChange(); //[공통] checkBox
	};

	// 기본 설정
	function fnDefaultSet(){
	    $("input[name=useYnFilter]").eq(0).prop("checked", true).trigger("change");

        $(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})
        $("#createDateTd").find('button[data-id="fnDateBtn5"]').attr("fb-btn-active", true);
        $("#modifyDateTd").find('button[data-id="fnDateBtn5"]').attr("fb-btn-active", true);

		$("#createStartDate").data("kendoDatePicker").value(fnGetMonthAdd(fnGetToday(), -1,'yyyy-MM-dd'));
        $("#createEndDate").data("kendoDatePicker").value(fnGetToday());
        $("#modifyStartDate").data("kendoDatePicker").value(fnGetMonthAdd(fnGetToday(), -1,'yyyy-MM-dd'));
        $("#modifyEndDate").data("kendoDatePicker").value(fnGetToday());
	};

    //추가
    function fnNew(){
        fnKendoPopup({
            id     : 'pmAdvertisingPopup',
            title  : '외부광고 코드 등록',
            src    : '#/pmAdvertisingPopup',
            param  : {popupType : "ADD" },
            width  : '900px',
            height : '450px',
            success: function( id, data ){
                if(data == 'SAVE'){
                    fnSearch();
                }
            }
        });
    }

    function fnGridClick(e){
        var dataItem = aGrid.dataItem($(e.target).closest('tr'));
        var sData = $('#searchForm').formSerialize(true);

        fnKendoPopup({
            id     : 'pmAdvertisingPopup',
            title  : '외부광고 코드 수정',
            src    : '#/pmAdvertisingPopup',
            param  : {popupType : "PUT", pmAdExternalCd :dataItem.pmAdExternalCd },
            width  : '900px',
            height : '450px',
            success: function( id, data ){
                if(data == 'SAVE'){
                    fnSearch();
                }
            }
        });
    }

    // --------------------------------- Button
	// End---------------------------------

	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/promotion/advertising/getAdvertisingExternalList",
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
                { field:'advertisingName' ,title : '광고명'	,width:'90px' ,attributes:{ style:'text-align:center;text-decoration: underline;color:blue;' }}
                ,{ field:'pmAdExternalCd' ,title : '광고 ID'	,width:'90px' ,attributes:{ style:'text-align:center;text-decoration: underline;color:blue;' }}
                ,{ field:'source'	      ,title : '매체(source)'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'medium'	      ,title : '구좌(medium)'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'campaign'	      ,title : '캠페인(campaign)'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'term'	          ,title : '키워드(term)'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'content'	      ,title : '콘텐츠(content)'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'useYnName'	  ,title : '사용여부'   ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'createDateTime' ,title : '등록일'   ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'modifyDateTime' ,title : '수정일'   ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'userInfo'	      ,title : '담당자'   ,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'management'	  ,title : '복사'		, width:'150px', attributes : { style : "text-align:center" },
                    template : function(dataItem){
                        return '<button type="button" class="k-button k-button-icontext" kind="btnUrlCopy">URL 복사</button>';
                    }
                }
            ]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#totalCnt').text(aGridDs._total);
        });

        $(aGrid.tbody).on("click", "td", function (e) {
            var row = $(this).closest("tr");
            var rowIdx = $("tr", aGrid.tbody).index(row);
            var colIdx = $("td", row).index(this);
            if(colIdx == 0 || colIdx == 1){
                fnGridClick(e);
            }
        });

        // 그리드 버튼 이벤트 - URL 복사
        $('#aGrid').on("click", "button[kind=btnUrlCopy]", function(e) {
            e.preventDefault();
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

            // 복사할 텍스트를 변수에 할당해줍니다.
            var text = dataItem.advertisingUrl;

            const tempElem = document.createElement('textarea');
            tempElem.value = text;
            document.body.appendChild(tempElem);

            tempElem.select();
            document.execCommand('copy');
            document.body.removeChild(tempElem);

            fnKendoMessage({message : 'URL이 복사 되었습니다'});
        });

	}

	// ------------------------------- Grid End -------------------------------

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------

	/** Common Search */
	$scope.fnSearch = function(){	fnSearch();	};
	/** Common Clear */
	$scope.fnClear = function(){	 fnClear();	};

	/** Button fnNew */
	$scope.fnNew = function() { fnNew(); };

	/** Button fnExcelDownload */
    $scope.fnExcelDownload = function() {
        fnKendoMessage({
             message:'다운로드 하시겠습니까?',
             type : "confirm" ,
             ok : function(){
                 var data = $('#searchForm').formSerialize(true);
                 fnExcelDownload('/admin/promotion/advertising/getAdvertisingExternalListExcelDownload', data);
             }
         });
    };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
