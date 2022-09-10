package com.quanticheart.monitor.system.camera

import org.json.JSONArray

class Camera {
    /**
     * 摄像头的位置
     */
    var cameraFacing: String? = null

    /**
     * 摄像头支持水平
     */
    var cameraLevel: String? = null

    /**
     * 摄像头支持的格式
     */
    private var outputFormats: JSONArray? = null

    /**
     * 设备支持的像差校正模式列表
     */
    private var aberrationModes: JSONArray? = null

    /**
     * 本相机设备支持的自动曝光防条纹模式列表
     */
    private var antiBandingModes: JSONArray? = null

    /**
     * 是否有闪光灯
     */
    var isCameraFlashInfo = false

    /**
     * 本相机设备支持的自动曝光模式列表
     */
    private var aeAvailableModes: JSONArray? = null

    /**
     * 相机设备支持的帧频范围列表
     */
    private var fpsRanges: JSONArray? = null

    /**
     * 此相机设备支持的最大和最小曝光补偿值
     */
    var compensationRange: String? = null

    /**
     * 可以更改曝光补偿的最小步长
     */
    var compensationStep = 0.0

    /**
     * 是否锁定自动曝光
     */
    var isLockAvailable = false

    /**
     * 相机设备支持的自动对焦（AF）模式列表
     */
    private var afAvailableModes: JSONArray? = null

    /**
     * 本相机设备支持的色彩效果列表
     */
    private var availableEffects: JSONArray? = null

    /**
     * 本相机设备支持的控制模式列表
     */
    private var availableModes: JSONArray? = null

    /**
     * 本相机设备支持的场景模式列表
     */
    private var availableSceneModes: JSONArray? = null

    /**
     * 本相机设备支持的视频稳定模式列表
     */
    private var videoStabilizationModes: JSONArray? = null

    /**
     * 本相机设备支持的自动白平衡模式列表
     */
    private var awbAvailableModes: JSONArray? = null

    /**
     * 是否支持自动白平衡
     */
    var isAwbLockAvailable = false
    var sensorInfoSensitivityRange: String? = null
    var maxRegionsAe = 0
    var maxRegionsAf = 0
    var maxRegionsAwb = 0
    var rawSensitivityBoostRange: String? = null
    var isDepthIsExclusive = false
    private var correctionAvailableModes: JSONArray? = null
    private var availableEdgeModes: JSONArray? = null
    private var availableHotPixelModes: JSONArray? = null
    private var jpegAvailableThumbnailSizes: JSONArray? = null
    private var lensDistortion: JSONArray? = null
    private var lensInfoAvailableApertures: JSONArray? = null
    private var lensInfoAvailableFilterDensities: JSONArray? = null
    private var lensInfoAvailableFocalLengths: JSONArray? = null
    private var availableOpticalStabilization: JSONArray? = null
    private var lensIntrinsicCalibration: JSONArray? = null
    private var lensPoseRotation: JSONArray? = null
    private var lensPoseTranslation: JSONArray? = null
    private var requestAvailableCapabilities: JSONArray? = null
    var supportedHardwareLevel: String? = null
    var infoVersion: String? = null
    var focusDistanceCalibration: String? = null
    var lensPoseReference: String? = null
    var cameraSensorSyncType: String? = null
    var hyperFocalDistance = 0f
    var minimumFocusDistance = 0f
    private var availableNoiseReductionModes: JSONArray? = null
    private var sensorAvailableTestPatternModes: JSONArray? = null
    var maxCaptureStall = 0
    var requestMaxNumInputStreams = 0
    var requestMaxNumOutputProc = 0
    var requestMaxNumOutputProcStalling = 0
    var requestMaxNumOutputRaw = 0
    var requestPartialResultCount = 0
    var statisticsInfoMaxFaceCount = 0
    var requestPipelineMaxDepth: Byte = 0
    var scalerAvailableMaxDigitalZoom = 0f
    var scalerCroppingType: String? = null
    var sensorInfoColorFilterArrangement: String? = null
    var sensorInfoExposureTimeRange: String? = null
    var syncMaxLatency: String? = null
    var isSensorInfoLensShadingApplied = false
    var sensorInfoaxFrameDuration: Long = 0
    var sensorInfoTimestampSource: String? = null
    var sensorReferenceIlluminant1: String? = null
    private var shadingAvailableModes: JSONArray? = null
    private var availableFaceDetectModes: JSONArray? = null
    private var availableLensShadingMapModes: JSONArray? = null
    private var availableOisDataModes: JSONArray? = null
    private var availableToneMapModes: JSONArray? = null
    var sensorInfoWhiteLevel = 0
    var sensorMaxAnalogSensitivity = 0
    var sensorOrientation = 0
    var tonemapMaxCurvePoints = 0

    fun setAvailableToneMapModes(availableToneMapModes: JSONArray?) {
        this.availableToneMapModes = availableToneMapModes
    }

    fun setAvailableOisDataModes(availableOisDataModes: JSONArray?) {
        this.availableOisDataModes = availableOisDataModes
    }

    fun setAvailableLensShadingMapModes(availableLensShadingMapModes: JSONArray?) {
        this.availableLensShadingMapModes = availableLensShadingMapModes
    }

    fun setAvailableFaceDetectModes(availableFaceDetectModes: JSONArray?) {
        this.availableFaceDetectModes = availableFaceDetectModes
    }

    fun setShadingAvailableModes(shadingAvailableModes: JSONArray?) {
        this.shadingAvailableModes = shadingAvailableModes
    }

    fun setAvailableOpticalStabilization(availableOpticalStabilization: JSONArray?) {
        this.availableOpticalStabilization = availableOpticalStabilization
    }

    fun setLensInfoAvailableFocalLengths(lensInfoAvailableFocalLengths: JSONArray?) {
        this.lensInfoAvailableFocalLengths = lensInfoAvailableFocalLengths
    }

    fun setLensInfoAvailableFilterDensities(lensInfoAvailableFilterDensities: JSONArray?) {
        this.lensInfoAvailableFilterDensities = lensInfoAvailableFilterDensities
    }

    fun setLensInfoAvailableApertures(lensInfoAvailableApertures: JSONArray?) {
        this.lensInfoAvailableApertures = lensInfoAvailableApertures
    }

    fun setAwbAvailableModes(awbAvailableModes: JSONArray?) {
        this.awbAvailableModes = awbAvailableModes
    }

    fun setVideoStabilizationModes(videoStabilizationModes: JSONArray?) {
        this.videoStabilizationModes = videoStabilizationModes
    }

    fun setAvailableSceneModes(availableSceneModes: JSONArray?) {
        this.availableSceneModes = availableSceneModes
    }

    fun setAvailableEffects(availableEffects: JSONArray?) {
        this.availableEffects = availableEffects
    }

    fun setAfAvailableModes(afAvailableModes: JSONArray?) {
        this.afAvailableModes = afAvailableModes
    }

    fun setFpsRanges(fpsRanges: JSONArray?) {
        this.fpsRanges = fpsRanges
    }

    fun setAeAvailableModes(aeAvailableModes: JSONArray?) {
        this.aeAvailableModes = aeAvailableModes
    }

    fun setAntiBandingModes(antiBandingModes: JSONArray?) {
        this.antiBandingModes = antiBandingModes
    }

    fun setAberrationModes(aberrationModes: JSONArray?) {
        this.aberrationModes = aberrationModes
    }

    fun setAvailableModes(availableModes: JSONArray?) {
        this.availableModes = availableModes
    }

    fun setOutputFormats(outputFormats: JSONArray?) {
        this.outputFormats = outputFormats
    }

    fun setCorrectionAvailableModes(correctionAvailableModes: JSONArray?) {
        this.correctionAvailableModes = correctionAvailableModes
    }

    fun setAvailableEdgeModes(availableEdgeModes: JSONArray?) {
        this.availableEdgeModes = availableEdgeModes
    }

    fun setAvailableHotPixelModes(availableHotPixelModes: JSONArray?) {
        this.availableHotPixelModes = availableHotPixelModes
    }

    fun setJpegAvailableThumbnailSizes(jpegAvailableThumbnailSizes: JSONArray?) {
        this.jpegAvailableThumbnailSizes = jpegAvailableThumbnailSizes
    }

    fun setLensDistortion(lensDistortion: JSONArray?) {
        this.lensDistortion = lensDistortion
    }

    fun setLensIntrinsicCalibration(lensIntrinsicCalibration: JSONArray?) {
        this.lensIntrinsicCalibration = lensIntrinsicCalibration
    }

    fun setLensPoseRotation(lensPoseRotation: JSONArray?) {
        this.lensPoseRotation = lensPoseRotation
    }

    fun setLensPoseTranslation(lensPoseTranslation: JSONArray?) {
        this.lensPoseTranslation = lensPoseTranslation
    }

    fun setAvailableNoiseReductionModes(availableNoiseReductionModes: JSONArray?) {
        this.availableNoiseReductionModes = availableNoiseReductionModes
    }

    fun setRequestAvailableCapabilities(requestAvailableCapabilities: JSONArray?) {
        this.requestAvailableCapabilities = requestAvailableCapabilities
    }

    fun setSensorAvailableTestPatternModes(sensorAvailableTestPatternModes: JSONArray?) {
        this.sensorAvailableTestPatternModes = sensorAvailableTestPatternModes
    }
}