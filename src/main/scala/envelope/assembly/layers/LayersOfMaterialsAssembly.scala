package passivhaus.envelope.assembly.layers

import passivhaus.envelope.materials.PassivhausCertifiedMaterial

import passivhaus.envelope.layers.Layer
import passivhaus.envelope.layers.computeAdjustedRValue

import passivhaus.envelope.units.ThermalResistance.*

import passivhaus.envelope.assembly.AssemblyType

import passivhaus.envelope.validators.AssemblyValidator.AssemblyValidatorOf

/*
    This is a recursive typeclass that walks a tuple of layers and ensures that each
    element is a Layer[M] where M <: PassivhausCertifiedMaterial.
*/
trait AllMaterials[Ts <: Tuple]

object AllMaterials:

    given emptyMaterialLayersTuple: AllMaterials[EmptyTuple] =
        new AllMaterials[EmptyTuple] {}

    given validateLayersOfPCM[
        M <: PassivhausCertifiedMaterial,
        Tail <: Tuple
    ](using
        AllMaterials[Tail]
    ): AllMaterials[Layer[M] *: Tail] =
        new AllMaterials[Layer[M] *: Tail] {}



/*
    This LayersOfMaterials type lambda maps a tuple of material types Ms into a tuple of
    Layer[M & PassivhausCertifiedMaterial] types (a type intersection which,
    in this case, in practice resumes to Layer[M] types).
    
    It is used internally to store the layers inside the LayersOfMaterialsAssembly case class.
*/
private[assembly] type LayersOfMaterials[Ms <: Tuple] =
    Tuple.Map[Ms, [M] =>> Layer[M & PassivhausCertifiedMaterial]]


final case class LayersOfMaterialsAssembly[Ms <: Tuple] private (
    layers: LayersOfMaterials[Ms]
):

    /*
        As you already saw with the Layer class, here too, in LayersOfMaterialsAssembly,
        we define a method of calculating the adjusted R value, only that this time
        it's the collective R values
    */
    def totalAdjustedRValue: RSquareMeterKelvinPerWatt =
        layers
            .toList
            .asInstanceOf[List[Layer[PassivhausCertifiedMaterial]]]
            .map(computeAdjustedRValue)
            .reduce(_ + _)



object LayersOfMaterialsAssembly:

    /* 
        Mainly used for type param inference aid.
    */
    def make[A <: AssemblyType]: AssemblyFactory[A] =
        new AssemblyFactory[A]

    /*
        The apply method is enabling the multi-factor evidence. Mainly two 
        type-level computations.

        Both of wich are recursive typeclasses that walk the tuple of layers
        and ensure that all required evidence exists.
    */
    class AssemblyFactory[A <: AssemblyType]:

        def apply[Ts <: Tuple](layers: Ts)(using
            AllMaterials[Ts],
            AssemblyValidatorOf[Ts, A]
        ): LayersOfMaterialsAssembly[Ts] =
            LayersOfMaterialsAssembly(layers.asInstanceOf[LayersOfMaterials[Ts]])
