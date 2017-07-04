package dao

import dao.DAOModel.Companion.connection
import models.Client
import models.Region
import models.RegionWrapper
import models.Session
import java.sql.*

/**
 * Created by Monta on 19/06/2017.
 */
class H2Model:DAOModel {
    init {
        if(connection == null) {
            Class.forName("org.h2.Driver")
            connection = DriverManager.getConnection("jdbc:h2:file:./DB/association", "sa", "")
            try {
                val stm = connection!!.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS REGIONS(ID INT AUTO_INCREMENT PRIMARY KEY,NAME VARCHAR(80));"
                                + "CREATE TABLE IF NOT EXISTS CLIENT(ID INT AUTO_INCREMENT PRIMARY KEY,REF INT,NAME VARCHAR(80),PHONE VARCHAR(20),CIN VARCHAR(20),RID INT,SUSPENDED BOOLEAN);"
                                + "CREATE TABLE IF NOT EXISTS SESSION(ID INT AUTO_INCREMENT PRIMARY KEY,FROMDATE DATE,TODATE DATE,UPRICE DOUBLE,FIXEDTX DOUBLE,DELAYTX DOUBLE,OTHERTX DOUBLE);")
                stm.execute()
                stm.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    override fun getRegionsWrapper(): ArrayList<RegionWrapper> {
        val regions:ArrayList<RegionWrapper> = arrayListOf()
        val stm = connection!!.prepareStatement("SELECT ID,NAME,(SELECT COUNT(*) FROM CLIENT WHERE RID=R.ID) AS NB,(SELECT COUNT(*) FROM CLIENT) FROM REGIONS R ORDER BY 2")
        val rs = stm.executeQuery()
        while (rs.next()){
            regions.add(RegionWrapper(rs.getInt(1),rs.getString(2),rs.getInt(3),if(rs.getDouble(4)==0.0)0.0 else rs.getInt(3)/rs.getDouble(4)))
        }
        rs.close()
        stm.close()
        return regions
    }

    override fun getClients(): ArrayList<Client> {
        val res:ArrayList<Client> = ArrayList()
        val stm = connection!!.prepareStatement("SELECT C.ID AS CID,REF,CIN,C.NAME AS CNAME,RID,R.NAME AS RNAME,PHONE,SUSPENDED " +
                "FROM REGIONS R,CLIENT C " +
                "WHERE RID = R.ID " +
                "ORDER BY REF,RID;")
        val rs = stm.executeQuery()
        while (rs.next()){
            res.add(Client(
                    rs.getInt("CID"),
                    rs.getInt("REF"),
                    rs.getString("NAME"),
                    rs.getString("PHONE"),
                    rs.getString("CIN"),
                    Region(rs.getInt("RID"),rs.getString("RNAME")),
                    rs.getBoolean("SUSPENDED")
            ))
        }
        rs.close()
        stm.close()
        return res
    }

    override fun mergeClient(client: Client) {
        val stm = connection!!.prepareStatement("MERGE INTO CLIENT (REF,NAME,RID,PHONE,CIN,SUSPENDED) KEY(REF) VALUES (?,?,?,?,?,?)")
        with(stm){
            setInt(1,client.ref)
            setString(2,client.name)
            setInt(3,client.region.id)
            setString(4,client.phone)
            setString(5,client.cin)
            setBoolean(6,client.suspended)
            executeUpdate()
            closeOnCompletion()
        }
    }

    override fun deleteClient(client: Client) {
        val stm = connection!!.prepareStatement("DELETE FROM CLIENT WHERE ID = ?")
        stm.setInt(1,client.id)
        stm.executeUpdate()
    }

    override fun getRegions(): ArrayList<Region> {
        val regions:ArrayList<Region> = arrayListOf()
        val stm = connection!!.prepareStatement("SELECT ID,NAME FROM REGIONS")
        val rs = stm.executeQuery()
        while (rs.next()){
            regions.add(Region(rs.getInt(1),rs.getString(2)))
        }
        rs.close()
        stm.close()
        return regions
    }

    override fun mergeRegion(region: Region):Int {
        var stm:PreparedStatement?
        when(region.id){
            -1 -> stm = connection!!.prepareStatement("MERGE INTO REGIONS (NAME) KEY(NAME) VALUES (?)")
            else ->{
                stm = connection!!.prepareStatement("select count(*) from REGIONS where NAME= ?")
                stm.setString(1,region.name)
                val rs = stm.executeQuery()
                rs.next()
                when(rs.getInt(1)) {
                    0 -> {
                        stm = connection!!.prepareStatement("MERGE INTO REGIONS (NAME,ID) VALUES (?,?)")
                        stm.setInt(2,region.id)
                    }
                    else -> {
                        rs.close()
                        stm.close()
                        return -1
                    } // todo value in use
                }
            }
        }
        stm.setString(1,region.name)
        stm.executeUpdate()
        stm.close()
        return 1
    }

    override fun deleteRegion(region: Region):Boolean {
        var stm = connection!!.prepareStatement("SELECT COUNT(*) FROM Client WHERE RID = ?")
        with(stm){
            setInt(1,region.id)
            val rs = executeQuery()
            rs.next()
            when(rs.getInt(1)){
                0 -> {
                    rs.close() // Note to self: not to forget this next time
                    stm = connection.prepareStatement("DELETE FROM REGIONS WHERE ID = ?")
                    stm.setInt(1,region.id)
                    stm.executeUpdate()
                    return true
                }
                else -> return false
            }
        }
    }

    override fun getSessions(): ArrayList<Session> {
        val res = ArrayList<Session>()
        val stm = connection!!.prepareStatement("SELECT ID,FROMDATE,TODATE,UPRICE,FIXEDTX,DELAYTX,OTHERTX FROM SESSION")
        val rs = stm.executeQuery()
        while (rs.next()){
            res.add(Session(
                    rs.getInt(1),
                    rs.getDate(2).toLocalDate(),
                    rs.getDate(3).toLocalDate(),
                    rs.getDouble(4),
                    rs.getDouble(5),
                    rs.getDouble(6),
                    rs.getDouble(7)
            ))
        }
        return res
    }

    override fun mergeSession(session: Session) {
        var stm : PreparedStatement
        with(session){
            if(id!=-1){
                stm = connection!!.prepareStatement("MERGE INTO SESSION (FROMDATE,TODATE,UPRICE,FIXEDTX,DELAYTX,OTHERTX,ID) KEY(ID) VALUES(?,?,?,?,?,?,?)")
                stm.setInt(7,id)
            }else{
                stm = connection!!.prepareStatement("MERGE INTO SESSION (FROMDATE,TODATE,UPRICE,FIXEDTX,DELAYTX,OTHERTX) KEY(FROMDATE,TODATE) VALUES(?,?,?,?,?,?)")
            }
            stm.setDate(1, java.sql.Date.valueOf(fromDate.toString()))
            stm.setDate(2, java.sql.Date.valueOf(toDate.toString()))
            stm.setDouble(3,uPrice)
            stm.setDouble(4,fixedTx)
            stm.setDouble(5,delayTx)
            stm.setDouble(6,otherTx)
            stm.executeUpdate()
            stm.close()
        }
    }

    override fun deleteSession(session: Session) {
        val stm = connection!!.prepareStatement("DELETE FROM SESSION WHERE ID = ?")
        stm.setInt(1,session.id)
        stm.executeUpdate()
        stm.closeOnCompletion()
    }
}