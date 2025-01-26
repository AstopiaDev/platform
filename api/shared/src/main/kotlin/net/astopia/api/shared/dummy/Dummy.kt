package net.astopia.api.shared.dummy

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.astopia.api.backend.adventure.Adventure.MM
import net.astopia.api.shared.dummy.action.impl.CommonDummyAction
import net.astopia.api.shared.dummy.action.impl.PlayerDummyAction
import net.astopia.api.shared.dummy.impl.PlayerDummy
import net.astopia.api.shared.user.ServerUser

open class Dummy(val id: String) {
    companion object {
        private val resolvers: HashSet<TagResolver> = hashSetOf()
        private var resolversCache: Array<TagResolver>? = null

        fun add(dummy: Dummy) {
            val resolver = TagResolver.resolver(dummy.id) { args, context ->
                Tag.selfClosingInserting(when (dummy) {
                    is PlayerDummyAction -> {
                        val player = context.targetAsType(ServerUser::class.java)
                        dummy.playerReplacement(player, args)
                    }

                    is CommonDummyAction -> {
                        (dummy as CommonDummyAction).replacement(args)
                    }

                    else -> Component.text("Invalid dummy action type")
                })
            }

            resolvers.add(resolver)
            resolversCache = resolvers.toTypedArray()
        }

        fun resolvers() : Array<TagResolver> {
            if (resolversCache == null) {
                resolversCache = resolvers.toTypedArray()
                return resolversCache!!
            }

            return resolversCache!!
        }

        fun init() {
            add(PlayerDummy())
        }

        val DMM: (String, ServerUser?) -> Component = { inputString, user ->
            if (user != null) {
                MM.deserialize(inputString, user, *resolvers())
            } else {
                MM.deserialize(inputString, *resolvers())
            }
        }
    }
}