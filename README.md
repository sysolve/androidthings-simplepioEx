# androidthings-simplepioEx
Android Things samples from Sysolve IoT Open Source Project

Android Things Simple PIO扩展案例 - 报警灯
====

代码已升级到Android Things Developer Preview 8，建议先阅读我的《Android Things DP8新特性》一文，了解DP8中需要注意的地方。
https://zhuanlan.zhihu.com/p/36168138

这是一个Android Things Simple PIO的扩展案例，使用入门开发配件包中的两个LED灯，1个蜂鸣器，1个按钮，实现LED灯闪烁、按钮响应、PWM驱动蜂鸣器功能。

![实物效果](https://github.com/sysolve/androidthings-simplepioEx/blob/master/photo.png)
![面包板接线图](https://github.com/sysolve/androidthings-simplepioEx/blob/master/blink_schematics.png)

目前树莓派、IMX6UL_PICO、IMX7D_PICO三种开发板，在扩展接口的定义和名称上有所差别，端口功能基本一致，名称有所不同。
我已汇总如下，代码中com.sysolve.androidthings.utils.BoardSpec根据运行的设备会自动选择端口配置：
![三种开发板的端口配置](https://github.com/sysolve/androidthings-simplepioEx/blob/master/port_define.png)

为方便使用不同开发板的开发者，可直接通过 PIN 脚编号获取GPIO名称：
```Java
String gpioName = BoardSpec.getInstance().getGpioPin(BoardSpec.PIN_29);
```

为方便调试，AndroidManifest.xml文件中的IOT_LAUNCHER项已注释，开机不会自动运行。如要开机自动运行，请自行将注释去掉。
```html
<!--
    <category android:name="android.intent.category.IOT_LAUNCHER" />
-->
```



