package org.valkyrienskies.eureka.ship

import com.fasterxml.jackson.annotation.JsonIgnore
import org.joml.Math.clamp
import org.joml.Math.cos
import org.joml.Vector3d
import org.valkyrienskies.core.api.ForcesApplier
import org.valkyrienskies.core.api.ServerShip
import org.valkyrienskies.core.api.ServerShipUser
import org.valkyrienskies.core.api.ShipForcesInducer
import org.valkyrienskies.core.api.Ticked
import org.valkyrienskies.core.api.shipValue
import org.valkyrienskies.core.game.ships.PhysShip
import org.valkyrienskies.core.pipelines.SegmentUtils
import org.valkyrienskies.eureka.EurekaConfig
import org.valkyrienskies.mod.api.SeatedControllingPlayer
import org.valkyrienskies.mod.common.util.toJOMLD
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val MAX_RISE_VEL = 3.0
private val BALLOON_PER_MASS = 1 / EurekaConfig.SERVER.massPerBalloon
private val NEUTRAL_FLOAT = EurekaConfig.SERVER.neutralLimit
private val NEUTRAL_LIMIT get() = NEUTRAL_FLOAT - 10

class EurekaShipControl : ShipForcesInducer, ServerShipUser, Ticked {

    @JsonIgnore
    override var ship: ServerShip? = null
    val controllingPlayer by shipValue<SeatedControllingPlayer>()

    private var extraForce = 0.0
    private var alleviationTarget = Double.NaN
    private var aligning = 0 // tries to align the ship in this amount of physticks
    private var cruiseSpeed = Double.NaN
    private val anchored get() = anchorsActive > 0
    private var wasAnchored = false
    private val alleviationPower get() = balloons + 1.0

    override fun applyForces(forcesApplier: ForcesApplier, physShip: PhysShip) {
        val mass = physShip.inertia.shipMass
        val moiTensor = physShip.inertia.momentOfInertiaTensor
        val segment = physShip.segments.segments[0]?.segmentDisplacement!!
        val omega = SegmentUtils.getOmega(physShip.poseVel, segment, Vector3d())
        val vel = SegmentUtils.getVelocity(physShip.poseVel, segment, Vector3d())
        val pos = physShip.poseVel.pos

        // Revisiting eureka control code.
        // [x] Move torque stabilization code
        // [x] Move linear stabilization code
        // [x] Revisit player controlled torque
        // [x] Revisit player controlled linear force
        // [x] Anchor freezing
        // [ ] Rewrite Alignment code
        // [ ] Revisit Alleviation code
        // [ ] Balloon limiter
        // [ ] Add Cruise code

        // region Aligning
        /*
        if (aligning > 0) {



            val linearDiff = Vector3d(physShip.position)
                .sub(
                    Vector3d(physShip.position)
                        .add(0.5, 0.5, 0.5)
                        .floor()
                        .add(0.5, 0.5, 0.0)
                )

            // Were gonna use a direction as sole input, cus this would work well with dissasembly
            val alignFront = alignTo.normal.toJOMLD()
            val angleAlign = shipFront.angle(alignFront)
            if (angleAlign < 0.01 && linearDiff.lengthSquared() < 0.1)
                aligning--
            else {
                // Torque


                // Linear
                linearStabilize = false

                val idealVelocity = linearDiff
                idealVelocity.sub(physShip.velocity)
                idealVelocity.mul(mass * 10)

                forcesApplier.applyInvariantForce(idealVelocity)
            }
        }
        */

        // endregion

        if (!anchored) {
            if (aligning > 0)
                if (alignShip(physShip, forcesApplier, ship!!))
                    aligning--

            stabilize(
                physShip,
                omega,
                vel,
                segment,
                forcesApplier,
                controllingPlayer == null && aligning == 0,
                controllingPlayer == null
            )

            controllingPlayer?.let { player ->
                // region Player controlled rotation
                val rotationVector = Vector3d(
                    0.0,
                    if (player.leftImpulse != 0.0f)
                        (player.leftImpulse.toDouble() * EurekaConfig.SERVER.turnSpeed)
                    else
                        -omega.y() * EurekaConfig.SERVER.turnSpeed,
                    0.0
                )

                rotationVector.sub(0.0, omega.y(), 0.0)

                SegmentUtils.transformDirectionWithScale(
                    physShip.poseVel,
                    segment,
                    moiTensor.transform(
                        SegmentUtils.invTransformDirectionWithScale(
                            physShip.poseVel,
                            segment,
                            rotationVector,
                            rotationVector
                        )
                    ),
                    rotationVector
                )

                forcesApplier.applyInvariantTorque(rotationVector)
                // endregion

                // region Player controlled forward and backward thrust
                val forwardVector = player.seatInDirection.normal.toJOMLD()
                SegmentUtils.transformDirectionWithoutScale(
                    physShip.poseVel,
                    segment,
                    forwardVector,
                    forwardVector
                )
                forwardVector.mul(player.forwardImpulse.toDouble())
                val idealForwardVel = Vector3d(forwardVector)
                idealForwardVel.mul(EurekaConfig.SERVER.baseSpeed)
                val forwardVelInc = idealForwardVel.sub(vel.x(), 0.0, vel.z())
                forwardVelInc.mul(mass * 10)
                forwardVelInc.add(forwardVector.mul(extraForce))

                forcesApplier.applyInvariantForce(forwardVelInc)
                // endregion

                // Player controlled alleviation
                if (player.upImpulse != 0.0f)
                    alleviationTarget =
                        pos.y() + (player.upImpulse * EurekaConfig.SERVER.impulseAlleviationRate * max(
                            alleviationPower * 0.2,
                            1.5
                        ))
            }

            // region Alleviation
            if (alleviationTarget.isFinite()) {
                val massPenalty =
                    min((alleviationPower / (mass * BALLOON_PER_MASS)) - 1.0, alleviationPower) * NEUTRAL_FLOAT
                val limit = (NEUTRAL_LIMIT + massPenalty)
                var stable = true

                val alleviationPower = if (pos.y() > limit) {
                    if ((pos.y() - limit) > 20.0) {
                        stable = false
                        0.0
                    } else {
                        val mod = 1 + cos(((pos.y() - limit) / 10.0) * (Math.PI / 2) + (Math.PI / 2))
                        if ((pos.y() - limit) > 10.0) {
                            stable = false
                            (1 - mod) * 0.1 + 0.1
                        } else alleviationPower * mod
                    }
                } else alleviationPower

                val diff = (alleviationTarget - pos.y())
                    .let { if (abs(it) < 0.05) 0.0 else it }

                val penalisedVel = if (alleviationPower < 0.1) 0.0 else
                    (MAX_RISE_VEL * alleviationPower)

                val shipRiseVelo = vel.y()
                val idealRiseVelo = clamp(-MAX_RISE_VEL, penalisedVel, diff)
                val impulse = idealRiseVelo - shipRiseVelo

                if (idealRiseVelo > 0.1 || stable)
                    forcesApplier.applyInvariantForce(Vector3d(0.0, (impulse + if (stable) 1 else 0) * mass * 10, 0.0))
            }
            // endregion
        } else if (wasAnchored != anchored) {
            forcesApplier.setStatic(anchored)
        }

        // Drag
        // forcesApplier.applyInvariantForce(Vector3d(vel.y()).mul(-mass))
    }

    var power = 0.0

    override fun tick() {
        extraForce = power
        power = 0.0
    }

    fun align() {
        if (aligning == 0)
            aligning += 60
    }

    var anchors = 0 // Amount of anchors
    var anchorsActive = 0 // Anchors that are active
    var balloons = 0 // Amount of balloons
    var floaters = 0 // Amount of floaters
}
