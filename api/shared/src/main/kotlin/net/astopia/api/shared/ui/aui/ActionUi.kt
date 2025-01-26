package net.astopia.api.shared.ui.aui

import net.kyori.adventure.text.Component
import net.minestom.server.event.GlobalEventHandler
import net.astopia.api.shared.dummy.Dummy.Companion.DMM
import net.astopia.api.shared.event.EventAction
import net.astopia.api.shared.event.EventSubbed
import net.astopia.api.shared.ui.UserInterface
import net.astopia.api.shared.user.ServerUser
import net.astopia.backend.minestom.HeartBeat
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
                    displayHighestWeightedContent(user, uis)
                }
            }, 1, 20)
        }

        private fun displayHighestWeightedContent(user: ServerUser, uis: LinkedList<ActionUi>) {
            if (uis.isEmpty()) return

            val maxWeight = uis.maxOf { it.weight }
            val highestWeightUis = uis.filter { it.weight == maxWeight }

            if (highestWeightUis.size == 1) {
                highestWeightUis.first().load(user)
                return
            }

            val combinedContent = highestWeightUis.joinToString(" ") { it.content }
            val combinedUI = ActionUi(combinedContent, maxWeight)
            combinedUI.load(user)
        }

        fun display(target: ServerUser, ui: ActionUi) {
            val list: LinkedList<ActionUi> = players.getOrDefault(target, LinkedList())

            if (list.contains(ui)) {
                return
            }

            list.add(ui)

            displayHighestWeightedContent(target, list)
            players.put(target, list)
        }

        fun remove(target: ServerUser, ui: ActionUi) {
            val list = players.get(target) ?: return
            list.remove(ui)

            if (list.isEmpty()) {
                players.remove(target)
                return
            }

            displayHighestWeightedContent(target, list)
            players.put(target, list)
        }
    }

    init {
        if (this is EventSubbed) {
            for (event in this.subscribedTo()) {
                EventSubbed.subscribe(event) {
                    for (player in cachedContent.keys()) {
                        cachedContent.put(player, DMM.invoke(content, player))

                        val playerUis = players.get(player) ?: continue
                        displayHighestWeightedContent(player, playerUis)
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