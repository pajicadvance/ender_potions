# Ender Potions

This mod adds two new ender-themed potions, with effects inspired by Terraria potions.

## Potion of Wormhole

Drinking the potion in multiplayer opens up a screen where you can choose a player to teleport to from a list of online players. This sends a teleport request to the selected player. The receiving player can accept the request via a chat message to let you teleport to their location. If the request doesn't get accepted in time (60 seconds by default), the request is declined and the potion gets refunded.

The potion does nothing in singleplayer or when there is only one player on the server.

Can be brewed in the brewing stand with awkward potions and an ender eye.

## Potion of Teleportation

Drinking the potion teleports the player to a random location in a configurable radius. The default radius is 1000 blocks around the player. The randomly selected location will always be safe for the player to stand on.

Can be brewed in the brewing stand with awkward potions and an ender pearl.

### Recipe viewer support

Brewing recipes for both potions will show up on all versions in JEI, RRV, REI, and EMI.

### Configuration

Change in the "World Options" menu, at world creation, or using the `/gamerule` command:

- `ender_potions:teleport_radius`/`enderPotionsTeleportRadius`: Maximum distance in blocks (on the X and Z axes) that the Potion of Teleportation can move the player
- `ender_potions:teleport_max_height`/`enderPotionsTeleportMaxHeight`: Highest Y level the Potion of Teleportation can place the player at. The dimension's own height limit still applies
- `ender_potions:teleport_underground_chance`/`enderPotionsTeleportUndergroundChance`: Chance in percent that the Potion of Teleportation picks a spot underground instead of on the surface, when the chosen column has one
- `ender_potions:wormhole_request_timeout`/`enderPotionsWormholeRequestTimeout`: Seconds a Potion of Wormhole teleport request waits to be accepted before it is declined and the potion is refunded
