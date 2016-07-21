package wiresegal.zenmodelloader.common.items.base

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Loader
import wiresegal.zenmodelloader.common.core.IVariantHolder
import wiresegal.zenmodelloader.common.core.VariantHelper

/**
 * The default implementation for an IVariantHolder item.
 */
open class ItemMod(name: String, vararg variants: String) : Item(), IVariantHolder {

    override val variants: Array<out String>

    private val bareName: String
    private val modId: String

    init {
        modId = Loader.instance().activeModContainer().modId
        bareName = name
        this.variants = VariantHelper.setupItem(this, name, variants)
    }

    override fun setUnlocalizedName(name: String): Item {
        VariantHelper.setUnlocalizedNameForItem(this, modId, name)
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

