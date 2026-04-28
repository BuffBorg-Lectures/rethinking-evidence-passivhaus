package passivhaus.envelope.materials

import passivhaus.envelope.materials.PassivhausCertifiedMaterial.*

object MaterialCorrections:
        
    extension (m: MineralWool.type)
        def compressionFactor: Double = 0.97   // wool compresses slightly

    extension (m: WoodFiber.type)
        def moistureExpansionFactor: Double = 1.02   // wood fiber swells a bit

    extension (m: Cellulose.type)
        def settlingFactor: Double = 0.95      // cellulose settles over time
