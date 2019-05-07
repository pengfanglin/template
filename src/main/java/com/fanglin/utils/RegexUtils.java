package com.fanglin.utils;

import java.util.regex.Pattern;

/**
 * 正则校验工具类
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/16 14:33
 **/
public class RegexUtils {
    /**
     * 邮箱
     */
    private final static String EMAIL = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
    /**
     * 身份证号
     */
    private final static String ID_CARD = "[1-9]\\d{13,16}[a-zA-Z0-9]";
    /**
     * 手机号
     */
    private final static String MOBILE = "(\\+\\d+)?1[34578]\\d{9}$";
    /**
     * 电话
     */
    private final static String PHONE = "(\\+\\d+)?(\\d{3,4}-?)?\\d{7,8}$";
    /**
     * 整数
     */
    private final static String DIGIT = "-?[1-9]\\d+";
    /**
     * 小数
     */
    private final static String DECIMAL = "-?[1-9]\\d+(\\.\\d+)?";
    /**
     * 空格
     */
    private final static String BLANK_SPACE = "\\s+";
    /**
     * 中文
     */
    private final static String CHINESE = "^[\u4E00-\u9FA5]+$";
    /**
     * 生日
     */
    private final static String BIRTHDAY = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
    /**
     * 网址
     */
    private final static String URL = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
    /**
     * 邮编
     */
    private final static String POST_CODE = "[1-9]\\d{5}";
    /**
     * IP地址
     */
    private final static String IP_ADDRESS = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@zuidaima.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     */
    public static boolean checkEmail(String email) {
        return Pattern.matches(EMAIL, email);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     */
    public static boolean checkIdCard(String idCard) {
        return Pattern.matches(ID_CARD, idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     */
    public static boolean checkMobile(String mobile) {
        return Pattern.matches(MOBILE, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     */
    public static boolean checkPhone(String phone) {
        return Pattern.matches(PHONE, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     */
    public static boolean checkDigit(String digit) {
        return Pattern.matches(DIGIT, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     */
    public static boolean checkDecimals(String decimals) {
        return Pattern.matches(DECIMAL, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     */
    public static boolean checkBlankSpace(String blankSpace) {
        return Pattern.matches(BLANK_SPACE, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     */
    public static boolean checkChinese(String chinese) {
        return Pattern.matches(CHINESE, chinese);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     */
    public static boolean checkBirthday(String birthday) {
        return Pattern.matches(BIRTHDAY, birthday);
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     */
    public static boolean checkURL(String url) {
        return Pattern.matches(URL, url);
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     */
    public static boolean checkPostcode(String postcode) {
        return Pattern.matches(POST_CODE, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     */
    public static boolean checkIpAddress(String ipAddress) {
        return Pattern.matches(IP_ADDRESS, ipAddress);
    }

}