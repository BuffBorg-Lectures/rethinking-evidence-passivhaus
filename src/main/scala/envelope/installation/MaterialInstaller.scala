package passivhaus.envelope.installation

import passivhaus.envelope.materials.PassivhausCertifiedMaterial

/* 
    Each constructor fixes the type parameter M to the only material that
    supports that installation method. This fixed type parameter is a compile‑time proof.

    Because the constructor itself carries this proof, the compiler can reject
    any attempt to use an installation method with a material whose singleton
    type does not match the constructor’s fixed M.

    The GADT therefore restricts the space of legal installation methods to the
    known cases, and each case encodes its own validity conditions.
*/
enum InstallMethod[M <: PassivhausCertifiedMaterial]:
    case Stapled   extends InstallMethod[PassivhausCertifiedMaterial.MineralWool.type]
    case BlownIn   extends InstallMethod[PassivhausCertifiedMaterial.Cellulose.type]
    case PressFit  extends InstallMethod[PassivhausCertifiedMaterial.WoodFiber.type]

object MaterialInstaller:
    /* 
        The install method requires that the material and the method share the same
        type parameter M. Because each GADT constructor fixes M to a specific
        material, only valid combinations can satisfy this requirement.

        For example:
        install(material = Cellulose, withMethod = Stapled)   // rejected at compile time
        install(material = MineralWool, withMethod = Stapled) // accepted

        No runtime checks are needed. The GADT’s type parameter is the evidence,
        and the method signature forces the compiler to respect it.

        Illegal states are not representable.
    */
    def install[M <: PassivhausCertifiedMaterial](
        material: M,
        withMethod: InstallMethod[M]
    ): Unit =
        println(s"Installing ${material} using $withMethod")
