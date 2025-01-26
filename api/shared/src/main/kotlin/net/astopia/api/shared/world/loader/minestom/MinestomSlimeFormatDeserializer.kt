package net.astopia.api.shared.world.loader.minestom

import com.github.luben.zstd.Zstd
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.DynamicChunk
import net.minestom.server.instance.Instance
import net.minestom.server.instance.Section
import net.minestom.server.instance.palette.Palette
import net.astopia.api.shared.util.LocationUtil
import java.io.DataInputStream

internal class MinestomSlimeFormatDeserializer(
    private val instance: Instance,
    private val dataStream: DataInputStream
) {
    companion object {
        private const val ARRAY_SIZE = 16 * 16 * 16 / (8 / 4)

        private fun readCompressed(stream: DataInputStream): ByteArray {
            val compressedLength = stream.readInt();
            val decompressedLength = stream.readInt();
            val compressedData = ByteArray(compressedLength);
            val decompressedData = ByteArray(decompressedLength);
            stream.read(compressedData);
            Zstd.decompress(decompressedData, compressedData);
            return decompressedData;
        }

        private fun read(stream: DataInputStream): ByteArray {
            val length = stream.readInt()
            val data = ByteArray(length)
            stream.read(data)
            return data
        }

        private fun readCompound(tagBytes: ByteArray): CompoundBinaryTag? {
            if (tagBytes.isEmpty()) {
                return null;
            }

            val nbtStream = BinaryTagIO.reader().read(FastByteArrayInputStream(tagBytes), BinaryTagIO.Compression.NONE);
            return nbtStream
        }

        private fun deserializePalette(paletteData: ByteArray, palette: Palette) {
            var index = 0
            var byteIndex = 0
            var bitIndex = 0

            while (byteIndex < paletteData.size) {
                var value = 0
                for (bit in 0 until palette.maxBitsPerEntry()) {
                    val byteValue = paletteData[byteIndex].toInt() and 0xFF
                    value = value or ((byteValue shr bitIndex) and 1 shl bit)

                    bitIndex++

                    if (bitIndex >= 8) {
                        bitIndex = 0
                        byteIndex++
                    }
                }

                val x = index % palette.dimension()
                val y = (index / palette.dimension()) % palette.dimension()
                val z = index / (palette.dimension() * palette.dimension())

                palette.set(x, y, z, value)

                index++
            }
        }
    }

    fun deserializeWorld(dataStream: DataInputStream): ByteArray {
        val chunkBytes = readCompressed(dataStream)

        // we don't really need extra tags, do we??
/*        val extraTagBytes = readCompressed(dataStream)
        val extraTag = readCompound(extraTagBytes)*/

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

            // Sections
            val sectionAmount = -4 - 19 + 1
            val chunkSections: Array<Section> = Array(sectionAmount) { throw IllegalStateException("Not initialized") }
            val sectionCount = chunkData.readInt()

            for (i in 0 until sectionCount) {
                val section = Section()

                // Block Light Nibble Array
                val blockLight = section.blockLight()
                if (chunkData.readBoolean()) {
                    val blockLightByteArray = ByteArray(ARRAY_SIZE)
                    chunkData.read(blockLightByteArray)
                    blockLight.set(blockLightByteArray)
                }

                // Sky Light Nibble Array
                val skyLight = section.skyLight()
                if (chunkData.readBoolean()) {
                    val skyLightByteArray = ByteArray(ARRAY_SIZE)
                    chunkData.read(skyLightByteArray)
                    skyLight.set(skyLightByteArray)
                }

                //  TAG_List("palette"): 1 entries of type TAG_Compound
                //[13:15:06 INFO]:    {
                //[13:15:06 INFO]:       TAG_Compound: 1 entries
                //[13:15:06 INFO]:       {
                //[13:15:06 INFO]:          TAG_String("Name"): minecraft:air
                //[13:15:06 INFO]:       }
                // Block Data
                val blockPaletteData: ByteArray = ByteArray(chunkData.readInt())
                val blocksPalette: Palette = section.blockPalette()

                chunkData.read(blockPaletteData)

                deserializePalette(blockPaletteData, blocksPalette)

                // Biome Data
                val biomePaletteData: ByteArray = ByteArray(chunkData.readInt())
                val biomePalette: Palette = section.biomePalette()

                chunkData.read(biomePaletteData)

                deserializePalette(biomePaletteData, biomePalette)

                chunkSections[i] = section
            }

            val chunk = DynamicChunk(instance, x, z)

            // Motion Blocking Height Map
            val motionBlockingHeightMapSize = chunkData.readInt()
            val motionBlockingHeightMap: LongArray = LongArray(motionBlockingHeightMapSize)

            for (i in 0 until motionBlockingHeightMapSize) {
                motionBlockingHeightMap[i] = chunkData.readLong()
            }

            chunk.motionBlockingHeightmap().loadFrom(motionBlockingHeightMap)

            val worldSurfaceHeightMapSize = chunkData.readInt()
            val worldSurfaceHeightMap: LongArray = LongArray(worldSurfaceHeightMapSize)

            for (i in 0 until worldSurfaceHeightMapSize) {
                worldSurfaceHeightMap[i] = chunkData.readLong()
            }

            chunk.worldSurfaceHeightmap().loadFrom(worldSurfaceHeightMap)

            chunkMap.put(LocationUtil.chunkPos(x, z), chunk)
        }

        return chunkMap
    }
}