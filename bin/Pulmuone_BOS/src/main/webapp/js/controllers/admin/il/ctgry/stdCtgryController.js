/**-----------------------------------------------------------------------------
 * description      : 시스템관리 - 공통코드
 * @
 * @ 수정일      수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.02.03    홍진영          최초생성
 * @
 * **/
'use strict';

var treeView, parentNode, dataSource;

var selectedDepth = 1;
var selectedUid;
var selectedStandardCategoryId;
var selectedData;                           /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
var useAllYn      = 'N';                    // 미사용포함(기본값:N)
var returnableYn  = 'Y';

function compareSort(a, b) {
  return Number(a.sort) - Number(b.sort);
}

$(document).ready(function() {
  //Initialize Page Call
  fnInitialize();

  // ==========================================================================
  // # Initialize PageR
  // ==========================================================================
  function fnInitialize() {
    $scope.$emit('fnIsMenu', { flag : 'true' });

    fnPageInfo({
      PG_ID  : 'stdCtgry',
      callback : fnUI
    });
  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

       fnTranslate();     // comm.lang.js 안에 있는 공통함수 다국어 변환-------

       fnInitButton();    // Initialize Button  -------------------------------

       fnInitOptionBox(); // Initialize radio ---------------------------------

       fnInitTree();      // Initialize Tree ----------------------------------

       fnInitInput();     // Data 초기화 --------------------------------------
  }

  // ==========================================================================
  // # 버튼 초기화
  // ==========================================================================
  function fnInitButton() {
    $('#fnClear, #fnNew, #fnSave, #fnExcelDown').kendoButton();
    $('#fnDel').kendoButton({ enable: false });
  }

  // ==========================================================================
  // # 입력랎 초기화
  // ==========================================================================
  function fnInitInput() {
      $('#inputForm').formClear(true);
      $('#inputForm input[name=parentsCategoryId]').val(0);
      $('#inputForm input[name=depth]').val(1);
      $('#inputForm input[name=gbDicMstId]').val(null);
      $('#inputForm input[name=gbDicMstNm]').val(null);
      // 미사용포함 체크/언체크 설정
      if (useAllYn == 'Y') {
          $('INPUT[name=checkUseAllYn]').prop("checked", true);
      }
      else {
          $('INPUT[name=checkUseAllYn]').prop("checked", false);
      }
      $('#useAllYn').val(useAllYn);

      // 반품가능여부
      $('input:radio[name="returnableYn"]:input[value="'+returnableYn+'"]').prop("checked", true);
  }

  // ---------------Initialize Option Box Start -------------------------------
  // ==========================================================================
  // # fnInitOptionBox
  // ==========================================================================
  function fnInitOptionBox(){

    // ------------------------------------------------------------------------
    // 미사용포함
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id    : 'checkUseAllYn',
        data  : [
            { "CODE" : 'Y' , "NAME" : "미사용포함" }
            ],
            tagId : 'checkUseAllYn',
            chkVal: 'N',
            style : {}
    });
    // ------------------------------------------------------------------------
    // 사용여부
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id    :  "useYn",
        data  : [ { "CODE" : "Y" , "NAME":fnGetLangData({key :"com.msg.yes",nullMsg :'예' })}
                , { "CODE" : "N" , "NAME":fnGetLangData({key :"com.msg.no",nullMsg :'아니오' })}
                ],
        tagId : "useYn",
        chkVal: 'Y',
        style : {}
    });
    // ------------------------------------------------------------------------
    // 반품가능여부
    // ------------------------------------------------------------------------
    fnTagMkRadio({
      id    :  "returnableYn",
      data  : [ { "CODE" : "Y" , "NAME":fnGetLangData({key :"com.msg.yes",nullMsg :'예' })}
              , { "CODE" : "N" , "NAME":fnGetLangData({key :"com.msg.no",nullMsg :'아니오' })}
              ],
      tagId : "returnableYn",
      chkVal: returnableYn,
      style : {}
    });
    // 비활성처리 : click 이벤트 시 alert을 위해 비활성 주석처리
    //$("input[name=returnableYn]").attr('disabled', true);

    // 반품가능여부 숨김 (html에거 기본값 display:none 처리됨)
    //$('#trReturnableYn').hide();

    // ------------------------------------------------------------------------
    // 이벤트 등록
    // ------------------------------------------------------------------------
    // 미사용포함여부
    $("input:checkbox[name=checkUseAllYn]").click(function() {

        if ($("input:checkbox[name=checkUseAllYn]").is(":checked") == true) {
            useAllYn = 'Y';
        }
        else {
            useAllYn = 'N';
        }
        $('#useAllYn').val(useAllYn);

        treeView.destroy();
        fnInitTree();
    });

    //// 반품가능여부 선택 시
    //$("input:radio[name=returnableYn]").click(function() {
    //
    //  //if (returnableYn == 'Y') {
    //    var checkVal = $('input[name="returnableYn"]:checked').val();
    //    if (checkVal != 'Y') {
    //      //alert('현재 변경하실 수 없습니다.');
    //      valueCheck("", "현재 변경하실 수 없습니다.", 'returnableYn');
    //      $('input:radio[name="returnableYn"]:input[value="Y"]').prop("checked", true);
    //    }
    //  //}
    //});

  }
  // ---------------Initialize Option Box End ---------------------------------

  // ==========================================================================
  // # 분류단계 Set
  // ==========================================================================
  function fnDepthNameSet(depth, addStr) {
      var depthNm;

      if (depth == 1) {
          depthNm = addStr + '대분류';
      }
      else if (depth == 2) {
          depthNm = addStr + '중분류';
      }
      else if (depth == 3) {
          depthNm = addStr + '소분류';
      }
      else if (depth == 4) {
          depthNm = addStr + '세분류';
      }
      else {
          depthNm = addStr + '대분류';
      }
      $("#depthNm").text(depthNm);
  }



  // ==========================================================================
  // # fnClear
  // ==========================================================================
  function fnClear() {
    // 트리 선택해제
    //treeView.select($());
    // 초기화
    //fnInitInput();

    /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
    if (selectedData != null && selectedData != undefined && selectedData != 'undefined') {
      // ----------------------------------------------------------------------
      // 조회된 상태
      // ----------------------------------------------------------------------
      // 선택된 데이터가 있는 경우 조회한 값으로 Set (지우지 않음)
      $('#inputForm').bindingForm( {'rows':selectedData}, 'rows' );
    }
    else {
      // ----------------------------------------------------------------------
      // 그룹생성 또는 하위분류생성  신규 상태
      // ----------------------------------------------------------------------
      /* HGRM-2380 - dgyoun : 팜품가능여부 노출 설정 */
      if (selectedDepth == 2) {
        // 신규로 생성하려는 카테고리가 소분류(3depth)인 경우
        $('#trReturnableYn').show();
      }
      else {
        $('#trReturnableYn').hide();
      }
      // * 값 초기화
      // 카테고리명
      $('#standardCategoryName').val('');
      // 카테고리 순서
      $('#sort').val('');
      // 사용여부
      $('input:radio[name="useYn"]:input[value="Y"]').prop("checked", true);
      // 반품가능여부
      $('input:radio[name="returnableYn"]:input[value="Y"]').prop("checked", true);
    }

  }

  // ==========================================================================
  // # 대분류생성
  // ==========================================================================
  function fnNewDepth1() {

      // 트리 선택해제
      treeView.select($());
      // 입력값 초기화
      fnInitInput();
      // 대분류 설정
      //$('#inputForm input[name=parentsCategoryId]').val(1);
      $('#inputForm input[name=depth]').val(1);
      // 포커스 설정
      inputFocus();
      // 분류단계
      $("#depthNm").text('대분류');

      /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
      selectedDepth = 1;
      selectedData = null;

      /* HGRM-2380 - dgyoun : 팜품가능여부 노출 설정 */
      // 소분류(3)에서만 노출되므로 숨김처리
      $('#trReturnableYn').hide();

  }

  // ==========================================================================
  // # 하위분류생성
  // ==========================================================================
  function fnNew() {

    if (selectedDepth >= 4) {
      fnKendoMessage({message : '세분류 카테고리 하위분류를 생성하실 수 없습니다.' });
      return false;
    }


    var data = getTreeSelectData();
    var depth;

    if (data == null || data == undefined || data == 'undefined') {
      // 1depth 생성은 fnNewDepth1 에서 하므로 fnNew에서는 모든 호출에 대해 다 상위 카테고리 체크함
      fnKendoMessage({message : '상위 카테고리를 선택해 주세요.' });
      return false;
    }

    /* HGRM-2390 - dgyoun : validation 체크 후 초기화 처리 */
    // 입력값 초기화
    fnInitInput();

    if( data != undefined ){

        // 생성대상의 상위 카테고리
        $('#inputForm input[name=parentsCategoryId]').val(data.standardCategoryId);
        // 생성대상의 depth
        depth = Number(data.depth);

        $('#inputForm input[name=depth]').val( depth+1 );

        // 분류단계명 Set
        fnDepthNameSet(depth+1, data.standardCategoryName + ' > ');
    }
    inputFocus();

    /* HGRM-2380 - dgyoun : 팜품가능여부 노출 설정 */
    if (selectedDepth == 2) {
      // 신규로 생성하려는 카테고리가 소분류(3depth)인 경우
      $('#trReturnableYn').show();
    }
    else {
      $('#trReturnableYn').hide();
    }

    /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
    selectedData = null;

  }

  // ==========================================================================
  // # fnSave
  // ==========================================================================
  function fnSave() {
      //console.log('# fnSave Start');

      // ----------------------------------------------------------------------
      // 등록/수정 호출 정보
      // ----------------------------------------------------------------------
      var url  = '/admin/goods/category/addCategoryStd';
      var cbId = 'insert';
      var isAction = 'insert';

      if( OPER_TP_CODE == 'U' ){
          url  = '/admin/goods/category/putCategoryStd';
          cbId= 'update';
          isAction = 'update';
      }
      //console.log('# selectedDepth :: ' + selectedDepth);

      // ----------------------------------------------------------------------
      // 화면 동기화
      // ----------------------------------------------------------------------
      var data = $('#inputForm').formSerialize(true);
      //console.log('# data :: ' + JSON.stringify(data));
      //console.log('# fnSave 1111');

      // ----------------------------------------------------------------------
      // Vlidation  Check
      // ----------------------------------------------------------------------
      // 순서
      var sortVal = $('#sort').val();
      if (sortVal != null && sortVal != '' && sortVal != undefined && sortVal != 'undefined') {
          if (isNaN(sortVal) == true) {
              valueCheck("", "카테고리 순서는 숫자만 입력 가능합니다.", 'sort');
          }
      }

      // ----------------------------------------------------------------------
      // 공통Validation Check + 실행
      // ----------------------------------------------------------------------
      if(data.rtnValid){
          fnAjax({
              url     : url,
              params  : data,
              success : function(result){
                          //console.log('# fnSave success... :: ' + JSON.stringify(result));
                          fnBizCallback(cbId, result);
                        },
              isAction : isAction
          });
      }
  }

  // ==========================================================================
  // # fnDel
  // ==========================================================================
  function fnDel() {
      fnKendoMessage({message:fnGetLangData({key :"102938",nullMsg :'카테고리를 삭제하면 데이터 복구가 불가능 합니다. 삭제 하시겠습니까?' }), type : "confirm"
      ,ok : function(){
              var url  = '/admin/goods/category/delCategoryStd';
                var cbId = 'delete';
                var data = $('#inputForm').formSerialize(true);
                if( data.rtnValid ){
                    fnAjax({
                        url     : url,
                        params  : data,
                        success :
                            function(result){
                                fnBizCallback(cbId, result);
                            },
                        isAction : 'update'
                    });
                }
          }
       });
  }

  // ==========================================================================
  // # fnExcelDown
  // ==========================================================================
  function fnExcelDown() {

      // 세션 체크
      if (PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined') {
        console.log('# PG_SESSION :: ' + JSON.stringify(PG_SESSION));
        //location.href = "/layout.html#/goodsMgm?ilGoodsId="+data.ilGoodsId;
        fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function (){
          location.href = "/admVerify.html";
        }});
        return false;
      }
      // 필수체크 해제
      $("#standardCategoryName").prop("required", false);
      $("#sort").prop("required", false);
      // 엑셀다운로드실행
      var data = $('#inputForm').formSerialize(true);
      fnExcelDownload('/admin/goods/category/getExportExcelCategoryStdList', data);
      // 필수체크 원복
      $("#standardCategoryName").prop("required", true);
      $("#sort").prop("required", true);
  }

  // ==========================================================================
  // # inputFocus
  // ==========================================================================
  function inputFocus() {
    $('#standardCategoryName').focus();
  }

  // ==========================================================================
  // # fnInitTree
  // ==========================================================================
  function fnInitTree() {
      //console.log('# fnInitTree Start');

      dataSource = fnKendoTreeDS({
              url      : '/admin/goods/category/getCategoryStdList?useAllYn=' + useAllYn
            , model_id : 'standardCategoryId'
      });

      fnKendoTreeView({
            id            : 'Tree'
          , dataSource    : dataSource
          , dataTextField : 'standardCategoryName'
          , template      : kendo.template($("#treeview-template").html())
          , autoBind      : false
          , dragAndDrop   : false
          , autoScroll    : true
          , change        : function(e){
                              // ----------------------------------------------
                              // 카테고리 선택
                              // ----------------------------------------------
                              //console.log('# change Event');
                              var data = getTreeSelectData();
                              $('#inputForm').bindingForm( {'rows':data}, 'rows' );

                              // depth
                              if (data != null) {

                                /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
                                selectedData = data;

                                if (data.depth != null) {
                                  selectedDepth = data.depth;
                                }

                              //console.log('# selectedDepth :: ' + selectedDepth);
                              // 분류단계 노출 Set
                              fnDepthNameSet(selectedDepth, '');
                              //console.log('# data  :: ' + JSON.stringify(data));
                              //console.log('# depth :: ' + data.depth);
                              // 반품가능여부
                              returnableYn = data.returnableYn;
                              //console.log('# returnableYn :: ' + returnableYn);
                              }
                              // 반품가능여부 노출/숨김 처리 : 소분류인 경우만 노출
                              //console.log('# selectedDepth :: ' + selectedDepth);
                              if (selectedDepth == 3) {
                                $('#trReturnableYn').show();
                              }
                              else {
                                $('#trReturnableYn').hide();
                              }
                            }
          //, dragstart     : function(e){
          //                    console.log('# dragstart Event');
          //                    parentNode = treeView.parent(e.sourceNode);
          //                }
          //, drop        : function(e){
          //                    console.log('# drop Event');
          //                    var bool = false;
          //                    var dropParentNode = treeView.parent(e.dropTarget);
          //
          //                    // 같은 부모에 위치 이동만 가능
          //                    if( parentNode.length > 0 && dropParentNode.length > 0
          //                        && parentNode.data().uid == dropParentNode.data().uid
          //                        && ( e.dropPosition == 'before' || e.dropPosition == 'after' ) ){
          //                        bool = true;
          //                    }
          //
          //                    //최상단 노드일때 예외처리
          //                    if ( parentNode.length == 0
          //                        && ( e.dropPosition == 'before' || e.dropPosition == 'after' ) ){
          //                        bool = true;
          //                    }
          //                    e.setValid(bool);
          //                }
          //, dragend     : function(e){
          //                    console.log('# dragend Event');
          //                    var childrenData;
          //                    var sortData = Array();
          //                    console.log('# parentNode.length :: ' + parentNode.length);
          //                    if( parentNode.length > 0 ){
          //                        childrenData = dataSource.getByUid( parentNode.data().uid ).children.data();
          //                    }else{
          //                        childrenData = dataSource.data();
          //                    }
          //
          //                    for (var i=0; i < childrenData.length; i++) {
          //                        sortData[i] = childrenData[i].standardCategoryId;
          //                    };
          //
          //                    putSort(sortData);
          //                }
      });

      treeView = $('#Tree').data('kendoTreeView');

      // 스크롤 설정
      const maxHeight = 600;
      $("#Tree").css({"maxHeight" : maxHeight + "px"});

      // ----------------------------------------------------------------------
      // 최초 조회 요청
      // ----------------------------------------------------------------------
      //console.log('# 초기 트리 조회');
      dataSource.read({'standardCategoryId':0});

  }

  // ==========================================================================
  // # putSort
  // ==========================================================================
  function putSort(data) {
        var url  = '/admin/goods/category/putCategoryStdSort';
        var cbId = 'sortUpdate';

        if( data.length > 1 ){

            var dataParams = {"updateData":kendo.stringify(data)};

            fnAjax({
                url     : url,
                params  : dataParams,
                success :
                    function(result){
                        fnBizCallback(cbId, result);
                    },
                isAction : 'update'
            });
        }
  }

  // -------------------------------  POPUP START  ----------------------------
  // ==========================================================================
  // # fnPopupButton
  // ==========================================================================
  function fnPopupButton(param) {
    fnKendoPopup({
          id     : 'SAMPLE_POP',
          title  : fnGetLangData({key :"4620",nullMsg :'표준용어 팝업' }),
          src    : '#/dicMst001',
          success: function( id, data ){
              if(data.GB_DIC_MST_ID){
                $('#GB_DIC_MST_ID').val(data.GB_DIC_MST_ID);
                $('#GB_DIC_MST_NM').val(data.BASE_NAME);
              }
            }
      });
  };
  // -------------------------------  POPUP End  ------------------------------



  // ==========================================================================
  // # 콜백합수
  // ==========================================================================
  function fnBizCallback(id, result) {
      //console.log('# fnBizCallback Start ('+id+', data)');
    switch(id){
      case 'insert':
          //console.log('# fnBizCallback.insert');
          // console.log('# result :: ' + JSON.stringify(result));
          addTreeData(result.detail);

        if (result.detail.depth == 1) {
          fnKendoMessage({message : "입력되었습니다." ,ok :fnNewDepth1});

        }
        else {
          fnKendoMessage({message : "입력되었습니다." ,ok :fnNew});
        }

          // 재조회
          //treeView.destroy();
          //fnInitTree();
          break;
      case 'update':
          //console.log('# fnBizCallback.update');
          putTreeData(result.detail);
          fnKendoMessage({message : '수정되었습니다.' });



          // 재조회
          //treeView.destroy();
          //fnInitTree();
          //fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }) });
          break;
      case 'sortUpdate':
          //console.log('# fnBizCallback.sortUpdate');
          break;
      case 'delete':
          //console.log('# fnBizCallback.delete');
          delTreeData(result.detail);
          fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' }) });
          break;
    }
  }

  // ==========================================================================
  // # 트리 항목 선택
  // ==========================================================================
  function getTreeSelectData() {
      //console.log('# Tree 선택');
      var uid = treeView.select().data('uid');
      selectedUid = uid;
      var selectedRowData = dataSource.getByUid(uid);   // 선택한 로우 데이터

      if (selectedRowData != null) {
          //console.log('selected :: ' + JSON.stringify(selectedRowData));
          selectedStandardCategoryId = selectedRowData.standardCategoryId;
          selectedDepth     = selectedRowData.depth;
      }
      else {
          //console.log('# selected is Null');
      }
      //console.log('# selected :: [uid:'+uid+'][standardCategoryId:'+selectedStandardCategoryId+'][depth:'+selectedDepth+']');
      //console.log('# uid data :: ' + JSON.stringify(dataSource.getByUid(uid)));
      return selectedRowData;
  }

  // ==========================================================================
  // # 트리 항목 추가
  // ==========================================================================
  function addTreeData(data){

    const sortOpt = [
      {
        field : "sort", dir : "asc", compare : compareSort
      },
      {
        field : "standardCategoryName", dir : "asc"
      }
    ];

    data.hasChildren = false;
    var uid = treeView.select().data('uid');
    if( uid != undefined ){
      if (data.depth == '0') {
        // 그룹생성인 경우 최상단에 추가
        //treeView.append(data);
        treeView.append(data, null, function() {
          treeView.dataSource.sort(sortOpt);
        });
      }
      else {
        // 하위 분류 생성 시 빈 폴더 없애기
        const target = $(treeView.select());

        if(target.length !== 0){
          const isleaf = treeView.dataItem(treeView.select()).isleaf;

          //자식이 없을 경우
          if( isleaf === 'false' ){
            const folderImage = target.find('.k-icon.k-no-expand');
            folderImage.css('display','none');
            treeView.dataItem(treeView.select()).isleaf = "true";
            treeView.append(data, treeView.select());
          } else {
            treeView.append(data, treeView.select(), function(){
              const $target = treeView.select();

              if($target.length <= 0) return;

              treeView.dataItem($target).children.sort(sortOpt);
            });
          }


        }
          // 하위분류생성이면 선택한 카테고리 하위에 생성
        }
    }
    else{
      // 값이 없을 경우 최상단에 생성
      treeView.append(data, null, function() {
        treeView.dataSource.sort(sortOpt);
      });
    }

  }

  //treeView 정렬 함수
  function setSort(items){
    for(var i=0; i < items.length; i++){
      if(items[i].hasChildren){
        items[i].children.sort({field: "sort", dir: "asc"});
        setSort(items[i].children.view());
      }
    }
  }

  // ==========================================================================
  // # putTreeData - row는 단건
  // ==========================================================================
  function putTreeData(row) {
      dataSource.pushUpdate(row);

      const sortOpt = [
        {
          field : "sort", dir : "asc", compare : compareSort
        },
        {
          field : "standardCategoryName", dir : "asc"
        }
      ];


      const $parent = treeView.parent(treeView.select()).length > 0 ? treeView.parent(treeView.select()) : null;

      if($parent) {
        treeView.dataItem($parent).children.sort(sortOpt);
      } else {
        treeView.dataSource.sort(sortOpt);
      }


  }

  // ==========================================================================
  // # delTreeData
  // ==========================================================================
  function delTreeData() {
      treeView.remove(treeView.select());
  }

  // ==========================================================================
  // # 오류메시지처리
  // ==========================================================================
  function valueCheck(key, nullMsg, ID){
    fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
      $('#'+ID).focus();
    }});

    return false;
  }

  // ------------------------------- Html 버튼 바인딩  Start -----------------------
  /** Common Clear*/
  $scope.fnClear = function( ){ fnClear();};
  /** Common NewGroup*/
  $scope.fnNewDepth1 = function( ){  fnNewDepth1();};
  /** Common New*/
  $scope.fnNew = function( ){  fnNew();};
  /** Common Save*/
  $scope.fnSave = function(){   fnSave();};
  /** Common ExcelDown*/
  $scope.fnExcelDown = function( ){  fnExcelDown();};
  /** Common Delete*/
  $scope.fnDel = function(){   fnDel();};
  /** popup button **/
  $scope.fnPopupButton = function( param ){ fnPopupButton(param); };
  // ------------------------------- Html 버튼 바인딩  End -------------------------

}); // document ready - END
