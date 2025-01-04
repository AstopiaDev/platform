rootProject.name = "skykingdoms-platform"

// Backend
include("backend")
include("backend:backend-api")
findProject(":backend:backend-api")?.name = "backend-api"
include("backend:base")
findProject(":backend:base")?.name = "base"
include("backend:bukkit-connector")
findProject(":backend:bukkit-connector")?.name = "bukkit-connector"
include("backend:bukkit-startup-app")
findProject(":backend:bukkit-startup-app")?.name = "bukkit-startup-app"
include("backend:impl-loader")
findProject(":backend:impl-loader")?.name = "impl-loader"
include("backend:megamind")
findProject(":backend:megamind")?.name = "megamind"
include("backend:megamind-api")
findProject(":backend:megamind-api")?.name = "megamind-api"
include("backend:megamind-modules")
findProject(":backend:megamind-modules")?.name = "megamind-modules"

// Megamind Modules
include(":backend:megamind-modules:discord-module")
findProject(":backend:megamind-modules:discord-module")?.name = "discord-module"
include(":backend:megamind-modules:message-channel-module")
findProject(":backend:megamind-modules:message-channel-module")?.name = "message-channel-module"
include(":backend:megamind-modules:rest-api-module")
findProject(":backend:megamind-modules:rest-api-module")?.name = "rest-api-module"
include(":backend:megamind-modules:tcpshield-module")
findProject(":backend:megamind-modules:tcpshield-module")?.name = "tcpshield-module"
include(":backend:megamind-modules:votifier-module")
findProject(":backend:megamind-modules:votifier-module")?.name = "votifier-module"

include("backend:minestom-connector")
findProject(":backend:minestom-connector")?.name = "minestom-connector"
include("backend:node-app")
findProject(":backend:node-app")?.name = "node-app"
include("backend:protocol")
findProject(":backend:protocol")?.name = "protocol"
include("backend:protocol-api")
findProject(":backend:protocol-api")?.name = "protocol-api"
include("backend:velocity-connector")
findProject(":backend:velocity-connector")?.name = "velocity-connector"

// Api
include("api")
include("api:shared")
findProject(":api:shared")?.name = "shared"
include("api:lobby")
findProject(":api:lobby")?.name = "lobby"
include("api:game-engine")
findProject(":api:game-engine")?.name = "game-engine"
include("lobby")
include("lobby:main-lobby")
findProject(":lobby:main-lobby")?.name = "main-lobby"
