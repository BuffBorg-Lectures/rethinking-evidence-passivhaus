package passivhaus.envelope.units

import scala.annotation.targetName

/* 
    Base Units
*/

object Meter:

    opaque type Meter = Double

    import WPerMeterKelvin.WPerMeterKelvin
    import RSquareMeterKelvinPerWatt.RSquareMeterKelvinPerWatt
    import SquareMeter.SquareMeter
    import CubicMeter.CubicMeter

    @targetName("meterApply")
    def apply(value: Double): Meter =
        require(value > 0, s"Length must be positive, got $value")
        value

    extension (m: Meter)
        @targetName("meterValue")
        private[units] def value: Double = m

        @targetName("meterMeterPlus")
        def +(other: Meter): Meter = m + other

        @targetName("meterDoubleScalarProduct")
        def *(scalar: Double): Meter = m * scalar

        @targetName("meterMeterGreaterThan")
        def >(other: Meter): Boolean = m > other

        @targetName("meterMeterLessThan")
        def <(other: Meter): Boolean = m < other

        @targetName("meterScalarProduct")
        def *(other: Meter): SquareMeter =
            SquareMeter(m.value * other.value)

        @targetName("meterSquareMeterProduct")
        def *(area: SquareMeter): CubicMeter =
            CubicMeter(m.value * area.value)

        @targetName("meterWmkDivision")
        def /(lambda: WPerMeterKelvin): RSquareMeterKelvinPerWatt =
            RSquareMeterKelvinPerWatt.from(m, lambda)

    extension (scalar: Double)
        @targetName("scalarMeterProduct")
        def *(m: Meter): Meter = m * scalar


object Kilogram:

    opaque type Kilogram = Double

    @targetName("kgApply")
    def apply(value: Double): Kilogram =
        require(value > 0, s"Mass must be positive, got $value")
        value

    extension (kg: Kilogram)
        @targetName("kgValue")
        private[units] def value: Double = kg

        @targetName("kgKgPlus")
        def +(other: Kilogram): Kilogram = kg + other

        @targetName("kgScalarProduct")
        def *(scalar: Double): Kilogram = kg * scalar

        @targetName("kgKgGreaterThan")
        def >(other: Kilogram): Boolean = kg > other

        @targetName("kgKgLessThan")
        def <(other: Kilogram): Boolean = kg < other

    extension (scalar: Double)
        @targetName("scalarKgProduct")
        def *(kg: Kilogram): Kilogram = kg * scalar


object Watt:

    opaque type Watt = Double

    @targetName("wattApply")
    def apply(value: Double): Watt =
        require(value > 0, s"Power must be positive, got $value")
        value

    extension (w: Watt)
        @targetName("wattValue")
        private[units] def value: Double = w

        @targetName("wattWattPlus")
        def +(other: Watt): Watt = w + other

        @targetName("wattScalarProduct")
        def *(scalar: Double): Watt = w * scalar

        @targetName("wattWattGreaterThan")
        def >(other: Watt): Boolean = w > other

        @targetName("wattWattLessThan")
        def <(other: Watt): Boolean = w < other

    extension (scalar: Double)
        @targetName("scalarWattProduct")
        def *(w: Watt): Watt = w * scalar


object Kelvin:

    opaque type Kelvin = Double

    @targetName("kelvinApply")
    def apply(value: Double): Kelvin =
        require(value > 0, s"Kelvin temperature must be positive, got $value")
        value

    extension (k: Kelvin)
        @targetName("kelvinValue")
        private[units] def value: Double = k

        @targetName("kelvinKelvinPlus")
        def +(other: Kelvin): Kelvin = k + other

        @targetName("kelvinScalarProduct")
        def *(scalar: Double): Kelvin = k * scalar

        @targetName("kelvinKelvinGreaterThan")
        def >(other: Kelvin): Boolean = k > other

        @targetName("kelvinKelvinLessThan")
        def <(other: Kelvin): Boolean = k < other

    extension (scalar: Double)
        @targetName("scalarKelvinProduct")
        def *(k: Kelvin): Kelvin = k * scalar


object Ratio:
    
    opaque type Ratio = Double

    @targetName("ratioApply")
    def apply(value: Double): Ratio =
        require(value >= 0 && value <= 1, s"Ratio must be between 0 and 1, got $value")
        value

    extension (r: Ratio)
        @targetName("ratioValue")
        private[units] def value: Double = r

        @targetName("ratioRatioGreaterThan")
        def >(other: Ratio): Boolean = r > other

        @targetName("ratioRatioLessThan")
        def <(other: Ratio): Boolean = r < other

        @targetName("ratioRatioGreaterOrEqual")
        def >=(other: Ratio): Boolean = r >= other


/* 
    Compound Units
*/

object SquareMeter:

    opaque type SquareMeter = Double

    import Meter.Meter
    import CubicMeter.CubicMeter

    @targetName("sqmApply")
    def apply(value: Double): SquareMeter =
        require(value > 0, s"Area must be positive, got $value")
        value

    @targetName("sqmFromMeter")
    def from(m: Meter): SquareMeter =
        apply(m.value * m.value)

    extension (a: SquareMeter)
        @targetName("sqmValue")
        private[units] def value: Double = a

        @targetName("sqmSqmPlus")
        def +(other: SquareMeter): SquareMeter = a + other

        @targetName("sqmScalarProduct")
        def *(scalar: Double): SquareMeter = a * scalar

        @targetName("sqmSqmGreaterThan")
        def >(other: SquareMeter): Boolean = a > other

        @targetName("sqmSqmLessThan")
        def <(other: SquareMeter): Boolean = a < other

        @targetName("sqmMeterProduct")
        def *(m: Meter): CubicMeter =
            CubicMeter(a.value * m.value)

    extension (scalar: Double)
        @targetName("scalarSqmProduct")
        def *(a: SquareMeter): SquareMeter = a * scalar


object CubicMeter:

    opaque type CubicMeter = Double

    import Meter.Meter

    @targetName("cbmApply")
    def apply(value: Double): CubicMeter =
        require(value > 0, s"Volume must be positive, got $value")
        value

    @targetName("cbmFromMeter")
    def from(m: Meter): CubicMeter =
        apply(m.value * m.value * m.value)

    extension (v: CubicMeter)
        @targetName("cbmValue")
        private[units] def value: Double = v

        @targetName("cbmCbmPlus")
        def +(other: CubicMeter): CubicMeter = v + other

        @targetName("cbmScalarProduct")
        def *(scalar: Double): CubicMeter = v * scalar

        @targetName("cbmCbmGreaterThan")
        def >(other: CubicMeter): Boolean = v > other

        @targetName("cbmCbmLessThan")
        def <(other: CubicMeter): Boolean = v < other

    extension (scalar: Double)
        @targetName("scalarCbmProduct")
        def *(v: CubicMeter): CubicMeter = v * scalar


object KgPerCubicMeter:

    opaque type KgPerCubicMeter = Double

    import Kilogram.Kilogram
    import CubicMeter.CubicMeter

    @targetName("kcmApply")
    def apply(value: Double): KgPerCubicMeter =
        require(value > 0, s"Density must be positive, got $value")
        value

    @targetName("kcmFromKgCbm")
    def from(mass: Kilogram, volume: CubicMeter): KgPerCubicMeter =
        apply(mass.value / volume.value)

    extension (rho: KgPerCubicMeter)
        @targetName("kcmValue")
        private[units] def value: Double = rho

        @targetName("kcmKcmPlus")
        def +(other: KgPerCubicMeter): KgPerCubicMeter = rho + other

        @targetName("kcmScalarProduct")
        def *(scalar: Double): KgPerCubicMeter = rho * scalar

        @targetName("kcmKcmGreaterThan")
        def >(other: KgPerCubicMeter): Boolean = rho > other

        @targetName("kcmKcmLessThan")
        def <(other: KgPerCubicMeter): Boolean = rho < other

    extension (scalar: Double)
        @targetName("scalarKcmProduct")
        def *(rho: KgPerCubicMeter): KgPerCubicMeter = rho * scalar


object WPerMeterKelvin:

    opaque type WPerMeterKelvin = Double

    import Watt.Watt
    import Meter.Meter
    import Kelvin.Kelvin

    @targetName("wmkApply")
    def apply(value: Double): WPerMeterKelvin =
        require(value > 0, s"Thermal conductivity must be positive, got $value")
        value

    @targetName("wmkFromWattMeterKelvin")
    def from(power: Watt, length: Meter, deltaT: Kelvin): WPerMeterKelvin =
        apply(power.value / (length.value * deltaT.value))

    extension (lambda: WPerMeterKelvin)
        @targetName("wmkValue")
        private[units] def value: Double = lambda

        @targetName("wmkWmkPlus")
        def +(other: WPerMeterKelvin): WPerMeterKelvin = lambda + other

        @targetName("wmkScalarProduct")
        def *(scalar: Double): WPerMeterKelvin = lambda * scalar

        @targetName("wmkWmkGreaterThan")
        def >(other: WPerMeterKelvin): Boolean = lambda > other

        @targetName("wmkWmkLessThan")
        def <(other: WPerMeterKelvin): Boolean = lambda < other

    extension (scalar: Double)
        @targetName("scalarWmkProduct")
        def *(wmk: WPerMeterKelvin): WPerMeterKelvin = wmk * scalar


object WattsPerSquareMeterKelvin:

    opaque type WattsPerSquareMeterKelvin = Double

    import Watt.Watt
    import SquareMeter.SquareMeter
    import Kelvin.Kelvin

    @targetName("wsmkApply")
    def apply(value: Double): WattsPerSquareMeterKelvin =
        require(value > 0, s"U-value must be positive, got $value")
        value

    @targetName("wsmkFromWattSqmKelvin")
    def from(power: Watt, area: SquareMeter, deltaT: Kelvin): WattsPerSquareMeterKelvin =
        apply(power.value / (area.value * deltaT.value))

    extension (u: WattsPerSquareMeterKelvin)
        @targetName("wsmkValue")
        private[units] def value: Double = u

        @targetName("wsmkWsmkPlus")
        def +(other: WattsPerSquareMeterKelvin): WattsPerSquareMeterKelvin = u + other

        @targetName("wsmkScalarProduct")
        def *(scalar: Double): WattsPerSquareMeterKelvin = u * scalar

        @targetName("wsmkWsmkGreaterThan")
        def >(other: WattsPerSquareMeterKelvin): Boolean = u > other

        @targetName("wsmkWsmkLessThan")
        def <(other: WattsPerSquareMeterKelvin): Boolean = u < other

    extension (scalar: Double)
        @targetName("scalarWsmkProduct")
        def *(u: WattsPerSquareMeterKelvin): WattsPerSquareMeterKelvin = u * scalar


object RSquareMeterKelvinPerWatt:

    opaque type RSquareMeterKelvinPerWatt = Double

    import Meter.Meter
    import WPerMeterKelvin.WPerMeterKelvin
    import SquareMeter.SquareMeter
    import Kelvin.Kelvin
    import Watt.Watt

    @targetName("rsmkwApply")
    def apply(value: Double): RSquareMeterKelvinPerWatt =
        require(value > 0, s"R-value must be positive, got $value")
        value

    @targetName("rsmkwFromSqmKelvinWatt")
    def from(area: SquareMeter, deltaT: Kelvin, power: Watt): RSquareMeterKelvinPerWatt =
        apply(area.value * deltaT.value / power.value)

    @targetName("rsmkwFromMeterWmk")
    def from(thickness: Meter, lambda: WPerMeterKelvin): RSquareMeterKelvinPerWatt =
        apply(thickness.value / lambda.value)

    extension (r: RSquareMeterKelvinPerWatt)
        @targetName("rsmkwValue")
        private[units] def value: Double = r

        @targetName("rsmkwRsmkwPlus")
        def +(other: RSquareMeterKelvinPerWatt): RSquareMeterKelvinPerWatt = r + other

        @targetName("rsmkwScalarProduct")
        def *(scalar: Double): RSquareMeterKelvinPerWatt = r * scalar

        @targetName("rsmkwRsmkwGreaterThan")
        def >(other: RSquareMeterKelvinPerWatt): Boolean = r > other

        @targetName("rsmkwRsmkwLessThan")
        def <(other: RSquareMeterKelvinPerWatt): Boolean = r < other

    extension (scalar: Double)
        @targetName("scalarRsmkwProduct")
        def *(r: RSquareMeterKelvinPerWatt): RSquareMeterKelvinPerWatt = r * scalar
