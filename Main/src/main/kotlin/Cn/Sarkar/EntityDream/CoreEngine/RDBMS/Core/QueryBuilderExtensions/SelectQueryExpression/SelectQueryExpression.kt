/**
Company: Sarkar software technologys
WebSite: http://www.sarkar.cn
Author: yeganaaa
Date : 11/22/17
Time: 11:06 PM
 */

package Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryBuilderExtensions.SelectQueryExpression

import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.*
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Common.Function.Aggregate.*
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Common.Function.IDBFunction
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Common.Function.Scalar.*
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Common.Where
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Common.WhereItemCondition
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.ISelectQueryExpression
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Select.*
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.IDataContext

val IDBColumn<*>.fullColumnName: String
    get() = "${this.Table.TableName}.${this.ColumnName}"


/*************************************************************************/
val IDBTable.SelectAllColumns: Select get() = Select(*(this.Columns.map { FromColumn(it.fullColumnName, DefaultValue = it.DataType.DefaultValue!!, AliasName = it.ColumnName) }.toTypedArray()))
val IDBTable.FromThis: From get() = From(FromTable(TableName))
val IDBTable.SelectQuery : ISelectQueryExpression get() = SelectQueryExpression(this, SelectAllColumns, FromThis)
/*************************************************************************/
val IDBTable.FromColumns: Array<FromColumn> get() = arrayOf(*(this.Columns.map { FromColumn(it.ColumnName, DefaultValue = it.DataType.DefaultValue!!) }.toTypedArray()))

/*************************************************************************/

private fun <ENTITY: IDBEntity, COLLECTION> COLLECTION.applyUpdate(apply: QueriableCollection<ENTITY>.() -> Unit) : QueriableCollection<ENTITY> where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression{



    val qc = QueriableCollection<ENTITY>(this.Context, this.table, this.Where?.condition, ItemGenerator)

    qc.Select = Select
    qc.From = From
    qc.GroupBy = GroupBy
    qc.GroupBy = GroupBy
    qc.Having = Having
    qc.OrderBy = OrderBy

    qc.apply()
    return qc
}

infix fun <ENTITY: IDBEntity, COLLECTION> COLLECTION.where(condition: () -> WhereItemCondition) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = applyUpdate { if (this.Where == null) this.Where = Where(condition()) else this.Where!!.condition = condition() }

infix fun <ENTITY: IDBEntity, COLLECTION> COLLECTION.andWhere(condition: (table: IDBTable) -> WhereItemCondition) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = applyUpdate { Where!!.condition = Where!!.condition and condition(this.table) }
infix fun <ENTITY: IDBEntity, COLLECTION> COLLECTION.orWhere(condition: (table: IDBTable) -> WhereItemCondition) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = applyUpdate { Where!!.condition = Where!!.condition or condition(this.table) }
fun <ENTITY: IDBEntity, COLLECTION> COLLECTION.slice(vararg columns: IDBColumn<*>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.apply { this.Select.selectors.clear(); this.Select.selectors.addAll(columns.map { FromColumn(it.fullColumnName, DefaultValue = it.DataType.DefaultValue!!) }) }
internal fun <ENTITY: IDBEntity, COLLECTION> COLLECTION.removeFromColumnIfExists(column: IDBColumn<*>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression {
    val col = this.Select.selectors.firstOrNull { it is FromColumn && it.SourceName == column.fullColumnName } as FromColumn?
    if (col != null) this.Select.selectors.remove(col)
}

/*************************************************************************/
private fun IDBColumn<*>.fromFunction(function: IDBFunction): FromFunction = FromFunction(function, ColumnName, this.DataType.DefaultValue!!)

/*Aggregate Functions*/
infix fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.sum(column: IDBColumn<T>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(Sum({ column.fullColumnName }))) }

infix fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.avg(column: IDBColumn<T>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(Avg({ column.fullColumnName }))) }
infix fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.count(column: IDBColumn<T>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(Count({ column.fullColumnName }))) }
infix fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.max(column: IDBColumn<T>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(Max({ column.fullColumnName }))) }
infix fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.min(column: IDBColumn<T>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(Min({ column.fullColumnName }))) }
/*Scalar Functions*/
infix fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.lCase(column: IDBColumn<T>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(LCase({ column.fullColumnName }))) }

infix fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.uCase(column: IDBColumn<T>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(UCase({ column.fullColumnName }))) }
infix fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.len(column: IDBColumn<T>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(Len({ column.fullColumnName }))) }
fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.format(column: IDBColumn<T>, format: String) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(Format({ column.fullColumnName }, format))) }
fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.mid(column: IDBColumn<T>, from: Int, length: Int? = null) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(Mid({ column.fullColumnName }, from, length))) }
fun <T, ENTITY: IDBEntity, COLLECTION> COLLECTION.round(column: IDBColumn<T>, decimals: Float) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { removeFromColumnIfExists(column);this.Select.selectors.add(column.fromFunction(Round({ column.fullColumnName }, decimals))) }
/*************************************************************************/
infix fun <ENTITY: IDBEntity, COLLECTION> COLLECTION.orderBy(column: IDBColumn<*>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { OrderBy = OrderBy(column.fullColumnName, Order.Asc) }

infix fun <ENTITY: IDBEntity, COLLECTION> COLLECTION.orderByDesc(column: IDBColumn<*>) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { OrderBy = OrderBy(column.fullColumnName, Order.Desc) }
infix fun <ENTITY: IDBEntity, COLLECTION> COLLECTION.take(number: Int) where COLLECTION : IQueriableCollection<ENTITY>, COLLECTION : ISelectQueryExpression = this.applyUpdate { Select.top = number }
