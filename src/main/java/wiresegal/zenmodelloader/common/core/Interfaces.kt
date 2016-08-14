package wiresegal.zenmodelloader.common.core

import net.minecraft.block.Block
import net.minecraft.block.properties.IProperty
import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraft.client.renderer.block.statemap.IStateMapper
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.EnumRarity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.zenmodelloader.common.tab.ModCreativeTab

/**
 * Provides a list of variants to load. The most basic class which can be put into the ModelHandler.
 */
interface IVariantHolder {

    /**
     * The variants which will be loaded as models. In all implementations these should correspond to metadata values of items.
     * If the mesh definition is not null, none of these will be loaded as models. It's recommended in such situations to use IExtraVariantHolder.
     */
    val variants: Array<out String>
}

/**
 * Provides an extra list of variants to load, without adding extra metadata variants.
 */
interface IExtraVariantHolder : IVariantHolder {

    /**
     * Extra variants to load as models without corresponding to metadata.
     */
    val extraVariants: Array<out String>
}

/**
 * Defines a mod item holder. This can be used to wrap in the model handling of ZML without any of the other registration.
 */
interface IModItemProvider : IVariantHolder {

    /**
     * Provides an item instance to use for registration. This does not have to be the same instance as the IModItemProvider.
     */
    val providedItem: Item

    /**
     * Provides a mesh definition which can override the model loaded based on the MRL you pass back. Leave null to use default behavior.
     */
    @SideOnly(Side.CLIENT)
    fun getCustomMeshDefinition(): ItemMeshDefinition? {
        return null
    }
}

interface IModBlockProvider : IVariantHolder {

    /**
     * Provides a block instance to use for registration. This does not have to be the same instance as the IModBlockProvider.
     */
    val providedBlock: Block

    /**
     * Blocks can load item models by providing an enum instead of variants. The variants must still be provided for metadata purposes.
     * This is only relevant if the IModBlockProvider extends IModItemProvider.
     */
    val variantEnum: Class<Enum<*>>?
        get() = null

    /**
     * A list of IProperties to ignore in a blockstate file.
     * If getStateMapper is overridden, this will have to be implemented in the overriden implementation.
     */
    val ignoredProperties: Array<IProperty<*>>?
        get() = null

    /**
     * Provides a statemapper for the block, if needed. By default uses ignored properties only.
     */
    @SideOnly(Side.CLIENT)
    fun getStateMapper(): IStateMapper? {
        val ignored = ignoredProperties
        return if (ignored == null || ignored.isEmpty()) null else StateMap.Builder().ignore(*ignored).build()
    }
}

/**
 * An interface which defines a block which can be used with an ItemModBlock.
 */
interface IModBlock : IModBlockProvider {

    /**
     * The name of the block.
     */
    val bareName: String

    /**
     * The rarity of the block, for the ItemModBlock.
     */
    fun getBlockRarity(stack: ItemStack): EnumRarity {
        return EnumRarity.COMMON
    }

    /**
     * Provides a mesh definition which can override the model loaded based on the MRL you pass back. Leave null to use default behavior.
     */
    @SideOnly(Side.CLIENT)
    fun getCustomMeshDefinition(): ItemMeshDefinition? {
        return null
    }

    /**
     * The tab to register the block in. Leave null to not register.
     */
    val creativeTab: ModCreativeTab?
        get() = null

    /**
     * All IModBlock instances must extend Block.
     */
    override val providedBlock: Block
        get() = this as Block
}

/**
 * An interface which defines an item (or a block with an item) that colorizes the item.
 * For those using kotlin, you can implement the IItemColor as a lambda.
 * If you're using java, you must implement it as an anonymous class.
 */
interface IItemColorProvider : IVariantHolder {
    @SideOnly(Side.CLIENT)
    fun getItemColor(): IItemColor?
}

/**
 * An interface which defines a block (not necessarily with an item) that colorizes the block.
 * For those using kotlin, you can implement the IBlockColor as a lambda.
 * If you're using java, you must implement it as an anonymous class.
 */
interface IBlockColorProvider : IItemColorProvider, IModBlockProvider {
    @SideOnly(Side.CLIENT)
    fun getBlockColor(): IBlockColor?

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor? = null
}
