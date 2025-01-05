package net.skykingdoms.api.shared.world.loader.slime

import com.github.luben.zstd.Zstd
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import net.kyori.adventure.nbt.BinaryTag
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.ByteArrayBinaryTag
import net.kyori.adventure.nbt.ByteBinaryTag
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.DoubleBinaryTag
import net.kyori.adventure.nbt.FloatBinaryTag
import net.kyori.adventure.nbt.IntArrayBinaryTag
import net.kyori.adventure.nbt.IntBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import net.kyori.adventure.nbt.LongArrayBinaryTag
import net.kyori.adventure.nbt.LongBinaryTag
import net.kyori.adventure.nbt.ShortBinaryTag
import net.kyori.adventure.nbt.StringBinaryTag
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.DynamicChunk
import net.minestom.server.instance.Instance
import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.palette.Palette
import net.skykingdoms.api.protocol.logging.Logging
import net.skykingdoms.api.shared.util.LocationUtil
import java.io.DataInputStream
import kotlin.math.max

// TODO: FIX LIGHTING

internal class SlimeFormatDeserializer(
    private val instance: Instance
) {
    companion object {
        private const val ARRAY_SIZE = 16 * 16 * 16 / (8 / 4)

        private fun readCompressed(stream: DataInputStream): ByteArray {
            val compressedLength = stream.readInt()
            val decompressedLength = stream.readInt()
            val compressedData = ByteArray(compressedLength)
            val decompressedData = ByteArray(decompressedLength)
            stream.read(compressedData)
            Zstd.decompress(decompressedData, compressedData)
            return decompressedData
        }

        private fun read(stream: DataInputStream): ByteArray {
            val length = stream.readInt()
            val data = ByteArray(length)
            stream.read(data)
            return data
        }

        private fun readCompound(tagBytes: ByteArray): CompoundBinaryTag? {
            if (tagBytes.isEmpty()) {
                return null
            }

            val nbtStream = BinaryTagIO.reader().read(FastByteArrayInputStream(tagBytes), BinaryTagIO.Compression.NONE)
            return nbtStream
        }

        private fun loadTileEntities(chunk: Chunk, tileEntitiesCompound: CompoundBinaryTag?) {
            //Logging.IMP.info(tileEntitiesCompound.toString())
        }

        fun deserializeBlockPalette(blockStateTag: CompoundBinaryTag, palette: Palette, sectionY: Int) {
            try {
                val paletteList = blockStateTag.getList("palette")
                val dataArray = blockStateTag.getLongArray("data")

                val blockMapping = mutableMapOf<Int, Block>()

                for (i in 0 until paletteList.size()) {
                    val blockCompound = paletteList.getCompound(i)
                    val blockName = blockCompound.getString("Name")
                    var block = Block.fromNamespaceId(blockName)

                    if (block != null) {
                        if (blockCompound.keySet().contains("Properties")) {
                            val properties = blockCompound.getCompound("Properties")
                            properties.forEach { (key, value) ->
                                block = block?.withProperty(key, (value as StringBinaryTag).value())
                            }
                        }
                        blockMapping[i] = block as Block
                    }
                }

                if (paletteList.size() == 0) {
                    throw IllegalArgumentException("Palette list is empty")
                }

                val bitsPerEntry = max(4, kotlin.math.ceil(kotlin.math.log2(paletteList.size().toDouble())).toInt())
                val entriesPerLong = 64 / bitsPerEntry
                val maxValue = (1L shl bitsPerEntry) - 1L

                for (y in 0 until Chunk.CHUNK_SECTION_SIZE) {
                    for (z in 0 until Chunk.CHUNK_SECTION_SIZE) {
                        for (x in 0 until Chunk.CHUNK_SECTION_SIZE) {
                            val blockIndex = y * Chunk.CHUNK_SECTION_SIZE * Chunk.CHUNK_SECTION_SIZE + z * Chunk.CHUNK_SECTION_SIZE + x
                            val longIndex = blockIndex / entriesPerLong
                            val bitOffset = (blockIndex % entriesPerLong) * bitsPerEntry

                            if (longIndex >= dataArray.size) continue

                            val paletteIndex = (dataArray[longIndex] shr bitOffset) and maxValue
                            val block = blockMapping[paletteIndex.toInt()] ?: continue

                            val absoluteY = (sectionY * Chunk.CHUNK_SECTION_SIZE) + y
                            palette.set(x, absoluteY, z, block.stateId())
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deserializeWorld(dataStream: DataInputStream): ByteArray {
        val chunkBytes = readCompressed(dataStream)

        return chunkBytes
    }

    fun readChunks(chunkBytes: ByteArray): Long2ObjectMap<Chunk> {
        val chunkMap = Long2ObjectOpenHashMap<Chunk>()
        val chunkData = DataInputStream(FastByteArrayInputStream(chunkBytes))

        val chunks = chunkData.readInt()
        for (i in 0 until chunks) {
            // ChunkPos
            val x = chunkData.readInt()
            val z = chunkData.readInt()

            val chunk = instance.chunkSupplier.createChunk(instance, x, z)

            // Sections
            val sectionCount = chunkData.readInt()

            for (i in 0 until sectionCount) {
                val sectionY = i - 4

                val section = chunk.getSection(sectionY)

                // Block Light Nibble Array
                val blockLightByteArray = ByteArray(ARRAY_SIZE)
                if (chunkData.readBoolean()) {
                    chunkData.read(blockLightByteArray)
                    section.blockLight().set(blockLightByteArray)
                }

                // Sky Light Nibble Array
                val skyLightByteArray = ByteArray(ARRAY_SIZE)
                if (chunkData.readBoolean()) {
                    chunkData.read(skyLightByteArray)
                    section.skyLight().set(skyLightByteArray)
                }

                // Block Data
                val blockStateData = ByteArray(chunkData.readInt())
                chunkData.read(blockStateData)
                val blockStateTag = readCompound(blockStateData)

                if (blockStateTag != null) {
                    deserializeBlockPalette(blockStateTag, section.blockPalette(), sectionY)
                }

                // Biome Data
                val biomeData: ByteArray = ByteArray(chunkData.readInt())
                chunkData.read(biomeData)
                val biomeTag = readCompound(biomeData)

                if (biomeTag != null) {
                    //prettyPrintNBT(biomeTag as BinaryTag)
                }
            }

            val heightMapData = ByteArray(chunkData.readInt())
            chunkData.read(heightMapData)
            val heightMaps = readCompound(heightMapData)

            if (heightMaps != null) {
                chunk.loadHeightmapsFromNBT(heightMaps)
            }

            val tileEntities = read(chunkData)
            val tileEntitiesCompound = readCompound(tileEntities)
            loadTileEntities(chunk, tileEntitiesCompound)

            val entities = read(chunkData)
            val entitiesCompound = readCompound(entities)

            val rawExtra = read(chunkData)
            val extra = readCompound(rawExtra)

            chunkMap.put(LocationUtil.chunkPos(x, z), chunk)
        }

        return chunkMap
    }
}