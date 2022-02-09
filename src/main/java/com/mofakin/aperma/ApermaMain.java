package com.mofakin.aperma;

import com.mofakin.aperma.config.ConfigMain;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(ApermaMain.MODID)
public class ApermaMain {
	
	public static final String MODID = "aperma";
	
	public static final ConfigMain configCommon = new ConfigMain();
	
    	public ApermaMain() {
    		ModLoadingContext.get().registerConfig(Type.COMMON, ApermaMain.configCommon.getSpec());
    	}
}
