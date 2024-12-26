Gutted and Re-wrote All of KubeVS from scratch :)

## Changes
- Replaced `vs.info.init` with `vs.blockstate.info` event and remade it as a Startup Event (they are different I swear)
- Removed `vs.ship.game_tick`
- Renamed `vs.ship.phys_tick` to `vs.ship.phys`
- Added `ShipAssemblyKt` to bindings