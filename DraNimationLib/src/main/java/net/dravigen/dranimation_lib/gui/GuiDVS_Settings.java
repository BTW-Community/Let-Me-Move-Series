package net.dravigen.dranimation_lib.gui;

import net.dravigen.dranimation_lib.settings.DVS_ConfigManager;
import net.dravigen.dranimation_lib.settings.BaseSetting;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.I18n;
import net.minecraft.src.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class GuiDVS_Settings extends GuiScreen {
	private static final int HEADER_HEIGHT = 30;
	private static final int ITEM_HEIGHT = 22;
	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 20;
	private static final int SCROLLBAR_WIDTH = 6;
	private static final int SCROLL_AREA_PADDING = 8;
	private static final int SECTION_HEADER_HEIGHT = 16;
	
	public static final int buttonIDStart = 1000;
	
	private final DVS_ConfigManager settingsManager;
	private final GuiScreen parentScreen;
	
	private final TreeMap<String, List<BaseSetting>> categorizedSettings;
	private final List<BaseSetting> settingList;
	
	private float scrollOffset = 0.0F;
	private int lastMouseY;
	private boolean isScrolling = false;
	
	public GuiDVS_Settings(GuiScreen parent, DVS_ConfigManager manager) {
		this.parentScreen = parent;
		this.settingsManager = manager;
		this.settingList = settingsManager.getSettings();
		
		this.categorizedSettings = new TreeMap<>();
		
		for (BaseSetting setting : this.settingList) {
			String category = setting.category() == null ? "General" : setting.category();
			this.categorizedSettings.computeIfAbsent(category, k -> new ArrayList<>()).add(setting);
		}
	}
	
	private void handleSettingInteraction(int buttonId) {
		int settingIndex = (buttonId - buttonIDStart) / 2;
		int actionType = (buttonId - buttonIDStart) % 2;
		
		if (settingIndex >= 0 && settingIndex < settingList.size()) {
			BaseSetting setting = settingList.get(settingIndex);
			
			if (setting.type() == DVS_ConfigManager.Type.BOOLEAN) {
				boolean currentValue = settingsManager.getBoolean(setting.id());
				settingsManager.setValue(setting.id(), !currentValue);
			}
			else if (setting.type() == DVS_ConfigManager.Type.DOUBLE) {
				double currentValue = settingsManager.getDouble(setting.id());
				
				double delta = (actionType == 0) ? -0.1 : 0.1;
				double modifier = 1.0;
				
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) modifier = 5.0;
				else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) modifier = 0.5;
				
				double newValue = currentValue + (delta * modifier);
				
				newValue = Math.max(newValue, setting.min());
				newValue = Math.min(newValue, setting.max());
				
				settingsManager.setValue(setting.id(), newValue);
			}
			else if (setting.type() == DVS_ConfigManager.Type.INT) {
				int currentValue = settingsManager.getInt(setting.id());
				
				int delta = (actionType == 0) ? -1 : 1;
				int modifier = 1;
				
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) modifier = 5;
				
				int newValue = currentValue + (delta * modifier);
				
				newValue = (int) Math.max(newValue, setting.min());
				newValue = (int) Math.min(newValue, setting.max());
				
				settingsManager.setValue(setting.id(), newValue);
			}
		}
	}
	
	private void applyClipping(int x, int y, int width, int height) {
		if (width <= 0 || height <= 0) return;
		
		ScaledResolution sr = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int scale = sr.getScaleFactor();
		
		int nativeX = x * scale;
		int nativeY = this.mc.displayHeight - (y + height) * scale;
		int nativeWidth = width * scale;
		int nativeHeight = height * scale;
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(nativeX, nativeY, nativeWidth, nativeHeight);
	}
	
	private void clearClipping() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		String hoveredDescription = null;
		
		this.drawCenteredString(this.fontRenderer, "Dynamic Visuals Series Settings", this.width / 2, 10, 0xFFFFFF);
		
		int scrollXStart = this.width / 2 - 150;
		int scrollXEnd = this.width / 2 + 150;
		int scrollYStart = HEADER_HEIGHT;
		int scrollYEnd = this.height - 40;
		int scrollHeight = scrollYEnd - scrollYStart;
		
		int numCategories = categorizedSettings.size();
		int contentHeight = (settingList.size() * ITEM_HEIGHT) + (numCategories * SECTION_HEADER_HEIGHT);
		
		float maxScroll = Math.max(0, contentHeight - scrollHeight);
		int currentScrollY = (int) (scrollOffset * maxScroll);
		
		// 1. Draw list background
		drawRect(scrollXStart - SCROLL_AREA_PADDING,
				 scrollYStart - SCROLL_AREA_PADDING,
				 scrollXEnd + SCROLLBAR_WIDTH + SCROLL_AREA_PADDING,
				 scrollYEnd + SCROLL_AREA_PADDING,
				 0xAA000000);
		
		// 2. Clip
		this.applyClipping(scrollXStart,
						   scrollYStart,
						   scrollXEnd - scrollXStart + SCROLLBAR_WIDTH,
						   scrollYEnd - scrollYStart);
		
		// 3. Render Items
		{
			int itemY = scrollYStart - currentScrollY;
			this.buttonList.clear();
			
			for (Map.Entry<String, List<BaseSetting>> categoryEntry : categorizedSettings.entrySet()) {
				String categoryName = categoryEntry.getKey();
				List<BaseSetting> settings = categoryEntry.getValue();
				
				// Draw Category Header
				if (itemY + SECTION_HEADER_HEIGHT > scrollYStart && itemY < scrollYEnd) {
					this.drawCenteredString(this.fontRenderer,
											categoryName,
											scrollXStart + (scrollXEnd - scrollXStart) / 2,
											itemY + 4,
											0xFFAA00);
				}
				itemY += SECTION_HEADER_HEIGHT;
				
				for (BaseSetting setting : settings) {
					int originalIndex = settingList.indexOf(setting);
					
					if (itemY + ITEM_HEIGHT > scrollYStart && itemY < scrollYEnd) {
						int itemMouseYStart = Math.max(scrollYStart, itemY);
						int itemMouseYEnd = Math.min(scrollYEnd, itemY + ITEM_HEIGHT);
						
						if (mouseX >= scrollXStart && mouseX <= scrollXEnd &&
								mouseY >= itemMouseYStart && mouseY <= itemMouseYEnd) {
							hoveredDescription = setting.description();
						}
						
						this.fontRenderer.drawString(setting.name(), scrollXStart + 2, itemY + 6, 0xFFFFFF);
						
						int controlX = scrollXEnd - 10;
						int controlY = itemY + (ITEM_HEIGHT - BUTTON_HEIGHT) / 2;
						
						if (setting.type() == DVS_ConfigManager.Type.BOOLEAN) {
							boolean val = settingsManager.getBoolean(setting.id());
							String buttonText = val ? "True" : "False";
							
							GuiButton toggleButton = new GuiButton(buttonIDStart + (originalIndex * 2),
																   controlX - BUTTON_WIDTH - 2,
																   controlY,
																   BUTTON_WIDTH + 2,
																   BUTTON_HEIGHT,
																   buttonText);
							toggleButton.drawButton(this.mc, mouseX, mouseY);
							this.buttonList.add(toggleButton);
						}
						else if (setting.type() == DVS_ConfigManager.Type.DOUBLE) {
							double val = settingsManager.getDouble(setting.id());
							String valText = String.format("%.2f", val);
							
							this.drawCenteredString(this.fontRenderer,
													valText,
													controlX - BUTTON_WIDTH / 2,
													itemY + 6,
													0xFFFFFF);
							
							GuiButton decButton = new GuiButton(buttonIDStart + (originalIndex * 2),
																controlX - BUTTON_WIDTH / 2 - 35,
																controlY,
																20,
																BUTTON_HEIGHT,
																"-");
							decButton.drawButton(this.mc, mouseX, mouseY);
							this.buttonList.add(decButton);
							
							GuiButton incButton = new GuiButton(buttonIDStart + (originalIndex * 2) + 1,
																controlX - BUTTON_WIDTH / 2 + 15,
																controlY,
																20,
																BUTTON_HEIGHT,
																"+");
							incButton.drawButton(this.mc, mouseX, mouseY);
							this.buttonList.add(incButton);
						}
						else if (setting.type() == DVS_ConfigManager.Type.INT) {
							int val = settingsManager.getInt(setting.id());
							String valText = String.valueOf(val);
							
							this.drawCenteredString(this.fontRenderer,
													valText,
													controlX - BUTTON_WIDTH / 2,
													itemY + 6,
													0xFFFFFF);
							
							GuiButton decButton = new GuiButton(buttonIDStart + (originalIndex * 2),
																controlX - BUTTON_WIDTH / 2 - 35,
																controlY,
																20,
																BUTTON_HEIGHT,
																"-");
							decButton.drawButton(this.mc, mouseX, mouseY);
							this.buttonList.add(decButton);
							
							GuiButton incButton = new GuiButton(buttonIDStart + (originalIndex * 2) + 1,
																controlX - BUTTON_WIDTH / 2 + 15,
																controlY,
																20,
																BUTTON_HEIGHT,
																"+");
							incButton.drawButton(this.mc, mouseX, mouseY);
							this.buttonList.add(incButton);
						}
					}
					itemY += ITEM_HEIGHT;
				}
			}
		}
		
		// 5. Scrollbar
		{
			int barHeight = (int) (scrollHeight * (scrollHeight / (float) contentHeight));
			if (barHeight < 10) barHeight = 10;
			if (contentHeight < scrollHeight) barHeight = scrollHeight;
			
			int maxBarTravel = scrollHeight - barHeight;
			int barYStart = scrollYStart + (int) (scrollOffset * maxBarTravel);
			
			drawRect(scrollXEnd, scrollYStart, scrollXEnd + SCROLLBAR_WIDTH, scrollYEnd, 0x88FFFFFF);
			drawRect(scrollXEnd, barYStart, scrollXEnd + SCROLLBAR_WIDTH, barYStart + barHeight, 0xFFFFFFFF);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.clearClipping();
		
		GuiButton done = new GuiButton(200, this.width / 2 - 100, this.height - 25, I18n.getString("gui.done"));
		this.buttonList.add(done);
		done.drawButton(this.mc, mouseX, mouseY);
		
		if (hoveredDescription != null && !hoveredDescription.isEmpty()) {
			drawTooltip(hoveredDescription, mouseX, mouseY);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		super.mouseClicked(mouseX, mouseY, button);
		
		int scrollXStart = this.width / 2 - 150;
		int scrollXEnd = this.width / 2 + 150;
		int scrollYEnd = this.height - 40;
		
		if (mouseX >= scrollXStart &&
				mouseX <= scrollXEnd + SCROLLBAR_WIDTH &&
				mouseY >= HEADER_HEIGHT &&
				mouseY <= scrollYEnd) {
			
			isScrolling = true;
			lastMouseY = mouseY;
		}
	}
	
	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int button) {
		super.mouseMovedOrUp(mouseX, mouseY, button);
		if (button == 0) {
			isScrolling = false;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 200) {
				settingsManager.save();
				this.mc.displayGuiScreen(this.parentScreen);
			} else if (button.id >= buttonIDStart) {
				handleSettingInteraction(button.id);
			}
		}
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		
		int wheel = Mouse.getDWheel();
		int numCategories = categorizedSettings.size();
		int scrollYEnd = this.height - 40;
		int scrollHeight = scrollYEnd - HEADER_HEIGHT;
		int contentHeight = (settingList.size() * ITEM_HEIGHT) + (numCategories * SECTION_HEADER_HEIGHT);
		float maxScroll = Math.max(0, contentHeight - scrollHeight);
		
		if (wheel != 0) {
			float scrollChange = (float) (wheel / 120) * (ITEM_HEIGHT * 3);
			float offsetChange = scrollChange / maxScroll;
			scrollOffset -= offsetChange;
			scrollOffset = Math.max(0, Math.min(1, scrollOffset));
		}
		
		if (isScrolling) {
			int currentMouseY = Mouse.getY() * this.height / this.mc.displayHeight;
			int deltaY = currentMouseY - lastMouseY;
			lastMouseY = currentMouseY;
			float maxScrollDrag = Math.max(1, contentHeight - scrollHeight);
			float offsetChange = (float) -deltaY / maxScrollDrag;
			scrollOffset += offsetChange;
			scrollOffset = Math.max(0, Math.min(1, scrollOffset));
		}
	}
	
	private void drawTooltip(String text, int x, int y) {
		int padding = 3;
		int textWidth = this.fontRenderer.getStringWidth(text);
		int tooltipWidth = textWidth + 2 * padding;
		
		int tooltipX = x + 12;
		int tooltipY = y - 10;
		
		if (tooltipX + tooltipWidth + padding > this.width) {
			tooltipX = x - 12 - tooltipWidth;
		}
		
		int bgColor = 0xF0100010;
		int borderColor = 0x505000FF;
		
		drawRect(tooltipX - padding,
				 tooltipY - padding,
				 tooltipX + textWidth + padding,
				 tooltipY + this.fontRenderer.FONT_HEIGHT + padding,
				 bgColor);
		
		drawRect(tooltipX - padding,
				 tooltipY - padding - 1,
				 tooltipX + textWidth + padding,
				 tooltipY - padding,
				 borderColor);
		drawRect(tooltipX - padding,
				 tooltipY + this.fontRenderer.FONT_HEIGHT + padding,
				 tooltipX + textWidth + padding,
				 tooltipY + this.fontRenderer.FONT_HEIGHT + padding + 1,
				 borderColor);
		
		drawRect(tooltipX - padding - 1,
				 tooltipY - padding,
				 tooltipX - padding,
				 tooltipY + this.fontRenderer.FONT_HEIGHT + padding,
				 borderColor);
		drawRect(tooltipX + textWidth + padding,
				 tooltipY - padding,
				 tooltipX + textWidth + padding + 1,
				 tooltipY + this.fontRenderer.FONT_HEIGHT + padding,
				 borderColor);
		
		this.fontRenderer.drawStringWithShadow(text, tooltipX, tooltipY, 0xFFFFFF);
	}
}