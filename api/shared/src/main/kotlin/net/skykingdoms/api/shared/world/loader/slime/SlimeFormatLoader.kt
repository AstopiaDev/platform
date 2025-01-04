package net.skykingdoms.api.shared.world.loader.slime

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.IChunkLoader
import net.minestom.server.instance.Instance
import net.skykingdoms.api.protocol.logging.Logging
import net.skykingdoms.api.shared.util.LocationUtil
import net.skykingdoms.api.shared.world.source.WorldSource
import java.io.DataInputStream
import java.io.DataOutputStream

class SlimeFormatLoader(
    instance: Instance,
    private val slimeSource: WorldSource,
    private val readOnly: Boolean = false,
) : IChunkLoader {

    companion object {
        val slimeHeader = byteArrayOf(-79, 11)
        val slimeVersion: Byte = 12
    }

    private val chunks: Long2ObjectMap<Chunk>;

    init {
        val dataStream = DataInputStream(slimeSource.load())
        val deserializer: SlimeFormatDeserializer = SlimeFormatDeserializer(instance, dataStream)

        // File Header and Slime version
        val slimeHeader = dataStream.readNBytes(2)
        val slimeVersion = dataStream.readByte()

        Logging.IMP.info("${slimeVersion.toInt()} yuh!")
        Logging.IMP.info("${dataStream.readInt()} world version")

        // Chunks
        val chunkBytes: ByteArray = deserializer.deserializeWorld(dataStream)

        chunks = deserializer.readChunks(chunkBytes)

        dataStream.close()
    }

    override fun loadChunk(instance: Instance, chunkX: Int, chunkZ: Int): Chunk? {
        return chunks[LocationUtil.chunkPos(chunkX, chunkZ)]
    }

    override fun saveChunk(chunk: Chunk) {
        if (readOnly) return
        chunks[LocationUtil.chunkPos(chunk.chunkX, chunk.chunkZ)] = chunk
    }

    override fun saveInstance(instance: Instance) {
        if (readOnly) return

        val outputStream = slimeSource.save()
        val dataOutputStream = DataOutputStream(outputStream)

        val serializer = SlimeFormatSerializer()
        serializer.serialize(dataOutputStream, instance)
    }
}
