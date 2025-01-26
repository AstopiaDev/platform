package net.astopia.api.shared.world.loader.minestom

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.IChunkLoader
import net.minestom.server.instance.Instance
import net.astopia.api.shared.util.LocationUtil
import net.astopia.api.shared.world.source.WorldSource
import java.io.DataInputStream
import java.io.DataOutputStream

class MinestomSlimeFormatLoader(
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
        val deserializer: MinestomSlimeFormatDeserializer = MinestomSlimeFormatDeserializer(instance, dataStream)

        // File Header and Slime version
        val slimeHeader = dataStream.readNBytes(2)
        val slimeVersion = dataStream.readByte()

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

        val serializer = MinestomSlimeFormatSerializer()
        serializer.serialize(dataOutputStream, instance)
    }
}
