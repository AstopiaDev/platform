package net.astopia.core.protocol.packet.serializable

import io.netty.buffer.ByteBuf

/**
 * Represents a serializable object that can serialize itself into a [ByteBuf]
 * and deserialize itself from a [ByteBuf].
 */
interface Serializable<T : Serializable<T>> {
    /**
     * Serializes this object into the given [ByteBuf].
     *
     * @param out The [ByteBuf] to write the serialized data into.
     * @return This object for method chaining.
     */
    fun serialize(out: ByteBuf): T

    /**
     * Deserializes this object from the given [ByteBuf].
     *
     * @param in The [ByteBuf] to read the serialized data from.
     * @return This object for method chaining.
     */
    fun deserialize(inBuf: ByteBuf): T
}