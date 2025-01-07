package net.skykingdoms.api.shared.dummy.impl

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.skykingdoms.api.shared.dummy.Dummy
import net.skykingdoms.api.shared.dummy.action.impl.PlayerDummyAction
import net.skykingdoms.api.shared.user.ServerUser

class PlayerDummy : Dummy("player"), PlayerDummyAction {
    override fun playerReplacement(
        loader: ServerUser,
        args: ArgumentQueue
    ): Component {
        return when (args.popOr("player tag requires an argument").value()) {
            "name" -> loader.name
            else -> Component.text("Unknown player argument")
        }
    }
}