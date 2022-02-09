package com.mofakin.aperma.event;

import com.mofakin.aperma.ApermaMain;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.StatsScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ApermaMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class RenderEventHandler {
	
	public static final ResourceLocation FRAME_LOCATION = new ResourceLocation("textures/gui/widgets.png");
	
	public static int lifes;
	private static int deaths;
	protected static int screenHeight;
	protected static int screenWidth;
	
	private static boolean doOnce = false;

	@SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Post event) {
		
		Minecraft minecraft = Minecraft.getInstance();
		
		if(doOnce == false) {
			
			IngameMenuScreen menu = new IngameMenuScreen(false);
			minecraft.setScreen(new StatsScreen(menu, minecraft.player.getStats()));
			minecraft.setScreen((Screen)null);
			doOnce = true;
		}
		
		ElementType elementType = event.getType();
		
		if (elementType == ElementType.ALL) {
			
			GameType gameType = minecraft.gameMode.getPlayerMode();
			ClientPlayerEntity player = minecraft.player;
			
			if(gameType.isCreative())
				return;
			
			if (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR && !minecraft.options.hideGui) {
				
				MainWindow main = minecraft.getWindow();
				screenHeight = main.getGuiScaledHeight();
				screenWidth = main.getGuiScaledWidth();
				
				FontRenderer font = minecraft.font;
				String deathSuffered = "" + deaths;
				String lifesLeft = "" + lifes;
		        int i1 = (screenWidth - font.width(lifesLeft)) / 2 + 95;
		        int j1 = screenHeight - 30;
		        if(ApermaMain.configCommon.getInitLifes() + player.experienceLevel - deaths > 0) {
		        	deaths = player.getStats().getValue(Stats.CUSTOM.get(Stats.DEATHS));
			        lifes = ApermaMain.configCommon.getInitLifes() + player.experienceLevel - deaths;
		        } else {
		        	lifes = ApermaMain.configCommon.getInitLifes();
		        	player.getStats().setValue(player, Stats.CUSTOM.get(Stats.DEATHS), 0);
		        	deaths = 0;
		        }

				if(deaths != 0) {
					
					MatrixStack matrixStack = event.getMatrixStack();
					
					SkeletonEntity skeleton = EntityType.SKELETON.create(minecraft.level);
			        
					matrixStack.pushPose();
					
					minecraft.getTextureManager().bind(FRAME_LOCATION);
			        AbstractGui gui = new IngameGui(minecraft);
			        gui.blit(matrixStack, i1 + 5, j1, 60, 23, 82, 22);
					
					InventoryScreen.renderEntityInInventory(i1 + 16, j1 + 17, 7, (float)(0), (float) (0), skeleton);
					
					matrixStack.translate(0, 0, 100);
					
			        font.draw(matrixStack, deathSuffered, i1 + 5.0F, j1, 0);
			        font.draw(matrixStack, deathSuffered, i1 + 3.0F, j1, 0);
			        font.draw(matrixStack, deathSuffered, i1 + 4.0F, j1 + 1.0F, 0);
			        font.draw(matrixStack, deathSuffered, i1 + 4.0F, j1 - 1.0F, 0);
			        font.draw(matrixStack, deathSuffered, i1 + 4.0F, j1, -57297);

					matrixStack.popPose();
				}
		        
				if(lifes != 0) {
					
					MatrixStack matrixStack = event.getMatrixStack();
					PlayerEntity playerentity = !(minecraft.getCameraEntity() instanceof PlayerEntity) ? null : (PlayerEntity)minecraft.getCameraEntity();
			        ItemStack itemstack = playerentity.getOffhandItem();
			        
			        int i2 = 0;
			        int j2 = 0;
			        
			        if (!itemstack.isEmpty()) {
			        	i2 = (screenWidth - font.width(deathSuffered)) / 2 - 94;
			        	j2 = j1 - 15;
			        } else {
		        		i2 = (screenWidth - font.width(deathSuffered)) / 2 - 94;
		        		j2 = j1 - 1;
		        	}
			        
					matrixStack.pushPose();
					
			        minecraft.getTextureManager().bind(FRAME_LOCATION);
			        AbstractGui gui2 = new IngameGui(minecraft);
			        gui2.blit(matrixStack, i2 - 23, j2 + 1, 60, 23, 82, 22);
			        
					InventoryScreen.renderEntityInInventory(i2 - 12, j2 + 18, 7, (float)(0), (float) (0), minecraft.player);
					
					matrixStack.translate(0, 0, 100);
					
			        font.draw(matrixStack, lifesLeft, i2 - 3.5F, j2 + 1.0F, 0);
			        font.draw(matrixStack, lifesLeft, i2 - 5.5F, j2 + 1.0F, 0);
			        font.draw(matrixStack, lifesLeft, i2 - 4.5F, j2 + 2.0F, 0);
			        font.draw(matrixStack, lifesLeft, i2 - 4.5F, j2, 0);
			        font.draw(matrixStack, lifesLeft, i2 - 4.5F, j2 + 1.0F, -14614544);
			        
			        String initialLifes = "" + ApermaMain.configCommon.getInitLifes(); 
			        
			        font.draw(matrixStack, initialLifes, i2 - 4.5F, j2 + 14.5F, 0);
			        font.draw(matrixStack, initialLifes, i2 - 5.5F, j2 + 14.5F, 0);
			        font.draw(matrixStack, initialLifes, i2 - 4.5F, j2 + 15.5F, 0);
			        font.draw(matrixStack, initialLifes, i2 - 4.5F, j2 + 13.5F, 0);
			        font.draw(matrixStack, initialLifes, i2 - 4.5F, j2 + 14.5F, -14614544);

					matrixStack.popPose();
				}
			}
		}
    }
}