package wiresegal.zenmodelloader.common

import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import wiresegal.zenmodelloader.common.core.CommonProxy
import wiresegal.zenmodelloader.common.lib.LibMisc

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES, acceptedMinecraftVersions = LibMisc.VERSIONS)
class ZenModelLoader {
    companion object {
        @SidedProxy(serverSide = LibMisc.PROXY_COMMON,
                clientSide = LibMisc.PROXY_CLIENT)
        lateinit var PROXY: CommonProxy

        val LOGGER: Logger = LogManager.getLogger(LibMisc.MOD_ID)

        val DEV_ENVIRONMENT: Boolean by lazy {
            Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean
        }
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        PROXY.pre(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        PROXY.init(event)
    }

    @Mod.EventHandler
    fun post(event: FMLPostInitializationEvent) {
        PROXY.post(event)
    }
}
