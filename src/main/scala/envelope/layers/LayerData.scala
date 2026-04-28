package passivhaus.envelope.layers

import passivhaus.envelope.materials.PassivhausCertifiedMaterial
import passivhaus.envelope.materials.PassivhausCertifiedMaterial.*
import passivhaus.envelope.materials.MaterialCorrections.*

import passivhaus.envelope.units.LayerThickness.*
import passivhaus.envelope.units.ThermalConductivity.*
import passivhaus.envelope.units.ThermalResistance.*

/*
    LayerData carries the physical parameters needed for thermal calculations.

    It is generic in M so that the material field preserves the refined type.
    This is essential: when a Layer[M] is pattern‑matched, the compiler refines M
    to a specific singleton type (e.g., MineralWool.type). Because LayerData
    stores the material as M, the refined type flows into data.material as well.

    This allows material‑specific operations (compressionFactor, settlingFactor,
    etc.) to become available only in the branches where the compiler knows the
    exact material type.

    λ (lambda) is the thermal conductivity of the material.
    R‑value = thickness / lambda --- standard building‑physics practice.
*/
final case class LayerData[M <: PassivhausCertifiedMaterial](
    material: M,
    thickness: Meter,
    lambda: WPerMeterKelvin
)

/*
    Layer is a GADT whose constructors refine the material type.

    Each constructor fixes the type parameter M to the singleton type of the
    certified material it represents:

        WoolLayer      : Layer[MineralWool.type]
        FiberLayer     : Layer[WoodFiber.type]
        CelluloseLayer : Layer[Cellulose.type]

    Because LayerData is also parameterized by M, the refined type flows into
    the data it carries. Inside each match branch, the compiler knows the exact
    material type, enabling material‑specific operations that would otherwise be
    illegal on the general PassivhausCertifiedMaterial supertype.

    This is compile‑time refinement: no runtime checks, no casting.
    The GADT constructors themselves are the refinement evidence.
*/
enum Layer[M <: PassivhausCertifiedMaterial]:
    case WoolLayer(data: LayerData[MineralWool.type])
        extends Layer[MineralWool.type]

    case FiberLayer(data: LayerData[WoodFiber.type])
       extends Layer[WoodFiber.type]

    case CelluloseLayer(data: LayerData[Cellulose.type])
       extends Layer[Cellulose.type]

/*
    computeAdjustedRValue demonstrates refinement‑driven specialization.

    Pattern matching on Layer[M] refines M to a specific singleton type.
    Because LayerData stores its material as M, the refined type flows into
    data.material as well.

    This enables calling material‑specific correction factors:

        • MineralWool.type    → compressionFactor
        • WoodFiber.type      → moistureExpansionFactor
        • Cellulose.type      → settlingFactor

    These extension methods are not available on the general
    PassivhausCertifiedMaterial type. They only become legal inside the
    corresponding match branch because the GADT has refined M.

    This is the essence of GADT refinement: new operations become available
    precisely because the compiler now knows the exact material type.
*/
def computeAdjustedRValue[M <: PassivhausCertifiedMaterial](
    layer: Layer[M]
): RSquareMeterKelvinPerWatt =
    val (layerData, correctionFactor) = layer match
        // M is refined to MineralWool.type
        case Layer.WoolLayer(data) => (data, data.material.compressionFactor)

        // M is refined to WoodFiber.type
        case Layer.FiberLayer(data) => (data, data.material.moistureExpansionFactor)
            
        // M is refined to Cellulose.type
        case Layer.CelluloseLayer(data) => (data, data.material.settlingFactor)

    val adjustedThickness = layerData.thickness * correctionFactor

    RSquareMeterKelvinPerWatt.from(adjustedThickness, layerData.lambda)


/*
    computeRValue shows the simpler case where refinement is present but no
    material‑specific behavior is required.

    Even though all branches compute R‑value the same way, the match still
    refines M to a specific material type in each branch. This ensures:

        • the material is certified
        • the branch is exhaustive
        • no unknown material type can appear

    This function illustrates the structural safety provided by GADTs, even
    when no material‑specific logic is needed.
*/
def computeRValue[M <: PassivhausCertifiedMaterial](
    layer: Layer[M]
): RSquareMeterKelvinPerWatt =
    layer match
        case Layer.WoolLayer(data) =>
            RSquareMeterKelvinPerWatt.from(data.thickness, data.lambda)

        case Layer.FiberLayer(data) =>
            RSquareMeterKelvinPerWatt.from(data.thickness, data.lambda)

        case Layer.CelluloseLayer(data) =>
            RSquareMeterKelvinPerWatt.from(data.thickness, data.lambda)
