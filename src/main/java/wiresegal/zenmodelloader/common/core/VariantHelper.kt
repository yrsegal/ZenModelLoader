package wiresegal.zenmodelloader.common.core

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.zenmodelloader.client.core.ModelHandler
import wiresegal.zenmodelloader.common.tab.ModCreativeTab

/**
 * Tools to implement variants easily.
 */
object VariantHelper {

    /**
     * All items which use this method in their constructor should implement the setUnlocalizedNameForItem provided below in their setUnlocalizedName.
     */
    @JvmOverloads
    @JvmStatic
    fun setupItem(item: Item, name: String, variants: Array<out String>, creativeTab: ModCreativeTab? = null): Array<out String> {
        var variantTemp = variants
        item.unlocalizedName = name
        if (variantTemp.size > 1) {
            item.hasSubtypes = true
        }

        if (variantTemp.size == 0) {
            variantTemp = arrayOf(name)
        }

        ModelHandler.registerVariantHolder(item as IVariantHolder)
        creativeTab?.set(item)
        return variantTemp
    }

    /**
     * All blocks which use this method in their constructor should implement the setUnlocalizedNameForBlock provided below in their setUnlocalizedName.
     * After caching variants using this, call finishSetupBlock.
     */
    @JvmStatic
    fun beginSetupBlock(name: String, variants: Array<out String>): Array<out String> {
        var variantTemp = variants
        if (variants.size == 0) {
            variantTemp = arrayOf(name)
        }
        return variantTemp
    }

    @JvmOverloads
    @JvmStatic
    fun finishSetupBlock(block: Block, name: String, itemForm: ItemBlock?, creativeTab: ModCreativeTab? = null) {
        block.unlocalizedName = name
        if (itemForm == null)
            ModelHandler.registerVariantHolder(block as IVariantHolder)
        creativeTab?.set(block)
    }

    @JvmStatic
    fun setUnlocalizedNameForItem(item: Item, modId: String, name: String) {
        val rl = ResourceLocation(modId, name)
        GameRegistry.register(item, rl)
    }

    @JvmStatic
    fun setUnlocalizedNameForBlock(block: Block, modId: String, name: String, itemForm: ItemBlock?) {
        block.setRegistryName(name)
        GameRegistry.register(block)
        if (itemForm != null)
            GameRegistry.register(itemForm, ResourceLocation(modId, name))
    }

}
