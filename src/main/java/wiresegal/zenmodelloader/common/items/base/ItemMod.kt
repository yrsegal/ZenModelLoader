package wiresegal.zenmodelloader.common.items.base

import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.zenmodelloader.client.core.ModelHandler
import wiresegal.zenmodelloader.client.core.TooltipHelper
import wiresegal.zenmodelloader.common.core.IVariantHolder

/**
 * @author WireSegal
 * Created at 8:50 AM on 3/20/16.
 */
open class ItemMod(name: String, vararg variants: String) : Item(), IVariantHolder {

    companion object {
        fun tooltipIfShift(tooltip: MutableList<String>, r: () -> Unit) {
            TooltipHelper.tooltipIfShift(tooltip, r)
        }

        fun tooltipIfShift(tooltip: MutableList<String>, r: Runnable) {
            TooltipHelper.tooltipIfShift(tooltip, r)
        }

        fun addToTooltip(tooltip: MutableList<String>, s: String, vararg format: Any) {
            TooltipHelper.addToTooltip(tooltip, s, *format)
        }

        fun local(s: String): String {
            return TooltipHelper.local(s)
        }
    }

    override val variants: Array<out String>

    @SideOnly(Side.CLIENT)
    override fun getCustomMeshDefinition(): ItemMeshDefinition? = null

    private val bareName: String
    private val modId: String

    init {
        modId = Loader.instance().activeModContainer().modId

        var variantTemp = variants
        this.unlocalizedName = name
        if (variantTemp.size > 1) {
            this.setHasSubtypes(true)
        }

        if (variantTemp.size == 0) {
            variantTemp = arrayOf(name)
        }

        this.bareName = name
        this.variants = variantTemp
        ModelHandler.addToCache(this)
    }

    override fun setUnlocalizedName(name: String): Item {
        val rl = ResourceLocation(modId, name)
        GameRegistry.register(this, rl)
        return super.setUnlocalizedName(name)
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        val dmg = stack.itemDamage
        val variants = this.variants
        val name: String
        if (dmg >= variants.size) {
            name = this.bareName
        } else {
            name = variants[dmg]
        }

        return "item.$modId:$name"
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for (i in 0..this.variants.size - 1) {
            subItems.add(ItemStack(itemIn, 1, i))
        }
    }
}

