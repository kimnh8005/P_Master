/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 고객보상제 리스트
 * @
 * @ 수정일 			수정자 			수정내용
 * @ ------------------------------------------------------
 * @ 2021.06.24 	최윤지			최초생성 @
 ******************************************************************************/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

var paramData = parent.POP_PARAM["parameter"];
var csRewardId;
var csRewardId;
if(paramData != undefined){
    csRewardId =  paramData.csRewardId;
}

var gbPageParam = '';
gbPageParam = fnGetPageParam();
var gbCsRewardId = '';
gbCsRewardId = gbPageParam.csRewardId;
$(document).ready(function() {
	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'rewardList',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	// Initialize Button ---------------------------------

		fnInitGrid();	// Initialize Grid ------------------------------------

		fnInitOptionBox();// Initialize Option Box

		fnSearch();

	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnClear').kendoButton();
	}

	function fnSearch(){
		$('#inputForm').formClear(false);

		var data;
		data = $('#searchForm').formSerialize(true);

		//validation
		 var keyword = $('#keyWord').val();
		 
		//진행 기간
		if( $('#startDt').val() == "" && $('#endDt').val() != ""){
			fnKendoMessage({ message : "시작일 또는 종료일을 입력해주세요.."});
			return false;
		}
		if( $('#startDt').val() != "" && $('#endDt').val() == ""){
			fnKendoMessage({ message : "시작일 또는 종료일을 입력해주세요."});
			return false;
		}
		if( $('#startDt').val() > $('#endDt').val()){
			fnKendoMessage({ message : "시작일을 종료일보다 뒤로 설정할 수 없습니다."});
			return false;
		}

		//키워드 검색
        if ($('#searchSe').val() == 'NAME') { // 키워드 검색 > 보상제 명
          if (keyword != null && keyword != 'null' && keyword != '') {
            keyword = $.trim(keyword);

            if (keyword.length < 2) {
              fnKendoMessage({ message : "이벤트명을 두글자 이상 입력하세요."});
              return false;
            }
          }
          data.rewardNm = keyword;
        } else if($('#searchSe').val() == 'ID'){ // 키워드 검색 > 보상제 ID
          data.csRewardId = keyword;
		}

        if(!fnIsEmpty(gbCsRewardId)) { // csRewardId가 있으면 (고객보상제 신청관리 내 보상제명 > 리스트)
			data.csRewardId = gbCsRewardId;
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

	// 초기화
	function fnClear(){

		$('#searchForm').formClear(true);

        $("input[name=statusSe]").eq(0).prop("checked", true).trigger("change");
        $("input[name=rewardPayTp]").eq(0).prop("checked", true).trigger("change");
        $(".date-controller button[fb-btn-active=true]").attr("fb-btn-active", false);    
	}

	//신규등록
	function fnNew(){
     // ----------------------------------------------------------------------
    // 세션 lastPage 삭제(페이징 기억 관련)
    // ----------------------------------------------------------------------
    var curPage = aGridDs._page;
    sessionStorage.setItem('lastPage', JSON.stringify(curPage));

    // 링크정보
    let option = {};
    option.url    = '#/rewardMgm';
    option.menuId = 1355;
    option.target = '_blank';
    option.data = { mode : 'insert' };
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)

    }

    // 리스트 내 수정 버튼
    $('#aGrid').on("click", "button[kind=rewardListEdit]", function(e) {
    e.preventDefault();
    let dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
	// 링크정보
    let option = {};
    option.url    = '#/rewardMgm';
    option.menuId = 1355;
    option.target = '_blank';
    option.data = { csRewardId : dataItem.csRewardId, mode : 'update' };
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)
	});

	// 리스트 내 신청내역 버튼
    $('#aGrid').on("click", "button[kind=rewardListApply]", function(e) {
    e.preventDefault();
    let dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
	// 링크정보
    let option = {};
    option.url    = '#/rewardApply';
    option.menuId = 1355;
    option.target = '_blank';
    option.data = { csRewardId : dataItem.csRewardId };
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)
	});

	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/customer/reward/getRewardList",
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
                 { field:'no'               ,title : 'No'       , width:'50px',attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
                ,{ field:'csRewardId'       ,title : '보상제 ID'       , width:'50px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'rewardNm'         ,title : '보상제 명'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'startDt'          ,title : '시작일자'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'endDt'            ,title : '종료일자'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'rewardPayTp'      ,title : '보상지급유형'       , width:'200px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'statusNm'     	,title : '진행상태'       , width:'200px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'useYn'            ,title : '사용여부'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'createUserNm'     ,title : '등록자'       , width:'100px' ,attributes:{ style:'text-align:center' }
                    , template: function (row) {
                            let returnVal;
                                returnVal = row.createUserNm;
                                if(!fnIsEmpty(row.createUserId)) {
                                    returnVal += '</br> ('+row.createUserId + ')';
                                }
                        return returnVal;
                    }
                }
                ,{ field:'createDt'         ,title : '등록일'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'modifyDt'         ,title : '수정일'       , width:'100px' ,attributes:{ style:'text-align:center' }}
                ,{ field:'modifyUserNm'     ,title : '수정자'       , width:'100px' ,attributes:{ style:'text-align:center' }
                    , template: function (row) {
                                let returnVal;
                                    returnVal = row.modifyUserNm;
                                    if(!fnIsEmpty(row.modifyUserId)) {
                                        returnVal += '</br> ('+row.modifyUserId + ')';
                                    }
                            return returnVal;
                        }
                }
                ,{ field:'management'       ,title : '관리'       , width:'100px' ,attributes:{ style:'text-align:center' }
                 , template: function (row) {
                            let returnVal;
                                returnVal = '<button type="button" class="btn-white btn-s" kind="rewardListEdit">수정</button>'
                                returnVal += '<button type="button" class="btn-white btn-s" kind="rewardListApply">신청내역</button>';
                        return returnVal;
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
	}

	// ------------------------------- Grid End -------------------------------

	// ---------------Initialize Option Box Start
	// ------------------------------------------------
	function fnInitOptionBox(){

		// 키워드 검색
		fnKendoDropDownList({
            id      : 'searchSe'
          , tagId   : 'searchSe'
          , data    : [
                        {'CODE':'NAME','NAME':'보상제 명'}
                      , {'CODE':'ID'  ,'NAME':'보상제 ID'}
                      ]
          , chkVal  : 'NAME'
        });

		//진행상태
		fnTagMkChkBox({
            id        : 'statusSe'
          , tagId     : 'statusSe'
          , data      : [
                          { 'CODE' : 'ALL'    , 'NAME' : '전체'    }
                        , { 'CODE' : 'BEF'   , 'NAME' : '진행예정'}
                        , { 'CODE' : 'ING'    , 'NAME' : '진행중'  }
                        , { 'CODE' : 'END'    , 'NAME' : '진행종료'}
                        ]
          , chkVal    : 'ALL'
          , style     : {}
        });
		// 전체가 체크 되었을 경우
        checkAll('statusSe');

		//사용여부
		fnTagMkRadio({
            id      : 'useYn'
          , tagId   : 'useYn'
          , data    : [
                        { 'CODE' : ''   , 'NAME' : '전체'   }
                      , { 'CODE' : 'Y'  , 'NAME' : '예'     }
                      , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                      ]
          , chkVal  : ''
          , style   : {}
        });

		// 보상지급 유형
		fnTagMkChkBox({
            id          : 'rewardPayTp'
          , tagId       : 'rewardPayTp'
          , url         : '/admin/comn/getCodeList'
          , params      : {'stCommonCodeMasterCode' : 'REWARD_PAY_TP', 'useYn' : 'Y'}
          , async       : false
          , isDupUrl    : 'Y'
          , style       : {}
          , chkVal      : 'ALL'
          , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
        });
		// 전체가 체크 되었을 경우
        checkAll('rewardPayTp');

		//진행기간
		fnKendoDatePicker({
            id          : 'startDt'
          , format      : 'yyyy-MM-dd'
          , btnStartId  : 'startDt'
          , btnEndId    : 'endDt'
          , change      : onChangeStartDt
        });
        fnKendoDatePicker({
            id          : 'endDt'
          , format      : 'yyyy-MM-dd'
          , btnStyle    : true
          , btnStartId  : 'startDt'
          , btnEndId    : 'endDt'
          , change      : onChangeEndDt
        });
        function onChangeStartDt(e) {
          fnOnChangeDatePicker(e, 'start', 'startDt', 'endDt');
        }
        function onChangeEndDt(e) {
          fnOnChangeDatePicker(e, 'end', 'startDt', 'endDt');
        }

        // 체크 박스 전체 선택
        function checkAll(id) {
            $(`#${id}`).find("input[type='checkbox']").prop("checked", true);
        }

        $('#statusSe').bind("change", onCheckboxWithTotalChange);
        $('#rewardPayTp').bind("change", onCheckboxWithTotalChange);

	}

	function onCheckboxWithTotalChange(e) {   // 체크박스 change event

        // 첫번째 체크박스가 '전체' 체크박스라 가정함
        var totalCheckedValue = $("input[name=" + e.target.name + "]:eq(0)").attr('value');

        if( e.target.value == totalCheckedValue ) {  // '전체' 체크 or 체크 해제시

            if( $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ) {  // '전체' 체크시

                $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
                    $(element).prop('checked', true);  // 나머지 모두 체크
                });

            } else { // '전체' 체크 해제시

                $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element){
                    $(element).prop('checked', false);  // 나머지 모두 체크 해제
                });

            }

        } else { // 나머지 체크 박스 중 하나를 체크 or 체크 해재시

            var allChecked = true; // 나머지 모두 체크 상태인지 flag

            $("input[name=" + e.target.name + "]:gt(0)").each(function(idx, element) {
                if( $(element).prop('checked') == false ) {
                    allChecked = false;  // 하나라도 체크 해제된 상태가 있는 경우 flag 값 false
                }
            });

            if( allChecked ) { // 나머지 모두 체크 상태인 경우
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', true);  // 나머지 모두 '전체' 체크
            } else {
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', false);  // 나머지 모두 '전체' 체크 해제
            }

        }

	}

	// ---------------Initialize Option Box End
	// ------------------------------------------------
	// ------------------------------- Common Function start
	// -------------------------------
	// ------------------------------- Common Function end
	// -------------------------------


	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------
	/** Common Search */
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear */
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
