package com.common.widget.aciton;

/**
 * Created by JustinWjq
 * @date 2019-12-23.
 * description：网络状态
 */
public interface NetStatusAction {

    //网络状态更改为wifi状态
    public default void changeWifi(){};
    //网络状态更改为移动数据
    public default void changeMobile(){};
    //网络状态更改为没有网络
    public default void changeNetNull(){};
}