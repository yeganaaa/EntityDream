/**
Company: Sarkar software technologys
WebSite: http://www.sarkar.cn
Author: yeganaaa
Date : 11/15/17
Time: 12:29 AM
 */

package Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryBlocks.Insert

import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.IQueryExpression

class InsertQueryExpression(
        var insertInto: InsertInto,
        var values: Values
) : IQueryExpression {

}