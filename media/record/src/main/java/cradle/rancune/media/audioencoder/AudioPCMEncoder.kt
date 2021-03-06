package cradle.rancune.media.audioencoder

import android.media.AudioRecord
import cradle.rancune.internal.logger.AndroidLog
import cradle.rancune.media.*

/**
 * Created by Rancune@126.com 2020/3/15.
 */
class AudioPCMEncoder(config: AudioConfig) : AudioEncoder {

    private val byteArraySize: Int = if (config.minBufferSize > 0) {
        config.minBufferSize
    } else {
        2048
    }

    private var infoListener: OnInfoListener? = null
    private var dataListener: OnDataListener? = null

    override fun setOnInfoListener(infoListener: OnInfoListener?) {
        this.infoListener = infoListener
    }

    override fun setOnDataListener(dataListener: OnDataListener?) {
        this.dataListener = dataListener
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun encode(record: AudioRecord, endOfStream: Boolean) {
        if (endOfStream) {
            return
        }
        val data = EncodedData()
        data.offset = 0
        data.byteArray = ByteArray(byteArraySize)
        val size = record.read(data.byteArray!!, data.offset, byteArraySize)
        when {
            size == AudioRecord.ERROR_INVALID_OPERATION -> {
                AndroidLog.d("AudioPCMEncoder", "error: ERROR_INVALID_OPERATION")
            }
            size == AudioRecord.ERROR_BAD_VALUE -> {
                AndroidLog.d("AudioPCMEncoder", "error: ERROR_BAD_VALUE")
            }
            size == AudioRecord.ERROR_DEAD_OBJECT -> {
                AndroidLog.d("AudioPCMEncoder", "error: ERROR_DEAD_OBJECT")
            }
            size > 0 -> {
                data.size = size
                dataListener?.onOutputAvailable(data)
            }
        }
    }
}