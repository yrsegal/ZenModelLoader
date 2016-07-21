package wiresegal.zenmodelloader.client.core

import net.minecraft.client.resources.I18n
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import wiresegal.zenmodelloader.common.core.CommonProxy


/**
 * @author WireSegal
 * Created at 8:37 AM on 3/20/16.
 */
class ClientProxy : CommonProxy() {
    override fun pre(e: FMLPreInitializationEvent) {
        super.pre(e)
    }

    override fun init(e: FMLInitializationEvent) {
        super.init(e)
        ModelHandler.init()
    }

    override fun translate(s: String, vararg format: Any?): String {
        return I18n.format(s, *format)
    }
}
