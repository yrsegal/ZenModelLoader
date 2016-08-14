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
    fun tooltipIfShift(tooltip: MutableList<String>, r: () -> Unit) {
        if (GuiScreen.isShiftKeyDown())
            r.invoke()
        else
            addToTooltip(tooltip, "${LibMisc.MOD_ID}.shiftinfo")
    }

    @SideOnly(Side.CLIENT)
    fun tooltipIfShift(tooltip: MutableList<String>, r: Runnable) {
        tooltipIfShift(tooltip) { r.run() }
    }

    /**
     * Add something to the tooltip that's translated and colorized.
     */
    fun addToTooltip(tooltip: MutableList<String>, s: String, vararg format: Any?) {
        tooltip.add(local(s, *format).replace("&".toRegex(), "§"))
    }

    /**
     * Localize a key and format it.
     */
    fun local(s: String, vararg format: Any?): String {
        return ZenModelLoader.PROXY.translate(s, *format)
    }
}
