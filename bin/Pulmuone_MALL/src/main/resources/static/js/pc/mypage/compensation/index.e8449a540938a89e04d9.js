(function(t){function e(e){for(var a,o,_=e[0],r=e[1],c=e[2],m=0,p=[];m<_.length;m++)o=_[m],Object.prototype.hasOwnProperty.call(i,o)&&i[o]&&p.push(i[o][0]),i[o]=0;for(a in r)Object.prototype.hasOwnProperty.call(r,a)&&(t[a]=r[a]);l&&l(e);while(p.length)p.shift()();return n.push.apply(n,c||[]),s()}function s(){for(var t,e=0;e<n.length;e++){for(var s=n[e],a=!0,_=1;_<s.length;_++){var r=s[_];0!==i[r]&&(a=!1)}a&&(n.splice(e--,1),t=o(o.s=s[0]))}return t}var a={},i={"pc/mypage/compensation/index":0},n=[];function o(e){if(a[e])return a[e].exports;var s=a[e]={i:e,l:!1,exports:{}};return t[e].call(s.exports,s,s.exports,o),s.l=!0,s.exports}o.m=t,o.c=a,o.d=function(t,e,s){o.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:s})},o.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},o.t=function(t,e){if(1&e&&(t=o(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var s=Object.create(null);if(o.r(s),Object.defineProperty(s,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var a in t)o.d(s,a,function(e){return t[e]}.bind(null,a));return s},o.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return o.d(e,"a",e),e},o.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},o.p="/";var _=window["webpackJsonp"]=window["webpackJsonp"]||[],r=_.push.bind(_);_.push=e,_=_.slice();for(var c=0;c<_.length;c++)e(_[c]);var l=r;n.push([134,"chunk-commons"]),s()})({134:function(t,e,s){t.exports=s("d4de")},"66b25":function(t,e,s){},a51b:function(t,e,s){"use strict";var a=s("66b25"),i=s.n(a);i.a},d4de:function(t,e,s){"use strict";s.r(e);var a=s("5530"),i=(s("e260"),s("e6cf"),s("cca6"),s("a79d"),function(){var t=this,e=t.$createElement,s=t._self._c||e;return t.userSession.isLogin?s("main",{staticClass:"fb__mypage fb__mypage__compensation"},[s("fb-header",{attrs:{type:"sub"}}),s("div",{ref:"scrollWrapper",staticClass:"product-layout__body"},[s("section",{staticClass:"product-layout__body__left"},[s("fb-mypage-menu")],1),s("section",{staticClass:"product-layout__body__right"},[s("h2",{staticClass:"fb__mypage__title"},[t._v(t._s(t.header.title))]),t.$FB_CODES.FETCHES.WAIT===t.fetches.compensationInfo?s("div",{staticClass:"fb__fetching"}):t.$FB_CODES.FETCHES.SUCCESS===t.fetches.compensationInfo?[s("div",{staticClass:"compensation"},[s("div",{staticClass:"compensation__search"},[s("h3",{staticClass:"compensation__search__title"},[t._v("조회기간")]),s("div",{staticClass:"compensation__search__form"},[s("fb-months",{on:{"change-date":t.changeDate}}),s("button",{staticClass:"form__submit",attrs:{type:"button"},on:{click:t.requests}},[t._v("조회하기")])],1)]),s("div",{staticClass:"compensation__filter"},[s("p",{staticClass:"compensation__filter__label register"},[s("span",[t._v("신청완료 "),t.summary.acceptCount?s("em",[t._v(t._s(t.summary.acceptCount))]):t._e()])]),s("p",{staticClass:"compensation__filter__label onGoing"},[s("span",[t._v("신청확인중 "),t.summary.confirmCount?s("em",[t._v(t._s(t.summary.confirmCount))]):t._e()])]),s("p",{staticClass:"compensation__filter__label done"},[s("span",[t._v("처리완료 "),t.summary.completeCount?s("em",[t._v(t._s(t.summary.completeCount))]):t._e()])])]),t.$FB_CODES.FETCHES.WAIT===t.fetches.compensationList?s("div",{staticClass:"fb__fetching"}):t.$FB_CODES.FETCHES.SUCCESS===t.fetches.compensationList?[t.compensationList&&t.compensationList.length>0?[s("ul",{staticClass:"compensation__list"},t._l(t.compensationList,(function(e,a){return s("li",{key:"compensationList-"+a,staticClass:"compensation__item"},[s("div",{staticClass:"item__header"},[s("span",{staticClass:"item__header__date"},[t._v("신청일 "),s("em",[t._v(t._s(e.requestDate))]),t._v(" "),e.odid?s("span",[t._v("("+t._s(e.odid)+")")]):t._e()]),s("p",{staticClass:"item__header__title"},[t._v(t._s(e.rewardName))]),s("div",{staticClass:"item__header__content"},[s("div",{staticClass:"item__header__left"},[e.odid?s("div",{staticClass:"item__goods"},[s("figure",{staticClass:"item__goods__thumb",class:{goodsThumbnailDimmed:t.goodsThumbnailDimmed}},[s("a",{attrs:{href:t.$APP_CONFIG.PAGE_URL.GOODS_VIEW+"?goods="+e.ilGoodsId}},["GREENJUICE"==e.packType?s("img",{attrs:{src:t.$APP_CONFIG.IMAGES.GREEN_JUICE,alt:""}}):"EXHIBIT"==e.packType?s("img",{attrs:{src:t.$APP_CONFIG.IMAGES.FLAT_PRICE,alt:""}}):s("img",{attrs:{src:t.mergeImageHost(e.thumbnailPath),alt:""}})])]),s("div",{staticClass:"item__goods__info"},[t.isStandardPickDelivery(e)?s("span",{staticClass:"info__arrival"},[t._v("도착예정일 "),s("em",[t._v(t._s(e.deliveryDate))])]):t._e(),s("span",{staticClass:"info__date"},[t._v("주문일자 "),s("em",[t._v(t._s(e.orderDate))])]),s("p",{staticClass:"info__title"},[s("a",{attrs:{href:t.goodsLink(e)}},[s("span",{staticClass:"info__title__name"},["NORMAL"!=e.packType?[t._v(" "+t._s(e.packTitle)+" ")]:[t._v(" "+t._s(e.goodsName)+" ")]],2),e.orderGoodsCount?s("span",{staticClass:"info__title__count"},[t._v("외 "),s("em",[t._v(t._s(e.orderGoodsCount)+"건")])]):t._e()])])])]):t._e()]),s("div",{staticClass:"item__header__right"},[[e.isAccept?s("span",{staticClass:"item__header__status"},[t._v("신청완료")]):e.isConfirm?s("span",{staticClass:"item__header__status confirm"},[t._v("신청확인중")]):e.isComplete?s("span",{staticClass:"item__header__status complete"},[t._v("보상완료")]):e.isImpossible?s("span",{staticClass:"item__header__status impossible"},[t._v("보상불가")]):t._e()],s("button",{staticClass:"item__header__toggle",class:{on:e.visible},attrs:{type:"button"},on:{click:function(s){return t.toggleVisible(e)}}},[t._v("상세내용 보이기/감추기")])],2)])]),e.visible?s("div",{staticClass:"item__content"},[s("div",[s("p",{staticClass:"item__detail"},[t._v(t._s(e.rewardApplyContent))]),s("div",{staticClass:"item__attache"},t._l(e.file,(function(e,a){return s("figure",{key:"itemImage-"+a},[s("img",{attrs:{src:t.mergeImageHost(e.imagePath+e.imageName),alt:""}})])})),0)]),e.isAccept?s("div",{staticClass:"item__modify"},[s("a",{attrs:{href:t.$APP_CONFIG.PAGE_URL.CUSTOMER_COMPENSATION_APPLY+"?mod="+e.csRewardApplyId}},[t._v(" 수정")]),s("button",{attrs:{type:"button"},on:{click:function(s){return t.withdraw(e)}}},[t._v("신청철회")])]):t._e(),e.isComplete||e.isImpossible?s("div",{staticClass:"item__modify"},[s("button",{attrs:{type:"button"},on:{click:function(s){return t.deleteApply(e)}}},[t._v("삭제")])]):t._e(),e.isComplete||e.isImpossible?s("div",[s("p",{staticClass:"item__result"},[e.modifyDate?s("span",{staticClass:"item__result__date"},[s("span",[t._v("처리일시")]),s("em",[t._v(t._s(e.modifyDate))])]):t._e(),e.rewardPayType==t.APPLY_PAY_TYPE.POINT&&e.rewardPayDetail||e.answer?s("span",{staticClass:"item__result__detail"},[s("span",[t._v("처리결과")]),s("em",[e.rewardPayType==t.APPLY_PAY_TYPE.POINT?[t._v("적립금 "+t._s(t._f("price")(e.rewardPayDetail))+"원")]:[t._v(t._s(e.answer))]],2)]):t._e()])]):t._e()]):t._e()])})),0),s("div",{staticClass:"compensation__pagination"},[s("fb-pagination",{attrs:{pagination:t.pagination},on:{movePaging:t.movePaging}})],1)]:s("div",{staticClass:"compensation__list empty"},[s("p",[t._v("보상제 신청 내역이 없습니다.")])])]:t._e()],2)]:t.$FB_CODES.FETCHES.ERROR===t.fetches.compensationInfo?[s("error-layout",{attrs:{"error-type":"default"}},[[s("div",{staticClass:"fb__btn-wrap margin"},[s("button",{staticClass:"fb__btn-margin--white",attrs:{type:"button",tabindex:"0"},on:{click:function(e){return t.reload(e,"events")}}},[t._v("새로고침")]),s("button",{staticClass:"fb__btn-margin--green",attrs:{type:"button",tabindex:"0"},on:{click:function(e){return t.goToRoute(t.$APP_CONFIG.PAGE_URL.ROOT)}}},[t._v("홈으로")])])]],2)]:t._e()],2)]),s("fb-footer"),s("fb-alert",{attrs:{open:t.alert.open,message:t.alert.message},on:{"close-alert":t.closeAlert}}),s("fb-confirm",{attrs:{open:t.confirm.open,title:t.confirm.title,message:t.confirm.message,ok:t.confirm.ok},on:{"close-confirm":t.closeConfirm}})],1):t._e()}),n=[],o=s("4ec8"),_=s("dcbe"),r=s("4402"),c=s("7fcc"),l=s("7e74"),m=s("9032"),p=s("9d0b"),u=s("aa0e"),d=s("3540"),f={extends:o["a"],components:{fbHeader:_["a"],fbFooter:r["a"],fbMypageMenu:c["a"],fbPagination:l["a"],fbMonths:m["a"],fbAlert:p["a"],fbConfirm:u["a"],errorLayout:d["a"]}},C=f,v=(s("a51b"),s("2877")),b=Object(v["a"])(C,i,n,!1,null,null,null),g=b.exports,y=s("8f7d"),h=s("79a5"),P=s("caf9");y["a"].use(h["a"]),y["a"].use(P["a"],{error:y["b"].IMAGES.NOT_FOUND,loading:y["b"].IMAGES.IMG_LOADING}),y["c"].components=Object(a["a"])({App:g},y["c"].components),y["d"].dispatch("init").then((function(){return new y["a"](y["c"])}))}});