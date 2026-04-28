package passivhaus.envelope.thermal.specs

import passivhaus.envelope.units.LayerDensity.*
import passivhaus.envelope.units.WindowGValue.*
import passivhaus.envelope.units.WindowUValue.*
import passivhaus.envelope.units.LayerThickness.*

import passivhaus.envelope.materials.Material

enum InsulationSpec:
    case ExteriorFacade(
        material: Material,
        thickness: Meter,
        density: KgPerCubicMeter
    )

    case Window(
        uValue: WattsPerSquareMeterKelvin,
        gValue: Ratio
    )
