package passivhaus.envelope.assembly.constraints.moisture

import passivhaus.envelope.materials.PassivhausCertifiedMaterial
import passivhaus.envelope.materials.PassivhausCertifiedMaterial.*

import passivhaus.envelope.assembly.AssemblyType
import passivhaus.envelope.assembly.AssemblyType.*

enum MoistureRisk:
    case Low
    case Medium
    case High

import MoistureRisk.*

/*
    AssemblyMoistureRisk[A] is a type‑level function that assigns a moisture
    risk class to each assembly type.
*/
type AssemblyMoistureRisk[A <: AssemblyType] <: MoistureRisk = A match
    case ExteriorWall.type => High.type
    case InteriorWall.type => Medium.type
    case BelowGradeWall.type => High.type

/*
    MaterialMoistureTolerance[M] is a type‑level function that assigns a moisture
    tolerance class to each certified material.
*/
type MaterialMoistureTolerance[M <: PassivhausCertifiedMaterial] <: MoistureRisk = M match
    case MineralWool.type => High.type
    case WoodFiber.type => Medium.type
    case Cellulose.type => Low.type

/*
    MoistureCompatible[M, A] is a type‑level Boolean that encodes whether
    the material’s tolerance is sufficient for the assembly’s moisture risk.

    This is a multi-factor type‑level computation:
    1. compute MaterialMoistureTolerance[M]
    2. compute AssemblyMoistureRisk[A]
    3. compare the two and yield true or false
*/
type MoistureCompatible[M <: PassivhausCertifiedMaterial, A <: AssemblyType] <: Boolean =
    (MaterialMoistureTolerance[M], AssemblyMoistureRisk[A]) match
        case (High.type, High.type) => true
        case (High.type, Medium.type) => true
        case (High.type, Low.type) => true
        case (Medium.type, Medium.type) => true
        case (Medium.type, Low.type) => true
        case (Low.type, Low.type) => true
        case _  => false
