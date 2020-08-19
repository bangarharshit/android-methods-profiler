package com.github.grishberg.profiler.ui.dialogs.recorder

import com.github.grishberg.profiler.common.AppLogger
import com.github.grishberg.profiler.common.settings.SettingsRepository
import com.github.grishberg.profiler.ui.Main
import com.github.grishberg.tracerecorder.MethodTraceEventListener
import com.github.grishberg.tracerecorder.MethodTraceRecorder
import com.github.grishberg.tracerecorder.MethodTraceRecorderImpl

interface MethodTraceRecorderFactory {
    fun create(listener: MethodTraceEventListener): MethodTraceRecorder
}

private const val SETTINGS_ROOT = "MethodTraceRecordDialog"
private const val DEBUG_PORT_SETTINGS = "$SETTINGS_ROOT.debugPort"
private const val DEFAULT_DEBUG_PORT = 8699

private const val TIMEOUT_BEFORE_RECORDING = "$SETTINGS_ROOT.timeoutBeforeRecording"
private const val DEFAULT_TIMEOUT_BEFORE_RECORDING = 10

internal class MethodTraceRecorderFactoryImpl(
    private val logger: AppLogger,
    private val settings: SettingsRepository
) : MethodTraceRecorderFactory {
    private val debugPort: Int = settings.getIntValueOrDefault(
        DEBUG_PORT_SETTINGS,
        DEFAULT_DEBUG_PORT
    )

    init {
        settings.setIntValue(DEBUG_PORT_SETTINGS, debugPort)
    }

    override fun create(listener: MethodTraceEventListener): MethodTraceRecorder {
        return MethodTraceRecorderImpl(
            listener,
            true,
            false,
            RecorderLoggerWrapper(
                logger
            ),
            settings.getStringValue(Main.SETTINGS_ANDROID_HOME),
            debugPort,
            applicationWaitPostTimeoutInMilliseconds = settings.getIntValueOrDefault(
                TIMEOUT_BEFORE_RECORDING,
                DEFAULT_TIMEOUT_BEFORE_RECORDING
            ).toLong()

        )
    }
}