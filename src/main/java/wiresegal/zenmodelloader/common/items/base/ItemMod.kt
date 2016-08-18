package wiresegal.zenmodelloader.common.items.base

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Loader
import wiresegal.zenmodelloader.common.core.IModItemProvider
import wiresegal.zenmodelloader.common.core.VariantHelper
import wiresegal.zenmodelloader.common.tab.ModCreativeTab

/**
 * The default implementation for an IVariantHolder item.
 */
open class ItemMod(name: String, vararg variants: String) : Item(), IModItemProvider {

    override val providedItem: Item
        get() = this

    override val variants: Array<out String>

    private val bareName: String
    private val modId: String

    init {
        modId = Loader.instance().activeModContainer().modId
        bareName = name
        this.variants = VariantHelper.setupItem(this, name, variants, creativeTab)
    }

    override fun setUnlocalizedName(name: String): Item {
        VariantHelper.setUnlocalizedNameForItem(this, modId, name)
        return super.setUnlocalizedName(name)
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        val dmg = stack.itemDamage
        val variants = this.variants
        val name = if (dmg >= variants.size) this.bareName else variants[dmg]

        return "item.$modId:$name"
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for (i in 0..this.variants.size - 1)
            subItems.add(ItemStack(itemIn, 1, i))
    }

    open val creativeTab: ModCreativeTab?
        get() = null
}

