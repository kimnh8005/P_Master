package kr.co.pulmuone.v1.comm.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * ### 작업 Start : YoonHyunhee ###
 * @author Yoon Hyunhee
 *
 */
public class BindUtil {

	public static <T> T bindDto(HttpServletRequest req, Class<T> clazz) throws Exception {
		return (T) convertRequestToObject(req, clazz);
	}

	/**
	 * Grid 조회 ajax - request의 filter,page 정보를 Dto Object로 리턴
	 * @param req
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
		public static Object convertRequestToObject(HttpServletRequest req, Class<?> clazz) throws Exception {
			// request로 부터 받은 grid 검색조건과 page 정보
			String filterString = StringUtil.nvl(req.getParameter("filter[filters][]"));

		// gson - JSON => Object
		Gson gson = new Gson();
		Object returnObject = clazz.newInstance();		// new class

		// filter정보 null체크 후 setting
		if(StringUtil.isNotEmpty(filterString)) {
			returnObject = gson.fromJson(filterString, clazz);
		}

		// page정보 null체크 후 setting
		if(StringUtil.isNotEmpty(req.getParameter("page"))) {
			int page = Integer.parseInt(req.getParameter("page"));
			Method method = returnObject.getClass().getMethod("setPage", new Class[] { int.class });
			method.invoke(returnObject, new Object[] { page });
		}
		if(StringUtil.isNotEmpty(req.getParameter("pageSize"))) {
			int pageSize = Integer.parseInt(req.getParameter("pageSize"));
			Method method2 = returnObject.getClass().getMethod("setPageSize", new Class[] { int.class });
			method2.invoke(returnObject, new Object[] { pageSize });
		}


		// kendo ui 에서 filter[filters][0][field]....  형태로 보내올 경우
		String mapKey = null;
		Map<String, String[]> paramMaps = req.getParameterMap();
		Iterator itr = paramMaps.keySet().iterator();
		while(itr.hasNext()) {
			mapKey = (String) itr.next();

			if(Pattern.matches("(filter\\[filters\\]\\[)\\d+(\\]).*", mapKey)) {
				String fieldName = mapKey.substring(mapKey.lastIndexOf("[")+1, mapKey.lastIndexOf("]"));
				// case1 : filter[filters][0][field] OR // filter[filters][1][filters][0][field]
				if(fieldName.equals("field")) {
					//field명에 숫자가 포함될수 있으므로 마지막 [] 영역을 날려서 filter[filters][0] 형태로 변경후에 index를 찾는다
					String keyName = mapKey.substring(0, mapKey.lastIndexOf("["));
					setterMethodInvoke(returnObject, paramMaps.get(keyName + "[field]")[0].toString(), paramMaps.get(keyName + "[value]")[0]);
				// case2 : filter[filters][0][ST_ROLE_TYPE_ID]
				} else {
					setterMethodInvoke(returnObject, fieldName, paramMaps.get(mapKey)[0]);

				}
			}

		}

		return returnObject;
	}

	public static void setterMethodInvoke(Object targetObejct, String field, Object value) {
		try {
			String setterMethodName = "set" + field.substring(0,1).toUpperCase() + field.substring(1);	// 첫글자 대문자로 바꿔서 setter 메서드명 만들기
			Method[] methods = targetObejct.getClass().getMethods();

			for(Method meth : methods) {
				if(meth.getName().equals(setterMethodName)) {
					Class<?>[] paramTypes = meth.getParameterTypes();
					if(paramTypes.length > 0) {
						String paramClassName = paramTypes[0].getName();

						switch (paramClassName) {
						case "java.lang.String":
							meth.invoke(targetObejct, new Object[] { value.toString() });
							break;

						case "int":
							meth.invoke(targetObejct, new Object[] { (int) value });
							break;

						case "java.lang.Integer":
							meth.invoke(targetObejct, new Object[] { Integer.parseInt(value.toString()) });
							break;

						case "long":
							meth.invoke(targetObejct, new Object[] { (long) value });
							break;

						case "java.lang.Long":
							meth.invoke(targetObejct, new Object[] { Long.parseLong(value.toString()) });
							break;

						default:
							break;
						}

					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * JSON Array => Dto리스트로 변환하여 리턴
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "deprecation", "hiding" })
	public static <T> List<T> convertJsonArrayToDtoList(String jsonString, Class<T> clazz) throws Exception {
		Gson gson = new Gson();

		// String => JsonArray
		JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(jsonString).getAsJsonArray();

        // JsonArray => Dto List
        List<T> returnList =  new ArrayList<T>();
        for(final JsonElement json: array){
            T entity = gson.fromJson(json, clazz);
            returnList.add(entity);
        }

        return returnList;
	}

}
