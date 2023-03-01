/**-----------------------------------------------------------------------------
 * description 		 : 상품카테고리 관련 공통 FUnc
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.26		임상건   최초생성
 * @
 * **/
	/*카테고리 공통 Function Start!!!!!*/
	//카테고리 리셋
	function resetCtgory(ds) {
		var data = ds.data();

		for (var i = 0; i < data.length; i++) {
			ds.remove(data[i]);
		};
	}
	/*카테고리 공통 Function End!!!!!*/


	/*전시 카테고리 관련 Function Start!!!!!!*/

	//선택된 전시 카테고리 분류 리스트 Grid
	function fnInitCategoryGrid() {
		ctgryGridDs = fnGetEditDataSource({
			url : '',
			model_id : 'ilGoodsCategoryId',
			model_fields : {
				ilGoodsCategoryId : {
					editable : false,
					type : 'number',
					validation : {
						required : true
					}
				},
				categoryFullName : {
					editable : false,
					type : 'string',
					validation : {
						required : true
					}
				},
				ilCtgryId : {
					editable : false,
					type : 'number',
					validation : {
						required : true
					}
				},
				basicYn : {
					editable : true,
					type : 'string',
					validation : {
						required : true
					}
				}
			}
		});

		ctgryGridOpt = {
			dataSource : ctgryGridDs,
			editable : true,
			columns : [{
				field : '',
				title : {
					key : '836',
					nullMsg : '기본'
				},
				width : '10%',
				template : '<input type="radio" name="basicYn" #= basicYn == "Y" ? checked="checked" : "" #/>',
				attributes : {
					style : 'text-align:center'
				}
			}, {
				field : 'categoryFullName',
				title : {
					key : '4410',
					nullMsg : '전시 카테고리 정보'
				},
				width : '75%',
				attributes : {
					style : 'text-align:left'
				}
			}, {
                // PJH Start
                command : [ { text: "삭제", click: function(e) {
                    e.preventDefault();
                    var command = this;

                    const $targetRow = $(e.target).closest("tr");

                    ctgryGrid.select($targetRow);

                    if (ctgryGrid.dataItem(ctgryGrid.select()).basicYn === "Y") {
            			fnKendoMessage({ message : "기본 카테고리는 삭제하실 수 없습니다." });
            			return;
                    }

                    fnKendoMessage({
                        type : "confirm",
                        message : "삭제하시겠습니까?",
                        ok : function() {
                            var dataItem = command.dataItem($(e.target).closest("tr"));
                            var dataSource = $("#goodsDisplayCategoryGrid").data("kendoGrid").dataSource;

                            dataSource.remove(dataItem);
                        },
                        cancel : function() {
                            return;
                        }
                    })
                } } ],
                title : '관리',
                // PJH END
				width : '15%',
				attributes : {
					style : 'text-align:center',
					class : 'forbiz-cell-readonly'
				}
			}]
		};

		ctgryGrid = $('#goodsDisplayCategoryGrid').initializeKendoGrid(ctgryGridOpt).cKendoGrid();
		ctgryGrid.bind('dataBinding', function(e) {
			if (e.action == 'remove' && $('form#inputForm input[name=basicYn]:checked:not(:eq(' + e.index + '))').length == 0) {
				if (ctgryGrid.dataSource.data().length > 0) {
					fnSetCtgryBasic(ctgryGrid.dataSource.data()[0].uid);
				}
			}
		});

		$('#goodsDisplayCategoryGrid').on('change', ':radio', function(e) {
			fnSetCtgryBasic($(e.target).closest('tr').data('uid'));
		});
	}

	function fnSetCtgryBasic(uid) {
		var datas = ctgryGrid.dataSource.data();
		for (var i = 0; i < datas.length; i++) {
			if (uid == datas[i].uid) {
				datas[i].basicYn = "Y";
				datas[i].dirty = true;
			} else {
				datas[i].basicYn = "N";
				datas[i].dirty = true;
			}
		}
	}

	function fnCheckCtgrybyId(ilCtgryId) {
		var datas = ctgryGrid.dataSource.data();
		for (var i = 0; i < datas.length; i++) {
			if (ilCtgryId == datas[i].ilCtgryId) {
				fnKendoMessage({
					message : '동일한 카테고리가 존재합니다.',
					ok : function(e) {
					}
				});
				return true;
			}
		}
		return false;
	}

	function fnCtgrySelect() {

		var ctgry1 = ctgryGrid1.select();
		var ctgry2 = ctgryGrid2.select();
		var ctgry3 = ctgryGrid3.select();
		var ctgry4 = ctgryGrid4.select();

		for (var i = 4; i > 0; i--) {

			//활성 상태일때 이고 리스트가 있을경우
			if (eval('ctgryGridDs' + i + '.data().length') > 0) {

				//선택 완료
				if (eval('ctgry' + i + '.length') > 0) {

					var data = eval("ctgryGrid" + i + ".dataItem(ctgry" + i + ")");
					var CTGRY = {};
					CTGRY['ilCtgryId'] = data.ilCtgryId;

					if (!fnCheckCtgrybyId(CTGRY['ilCtgryId'])) {
						CTGRY['basicYn'] = $('form#inputForm input[name=basicYn]:checked').length > 0 ? "N" : "Y";
						CTGRY['categoryFullName'] = $('#selectCtgoryText').text();
						CTGRY['mallDiv'] = data.mallDiv;

						ctgryGridDs.add(CTGRY);
						$('#goodsDisplayCategoryGridArea').show();
					}
				} else {
					fnKendoMessage({
						//* 문구 확인후 번역 필요
						message : '최종 분류 선택 시 카테고리 추가가 가능합니다.',  // PJH 수정
						ok : function(e) {
							eval('ctgry' + i + '.focus()');
						}
					});
				}
				break;
			}
		};
	}

	function ctgryGridClick1() {
		var params = this.dataItem(this.select());
		ctgryGridDs2.read({
			'ilCtgryId' : params.ilCtgryId
		});
		$("#goodsDisplayCategory3").data("kendoGrid").dataSource.data([]);
		$("#goodsDisplayCategory4").data("kendoGrid").dataSource.data([]);
		setSelectCtgoryText(1);
	}

	function ctgryGridClick2() {
		var params = this.dataItem(this.select());
		ctgryGridDs3.read({
			'ilCtgryId' : params.ilCtgryId
		});
		$("#goodsDisplayCategory4").data("kendoGrid").dataSource.data([]);
		setSelectCtgoryText(2);
	}

	function ctgryGridClick3() {
		var params = this.dataItem(this.select());
		ctgryGridDs4.read({
			'ilCtgryId' : params.ilCtgryId
		});
		setSelectCtgoryText(3);
	}

	function ctgryGridClick4() {
		setSelectCtgoryText(4);
	}

	function setSelectCtgoryText(index) {
		var ctgry1 = ctgryGrid1.select();
		var ctgry2 = ctgryGrid2.select();
		var ctgry3 = ctgryGrid3.select();
		var ctgry4 = ctgryGrid4.select();

		var ctgoryTextArray = [];
		for (var i = index; i > 0; i--) {
			if (eval('ctgryGrid' + i + '.select().length') > 0) {
				ctgoryTextArray[(i - 1)] = eval("ctgryGrid" + i + ".dataItem(ctgryGrid" + i + ".select())").categoryName;
			}
		};
		if (ctgoryTextArray.length > 0) {
			$("#selectCtgoryText-addon").show();
			$('#selectCtgoryText').text(ctgoryTextArray.join(" > "));
		}
	}

	function fnInitCtgry() {
		ctgryGridDs1 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});

		var ctgryGridopt1 = {
			dataSource : ctgryGridDs1,
			change : ctgryGridClick1,
			columns : [{
				field : 'categoryName',
				title : '카테고리1',
				width : '80px',
				filterable : {
					cell : {
						enabled: true,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : ctgryGridDs1
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			scrollable : true,
			height : "225px",
			filterable : {
				mode : "row"
			}
		};
		ctgryGrid1 = $('#goodsDisplayCategory1').initializeKendoGrid(ctgryGridopt1).cKendoGrid();
		ctgryGridDs1.read({
			depth : '1'
		,	mallDiv : 'MALL_DIV.PULMUONE'
		});

		ctgryGridDs2 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt2 = {
			dataSource : ctgryGridDs2,
			change : ctgryGridClick2,
			columns : [{
				field : 'categoryName',
				title : '카테고리2',
				width : '80px',
				filterable : {
					cell : {
						enabled: true,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : ctgryGridDs2
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			filterable : {
				mode : "row"
			},
			fbMessage : 'none',
			scrollable : true,
			height : "225px",
		};
		ctgryGrid2 = $('#goodsDisplayCategory2').initializeKendoGrid(ctgryGridopt2).cKendoGrid();

		ctgryGridDs3 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt3 = {
			dataSource : ctgryGridDs3,
			change : ctgryGridClick3,
			columns : [{
				field : 'categoryName',
				title : '카테고리3',
				width : '80px',
				filterable : {
					cell : {
						enabled: true,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : ctgryGridDs3
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			filterable : {
				mode : "row"
			},
			fbMessage : 'none',
			scrollable : true,
			height : "225px",
		};
		ctgryGrid3 = $('#goodsDisplayCategory3').initializeKendoGrid(ctgryGridopt3).cKendoGrid();

		ctgryGridDs4 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt4 = {
			dataSource : ctgryGridDs4,
			change : ctgryGridClick4,
			columns : [{
				field : 'categoryName',
				title : '카테고리4',
				width : '80px',
				filterable : {
					cell : {
						enabled: true,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : ctgryGridDs4
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			filterable : {
				mode : "row"
			},
			fbMessage : 'none',
			scrollable : true,
			height : "225px",
		};
		ctgryGrid4 = $('#goodsDisplayCategory4').initializeKendoGrid(ctgryGridopt4).cKendoGrid();

		//헤더 라인 삭제
		$('#goodsDisplayCategory1,#goodsDisplayCategory2,#goodsDisplayCategory3,#goodsDisplayCategory4').find('tr:first').remove().end().each(function(){
			$(this).find(".k-grid-content").css("height", "192");
		});
	}
	/*전시 카테고리 관련 Function End!!!!!!*/


	/*몰인몰 카테고리 관련 Function Start!!!!!!*/
	function fnInitMallInMallCategoryGrid() {
		mallInMallCtgryGridDs = fnGetEditDataSource({
			url : '',
			model_id : 'ilGoodsCategoryId',
			model_fields : {
				ilGoodsCategoryId : {
					editable : false,
					type : 'number',
					validation : {
						required : true
					}
				},
				categoryFullName : {
					editable : false,
					type : 'string',
					validation : {
						required : true
					}
				},
				ilCtgryId : {
					editable : false,
					type : 'number',
					validation : {
						required : true
					}
				},
				mallInMallBasicYn : {
					editable : true,
					type : 'string',
					validation : {
						required : true
					}
				}
			}
		});

		mallInMallCtgryGridOpt = {
			dataSource : mallInMallCtgryGridDs,
			editable : true,
			columns : [{
				field : '',
				title : {
					key : '836',
					nullMsg : '기본'
				},
				width : '10%',
				template : '<input type="radio" name="mallInMallBasicYn" #= mallInMallBasicYn == "Y" ? checked="checked" : "" #/>',
				attributes : {
					style : 'text-align:center'
				}
			},{
				field : 'categoryFullName',
				title : {
					key : '4410',
					nullMsg : '몰인몰 카테고리 정보'
				},
				width : '75%',
				attributes : {
					style : 'text-align:left'
				}
			}, {
                // PJH Start
                command : [ { text: "삭제", click: function(e) {
                    e.preventDefault();
                    var command = this;

                    const $targetRow = $(e.target).closest("tr");

                    mallInMallCtgryGrid.select($targetRow);

                    if (mallInMallCtgryGrid.dataItem(mallInMallCtgryGrid.select()).mallInMallBasicYn === "Y") {
            			fnKendoMessage({ message : "기본 카테고리는 삭제하실 수 없습니다." });
            			return;
                    }

                    fnKendoMessage({
                        type : "confirm",
                        message : "삭제하시겠습니까?",
                        ok : function() {
                            var dataItem = command.dataItem($(e.target).closest("tr"));
                            var dataSource = $("#mallInMallCategoryGrid").data("kendoGrid").dataSource;

                            dataSource.remove(dataItem);
                        },
                        cancel : function() {
                            return;
                        }
                    })
                } } ],
                title : '관리',
                // PJH END
				width : '15%',
				attributes : {
					style : 'text-align:center',
					class : 'forbiz-cell-readonly'
				}
			}]
		};

		mallInMallCtgryGrid = $('#mallInMallCategoryGrid').initializeKendoGrid(mallInMallCtgryGridOpt).cKendoGrid();
		mallInMallCtgryGrid.bind('dataBinding', function(e) {
			if (e.action == 'remove' && $('form#inputForm input[name=mallInMallBasicYn]:checked:not(:eq(' + e.index + '))').length == 0) {
				if (mallInMallCtgryGrid.dataSource.data().length > 0) {
					fnSetMallInMallCtgryBasic(mallInMallCtgryGrid.dataSource.data()[0].uid);
				}
			}
		});

		$('#mallInMallCategoryGrid').on('change', ':radio', function(e) {
			fnSetMallInMallCtgryBasic($(e.target).closest('tr').data('uid'));
		});
	}

	function fnSetMallInMallCtgryBasic(uid) {
		var datas = mallInMallCtgryGrid.dataSource.data();
		for (var i = 0; i < datas.length; i++) {
			if (uid == datas[i].uid) {
				datas[i].mallInMallBasicYn = "Y";
				datas[i].dirty = true;
			} else {
				datas[i].mallInMallBasicYn = "N";
				datas[i].dirty = true;
			}
		}
	}

	function fnCheckMallInMallCtgrybyId(ilCtgryId) {
		var datas = mallInMallCtgryGrid.dataSource.data();
		for (var i = 0; i < datas.length; i++) {
			if (ilCtgryId == datas[i].ilCtgryId) {
				fnKendoMessage({
					message : '동일한 카테고리가 존재합니다.',
					ok : function(e) {
					}
				});
				return true;
			}
		}
		return false;
	}

	function fnMallInMallCtgrySelect() {

		var mallInMallCtgry1 = mallInMallCtgryGrid1.select();
		var mallInMallCtgry2 = mallInMallCtgryGrid2.select();
		var mallInMallCtgry3 = mallInMallCtgryGrid3.select();
		var mallInMallCtgry4 = mallInMallCtgryGrid4.select();

		for (var i = 4; i > 0; i--) {

			//활성 상태일때 이고 리스트가 있을경우
			if (eval('mallInMallCtgryGridDs' + i + '.data().length') > 0) {

				//선택 완료
				if (eval('mallInMallCtgry' + i + '.length') > 0) {

					var data = eval("mallInMallCtgryGrid" + i + ".dataItem(mallInMallCtgry" + i + ")");
					var CTGRY = {};
					CTGRY['ilCtgryId'] = data.ilCtgryId;

					if (!fnCheckMallInMallCtgrybyId(CTGRY['ilCtgryId'])) {
						CTGRY['mallInMallBasicYn'] = $('form#inputForm input[name=mallInMallBasicYn]:checked').length > 0 ? "N" : "Y";
						CTGRY['categoryFullName'] = $('#selectMallInMallCtgoryText').text();
						CTGRY['mallDiv'] = data.mallDiv;
						mallInMallCtgryGridDs.add(CTGRY);
						$('#mallInMallCategoryGridArea').show();
					}
				} else {
					fnKendoMessage({
						//* 문구 확인후 번역 필요
						message : '최종 분류 선택 시 카테고리 추가가 가능합니다',
						ok : function(e) {
							eval('mallInMallCtgry' + i + '.focus()');
						}
					});
				}
				break;
			}
		};
	}

	function mallInMallCtgryGridClick1() {
		var params = this.dataItem(this.select());
		mallInMallCtgryGridDs2.read({
			'ilCtgryId' : params.ilCtgryId
		});
		$("#mallInMallCategory3").data("kendoGrid").dataSource.data([]);
		$("#mallInMallCategory4").data("kendoGrid").dataSource.data([]);
		setSelectMallInMallCtgoryText(1);
	}

	function mallInMallCtgryGridClick2() {
		var params = this.dataItem(this.select());
		mallInMallCtgryGridDs3.read({
			'ilCtgryId' : params.ilCtgryId
		});
		$("#mallInMallCategory4").data("kendoGrid").dataSource.data([]);
		setSelectMallInMallCtgoryText(2);
	}

	function mallInMallCtgryGridClick3() {
		var params = this.dataItem(this.select());
		mallInMallCtgryGridDs4.read({
			'ilCtgryId' : params.ilCtgryId
		});
		setSelectMallInMallCtgoryText(3);
	}

	function mallInMallCtgryGridClick4() {
		setSelectMallInMallCtgoryText(4);
	}

	function setSelectMallInMallCtgoryText(index) {
		var mallInMallCtgry1 = mallInMallCtgryGrid1.select();
		var mallInMallCtgry2 = mallInMallCtgryGrid2.select();
		var mallInMallCtgry3 = mallInMallCtgryGrid3.select();
		var mallInMallCtgry4 = mallInMallCtgryGrid4.select();

		var ctgoryTextArray = [];
		for (var i = index; i > 0; i--) {
			if (eval('mallInMallCtgryGrid' + i + '.select().length') > 0) {
				ctgoryTextArray[(i - 1)] = eval("mallInMallCtgryGrid" + i + ".dataItem(mallInMallCtgryGrid" + i + ".select())").categoryName;
			}
		};
		if (ctgoryTextArray.length > 0) {
			$("#selectMallInMallCtgoryText-addon").show();
			$('#selectMallInMallCtgoryText').text(ctgoryTextArray.join(" > "));
		}
	}

	function fnInitMallInMallCtgry() {

		var mallDiv = "";

		fnAjax({
			url		: '/admin/goods/regist/getItemDetail',
			params	: {ilItemCode : ilItemCode, urWarehouseId : urWarehouseId},
			async	: false,
			success	:
				function( data ){
					var urSupplierId = data.row.urSupplierId;
					var urBrandId = data.row.urBrandId;
					mallinmallCategoryId = data.row.mallinmallCategoryId;

					if(urSupplierId == "2"){									//공급처가 올가홀푸드이면
						mallDiv = "MALL_DIV.ORGA";
					}
					else if(urSupplierId == "5" && mallinmallCategoryId){		//공급처가 풀무원 녹즙(PDM) 이고 브랜드가 잇슬림, 베이비밀이면
						mallDiv = data.row.mallinmallCategoryId;
					}
					else{
						mallDiv = "MALL_DIV.NONE";
					}
				}
		});

		mallInMallCtgryGridDs1 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt1 = {
			dataSource : mallInMallCtgryGridDs1,
			change : mallInMallCtgryGridClick1,
			columns : [{
				field : 'categoryName',
				title : '카테고리1',
				width : '80px',
				filterable : {
					cell : {
						enabled: true,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : mallInMallCtgryGridDs1
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			scrollable : true,
			height : "225px",
			filterable : {
				mode : "row"
			}
		};
		mallInMallCtgryGrid1 = $('#mallInMallCategory1').initializeKendoGrid(ctgryGridopt1).cKendoGrid();
		mallInMallCtgryGridDs1.read({
			depth : "1"
		,	mallDiv : mallDiv
		});

		mallInMallCtgryGridDs2 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt2 = {
			dataSource : mallInMallCtgryGridDs2,
			change : mallInMallCtgryGridClick2,
			columns : [{
				field : 'categoryName',
				title : '카테고리2',
				width : '80px',
				filterable : {
					cell : {
						enabled: true,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : mallInMallCtgryGridDs2
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			filterable : {
				mode : "row"
			},
			fbMessage : 'none',
			scrollable : true,
			height : "225px",
		};
		mallInMallCtgryGrid2 = $('#mallInMallCategory2').initializeKendoGrid(ctgryGridopt2).cKendoGrid();

		mallInMallCtgryGridDs3 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt3 = {
			dataSource : mallInMallCtgryGridDs3,
			change : mallInMallCtgryGridClick3,
			columns : [{
				field : 'categoryName',
				title : '카테고리3',
				width : '80px',
				filterable : {
					cell : {
						enabled: true,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : mallInMallCtgryGridDs3
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			filterable : {
				mode : "row"
			},
			fbMessage : 'none',
			scrollable : true,
			height : "225px",
		};
		mallInMallCtgryGrid3 = $('#mallInMallCategory3').initializeKendoGrid(ctgryGridopt3).cKendoGrid();

		mallInMallCtgryGridDs4 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt4 = {
			dataSource : mallInMallCtgryGridDs4,
			change : mallInMallCtgryGridClick4,
			columns : [{
				field : 'categoryName',
				title : '카테고리4',
				width : '80px',
				filterable : {
					cell : {
						enabled: true,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : mallInMallCtgryGridDs4
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			filterable : {
				mode : "row"
			},
			fbMessage : 'none',
			scrollable : true,
			height : "225px",
		};
		mallInMallCtgryGrid4 = $('#mallInMallCategory4').initializeKendoGrid(ctgryGridopt4).cKendoGrid();

		//헤더 라인 삭제
		$('#mallInMallCategory1,#mallInMallCategory2,#mallInMallCategory3,#mallInMallCategory4').find('tr:first').remove().end().each(function(){
			$(this).find(".k-grid-content").css("height", "181");
		});
	}
	/*몰인몰 카테고리 관련 Function End!!!!!!*/