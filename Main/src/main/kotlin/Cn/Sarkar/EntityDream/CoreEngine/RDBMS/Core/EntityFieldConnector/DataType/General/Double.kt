package Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.EntityFieldConnector.DataType.General

import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.EntityFieldConnector.DataType.IDBNumberType
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.EntityFieldConnector.DataType.IDBPlainDataType
import kotlin.Double

class Double(override var DefaultValue: Double = 0.toDouble(), override var Unsigned: Boolean = false) : IDBPlainDataType<Double>, IDBNumberType<Double> {
    override var Name: String = "Double"
}