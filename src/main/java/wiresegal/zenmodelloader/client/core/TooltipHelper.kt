package wiresegal.zenmodelloader.client.core

import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.zenmodelloader.common.ZenModelLoader
import wiresegal.zenmodelloader.common.lib.LibMisc

/**
 * Utilities for tooltips.
 */
object TooltipHelper {

    /**
     * If the client player is sneaking, invoke the given lambda, and otherwise add a 'press shift for info'.
     */
    @SideOnly(Side.CLIENT)
    fun tooltipIfShift(tooltip: MutableList<String>, lambda: () -> Unit) {
        if (GuiScreen.isShiftKeyDown())
            lambda()
        else
            addToTooltip(tooltip, "${LibMisc.MOD_ID}.shiftinfo")
    }

    /**
     * A wrapper for easy access from java of the tooltipIfShift function.
     */
    @SideOnly(Side.CLIENT)
    @JvmStatic
    fun tooltipIfShift(tooltip: MutableList<String>, lambda: Runnable) = tooltipIfShift(tooltip, { lambda.run() })

    /**
     * Add something to the tooltip that's translated and colorized.
     */
    @JvmStatic
    fun addToTooltip(tooltip: MutableList<String>, s: String, vararg format: Any?) {
        tooltip.add(local(s, *format).replace("&".toRegex(), "§"))
    }

    /**
     * Localize a key and format it.
     */
    @JvmStatic
    fun local(s: String, vararg format: Any?): String {
        return ZenModelLoader.PROXY.translate(s, *format)
    }
}
