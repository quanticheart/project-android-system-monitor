package com.quanticheart.monitor.system.system.hook

class Hook {
    /**
     * Xposed详细信息
     */
    var isHaveXposed: Xposed? = null

    /**
     * Substrate详细信息
     */
    var isHaveSubstrate: Substrate? = null

    /**
     * Frida详细信息
     */
    var isHaveFrida: Frida? = null

    class Xposed {
        /**
         * 包名检测
         */
        var isCheckXposedPackage = false

        /**
         * 检测调用栈中的可疑方法
         */
        var isCheckXposedHookMethod = false

        /**
         * 检测内存中可疑的jars
         */
        var isCheckXposedJars = false

        /**
         * 检测载入Xposed工具类
         */
        var isCheckClassLoader = false

        /**
         * 新增判断系统方法调用钩子
         */
        var isCheckNativeMethod = false

        /**
         * 虚拟检测Xposed环境
         */
        var isCheckSystem = false

        /**
         * 寻找Xposed运行库文件
         */
        var isCheckExecLib = false

        /**
         * 环境变量特征字判断
         */
        var isCheckXposedBridge = false
        private var cMap = false
        private var cPackage = false
        fun iscMap(): Boolean {
            return cMap
        }

        fun setcMap(cMap: Boolean) {
            this.cMap = cMap
        }

        fun iscPackage(): Boolean {
            return cPackage
        }

        fun setcPackage(cPackage: Boolean) {
            this.cPackage = cPackage
        }
    }

    class Substrate {
        /**
         * 包名检测
         */
        var isCheckSubstratePackage = false

        /**
         * 检测调用栈中的可疑方法
         */
        var isCheckSubstrateHookMethod = false

        /**
         * 检测内存中可疑的jars
         */
        var isCheckSubstrateJars = false
        private var cSo = false
        private var cMap = false
        private var cPackage = false
        fun iscSo(): Boolean {
            return cSo
        }

        fun setcSo(cSo: Boolean) {
            this.cSo = cSo
        }

        fun iscMap(): Boolean {
            return cMap
        }

        fun setcMap(cMap: Boolean) {
            this.cMap = cMap
        }

        fun iscPackage(): Boolean {
            return cPackage
        }

        fun setcPackage(cPackage: Boolean) {
            this.cPackage = cPackage
        }
    }

    class Frida {
        /**
         * 检测进程信息
         */
        var isCheckRunningProcesses = false

        /**
         * 检测内存中可疑的jars
         */
        var isCheckFridaJars = false
        private var cMap = false
        fun iscMap(): Boolean {
            return cMap
        }

        fun setcMap(cMap: Boolean) {
            this.cMap = cMap
        }
    }
}