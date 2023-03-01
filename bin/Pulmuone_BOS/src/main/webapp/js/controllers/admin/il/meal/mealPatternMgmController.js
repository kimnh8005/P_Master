/**-----------------------------------------------------------------------------
 * system            : 식단패턴 / 연결상품 등록/수정 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.07     최윤지          최초생성
 * @
 * **/
"use strict";

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var viewModel; // viewModel
var patternStartDate, patternEndDate;
var updatePatternDetlIdList = [];
var mealContsArrInfo = [];
$(document).ready(function() {

    fnInitialize();	//Initialize Page Call ---------------------------------

    // sheetJs 스크립트 추가
    let myScript = document.createElement("script");
    myScript.setAttribute("src", "js/lib/sheetJs/xlsx.full.min.js");
    document.body.appendChild(myScript);

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "mealPatternMgm",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();	// Initialize Button  ---------------------------------
        fnInitOptionBox(); // Initialize Option Box ------------------------------------
        fnDefaultSetting();
        fnInitEventBind();
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSave").kendoButton();
    };

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
                            $("#mallDivNm").data("kendoDropDownList").value(data.mallDiv);
                            $("#mallDivNm").data("kendoDropDownList").enable( false );
                            $('#createDt').html(createDtStr);
                            $('#patternCd').text(data.patternCd);
                            $('#patternNm').val(data.patternNm);
                            $('#patternStartDt').val(data.patternStartDt);
                            $('#patternEndDt').val(data.patternEndDt);
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

        fnInitPatternGoodsGrid(); // 기본정보 > 연결상품정보 그리드
        fnInitPatternGrid();// 패턴정보 > 패턴 그리드
    }

    // 팝업 닫기
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	};

    // 신규등록 저장버튼
    function fnSave() {
        let paramData = $("#inputForm").formSerialize(true);
        paramData.mallDiv = paramData.mallDivNm;
        delete paramData.mallDivNm;
        paramData.patternStartDt = paramData.patternStartDt.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
        paramData.patternEndDt = paramData.patternEndDt.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
        paramData.updatePatternDetlIdList = null;

        //연결상품정보 setting
        paramData.patternGoodsList = fnSetPatternGoodsList();
        if(fnNvl(paramData.patternGoodsList) == '') {
            fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[연결상품]</span> 필수 입력입니다.'});
            return false;
        }
        //패턴상세정보 setting
        paramData.patternDetlList = fnSetPatternDetlList();
        if(fnNvl(paramData.patternDetlList) == '') {
            fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[패턴 정보]</span> 필수 입력입니다.'});
            return false;
        }

        if( paramData.rtnValid && paramData.patternGoodsList != false){
            fnKendoMessage({message : '저장된 패턴정보로 스케쥴이 자동생성됩니다. 진행하시겠습니까?', type : "confirm", ok : function(){

                patternLoading(true);

				fnAjax({
					url     : '/admin/item/meal/addMealPatternInfo',
                    params: paramData,
                    contentType: "application/json",
					success :
						function( data ){
					        patternLoading(false);
							fnKendoMessage({message:"패턴코드 "+data+"가 생성되었습니다.", ok:function(){fnClose();}});
						},
                    isAction: 'batch'
				});
			}});
        }
    }

    // 패턴 저장
    function fnPatternDetailSave(){
        let paramData = $("#inputForm").formSerialize(true);
        paramData.patternCd = $('#patternCd').text();
        paramData.mallDiv = paramData.mallDivNm;
        delete paramData.mallDivNm;
        //패턴상세정보 setting
        paramData.patternDetlList = fnSetPatternDetlList();
        paramData.patternStartDt = paramData.patternStartDt.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
        paramData.patternEndDt = paramData.patternEndDt.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
        paramData.updatePatternDetlIdList = fnNvl(updatePatternDetlIdList) != '' ? Array.from(new Set(updatePatternDetlIdList)) : null;

        if( paramData.rtnValid && paramData.patternDetlList != false){
            fnKendoMessage({message : '수정한 패턴상세정보로 해당 패턴이 등록된 상품의 스케쥴정보가 일괄 업데이트됩니다. 진행하시겠습니까?', type : "confirm", ok : function(){

                patternLoading(true);

				fnAjax({
					url     : '/admin/item/meal/putMealPatternDetail',
                    params: paramData,
                    contentType: "application/json",
					success :
						function( data ){
					        patternLoading(false);
							fnKendoMessage({message:"저장되었습니다.",ok:function(){fnClose();}});
						},
                    isAction: 'batch'
				});
			}});
        }
    };

    // 기본정보 변경
    function fnPatternInfoUpdate(){
        let paramData = $("#inputForm").formSerialize(true);
        paramData.patternCd = $('#patternCd').text();
        delete paramData.patternStartDt;
        delete paramData.patternEndDt;

        //연결상품 setting
        paramData.patternGoodsList = fnSetPatternGoodsList();
        if(fnNvl(paramData.patternGoodsList) == '') {
            fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[연결상품]</span> 필수 입력입니다.'});
            return false;
        }

        if( paramData.rtnValid ){
            fnKendoMessage({message : '패턴을 제외한 기본정보를 변경하시겠습니까?', type : "confirm", ok : function(){
				fnAjax({
					url     : '/admin/item/meal/putMealPatternInfo',
                    params: paramData,
                    contentType: "application/json",
					success :
						function( data ){
							fnKendoMessage({message : '변경되었습니다.'});
							fnDefaultSetting();
						},
                    isAction: 'batch'
				});
			}});
        }
    };

    //연결상품 data setting
    function fnSetPatternGoodsList() {
        var selectGoodsList       = new Array();  // 상품리스트
        var patternGoodsGridData = $('#patternGoodsGrid').data('kendoGrid').dataSource.data();

        if (patternGoodsGridData.length > 0) {
          for (let i = 0; i < patternGoodsGridData.length; i++) {
            let goodsData = new Object();
            goodsData.ilGoodsId   = patternGoodsGridData[i].ilGoodsId;
            selectGoodsList.push(goodsData);
          }
        }
        return selectGoodsList;
    }
    
    //패턴상세정보 data setting
    function fnSetPatternDetlList(patternDetlId) {
        var selectPatternList       = new Array();  // 패턴상세리스트
        var patternGridData = $('#patternGrid').data('kendoGrid').dataSource.data();
        let mallDiv = $('#mallDivNm').data("kendoDropDownList").value();
        let numExp = /^[0-9]*$/;                                                                   // 숫자 허용
        let nmExp = /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\s\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]*$/; // 한글,영문,숫자,특수문자 허용
        let spExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;                           // 특수문자 허용

        if (patternGridData.length > 0) {
          for (let i = 0; i < patternGridData.length; i++) {
            let patternData = new Object();

            patternData.patternCd   = $('#patternCd').text();
            patternData.patternDetlId = patternGridData[i].patternDetlId;
            patternData.patternNo   = patternGridData[i].patternNo;
            patternData.setNo       = patternGridData[i].setNo;
            patternData.setCd       = patternGridData[i].setCd;
            patternData.setNm       = patternGridData[i].setNm;
            patternData.mealContsCd = patternGridData[i].mealContsCd;
            // patternData.modifyDt = patternGridData[i].modifyDt;
            // patternData.modifyId = patternGridData[i].modifyId;

            // patternData validation
            if(fnNvl(patternData.patternNo) == "") {
                fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[패턴순번]</span> 필수 입력입니다.'});
                return false;
            }else if (fnNvl(patternData.setNo) == "") {
                fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[세트순번]</span> 필수 입력입니다.'});
                return false;
            } else if (fnNvl(patternData.setCd) == "" && mallDiv == 'MALL_DIV.BABYMEAL') {
                fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[세트코드]</span> 필수 입력입니다.'});
                return false;
            } else if (fnNvl(patternData.mealContsCd) == "") {
                fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[식단품목코드]</span> 필수 입력입니다.'});
                return false;
            }

            if(fnNvl(patternData.patternNo) != "" && (numExp.test(patternData.patternNo) == false || $.trim(patternData.patternNo).length > 3)) {
                    fnKendoMessage({message : '패턴순번은 최대 3자리의 숫자만 입력 가능합니다.'});
                    return false;
            } else if(fnNvl(patternData.setNo) != "" && (numExp.test(patternData.setNo) == false || $.trim(patternData.setNo).length > 3)) {
                    fnKendoMessage({message : '세트순번은 최대 3자리의 숫자만 입력 가능합니다.'});
                    return false;
            }else if(fnNvl(patternData.setCd) != "" && (numExp.test(patternData.setCd) == false || $.trim(patternData.setCd).length > 10) && mallDiv == 'MALL_DIV.BABYMEAL') {
                    fnKendoMessage({message: '세트코드는 최대 10자리의 숫자만 입력 가능합니다.'});
                    return false;
            }else if(fnNvl(patternData.setNm) != "" && (nmExp.test(patternData.setNm) == false || $.trim(patternData.setNm).length > 15 || !spExp.test(patternData.setNm.charAt(0)) == false)) {
                    fnKendoMessage({message : '세트명은 최대 15자리의 한글,영문,숫자,특수문자만 입력가능합니다. (특수문자는 첫글자 불가)'});
                    return false;
            }else if(fnNvl(patternData.mealContsCd) != "" && (numExp.test(patternData.mealContsCd) == false || $.trim(patternData.mealContsCd).length > 10)) {
                    fnKendoMessage({message : '식단품목코드는 최대 10자리의 숫자만 입력 가능합니다.'});
                    return false;
            }

            if(fnNvl(patternDetlId) != "") {
                if(patternGridData[i].patternDetlId != patternDetlId) {
                    selectPatternList.push(patternData);
                }
            } else {
                selectPatternList.push(patternData);
            }
          }
        } else {
            fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[패턴 정보]</span> 필수 입력입니다.'});
            return false;
        }
        return selectPatternList;
    }
    
    //연결상품 > 삭제버튼
    $('#patternGoodsGrid').on("click", "button[kind=btnPatternGoodsDel]", function(e) {
        var grid =  $('#patternGoodsGrid').data('kendoGrid');
        e.preventDefault();
        let dataItem = grid.dataItem($(e.currentTarget).closest("tr"));
        // 임시데이터이므로 그리드에서만 제거
        fnKendoMessage( {
            message : fnGetLangData({key :'', nullMsg : '<div>삭제하시겠습니까?</div>' })
          , type    : 'confirm'
          , ok      : function(){
                        // 가변 그리드이므로 매번 let dataSource 를 읽음
                        let dataSource =  $('#patternGoodsGrid').data('kendoGrid').dataSource;
                        dataSource.remove(dataItem);
                        //fnMessage('', '제거되었습니다.', '');
                      }
        });
    });
    
    //패턴정보 > 상세버튼
    $('#patternGrid').on("click", "button[kind=btnPatternGridDetl]", function(e) {
        var grid =  $('#patternGrid').data('kendoGrid');
        e.preventDefault();
        let dataItem = grid.dataItem($(e.currentTarget).closest("tr"));
        dataItem.detlMgmType = "PATTERN";
        dataItem.patternCd = $('#patternCd').text();
        dataItem.mallDivNm = $('#mallDivNm').data("kendoDropDownList").text();
        dataItem.mallDiv = $('#mallDivNm').data("kendoDropDownList").value();
        dataItem.patternNm = $('#patternNm').val();
        dataItem.patternStartDt = patternStartDate;
        dataItem.patternEndDt = patternEndDate;

        //패턴상세정보 setting
        dataItem.patternDetlList = fnSetPatternDetlList(dataItem.patternDetlId);

        fnKendoPopup({
                id         : "mealInfoDetailMgmPopup",
                title      : '패턴별 상세 등록/수정',
                width      : "850px",
                height     : "300px",
                src        : "#/mealInfoDetailMgmPopup",
                param      : dataItem,
                success    : function( id, data ){
                            let updatePatternData = data.parameter.updatePatternData;
                            if(fnNvl(updatePatternData) != '') {
                              // 가변 그리드이므로 매번 let dataSource 를 읽음
                                let gridDs =  $('#patternGrid').data('kendoGrid').dataSource;
                                gridDs.remove(dataItem);

                                let gridObj = new Object();
                                gridObj.patternNo = updatePatternData.patternNo;
                                gridObj.setNo = updatePatternData.setNo;
                                gridObj.mealContsCd = updatePatternData.mealContsCd;
                                gridObj.setCd = updatePatternData.setCd;
                                gridObj.setNm = updatePatternData.setNm;
                                gridObj.patternDetlId = updatePatternData.patternDetlId;

                                updatePatternDetlIdList.push(updatePatternData.patternDetlId);

                                // 식단품목명, 알러지여부 조회
                                fnAjax({
                                    url: '/admin/item/meal/getMealPatternUploadData',
                                    params: {mealContsCd: updatePatternData.mealContsCd},
                                    async: false,
                                    success:
                                        function (data) {
                                                gridObj.mealNm = fnNvl(data.mealNm);
                                                gridObj.allergyYn = fnNvl(data.allergyYn) == 'Y' ? 'Y' : '';
                                        },
                                    isAction: 'select'
                                });

                                gridDs.insert(gridObj);

                                gridDs.sort([{field: 'patternNo', dir: 'asc'}, {field: 'setNo', dir: 'asc'}]);
                            }
                }
            });
    });

     // 연결상품 추가
    function btnAddPatternGoods(){
        if(!fnIsEmpty($("#ilGoodsIdSearch").val())) {
            let mallDiv = $('#mallDivNm').data("kendoDropDownList").value() == 'MALL_DIV.BABYMEAL' ? 'MALL_DIV.EATSLIM' : 'MALL_DIV.BABYMEAL';
            fnAjax({
                url     : '/admin/item/meal/checkMealPatternGoods',
                params: {"mallDiv" : mallDiv, "ilGoodsId" : $.trim($('#ilGoodsIdSearch').val())},
                success :
                    function( data ){
                        if(data.messageEnum != "SUCCESS"){
                            fnKendoMessage({ message : data.message });
                        } else {
                            //연결상품 추가 (그리드에만 적용)
                            fnAddPatternGoods(data.data);

                        }
                    },
                isAction: 'batch'
            });
        } else {
            fnKendoMessage({ message : '상품코드를 입력해주세요.'});
        }

    };

    //연결상품 추가 (그리드)
    function fnAddPatternGoods(data) {
        var gridObj;
        var oriCnt = 0;
        var gridDs = $('#patternGoodsGrid').data('kendoGrid').dataSource;
        var gridArr = gridDs.data();

        if (gridArr != undefined && gridArr != null && gridArr.length > 0) {
            oriCnt = gridArr.length;
        }
        if (data != null && data != '') {
            var addIdx = 0;
            if (oriCnt > 0) {
              for (var j = 0; j < gridArr.length; j++) {
                //중복상품인 경우
                if (Number(gridArr[j].ilGoodsId) == data.ilGoodsId) {
                    fnKendoMessage({message : '이미 등록되었거나 존재하지 않는 상품코드입니다.'});
                    return false;
                }
              }
            }

            gridObj = new Object();
            gridObj.ilItemCd = data.ilItemCd;
            gridObj.ilGoodsId = data.ilGoodsId;
            gridObj.goodsNm = data.goodsNm;
            gridObj.mallDivNm = data.mallDivNm;
            gridObj.saleStatusNm = data.saleStatusNm;
            gridObj.displayYnNm = data.displayYnNm;
            gridDs.insert(addIdx, gridObj);
            addIdx++;

            gridDs.sort([{field: 'ilGoodsId', dir: 'asc'}]);
        }
    }

    //패턴 다운로드 (최근 저장된 패턴정보로 다운로드)
	function fnMealInfoExcelDownload(){
        var data = {};
		data.downloadType = '패턴';
		data.mallDivNm = $('#mallDivNm').data("kendoDropDownList").text();
		data.patternCd = $('#patternCd').text();
		data.patternNm = $('#patternNm').val().replace(/(\s*)/g, "");
        data.patternNmExcel = $('#patternNm').val().replace(/(\s*)/g, "");

		fnExcelDownload('/admin/il/meal/getMealPatternExportExcel', data);
	};
    
    // 패턴업로드양식 다운로드
    function fnSampleDownload(){
        document.location.href = "/contents/excelsample/meal/풀무원_일일상품_식단패턴_업로드_양식.xlsx"
    }

    //--------------------------------- Button End---------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){
		
    	// 식단분류
        fnKendoDropDownList({
            id : "mallDivNm",
            tagId : "mallDivNm",
            data : [ {"CODE" : "", "NAME" : "없음"},
                     {"CODE" : "MALL_DIV.BABYMEAL", "NAME" : "베이비밀"},
                     {"CODE" : "MALL_DIV.EATSLIM", "NAME" : "잇슬림"}
                   ]
        });

    	//시작일
		fnKendoDatePicker({
			id    : 'patternStartDt',
			format: 'yyyy-MM-dd',
			defVal : fnGetToday(),
            change: function() {
				fnInputValidateUse('patternStartDt', 'patternEndDt', 'end');

			}
		});

		//종료일
		fnKendoDatePicker({
			id    : 'patternEndDt',
			format: 'yyyy-MM-dd',
			defVal : fnGetMonthAdd(fnGetToday(), 6),
            change: function() {
				fnInputValidateUse('patternStartDt', 'patternEndDt', 'end');

			}
		});

    };

    //---------------Initialize Option Box End ------------------------------------------------

    //-------------------------------  Common Function start -------------------------------
    function fnInputValidateUse(dateSearchStart, dateSearchEnd, picker) {
    //달력 조회 기간 체크
    if(!fnIsEmpty($("#"+dateSearchStart).val()) && !fnIsEmpty($("#"+dateSearchEnd).val())) {
        //달력 시작일 종료일 체크
        if($("#"+ dateSearchStart).val() > $("#"+dateSearchEnd).val()) {
            fnKendoMessage({message : "시작일은 종료일보다 빨라야합니다.", ok : function() {
                    $('#'+dateSearchStart).val(fnGetToday());
                    $('#'+dateSearchEnd).val(fnGetMonthAdd(fnGetToday(), 6));
                    picker == 'start' ?  $("#"+dateSearchStart).focus() :  $("#"+dateSearchEnd).focus();
                }});
            return false;
        }
    }
        return true;
    }

    //연결상품
    function fnInitPatternGoodsGrid() {
    var callUrl          = '';
    // 페이징없는 그리드
    var patternGoodsGridDs = fnGetDataSource({
        url      : '/admin/item/meal/getMealPatternGoodsList?patternCd='+ paramData.patternCd
    });

    var patternGoodsGridOpt = {
          dataSource  : patternGoodsGridDs
        , noRecordMsg : '연결상품 목록이 없습니다.'
        , height : 120
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , resizable   : true
        , autobind    : false
        , columns     : [
                          { field : 'ilItemCd'       , title : '품목코드'      , width:  '100px', attributes : {style : 'text-align:center;'}}
                        , { field : 'ilGoodsId'         , title : '상품코드'      , width: '100px', attributes : {style : 'text-align:center;'}}
                        , { field : 'goodsNm'         , title : '상품명'        , width: '200px', attributes : {style : 'text-align:center;'}}
                        , { field : 'mallDivNm'    , title : '표준 브랜드'      , width:  '80px', attributes : {style : 'text-align:center;'}}
                        , { field : 'saleStatusNm'    , title : '판매상태'      , width:  '80px', attributes : {style : 'text-align:center;'}}
                        , { field : 'displayYnNm'    , title : '전시상태'      , width:  '80px', attributes : {style : 'text-align:center;'}}
                        , { field : 'management'      , title : '관리'          , width:  '80px', attributes : {style : 'text-align:center;'},
                            template : function(dataItem) {
		                            return '<button type="button" class="btn-red btn-s"  kind="btnPatternGoodsDel">삭제</button>';
                            }
                          }
                        ]
    };

    // patternGoodsGrid
    $('#patternGoodsGrid').initializeKendoGrid( patternGoodsGridOpt ).data('kendoGrid');
    $('#patternGoodsGrid').data('kendoGrid').dataSource.read();
  }
  
  
  // 패턴정보
  function fnInitPatternGrid() {
    var callUrl          = '';
    // 페이징없는 그리드
    var patternGridDs = fnGetDataSource({
        url      : '/admin/item/meal/getMealPatternDetailList?patternCd='+ paramData.patternCd
    });

    var patternGridOpt = {
          dataSource  : patternGridDs
        , noRecordMsg : '업로드된 패턴정보가 없습니다.'
        , height : 220
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , resizable   : true
        , autobind    : false
        , columns     : [
                          { field : 'patternNo'       , title : '패턴순번'      , width:  '80px', attributes : {style : 'text-align:center;'}}
                        , { field : 'setNo'         , title : '세트순번'      , width: '80px', attributes : {style : 'text-align:center;'}}
                        , { field : 'setCd'         , title : '세트코드'        , width: '100px', attributes : {style : 'text-align:center;'}}
                        , { field : 'setNm'    , title : '세트명'      , width:  '120px', attributes : {style : 'text-align:center;'}}
                        , { field : 'mealContsCd'    , title : '식단품목코드'      , width:  '100px', attributes : {style : 'text-align:center;'}}
                        , { field : 'mealNm'    , title : '식단품목명'      , width:  '150px', attributes : {style : 'text-align:center;'}}
                        , { field : 'allergyYn'    , title : '알러지 식단'      , width:  '80px', attributes : {style : 'text-align:center;'},
                            template : function(dataItem) {
                                        return dataItem.allergyYn != 'Y' ? '' : 'Y';
                                }
                            }
                        , { field : 'modifyDt'    , title : '최근 개별 수정일'      , width:  '100px', attributes : {style : 'text-align:center;'}}
                        , { field : 'modifyId'    , title : '최근 개별 수정자'      , width:  '100px', attributes : {style : 'text-align:center;'}, hidden : true}
                        , { field : 'management'      , title : '관리'          , width:  '80px', attributes : {style : 'text-align:center;'},
                            template : function(dataItem) {
		                            return '<button type="button" class="btn-white btn-s"  kind="btnPatternGridDetl">상세</button>';
                            }
                          }
                        ]
    };

    // patternGrid
    $('#patternGrid').initializeKendoGrid( patternGridOpt ).data('kendoGrid');
    $('#patternGrid').data('kendoGrid').dataSource.read();
  }

    //-------------------------------  Common Function end -------------------------------
    // 엑셀 업로드 (SheetJs)
    function excelExport(event) {
        // Excel Data => Javascript Object 로 변환
        var input = event.target;
        var reader = new FileReader();
        var gridDs = $('#patternGrid').data('kendoGrid').dataSource;
        var gridArr = gridDs.data();
        var gridObj = new Object();
        var fileName = event.target.files[0].name;
        var fileExtension = fileName.split('.')[1];
        var addIdx = 0;

        var mallDiv = $('#mallDivNm').data("kendoDropDownList").value();
        let numExp = /^[0-9]*$/;                                                                    // 숫자 허용
        let nmExp = /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\s\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]*$/;  // 한글,영문,숫자,특수문자 허용
        let spExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;                            // 특수문자 허용

        //파일확장자 검사
		if(fileExtension != 'xls' && fileExtension != 'xlsx') {
            fnKendoMessage({message : '허용되지 않은 파일종류입니다.'});
            return false;
        }

        reader.onload = function() {
            var fileData = reader.result;
            var wb = XLSX.read(fileData, {
                type : 'binary'
            });

			wb.SheetNames.forEach(function(sheetName) {
			    var excelData = XLSX.utils.sheet_to_json(wb.Sheets[sheetName]);
                excelData.splice(0,1);
                let DataArray = [];

                // 엑셀데이터 유효성검사
                for(let i = 0; i < excelData.length; i++){
                    // 필수값 체크
                    if(Object.keys(excelData[i]).indexOf('패턴순번 (필수)') == -1){
                        fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[패턴순번]</span> 필수 입력입니다.'});
                        return;
                    }
                    if(Object.keys(excelData[i]).indexOf('세트순번 (필수)') == -1){
                        fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[세트순번]</span> 필수 입력입니다.'});
                        return;
                    }
                    if(Object.keys(excelData[i]).indexOf('식단품목코드(필수)') == -1){
                        fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[식단품목코드]</span> 필수 입력입니다.'});
                        return;
                    }
                    if((Object.keys(excelData[i]).indexOf('세트코드(베이비밀 필수)') == -1) && mallDiv == 'MALL_DIV.BABYMEAL'){
                        fnKendoMessage({message : '<span style="color: #FF0000;font-size: 9pt;font-weight: bolder;">[세트코드]</span> 필수 입력입니다.'});
                        return;
                    }
                    
                    // 유효성 체크
                    for(let j = 0; j < Object.keys(excelData[i]).length; j++){
                        let key = Object.keys(excelData[i])[j];
                        let value = Object.values(excelData[i])[j];

                        if(key == '패턴순번 (필수)' && (numExp.test(value) == false || $.trim(value).length > 3)){
                            fnKendoMessage({message : '패턴순번은 최대 3자리의 숫자만 입력 가능합니다.'});
                            return;
                        }
                        if(key == '세트순번 (필수)' && (numExp.test(value) == false || $.trim(value).length > 3)){
                            fnKendoMessage({message : '세트순번은 최대 3자리의 숫자만 입력 가능합니다.'});
                            return;
                        }
                        if(key == '식단품목코드(필수)' && (numExp.test(value) == false || $.trim(value).length > 10)){
                            fnKendoMessage({message : '식단품목코드는 최대 10자리의 숫자만 입력 가능합니다.'});
                            return;
                        }
                        if(key == '세트코드(베이비밀 필수)' && mallDiv == 'MALL_DIV.BABYMEAL' && (numExp.test(value) == false || $.trim(value).length > 10)){
                            fnKendoMessage({message: '세트코드는 최대 10자리의 숫자만 입력 가능합니다.'});
                            return;
                        }
                        if(key == '세트명' && (nmExp.test(value) == false || $.trim(value).length > 15 || !spExp.test(value.charAt(0)) == false)){
                            fnKendoMessage({message : '세트명은 최대 15자리의 한글,영문,숫자,특수문자만 입력가능합니다. (특수문자는 첫글자 불가)'});
                            return;
                        }
                    }
                }

                //패턴정보가 있으면 제거 후 업로드
                if($('#patternGrid').data('kendoGrid')._data.length > 0) {
                    gridDs.data([]);
                }

                var mealContsCdArray = new Array();

                for(let i = 0; i < excelData.length; i++) {
                    for (let j = 0; j < Object.keys(excelData[i]).length; j++) {
                        let key = Object.keys(excelData[i])[j];
                        let value = Object.values(excelData[i])[j];

                        //식단품목명, 알러지식단 데이터 조회
                        if (key == '식단품목코드(필수)') {
                            mealContsCdArray.push(value);
                        }

                    }
                }

                mealContsArrInfo = fnMealContsCdArrayInfo(mealContsCdArray);

                for(let i = 0; i < excelData.length; i++){

                    for(let j = 0; j < Object.keys(excelData[i]).length; j++){
                        let key = Object.keys(excelData[i])[j];
                        let value = Object.values(excelData[i])[j];

                        if(key == '패턴순번 (필수)') gridObj.patternNo = value;
                        if(key == '세트순번 (필수)') gridObj.setNo = value;

                        if(key == '식단품목코드(필수)') {
                            let mealContsCdCorrectInfo = new Object();
                            mealContsCdCorrectInfo = mealContsArrInfo.filter(elem => elem.mealContsCd == value);

                            gridObj.mealContsCd = value;
                            gridObj.mealNm = mealContsCdCorrectInfo[0].mealNm;
                            gridObj.allergyYn = mealContsCdCorrectInfo[0].allergyYn;
                        }
                        if(key == '세트코드(베이비밀 필수)') gridObj.setCd = value;
                        if(key == '세트명') gridObj.setNm = value;
                    }
                    gridDs.insert(addIdx, gridObj);
                    addIdx++;

                    //gridObj 초기화 (중복데이터 방지)
                    gridObj = {};
                }
            })

            patternLoading(false);
		};

        reader.readAsBinaryString(input.files[0]);
    };

    // 엑셀데이터 내 식단품목코드 리스트 조회 (식단품목명, 알러지식단 데이터 조회)
    function fnMealContsCdArrayInfo(mealContsCdArray) {
        var mealContsCdArrayInfo = new Array();
           fnAjax({
                url: '/admin/item/meal/getMealPatternUploadDataList',
                params: {"mealContsCdArray[]": Array.from(new Set(mealContsCdArray))},
                async: false,
                success:
                    function (data) {
                            mealContsCdArrayInfo = data;
                    },
                isAction: 'select'
            });
        return mealContsCdArrayInfo;
    };

    // 업로드 버튼 클릭
    function fnPoRequestExcelUpload(){
        //식단분류 validation
		if($('#mallDivNm').val() == '') {
            fnKendoMessage({message : '식단분류를 선택해주세요.'});
            return false;
        }
    	fileClear();
        $("#uploadFile").trigger("click");
    };

    // 파일 초기화
    function fileClear() {
        var agent = navigator.userAgent.toLowerCase();
        // IE
        if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ) {
            $("#uploadFile").replaceWith($("#uploadFile").clone(true));
            // OTHER
        } else {
            $("#uploadFile").val("");
        }
    }

    function fnInitEventBind(){
        
        // 식단 분류 변경시 연결상품 영역 초기화
        $('#ng-view').on("change", "#mallDivNm", function(e) {

            var patternGoodsGridData = $('#patternGoodsGrid').data('kendoGrid').dataSource.data();

            if (patternGoodsGridData.length > 0) {
                fnKendoMessage({ message : '식단분류 변경 시 연결상품은 초기화 됩니다.', ok:function(){
                        $("#patternGoodsGrid").data("kendoGrid").dataSource.data([]);
                        $("#ilGoodsIdSearch").val('');
                    }});
            }

        });
    }

    function patternLoading(toggle = true) {
        const $loading = $('.pattern-loading');

        if(toggle) {
            $loading.css({
                visibility: 'visible',
            });
        } else {
            $loading.css({
                visibility: 'hidden',
            });
        }
    }

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.btnAddPatternGoods = function(){ btnAddPatternGoods(); }; // 연결상품 추가
    $scope.fnPatternInfoUpdate = function(){ fnPatternInfoUpdate(); }; // 기본정보 변경
    $scope.fnSampleDownload =function(){ fnSampleDownload(); }; // 패턴업로드양식 다운로드
    $scope.fnMealInfoExcelDownload =function(){	fnMealInfoExcelDownload(); }; // 패턴 다운로드
    $scope.fnPatternDetailSave = function(){ fnPatternDetailSave(); }; // 패턴 저장
    $scope.fnSave = function(){ fnSave(); }; // 저장
    $scope.fnClose = function(){ fnClose(); }; // 닫기
    $scope.fnPoRequestExcelUpload =function(){ fnPoRequestExcelUpload(); }; // 업로드 파일첨부 버튼
    $scope.fnExcelUpload = function(event) { patternLoading(true);  excelExport(event);} // 엑셀 업로드 버튼

    //------------------------------- Html 버튼 바인딩  End -------------------------------
    fnInputValidationForAlphabetHangulNumberSpace('patternNm');
}); // document ready - END
