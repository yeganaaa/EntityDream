/**
Company: Sarkar software technologys
WebSite: http://www.sarkar.cn
Author: yeganaaa
Date : 11/24/17
Time: 2:25 PM
 */

package Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.EntityFieldConnector

data class KeyValuePair<K, V>(var key: K, var value: V)
infix fun <K, V> K.to(value: V): KeyValuePair<K, V> = KeyValuePair(this, value)