package passivhaus.envelope.validators

import passivhaus.envelope.materials.PassivhausCertifiedMaterial
import passivhaus.envelope.materials.PassivhausCertifiedMaterial.*

import passivhaus.envelope.assembly.AssemblyType
import passivhaus.envelope.assembly.AssemblyType.*

import passivhaus.envelope.assembly.constraints.moisture.MoistureRisk
import passivhaus.envelope.assembly.constraints.moisture.MoistureRisk.*

import passivhaus.envelope.assembly.constraints.moisture.MaterialMoistureTolerance
import passivhaus.envelope.assembly.constraints.moisture.AssemblyMoistureRisk
import passivhaus.envelope.assembly.constraints.moisture.MoistureCompatible

import passivhaus.envelope.layers.Layer


private[envelope] object AssemblyValidator:

    /*
        AssemblyMemberValidator[M, A] is a custom evidence type.

        A value of this type is a proof that the combination of a group member, M, and an
        AssemblyType, A, has passed all type‑level checks (for now we have only the moisture 
        compatibility check, other checks could be added later on as necessary for the domain logic).

        The key point: we do not enumerate all (M, A) pairs manually. Instead, we compute whether
        a given combination (M, A) is valid using the match types for moisture risk.
    */
    private[validators] sealed trait AssemblyMemberValidator[M, A <: AssemblyType]

    private[validators] object AssemblyMemberValidator:
        /*
            This given is only available when MoistureCompatible[M, A] is true.

            The constraint is enforced by requiring evidence that
            MoistureCompatible[M, A] =:= true.

            If the type‑level computation MoistureCompatible[M, A] yields false,
            the compiler cannot construct this given, and therefore no
            AssemblyMemberValidator[M, A] exists.
        */
        given validateMaterialAssembly[M <: PassivhausCertifiedMaterial, A <: AssemblyType](using
            moistureCompatibility: MoistureCompatible[M, A] =:= true
        ): AssemblyMemberValidator[M, A] =
            new AssemblyMemberValidator[M, A] {}

    /*
        This is a recursive typeclass that walks the tuple of layers and requires
        AssemblyMemberValidator[M, A] for each material M of each layer.
        Later on, this could also be extended (as it's necessary for the domain logic) to include givens
        for different groups, not only layers of materials (for example, it can cover a Tuple of
        technologies specific to a particular AssemblyType).
    */
    sealed trait AssemblyValidatorOf[Ts <: Tuple, A <: AssemblyType]

    object AssemblyValidatorOf:

        given emptyAssembly[A <: AssemblyType]: AssemblyValidatorOf[EmptyTuple, A] =
            new AssemblyValidatorOf[EmptyTuple, A] {}

        given validateLayersAssembly[
            M <: PassivhausCertifiedMaterial,
            Tail <: Tuple,
            A <: AssemblyType
        ](using
            AssemblyMemberValidator[M, A],
            AssemblyValidatorOf[Tail, A]
        ): AssemblyValidatorOf[Layer[M] *: Tail, A] =
            new AssemblyValidatorOf[Layer[M] *: Tail, A] {}
            