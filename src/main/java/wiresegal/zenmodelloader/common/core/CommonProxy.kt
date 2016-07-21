@file:Suppress("DEPRECATION")

package wiresegal.zenmodelloader.common.core

import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.translation.I18n
import net.minecraft.world.World
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import vazkii.botania.common.core.helper.Vector3
import java.awt.Color

/**
 * @author WireSegal
 * Created at 5:07 PM on 4/12/16.
 */
open class CommonProxy {
    open fun pre(e: FMLPreInitializationEvent) {
        //NO-OP
    }

    open fun init(e: FMLInitializationEvent) {
        //NO-OP
    }

    open fun post(e: FMLPostInitializationEvent) {
        // NO-OP
    }

    open fun translate(s: String, vararg format: Any?): String {
        return I18n.translateToLocalFormatted(s, *format)
    }

}
