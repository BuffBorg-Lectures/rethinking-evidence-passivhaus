package passivhaus.envelope.materials.lookup

import scala.compiletime.{erasedValue, constValue, error}

import passivhaus.envelope.materials.PassivhausCertifiedMaterial
import passivhaus.envelope.units.ThermalConductivity.*

/*
    MaterialCode is part of the domain’s ubiquitous language. 
    Encoding the allowed codes as literal types ensures that only 
    valid codes can ever appear in the system. No runtime validation
    is needed, the type system enforces correctness.
*/
type MaterialCode = "WF" | "MW" | "CL"

/*
    ValueOf[Code] is evidence that Code is a literal type whose 
    value is known at compile time. This evidence has two effects:
    1. Compile-time blocking: the compiler cannot construct a 
    MaterialTag unless Code is a literal type. Dynamic strings 
    or unknown codes simply cannot be used. 
    2. Runtime reverberation: unlike purely compile-time evidence 
    (e.g., Mirror), ValueOf reifies the literal type into an 
    actual runtime value. This allows the literal identity of 
    the material code to be used in domain objects, logging, 
    serialization and so on.
*/
final case class MaterialTag[Code <: MaterialCode]()(using val valueOfCode: ValueOf[Code]):
    def code: String = valueOfCode.value

/* 
    This given auto-derives tags using ValueOf evidence. 
    Basically, the compiler materializes the literal value automatically.
*/
given materialTag[Code <: MaterialCode](using v: ValueOf[Code]): MaterialTag[Code] =
    MaterialTag()

/* 
    ValueOf is used indirectly in here, it is the reason Code is guaranteed
    to be a literal type, thus enabling erasedValue-based branching.
*/
object MaterialLambdaLookup:

    inline def materialFor[Code <: MaterialCode]: PassivhausCertifiedMaterial =
        inline erasedValue[Code] match
            case _: "WF" => PassivhausCertifiedMaterial.WoodFiber
            case _: "MW" => PassivhausCertifiedMaterial.MineralWool
            case _: "CL" => PassivhausCertifiedMaterial.Cellulose
            case _ => error("Unhandled material code! Separate implementation is worth considering.")

    inline def lambdaFor[Code <: MaterialCode]: WPerMeterKelvin =
        inline erasedValue[Code] match
            case _: "WF" => WPerMeterKelvin(0.040)
            case _: "MW" => WPerMeterKelvin(0.036)
            case _: "CL" => WPerMeterKelvin(0.038)
            case _ => error("Unhandled material code! Separate implementation is worth considering.")

/* 
    constValue[Code] retrieves the literal value of Code at compile time. 
    This is only possible because ValueOf evidence exists for Code. 
    The comparison `code == constValue[Code]` is reduced statically, 
    eliminating all runtime branching.
*/
    private inline def lambdaTable =
        ("WF", WPerMeterKelvin(0.040)) *:
        ("MW", WPerMeterKelvin(0.036)) *:
        ("CL", WPerMeterKelvin(0.038)) *:
        EmptyTuple

    private inline def lookupLambda[Code <: MaterialCode](table: Tuple): WPerMeterKelvin =
        inline table match
            case EmptyTuple =>
                error("Invalid material code. It's unreachable due to type constraints.")
            case (code: String, value: WPerMeterKelvin) *: tail =>
                inline if code == constValue[Code] then value
                    else lookupLambda[Code](tail)

    inline def lambdaForViaTable[Code <: MaterialCode]: WPerMeterKelvin =
        lookupLambda[Code](lambdaTable)

