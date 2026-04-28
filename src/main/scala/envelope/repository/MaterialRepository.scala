package passivhaus.envelope.repository

import scala.reflect.ClassTag

import passivhaus.envelope.materials.*

final case class MaterialRepository(materials: Vector[Material]):
    def add(material: Material): MaterialRepository =
        copy(materials = materials :+ material)

    /*
        Only allows retrieving materials of a particular certified type.
    */
    def certifiedOnly[A <: PassivhausCertifiedMaterial](
        using ClassTag[A]
    ): Vector[A] =
        /*
            The ClassTag acts as an enabler: it gives the compiler enough runtime type
            information to perform a meaningful type test even after JVM erasure. The
            compiler rewrites the pattern match to use this retained runtime class, so
            the match is both valid and safe.

            Developers coding this kind of evidence are aware that inside the case body the 
            value is guaranteed to be of type T <: A, so they can write the case body knowingly
        */
        materials.collect {
            case material: A => material
        }