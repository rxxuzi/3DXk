package main

class Plane {
    var V1: Vector
    var V2: Vector
    var NV: Vector
    var P = DoubleArray(3)

    constructor(DP: DPolygon) {
        P[0] = DP.x[0]
        P[1] = DP.y[0]
        P[2] = DP.z[0]
        V1 = Vector(DP.x[1] - DP.x[0],
                DP.y[1] - DP.y[0],
                DP.z[1] - DP.z[0])
        V2 = Vector(DP.x[2] - DP.x[0],
                DP.y[2] - DP.y[0],
                DP.z[2] - DP.z[0])

        //V1Ç∆V2ÇíºçsÇ≥ÇπÇÈ
        NV = V1.CrossProduct(V2)
    }

    constructor(VE1: Vector, VE2: Vector, Z: DoubleArray) {
        //double[] Z = P
        P = Z
        V1 = VE1
        V2 = VE2
        NV = V1.CrossProduct(V2)
    }
}