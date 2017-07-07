package com.gg.baseapp.utils;

import android.annotation.SuppressLint;
import android.support.compat.BuildConfig;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Log {
    public static boolean debug = true;

    /**
     * 设置是否打开log日志开关
     *
     * @param debug
     */
    public static void setShow(boolean debug) {
        Log.debug = debug;
    }

    /**
     * 根据tag打印相关v信息
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (debug) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.v(tag, traceInfo + msg);
        }
    }

    /**
     * 根据tag打印v信息,包括Throwable的信息
     * * @param tag
     *
     * @param msg
     * @param tr
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (debug) {
            android.util.Log.v(tag, msg, tr);
        }
    }


    /**
     * 根据tag打印输出debug信息
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (debug) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.d(tag, traceInfo + msg);
        }
    }

    /**
     * 根据tag打印输出debug信息 包括Throwable的信息
     * * @param tag
     *
     * @param msg
     * @param tr
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (debug) {
            android.util.Log.d(tag, msg, tr);
        }
    }

    /**
     * 根据tag打印输出info的信息
     * * @param tag
     *
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (debug) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.i(tag, traceInfo + msg);
        }
    }

    /**
     * 根据tag打印输出info信息 包括Throwable的信息
     *
     * @param tag
     * @param msg
     * @param tr
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (debug) {
            android.util.Log.i(tag, msg, tr);
        }
    }

    /**
     * 根据tag打印输出error信息
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (debug) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.e(tag, traceInfo + msg);
        }
    }

    public static void e(String msg) {
        if (debug) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.e(APP_DEFAULT_TAG, traceInfo + msg);
        }
    }

    public static void w(String msg) {
        if (debug) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.w(APP_DEFAULT_TAG, traceInfo + msg);
        }
    }

    /**
     * 根据tag打印输出的error信息 包括Throwable的信息
     *
     * @param tag
     * @param msg
     * @param tr
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (debug) {
            android.util.Log.e(tag, msg, tr);
        }
    }

    /**
     * 根据tag打印输出error信息
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (debug) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.w(tag, traceInfo + msg);
        }
    }

    /**
     * 根据tag打印输出的error信息 包括Throwable的信息
     *
     * @param tag
     * @param msg
     * @param tr
     */
    public static void w(String tag, String msg, Throwable tr) {
        if (debug) {
            android.util.Log.w(tag, msg, tr);
        }
    }


    private static String APP_DEFAULT_TAG = "Ging";// LOG默认TAG
    private static final String TAG_CONTENT_PRINT = "%s.%s():%d:%s";
    private static ConcurrentHashMap<String, Long> mTraceTime;
    private static final boolean SHOWLOG = BuildConfig.DEBUG;
    private static final boolean SHOW_TRACE_INFO = false;
    private static final int SEPERATE_LENGTH = 2048;

    //    public static void v(String msg){
//        println(android.util.Log.VERBOSE, APP_DEFAULT_TAG, msg, null, 5);
//    }
//    /**
//     * Send a {@link #VERBOSE} log message.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     */
//    public static void v(String tag, String msg) {
//        println(android.util.Log.VERBOSE, tag, msg, null, 5);
//    }
//
//    /**
//     * Send a {@link #VERBOSE} log message and log the exception.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     * @param tr An exception to log
//     */
//    public static void v(String tag, String msg, Throwable tr) {
//        println(android.util.Log.VERBOSE, tag, msg, tr, 5);
//    }
//    public static void d(String msg){
//        println(android.util.Log.DEBUG, APP_DEFAULT_TAG, msg, null, 5);
//    }
//    /**
//     * Send a {@link #DEBUG} log message.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     */
//    public static void d(String tag, String msg) {
//        println(android.util.Log.DEBUG, tag, msg, null, 5);
//    }
//    /**
//     * Send a {@link #DEBUG} log message and log the exception.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     * @param tr An exception to log
//     */
//    public static void d(String tag, String msg, Throwable tr) {
//        println(android.util.Log.DEBUG, tag, msg, tr, 5);
//    }
//    public static void i(String msg){
//        println(android.util.Log.INFO, APP_DEFAULT_TAG, msg, null, 5);
//    }
//    /**
//     * Send an {@link #INFO} log message.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     */
//    public static void i(String tag, String msg) {
//        println(android.util.Log.INFO, tag, msg, null, 5);
//    }
//    /**
//     * Send a {@link #INFO} log message and log the exception.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     * @param tr An exception to log
//     */
//    public static void i(String tag, String msg, Throwable tr) {
//        println(android.util.Log.INFO, tag, msg, tr, 5);
//    }
//    public static void w(String msg){
//        println(android.util.Log.WARN, APP_DEFAULT_TAG, msg, null, 5);
//    }
//    /**
//     * Send a {@link #WARN} log message.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     */
//    public static void w(String tag, String msg) {
//        println(android.util.Log.WARN, tag, msg, null, 5);
//    }
//    /**
//     * Send a {@link #WARN} log message and log the exception.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     * @param tr An exception to log
//     */
//    public static void w(String tag, String msg, Throwable tr) {
//        println(android.util.Log.WARN, tag, msg, tr, 5);
//    }
//    /**
//     * Send a {@link #WARN} log message and log the exception.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param tr An exception to log
//     */
//    public static void w(String tag, Throwable tr) {
//        println(android.util.Log.WARN, tag, "", tr, 5);
//    }
//    public static void e(String msg){
//        println(android.util.Log.ERROR, APP_DEFAULT_TAG, msg, null, 5);
//    }
//    /**
//     * Send an {@link #ERROR} log message.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     */
//    public static void e(String tag, String msg) {
//        println(android.util.Log.ERROR, tag, msg, null, 5);
//    }
//    /**
//     * Send a {@link #ERROR} log message and log the exception.
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     * @param tr An exception to log
//     */
//    public static void e(String tag, String msg, Throwable tr) {
//        println(android.util.Log.ERROR, tag, msg, tr, 5);
//    }
//    /**
//     * Low-level logging call.
//     * @param priority The priority/type of this log message
//     * @param tag Used to identify the source of a log message.  It usually identifies
//     *        the class or activity where the log call occurs.
//     * @param msg The message you would like logged.
//     * @return The number of bytes written.
//     */
//    private static void println(int priority, String tag, String msg, Throwable tr, int traceIndex) {
//        if(SHOWLOG){
//            android.util.Log.println(priority, tag, getContent(traceIndex, msg, tr));
//        }
//    }
    //format log
    @SuppressLint("DefaultLocale")
    private static String getContent(int traceIndex, String msg, Throwable tr) {
        String content = msg + (null != tr ? tr.toString() : "");
        try {
            StackTraceElement trace = Thread.currentThread().getStackTrace()[traceIndex];
            if (SHOW_TRACE_INFO) {
                content = String.format(TAG_CONTENT_PRINT,
                        trace.getClassName(), trace.getMethodName(), trace.getLineNumber(), msg);
            }
            if (null != tr) {
                content += "\n tr = " + tr.toString();
            }
        } catch (Exception e) {
        }
        return content;
    }

    /**
     * 开始时间戳，打印开始时间，与stopTimeTrace配合使用
     *
     * @param timeTraceTag 时间戳名
     */
    public static void startTimeTrace(String timeTraceTag) {
        if (SHOWLOG) {
            if (null == mTraceTime) {
                mTraceTime = new ConcurrentHashMap<>();
            }
            long startTime = System.currentTimeMillis();
            mTraceTime.put(timeTraceTag, startTime);
            android.util.Log.w(timeTraceTag, "strart time = " + startTime);
        }
    }

    /**
     * 结束时间戳，打印结束时间，与startTimeTrace配合使用
     *
     * @param timeTraceTag 时间戳名
     */
    public static void stopTimeTrace(String timeTraceTag) {
        if (SHOWLOG) {
            if (null == mTraceTime) {
                android.util.Log.w(timeTraceTag, "not startActivity.");
            } else {
                Long time = mTraceTime.remove(timeTraceTag);
                if (null == time) {
                    android.util.Log.w(timeTraceTag, "not startActivity.");
                } else {
                    long endTime = System.currentTimeMillis();
                    long diff = endTime - time;
                    android.util.Log.w(timeTraceTag, "end time = " + endTime + " ,diff = " + diff + " ms");
                }
            }
        }
    }

    /**
     * 打印类堆栈信息
     *
     * @param tag
     */
    public static void printStackTrace(String tag) {
        if (SHOWLOG) {
            try {
                StackTraceElement[] traces = Thread.currentThread().getStackTrace();
                StringBuilder builder = new StringBuilder();
                builder.append("[").append('\n');
                for (int i = 3; i < traces.length; i++) {
                    builder.append("{class = ").append(traces[i].getClassName()).append(" , Method = ")
                            .append(traces[i].getMethodName()).append("}").append('\n');
                }
                builder.append("]");
                android.util.Log.w(tag, builder.toString());
            } catch (Exception e) {
            }
        }
    }

    private static StringBuilder formatObjectToString(Object obj, StringBuilder builder) {
        builder.append("{");
        if (obj instanceof String || obj.getClass().getSuperclass() == Number.class) {
            builder.append("type = ").append(obj.getClass().toString()).append(", value = ").append(obj).append('\n');
        } else {
            builder.append('\n').append("type = ").append(obj.getClass().toString()).append("{");
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    builder.append("filedName = ").append(field.getName()).append(",")
                            .append("value = ").append(field.get(obj)).append(",").append('\n');
                } catch (Exception e) {
                }
            }
            builder.append("}").append("}").append('\n');
        }
        return builder;
    }

    private static String formatObjectMethodValues(Object obj, String... methodNames) {
        StringBuilder builder = new StringBuilder();
        Method method = null;
        if (null != methodNames) {
            builder.append("{");
            for (String methodName : methodNames) {
                try {
                    method = obj.getClass().getMethod(methodName, new Class[]{});
                    Object value = method.invoke(obj, new Object[]{});
                    builder.append(methodName).append(" = ").append(value).append(",");
                } catch (Exception e) {

                }
            }
            builder.append("}");
        }
        return builder.toString();
    }

    public static void printObj(Object obj) {
        printObj(APP_DEFAULT_TAG, obj);
    }

    /**
     * 打印对象属性名、属性值
     *
     * @param tag
     * @param obj
     */
    public static void printObj(String tag, Object obj) {
        if (SHOWLOG) {
            android.util.Log.w(tag, formatObjectToString(obj, new StringBuilder()).toString());
        }
    }

    public static void printObj(Object obj, String... methodNames) {
        printObj(APP_DEFAULT_TAG, obj, methodNames);
    }

    /**
     * 打印对象方法值
     *
     * @param obj
     * @param methodNames 方法名
     */
    public static void printObj(String tag, Object obj, String... methodNames) {
        if (BuildConfig.DEBUG) {
            String msg = formatObjectMethodValues(obj, methodNames);
            android.util.Log.w(tag, msg);
        }
    }

    public static void printList(List<?> list) {
        printList(APP_DEFAULT_TAG, list);
    }

    /**
     * 打印list
     *
     * @param tag
     * @param list
     */
    public static void printList(String tag, List<?> list) {
        if (SHOWLOG) {
            StringBuilder builder = new StringBuilder();
            if (null == list) {
                builder.append("list is null");
            } else {
                builder.append("list size = ").append(list.size()).append('\n').append("[").append('\n');
                for (Object obj : list) {
                    builder = formatObjectToString(obj, builder);
                }
                builder.append("]").append('\n');
            }
            android.util.Log.w(tag, builder.toString());
        }
    }

    /**
     * 打印map
     *
     * @param tag
     * @param map
     */
    public static <K, V> void printMap(String tag, Map<K, V> map) {
        if (SHOWLOG) {
            StringBuilder builder = new StringBuilder();
            if (null == map) {
                builder.append("map is null");
            } else {
                builder.append("map size = ").append(map.size()).append('\n').append("[").append('\n');
                for (K k : map.keySet()) {
                    builder.append("key = ").append(k).append(", value = ").append(formatObjectToString(map.get(k), builder));
                }
            }
            android.util.Log.w(tag, builder.toString());
        }
    }

    public static <K, V> void printMap(Map<K, V> map) {
        printMap(APP_DEFAULT_TAG, map);
    }

    public static void printSet(String tag, Set<?> set) {
        if (SHOWLOG) {
            StringBuilder builder = new StringBuilder();
            if (null == set) {
                builder.append("set is null");
            } else {
                builder.append("set size = ").append(set.size()).append('\n').append("[");
                if (set.size() > 0) {
                    Iterator iterator = set.iterator();
                    do {
                        Object object = iterator.next();
                        builder.append(formatObjectToString(object, builder));
                        if (iterator.hasNext()) {
                            builder.append(", ");
                        }
                    } while (iterator.hasNext());
                }
                builder.append("]");
            }
            android.util.Log.w(tag, builder.toString());
        }
    }

    public static void printSet(Set<?> set) {
        printSet(APP_DEFAULT_TAG, set);
    }

//    public static void printInToast(String message) {
//        showViaToast(message);
//    }
//
//    public static void printObjectInToast(Object object) {
//        StringBuilder builder = new StringBuilder();
//        if (object == null) {
//            showViaToast("Object is null");
//        } else {
//            builder = formatObjectToString(object, builder);
//            showViaToast(builder.toString());
//        }
//    }

//    private static void showViaToast(String text) {
//        if (SHOWLOG) {
//            Toast.makeText(DuckrApp.getInstance(), text, Toast.LENGTH_SHORT).show();
//            android.util.Log.w(APP_DEFAULT_TAG, "toast : " + text);
//        }
//    }

    /**
     * 得到格式化json数据  退格用\t 换行用\r
     */
    private static String formatJSON(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

    public static void printJSON(String tag, String jsonStr) {
        String message = formatJSON(jsonStr);
        while (true) {
            if (message.length() > SEPERATE_LENGTH) {
                String subStr = message.substring(0, SEPERATE_LENGTH - 1);
                int index = subStr.lastIndexOf("\n");
                if (index > 0) {
                    w(tag, message.substring(0, index));
                    message = message.substring(index + 1);
                } else {
                    w(tag, message);
                    break;
                }
            } else {
                w(tag, message);
                break;
            }
        }
    }

    public static void printJSON(String jsonStr) {
        String message = formatJSON(jsonStr);
        while (true) {
            if (message.length() > SEPERATE_LENGTH) {
                String subStr = message.substring(0, SEPERATE_LENGTH - 1);
                int index = subStr.lastIndexOf("\n");
                if (index > 0) {
                    w(APP_DEFAULT_TAG, message.substring(0, index));
                    message = message.substring(index + 1);
                } else {
                    w(APP_DEFAULT_TAG, message);
                    break;
                }
            } else {
                w(APP_DEFAULT_TAG, message);
                break;
            }
        }
    }

    public static void printJSON(String tag, JSONObject object) {
        printJSON(tag, object.toString());
    }
}  