![kubevs_logo](https://raw.githubusercontent.com/TechTastic/kubevs/refs/heads/1.18.x/main/forge/src/main/resources/logo.png)
# KubeVS
A KubeJS x Valkyrien Skies 2 integration addon!

Discord: https://discord.gg/CgApHqxZXX

---

### Bindings
This addon was half-forced to add quite a few new bindings for VS-related classes.

##### Common Bindings
- Ship
- LoadedShip
- LoadedShipCore
- Vector3i
- Vector3ic
- Vector3d
- Vector3dc
- Vector3f
- Vector3fc
- AABBi
- AABBic
- AABBd
- AABBdc
- AABBf
- AABBfc
- Matrix2d
- Matrix2dc
- Matrix2f
- Matrix2dc
- Matrix3d
- Matrix3dc
- Matrix3f
- Matrix3dc
- DenseBlockPosSet
- BlockType
- VSGameUtilsKt
- VectorConversionsKt
- VectorConversionsMCKt
- ShipAssemblyKt

##### Server Bindings
- ServerShip
- LoadedServerShip
- ServerShipCore
- ServerShipWorld
- ServerShipWorldCore
- BlockStateInfo
  - Only for `get` which returns the mass and BlockType of a given BlockState as a pair.

##### Client Bindings
- ClientShip
- ClientShipCore
- ClientShipWorld
- ClientShipWorldCore

---

### Attachments
This addon also adds new fields to Server, Level, and Player.

##### Server Attachments
- serverShipObjectWorld
- vsPipeline

##### Level Attachments
- shipObjectWorld
- allShips
- dimensionId
- shipWorldNullable

##### ServerLevel Attachments
- serverShipObjectWorld

##### ClientLevel Attachments
- clientShipObjectWorld

##### Player Attachments
- dimensionId
- draggingInformation

---

### Startup Events
#### vs.blockstate.info
This event is fired upon startup and cannot be cancelled.
It is used to register callbacks to be called upon resolving mass and BlockTypes for BlockStates.

This code sets all BlockStates to provide 0kg of mass and have the BlockType (and thus collision) of Air.
```javascript
onEvent('vs.blockstate.info', event => {
    event.mass(state => {
        return 0
    })
    
    event.type(state => {
        return BlockType.AIR
    })
})
```

---

### Server Events
#### vs.ship.load
This event is fired upon a Ship being loaded and cannot be cancelled.

This code prints out the Slug of a Ship as the Ship is loaded.
```javascript
onEvent('vs.ship.load', event => {
    var ship = event.getShipObjectServer()
    console.log("Ship Name: " + ship.getSlug())
})
```

#### vs.ship.phys
This event is fired upon the physics tick of a Ship and cannot be cancelled.

This code flings the Ship up into the air along the world's Y axis by a force of 100x the Ship's mass in Newtons.
```javascript
onEvent('vs.ship.phys', event => {
    event.physShip.applyInvariantForce(Vector3d(0.0, 100.0 * event.physShip.inertia.shipMass, 0.0))
})
```

### Client Events
#### vs.ship.load
This event is fired upon a Ship being loaded and cannot be cancelled.

This code prints out the Slug of a Ship as the Ship is loaded.
```javascript
onEvent('vs.ship.load', event => {
    var ship = event.getShipObjectClient()
    console.log("Ship Name: " + ship.getSlug())
})
```

#### vs.ship.render
This event is fired after a Ship is rendered and cannot be cancelled.

```javascript
onEvent('vs.ship.load', event => {
    var ship = event.getClientShip()
    
    // TBH, theres not much I can think of putting here.
})
```