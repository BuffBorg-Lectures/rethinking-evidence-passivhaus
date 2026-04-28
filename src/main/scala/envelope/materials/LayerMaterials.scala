package passivhaus.envelope.materials

import passivhaus.envelope.units.LayerThickness.*

trait Material

enum PassivhausCertifiedMaterial extends Material:
    case WoodFiber
    case MineralWool
    case Cellulose


final case class Insulation[A <: PassivhausCertifiedMaterial](material: A, thicknessM: Meter)
