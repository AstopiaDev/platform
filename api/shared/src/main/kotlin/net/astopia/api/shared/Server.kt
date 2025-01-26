package net.astopia.api.shared

interface Server {
    fun onStart()
    fun onStop()
    fun onActive()
}