package kr.co.pulmuone.v1.comm.constants;

/**
 * <PRE>
 * Forbiz Korea
 * 모바일 앱에서 사용하는 상수 정의
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0		20210201		허성민              최초작성
 * <p>
 * =======================================================================
 * </PRE>
 */
public class AppConstants {
	public static final String APP_SCHEME = "pulmuone://";
    public static final String OS_TYPE = "osType";
    public static final String DEVICE_ID = "deviceId";
    public static final String ANDROID = "a";
    public static final String APP_IOS_VERSION = "APP_IOS_VERSION";
    public static final String APP_IOS_UPDATE_REQUIRED_YN = "APP_IOS_UPDATE_REQUIRED_YN";
    public static final String APP_ANDROID_VERSION = "APP_ANDROID_VERSION";
    public static final String APP_ANDROID_UPDATE_REQUIRED_YN = "APP_ANDROID_UPDATE_REQUIRED_YN";
    public static final String APP_ANDROID_EVENT_IMG_URL = "APP_ANDROID_EVENT_IMG_URL";
    public static final String APP_ANDROID_EVENT_IMG_YN = "APP_ANDROID_EVENT_IMG_YN";
    public static final String APP_IOS_EVENT_IMG_URL = "APP_IOS_EVENT_IMG_URL";
    public static final String APP_IOS_EVENT_IMG_YN = "APP_IOS_EVENT_IMG_YN";
    public static final String APP_IOS_STORE_ID = "APP_IOS_STORE_ID";
    public static final String APP_IOS = "APP_IOS";
    public static final String APP_ANDROID = "APP_ANDROID";

    public enum WriteType {
        INSERT, DELETE
    }
}
