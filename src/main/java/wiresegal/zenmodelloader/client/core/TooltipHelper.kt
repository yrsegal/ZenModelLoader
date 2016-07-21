package wiresegal.zenmodelloader.client.core

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.resources.I18n
import wiresegal.zenmodelloader.common.ZenModelLoader

/**
 * Utilities for tooltips.
 */
object TooltipHelper {

    /**
     * If the client player is sneaking, invoke the given lambda, and otherwise add a 'press shift for info'.
     */
    fun tooltipIfShift(tooltip: MutableList<String>, r: () -> Unit) {
        if (GuiScreen.isShiftKeyDown()) {
            r.invoke()
        } else {
            addToTooltip(tooltip, "zml.shiftinfo")
        }
    }

    fun tooltipIfShift(tooltip: MutableList<String>, r: Runnable) {
        tooltipIfShift(tooltip) {
            r.run()
        }
    }

    /**
     * Add something to the tooltip that's translated and colorized.
     */
    fun addToTooltip(tooltip: MutableList<String>, s: String, vararg format: Any?) {
        val toAdd = local(s, *format).replace("&".toRegex(), "§")

        tooltip.add(toAdd)
    }

    /**
     * Localize a key and format it.
     */
    fun local(s: String, vararg format: Any?): String {
        return ZenModelLoader.PROXY.translate(s, *format)
    }
}
