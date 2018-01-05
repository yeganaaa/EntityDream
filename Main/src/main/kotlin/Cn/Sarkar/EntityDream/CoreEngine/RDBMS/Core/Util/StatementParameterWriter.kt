/**
Company: Sarkar software technologys
WebSite: http://www.sarkar.cn
Author: yeganaaa
Date : 12/2/17
Time: 8:00 PM
 */

package Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.Util

import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.IDBColumn
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Common.ColumnParameter
import org.joda.time.DateTime
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.Time
import java.sql.Timestamp
import java.util.*

fun PreparedStatement.WriteParameters(index: Int, value: Any){
    when (value)
    {
        is String -> this.setString(index, value)
        is Boolean -> this.setBoolean(index, value)
        is Byte -> this.setByte(index, value)
        is Short -> this.setShort(index, value)
        is Int -> this.setInt(index, value)
        is Long -> this.setLong(index, value)
        is Float -> this.setFloat(index, value)
        is Double -> this.setDouble(index, value)
//                is Decimal -> statement.setDouble(index, value)
        is ByteArray -> this.setBytes(index, value)
        is Date -> this.setDate(index, value)
        is Time -> this.setTime(index, value)
        is Timestamp -> this.setTimestamp(index, value)
        is DateTime -> this.setTimestamp(index, java.sql.Timestamp(value.millis))
    /**
     * جەدۋەل پارامىتىر ئۈچۈن
     */
//        is IDBColumn<*> -> this.setObject(index, value.toString())
        else -> throw Exception("قوللىمايدىغان پارامىتىر تىپى...")
    }
}