/**
Company: Sarkar software technologys
WebSite: http://www.sarkar.cn
Author: yeganaaa
Date : 11/29/17
Time: 4:51 PM
 */

package Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.PipeLine.Subjects

import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.PipeLine.IPipeLineSubject

class UpdateAndDeleteResultReaderSubject(var executionSubject: QueryExecutionSubject) : IPipeLineSubject {
    override val operationName: String = "UpdateAndDeleteResult"
    override val operationDescription: String = "يىڭىلاش ۋە ئۆچۈرۈش نەتىجىسىنى ئوقۇش"
    var EffectedRows: IntArray = IntArray(0, { 0 })
}