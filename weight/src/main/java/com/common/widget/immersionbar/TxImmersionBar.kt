package com.common.widget.immersionbar

import android.app.Activity
import android.app.Dialog
import androidx.core.app.DialogFragment
import androidx.core.app.Fragment
import android.view.View

/**
 * author ：Justin
 * time ：2021/3/30.
 * des ：
 */
// 初始化ImmersionBar
inline fun Activity.TximmersionBar(block: TxImmersionBar.() -> Unit) = TxImmersionBar.with(this).apply { block(this) }.init()

inline fun Fragment.TximmersionBar(block: TxImmersionBar.() -> Unit) = TxImmersionBar.with(this).apply { block(this) }.init()

inline fun android.app.Fragment.TximmersionBar(block: TxImmersionBar.() -> Unit) = TxImmersionBar.with(this).apply { block(this) }.init()

inline fun DialogFragment.TximmersionBar(block: TxImmersionBar.() -> Unit) = TxImmersionBar.with(this).apply { block(this) }.init()

inline fun android.app.DialogFragment.TximmersionBar(block: TxImmersionBar.() -> Unit) = TxImmersionBar.with(this).apply { block(this) }.init()

inline fun Dialog.TximmersionBar(activity: Activity, block: TxImmersionBar.() -> Unit) = TxImmersionBar.with(activity, this).apply { block(this) }.init()

inline fun Activity.TximmersionBar(dialog: Dialog, block: TxImmersionBar.() -> Unit) = TxImmersionBar.with(this, dialog).apply { block(this) }.init()

inline fun Fragment.TximmersionBar(dialog: Dialog, block: TxImmersionBar.() -> Unit) = activity?.run { TxImmersionBar.with(this, dialog).apply { block(this) }.init() }
    ?: Unit

inline fun android.app.Fragment.TximmersionBar(dialog: Dialog, block: TxImmersionBar.() -> Unit) = activity?.run { TxImmersionBar.with(this, dialog).apply { block(this) }.init() }
    ?: Unit

fun Activity.TximmersionBar() = TximmersionBar { }

fun Fragment.TximmersionBar() = TximmersionBar { }

fun android.app.Fragment.TximmersionBar() = TximmersionBar { }

fun DialogFragment.TximmersionBar() = TximmersionBar { }

fun android.app.DialogFragment.TximmersionBar() = TximmersionBar { }

fun Dialog.TximmersionBar(activity: Activity) = TximmersionBar(activity) {}

fun Activity.TximmersionBar(dialog: Dialog) = TximmersionBar(dialog) {}

fun Fragment.TximmersionBar(dialog: Dialog) = TximmersionBar(dialog) {}

fun android.app.Fragment.TximmersionBar(dialog: Dialog) = TximmersionBar(dialog) {}

// dialog销毁
fun Activity.destroyImmersionBar(dialog: Dialog) = TxImmersionBar.destroy(this, dialog)

fun Fragment.destroyImmersionBar(dialog: Dialog) = activity?.run { TxImmersionBar.destroy(this, dialog) }
    ?: Unit

fun android.app.Fragment.destroyImmersionBar(dialog: Dialog) = activity?.run { TxImmersionBar.destroy(this, dialog) }
    ?: Unit

// 状态栏扩展
val Activity.statusBarHeight get() = TxImmersionBar.getStatusBarHeight(this)

val Fragment.statusBarHeight get() = TxImmersionBar.getStatusBarHeight(this)

val android.app.Fragment.statusBarHeight get() = TxImmersionBar.getStatusBarHeight(this)

// 导航栏扩展
val Activity.navigationBarHeight get() = TxImmersionBar.getNavigationBarHeight(this)

val Fragment.navigationBarHeight get() = TxImmersionBar.getNavigationBarHeight(this)

val android.app.Fragment.navigationBarHeight get() = TxImmersionBar.getNavigationBarHeight(this)

val Activity.navigationBarWidth get() = TxImmersionBar.getNavigationBarWidth(this)

val Fragment.navigationBarWidth get() = TxImmersionBar.getNavigationBarWidth(this)

val android.app.Fragment.navigationBarWidth get() = TxImmersionBar.getNavigationBarWidth(this)

// ActionBar扩展
val Activity.actionBarHeight get() = TxImmersionBar.getActionBarHeight(this)

val Fragment.actionBarHeight get() = TxImmersionBar.getActionBarHeight(this)

val android.app.Fragment.actionBarHeight get() = TxImmersionBar.getActionBarHeight(this)

// 是否有导航栏
val Activity.hasNavigationBar get() = TxImmersionBar.hasNavigationBar(this)

val Fragment.hasNavigationBar get() = TxImmersionBar.hasNavigationBar(this)

val android.app.Fragment.hasNavigationBar get() = TxImmersionBar.hasNavigationBar(this)

// 是否有刘海屏
val Activity.hasNotchScreen get() = TxImmersionBar.hasNotchScreen(this)

val Fragment.hasNotchScreen get() = TxImmersionBar.hasNotchScreen(this)

val android.app.Fragment.hasNotchScreen get() = TxImmersionBar.hasNotchScreen(this)

val View.hasNotchScreen get() = TxImmersionBar.hasNotchScreen(this)

// 获得刘海屏高度
val Activity.notchHeight get() = TxImmersionBar.getNotchHeight(this)

val Fragment.notchHeight get() = TxImmersionBar.getNotchHeight(this)

val android.app.Fragment.notchHeight get() = TxImmersionBar.getNotchHeight(this)

// 是否支持状态栏字体变色
val isSupportStatusBarDarkFont get() = TxImmersionBar.isSupportStatusBarDarkFont()

// 师傅支持导航栏图标
val isSupportNavigationIconDark get() = TxImmersionBar.isSupportNavigationIconDark()

// 检查view是否使用了fitsSystemWindows
val View.checkFitsSystemWindows get() = TxImmersionBar.checkFitsSystemWindows(this)

// 导航栏是否在底部
val Activity.isNavigationAtBottom get() = TxImmersionBar.isNavigationAtBottom(this)
val Fragment.isNavigationAtBottom get() = TxImmersionBar.isNavigationAtBottom(this)

val android.app.Fragment.isNavigationAtBottom get() = TxImmersionBar.isNavigationAtBottom(this)

// statusBarView扩展
fun Activity.fitsStatusBarView(view: View) = TxImmersionBar.setStatusBarView(this, view)

fun Fragment.fitsStatusBarView(view: View) = TxImmersionBar.setStatusBarView(this, view)

fun android.app.Fragment.fitsStatusBarView(view: View) = TxImmersionBar.setStatusBarView(this, view)

// titleBar扩展
fun Activity.fitsTitleBar(vararg view: View) = TxImmersionBar.setTitleBar(this, *view)

fun Fragment.fitsTitleBar(vararg view: View) = TxImmersionBar.setTitleBar(this, *view)

fun android.app.Fragment.fitsTitleBar(vararg view: View) = TxImmersionBar.setTitleBar(this, *view)

fun Activity.fitsTitleBarMarginTop(vararg view: View) = TxImmersionBar.setTitleBarMarginTop(this, *view)

fun Fragment.fitsTitleBarMarginTop(vararg view: View) = TxImmersionBar.setTitleBarMarginTop(this, *view)

fun android.app.Fragment.fitsTitleBarMarginTop(vararg view: View) = TxImmersionBar.setTitleBarMarginTop(this, *view)

// 隐藏状态栏
fun Activity.hideStatusBar() = TxImmersionBar.hideStatusBar(window)

fun Fragment.hideStatusBar() = activity?.run { TxImmersionBar.hideStatusBar(window) } ?: Unit

fun android.app.Fragment.hideStatusBar() = activity?.run { TxImmersionBar.hideStatusBar(window) }
    ?: Unit

// 显示状态栏
fun Activity.showStatusBar() = TxImmersionBar.showStatusBar(window)

fun Fragment.showStatusBar() = activity?.run { TxImmersionBar.showStatusBar(window) } ?: Unit

fun android.app.Fragment.showStatusBar() = activity?.run { TxImmersionBar.showStatusBar(window) }
    ?: Unit

// 解决顶部与布局重叠问题，不可逆
fun Activity.setFitsSystemWindows() = TxImmersionBar.setFitsSystemWindows(this)

fun Fragment.setFitsSystemWindows() = TxImmersionBar.setFitsSystemWindows(this)

fun android.app.Fragment.setFitsSystemWindows() = TxImmersionBar.setFitsSystemWindows(this)