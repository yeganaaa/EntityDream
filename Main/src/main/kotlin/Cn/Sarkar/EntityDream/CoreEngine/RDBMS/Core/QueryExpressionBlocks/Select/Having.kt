/**
Company: Sarkar software technologys
WebSite: http://www.sarkar.cn
Author: yeganaaa
Date : 11/22/17
Time: 6:01 PM
 */

package Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Select

import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Common.Function.IDBFunction
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.Common.WhereItemCondition
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryExpressionBlocks.SuperBlock

class Having(var function: IDBFunction, var condition: WhereItemCondition) : SuperBlock {
}