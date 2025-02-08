package net.astopia.core.protocol.util

import io.netty.buffer.ByteBuf
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.charset.StandardCharsets
import net.astopia.core.protocol.packet.serializable.Serializable

object PacketUtils {
    fun writeString(buf: ByteBuf, s: String) {
        val bytes = s.toByteArray(StandardCharsets.UTF_8)
        writeVarInt(buf, bytes.size)
        buf.writeBytes(bytes)
    }

    @Throws(UnknownHostException::class)
    fun readAddress(buf: ByteBuf): InetAddress {
        val bytes = ByteArray(4)
        buf.readBytes(bytes)
        return InetAddress.getByAddress(bytes)
    }

    fun writeAddress(buf: ByteBuf, address: InetAddress) {
        buf.writeBytes(address.address)
    }

    inline fun <reified T : Enum<T>> readEnum(buf: ByteBuf, safe: Boolean = false): T? {
        return try {
            if (safe) {
                enumValueOf<T>(readString(buf))
            } else {
                T::class.java.enumConstants[buf.readUnsignedByte().toInt()]
            }
        } catch (t: Throwable) {
            System.err.printf("Cannot find an enum %s [%s]: %s%n", T::class.java, safe, t)
            null
        }
    }

    fun writeEnum(buf: ByteBuf, e: Enum<*>, safe: Boolean = false) {
        if (safe) {
            writeString(buf, e.name)
        } else {
            buf.writeByte(e.ordinal)
        }
    }

    inline fun <T, reified C : Collection<T>> readCollection(
        buf: ByteBuf,
        noinline colFactory: (Int) -> C,
        reader: () -> T?
    ): C {
        val size = readVarInt(buf)
        val col = colFactory(size)
        repeat(size) {
            val element = reader()
            if (element != null) {
                @Suppress("UNCHECKED_CAST")
                (col as MutableCollection<T>).add(element)
            }
        }
        return col
    }

    inline fun <T> readArray(
        buf: ByteBuf,
        noinline arrayFactory: (Int) -> Array<T?>,
        reader: () -> T?
    ): Array<T?> {
        val size = readVarInt(buf)
        val array = arrayFactory(size)
        for (i in 0 until size) {
            array[i] = reader()
        }
        return array
    }

    inline fun <T> writeArray(buf: ByteBuf, array: Array<T>, writer: (T) -> Unit) {
        writeVarInt(buf, array.size)
        array.forEach(writer)
    }

    fun readVarIntArray(buf: ByteBuf): IntArray {
        val size = buf.readUnsignedByte().toInt()
        return IntArray(size) { readVarInt(buf) }
    }

    fun writeVarIntArray(buf: ByteBuf, array: IntArray) {
        buf.writeByte(array.size)
        array.forEach { writeVarInt(buf, it) }
    }

    inline fun <reified T : Serializable<T>, reified C : Collection<T>> readSerializableCollection(
        buf: ByteBuf,
        noinline colFactory: (Int) -> C,
        factory: () -> T
    ): C {
        return readCollection(buf, colFactory) { factory().deserialize(buf) }
    }

    inline fun <T> writeCollection(buf: ByteBuf, collection: Collection<T>, writer: (T) -> Unit) {
        writeVarInt(buf, collection.size)
        collection.forEach(writer)
    }

    inline fun <reified T : Serializable<T>> writeSerializableCollection(buf: ByteBuf, collection: Collection<T>) {
        writeCollection(buf, collection) { it.serialize(buf) }
    }

    inline fun <reified T : Serializable<T>> writeSerializableArray(buf: ByteBuf, array: Array<T>) {
        writeArray(buf, array) { it.serialize(buf) }
    }

    inline fun <reified T : Serializable<T>> readSerializableArray(
        buf: ByteBuf,
        noinline arrayFactory: (Int) -> Array<T?>,
        factory: () -> T
    ): Array<T?> {
        return readArray(buf, arrayFactory) { factory().deserialize(buf) }
    }

    fun readIntArray(buf: ByteBuf): IntArray {
        val size = buf.readUnsignedByte().toInt()
        return IntArray(size) { buf.readInt() }
    }

    fun writeIntArray(buf: ByteBuf, array: IntArray) {
        buf.writeByte(array.size)
        array.forEach { buf.writeInt(it) }
    }

    fun readString(buf: ByteBuf): String {
        val size = readVarInt(buf)
        if (size < 0) throw IllegalStateException("Received wrong string size")
        if (size > buf.readableBytes()) throw IllegalArgumentException("$size > ${buf.readableBytes()}")
        val bytes = ByteArray(size)
        buf.readBytes(bytes)
        return String(bytes, StandardCharsets.UTF_8)
    }

    fun getVarIntSize(input: Int): Int {
        return when {
            (input and 0xFFFFFF80.toInt()) == 0 -> 1
            (input and 0xFFFFC000.toInt()) == 0 -> 2
            (input and 0xFFE00000.toInt()) == 0 -> 3
            (input and 0xF0000000.toInt()) == 0 -> 4
            else -> 5
        }
    }

    fun readVarInt(buf: ByteBuf): Int {
        var result = 0
        var numRead = 0
        var read: Byte
        do {
            read = buf.readByte()
            result = result or ((read.toInt() and 127) shl numRead++ * 7)
            if (numRead > 5) throw RuntimeException("VarInt is too big")
        } while (read.toInt() and 128 == 128)
        return result
    }

    fun writeVarInt(buf: ByteBuf, value: Int) {
        var v = value
        while (v and -128 != 0) {
            buf.writeByte(v and 127 or 128)
            v = v ushr 7
        }
        buf.writeByte(v)
    }
}