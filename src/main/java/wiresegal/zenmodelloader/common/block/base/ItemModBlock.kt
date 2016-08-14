package wiresegal.zenmodelloader.common.block.base

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.zenmodelloader.client.core.ModelHandler
import wiresegal.zenmodelloader.common.core.*

/**
 * The default implementation for an IModBlock wrapper Item that gets registered as an IVariantHolder.
 */
open class ItemModBlock(block: Block) : ItemBlock(block), IModItemProvider, IBlockColorProvider {

    private val modBlock: IModBlock
    private val modId: String

    init {
        this.modId = Loader.instance().activeModContainer().modId
        this.modBlock = block as IModBlock
        if (this.variants.size > 1) {
            this.setHasSubtypes(true)
        }
        ModelHandler.addToCache(this)
    }

    override val providedItem: Item
        get() = this

    override val providedBlock: Block
        get() = block

    override fun getMetadata(damage: Int): Int {
        return damage
    }

    override fun setUnlocalizedName(par1Str: String): ItemBlock {
        val rl = ResourceLocation(Loader.instance().activeModContainer().modId, par1Str)
        GameRegistry.register(this, rl)
        return super.setUnlocalizedName(par1Str)
    }

    override fun getUnlocalizedName(stack: ItemStack?): String {
        val dmg = stack!!.itemDamage
        val variants = this.variants
        val name: String
        if (dmg >= variants.size) {
            name = this.modBlock.bareName
        } else {
            name = variants[dmg]
        }

        return "tile.$modId:$name"
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        val variants = this.variants

        for (i in variants.indices) {
            subItems.add(ItemStack(itemIn, 1, i))
        }

    }

    @SideOnly(Side.CLIENT)
    override fun getCustomMeshDefinition() = this.modBlock.getCustomMeshDefinition()

    override val variants: Array<out String>
        get() = this.modBlock.variants

    @SideOnly(Side.CLIENT)
    override fun getItemColor() = if (this.modBlock is IItemColorProvider) this.modBlock.getItemColor() else null

    @SideOnly(Side.CLIENT)
    override fun getBlockColor() = if (this.modBlock is IBlockColorProvider) this.modBlock.getBlockColor() else null

    override fun getRarity(stack: ItemStack): EnumRarity? {
        return this.modBlock.getBlockRarity(stack)
    }
}

