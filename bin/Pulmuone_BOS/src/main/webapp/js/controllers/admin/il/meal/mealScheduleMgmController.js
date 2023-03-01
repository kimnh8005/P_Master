/**-----------------------------------------------------------------------------
 * system            : 식단패턴 / 연결상품 등록/수정 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.07     최윤지          최초생성
 * @
 * **/
var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var viewModel; // viewModel
var scheduleGridDs, scheduleGridOpt, scheduleGrid;
var patternStartDate, patternEndDate;
var PAGE_SIZE = 20;
var originDateSearchStart, originDateSearchEnd;

$(document).ready(function() {
    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "mealScheduleMgm",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();	// Initialize Button  ---------------------------------
        fnInitOptionBox(); // Initialize Option Box ------------------------------------
        fnDefaultSetting();
        fnSearch();
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSave").kendoButton();
    };

    function fnSearch(){

        let data = $('#inputForm').formSerialize(true);

        //달력 시작일, 종료일 빈값 체크
        if(fnIsEmpty(data.dateSearchStart) || fnIsEmpty(data.dateSearchEnd)) {
            fnKendoMessage({message : "기간을 입력해주세요.", ok : function() {
                    $('#dateSearchStart').val(fnGetToday());
                    $('#dateSearchEnd').val(fnGetMonthAdd(fnGetToday(), 3, 'yyyy-MM-dd'));
            }});
            return false;
        }

        data.patternCd = $('#patternCd').text();
        data.dateSearchStart = data.dateSearchStart.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
        data.dateSearchEnd = data.dateSearchEnd.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');

        let searchData = fnSearchData(data);
        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength : searchData.length,
                      filter : { filters : searchData }
        };
        scheduleGridDs.query(query);

	}

    function fnDefaultSetting(){
        if(fnNvl(paramData.patternCd) != "") {
            // 기본정보 조회
            fnAjax({
                url: '/admin/item/meal/getMealPatternInfo',
                params: {patternCd: paramData.patternCd},
                async: false,
                success:
                    function (data) {
                        if (data != null) {
                            let createDtStr = data.createDt + ' (' + data.createLoginId + '/' + data.createNm + ')';
                            if (fnNvl(data.modifyDt) != "") {
                                createDtStr += ' / </br>' + data.modifyDt + ' (' + data.modifyLoginId + '/' + data.modifyNm + ')';
                            }
                            $("#mallDivNm").text(data.mallDivNm);
                            $('#createDt').html(createDtStr);
                            $('#patternCd').text(data.patternCd);
                            $('#patternNm').text(data.patternNm);
                            $('#patternStartDt').text(data.patternStartDt);
                            $('#patternEndDt').text(data.patternEndDt);
                            patternStartDate = data.patternStartDt;
                            patternEndDate = data.patternEndDt;
                        }
                    },
                isAction: 'select'
            });
            $('#fnSave').hide();
        } else {
            $('#fnPatternInfoUpdate').hide();
            $('#fnPatternDetailSave').hide();
        }

        fnInitScheduleGrid(); // 스케쥴 상세정보 > 스케쥴 그리드
    }

    // 팝업 닫기
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	};
    
    //스케쥴정보 > 상세버튼
    $('#scheduleGrid').on("click", "button[kind=btnScheduleGridDetl]", function(e) {
        var grid =  $('#scheduleGrid').data('kendoGrid');
        e.preventDefault();
        let dataItem = grid.dataItem($(e.currentTarget).closest("tr"));
        dataItem.detlMgmType = "SCH";
        dataItem.patternCd = $('#patternCd').text();
        dataItem.patternStartDate = patternStartDate;
        dataItem.patternEndDate = patternEndDate;
        fnKendoPopup({
                id         : "mealInfoDetailMgmPopup",
                title      : '스케쥴 상세 등록/수정',
                width      : "850px",
                height     : "200px",
                src        : "#/mealInfoDetailMgmPopup",
                param      : dataItem,
                success    : function( id, data ){
                            fnSearch();
                }
            });
    });


    //스케쥴 다운로드 (최근 저장된 스케쥴로 다운로드)
	function fnMealInfoExcelDownload(){
        var data = {};
		data.downloadType = '스케쥴';
		data.mallDivNm = $('#mallDivNm').text();
		data.patternCd = $('#patternCd').text();
		data.patternNmExcel = $('#patternNm').text().replace(/(\s*)/g, "");
		data.patternNm = $('#patternNm').text();
        data.downloadDateStart = $('#dateSearchStart').val().replaceAll("-", "");
        data.downloadDateEnd = $('#dateSearchEnd').val().replaceAll("-", "");

		fnExcelDownload('/admin/il/meal/getMealScheduleExportExcel', data);
	};

    //--------------------------------- Button End---------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){
        //시작일
		fnKendoDatePicker({
			id    : 'dateSearchStart',
			format: 'yyyy-MM-dd',
			btnStartId : 'dateSearchStart',
			btnEndId : 'dateSearchEnd',
			defVal : fnGetToday(),
			defType : 'threeMonth',
            nextDate : true,
			change: function() {
			    fnInputValidateUse('dateSearchStart', 'dateSearchEnd', 'start');
			}
		});

		//종료일
		fnKendoDatePicker({
			id    : 'dateSearchEnd',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'dateSearchStart',
			btnEndId : 'dateSearchEnd',
			defVal : fnGetMonthAdd(fnGetToday(), 3, 'yyyy-MM-dd'),
			defType : 'threeMonth',
            nextDate : true,
			change: function() {
			    fnInputValidateUse('dateSearchStart', 'dateSearchEnd', 'end');
			}
		});
    };

    //---------------Initialize Option Box End ------------------------------------------------

    //-------------------------------  Common Function start -------------------------------
    function fnInputValidateUse(dateSearchStart, dateSearchEnd, picker) {
    //달력 조회 기간 체크
    if(!fnIsEmpty($("#"+dateSearchStart).val()) && !fnIsEmpty($("#"+dateSearchEnd).val())) {
        if(fnGetMonthMinus($("#"+dateSearchEnd).val(),3,'yyyy-MM-dd') > $("#"+ dateSearchStart).val() ) {
        fnKendoMessage({message : "최대 3개월까지 조회하실 수 있습니다.", ok : function() {
                $('#'+dateSearchStart).val(fnGetToday());
                $('#'+dateSearchEnd).val(fnGetMonthAdd(fnGetToday(), 3, 'yyyy-MM-dd'));
                picker == 'start' ?  $("#"+dateSearchStart).focus() :  $("#"+dateSearchEnd).focus();
            }});
        return false;
        }
        //달력 시작일 종료일 체크
        if($("#"+ dateSearchStart).val() > $("#"+dateSearchEnd).val()) {
            fnKendoMessage({message : "조회 시작일보다 과거로 설정할 수는 없습니다.", ok : function() {
                    $('#'+dateSearchStart).val(fnGetToday());
                    $('#'+dateSearchEnd).val(fnGetMonthAdd(fnGetToday(), 3, 'yyyy-MM-dd'));
                    picker == 'start' ?  $("#"+dateSearchStart).focus() :  $("#"+dateSearchEnd).focus();
                }});
            return false;
        }
    }
        return true;
    }

    //스케쥴 그리드
    function fnInitScheduleGrid() {
    var callUrl          = '';
    // 페이징있는 그리드
     scheduleGridDs = fnGetPagingDataSource({
        url      : '/admin/item/meal/getMealScheduleDetailList',
        pageSize : PAGE_SIZE,

    });

     scheduleGridOpt = {
          dataSource  : scheduleGridDs
        , noRecordMsg : '스케쥴 목록이 없습니다.'
        , pageable : { pageSizes: [20, 30, 50], buttonCount : 20 }
        , height : 400
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , resizable   : true
        , autobind    : false
        , sortable    : true
        , columns     : [
                          { field : 'deliveryDateStr'       , title : '날짜'      , width:  '150px', attributes : {style : 'text-align:center;'}, sortable : true,
                            template : function(dataItem) {
                                        return dataItem.holidayYn == 'Y' ? '<span style="color: #FF0000;">' +dataItem.deliveryDateStr+ '</span>' : dataItem.deliveryDateStr;
                                }
                          }
                        , { field : 'deliveryWeekCode'         , title : '요일'      , width: '80px', attributes : {style : 'text-align:center;'}, sortable : false,
                           template : function(dataItem) {
                                        return dataItem.holidayYn == 'Y' ? '<span style="color: #FF0000;">' +dataItem.deliveryWeekCode+ '</span>': dataItem.deliveryWeekCode;
                                }
                           }
                        , { field : 'mealContsCd'         , title : '식단품목코드'        , width: '100px', attributes : {style : 'text-align:center;'}, sortable : false,
                            template : function(dataItem) {
                                        return dataItem.holidayYn == 'Y' ? '<span style="color: #FF0000;"> 휴일 </span>' : dataItem.mealContsCd;
                                }
                           }
                        , { field : 'mealNm'         , title : '식단품목명'        , width: '100px', attributes : {style : 'text-align:center;'}, sortable : false,
                            template : function(dataItem) {
                                        return dataItem.holidayYn == 'Y' ? '<span style="color: #FF0000;">' +fnNvl(dataItem.mealNm)+ '</span>' : fnNvl(dataItem.mealNm);
                                }
                           }
                        , { field : 'allergyYn'    , title : '알러지식단'      , width:  '80px', attributes : {style : 'text-align:center;'}, sortable : false,
                            template : function(dataItem) {
                                        return dataItem.allergyYn != 'Y' ? '' : 'Y';
                                }
                            }
                        , { field : 'modifyDt'    , title : '최근 개별 수정일'      , width:  '80px', attributes : {style : 'text-align:center;'}, sortable : false,
                            template : function(dataItem) {
                                        return dataItem.holidayYn == 'Y' ? '<span style="color: #FF0000;">' +dataItem.modifyDt+ '</span>' : dataItem.modifyDt;
                                }
                            }
                        , { field : 'management'      , title : '관리'          , width:  '80px', attributes : {style : 'text-align:center;'}, sortable : false,
                            template : function(dataItem) {
		                            return '<button type="button" class="btn-white btn-s"  kind="btnScheduleGridDetl">상세</button>';
                            }
                          }
                        ]
    };

    // scheduleGrid
    scheduleGrid = $("#scheduleGrid").initializeKendoGrid( scheduleGridOpt ).cKendoGrid();
    scheduleGrid.bind("dataBound", function(){
        $("#countTotalSpan").text(scheduleGridDs._total );
    });
  }
    //-------------------------------  Common Function end -------------------------------
    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnMealInfoExcelDownload =function(){	fnMealInfoExcelDownload(); }; // 패턴 다운로드
    $scope.fnSave = function(){ fnSave(); }; // 저장
    $scope.fnClose = function(){ fnClose(); }; // 닫기
    $scope.fnSearch = function(){ fnSearch(); }; // 조회

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
