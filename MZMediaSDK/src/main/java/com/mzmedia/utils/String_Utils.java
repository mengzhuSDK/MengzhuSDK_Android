package com.mzmedia.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ScaleXSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mengzhu.live.sdk.core.utils.DateUtils;
import com.mengzhu.live.sdk.core.utils.DensityUtil;
import com.mengzhu.live.sdk.core.utils.SpannableClickable;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tv.mengzhu.core.frame.coreutils.DeviceUtil;

public class String_Utils {
    public static final float MUL = 1.0f;

    public static String[] getComment(String comment) {
        ArrayList<String> strings = new ArrayList<String>();
        String s = comment == null ? "" : comment;
        for (int i = 0; i < 5; i++) {
            s = s.replaceAll("\r\n\r\n", "\r\n");
        }
        s = s.replaceAll("\n\n", "\n");
        String regex = "\\[img=\\d+,\\d+\\][^\\[]+\\[/img\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        int lastString = 0;
        while (matcher.find()) {
            String group = matcher.group(0);
            if (lastString != matcher.start())
                strings.add(s.substring(lastString, matcher.start()));
            lastString = matcher.end();
            strings.add(group.replace("[/img]", ""));
        }
        if (lastString != 0 && lastString < s.length()) {
            strings.add(s.substring(lastString));
        }
        if (strings.isEmpty()) {
            strings.add(s);
        }
        String[] t = new String[strings.size()];
        strings.toArray(t);
        return t;
    }

    public static List<String> getCommentPics(String desc) {
        ArrayList<String> strings = new ArrayList<String>();
        String s = desc == null ? "" : desc;
        for (int i = 0; i < 5; i++) {
            s = s.replaceAll("\r\n\r\n", "\r\n");
        }
        s = s.replaceAll("\n\n", "\n");
        String regex = "\\[img=\\d+,\\d+\\][^\\[]+\\[/img\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String string = matcher.group();
            string = string.replace("[/img]", "").replaceAll("\\[.+\\]", "");
            strings.add(string);
            //				s1 = s1.replaceAll(string, "");
            //				System.out.println(string);
        }
        return strings;

    }

    public static List<String> getCommentPics2(String desc) {
        ArrayList<String> strings = new ArrayList<String>();
        String s = desc == null ? "" : desc;
        for (int i = 0; i < 5; i++) {
            s = s.replaceAll("\r\n\r\n", "\r\n");
        }
        s = s.replaceAll("\n\n", "\n");
        String regex = "\\[img=\\d+,\\d+\\][^\\[]+\\[/img\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String string = matcher.group();
            //			string = string.replace("[/img]", "").replaceAll("\\[.+\\]", "");
            strings.add(string);
            //				s1 = s1.replaceAll(string, "");
            //				System.out.println(string);
        }
        return strings;

    }

    public static String getFilterComment(String desc) {
        String s = desc == null ? "" : desc;
        for (int i = 0; i < 5; i++) {
            s = s.replaceAll("\r\n\r\n", "\r\n");
        }
        s = s.replaceAll("\n\n", "\n");
        String regex = "\\[img=\\d+,\\d+\\][^\\[]+\\[/img\\]";
        return s.replaceAll(regex, "");
    }

    public static String getFilterComment2(String desc) {
        String s = desc == null ? "" : desc;
        s = s.replaceAll(" ", "");
        s = s.replaceAll("\n", "");
        s = s.replaceAll("\r", "");
        String regex = "\\[img=\\d+,\\d+\\][^\\[]+\\[/img\\]";
        return s.replaceAll(regex, "");
    }

    public static String[] getComment(String comment[]) {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < comment.length; i++) {
            String froup = comment[i];
            Pattern pattern2 = Pattern.compile("(^\\[img=\\d+,\\d+\\])");
            Matcher matcher2 = pattern2.matcher(froup);
            if (matcher2.find()) {
                String end = matcher2.group(0);
                froup = froup.replace(end, "[attach]");
                strings.add(froup);
            } else {
                froup = froup.replaceAll("\\[.+\\]", "");
                strings.add(froup);
            }
        }
        String[] t = new String[strings.size()];
        strings.toArray(t);
        return t;
    }

    public static String[] getPics(String comment[]) {
        ArrayList<String> strings = new ArrayList<String>();
        for (int i = 0; i < comment.length; i++) {
            String froup = comment[i];
            Pattern pattern2 = Pattern.compile("(^\\[img=\\d+,\\d+\\])");
            Matcher matcher2 = pattern2.matcher(froup);
            if (matcher2.find()) {
                String end = matcher2.group(0);
                froup = froup.replace(end, "").replace("[/img]", "");
                strings.add(froup);
            } else {
                strings.add(froup);
            }
        }
        String[] t = new String[strings.size()];
        strings.toArray(t);
        return t;
    }

    //	public static String getFilterComment(String comment){
    //
    //	}

    public static int[][] getSize(String group[]) {
        ArrayList<int[]> list = new ArrayList<int[]>();
        for (int i = 0; i < group.length; i++) {
            list.add(getSize(group[i]));
        }
        int[][] t = new int[list.size()][2];
        list.toArray(t);
        return t;
    }

    private static int[] getSize(String group) {
        Pattern pattern2 = Pattern.compile("(^\\[img=\\d+,\\d+\\])");
        Matcher matcher2 = pattern2.matcher(group);
        String[] sp;
        if (matcher2.find()) {
            String end = matcher2.group(0);
            end = end.replace("[img=", "").replace("]", "");
            sp = end.split(",");
            int w = Integer.parseInt(sp[0]);
            int h = Integer.parseInt(sp[1]);
            int i[] = {w, h};
            return i;
        }
        int i[] = {0, 0};
        return i;
    }

    /**
     * <p>获取[min,max)之间的随机数</p><br/>
     *
     * @param min
     * @param max
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static int getRandomNum(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static final String getDateSp2(String d) {
        if (TextUtils.isEmpty(d))
            return "";
        String beginDate = d;
        long lo = Long.parseLong(beginDate);
        lo = lo * 1000;
        Date date = new Date(lo);
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day >= 365) {
            return "一年前";
        } else if (day > 0) {
            return day + "天前";
        } else if (hour > 0) {
            return hour + "小时前";
        } else if (min > 0) {
            return min + "分前";
//            return "刚刚";
        } else {
            if (s < 0) {
                s = 1;
            }
            return "刚刚";
        }
    }

    public static final String getDateSp3(String d) {
        if (TextUtils.isEmpty(d))
            return "";
        String beginDate = d;
        long lo = Long.parseLong(beginDate);
        lo = lo * 1000;
        Date date = new Date(lo);
        Date now = new Date();
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day >= 365) {
            return DateUtils.stringToDateNoss(String.valueOf(lo));
        } else if (day > 0) {
            return DateUtils.stringToDateNoss(String.valueOf(lo));
        } else if (hour > 0) {
            return hour + "小时前";
        } else if (min > 0) {
            return min + "分前";
//            return "刚刚";
        } else {
            if (s < 0) {
                s = 1;
            }
            return "刚刚";
        }
    }

    /**
     * <p>测量等比缩放后的图片的宽高</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param mActivity
     * @param w         实际的图片高
     * @param h         实际图片的高
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static int[] calculateWidthAndHeight(Activity mActivity, int w, int h) {
        int dW = DeviceUtil.getScreenWidthAndHeight(mActivity)[0];
        int dH = DeviceUtil.getScreenWidthAndHeight(mActivity)[1];
        int imgHeight = 0;
        int imgWidth = 0;

        if (w > h) {
            imgHeight = (int) (dW * (3.0 / 4));
            imgWidth = (int) (imgHeight * 1.0 / h * w);
        } else {
            imgWidth = dW;
            imgHeight = (int) (imgWidth * 1.0 / w * h);
        }
        return new int[]{imgWidth, imgHeight};
    }

    public static int[] calculateWidthAndHeight(Activity mActivity, int w, int h, int dx) {
        int dW = DeviceUtil.getScreenWidthAndHeight(mActivity)[0] - dx;
        int dH = DeviceUtil.getScreenWidthAndHeight(mActivity)[1];
        int imgHeight = 0;
        int imgWidth = 0;

        if (w > h) {
            imgHeight = (int) (dW * (3.0 / 4));
            imgWidth = (int) (imgHeight * 1.0 / h * w);
        } else {
            imgWidth = dW;
            imgHeight = (int) (imgWidth * 1.0 / w * h);
        }
        return new int[]{imgWidth, imgHeight};
    }

    /**
     * <p>根据图片的缩放后的高度来算控件的宽高</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param mActivity
     * @param imgH      缩放后的高度
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static int[] caluculateImgViewWidhtAndHeight(Activity mActivity, int imgH) {
        int w = DeviceUtil.getScreenWidthAndHeight(mActivity)[0];
        int h = (int) ((imgH > (w * 1.0 * MUL)) ? (w * 1.0 * MUL) : imgH);
        return new int[]{w, h};
    }

    /**
     * <p>根据图片的缩放后的高度来算控件的宽高</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param mActivity
     * @param imgH      缩放后的高度
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static int[] caluculateImgViewWidhtAndHeight(Activity mActivity, int imgH, int margin) {
        int w = DeviceUtil.getScreenWidthAndHeight(mActivity)[0] - margin;
        int h = (int) ((imgH > (w * 1.0 * MUL)) ? (w * 1.0 * MUL) : imgH);
        return new int[]{w, h};
    }

    /**
     * <p>根据话题规则加载图片</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param context
     * @param iconUrl
     * @param iv
     * @author sunxianhao
     * @since 1.0.0
     */
    public static void loadTopicIconView(Context context, String iconUrl[], final ImageView iv, final int defaultId, int w, int h) {
        String pic = getOnePic(iconUrl);
//		.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
        realLoadImg(iv, defaultId, w, h, pic);
    }

    public static void realLoadImg(final ImageView iv, final int defaultId, int w, int h, String pic) {
        if (!TextUtils.isEmpty(pic)) {
//			Picasso.with(context).load(pic).placeholder(defaultId).resize(w, h).centerCrop()
//			.into(iv);
            iv.setTag(pic);
            iv.setMaxWidth(w);
            iv.setMaxHeight(h);
            ImageAware imageAware = new ImageViewAware(iv, true);
//            ImageLoader.getInstance().displayImage(pic, imageAware, XBitmapUtils.img_detail_show);
            ImageLoader.getInstance().displayImage(pic, imageAware);

        } else {
            iv.setImageResource(defaultId);
        }
    }

    public static String getOnePic(String[] iconUrl) {
        if (iconUrl == null || iconUrl.length == 0) {
            return "";
        }
        List<String> pics = String_Utils.getCommentPics(iconUrl[0]);
        String comment = null;
        if (pics.size() > 0) {
            comment = pics.get(0);
        }
        if (!TextUtils.isEmpty(comment)) {
            String[] split = comment.split(",");
            if (split.length > 0) {
                return split[0];
            }
        }
        return "";
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * <p>数字转为万</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param count
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static String convert2W(long count) {
        String countStr = "";
        if (count < 10000) {
            countStr = count + "";
        } else {
            double div = count * 1.0 / 10000;
            countStr = new BigDecimal(div).setScale(2, BigDecimal.ROUND_HALF_UP) + "万";
//			countStr = new DecimalFormat("#.##").format(div) + "万";
        }
        return countStr;
    }

    /**
     * <p>数字转为万</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param count
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static String convert2WS(String count) {
        try {
            double ss = Double.parseDouble(count);
            if (ss == 0) {
                return "0";
            }
            return convert2W(ss);
        } catch (Exception e) {
        }
        return "0";
    }

    /**
     * <p>数字转为万</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param count
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static String convert2W(double count) {
        String countStr = "";
        DecimalFormat df = new DecimalFormat("0.00");
        if (count < 10000) {
            countStr = df.format(count) + "";
        } else {
            double div = count * 1.0 / 10000;
            countStr = df.format(div) + "万";
        }
        return countStr;
    }

    /**
     * <p>数字转为万</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param count
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static String convert2W(String count) {
        long strCount = 0;
        try {
            strCount = Long.parseLong(count);
            return convert2W(strCount);
        } catch (Exception e) {
        }
        return "0";
    }

    /**
     * <p>数字转为万</p><br/>
     * <p>TODO(详细描述)</p>
     */
    public static String convert2W0_0(String count) {
        long strCount = 0;
        try {
            strCount = Long.parseLong(count);
            String countStr = "";
            if (strCount < 10000) {
                countStr = strCount + "";
            } else {
                double div = strCount * 1.0 / 10000;
//                countStr = new DecimalFormat("#.#").format(div) + "万";
                countStr = new BigDecimal(div).setScale(1, BigDecimal.ROUND_HALF_UP) + "万";
                if(div>=10000d){
                    double yi = div * 1.0 / 10000;
                    countStr = new BigDecimal(yi).setScale(1, BigDecimal.ROUND_HALF_UP) + "亿";
                }
            }
            return countStr;
        } catch (Exception e) {
        }
        return "0";
    }

    public static String convert2W0_0(double count) {
        double strCount = 0.0;
        try {
            strCount = count;
            String countStr = "";
            if (strCount < 10000) {
                countStr = count + "";
            } else {
                double div = strCount * 1.0 / 10000;
//                countStr = new DecimalFormat("#.#").format(div) + "万";
                countStr = new BigDecimal(div).setScale(1, BigDecimal.ROUND_HALF_UP) + "万";
            }
            return countStr;
        } catch (Exception e) {
        }
        return "0";
    }

    public static String convert2Wlive(double count) {
        double strCount = 0.0;
        try {
            strCount = count;
            String countStr = "";
            if (strCount < 10000) {
//                countStr = count + "";
                countStr = new BigDecimal(strCount).setScale(2, BigDecimal.ROUND_HALF_UP) + "";
            } else {
                double div = strCount * 1.0 / 10000;
//                countStr = new DecimalFormat("#.#").format(div) + "万";
                countStr = new BigDecimal(div).setScale(1, BigDecimal.ROUND_HALF_UP) + "万";
            }
            return countStr;
        } catch (Exception e) {
        }
        return "0";
    }

    public static String convert2W0_0(int count) {
        int strCount = 0;
        try {
            strCount = count;
            String countStr = "";
            if (strCount < 10000) {
                countStr = count + "";
            } else {
                double div = strCount * 1.0 / 10000;
//                countStr = new DecimalFormat("#.#").format(div) + "万";
                countStr = new BigDecimal(div).setScale(1, BigDecimal.ROUND_HALF_UP) + "万";
            }
            return countStr;
        } catch (Exception e) {
        }
        return "0";
    }

    /**
     * <p>百万以上数字转为万</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param count
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static String convert2M(long count) {
        String countStr = "0";
        if (count < 0) {
            return countStr;
        }
        if (count < 1000000) {
            countStr = count + "";
        } else {
            long div = count / 10000;
            countStr = String.valueOf(div) + "万";
        }
        return countStr;
    }

    /**
     * <p>百万以上数字转为万</p><br/>
     * <p>TODO(详细描述)</p>
     *
     * @param count
     * @return
     * @author sunxianhao
     * @since 1.0.0
     */
    public static String convert2M(String count) {
        return formatMillionStr(count);
    }

    /**
     * 格式化数字为千分位显示；
     *
     * @param text 要格式化的数字；
     * @return
     */
    public static String fmt2Micrometer(String text) {
        if (TextUtils.isEmpty(text)) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("#0.00");
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }
        return df.format(number);
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 格式化数字为千分位显示；
     *
     * @param text 要格式化的数字；
     * @return
     */
    public static String fmtMicrometer(String text) {
        if (TextUtils.isEmpty(text)) {
            return "0";
        }
        DecimalFormat df = null;
        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else if (text.length() - text.indexOf(".") - 1 == 1) {
                df = new DecimalFormat("###,##0.0");
            } else {
                df = new DecimalFormat("###,##0.00");
            }
        } else {
            df = new DecimalFormat("###,##0");
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }
        return df.format(number);
    }

    public static int formatStr(String number) {
        if (!TextUtils.isEmpty(number)) {
            try {
                return Integer.decode(number).intValue();
            } catch (Exception e) {
                return 0;
            }
        } else
            return 0;
    }

    public static String formatNum(int number) {
        float numFloat = 0.0f;
        DecimalFormat decimalFormat = new DecimalFormat(".0");
        if (number > 10000) {
            numFloat = (float) ((number * 1.0) / 10000);
            return decimalFormat.format(numFloat) + "W";
        }
//        if (number > 1000) {
//            numFloat = (float) ((number * 1.0) / 1000);
//            return decimalFormat.format(numFloat) + "K";
//        }
        return String.valueOf(number);
    }

    public static String formatMillionStr(String number) {
        if (!TextUtils.isEmpty(number)) {
            try {
                int million = Integer.decode(number).intValue();
                if (million > 100 * 10000) {
                    DecimalFormat decimalFormat = new DecimalFormat(".0");
                    return decimalFormat.format((float) ((million * 1.0) / 10000)) + "万";
                } else {
                    return number;
                }
            } catch (Exception e) {
                return "0";
            }
        }
        return "0";
    }

    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    /**
     * 格式化doubele 保留两位小数
     *
     * @return
     */
    public static double doubleTo2(double num) {
        BigDecimal big = new BigDecimal(num);
        return big.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将长整型值转化成字符串
     *
     * @param time
     * @return
     */
    public static String converLongTimeToStr(long time) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        long hour = (time) / hh;
        long minute = (time - hour * hh) / mi;
        long second = (time - hour * hh - minute * mi) / ss;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        if (hour > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
        } else {
            return "00:" + strMinute + ":" + strSecond;
        }
    }

    public static String converLongTimeToStr(String time) {
        long lo = Long.parseLong(time) * 1000;
        return converLongTimeToStr(lo);
    }

    /**
     * 将长整型值转化成字符串
     *
     * @param time
     * @return
     */
    public static String converStringTimeToMs(String time) {
        long t = Long.parseLong(time);
        int mi = 60;
        long minute = t / mi;
        long second = (t - minute * mi);
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        if (minute > 0) {
            return strMinute + ":" + strSecond;
        } else {
            return "00" + ":" + strSecond;
        }
    }

    //设置显示6个字
    public static String formatLimitLengthStr(String stringToFormat, int max) {
        if (TextUtils.isEmpty(stringToFormat))
            return "";
        if (stringToFormat.length() > max + 1) {
            stringToFormat = stringToFormat.substring(0, max) + "...";
        }
        return stringToFormat;
    }

    //设置显示6个字
    public static String formatLimitLengthStr(String stringToFormat) {
        return formatLimitLengthStr(stringToFormat, 6);
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        boolean isValid = false;
        if (TextUtils.isEmpty(phoneNumber)) {
            return isValid;
        }
        if (phoneNumber.length() == 11) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isPassWord(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }
        if (pwd.length() < 6 || pwd.length() > 18) {
            return false;
        }
        return true;
    }

    /**
     * 用指定字符分隔格式化字符串
     * <br/>（如电话号为13912345678 指定startIndex为3，step为4，separator为'-'经过此处理后的结果为 <br/> 139-1234-5678）
     *
     * @param source     需要分隔的字符串
     * @param startIndex 开始分隔的索引号
     * @param step       步长
     * @param separator  指定的分隔符
     * @return 返回分隔格式化处理后的结果字符串
     */
    public static String separateString(String source, int startIndex, int step, char separator) {
        int times = 0;
        StringBuilder tmpBuilder = new StringBuilder(source);
        for (int i = 0; i < tmpBuilder.length(); i++) {
            if (i == startIndex + step * times + times) {//if(i == 3 || i == 8){
                if (separator != tmpBuilder.charAt(i)) {
                    tmpBuilder.insert(i, separator);
                }
                times++;
            } else {
                if (separator == tmpBuilder.charAt(i)) {
                    tmpBuilder.deleteCharAt(i);
                    i = -1;
                    times = 0;
                }
            }
        }
        return tmpBuilder.toString();
    }

    public static String formatPhoneNumber(String number) {
        return separateString(number, 3, 4, ' ');
    }

    public static String generateTime(String time) {
        try {
            Long ms = Long.parseLong(time);

            Integer mi = 60;
            Integer hh = mi * 60;
            Integer dd = hh * 24;

            Long day = ms / dd;
            Long hour = (ms - day * dd) / hh;
            Long minute = (ms - day * dd - hour * hh) / mi;
            Long second = ms - day * dd - hour * hh - minute * mi;

            StringBuffer sb = new StringBuffer();
            if (day > 0) {
                sb.append(day + "天");
            }
            if (hour > 0) {
                sb.append(hour + "小时");
            }
            if (minute > 0) {
                sb.append(minute + "分");
            }
            if (second > 0) {
                sb.append(second + "秒");
            }
            return sb.toString();

        } catch (Exception e) {
            return null;
        }

    }

    public static String generateTimeNos(String time) {
        try {
            Long ms = Long.parseLong(time);

            Integer mi = 60;
            Integer hh = mi * 60;
            Integer dd = hh * 24;

            Long day = ms / dd;
            Long hour = (ms - day * dd) / hh;
            Long minute = (ms - day * dd - hour * hh) / mi;
            Long second = ms - day * dd - hour * hh - minute * mi;

            StringBuffer sb = new StringBuffer();
            if (day > 0) {
                sb.append(day + "天");
            }
            if (hour > 0) {
                sb.append(hour + "小时");
            }
            if (minute > 0) {
                sb.append(minute + "分");
            }

            return sb.toString();

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 超过一天显示 x天x小时
     * 不足一天显示 x小时x分
     * 不足一小时显示 x分
     *
     * @param time
     * @return
     */

    public static String generateTimeNos2(String time) {
        try {
            Long ms = Long.parseLong(time);

            Integer mi = 60;
            Integer hh = mi * 60;
            Integer dd = hh * 24;

            Long day = ms / dd;
            Long hour = (ms - day * dd) / hh;
            Long minute = (ms - day * dd - hour * hh) / mi;
            Long second = ms - day * dd - hour * hh - minute * mi;

            StringBuffer sb = new StringBuffer();
            if (day > 0) {
                sb.append(day + "天");
            }
            if (hour > 0) {
                sb.append(hour + "小时");
            }
            if (minute > 0) {
                if (day <= 0) {
                    sb.append(minute + "分");
                }
            }

            return sb.toString();

        } catch (Exception e) {
            return null;
        }

    }

    public static List<String> generateTimeToList(String time) {
        List<String> list = new ArrayList<String>();
        try {
            Long ms = Long.parseLong(time);

            Integer mi = 60;
            Integer hh = mi * 60;
            Integer dd = hh * 24;

            Long day = ms / dd;
            Long hour = (ms - day * dd) / hh;
            Long minute = (ms - day * dd - hour * hh) / mi;

            if (day > 0) {
                if (day < 10) {
                    list.add("0" + day);
                } else {
                    list.add(day + "");
                }

            } else {
                list.add("00");
            }
            if (hour > 0) {
                if (hour < 10) {
                    list.add("0" + hour);
                } else {
                    list.add(hour + "");
                }
            } else {
                list.add("00");
            }
            if (minute > 0) {
                if (minute < 10) {
                    list.add("0" + minute);
                } else {
                    list.add(minute + "");
                }
            } else {
                list.add("00");
            }


        } catch (Exception e) {
            list.add("00");
            list.add("00");
            list.add("00");
            list.add("00");
        }
        return list;

    }

    /**
     * 返回天时分秒的集合
     */
    public static List<String> generateTimeDDhhmmssToList(long ms) {
        List<String> list = new ArrayList<String>();
        try {
//			Long ms = Long.parseLong(time);

            Integer mi = 60;
            Integer hh = mi * 60;
            Integer dd = hh * 24;

            Long day = ms / dd;
            Long hour = (ms - day * dd) / hh;
            Long minute = (ms - day * dd - hour * hh) / mi;
            Long second = ms - day * dd - hour * hh - minute * mi;
            if (day > 0) {
                if (day < 10) {
                    list.add("0" + day);
                } else {
                    list.add(day + "");
                }

            } else {
                list.add("00");
            }
            if (hour > 0) {
                if (hour < 10) {
                    list.add("0" + hour);
                } else {
                    list.add(hour + "");
                }
            } else {
                list.add("00");
            }
            if (minute > 0) {
                if (minute < 10) {
                    list.add("0" + minute);
                } else {
                    list.add(minute + "");
                }
            } else {
                list.add("00");
            }
            if (second > 0) {
                if (second < 10) {
                    list.add("0" + second);
                } else {
                    list.add(second + "");
                }
            } else {
                list.add("00");
            }

        } catch (Exception e) {
            list.add("00");
            list.add("00");
            list.add("00");
            list.add("00");
        }
        return list;

    }

    public static String getTimeDay(long s) {
        Integer mi = 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        Long day = s / dd;
        if (day > 0) {
            if (day < 10) {
                return "0" + day;
            } else {
                return day + "";
            }

        } else {
            return "00";
        }
    }

    public static String getTimeHour(long s) {
        Integer mi = 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        Long day = s / dd;
        Long hour = (s - day * dd) / hh;
        if (hour > 0) {
            if (hour < 10) {
                return "0" + hour;
            } else {
                return hour + "";
            }
        } else {
            return "00";
        }
    }

    public static String getTimeMinute(long s) {
        Integer mi = 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        Long day = s / dd;
        Long hour = (s - day * dd) / hh;
        Long minute = (s - day * dd - hour * hh) / mi;
        if (minute > 0) {
            if (minute < 10) {
                return "0" + minute;
            } else {
                return minute + "";
            }

        } else {
            return "00";
        }
    }

    public static String getTimeSecond(long s) {
        Integer mi = 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        Long day = s / dd;
        Long hour = (s - day * dd) / hh;
        Long minute = (s - day * dd - hour * hh) / mi;
        Long second = s - day * dd - hour * hh - minute * mi;
        if (second > 0) {
            if (second < 10) {
                return "0" + second;
            } else {
                return second + "";
            }
        } else {
            return "00";
        }
    }

    public static String hintPhoneNumber(String number) {
        if (number == null || number.length() != 11) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        return sb.append(number.substring(0, 3)).append("****").append(number.substring(7, 11)).toString();


    }

    /**
     * 半角转换为全角,解决TextView排版问题
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);

    }


    /**
     * isNull
     */
    public static boolean isNull(String content) {
        if (TextUtils.isEmpty(content)) {
            return true;
        } else if ("null".equalsIgnoreCase(content)) {
            return true;
        }
        return false;
    }

    /**
     * 图片尺寸后缀
     */
    public static String getPictureSize(int width, int height) {
        StringBuilder sb = new StringBuilder();
        int widthPx = DensityUtil.dip2px(width);
        int heightPx = DensityUtil.dip2px(height);
        return sb.append("?size=").append(widthPx).append("x").append(heightPx).toString();
    }

    /**
     * 推荐，订阅，图片尺寸后缀px
     */
    private static int imgWidth = 970;
    private static int imgHeight = 540;

    private static int imgSmallWidth = 480;
    private static int imgSmallHeight = 270;

    private static int imgAvatarWidth = 240;
    private static int imgAvaterHeight = 240;

    private static int imgFullWidth = 576;
    private static int imgFullHeight = 1024;

    //暖场图
    public static String getPictureSizeFull() {
        StringBuilder sb = new StringBuilder();
        return sb.append("?size=").append(imgFullWidth).append("x").append(imgFullHeight).toString();
    }

    public static String getPictureSizeRecomend() {
        StringBuilder sb = new StringBuilder();
        return sb.append("?size=").append(imgWidth).append("x").append(imgHeight).toString();
    }

    //	分类页面图片大小
    public static String getPictureSizeClassify() {
        StringBuilder sb = new StringBuilder();
        return sb.append("?size=").append(imgSmallWidth).append("x").append(imgSmallHeight).toString();
    }

    //	头像图片大小
    public static String getPictureSizeAvatar() {
        StringBuilder sb = new StringBuilder();
        return sb.append("?size=").append(imgAvatarWidth).append("x").append(imgAvaterHeight).toString();
    }

    //	自定义图片大小
    public static String getPictureSizeCostom(int width, int height) {
        StringBuilder sb = new StringBuilder();
        return sb.append("?size=").append(width).append("x").append(height).toString();
    }

    public static int getLength(String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        double valueLength = 0;
        String chinese = "[ \u2E80-\uFE4F]";
        String chinesec = "[ \uff01-\uff5e]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese) || temp.matches(chinesec)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        //进位取整
        return (int) Math.ceil(valueLength);
    }

    /**
     * 解析url
     */
    public static Map<String, String> analysisUrl(String url) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(url);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            if (arrSplitEqual.length > 1) {
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (!TextUtils.isEmpty(arrSplitEqual[0])) {
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim().toLowerCase();
        if (strURL.contains("?")) {
            arrSplit = strURL.split("[?]");
            if (strURL.length() > 1) {
                if (arrSplit.length > 1) {
                    if (arrSplit[1] != null) {
                        strAllParam = arrSplit[1];
                    }
                }
            }
        }
        return strAllParam;
    }

    public static void handlerContent(String content, TextView tvContent, int color) {
        if (!TextUtils.isEmpty(content)) {
            if (content.contains("@") && content.contains(" ")) {
                int[] indexArray = findStr(0, content, "@", " ");
                int atIndex = indexArray[0];
                int sepIndex = indexArray[1];
                if (atIndex > -1 && sepIndex > atIndex) {
                    SpannableStringBuilder builder = new SpannableStringBuilder(content);
                    setSpan(builder, atIndex, sepIndex, color);
                    while (atIndex > -1 && sepIndex > -1) {
                        indexArray = findStr(sepIndex + 1, content, "@", " ");
                        atIndex = indexArray[0];
                        sepIndex = indexArray[1];
                        if (atIndex > -1 && sepIndex > atIndex)
                            setSpan(builder, atIndex, sepIndex, color);
                        else
                            break;
                    }
                    tvContent.setText(builder, TextView.BufferType.SPANNABLE);
                } else {
                    tvContent.setText( content, TextView.BufferType.SPANNABLE);
                }
            } else {
                tvContent.setText( content, TextView.BufferType.SPANNABLE);
            }
        }
    }

//    public static void handlerTopicContent(String content, TextView tvContent, int color, int startIndex, int endIndex) {
//        if (!TextUtils.isEmpty(content)) {
//            if (content.contains("#")) {
//                SpannableStringBuilder builder = new SpannableStringBuilder(content);
//                builder.setSpan(new AbsoluteSizeSpan(14, true), 0, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                setSpan(builder, 0, startIndex, R.color.color_7a7a7a);
//                setSpan(builder, startIndex, endIndex, color);
//                tvContent.setText(EmojiUtils.getEmojiText(MengzhuApplication.getApplication(), builder), TextView.BufferType.SPANNABLE);
//            } else {
//                tvContent.setText(EmojiUtils.getEmojiText(MengzhuApplication.getApplication(), content), TextView.BufferType.SPANNABLE);
//
//            }
//        }
//    }

    public static int[] findStr(int contentStart, String content, String start, String end) {
        int[] array = new int[2];
        array[0] = content.indexOf(start, contentStart);
        array[1] = content.indexOf(end, contentStart);
        return array;
    }

    //设置@颜色
    public static void setSpan(SpannableStringBuilder builder, int atIndex, int sepIndex, int color) {
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(color);
        builder.setSpan(whiteSpan, atIndex, sepIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    //设置@颜色
    public static void setSpan(SpannableStringBuilder builder, int atIndex, int sepIndex) {
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Integer.parseInt("#ffffff"));
        builder.setSpan(whiteSpan, atIndex, sepIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    //设置@颜色
    public static void setSpans(SpannableStringBuilder builder, int atIndex, int sepIndex) {
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Integer.parseInt("#FE5656"));
        builder.setSpan(whiteSpan, atIndex, sepIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    public static String listToString(List<String> stringList) {
        if (stringList == null || stringList.size() == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    public final static int[] li_SecPosValue = {1601, 1637, 1833, 2078, 2274,
            2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858,
            4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590};
    public final static String[] lc_FirstLetter = {"a", "b", "c", "d", "e",
            "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "w", "x", "y", "z"};

    /**
     * 取得给定汉字的首字母,即声母
     *
     * @param chinese 给定的汉字
     * @return 给定汉字的声母
     */
    public static String getFirstLetter(String chinese) {
        if (chinese == null || chinese.trim().length() == 0) {
            return "";
        }
        chinese = conversionStr(chinese, "GB2312", "ISO8859-1");

        if (chinese.length() > 1) {// 判断是不是汉字
            int li_SectorCode = (int) chinese.charAt(0); // 汉字区码
            int li_PositionCode = (int) chinese.charAt(1); // 汉字位码
            li_SectorCode = li_SectorCode - 160;
            li_PositionCode = li_PositionCode - 160;
            int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; // 汉字区位码
            if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {
                for (int i = 0; i < 23; i++) {
                    if (li_SecPosCode >= li_SecPosValue[i]
                            && li_SecPosCode < li_SecPosValue[i + 1]) {
                        chinese = lc_FirstLetter[i];
                        break;
                    }
                }
            } else {// 非汉字字符,如图形符号或ASCII码
                chinese = conversionStr(chinese, "ISO8859-1", "GB2312");
                chinese = chinese.substring(0, 1);
            }
        }

        return chinese;
    }

    /**
     * 字符串编码转换
     *
     * @param str           要转换编码的字符串
     * @param charsetName   原来的编码
     * @param toCharsetName 转换后的编码
     * @return 经过编码转换后的字符串
     */
    public static String conversionStr(String str, String charsetName, String toCharsetName) {
        try {
            str = new String(str.getBytes(charsetName), toCharsetName);
        } catch (UnsupportedEncodingException ex) {
            System.out.println("字符串编码转换异常：" + ex.getMessage());
        }
        return str;
    }

    /**
     * 字符串截取
     */
    public static String subStr16Len(String str) {
        return subStr(str, 16);
    }

    /**
     * 字符串截取
     */
    public static String subStr(String str, int charLen) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int destLen = 0;
        for (int index = 0; index < str.length(); index++) {
            char charAt = str.charAt(index);
            int len = getStrLen(String.valueOf(charAt));
            destLen += len;
            if (destLen >= charLen) {
                return str.substring(0, index + 1) + "...";
            }
        }
        return str;
    }

    public static int getStrLen(String strInput) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]+");
        if (p.matcher(strInput).matches()) {
            return 2;
        }
        return 1;
    }

    public static SpannableStringBuilder formatUrlString(String contentStr) {

        SpannableStringBuilder sp;
        if (!TextUtils.isEmpty(contentStr)) {

            sp = new SpannableStringBuilder(contentStr);
            try {
                //处理url匹配
                Pattern urlPattern = Pattern.compile("(http|https|ftp|svn)://([a-zA-Z0-9]+[/?.?])" +
                        "+[a-zA-Z0-9]*\\??([a-zA-Z0-9]*=[a-zA-Z0-9]*&?)*");
                Matcher urlMatcher = urlPattern.matcher(contentStr);

                while (urlMatcher.find()) {
                    final String url = urlMatcher.group();
                    if (!TextUtils.isEmpty(url)) {
                        sp.setSpan(new SpannableClickable() {
                            @Override
                            public void onClick(View widget) {
                                Uri uri = Uri.parse(url);
                                Context context = widget.getContext();
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                                context.startActivity(intent);
                            }
                        }, urlMatcher.start(), urlMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                //处理电话匹配
                Pattern phonePattern = Pattern.compile("[1][34578][0-9]{9}");
                Matcher phoneMatcher = phonePattern.matcher(contentStr);
                while (phoneMatcher.find()) {
                    final String phone = phoneMatcher.group();
                    if (!TextUtils.isEmpty(phone)) {
                        sp.setSpan(new SpannableClickable() {
                            @Override
                            public void onClick(View widget) {
                                Context context = widget.getContext();
                                //用intent启动拨打电话
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }, phoneMatcher.start(), phoneMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sp = new SpannableStringBuilder();
        }
        return sp;
    }

    public static Spannable applyKerning(CharSequence src, float kerning,
                                         int start, int end) {
        if (src == null)
            return null;
        final int srcLength = src.length();
        if (srcLength < 2)
            return src instanceof Spannable ? (Spannable) src
                    : new SpannableString(src);
        if (start < 0)
            start = 0;
        if (end > srcLength)
            end = srcLength;
        final String nonBreakingSpace = "\u00A0";
        final SpannableStringBuilder builder = src instanceof SpannableStringBuilder ? (SpannableStringBuilder) src
                : new SpannableStringBuilder(src);
        for (int i = src.length(); i >= 1; i--) {
            builder.insert(i, nonBreakingSpace);
            builder.setSpan(new ScaleXSpan(kerning), i, i + 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    //在标题前添加图片icon
    public static SpannableStringBuilder insertIconHeader(Context context, String orginalStr, int resId) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        ImageSpan imageSpan;
        spannableString.append(orginalStr);
        if (resId != 0) {
            imageSpan = new ImageSpan(context, resId, ImageSpan.ALIGN_BASELINE);
            spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
