package com.mofakin.aperma.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ConfigMain {
	
    private final ForgeConfigSpec spec;
    
    private final BooleanValue keepInventory;
    private final BooleanValue naturalRegeneration;
    private final IntValue initialLifes;
    private final IntValue levelDelay;
    
    public ConfigMain() {
        
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Almost Permadeath General Settings");
        builder.push("general");
        
        builder.comment("Do you want to keep inventory after dying?");
        this.keepInventory = builder.define("Keep Inventory on Death", true);
        builder.comment("Do you want to disable automatic health regeneration?");
        this.naturalRegeneration = builder.define("Disable Automatic Health Regeneration", true);
        builder.comment("How much initial lifes do you want to have?");
        this.initialLifes = builder.defineInRange("Initial Lifes(0-100, default: 3)", 3, 0, 100);
        builder.comment("Delay step of getting lifes per level? Set to 1 if you want a life on every level up!");
        this.levelDelay = builder.defineInRange("Get a life every(0-100, default: 3) level(s)", 3, 0, 100);
        
        builder.pop();
        this.spec = builder.build();
    }
    
    public ForgeConfigSpec getSpec() {
        
        return this.spec;
    }
    
    public boolean shouldKeepInventory() {
        
        return this.keepInventory.get();
    }
    
    public boolean shouldAutoHeal() {
        
        return this.naturalRegeneration.get();
    }
    
    public int getInitLifes() {
        
        return this.initialLifes.get();
    }
    
    public int getLevelDelay() {
        
        return this.levelDelay.get();
    }
}