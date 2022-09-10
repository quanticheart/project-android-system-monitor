package com.quanticheart.monitor.system.build

class Build {
    var board: String? = null

    /**
     * 系统引导程序版本号
     */
    var bootloader: String? = null

    /**
     * 系统定制商
     */
    var brand: String? = null

    /**
     * 设备参数
     */
    var device: String? = null

    /**
     * 显示屏参数
     */
    var display: String? = null

    /**
     * 硬件名
     */
    var fingerprint: String? = null

    /**
     * 内核命令行中的硬件名
     */
    var hardware: String? = null

    /**
     * host
     */
    var host: String? = null

    /**
     * 标签
     */
    var id: String? = null

    /**
     * 硬件厂商
     */
    var manufacturer: String? = null

    /**
     * 版本
     */
    var model: String? = null

    /**
     * 手机厂商
     */
    var product: String? = null

    /**
     * 返回无线电固件的版本字符串
     */
    var radio: String? = null

    /**
     * 获取硬件序列号
     */
    var serial: String? = null

    /**
     * 描述Build的标签
     */
    var tags: String? = null

    /**
     * time
     */
    var time: Long = 0

    /**
     * type
     */
    var type: String? = null

    /**
     * user
     */
    var user: String? = null

    /**
     * os版本
     */
    var osVersion: String? = null

    /**
     * 版本
     */
    var releaseVersion: String? = null

    /**
     * 当前开发代码名称
     */
    var codeName: String? = null

    /**
     * 基础源代码控件用于表示此构建的内部值
     */
    var incremental: String? = null

    /**
     * SDK的版本
     */
    var sdkInt = 0

    /**
     * SDK的预览版本
     */
    var previewSdkInt = 0

    /**
     * 用户可见的安全补丁程序级别
     */
    var securityPatch: String? = null
}