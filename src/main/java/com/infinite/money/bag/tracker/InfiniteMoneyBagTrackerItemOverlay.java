package com.infinite.money.bag.tracker;

import static com.infinite.money.bag.tracker.Helper.MONEY_BAG_ITEM_ID;
import static com.infinite.money.bag.tracker.Helper.formatCount;
import static com.infinite.money.bag.tracker.Helper.getCountColor;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import static net.runelite.api.widgets.WidgetID.EQUIPMENT_GROUP_ID;
import static net.runelite.api.widgets.WidgetID.KEPT_ON_DEATH_GROUP_ID;
import static net.runelite.api.widgets.WidgetID.LOOTING_BAG_GROUP_ID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;

public class InfiniteMoneyBagTrackerItemOverlay extends WidgetItemOverlay
{
	private InfiniteMoneyBagTrackerPlugin plugin;

	public InfiniteMoneyBagTrackerItemOverlay(InfiniteMoneyBagTrackerPlugin plugin) {
		this.plugin = plugin;
		showOnInventory();
		showOnBank();
		showOnInterfaces(KEPT_ON_DEATH_GROUP_ID, LOOTING_BAG_GROUP_ID, EQUIPMENT_GROUP_ID);
	}

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem)
	{
		if(itemId == MONEY_BAG_ITEM_ID) {
			graphics.setFont(FontManager.getRunescapeSmallFont());
			int count = plugin.getGlobal();
			renderText(graphics, widgetItem.getCanvasBounds(), formatCount(count), getCountColor(count));
		}
	}


	private void renderText(Graphics2D graphics, Rectangle bounds, String text, Color color)
	{
		final TextComponent textComponent = new TextComponent();
		textComponent.setPosition(new Point(bounds.x + 0, bounds.y + 10));
		textComponent.setColor(color);
		textComponent.setText(text);
		textComponent.render(graphics);
	}
}
