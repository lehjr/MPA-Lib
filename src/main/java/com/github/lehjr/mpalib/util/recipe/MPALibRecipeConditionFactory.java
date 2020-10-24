package com.github.lehjr.mpalib.util.recipe;

import com.github.lehjr.mpalib.basemod.MPALibConstants;
import com.github.lehjr.mpalib.config.MPALibSettings;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class MPALibRecipeConditionFactory implements ICondition {
    static final ResourceLocation NAME = new ResourceLocation(MPALibConstants.MOD_ID, "flag");

    String conditionName;

    public MPALibRecipeConditionFactory(String conditionName) {
        this.conditionName = conditionName;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        switch (conditionName) {
            // Vanilla reciples only as fallback
            case "vanilla_recipes_enabled": {
                return (MPALibSettings.useVanillaRecipes());
            }

            case "vanilla_recipes_disabled": {
                return (!MPALibSettings.useVanillaRecipes());
            }
        }
        return false;
    }

    public static class Serializer implements IConditionSerializer<MPALibRecipeConditionFactory> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, MPALibRecipeConditionFactory value) {
            // Don't think anything else needs to be added here, as this is now working
//            System.out.println("json: " + json.toString());
//            System.out.println("value: " + value.conditionName);
//            json.addProperty("condition", value.conditionName);
        }

        @Override
        public MPALibRecipeConditionFactory read(JsonObject json) {
            return new MPALibRecipeConditionFactory(JSONUtils.getString(json, "flag"));
        }

        @Override
        public ResourceLocation getID() {
            return MPALibRecipeConditionFactory.NAME;
        }
    }
}