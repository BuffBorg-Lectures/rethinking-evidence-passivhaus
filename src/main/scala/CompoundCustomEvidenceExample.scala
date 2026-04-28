/*
    This is a short display of compound custom evidence in action.
    
    Here are presented a few cases of compilation success and failure due to this evidence.
*/ 


package passivhaus.examples

import passivhaus.envelope.layers.Layer
import passivhaus.envelope.layers.LayerData

import passivhaus.envelope.materials.PassivhausCertifiedMaterial
import passivhaus.envelope.materials.PassivhausCertifiedMaterial.*

import passivhaus.envelope.units.*

import passivhaus.envelope.assembly.AssemblyType
import passivhaus.envelope.assembly.AssemblyType.*

import passivhaus.envelope.assembly.layers.LayersOfMaterialsAssembly


// realistic layers

val wool: Layer[MineralWool.type] =
    Layer.WoolLayer(
        LayerData(
            material = MineralWool,
            thickness = Meter(0.20),
            lambda = WPerMeterKelvin(0.035)
        )
    )

val fiber: Layer[WoodFiber.type] =
    Layer.FiberLayer(
        LayerData(
            material = WoodFiber,
            thickness = Meter(0.06),
            lambda = WPerMeterKelvin(0.040)
        )
    )

val cellulose: Layer[Cellulose.type] =
    Layer.CelluloseLayer(
        LayerData(
            material = Cellulose,
            thickness = Meter(0.10),
            lambda = WPerMeterKelvin(0.038)
        )
    )


// 1. valid single‑layer assemblies (should compile)

val valid1 =
    LayersOfMaterialsAssembly.make[InteriorWall.type](wool *: EmptyTuple)

val valid2 =
    LayersOfMaterialsAssembly.make[InteriorWall.type](fiber *: EmptyTuple)

val valid3 =
    LayersOfMaterialsAssembly.make[ExteriorWall.type](wool *: EmptyTuple)

val valid4 =
    LayersOfMaterialsAssembly.make[BelowGradeWall.type](wool *: EmptyTuple)


// 2. invalid single‑layer assemblies (if you uncomment them, the compiler should complain)

/*
val invalid1 =
    LayersOfMaterialsAssembly.make[InteriorWall.type](cellulose *: EmptyTuple)

val invalid2 = 
    LayersOfMaterialsAssembly.make[ExteriorWall.type](fiber *: EmptyTuple)

val invalid3 = 
    LayersOfMaterialsAssembly.make[ExteriorWall.type](cellulose *: EmptyTuple)

val invalid4 =
    LayersOfMaterialsAssembly.make[BelowGradeWall.type](fiber *: EmptyTuple)

val invalid5 =
    LayersOfMaterialsAssembly.make[BelowGradeWall.type](cellulose *: EmptyTuple)
*/


// 3. valid multi‑layer assembly (should compile)

val interiorLayers =
  (wool, fiber)

val interiorAssembly =
  LayersOfMaterialsAssembly.make[InteriorWall.type](interiorLayers)


// 4. invalid multi‑layer assembly (if you uncomment it, the compiler should complain)

/*
val exteriorLayers =
  (wool, fiber, cellulose)

val exteriorAssembly =
    LayersOfMaterialsAssembly.make[ExteriorWall.type](exteriorLayers)
*/
