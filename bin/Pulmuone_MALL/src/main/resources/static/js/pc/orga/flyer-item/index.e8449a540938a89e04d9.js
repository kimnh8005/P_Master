(function(e){function t(t){for(var s,i,o=t[0],l=t[1],c=t[2],_=0,p=[];_<o.length;_++)i=o[_],Object.prototype.hasOwnProperty.call(r,i)&&r[i]&&p.push(r[i][0]),r[i]=0;for(s in l)Object.prototype.hasOwnProperty.call(l,s)&&(e[s]=l[s]);d&&d(t);while(p.length)p.shift()();return n.push.apply(n,c||[]),a()}function a(){for(var e,t=0;t<n.length;t++){for(var a=n[t],s=!0,o=1;o<a.length;o++){var l=a[o];0!==r[l]&&(s=!1)}s&&(n.splice(t--,1),e=i(i.s=a[0]))}return e}var s={},r={"pc/orga/flyer-item/index":0},n=[];function i(t){if(s[t])return s[t].exports;var a=s[t]={i:t,l:!1,exports:{}};return e[t].call(a.exports,a,a.exports,i),a.l=!0,a.exports}i.m=e,i.c=s,i.d=function(e,t,a){i.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:a})},i.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},i.t=function(e,t){if(1&t&&(e=i(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var a=Object.create(null);if(i.r(a),Object.defineProperty(a,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var s in e)i.d(a,s,function(t){return e[t]}.bind(null,s));return a},i.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return i.d(t,"a",t),t},i.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},i.p="/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],l=o.push.bind(o);o.push=t,o=o.slice();for(var c=0;c<o.length;c++)t(o[c]);var d=l;n.push([162,"chunk-commons"]),a()})({162:function(e,t,a){e.exports=a("8de3")},"8de3":function(e,t,a){"use strict";a.r(t);var s=a("5530"),r=(a("e260"),a("e6cf"),a("cca6"),a("a79d"),function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("main",{staticClass:"fb__flyer-item"},[a("fb-header",{attrs:{"page-type":e.headerType,brand:e.mallName}}),e.isPageError&&!e.isReloading?[a("error-layout",{staticClass:"fb__error--main",attrs:{"error-type":"default"}},[[a("div",{staticClass:"fb__btn-wrap margin"},[a("button",{staticClass:"fb__btn-margin--white",attrs:{type:"button",tabindex:"0"},on:{click:e.reloadPage}},[e._v("새로고침")]),a("button",{staticClass:"fb__btn-margin--green",attrs:{type:"button",tabindex:"0"},on:{click:function(t){return e.goToRoute(e.$APP_CONFIG.PAGE_URL.ROOT)}}},[e._v("홈으로")])])]],2)]:e._e(),e.$FB_CODES.FETCHES.WAIT===e.fetches.pageInventory||e.isReloading?a("div",{staticClass:"fb__fetching"}):e.$FB_CODES.FETCHES.SUCCESS===e.fetches.pageInventory?[a("div",{staticClass:"fb__flyer-item__inner"},[e.selectedKeywordGoods?a("section",[e.pageTitle?[a("h2",{staticClass:"fb__page__title"},[e._v(e._s(e.pageTitle.titleName))])]:e._e(),e.isValidateArray(e.keywordWithGoods)?a("section",{staticClass:"fb__section"},[a("nav",{staticClass:"flyer-item__nav fb__category-tab"},[e._l(e.keywordWithGoods,(function(t){return[a("label",{key:t.dpContsId,staticClass:"fb__category-tab__list flyer-item-tab__list"},[a("input",{directives:[{name:"model",rawName:"v-model",value:e.selectedKeywordId,expression:"selectedKeywordId"}],staticClass:"ga_orgamall_flyer_product_page_category",attrs:{type:"radio","data-ga-category":t.titleName},domProps:{value:t.dpContsId,checked:e._q(e.selectedKeywordId,t.dpContsId)},on:{change:function(a){e.selectedKeywordId=t.dpContsId}}}),a("span",{staticClass:"fb__category-tab__btn flyer-item-tab__btn"},[e._v(e._s(t.titleName))])])]}))],2),a("div",{staticClass:"keyWord-item__products"},[a("div",{staticClass:"result__header"},[a("div",{staticClass:"result__total"},[e._v("총 "),a("span",[e._v(e._s(e.totalCount))]),e._v("개")]),a("div",{staticClass:"result__listType"},[a("div",{staticClass:"result__listType__btn"},[a("label",{staticClass:"result__listType__list"},[a("input",{directives:[{name:"model",rawName:"v-model",value:e.viewType,expression:"viewType"}],attrs:{type:"radio"},domProps:{value:["wide"],checked:e._q(e.viewType,["wide"])},on:{change:function(t){e.viewType=["wide"]}}}),a("span",[e._v(" 리스트 ")])]),a("label",{staticClass:"result__listType__photo"},[a("input",{directives:[{name:"model",rawName:"v-model",value:e.viewType,expression:"viewType"}],attrs:{type:"radio"},domProps:{value:["small"],checked:e._q(e.viewType,["small"])},on:{change:function(t){e.viewType=["small"]}}}),a("span",[e._v(" 포토 ")])])])])]),a("div",{staticClass:"result__contents"},[a("fb-product",{staticClass:"fb__product--orga",attrs:{viewType:e.viewType,contentType:"flyerItem","product-list":e.selectedKeywordGoods,PageCd:e.getAdMallKey+"_PC_GNB5",ContentCd:e.selectedCategory&&e.selectedCategory.titleName}})],1)])]):e._e()],2):a("section",{staticClass:"flyeritem-replace"},[a("section",{staticClass:"fb__section"},[a("h2",{staticClass:"fb__page__title"},[e._v(e._s(e.replaceTitle))]),a("figure",{staticClass:"main-banner"},[a("img",{attrs:{src:"/assets/pc/images/main/banner-flyeritem.jpg"},on:{error:function(t){return e.imageLoadError(t)}}})])]),a("section",{staticClass:"fb__section"},[a("h2",{staticClass:"fb__page__title"},[e._v(e._s(e.replaceTitle2))]),a("div",{staticClass:"result__contents"},[a("fb-product",{staticClass:"fb__product--orga",attrs:{viewType:e.viewType,contentType:"flyerItem","product-list":e.replaceItem.goods,PageCd:e.getAdMallKey+"_PC_GNB5",ContentCd:"ReplaceItem"}})],1)])])])]:e._e(),a("fb-footer")],2)}),n=[],i=a("0bf4"),o=a("1026"),l=a("700d"),c=a("dcbe"),d=a("4402"),_=a("8757"),p={name:"flyer-item",extends:o["a"],mixins:[i["a"],l["a"]],components:{fbHeader:c["a"],fbFooter:d["a"],fbProduct:_["a"]},data:function(){return{viewType:["small"]}}},u=p,f=(a("9e6c"),a("2877")),y=Object(f["a"])(u,r,n,!1,null,null,null),b=y.exports,v=a("8f7d"),m=a("caf9");v["a"].use(m["a"],{error:v["b"].IMAGES.NOT_FOUND,loading:v["b"].IMAGES.IMG_LOADING}),v["c"].components=Object(s["a"])({App:b},v["c"].components),v["d"].dispatch("init").then((function(){return new v["a"](v["c"])}))},"9e6c":function(e,t,a){"use strict";var s=a("e9f9"),r=a.n(s);r.a},e9f9:function(e,t,a){}});