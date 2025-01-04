package net.skykingdoms.api.shared

interface Server {
    fun onStart()
    fun onStop()
    fun onActive()
}