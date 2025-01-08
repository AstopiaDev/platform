package net.skykingdoms.api.shared.ui.aui

import net.kyori.adventure.text.Component
import net.minestom.server.event.GlobalEventHandler
import net.skykingdoms.api.shared.event.EventAction
import net.skykingdoms.api.shared.event.EventSubbed
import net.skykingdoms.api.shared.ui.UserInterface
import net.skykingdoms.api.shared.user.ServerUser
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

open class ActionUi(content: Component) : UserInterface {
    private val players = HashSet<ServerUser>()

    init {
        if (this is EventSubbed) {
            for (event in this.subscribedTo()) {
                EventSubbed.subscribe(event) {
                    for (player in players) {
                        load(player)
                    }
                }
            }
        }
    }

    fun load(loader: ServerUser) {

    }

    override fun draw() {

    }
}