package net.skykingdoms.api.shared.dummy.action.impl

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue

interface CommonDummyAction {
    fun replacement(args: ArgumentQueue): Component
}