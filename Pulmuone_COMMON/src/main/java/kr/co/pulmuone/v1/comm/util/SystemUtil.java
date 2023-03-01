package kr.co.pulmuone.v1.comm.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Component // @Value 어노테이션 사용 위해 @Component 선언
public class SystemUtil {

    @Autowired
    protected SystemUtil(@Value("${module-name}") String moduleNameValue) {
        this.moduleNameValue = moduleNameValue;
        SystemUtil.moduleName = moduleNameValue;
    }

    @SuppressWarnings("unused")
    private String moduleNameValue; // 현재 가동중인 모듈명 : "BOS" 또는 "MALL"
    public static String moduleName; // moduleNameValue 의 static 타입 변수

    public static final String DEFAULT_PROFILE = "local";
    public static final String DEV_PROFILE = "dev";
    public static final String QA_PROFILE = "qa";
    public static final String VER_PROFILE = "ver";
    public static final String PROD_PROFILE = "prod";

    public static final String YML_PATH_SEPARATOR = ",";

    // 모든 profile 이 공유하는 공통 설정 yml 경로
    public static final String COMMON_PROFILE_YML_PATH = "classpath:profiles/application.yml";

    // 각 profile 별 yml 경로
    public static final String LOCAL_PROFILE_YML_PATH = "classpath:profiles/local/application-local.yml";
    private static final String DEV_PROFILE_YML_PATH = "classpath:profiles/develop/application-dev.yml";
    private static final String QA_PROFILE_YML_PATH = "classpath:profiles/qa/application-qa.yml";
    private static final String VER_PROFILE_YML_PATH = "classpath:profiles/ver/application-ver.yml";
    private static final String PROD_PROFILE_YML_PATH = "classpath:profiles/prod/application-prod.yml";

    // 각 profile 별 properties 파일들이 있는 디렉토리 경로 : glob 패턴으로 properties 파일 식별함
    public static final String LOCAL_PROFILE_PROPERTIES_DIRECTORY_PATH = "classpath:profiles/local/properties";
    private static final String DEV_PROFILE_PROPERTIES_DIRECTORY_PATH = "classpath:profiles/develop/properties";
    private static final String QA_PROFILE_PROPERTIES_DIRECTORY_PATH = "classpath:profiles/qa/properties";
    private static final String VER_PROFILE_PROPERTIES_DIRECTORY_PATH = "classpath:profiles/ver/properties";
    private static final String PROD_PROFILE_PROPERTIES_DIRECTORY_PATH = "classpath:profiles/prod/properties";

    // 로딩할 properties 파일들의 glob 패턴
    private static final String PROPERTIES_GLOB_PATTERN = "*.properties";

    // 스프링부트가 사용하는 환경변수명
    public static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    public static final String SPRING_CONFIG_LOCATION = "spring.config.location";

    // 각 모듈에서 src/main/resources/profiles/application.yml 의 module-name 에 선언한 모듈명 목록
    public static final String BOS_MODULE_NAME = "BOS";
    public static final String MALL_MODULE_NAME = "MALL";

    // properties 파일 체크시 사용하는 resolver
    private static final ResourcePatternResolver RESOLVER = //
            ResourcePatternUtils.getResourcePatternResolver(new PathMatchingResourcePatternResolver());

    /*
     * 스프링부트 Application 실행시 프로파일별 YML 경로 동적 세팅
     */
    public static void setApplicationProfile() {

        /*
         * 어플리케이션 실행시 argument 로 전달받은 profile 이 없는 경우 ( 예 : 로컬 환경에서 실행시 )
         * DEFAULT_PROFILE 로 세팅
         */
        String profile = System.getProperty(SPRING_PROFILES_ACTIVE);

        if (profile == null || profile.trim().length() == 0) {

            profile = DEFAULT_PROFILE;
            System.setProperty(SPRING_PROFILES_ACTIVE, profile);

        }

        /*
         * 어플리케이션 실행시 argument 로 전달받은 config 경로가 없는 경우 ( 공통 설정 yml + profile 별 yml ) 경로를
         * 환경변수에 세팅
         */

        if (System.getProperty(SPRING_CONFIG_LOCATION) == null || System.getProperty(SPRING_CONFIG_LOCATION).trim().length() == 0) {

            StringBuilder configLocation = new StringBuilder();

            configLocation.append(COMMON_PROFILE_YML_PATH); // 모든 profile 이 공유하는 공통 설정 yml 경로
            configLocation.append(YML_PATH_SEPARATOR); // yml 경로 구분자 : ","

            switch (profile) { // profile 별 yml, properties 경로 추가

                case DEFAULT_PROFILE:

                    configLocation.append(LOCAL_PROFILE_YML_PATH);

                    setApplicationProperties(LOCAL_PROFILE_PROPERTIES_DIRECTORY_PATH, configLocation);

                    break;

                case DEV_PROFILE:

                    configLocation.append(DEV_PROFILE_YML_PATH);

                    setApplicationProperties(DEV_PROFILE_PROPERTIES_DIRECTORY_PATH, configLocation);

                    break;

                case QA_PROFILE:

                    configLocation.append(QA_PROFILE_YML_PATH);

                    setApplicationProperties(QA_PROFILE_PROPERTIES_DIRECTORY_PATH, configLocation);

                    break;

                case VER_PROFILE:

                    configLocation.append(VER_PROFILE_YML_PATH);

                    setApplicationProperties(VER_PROFILE_PROPERTIES_DIRECTORY_PATH, configLocation);

                    break;

                case PROD_PROFILE:

                    configLocation.append(PROD_PROFILE_YML_PATH);

                    setApplicationProperties(PROD_PROFILE_PROPERTIES_DIRECTORY_PATH, configLocation);

                    break;

                default:

            } // end of switch

            System.setProperty(SPRING_CONFIG_LOCATION, configLocation.toString()); // yml 경로 환경변수에 세팅

        }

    }

    /*
     * 스프링부트 Application 실행시 각 프로파일별 properties 파일 동적 세팅
     */
    private static void setApplicationProperties(String propertiesDirectoryPath, StringBuilder configLocation) {

        /*
         * propertiesDirectoryPath 에 해당하는 디렉토리 경로에 glob 패턴에 해당하는 properties 파일들을 resolver 로 확인함
         *
         * propertiesDirectoryPath 경로가 없거나 해당 경로에 glob 패턴에 해당하는 properties 파일들이 존재하지 않는 경우
         * => java.io.FileNotFoundException 예외 발생 => catch 후 메서드 종료
         *
         */
        try {

            Resource[] properties = RESOLVER.getResources(propertiesDirectoryPath + File.separator + PROPERTIES_GLOB_PATTERN);

            for (Resource propertie : properties) {

                configLocation.append(YML_PATH_SEPARATOR); // yml 경로 구분자 : ","

                // 해당 properties 파일 경로 추가
                configLocation.append(propertiesDirectoryPath); // properties 파일들이 있는 디렉토리 경로
                configLocation.append(File.separator); // 파일경로 구분자
                configLocation.append(propertie.getFilename()); // glob 패턴과 일치하는 properties 파일명

            }

        } catch (IOException e) {
            /*
             * propertiesDirectoryPath 경로가 없거나 해당 경로에 glob 패턴에 해당하는 properties 파일들이 존재하지 않는 경우
             * => 예외 catch 후 메서드 종료
             */
        }

    }

    /*
     * 프로파일 / 가동 OS 별 ResourceHandler / ResourceLocations 지정
     */
    public static void setResourceInfo(ResourceHandlerRegistry registry, String urlPath, String storageLocation) {

        if (System.getProperty(SPRING_PROFILES_ACTIVE) != null) {

            switch (System.getProperty(SPRING_PROFILES_ACTIVE)) {

                case DEFAULT_PROFILE: // 윈도우, 맥 환경

                    // 파일 접근 url / 저장 경로 매핑
                    registry.addResourceHandler(urlPath + "/**") //
                            .addResourceLocations("file:///" + getAbsolutePathByProfile(storageLocation));

                    break;

                case DEV_PROFILE: // 리눅스 환경
                case QA_PROFILE: // 리눅스 환경
                case VER_PROFILE: // 리눅스 환경
                case PROD_PROFILE: // 리눅스 환경
                default:

                    // 파일 접근 url / 저장 경로 매핑
                    registry.addResourceHandler(urlPath + "/**") //
                            .addResourceLocations("file:" + getAbsolutePathByProfile(storageLocation)); //

                    break;

            }

            /*
             * junit test 시 환경 변수 "spring.profiles.active" 값이 null 인 현상이 간혹 발생
             *
             * => ResourceHandler / ResourceLocations 를 local 과 동일하게 지정
             *
             */

        } else {

            // 파일 접근 url / 저장 경로 매핑
            registry.addResourceHandler(urlPath + "/**") //
                    .addResourceLocations("file:///" + getAbsolutePathByProfile(storageLocation)); //

        }
    }

    /*
     * 현재 가동중인 모듈명 반환 : "BOS" 또는 "MALL"
     */
    public static String getCurrentModuleName() {

        switch (SystemUtil.moduleName) {

            case BOS_MODULE_NAME:
                return BOS_MODULE_NAME;

            case MALL_MODULE_NAME:
                return MALL_MODULE_NAME;

            default:
                return null;

        }

    }

    /*
     * 현재 가동중인 모듈의 프로파일을 반환
     */
    public static String getCurrentProfile() {

        if (System.getProperty(SPRING_PROFILES_ACTIVE) != null) {
            return System.getProperty(SPRING_PROFILES_ACTIVE);
        } else {
            return DEFAULT_PROFILE;
        }

    }

    /*
     * 현재 가동중인 모듈 프로젝트의 절대 경로를 반환
     */
    public static String getCurrentModuleProjectPath() {

        return Paths.get("").toFile().getAbsolutePath();
    }

    /*
     * 로컬인 경우, 인자로 받은 상대경로를 조합하여 Master 프로젝트 기준 해당 경로의 절대 경로를 반환,
     *
     * 그외 개발/운영 서버에서는 인자로 받은 경로의 절대 경로를 반환
     */
    public static String getAbsolutePathByProfile(String path) {

        if (System.getProperty(SPRING_PROFILES_ACTIVE) != null) {

            switch (System.getProperty(SPRING_PROFILES_ACTIVE)) {

                case DEFAULT_PROFILE: // 윈도우, 맥 환경

                    // 마스터 프로젝트의 경로 : 현재 모듈 프로젝트의 상위 디렉토리 경로
                    String masterProjectPath = Paths.get(getCurrentModuleProjectPath()).getParent().toAbsolutePath().toString();

                    // ( 마스터 프로젝트 기준 인자로 받은 상대경로의 절대 경로 + 파일경로 구분자 ) 반환
                    return Paths.get(masterProjectPath, path).toAbsolutePath().toString() + File.separator;

                case DEV_PROFILE: // 리눅스 환경
                case QA_PROFILE: // 리눅스 환경
                case VER_PROFILE: // 리눅스 환경
                case PROD_PROFILE: // 리눅스 환경
                default:

                    // 인자로 받은 경로의 절대경로를 반환
                    return Paths.get(path).toAbsolutePath().toString() + File.separator;

            }

            /*
             * junit test 시 환경 변수 "spring.profiles.active" 값이 null 인 현상이 간혹 발생 => local 과 동일하게 지정
             *
             */

        } else {

            // 마스터 프로젝트의 경로 : 현재 모듈 프로젝트의 상위 디렉토리 경로
            String masterProjectPath = Paths.get(getCurrentModuleProjectPath()).getParent().toAbsolutePath().toString();

            // ( 마스터 프로젝트 기준 인자로 받은 상대경로의 절대 경로 + 파일경로 구분자 ) 반환
            return Paths.get(masterProjectPath, path).toAbsolutePath().toString() + File.separator;

        }
    }

    /*
     * Request 요청 IP 받기
     * 2개 이상의 IP가 설정되어 있을 경우, 사설 IP 제거
     */
    public static String getIpAddress(HttpServletRequest req) {
		String strIp = "";
		String addressIp = "";

		if (req == null) { // http reqeust 확인
			return strIp;
		}

		addressIp = req.getHeader("X-FORWARDED-FOR");
		if (addressIp == null) { // X-FORWARDED-FOR IP가 설정되어 있지 않으면 remoteAddr로 설정
			addressIp = req.getRemoteAddr();
		}

		if (addressIp != null) {
			String[] arrIps = addressIp.split(",");
			if (arrIps.length > 1) { // IP가 2개 이상 설정되어 있을 경우 사설 IP 제거
				for (int i=0; i < arrIps.length; i++) {
					if (arrIps[i].indexOf("10.") == -1 && arrIps[i].indexOf("198.") == -1) {
						if (!"".equals(strIp)) { // 2개 이상일 경우 구분자 추가
							strIp += ", ";
						}
						strIp += arrIps[i].trim();
					}
				}

				if ("".equals(strIp)) { // 모두  사설IP인 경우 첫번째 IP를 반환
					strIp = arrIps[0];
				}
			}
			else {
				strIp = arrIps[0];
			}
		}

		return strIp;
	}

}
