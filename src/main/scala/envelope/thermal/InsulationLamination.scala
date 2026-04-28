package passivhaus.envelope.thermal

import passivhaus.envelope.materials.*

object InsulationLamination:

    def laminateLayers[
        A <: PassivhausCertifiedMaterial,
        B <: PassivhausCertifiedMaterial
    ] (
        topLayer: Insulation[A],
        bottomLayer: Insulation[B]
    )(using sameTypeEvidence: A =:= B): Insulation[A] =
        /*
            Safe to proceed: in call-sites, reaching this point means the evidence has not blocked the flow 
            and it validated the type constraint at compile-time, so A == B is guaranteed.
        */
        Insulation( 
            material = topLayer.material, 
            thicknessM = topLayer.thicknessM + bottomLayer.thicknessM
        )
        