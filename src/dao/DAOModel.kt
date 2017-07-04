package dao

import models.*
import java.sql.Connection

/**
 * Created by Monta on 19/06/2017.
 */

interface DAOModel{
    fun getRegions(): ArrayList<Region>
    fun getRegionsWrapper(): ArrayList<RegionWrapper>
    fun mergeRegion(region: Region): Int
    fun deleteRegion(region: Region): Boolean
    fun getClients(): ArrayList<Client>
    fun mergeClient(client: Client)
    fun deleteClient(client: Client)
    fun getSessions():ArrayList<Session>
    fun mergeSession(session: Session)
    fun deleteSession(session: Session)
    companion object{
        var connection:Connection? = null
    }

}