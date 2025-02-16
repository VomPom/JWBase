package wang.julis.jwbase.utils

import android.text.TextUtils
import android.util.Log

/**
 *
 * Created by juliswang on 2024/7/10 11:07
 *
 * @Description
 */
object Logger {
    private const val TAG = "vompom"

    /**
     * info 级别的日志
     *
     * @param message 可以传递Object类型，会对其进行toString
     */
    fun i(message: Any) {
        i(TAG, message)
    }

    fun i(tag: String, message: Any) {
        if (checkLogger(tag, message)) return
        log(LogLevel.INFO, tag, message.toString())
    }

    /**
     * debug 级别的日志
     *
     * @param message 可以传递Object类型，会对其进行toString
     */
    fun d(message: Any) {
        d(TAG, message)
    }

    fun d(tag: String, message: Any) {
        if (checkLogger(tag, message)) return
        log(LogLevel.DEBUG, tag, message.toString())
    }

    /**
     * warning 级别的日志
     *
     * @param message 可以传递Object类型，会对其进行toString
     */
    fun w(message: String) {
        e(TAG, message)
    }

    fun w(tag: String, message: Any) {
        if (checkLogger(tag, message)) return
        log(LogLevel.WARNING, tag, message.toString())
    }


    /**
     * error 级别的日志
     *
     * @param message 可以传递Object类型，会对其进行toString
     */
    fun e(message: String) {
        e(TAG, message)
    }

    fun e(tag: String, message: Any) {
        if (checkLogger(tag, message)) return
        log(LogLevel.ERROR, tag, message.toString())
    }


    /**
     * verbose 级别的日志
     *
     * @param message 可以传递Object类型，会对其进行toString
     */
    fun v(message: String) {
        v(TAG, message)
    }

    fun v(tag: String, message: Any) {
        if (checkLogger(tag, message)) return
        log(LogLevel.VERBOSE, tag, message.toString())
    }

    private fun log(logLevel: LogLevel, tag: String, msg: String) {
        when (logLevel) {
            LogLevel.INFO -> {
                Log.i(tag, msg)
                Log.d(tag, msg)
            }

            LogLevel.DEBUG -> Log.d(tag, msg)
            LogLevel.WARNING -> {
                Log.w(tag, msg)
                Log.v(tag, msg)
                Log.e(tag, msg)
            }

            LogLevel.VERBOSE -> {
                Log.v(tag, msg)
                Log.e(tag, msg)
            }

            LogLevel.ERROR -> Log.e(tag, msg)
            else -> {}
        }
    }

    /**
     * 对传入的tag和message进行简单的检查
     *
     * @param tag     TAG
     * @param message 日志内容
     * @return TRUE表示校验不通过
     */
    private fun checkLogger(tag: String, message: Any?): Boolean {
        return TextUtils.isEmpty(tag) || message == null
    }

    enum class LogLevel(i: Int) {
        VERBOSE(0), DEBUG(1), INFO(2), WARNING(3), ERROR(4), FATAL(5)
    }
}