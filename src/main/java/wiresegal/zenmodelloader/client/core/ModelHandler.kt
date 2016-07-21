package wiresegal.zenmodelloader.client.core

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelBakery
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.util.IStringSerializable
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.Loader
import org.apache.logging.log4j.Logger
import wiresegal.zenmodelloader.common.ZenModelLoader
import wiresegal.zenmodelloader.common.core.*
import java.util.*

/**
 * @author WireSegal
 * Created at 2:12 PM on 3/20/16.
 */
object ModelHandler {

    val logger: Logger = ZenModelLoader.LOGGER
    val debug = ZenModelLoader.DEV_ENVIRONMENT
    var modName = ""
    val namePad: String
        get() = modName.padEnd(modName.length)

    val variantCache = HashMap<String, MutableList<IVariantHolder>>()

    /**
     * This is Mod name -> (Variant name -> MRL), specifically for ItemMeshDefinitions.
     */
    val resourceLocations = HashMap<String, HashMap<String, ModelResourceLocation>>()

    /**
     * Use this method to inject your item into the list to be loaded at the end of preinit and colorized at the end of init.
     */
    fun addToCache(holder: IVariantHolder) {
        val name = Loader.instance().activeModContainer()?.modId ?: return
        variantCache.putIfAbsent(name, mutableListOf())
        variantCache[name]?.add(holder)
    }

    private fun addToCachedLocations(name: String, mrl: ModelResourceLocation) {
        resourceLocations.putIfAbsent(modName, hashMapOf())
        resourceLocations[modName]?.put(name, mrl)
    }

    fun preInit() {
        for ((modid, holders) in variantCache) {
            modName = modid
            for (holder in holders.sortedBy { (255 - it.variants.size).toChar() + if (it is ItemBlock) "b" else "I" + if (it is Item) it.registryName.resourcePath else "" }) {
                registerModels(holder)
            }
        }
    }

    fun init() {
        val itemColors = Minecraft.getMinecraft().itemColors
        val blockColors = Minecraft.getMinecraft().blockColors
        for ((modid, holders) in variantCache) {
            for (holder in holders) {
                if (holder is IItemColorProvider && holder is Item) {
                    val color = holder.getItemColor()
                    if (color != null)
                        itemColors.registerItemColorHandler(color, holder)
                }

                if (holder is ItemBlock && holder.getBlock() is IBlockColorProvider) {
                    val color = (holder.getBlock() as IBlockColorProvider).getBlockColor()
                    if (color != null)
                        blockColors.registerBlockColorHandler(color, holder.getBlock())
                } else if (holder is Block && holder is IBlockColorProvider) {
                    val color = holder.getBlockColor()
                    if (color != null)
                        blockColors.registerBlockColorHandler(color, holder)
                }
            }
        }
    }

    // The following is a blatant copy of Psi's ModelHandler with minor changes for various purposes.

    fun registerModels(holder: IVariantHolder) {
        val def = holder.getCustomMeshDefinition()
        if (def != null && holder is Item) {
            ModelLoader.setCustomMeshDefinition(holder, def)
        } else {
            registerModels(holder, holder.variants, false)
        }
        if (holder is IExtraVariantHolder) {
            registerModels(holder, holder.extraVariants, true)
        }
    }

    fun registerModels(item: IVariantHolder, variants: Array<out String>, extra: Boolean) {
        if (item is ItemBlock && item.getBlock() is IModBlock) {
            val i = item.getBlock() as IModBlock
            val name = i.variantEnum
            val loc = i.ignoredProperties
            if (loc != null && loc.size > 0) {
                val builder = StateMap.Builder()
                val var7 = loc
                val var8 = loc.size

                for (var9 in 0..var8 - 1) {
                    val p = var7[var9]
                    builder.ignore(p)
                }

                ModelLoader.setCustomStateMapper(i as Block, builder.build())
            }

            if (name != null) {
                registerVariantsDefaulted(item, i as Block, name, "variant")
                return
            }
        } else if (item is IModBlock) {
            val loc = item.ignoredProperties
            if (loc != null && loc.size > 0) {
                val builder = StateMap.Builder()
                val var7 = loc
                val var8 = loc.size

                for (var9 in 0..var8 - 1) {
                    val p = var7[var9]
                    builder.ignore(p)
                }

                ModelLoader.setCustomStateMapper(item as Block, builder.build())
            }
        }

        if (item is Item) {
            for (variant in variants.withIndex()) {
                if (variant.index == 0) {
                    var print = "$namePad | Registering "
                    if (variant.value != item.registryName.resourcePath || variants.size != 1)
                        print += "variant" + if (variants.size == 1) "" else "s" + " of "
                    print += if (item is ItemBlock) "block" else "item"
                    print += " ${item.registryName.resourcePath}"
                    log(print)
                }
                if ((variant.value != item.registryName.resourcePath || variants.size != 1)) {
                    log("$namePad |  Variant #${variant.index + 1}: ${variant.value}")
                }

                val model = ModelResourceLocation(ResourceLocation(modName, variant.value).toString(), "inventory")
                if (!extra) {
                    ModelLoader.setCustomModelResourceLocation(item, variant.index, model)
                    addToCachedLocations(getKey(item, variant.index), model)
                } else {
                    ModelBakery.registerItemVariants(item, model)
                    addToCachedLocations(variant.value, model)
                }
            }
        }

    }

    private fun registerVariantsDefaulted(item: Item, block: Block, enumclazz: Class<*>, variantHeader: String) {
        val locName = Block.REGISTRY.getNameForObject(block).toString()
        if (enumclazz.enumConstants != null)
            for (e in enumclazz.enumConstants) {
                if (e is IStringSerializable && e is Enum<*>) {
                    val variantName = variantHeader + "=" + e.name

                    if (e.ordinal == 0) {
                        var print = "$namePad | Registering "
                        if (variantName != item.registryName.resourcePath || enumclazz.enumConstants.size != 1)
                            print += "variant" + (if (enumclazz.enumConstants.size == 1) "" else "s") + " of "
                        print += if (item is ItemBlock) "block" else "item"
                        print += " " + item.registryName.resourcePath
                        log(print)
                    }
                    if (e.name != item.registryName.resourcePath || enumclazz.enumConstants.size != 1) {
                        log("$namePad |  Variant #${e.ordinal + 1}: $variantName")
                    }

                    val loc = ModelResourceLocation(locName, variantName)
                    val i = e.ordinal
                    ModelLoader.setCustomModelResourceLocation(item, i, loc)
                    addToCachedLocations(getKey(item, i), loc)
                }
            }

    }

    private fun getKey(item: Item, meta: Int): String {
        return "i_" + item.registryName + "@" + meta
    }

    fun log(text: String) {
        if (debug) logger.info(text)
    }
}
