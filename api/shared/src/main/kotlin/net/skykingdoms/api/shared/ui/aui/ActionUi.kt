package net.skykingdoms.api.shared.ui.aui

import net.kyori.adventure.text.Component
import net.minestom.server.event.GlobalEventHandler
import net.skykingdoms.api.shared.dummy.Dummy.Companion.DMM
import net.skykingdoms.api.shared.event.EventAction
import net.skykingdoms.api.shared.event.EventSubbed
import net.skykingdoms.api.shared.ui.UserInterface
import net.skykingdoms.api.shared.user.ServerUser
import net.skykingdoms.backend.minestom.HeartBeat
import java.util.LinkedList
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

open class ActionUi(val content: String, val weight: Int) : UserInterface {
    private val cachedContent: ConcurrentHashMap<ServerUser, Component> = ConcurrentHashMap()

    companion object {
        private val players: ConcurrentHashMap<ServerUser, LinkedList<ActionUi>> = ConcurrentHashMap()

        init {
            HeartBeat.runTimerAsync({
                for ((user, uis) in players) {
                    for (ui in uis) {
                        ui.load(user)
                    }
                }
            }, 0, 20)
        }

        fun display(target: ServerUser, ui: ActionUi) {
            val list: LinkedList<ActionUi> = players.getOrDefault(target, LinkedList())

            if (list.contains(ui)) {
                return
            }

            list.add(ui)
            ui.load(target)
            players.put(target, list)
        }
    }

    init {
        if (this is EventSubbed) {
            for (event in this.subscribedTo()) {
                EventSubbed.subscribe(event) {
                    for (player in cachedContent.keys()) {
                        cachedContent.put(player, DMM.invoke(content, player))
                        load(player)
                    }
                }
            }
        }
    }

    fun load(loader: ServerUser) {
        if (!cachedContent.contains(loader)) {
            cachedContent.put(loader, DMM.invoke(content, loader))
        }

        loader.sendActionBar(cachedContent.get(loader)!!)
    }

    override fun draw() {

    }
}