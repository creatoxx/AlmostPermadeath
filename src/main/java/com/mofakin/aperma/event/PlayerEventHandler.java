package com.mofakin.aperma.event;

import com.mofakin.aperma.ApermaMain;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Stats;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ApermaMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler {
	
    	private static int initLifes = ApermaMain.configCommon.getInitLifes();
    
	@SubscribeEvent
    	public static void onLoggingIn(PlayerEvent.PlayerLoggedInEvent event) {
		
		PlayerEntity player = event.getPlayer();
		GameType gameType = ((ServerPlayerEntity) player).gameMode.getGameModeForPlayer();
		
		if(gameType.isCreative())
			return;
		
		World level = player.level;
		
    		if(event.getEntity() instanceof ServerPlayerEntity && player != null) {
    		
    			int deaths = ((ServerPlayerEntity) player).getStats().getValue(Stats.CUSTOM.get(Stats.DEATHS));
    			if(initLifes + player.experienceLevel - deaths <= 0) {
    			
    				MinecraftServer server = level.getServer();
    			
    				if(server.isSingleplayer()) {
    				
        				player.setGameMode(GameType.SPECTATOR);
        				level.getGameRules().getRule(GameRules.RULE_SPECTATORSGENERATECHUNKS).set(false, server);  
    				} else {
    			
					player.inventory.clearContent();
					player.experienceLevel = 0;
					player.totalExperience = 0;
					((ServerPlayerEntity) player).getStats().setValue(player, Stats.CUSTOM.get(Stats.DEATHS), 0);
    				}			
    			}
    		}
    	}
	
	@SubscribeEvent
    	public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
		
		PlayerEntity player = event.getPlayer();
		GameType gameType = ((ServerPlayerEntity) player).gameMode.getGameModeForPlayer();
		
		if(gameType.isCreative())
			return;
		
		World level = player.level;
		
		((ServerPlayerEntity) player).getStats().sendStats((ServerPlayerEntity) player);
		
    		if(event.getEntity() instanceof ServerPlayerEntity && player != null) {
    		
    			int deaths = ((ServerPlayerEntity) player).getStats().getValue(Stats.CUSTOM.get(Stats.DEATHS));
    		
    			if(initLifes + player.experienceLevel - deaths <= 0) {
    			
    				MinecraftServer server = level.getServer();
    			
    				if(server.isSingleplayer()) {
    				
        				player.setGameMode(GameType.SPECTATOR);
        				level.getGameRules().getRule(GameRules.RULE_SPECTATORSGENERATECHUNKS).set(false, server);        			
    				} else {
    			
					player.inventory.clearContent();
					player.experienceLevel = 0;
					player.totalExperience = 0;
					((ServerPlayerEntity) player).getStats().setValue(player, Stats.CUSTOM.get(Stats.DEATHS), 0);
    				}	
    			}
    		}
    	}	
	
	@SubscribeEvent
	public static void changeGamerules(EntityJoinWorldEvent event) {
		
		GameRules rules = event.getWorld().getGameRules();
		MinecraftServer server = event.getWorld().getServer();
		
		if(ApermaMain.configCommon.shouldAutoHeal()) {
	
			((GameRules.BooleanValue) rules.getRule(GameRules.RULE_NATURAL_REGENERATION)).set(false, server);
		}
		
		if(ApermaMain.configCommon.shouldKeepInventory()) {
			
			((GameRules.BooleanValue) rules.getRule(GameRules.RULE_KEEPINVENTORY)).set(true, server);
		}		
	}
}
