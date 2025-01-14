/*
 *
 * ReactorGameData.java
 *
 * This file is part of Extreme Reactors 2 by ZeroNoRyouki, a Minecraft mod.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * DO NOT REMOVE OR EDIT THIS HEADER
 *
 */

package it.zerono.mods.extremereactors.gamecontent;

import it.zerono.mods.extremereactors.api.coolant.FluidMappingsRegistry;
import it.zerono.mods.extremereactors.api.coolant.FluidsRegistry;
import it.zerono.mods.extremereactors.api.coolant.TransitionsRegistry;
import it.zerono.mods.extremereactors.api.reactor.*;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

final class ReactorGameData {

    static void register() {

        registerReactants();
        registerReactantMappings();
        registerReactions();
        registerModerators();

        // register coolants and vapors
        registerCoolantsAndVapors();
    }

    //region internals

    private static void registerReactants() {

        // Yellorium (register it as a FuelProperties.DEFAULT fuel)
        ReactantsRegistry.register(REACTANT_YELLORIUM, ReactantType.Fuel, 0xc6ba54, "reactant.bigreactors.yellorium");

        // Blutonium
        ReactantsRegistry.registerFuel(REACTANT_BLUTONIUM, 0x17179c, 2.55f, 0.875f, 2.0f, "reactant.bigreactors.blutonium");

        // Cyanite
        ReactantsRegistry.register(REACTANT_CYANITE, ReactantType.Waste, 0x5387b7, "reactant.bigreactors.cyanite");

        // Magentite
        ReactantsRegistry.register(REACTANT_MAGENTITE, ReactantType.Waste, 0xe41de4, "reactant.bigreactors.magentite");
    }

    private static void registerReactantMappings() {

        registerIngotReactantMapping(REACTANT_YELLORIUM, ContentTags.Items.INGOTS_YELLORIUM, 1);
        registerIngotReactantMapping(REACTANT_YELLORIUM, "forge:ingots/uranium", 1);
        registerIngotReactantMapping(REACTANT_YELLORIUM, ContentTags.Items.BLOCKS_YELLORIUM, 9);
        registerIngotReactantMapping(REACTANT_YELLORIUM, "forge:storage_blocks/uranium", 9);

        registerIngotReactantMapping(REACTANT_BLUTONIUM, ContentTags.Items.INGOTS_BLUTONIUM, 1);
        registerIngotReactantMapping(REACTANT_BLUTONIUM, "forge:ingots/plutonium", 1);
        registerIngotReactantMapping(REACTANT_BLUTONIUM, ContentTags.Items.BLOCKS_BLUTONIUM, 9);
        registerIngotReactantMapping(REACTANT_BLUTONIUM, "forge:storage_blocks/plutonium", 9);

        registerIngotReactantMapping(REACTANT_CYANITE, ContentTags.Items.INGOTS_CYANITE, 1);
        registerIngotReactantMapping(REACTANT_CYANITE, ContentTags.Items.BLOCKS_CYANITE, 9);
        registerIngotReactantMapping(REACTANT_MAGENTITE, ContentTags.Items.INGOTS_MAGENTITE, 1);
        registerIngotReactantMapping(REACTANT_MAGENTITE, ContentTags.Items.BLOCKS_MAGENTITE, 9);
    }

    private static void registerReactions() {

        ReactionsRegistry.register(REACTANT_YELLORIUM, REACTANT_CYANITE, Reaction.STANDARD_REACTIVITY, Reaction.STANDARD_FISSIONRATE);
        ReactionsRegistry.register(REACTANT_BLUTONIUM, REACTANT_MAGENTITE, 1.1f, 0.05f);
    }

    private static void registerModerators() {

        ModeratorsRegistry.registerSolid("forge:storage_block/apatite", 0.48f, 0.73f, 1.30f, IHeatEntity.CONDUCTIVITY_STONE);
        ModeratorsRegistry.registerSolid("forge:storage_block/cinnabar", 0.48f, 0.75f, 1.32f, IHeatEntity.CONDUCTIVITY_STONE);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/iron", 0.50f, 0.75f, 1.40f, IHeatEntity.CONDUCTIVITY_IRON);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/manasteel", 0.60f, 0.75f, 1.50f, IHeatEntity.CONDUCTIVITY_IRON);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/elementium", 0.61f, 0.77f, 1.52f, IHeatEntity.CONDUCTIVITY_EMERALD);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/nickel", 0.51f, 0.77f, 1.40f, IHeatEntity.CONDUCTIVITY_IRON);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/gold", 0.52f, 0.80f, 1.45f, IHeatEntity.CONDUCTIVITY_GOLD);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/diamond", 0.55f, 0.85f, 1.50f, IHeatEntity.CONDUCTIVITY_DIAMOND);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/netherite", 0.55f, 0.95f, 1.65f, IHeatEntity.CONDUCTIVITY_DIAMOND);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/terrasteel", 0.57f, 0.87f, 1.52f, IHeatEntity.CONDUCTIVITY_DIAMOND);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/emerald", 0.55f, 0.85f, 1.50f, IHeatEntity.CONDUCTIVITY_EMERALD);
        ModeratorsRegistry.registerSolid("forge:glass/colorless", 0.20f, 0.25f, 1.10f, IHeatEntity.CONDUCTIVITY_GLASS);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/copper", 0.50f, 0.75f, 1.40f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/brass", 0.52f, 0.78f, 1.42f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/osmium", 0.51f, 0.77f, 1.41f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/refined_obsidian", 0.53f, 0.79f, 1.42f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/refined_glowstone", 0.54f, 0.79f, 1.44f, IHeatEntity.CONDUCTIVITY_EMERALD);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/bronze", 0.51f, 0.77f, 1.41f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/zinc", 0.51f, 0.77f, 1.41f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/aluminum",0.50f, 0.78f, 1.42f, IHeatEntity.CONDUCTIVITY_IRON);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/steel", 0.50f, 0.78f, 1.42f, IHeatEntity.CONDUCTIVITY_IRON);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/invar", 0.50f, 0.79f, 1.43f, IHeatEntity.CONDUCTIVITY_IRON);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/tin", 0.50f, 0.73f, 1.38f, IHeatEntity.CONDUCTIVITY_SILVER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/silver", 0.51f, 0.79f, 1.43f, IHeatEntity.CONDUCTIVITY_SILVER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/signalum", 0.51f, 0.75f, 1.42f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/lumium", 0.51f, 0.79f, 1.45f, IHeatEntity.CONDUCTIVITY_SILVER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/lead", 0.75f, 0.75f, 1.75f, IHeatEntity.CONDUCTIVITY_SILVER);
        ModeratorsRegistry.registerSolid("forge:storage_blocks/electrum", 0.53f, 0.82f, 1.47f, 2.2f); // Between gold and emerald
        //blockElectrumFlux
        ModeratorsRegistry.registerSolid("forge:storage_blocks/platinum", 0.57f, 0.86f, 1.58f, IHeatEntity.CONDUCTIVITY_EMERALD);
        //blockShiny
        ModeratorsRegistry.registerSolid("forge:storage_blocks/enderium", 0.60f, 0.88f, 1.60f, IHeatEntity.CONDUCTIVITY_DIAMOND);
        //blockTitanium
        //blockDraconium
        //blockDraconiumAwakened
        ModeratorsRegistry.registerSolid("forge:storage_blocks/graphite", 0.10f, 0.50f, 2.00f, IHeatEntity.CONDUCTIVITY_GOLD);
        ModeratorsRegistry.registerSolid("minecraft:ice", 0.33f, 0.33f, 1.15f, IHeatEntity.CONDUCTIVITY_WATER);
        ModeratorsRegistry.registerSolid("bigreactors:dry_ice", 0.42f, 0.52f, 1.32f, IHeatEntity.CONDUCTIVITY_WATER);

        // fluids

        ModeratorsRegistry.registerFluid("minecraft:water", Moderator.WATER.getAbsorption(), Moderator.WATER.getHeatEfficiency(), Moderator.WATER.getModeration(), Moderator.WATER.getHeatConductivity());
        ModeratorsRegistry.registerFluid("minecraft:flowing_water", Moderator.WATER.getAbsorption(), Moderator.WATER.getHeatEfficiency(), Moderator.WATER.getModeration(), Moderator.WATER.getHeatConductivity());

        // Astral Sorcery
        ModeratorsRegistry.registerFluid("astralsorcery:liquid_starlight", 0.92f, 0.80f, 2.00f, IHeatEntity.CONDUCTIVITY_DIAMOND);
        ModeratorsRegistry.registerFluid("astralsorcery:liquid_starlight_flowing", 0.80f, 0.85f, 2.00f, IHeatEntity.CONDUCTIVITY_DIAMOND);

        // Blood Magic
        ModeratorsRegistry.registerFluid("bloodmagic:life_essence_fluid", 0.80f, 0.55f, 1.75f, IHeatEntity.CONDUCTIVITY_EMERALD);
        ModeratorsRegistry.registerFluid("bloodmagic:life_essence_fluid_flowing", 0.70f, 0.55f, 1.75f, IHeatEntity.CONDUCTIVITY_EMERALD);

        // Mekanism
        ModeratorsRegistry.registerFluid("mekanism:hydrofluoric_acid", 0.68f, 0.45f, 1.40f, IHeatEntity.CONDUCTIVITY_EMERALD);
        ModeratorsRegistry.registerFluid("mekanism:flowing_hydrofluoric_acid", 0.60f, 0.45f, 1.40f, IHeatEntity.CONDUCTIVITY_EMERALD);
        ModeratorsRegistry.registerFluid("mekanism:sodium", 0.28f, 0.60f, 1.70f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerFluid("mekanism:flowing_sodium", 0.23f, 0.60f, 1.70f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerFluid("mekanism:hydrogen_chloride", 0.38f, 0.65f, 1.70f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerFluid("mekanism:flowing_hydrogen_chloride", 0.31f, 0.65f, 1.70f, IHeatEntity.CONDUCTIVITY_COPPER);
        ModeratorsRegistry.registerFluid("mekanism:ethene", 0.45f, 0.65f, 1.90f, IHeatEntity.CONDUCTIVITY_SILVER); // Etilene
        ModeratorsRegistry.registerFluid("mekanism:flowing_ethene", 0.37f, 0.65f, 1.90f, IHeatEntity.CONDUCTIVITY_SILVER); // Etilene

        /*

        ReactorInterior.registerFluid("redstone", 0.75f, 0.55f, 1.60f, IHeatEntity.conductivityEmerald); // Thermal Foundation Destabilized Redstone
        ReactorInterior.registerFluid("liquidredstone", 0.65f, 0.45f, 1.50f, IHeatEntity.conductivityEmerald); // Substratum Liquid Redstone (toned down because a bit cheap on the crafting side)

        ReactorInterior.registerFluid("fluidtesla", 0.75f, 0.55f, 1.60f, IHeatEntity.conductivityEmerald); // Modularity

        ReactorInterior.registerFluid("glowstone", 0.20f, 0.60f, 1.75f, IHeatEntity.conductivityCopper); // Thermal Foundation Energised Glowstone
        ReactorInterior.registerFluid("liquidglowstone", 0.18f, 0.50f, 1.55f, IHeatEntity.conductivityCopper); // Substratum Liquid Glowstone (toned down because a bit cheap on the crafting side)

        ReactorInterior.registerFluid("cryotheum", 0.66f, 0.95f, 6.00f, IHeatEntity.conductivityDiamond); // Thermal Foundation Gelid Cryotheum: an amazing moderator!

        ReactorInterior.registerFluid("ender", 0.90f, 0.75f, 2.00f, IHeatEntity.conductivityGold); // Thermal Foundation Resonant Ender
        ReactorInterior.registerFluid("liquidenderpearl", 0.75f, 0.60f, 1.80f, IHeatEntity.conductivityGold); // Substratum Liquid Ender Pearl (toned down because a bit cheap on the crafting side)

        ReactorInterior.registerFluid("pyrotheum", 0.66f, 0.90f, 1.00f, IHeatEntity.conductivityIron); // Thermal Foundation Blazing Pyrotheum

        if (Config.SERVER.general.enableComedy.get()) {

            ReactorInterior.registerFluid("blueslime", 0.33f, 0.50f, 1.35f, IHeatEntity.conductivityGold); // From Tinker's Construct
            ReactorInterior.registerFluid("purpleslime", 0.56f, 0.75f, 1.65f, IHeatEntity.conductivityGold); // From Tinker's Construct
            ReactorInterior.registerFluid("meat", 0.40f, 0.60f, 1.33f, IHeatEntity.conductivityStone);
            ReactorInterior.registerFluid("pinkSlime", 0.45f, 0.70f, 1.50f, IHeatEntity.conductivityIron);
            ReactorInterior.registerFluid("sewage", 0.50f, 0.65f, 1.44f, IHeatEntity.conductivityIron);
        }
         */

//        ReactorInterior.registerBlock("blockSnow", 0.15f, 0.33f, 1.05f, IHeatEntity.conductivityWater / 2f);
//        ReactorInterior.registerBlock("blockElectrumFlux", 0.54f, 0.83f, 1.48f, 2.4f); // Between gold and emerald
//        ReactorInterior.registerBlock("blockShiny", 0.57f, 0.86f, 1.58f, IHeatEntity.conductivityEmerald); // Thermal Foundation Shiny block
//        ReactorInterior.registerBlock("blockTitanium", 0.58f, 0.87f, 1.59f, 2.7f); // Mariculture
//        ReactorInterior.registerBlock("blockDraconium", 0.59f, 0.88f, 1.60f, 3.3f);
//        ReactorInterior.registerBlock("blockDraconiumAwakened", 0.76f, 0.88f, 1.78f, 4.8f);
//            ReactorInterior.registerBlock("blockMithril", 0.53f, 0.81f, 1.45f, IHeatEntity.conductivitySilver);
//            ReactorInterior.registerBlock("blockOrichalcum", 0.52f, 0.83f, 1.46f, 1.7f);    // Between silver and gold
//            ReactorInterior.registerBlock("blockQuicksilver", 0.53f, 0.84f, 1.48f, IHeatEntity.conductivityGold);
//            ReactorInterior.registerBlock("blockHaderoth", 0.54f, 0.84f, 1.49f, IHeatEntity.conductivityEmerald);
//            ReactorInterior.registerBlock("blockCelenegil", 0.54f, 0.84f, 1.49f, IHeatEntity.conductivityDiamond);
//            ReactorInterior.registerBlock("blockTartarite", 0.65f, 0.90f, 1.62f, 4f); // Between diamond and graphene
//            ReactorInterior.registerBlock("blockManyullyn", 0.68f, 0.88f, 1.75f, 4.5f);

    }

    private static void registerCoolantsAndVapors() {

        // water -> steam (and vice versa)

        FluidsRegistry.registerCoolant("water", 100.0f,
                4.0f, // Thermal Expansion converts 1 mB steam into 2 RF of work in a steam turbine, so we assume it's 50% efficient.
                "fluid.bigreactors.water");
        FluidMappingsRegistry.registerCoolantMapping("water", 1, ContentTags.Fluids.WATER);

        FluidsRegistry.registerVapor("steam", 10.0f, "fluid.bigreactors.steam");
        FluidMappingsRegistry.registerVaporMapping("steam", 1, ContentTags.Fluids.STEAM);

        TransitionsRegistry.register("water", "steam");
    }

    //region helpers

    private static void registerIngotReactantMapping(final String reactantName, final ITag.INamedTag<Item> tag, final int ingotsCount) {
        registerIngotReactantMapping(reactantName, tag.getName(), ingotsCount);
    }

    private static void registerIngotReactantMapping(final String reactantName, final String itemTagId, final int ingotsCount) {
        registerIngotReactantMapping(reactantName, new ResourceLocation(itemTagId), ingotsCount);
    }

    private static void registerIngotReactantMapping(final String reactantName, final ResourceLocation itemTagId, final int ingotsCount) {
        ReactantMappingsRegistry.registerSolid(reactantName, ingotsCount * ReactantMappingsRegistry.STANDARD_SOLID_REACTANT_AMOUNT, itemTagId);
    }

    //endregion

    private static final String REACTANT_YELLORIUM = "yellorium";
    private static final String REACTANT_BLUTONIUM = "blutonium";
    private static final String REACTANT_CYANITE = "cyanite";
    private static final String REACTANT_MAGENTITE = "magentite";

    //endregion
}
