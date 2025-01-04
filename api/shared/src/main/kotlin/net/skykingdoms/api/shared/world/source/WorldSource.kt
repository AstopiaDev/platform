package net.skykingdoms.api.shared.world.source

import java.io.InputStream
import java.io.OutputStream

interface WorldSource {
    fun load(): InputStream
    fun save(): OutputStream
}