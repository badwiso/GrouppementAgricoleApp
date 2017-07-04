package dao

/**
 * Created by Monta on 20/06/2017.
 */
class Provider{
    companion object{
        @JvmStatic fun getDAO():DAOModel=H2Model()
    }
}