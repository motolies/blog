package kr.hvy.blog.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class Common {

    public static int getTimeZoneOffset(HttpServletRequest request) {
        String stoff = CookieHelper.findByName(request, "TimeZoneOffset");
        return Integer.parseInt(stoff);
    }

    public static Timestamp getUtcTimestamp() {
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        ZonedDateTime gmt = zdt.withZoneSameInstant(ZoneId.of("GMT"));
        return Timestamp.valueOf(gmt.toLocalDateTime());
    }

    public static byte[] Base64StringIdToBinary(String id) {
        return Base64.getDecoder().decode(id);
    }

    public static byte[] Base64StringBinaryToBinary(byte[] id) {
        return Base64StringIdToBinary(new String(id, 0, id.length));
    }

    public static String BinaryToEncodeBase64(byte[] id) {
        if (id == null)
            return "";
        else
            return Base64.getEncoder().encodeToString(id);
    }

    public static String BinaryToEncodeURIBase64(byte[] id) {
        try {
            return URLEncoder.encode(BinaryToEncodeBase64(id), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static byte[] ObjectToByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bos.close();
            bytes = bos.toByteArray();
        } catch (IOException ex) {
            // TODO: Handle the exception } return bytes;
        }
        return bytes;
    }

    public static Map<String, String[]> getQueryMapWithoutPage(HttpServletRequest req) {
        Map<String, String[]> allRequestParams = new HashMap<>(req.getParameterMap());
        allRequestParams.remove("page");
        return allRequestParams;
    }

    public static Map<String, String[]> getQueryMapWithoutKey(HttpServletRequest req, String... args) {
        Map<String, String[]> allRequestParams = new HashMap<>(req.getParameterMap());
        allRequestParams.remove("page");
        for (String key : args) {
            allRequestParams.remove(key);
        }
        return allRequestParams;
    }

    public static String getQueryStringWithoutPage(HttpServletRequest req) {
        Map<String, String[]> map = getQueryMapWithoutPage(req);
        return urlEncodeUTF8(map);
    }

    public static String getRequestUriAndQueryStringWithoutPage(HttpServletRequest req) {
        String withParam = getQueryStringWithoutPage(req).equals("") ? "" : "?" + getQueryStringWithoutPage(req);
        return String.format("%s%s", req.getRequestURI(), withParam);
    }

    public static String getRequestUriAndQueryStringWithoutPage(HttpServletRequest req, String... args) {
        String withParam = urlEncodeUTF8(getQueryMapWithoutKey(req, args)).equals("") ? "" : "?" + urlEncodeUTF8(getQueryMapWithoutKey(req, args));
        return String.format("%s%s", req.getRequestURI(), withParam);
    }

    public static String urlEncodeUTF8(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey().toString()), urlEncodeUTF8(((String[]) entry.getValue())[0])));
        }
        return sb.toString();
    }

    public static Date addDate(Date date, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, amount);
        return c.getTime();
    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static boolean isStringEmpty(String str) {
        if (str == null) {
            return true;
        } else if (str.equals("")) {
            return true;
        } else if (str.length() == 0) {
            return true;
        }
        return false;
    }

    public static String getLoginAccount() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth.getName();
        } catch (Exception e) {
            return "";
        }
    }

    private static String byteToHex(byte a) {
        return String.format("%02x ", a & 0xff);
    }

    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (final byte b : a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();
    }

    public static String getUUIDString(byte[] id) {
        String str1 = byteToHex(id[3]) + byteToHex(id[2]) + byteToHex(id[1]) + byteToHex(id[0]);
        String str2 = byteToHex(id[5]) + byteToHex(id[4]);
        String str3 = byteToHex(id[7]) + byteToHex(id[6]);
        String str4 = byteArrayToHex(Arrays.copyOfRange(id, 8, 10));
        String str5 = byteArrayToHex(Arrays.copyOfRange(id, 11, 19));

        return str1 + str2 + str3 + str4 + str5;

    }

    public static String getStringDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getReplacedMark(String original) {
        String result = original.replace("<", "&lt").replace(">", "&gt").replace("<", "&lt").replace("\"", "&#34;").replace("|", "&#124;").replace("$", "&#36;").replace("%", "&#37;")
                .replace("'", "&#39;").replace("/", "&#47;").replace("(", "&#40;").replace(")", "&#41;").replace(",", "&#44;");
        return result;
    }

    public static int getTimeMin(String HHmm) {
        int ret = -1;
        String[] time = HHmm.trim().split(":");
        if (time.length == 2) {
            int hour = Integer.parseInt(time[0]);
            int min = Integer.parseInt(time[1]);
            ret = hour * 60 + min;
        }
        return ret;
    }

    public static String getTimeString(int iMin) {
        String ret = "";
        if (iMin == 0) {
            ret = "00:00";
        } else if (iMin > 0) {
            int hour = iMin / 60;
            int min = iMin % 60;
            ret = String.format("%02d:%02d", hour, min);
        }
        return ret;
    }

    public static String getTimeString(int iMin, String hourSuffix, String minSuffix) {
        String ret = "";
        if (iMin == 0) {
            ret = "0시간 0분";
        } else if (iMin > 0) {
            int hour = iMin / 60;
            int min = iMin % 60;
            ret = String.format("%02d%s %02d%s", hour, hourSuffix, min, minSuffix);
        }
        return ret;
    }

    public static String getTimeString(int iMin, String hourSuffix, String minSuffix, Boolean infinity) {
        // 7200 시간이 넘어가면 무제한으로 표기
        String ret = "";
        if (iMin == 0) {
            ret = "0시간 0분";
        } else if (infinity && iMin > 1000000) {
            ret = "무제한";
        } else if (iMin > 0) {
            int hour = iMin / 60;
            int min = iMin % 60;
            ret = String.format("%02d%s %02d%s", hour, hourSuffix, min, minSuffix);
        }
        return ret;
    }

    public static String getTimetoString(Object time, String hourSuffix, String minSuffix) {
        String ret = "";

        if (time != null && !time.equals("")) {

            String timeStr = time.toString();
            String[] arr = timeStr.split(":");
            if (arr.length > 1)
                ret = String.format("%s%s%s%s", Integer.parseInt(arr[0]), hourSuffix, Integer.parseInt(arr[1]), minSuffix);
        }
        return ret;
    }

    public static String getRemainTime(Object total, Object use, String hourSuffix, String minSuffix) {
        String ret = "";
        if (total != null && !total.equals("") && use != null && !use.equals("")) {

            int iMin = Integer.parseInt(total.toString()) - Integer.parseInt(use.toString());

            if (iMin == 0) {
                ret = "0시간 0분";
            } else if (iMin > 1000000) {
                ret = "무제한";
            } else if (iMin > 0) {
                int hour = iMin / 60;
                int min = iMin % 60;
                ret = String.format("%02d%s %02d%s", hour, hourSuffix, min, minSuffix);
            }
        }
        return ret;
    }

    public static String getDateTimetoDateString(Object time, String format) {
        String ret = "";

        if (time != null && !time.equals("")) {

            ret = new SimpleDateFormat(format).format(new Date(((Timestamp) time).getTime()));
        }
        return ret;
    }

    public static int convertStringToIntSecond(String hhmmss) {
        String[] times = hhmmss.split(":");

        int hour = Integer.parseInt(times[0]) * 3600;
        int min = Integer.parseInt(times[1]) * 60;
        int sec = Integer.parseInt(times[2]);

        return hour + min + sec;
    }

    public static String convertIntSecondToTimeString(int time) {
        int hours = time / 3600;
        int minutes = (time % 3600) / 60;
        int seconds = time % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        return timeString;
    }

    public static byte[] getAdminId(HttpServletRequest req) {
        String id = CookieHelper.findByName(req, "i");
        if (StringUtils.isBlank(id))
            return new byte[16];
        else
            return Base64StringIdToBinary(id);
    }

    public static boolean convertStringToBoolean(String value) {
        boolean returnValue = false;
        String strTrue = "true";
        if ("1".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value))
            returnValue = true;
        return returnValue;
    }

}
