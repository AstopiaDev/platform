package net.skykingdoms.api.shared.user

import net.minestom.server.entity.Player
import net.skykingdoms.api.backend.permission.Group
import net.skykingdoms.api.backend.setting.Setting
import net.skykingdoms.api.backend.setting.SettingType
import net.skykingdoms.backend.base.gamer.CommonGamer
import net.skykingdoms.backend.base.permission.UserRank
import java.time.LocalDateTime
import java.util.UUID

class ServerUser(val player: Player, val uuid: UUID) : CommonGamer(uuid) {
    override fun sendTitle(p0: String, p1: String, p2: Int, p3: Int, p4: Int) {
        TODO("Not yet implemented")
    }

    override fun disconnect(p0: String) {
        TODO("Not yet implemented")
    }

    override fun addRank(p0: Group, p1: LocalDateTime?) {
        TODO("Not yet implemented")
    }

    override fun addRank(p0: UserRank) {
        TODO("Not yet implemented")
    }

    override fun setRank(p0: Group, p1: LocalDateTime?) {
        TODO("Not yet implemented")
    }

    override fun setSetting(
        p0: SettingType,
        p1: Setting<*, *>
    ) {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> setProperty(p0: String, p1: T?) {
        TODO("Not yet implemented")
    }

    override fun removeProperty(p0: String) {
        TODO("Not yet implemented")
    }

    override fun dropProperty(p0: String) {
        TODO("Not yet implemented")
    }

    override fun addProperty(p0: String, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun dispatchCommand(p0: String?) {
        TODO("Not yet implemented")
    }
}