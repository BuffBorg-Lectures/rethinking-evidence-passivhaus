package passivhaus.envelope.validators

import passivhaus.envelope.materials.Material
import passivhaus.envelope.materials.PassivhausCertifiedMaterial

import passivhaus.envelope.thermal.specs.InsulationSpec
import passivhaus.envelope.thermal.specs.InsulationSpec.*

import scala.deriving.Mirror
import scala.compiletime.{erasedValue, summonInline}
import scala.reflect.ClassTag

import passivhaus.envelope.units.LayerThickness.*
import passivhaus.envelope.units.LayerDensity.*
import passivhaus.envelope.units.WindowGValue.*
import passivhaus.envelope.units.WindowUValue.*

object PassivhausSpecValidator:

    /*
        This example happens to have a second layer of evidence which are these field rules.
        As you probably figured it out by now this is a blocking type evidence.
        The main evidence, which is the catalyst for this second layer, is still the Mirror.
    */
    private[envelope] trait PassivhausFieldRule[-T]:
        def validate(value: T): Boolean

    private[envelope] given PassivhausFieldRule[Meter] with
        def validate(c: Meter): Boolean =
            c > Meter(0.25)

    private[envelope] given PassivhausFieldRule[KgPerCubicMeter] with
        def validate(d: KgPerCubicMeter): Boolean =
            d > KgPerCubicMeter(15)

    private[envelope] given PassivhausFieldRule[WattsPerSquareMeterKelvin] with
        def validate(u: WattsPerSquareMeterKelvin): Boolean =
            u < WattsPerSquareMeterKelvin(0.85)

    private[envelope] given PassivhausFieldRule[Ratio] with
        def validate(g: Ratio): Boolean =
            g >= Ratio(0.5)

    private[envelope] given PassivhausFieldRule[Material] with
        def validate(m: Material): Boolean =
            m.isInstanceOf[PassivhausCertifiedMaterial]

    private[envelope] given PassivhausFieldRule[Any] with
        def validate(value: Any): Boolean = false


    def validateExteriorFacadeSpec(spec: ExteriorFacade): Boolean = validateInline(spec)

    def validateWindowSpec(spec: Window): Boolean = validateInline(spec)

    /*
        The Mirror.ProductOf[A] parameter is evidence that A is a product type 
        with compiler-known structure. If A has no mirror, this method cannot 
        even be called. This is the blocking role of Mirror. It prevents 
        misuse by rejecting types that do not have structural information.
    */
    private inline def validateInline[A <: InsulationSpec](spec: A)(using m: Mirror.ProductOf[A]): Boolean =

        /* 
            Once the mirror exists, the inline match below uses it as refinement
            evidence. 
                
            The inline match forces the compiler to reduce the match at 
            compile time. Because m is a Mirror.ProductOf[A], the compiler 
            knows exactly which case applies for the concrete A at the call 
            site. This collapses the generic A into a specific domain type.
            This is the refinement role of Mirror. It exposes the structure 
            of A so the compiler can route the logic statically.
        */
        inline m match
            case _: Mirror.ProductOf[ExteriorFacade] =>
                validateExteriorFacade(spec.asInstanceOf[ExteriorFacade])

            case _: Mirror.ProductOf[Window] =>
                validateWindowInsulation(spec.asInstanceOf[Window])

            case _ => validateGeneric(spec)

    /* 
        Specialized paths for known domain shapes. These are selected entirely at compile time via the mirror refinement.
    */
    private inline def validateExteriorFacade(spec: ExteriorFacade): Boolean =
        validateFields[
            (Material, Meter, KgPerCubicMeter)
        ](Tuple.fromProductTyped(spec))

    private inline def validateWindowInsulation(spec: Window): Boolean =
        validateFields[
            (WattsPerSquareMeterKelvin, Ratio)
        ](Tuple.fromProductTyped(spec))

    /*  
        Generic fallback, this one uses the mirror to extract the tuple of field types.
    */
    private inline def validateGeneric[A <: Product](spec: A)(using m: Mirror.ProductOf[A]): Boolean =
        validateFields[m.MirroredElemTypes](Tuple.fromProductTyped(spec))

    /*  
        The recursion is unrolled at compile-time thanks to the inline match, erasedValue and
        the specialized literal type tuple resulted from the Mirror refinement
    */
    private inline def validateFields[Elems <: Tuple](values: Elems): Boolean =
        inline erasedValue[Elems] match
            case _: EmptyTuple => true

            case _: (h *: t) =>
                val fields = values.asInstanceOf[h *: t]
                val head = fields.head
                val tail = fields.tail

                val ok = summonInline[PassivhausFieldRule[h]].validate(head)

                ok && validateFields[t](tail)
