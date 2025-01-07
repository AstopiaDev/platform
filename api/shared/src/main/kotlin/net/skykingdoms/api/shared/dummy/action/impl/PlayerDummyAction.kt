package net.skykingdoms.api.shared.dummy.action.impl

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.skykingdoms.api.shared.dummy.action.DummyAction
import net.skykingdoms.api.shared.user.ServerUser

interface PlayerDummyAction : DummyAction {
    fun playerReplacement(loader: ServerUser, args: ArgumentQueue): Component
}