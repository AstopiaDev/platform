package net.astopia.api.shared.world.source

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class FileWorldSource(private val file: File) : WorldSource {
    override fun load(): InputStream {
        return FileInputStream(file)
    }

    override fun save(): OutputStream {
        return FileOutputStream(file)
    }
}