package passivhaus.envelope.thermal.runtime

import passivhaus.envelope.materials.*

/*
    This ADT preserves type info. Although necessary given the situation, it adds complexity and boilerplate code.
*/
enum LaminatedLayer:
    case Success[A <: PassivhausCertifiedMaterial](layer: Insulation[A])
    case MaterialMismatch(
        top: PassivhausCertifiedMaterial,
        bottom: PassivhausCertifiedMaterial
    )

object InsulationLamination:
    def laminateLayers(
        topLayer: Insulation[?],
        bottomLayer: Insulation[?]
    ): LaminatedLayer =
        /*
            Runtime will support validation this time. Depending on the complexity of the constraints this could be 
            resource expensive, but it could also be relatively cheap if the constraints are simple and straight-forward.
        */
        val sameMaterialType =
            topLayer.material.getClass == bottomLayer.material.getClass

        if (!sameMaterialType)
            /*
                In here the responsibility for failure is passed down to the user.

                This could involve further use of logging and error handling logic in the application, which would
                make use of even more resources.

                This is susceptible to human error so it can pass by being unhandled or handled inappropriately.
            */
            LaminatedLayer.MaterialMismatch(
                topLayer.material,
                bottomLayer.material
            )
        else
            /*
                Type precision is retained through the ADT.
            */
            LaminatedLayer.Success(
                Insulation(
                    material = topLayer.material.asInstanceOf[topLayer.material.type],
                    thicknessM = topLayer.thicknessM + bottomLayer.thicknessM
                )
            )
