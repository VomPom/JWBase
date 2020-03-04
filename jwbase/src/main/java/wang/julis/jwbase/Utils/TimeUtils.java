package wang.julis.jwbase.Utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/*******************************************************
 *
 * Created by julis.wang on 2020/03/04 15:33
 *
 * Description : 时间戳工具
 * History   :
 *
 *******************************************************/

public final class TimeUtils {

    // RFC 822 Date Format
    private static final String RFC822_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
    private static final int STATUS_UNFIX = -1;
    private static final int STATUS_FIXING = 0;
    private static final int STATUS_FIXED = 1;
    /**
     * 秒和毫秒转换进制
     */
    public static final long MILLISECONDS_PER_SECOND = 1000L;
    private static int Status = STATUS_UNFIX;
    private static long sTimeOffset = 0;// 秒
    private long mLastSecond = -1;
    private long mDecimal = 9;

    private static void setServerTime(long unixTime) {
        Status = STATUS_FIXED;
        sTimeOffset = unixTime - System.currentTimeMillis() / 1000;
    }

    public static boolean isTimeFixed() {
        return Status != STATUS_UNFIX;
    }



    /**
     * get the time offset for specified time
     *
     * @param unixTime time get from server
     * @return elapsed time in seconds, if < 0, then the time is not reached
     */
    public static long getTimeOffset(long unixTime) {
        long now = System.currentTimeMillis() / 1000 + sTimeOffset;
        return now - unixTime;
    }

    /**
     * Time is up?
     *
     * @param unixTime
     * @return true time is reached
     * false time is not reached
     */
    public static boolean isTimeReached(long unixTime) {
        long now = System.currentTimeMillis() / 1000 + sTimeOffset;
        return now - unixTime > 0 ? true : false;
    }

    /**
     * 时间还没到(商品还没开始)
     * <p>
     * 注：也可使用更通用的{@link #isTimeReached(long)}，虽然写的比较恶心.
     *
     * @param gmtBegin 开始时间戳，单位秒
     * @return true if now > gmtBegin.
     */
    public static boolean isNotBegin(long gmtBegin) {
        return getTimeOffset(gmtBegin) < 0;
    }

    public static final int DAY = 24 * 3600;
    public static final int HOUR = 3600;
    public static final int MINUTE = 60;

    /**
     * date1 - date2
     *
     * @param date1
     * @param date2
     * @return 相隔时间
     */
    public static String getTimeDivide(long date1, long date2) {

        long offset = Math.abs(date1 - date2) / 1000;
        return getTimeDivide(offset);
    }

    /**
     * change the time to string
     *
     * @param offsetInSeconds
     * @return
     */
    public static String getTimeDivide(long offsetInSeconds) {
        if (offsetInSeconds < 0) {
            offsetInSeconds = 0 - offsetInSeconds;
        }

        if (offsetInSeconds > DAY) {
            return offsetInSeconds / DAY + "天";
        } else if (offsetInSeconds > HOUR) {
            return offsetInSeconds / HOUR + "小时";
//            return offsetInSeconds / HOUR + ":";
        } else if (offsetInSeconds > MINUTE) {
            return offsetInSeconds / MINUTE + "分钟";
//            return offsetInSeconds / MINUTE + ":";
        } else {
            return offsetInSeconds + "秒";
//            return offsetInSeconds+"";
        }
    }

    public static long getLeftTime(long unixTime) {
        return -TimeUtils.getTimeOffset(unixTime);
    }

    public static String getFormatLeftTime(long unixTime) {
        return formatLeftTime(-TimeUtils.getTimeOffset(unixTime));
    }

    /**
     * 格式化输出剩余时间
     *
     * @param timeLeft
     * @return
     */
    public static String formatLeftTime(long timeLeft) {
        long dayLeft = timeLeft / (24 * 3600);
        timeLeft = timeLeft - dayLeft * 24 * 3600;
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft - minuteLeft * 60;

        StringBuilder sb = new StringBuilder();
        sb.append(dayLeft);
        sb.append("天");
        sb.append(String.format("%02d", hourLeft));
        sb.append("时");
        sb.append(String.format("%02d", minuteLeft));
        sb.append("分");
        sb.append(String.format("%02d", secondLeft));
        sb.append("秒");

        return sb.toString();
    }

    /**
     * 格式化输出剩余时间，0天 不显示天
     *
     * @param timeLeft
     * @return
     */
    public static String formatLeftTime2(long timeLeft) {
        long dayLeft = timeLeft / (24 * 3600);
        timeLeft = timeLeft - dayLeft * 24 * 3600;
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft - minuteLeft * 60;

        StringBuilder sb = new StringBuilder();
        if (dayLeft != 0) {
            sb.append(dayLeft);
            sb.append("天");
        }
        sb.append(String.format("%02d", hourLeft));
        sb.append("时");
        sb.append(String.format("%02d", minuteLeft));
        sb.append("分");
        sb.append(String.format("%02d", secondLeft));
        sb.append("秒");

        return sb.toString();
    }

    /**
     * 格式化输出剩余时间，0天 不显示天
     *
     * @param timeLeft
     * @return X天X小时X分钟X秒
     */
    public static String formatLeftTime3(long timeLeft) {
        long dayLeft = timeLeft / (24 * 3600);
        timeLeft = timeLeft - dayLeft * 24 * 3600;
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft - minuteLeft * 60;

        StringBuilder sb = new StringBuilder();
        if (dayLeft != 0) {
            sb.append(dayLeft);
            sb.append("天");
        }
        sb.append(String.format("%02d", hourLeft));
        sb.append("小时");
        sb.append(String.format("%02d", minuteLeft));
        sb.append("分钟");
        sb.append(String.format("%02d", secondLeft));
        sb.append("秒");

        return sb.toString();
    }

    /**
     * 格式化输出剩余时间，不显示天
     *
     * @param timeLeft
     * @return X小时X分钟X秒
     */
    public static String formatLeftTime4(long timeLeft) {
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft - minuteLeft * 60;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", hourLeft));
        sb.append("小时");
        sb.append(String.format("%02d", minuteLeft));
        sb.append("分钟");
        sb.append(String.format("%02d", secondLeft));
        sb.append("秒");

        return sb.toString();
    }


    /**
     * add by Seven
     * 控制商祥页闹钟显示效果
     */
    public static String formatDetailLeftTime(long timeLeft) {
        if (timeLeft < 0) {
            timeLeft = -timeLeft;
        }
        long dayLeft = timeLeft / (24 * 3600);
        timeLeft = timeLeft - dayLeft * 24 * 3600;
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft - minuteLeft * 60;

        StringBuilder sb = new StringBuilder();
        if (dayLeft != 0) {
            sb.append(dayLeft);
            sb.append("天 ");
        }
        sb.append(String.format("%02d", hourLeft));
        sb.append(":");
        sb.append(String.format("%02d", minuteLeft));
        sb.append(":");
        sb.append(String.format("%02d", secondLeft));

        return sb.toString();
    }

    /**
     * 专场详情页用的倒计时！
     *
     * @param timeLeft
     * @return
     */
    public static String formatBrandLeftTime(long timeLeft) {
        if (timeLeft < 0) {
            timeLeft = -timeLeft;
        }
        long dayLeft = timeLeft / (24 * 3600);
        timeLeft = timeLeft - dayLeft * 24 * 3600;
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft - minuteLeft * 60;

        StringBuilder sb = new StringBuilder();
        if (hourLeft != 0) {
            sb.append(String.format("%02d", hourLeft));
            sb.append(":");
        }
        sb.append(String.format("%02d", minuteLeft));
        sb.append(":");
        sb.append(String.format("%02d", secondLeft));

        return sb.toString();
    }

    /**
     * 剩余的天数
     *
     * @param timeLeft
     * @return
     */
    public static int getDayOfLeft(long timeLeft) {
        if (timeLeft < 0) {
            timeLeft = -timeLeft;
        }
        int dayLeft = (int) (timeLeft / (24 * 3600));
        return dayLeft;
    }

    /**
     * 格式化输出剩余时间
     *
     * @param timeLeft
     * @return
     */
    public static String formatLeftTimeWithoutSecond(long timeLeft) {
        long dayLeft = timeLeft / (24 * 3600);
        timeLeft = timeLeft - dayLeft * 24 * 3600;
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;

        StringBuilder sb = new StringBuilder();
        sb.append(dayLeft);
        sb.append("天");
        sb.append(String.format("%02d", hourLeft));
        sb.append("时");
        sb.append(String.format("%02d", minuteLeft));
        sb.append("分");

        return sb.toString();
    }

    /**
     * 格式化输出剩余时间
     * 输出分和秒
     *
     * @param timeLeft
     * @return
     */
    public static String formatLeftTimeWithoutDay(long timeLeft) {
        long dayLeft = timeLeft / (24 * 3600);
        timeLeft = timeLeft - dayLeft * 24 * 3600;
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", hourLeft));
        sb.append("时");
        sb.append(String.format("%02d", minuteLeft));
        sb.append("分");

        return sb.toString();
    }


    /**
     * 格式化输出剩余时间
     * 不带天和秒
     *
     * @param timeLeft
     * @return
     */
    public static String formatLeftTimeOrder(long timeLeft) {
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", minuteLeft));
        sb.append(":");
        sb.append(String.format("%02d", secondLeft));
        return sb.toString();
    }

    /**
     * 格式化输出剩余时间
     * 不带天,小时为0不显示
     *
     * @param timeLeft
     * @return
     */
    public static String formatLeftTimeHHMMSS(long timeLeft) {


        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        timeLeft = timeLeft - minuteLeft * 60;
        StringBuilder sb = new StringBuilder();

        if (hourLeft > 0) {
            sb.append(String.format("%02d", hourLeft));
            sb.append(":");
        }
        sb.append(String.format("%02d", minuteLeft));
        sb.append(":");
        sb.append(String.format("%02d", timeLeft));
        return sb.toString();
    }

    /**
     * 格式化输出剩余时间
     *
     * @param timeLeft
     * @return
     */
    public static String formatLeftTimeWithOutDay(long timeLeft) {
        if (timeLeft < 0) {
            timeLeft = Math.abs(timeLeft);
        }

        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft - minuteLeft * 60;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", hourLeft));
        sb.append("时");
        sb.append(String.format("%02d", minuteLeft));
        sb.append("分");
        sb.append(String.format("%02d", secondLeft));
        sb.append("秒");

        return sb.toString();
    }

    /**
     * 得到当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(TimeUtils.getTimeOffset(0) * 1000);
        Date date = c.getTime();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        return simple.format(date);
    }

    /**
     * 格式化输出剩余时间
     *
     * @param timeLeft
     * @return
     */
    public static String formatTimer(long timeLeft) {
        if (timeLeft < 0) {
            timeLeft = Math.abs(timeLeft);
        }
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft - minuteLeft * 60;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", hourLeft));
        sb.append(",");
        sb.append(String.format("%02d", minuteLeft));
        sb.append(",");
        sb.append(String.format("%02d", secondLeft));

        return sb.toString();
    }

    /**
     * 格式化输出剩余时间
     *
     * @param timeLeft
     * @return
     */
    public static String formatLeftTimeEndWithMinute(long timeLeft) {
        if (timeLeft < 0) {
            timeLeft = Math.abs(timeLeft);
        }

        long dayLeft = timeLeft / (24 * 3600);
        timeLeft = timeLeft - dayLeft * 24 * 3600;
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = (timeLeft + 59) / 60;

        StringBuilder sb = new StringBuilder();
        if (dayLeft > 0) {
            sb.append(dayLeft);
            sb.append("天");
        }
        sb.append(String.format("%02d", hourLeft));
        sb.append("时");
        sb.append(String.format("%02d", minuteLeft));
        sb.append("分");

        return sb.toString();
    }

    /**
     * 判断是否同一天(自然日)，true表示同一天
     *
     * @param startTime
     * @param nowTime
     * @return boolean
     */
    public static boolean isOneDay(long startTime, long nowTime) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(startTime * 1000);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(nowTime * 1000);
        String time1 = calendar1.get(Calendar.YEAR) + "" + calendar1.get(Calendar.MONTH) + "" + calendar1.get(Calendar.DAY_OF_MONTH);
        String time2 = calendar2.get(Calendar.YEAR) + "" + calendar2.get(Calendar.MONTH) + "" + calendar2.get(Calendar.DAY_OF_MONTH);
        return TextUtils.equals(time1, time2);
    }

    /**
     * 判断是否同一天(自然日)，true表示同一天
     *
     * @param startTime
     * @param nowTime
     * @return boolean
     */
    public static boolean isOneDayInMillis(long startTime, long nowTime) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(startTime);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(nowTime);
        String time1 = calendar1.get(Calendar.YEAR) + "" + calendar1.get(Calendar.MONTH) + "" + calendar1.get(Calendar.DAY_OF_MONTH);
        String time2 = calendar2.get(Calendar.YEAR) + "" + calendar2.get(Calendar.MONTH) + "" + calendar2.get(Calendar.DAY_OF_MONTH);
        return TextUtils.equals(time1, time2);
    }

    /**
     * 判断是否是昨天。
     *
     * @param startTime
     * @param nowTime
     * @return boolean
     */
    public static boolean isDayTomorrow(long nowTime, long startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        String time1 = sdf.format(nowTime * 1000);
        String time2 = sdf.format(startTime * 1000 - 24 * 3600 * 1000);
        return TextUtils.equals(time1, time2);
    }

    /**
     * 判断是否是一年。
     *
     * @param startTime
     * @param nowTime
     * @return boolean
     */
    public static boolean isOneYear(long startTime, long nowTime) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(startTime * 1000);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(nowTime * 1000);

        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        return year1 == year2 ? true : false;
    }

    /**
     * 判断是否是后天。
     *
     * @param startTime
     * @param nowTime
     * @return boolean
     */
    public static boolean isDayAfterTomorrow(long startTime, long nowTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        String time1 = sdf.format(startTime * 1000);
        String time2 = sdf.format(nowTime * 1000 + 48 * 3600 * 1000);
        return time1.compareTo(time2) >= 0;
    }

    /**
     * 将long类型时间转成字符串
     *
     * @param time
     * @return yyyy-MM-dd
     */
    public static String changeLongToString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(time);
    }


    /**
     * 将long类型时间转成字符串
     *
     * @param timeLeft
     * @return HH:MM:ss
     */
    public static String changeLongToString1(long timeLeft) {
        if (timeLeft < 0) {
            timeLeft = Math.abs(timeLeft);
        }
        long hourLeft = timeLeft / 3600;
        timeLeft = timeLeft - hourLeft * 3600;
        long minuteLeft = timeLeft / 60;
        long secondLeft = timeLeft - minuteLeft * 60;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", hourLeft));
        sb.append(":");
        sb.append(String.format("%02d", minuteLeft));
        sb.append(":");
        sb.append(String.format("%02d", secondLeft));

        return sb.toString();
    }


    /**
     * 将long类型时间转成字符串
     *
     * @param time
     * @return yyyy-MM-dd HH:mm
     */
    public static String changeLongToString2(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(time);
    }

    /**
     * 将long类型时间转成字符串
     *
     * @param time
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String changeLongToString3(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(time);
    }

    /**
     * 将long类型时间与现在时间对比时间差转成字符串
     *
     * @param time
     * @return yyyy-MM-dd 或 long ago
     */
    public static String changeLongToNowString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Calendar cal = Calendar.getInstance();
        //判断是否是同一天
        String curDate = sdf.format(cal.getTime());
        String paramDate = sdf.format(time);
        String ftime = "";
        if (TextUtils.equals(curDate, paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        } else {
            return sdf.format(time);
        }
    }

    /**
     * 将long类型时间与现在时间对比时间差转成字符串
     *
     * @param time
     * @return MM-dd HH:mm or long ago
     */
    public static String changeLongToNowString2(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Calendar cal = Calendar.getInstance();
        //判断是否是同一天
        String curDate = sdf.format(cal.getTime());
        String paramDate = sdf.format(time);
        String ftime = "";
        if (TextUtils.equals(curDate, paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        } else {
            return sdf.format(time);
        }
    }

    /**
     * 将long类型时间与现在时间对比时间差转成字符串
     *
     * @param time
     * @return MM-dd HH:mm or long ago
     */
    public static String changeLongToNowString3(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Calendar cal = Calendar.getInstance();
        //判断是否是同一天
        String curDate = sdf.format(cal.getTime());
        String paramDate = sdf.format(time);
        String ftime = "";
        if (curDate.contains(paramDate.substring(0, 5))) {
            int hour = (int) ((cal.getTimeInMillis() - time) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        } else {
            return sdf.format(time);
        }
    }

    public static long getTenClockOfTomorrow() {
        return getTenClock() + 24 * 3600;
    }

    public static long getTenClock() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeUtils.getTimeOffset(0) * 1000);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getNineClockOfTomorrow() {
        return getNineClock() + 24 * 3600;
    }

    public static long getNineClock() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeUtils.getTimeOffset(0) * 1000);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    public static String getFormateTime(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }

    public static String getMonthOfDayTime(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }

    /**
     * 获取自定义格式的时间样式。
     *
     * @param dateformat
     * @param time
     * @return
     */
    public static String getFormateTime(String dateformat, long time) {
        Date data = new Date(time * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);// 可以方便地修改日期格式
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(data);
    }

    public static String getDayOffset(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(TimeUtils.getTimeOffset(0) * 1000);
        long leftTime;
        if (now.before(calendar)) {
            leftTime = now.getTimeInMillis() - calendar.getTimeInMillis();
            return String.format("宝宝距离预产期还有%d天", leftTime / (1000 * 60 * 60 * 24));
        } else {
            leftTime = -calendar.getTimeInMillis() + now.getTimeInMillis();
            int days = (int) (leftTime / (1000 * 60 * 60 * 24));
            int year, month, day;
            if (days < 31) {
                return String.format("宝宝已经%d天了", days);
            } else if (days < 365) {
                if (calendar.get(Calendar.MONTH) < now.get(Calendar.MONTH)) {
                    month = now.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
                    if (calendar.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH) > 0) {
                        day = -calendar.get(Calendar.DAY_OF_MONTH) + now.get(Calendar.DAY_OF_MONTH) + 30;
                    } else {
                        day = -calendar.get(Calendar.DAY_OF_MONTH) + now.get(Calendar.DAY_OF_MONTH);
                    }
                    return String.format("宝宝已经%d个月%d天", month, day);
                } else {
                    if (calendar.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH) > 0) {
                        month = now.get(Calendar.MONTH) - calendar.get(Calendar.MONTH) + 11;
                        day = now.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) + 30;
                    } else {
                        month = now.get(Calendar.MONTH) - calendar.get(Calendar.MONTH) + 12;
                        day = -calendar.get(Calendar.DAY_OF_MONTH) + now.get(Calendar.DAY_OF_MONTH);
                    }
                    return String.format("宝宝已经%d个月%d天", month, day);
                }
            } else {
                year = days / 365;
                days = days % 365;
                if (calendar.get(Calendar.MONTH) < now.get(Calendar.MONTH)) {
                    month = now.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
                    if (calendar.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH) > 0) {
                        day = -calendar.get(Calendar.DAY_OF_MONTH) + now.get(Calendar.DAY_OF_MONTH) + 30;
                    } else {
                        day = -calendar.get(Calendar.DAY_OF_MONTH) + now.get(Calendar.DAY_OF_MONTH);
                    }
                    return String.format("宝宝已经%d年%d个月%d天", year, month, day);
                } else {
                    if (calendar.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH) > 0) {
                        month = now.get(Calendar.MONTH) - calendar.get(Calendar.MONTH) + 11;
                        day = now.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) + 30;
                    } else {
                        month = now.get(Calendar.MONTH) - calendar.get(Calendar.MONTH) + 12;
                        day = -calendar.get(Calendar.DAY_OF_MONTH) + now.get(Calendar.DAY_OF_MONTH);
                    }
                    return String.format("宝宝已经%d年%d个月%d天", year, month, day);
                }
            }
        }
    }

    /**
     * 获取的开抢时间
     * 三种情况（1）10：00开抢  （2）明日10：00开抢 （3）12-11 10：00开抢
     *
     * @param beginTime
     * @return
     */
    public static String getSaleTime(long beginTime) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(beginTime * 1000);

        long now = System.currentTimeMillis() / 1000 + sTimeOffset;//客户端启动后，经服务器校验过的时间，
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(now * 1000);
        Date date = new Date(beginTime * 1000);
        if (calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR) && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {// 判断是否同一天
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf.format(date);
        }

        calendar2.add(Calendar.DATE, 1);//日期往后增加1天
        if (calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR) && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {//判断是否第二天
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return "明日" + sdf.format(date);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }

    /**
     * 单品-商品开抢提示显示时间
     *
     * @param beginTime 商品开始时间(秒)
     * @return 1、如果同一天，返回"09:00"格式；2、不在同一天，返回"11-01"格式.
     */
    public static String getTuanNoticeTime(long beginTime) {
        Calendar calendarBegin = Calendar.getInstance();
        calendarBegin.setTimeInMillis(beginTime * 1000);

        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeInMillis(getDeviceTime());

        Date date = new Date(beginTime * 1000);
        // 判断是否同一天
        if (calendarBegin.get(Calendar.DAY_OF_YEAR) == calendarNow.get(Calendar.DAY_OF_YEAR)
                && calendarBegin.get(Calendar.YEAR) == calendarNow.get(Calendar.YEAR)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return sdf.format(date);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);
    }

    /**
     * 获取hour
     *
     * @param beginTime
     * @return
     */
    public static int getHour(long beginTime) {
        Date date = new Date(beginTime * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String str = sdf.format(date);
        if (str != null) {
            str = str.substring(11, 13);
            return Integer.valueOf(str);
        }
        return -1;
    }

    /**
     * HH:mm
     *
     * @param beginTime
     * @return
     */

    public static String getHourAndMin(long beginTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(beginTime * 1000L);
    }

    /**
     * 获取系统当前时间(毫秒)
     */
    public static long getDeviceTime() {
        return System.currentTimeMillis() + sTimeOffset * 1000;
    }

    public static String showPushTime() {
        Date date = new Date(getDeviceTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(date);

    }

    /**
     * T
     * 判断"XX前"这种格式
     */
    public static String showIMTime(long javaTime) {

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(javaTime);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(TimeUtils.getTimeOffset(0) * 1000);

        Date date = new Date(javaTime);

        String r = "";
        long nowTime = TimeUtils.getTimeOffset(0) * 1000;
        long result = Math.abs(nowTime - javaTime);


        if (result < 60000) {                                    // 一分钟内
            r = "刚刚";
        } else {
            String time1 = calendar1.get(Calendar.YEAR) + "" + calendar1.get(Calendar.MONTH) + "" + calendar1.get(Calendar.DAY_OF_MONTH);
            String time2 = calendar2.get(Calendar.YEAR) + "" + calendar2.get(Calendar.MONTH) + "" + calendar2.get(Calendar.DAY_OF_MONTH);
            if (TextUtils.equals(time1, time2)) {//当天
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                r = "今天 " + sdf.format(date);


            } else if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && !TextUtils.equals(time1, time2)) {//同一年 非同天
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                r = sdf.format(date);

            } else {//一年以前
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                r = sdf.format(date);
            }
        }
        return r;
    }

    public static long getTimeOffset() {
        return sTimeOffset;
    }

    /**
     * 倒计时设置成9点。
     *
     * @param mGmtEnd
     * @return
     */
    public static long setTimeLessThanOneDay(long mGmtEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(TimeUtils.getTimeOffset(0) * 1000);
        //首页的爆款的广告位只允许在24小时以内 //业务不要写道倒计时里边去
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 9) {
            if (mGmtEnd > TimeUtils.getNineClockOfTomorrow()) {
                mGmtEnd = TimeUtils.getNineClockOfTomorrow();
            }
        } else {
            if (mGmtEnd > TimeUtils.getNineClock()) {
                mGmtEnd = TimeUtils.getNineClock();
            }
        }

        return mGmtEnd;
    }

    /**
     * 4.4.0.日期填写规则
     * <p/>
     * (1）宝宝生日,选择［小王子、小公举］时，校验宝宝生日,可选日期：date≦当前日期（需过去时间），需小于18岁。
     * 提交保存时判断：
     * <p/>
     * ①若>当前日期，提示‘宝宝生日不能设置成未来时间哦’
     * ②若大于18岁，提示“宝宝已经成年啦，重新设置生日吧”
     * <p/>
     * (2）预产期
     * 选择［孕育中］时，校验预产期m可选日期：当前日期≦date≦（当前日期＋280天）
     * <p/>
     * 提交保存时判断：
     * ①若为过去时间（<当前日期），提示’预产期不能是过去时间哦，请重新选择’
     * ②若>当前日期＋280天，则提示’预产期太远啦，请填写正确日期’
     */

    /**
     * 是否是未来
     *
     * @param selectTime  , 选择的时间
     * @param currentTime ，今天
     * @return true=未来,
     */
    public static boolean isFuture(long selectTime, long currentTime) {
        //同一天
        if (isOneDayInMillis(selectTime, currentTime)) {
            return false;
        }

        Calendar calCur = Calendar.getInstance();
        calCur.setTimeInMillis(currentTime);
        if (selectTime - calCur.getTimeInMillis() >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断宝宝是否满18岁
     *
     * @param birthDayTime ,生日
     * @param currentTime  ,今天
     * @return 成年true，不成年false
     */
    public static boolean isAdult(long birthDayTime, long currentTime) {
        Calendar calAdult = Calendar.getInstance();
        calAdult.setTimeInMillis(currentTime);
        calAdult.add(Calendar.YEAR, -18);

        //今天是18岁生日
        if (isOneDayInMillis(birthDayTime, calAdult.getTimeInMillis())) {
            return true;
        }
        if (birthDayTime - calAdult.getTimeInMillis() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是过去的时间，
     *
     * @param selectTime,  选择时间
     * @param currentTime, 当前时间
     * @return， true 表示是过去的时间
     */
    public static boolean isPassTime(long selectTime, long currentTime) {
        //同一天
        if (isOneDayInMillis(selectTime, currentTime)) {
            return false;
        }

        //当前时间-选择的时间 < 0
        if ((TimeUtils.getTimeOffset(selectTime / 1000) < 0)) {
            return false;
        }

        return true;
    }

    /**
     * 根据当天的时间，获取收藏列表中的对象距离今天有多久
     *
     * @param num
     * @return
     */
    public static String compareCurrentTime(long num) {
        int days = 0;
        String formatStyle = "yyyy-MM-dd";
        String date1 = "";
        String date2 = "";
        DateFormat df = new SimpleDateFormat(formatStyle);
        date1 = df.format(new Date(num * 1000));
        date2 = df.format(new Date(getDeviceTime()));
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Calendar c3 = Calendar.getInstance();
        Calendar c4 = Calendar.getInstance();
        try {
            c3.setTime(df.parse(date1));
            c4.setTime(df.parse(date2));
        } catch (Exception e3) {
            Log.e("compareCurrentTime", "compareCurrentTime error");
        }
        while (!c3.after(c4)) {
            days++;
            c3.add(Calendar.DAY_OF_YEAR, 1);          // 比较天数，天数+1
        }
        days--;
        if (days <= 30)
            return "最近一个月";
        else if (days > 30 && days <= 60)
            return "一个月前";
        else if (days > 60 && days <= 90)
            return "二个月前";
        else
            return "三个月前";
    }

    /**
     * 是否大于280天
     *
     * @param selectTime  ,选择时间
     * @param currentTime ,今天
     * @return,true = 在280天内,
     */
    public static boolean isBeyond280Day(long selectTime, long currentTime) {
        Calendar cal280 = Calendar.getInstance();
        cal280.setTimeInMillis(currentTime);
        cal280.add(Calendar.DAY_OF_YEAR, +280);

        //同一天
        if (isOneDayInMillis(selectTime, cal280.getTimeInMillis())) {
            return false;
        }

        if ((cal280.getTimeInMillis() - selectTime) >= 0) {
            return false;
        }

        return true;
    }

    /**
     * 当前时间是否在时间区间内
     *
     * @return
     */
    public static boolean isInTime(long begin, long end) {
        if (begin <= 0 || end <= 0) {
            return false;
        }

        //服务器返回的时间是精确到“秒”
        long curTime = TimeUtils.getDeviceTime() / 1000;
        //还没开始
        if (begin > curTime) {
            return false;
        }
        //已经结束
        if (curTime > end) {
            return false;
        }
        return true;
    }

    private static DateFormat getRfc822DateFormat() {
        SimpleDateFormat rfc822DateFormat =
                new SimpleDateFormat(RFC822_DATE_FORMAT, Locale.US);
        rfc822DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return rfc822DateFormat;
    }

    /**
     * Formats Date to GMT string.
     *
     * @param date
     * @return
     */
    public static String formatRfc822Date(Date date) {
        return getRfc822DateFormat().format(date);
    }

    /**
     * 获取GMT格式时间
     *
     * @return EEE, dd MMM yyyy HH:mm:ss 'GMT'
     */
    public static synchronized String currentFixedSkewedTimeInRFC822Format() {
        return formatRfc822Date(new Date(System.currentTimeMillis() + getTimeOffset()));
    }

}
