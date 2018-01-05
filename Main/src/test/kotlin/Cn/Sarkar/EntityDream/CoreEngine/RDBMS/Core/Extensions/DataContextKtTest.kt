package Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.Extensions

import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.IDBColumn
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.PipeLine.CorePipeLine
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.PipeLine.IPipeLineSubject
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.PipeLine.Subjects.TranslationSubject
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueriableCollection
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.Core.QueryBuilderExtensions.SelectQueryExpression.*
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.IDataContext
import Cn.Sarkar.EntityDream.CoreEngine.RDBMS.MySql.MySqlDataContext
import Cn.Sarkar.EntityDream.Pipeline.Core.Info.FeatureInfo
import Cn.Sarkar.EntityDream.Pipeline.Core.PipeLineContext
import Cn.Sarkar.EntityDream.Pipeline.Core.PipeLineFeature
import Cn.Sarkar.EntityDream.Pipeline.Core.PipeLineFeatureMetaData
import Cn.Sarkar.EntityDream.Pipeline.Extension.installFeature
import org.joda.time.DateTime
import org.junit.Test
import java.sql.DriverManager


object Users : DBTable() {

    val ID = idColumn("ID")
    val Name = stringColumn("Name") isN true default "" size 100
    val Pwd = stringColumn("Pwd") isN true size 50
    val Age = byteColumn("Age") unsigned true notNull true default 0
    val EMail = stringColumn("EMail") size 200
    val Money = doubleColumn("Money") notNull false precision 4
    val BirthDay = dateTimeColumn("BirthDay") default DateTime() notNull true
    val CompanyID = intColumn("CompanyID") notNull true reference Companys.ID unsigned true
    override var PrimaryKey: Array<IDBColumn<*>> = arrayOf(ID)
}

typealias users = Users

object Companys : DBTable("Company") {

    val ID = idColumn("ID")
    val Name = stringColumn("Name")
    var WebSite = stringColumn("WebSite")

    override var PrimaryKey: Array<IDBColumn<*>> = arrayOf(ID)
}

class User(DataContext: IDataContext) : DBEntity(DataContext, Users) {
    var ID by Users.ID
    var Name by Users.Name
    var Pwd by Users.Pwd
    var Age by Users.Age
    var EMail by Users.EMail
    var Money by Users.Money
    var BirthDay by Users.BirthDay
    var CompanyID by Users.CompanyID
    var Company by hasOne(Users.CompanyID){ Company(it) }
}

class Company(DataContext: IDataContext) : DBEntity(DataContext, Companys) {
    var ID by Companys.ID
    var Name by Companys.Name
    var WebSite by Companys.WebSite
    val Users: QueriableCollection<User> by hasMany(users.CompanyID) { User(it) }
}

internal class DataContextKtTest {

    object db : MySqlDataContext(DriverManager.getConnection("jdbc:mysql://127.0.0.1:3308/Hello", "yeganaaa", "Developer653125"))
    {
        val Users = dbCollection(users) { User(it) }
        val Companies = dbCollection(Companys) { Company(it) }
    }

    val logger = object : PipeLineFeature<IPipeLineSubject, IDataContext>() {

        var index = 0

        override val getMetaData: PipeLineFeatureMetaData by lazy { PipeLineFeatureMetaData(CorePipeLine.after, "Logger") }
        override val info: FeatureInfo by lazy { FeatureInfo("Logger", "Demo", "Sarkar Software Technologys", "yeganaaa", 1, "v0.1") }

        override fun PipeLineContext<IPipeLineSubject, IDataContext>.onExecute(subject: IPipeLineSubject) {
//                println((++index).toString() + "---" + subject.operationName + "***********" + subject::class.java.simpleName)

            if (subject is TranslationSubject) {

                println("Generated SQL ->> " + subject.translationResult!!.fullSqlQuery)
            }
        }
    }

    init {
        db.pipeLine.installFeature(logger)
    }

    @Test
    fun executeSelectQuery() {

        val result = db.Users
        result.forEach {
//            println("Age: ${it.Age}, Name: ${it.Name}, Money: ${it.Money} ID: ${it.ID}")
            println("${it.Name}, ${it.BirthDay.toString("yyyy/MM/dd HH:mm:ss")}")
        }
    }

    @Test
    fun delete() {
        val all = ArrayList<User>()

        db.Users.forEach {
            all.add(it)
        }

        all.forEach {
            db.Users.remove(it)
        }

        db.saveChanges()
    }

    @Test
    fun selectCompanies() {
        db.Companies.forEach {
            println(it.Name)
        }
        println(db.saveChanges())
    }

    @Test
    fun insertCompany(){

        val company = db.Companies.first { Companys.WebSite equals "http://www.sarkar.cn" }

        val usr = User(db).apply {
            Name = "北京-白小飞"
            this.Age = 20
            this.EMail = "北京-白小飞@163.com"
            this.Pwd = "北京-白小飞"
            this.Money = 664.414
            this.CompanyID = company.ID
            this.BirthDay = DateTime(1994, 3, 1, 8, 9, 10)

        }

        db.Users.add(usr)

        println(usr.ID)
        db.saveChanges()
        println(usr.ID)

    }

    @Test
    fun selectCompany() {

        val newCompany = db.Companies.single { Companys.Name equals "NewCopany" }
        val users = db.Users.where { Users.CompanyID equals 2 or (Users.CompanyID equals 15) }.forEach {
            it.Company = newCompany
        }

        db.saveChanges()
    }

    @Test
    fun selectCompanyUsers()
    {
        val comp = db.Companies.first { Companys.Name equals "سەركار" }
        println(comp.WebSite)

        comp.Users.forEach {
            println(it.Name)
            it.EMail = "新邮箱"
        }

        db.saveChanges()
    }
}
































