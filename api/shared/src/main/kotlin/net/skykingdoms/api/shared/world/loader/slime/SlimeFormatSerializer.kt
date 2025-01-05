package net.skykingdoms.api.shared.world.loader.slime

import com.github.luben.zstd.Zstd
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream
import net.minestom.server.instance.Instance
import net.minestom.server.instance.palette.Palette
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Objects
import kotlin.experimental.or

class SlimeFormatSerializer {
    companion object {
        private fun serializeChunks(instance: Instance): ByteArray {
            val chunks = instance.chunks
            val outByteStream = FastByteArrayOutputStream(16384)
            val outStream = DataOutputStream(outByteStream)

            // Prune chunks TODO: chunk pruner maybe
            /*val chunksToSave = chunks.stream()
                .filter { chunk ->  }*/

            val chunksToSave = chunks.stream()
                .filter {
                    !it.isReadOnly
                }
                .toList()

            outStream.writeInt(chunksToSave.size)
            for (chunk in chunksToSave) {
                outStream.writeInt(chunk.chunkX)
                outStream.writeInt(chunk.chunkZ)

                val sections = chunk.sections.filter { Objects.nonNull(it) }.toTypedArray()

                outStream.writeInt(sections.size)

                for (section in sections) {
                    // Block Light
                    val blockLight = section.blockLight().array().size == 0
                    outStream.writeBoolean(blockLight)

                    if (blockLight) {
                        outStream.write(section.blockLight().array())
                    }

                    // Sky Light
                    val skyLight = section.skyLight().array().size == 0
                    outStream.writeBoolean(skyLight)

                    if (skyLight) {
                        outStream.write(section.skyLight().array())
                    }

                    // Block Data
                    val serializedBlockPalette = serializePalette(section.blockPalette())
                    if (serializedBlockPalette.isNotEmpty()) {
                        outStream.writeInt(serializedBlockPalette.size)
                        outStream.write(serializedBlockPalette)
                    }

                    // Biome Data
                    val serializedBiomePalette = serializePalette(section.biomePalette())
                    if (serializedBiomePalette.isNotEmpty()) {
                        outStream.writeInt(serializedBiomePalette.size)
                        outStream.write(serializedBiomePalette)
                    }
                }

                // Motion Blocking Height Map
                val motionBlockingHeightMap = chunk.motionBlockingHeightmap().nbt

                outStream.writeInt(motionBlockingHeightMap.size)
                for (i in 0 until motionBlockingHeightMap.size) {
                    outStream.writeLong(motionBlockingHeightMap[i])
                }


                // World Surface Height Map
                val worldSurfaceHeightMap = chunk.worldSurfaceHeightmap().nbt

                outStream.writeInt(worldSurfaceHeightMap.size)
                for (i in 0 until worldSurfaceHeightMap.size) {
                    outStream.writeLong(worldSurfaceHeightMap[i])
                }
            }

            return outByteStream.array
        }

        private fun serializePalette(palette: Palette): ByteArray {
            val bytesPerEntry = (palette.maxBitsPerEntry() + 7) / 8
            val paletteValues: ByteArray = ByteArray(palette.count() * bytesPerEntry)

            var index = 0;
            palette.getAll { x, y, z, value ->
                for (bit in 0 until palette.maxBitsPerEntry()) {
                    val byteIndex = index / 8
                    val bitIndex = index % 8
                    val bitValue = (value shr bitIndex) and 1

                    paletteValues[byteIndex] = paletteValues[byteIndex] or (bitValue shl bitIndex).toByte()

                    index++
                }
            }

            return paletteValues
        }
    }

    fun serialize(
        outputDataStream: DataOutputStream,
        instance: Instance
    ) : ByteArray {
        val outByteStream = FastByteArrayOutputStream()
        val outStream = DataOutputStream(outByteStream)

        // File Header and Slime version
        outStream.write(SlimeFormatLoader.slimeHeader)
        outStream.writeByte(SlimeFormatLoader.SLIME_VERSION.toInt())

        // World version
        //outStream.writeInt(instance.)

        val chunkData = serializeChunks(instance)
        val compressedChunkData = Zstd.compress(chunkData)

        outStream.writeInt(compressedChunkData.size)
        outStream.writeInt(chunkData.size)
        outStream.write(compressedChunkData)

        return outByteStream.array
    }

    fun createFile(instance: Instance, file: File) {
        if (!file.exists()) {
            file.createNewFile()
        }

        FileOutputStream(file).use { fileOutputStream ->
            val dataOutputStream = DataOutputStream(fileOutputStream)
            val serializedData = serialize(dataOutputStream, instance)
            dataOutputStream.write(serializedData)
        }
    }
}
