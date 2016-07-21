package wiresegal.zenmodelloader.common.block.base

import net.minecraft.block.Block
import net.minecraft.block.material.MapColor
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.item.EnumRarity
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.zenmodelloader.client.core.ModelHandler
import wiresegal.zenmodelloader.client.core.TooltipHelper
import wiresegal.zenmodelloader.common.core.IModBlock

/**
 * @author WireSegal
 * Created at 5:45 PM on 3/20/16.
 */
open class BlockMod(name: String, materialIn: Material, color: MapColor, vararg override var variants: String) : Block(materialIn, color), IModBlock {

    constructor(name: String, materialIn: Material, vararg variants: String) : this(name, materialIn, materialIn.materialMapColor, *variants)

    override val bareName: String = name
    val modId: String

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

    val itemForm: ItemBlock? by lazy { createItemForm() }

    init {
        modId = Loader.instance().activeModContainer().modId
        if (variants.size == 0) {
            this.variants = arrayOf(name)
        }
        this.unlocalizedName = name
        if (itemForm == null)
            ModelHandler.addToCache(this)
    }

    override fun setUnlocalizedName(name: String): Block {
        super.setUnlocalizedName(name)
        setRegistryName(name)
        GameRegistry.register(this)
        if (itemForm != null)
            GameRegistry.register(itemForm, ResourceLocation(modId, name))
        return this
    }

    open fun createItemForm(): ItemBlock? {
        return ItemModBlock(this)
    }

    @SideOnly(Side.CLIENT)
    override fun getCustomMeshDefinition() = null

    override val ignoredProperties: Array<IProperty<*>>?
        get() = arrayOf()

    override val variantEnum: Class<Enum<*>>?
        get() = null

    override fun getBlockRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.COMMON
    }
}
