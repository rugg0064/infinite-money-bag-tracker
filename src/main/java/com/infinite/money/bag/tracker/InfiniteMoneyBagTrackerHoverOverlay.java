package com.infinite.money.bag.tracker;

import com.google.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import static net.runelite.api.widgets.WidgetID.EQUIPMENT_GROUP_ID;
import static net.runelite.api.widgets.WidgetID.KEPT_ON_DEATH_GROUP_ID;
import static net.runelite.api.widgets.WidgetID.LOOTING_BAG_GROUP_ID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import net.runelite.client.util.QuantityFormatter;

public class InfiniteMoneyBagTrackerHoverOverlay extends Overlay
{
	private InfiniteMoneyBagTrackerPlugin plugin;

	private TooltipManager tooltipManager;

	private Client client;


	public InfiniteMoneyBagTrackerHoverOverlay(InfiniteMoneyBagTrackerPlugin plugin, Client client, TooltipManager tooltipManager) {
		this.plugin = plugin;
		this.client = client;
		this.tooltipManager = tooltipManager;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (client.isMenuOpen())
		{
			return null;
		}
		final MenuEntry[] menu = client.getMenuEntries();
		final int menuSize = menu.length;
		if (menuSize <= 0)
		{
			return null;
		}
		final MenuEntry entry = menu[menuSize - 1];
		final Widget widget = entry.getWidget();
		if (widget == null)
		{
			return null;
		}

		final int group = WidgetInfo.TO_GROUP(widget.getId());
		int itemId = -1;
		if (group == WidgetInfo.EQUIPMENT.getGroupId() ||
			// For bank worn equipment, check widget parent to differentiate from normal bank items
			(group == WidgetID.BANK_GROUP_ID && widget.getParentId() == WidgetInfo.BANK_EQUIPMENT_CONTAINER.getId()))
		{
			final Widget widgetItem = widget.getChild(1);
			if (widgetItem != null)
			{
				itemId = widgetItem.getItemId();
			}
		}
		else if (widget.getId() == WidgetInfo.INVENTORY.getId()
			|| group == WidgetInfo.EQUIPMENT_INVENTORY_ITEMS_CONTAINER.getGroupId()
			|| widget.getId() == WidgetInfo.BANK_ITEM_CONTAINER.getId()
			|| group == WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER.getGroupId()
			|| widget.getId() == WidgetInfo.GROUP_STORAGE_ITEM_CONTAINER.getId()
			|| group == WidgetID.GROUP_STORAGE_INVENTORY_GROUP_ID)
		{
			itemId = widget.getItemId();
		}
		if (itemId == -1)
		{
			return null;
		}

		if(itemId == Helper.MONEY_BAG_ITEM_ID) {
			tooltipManager.add(new Tooltip(String.format("Global: %s", QuantityFormatter.formatNumber(plugin.getGlobal()))));
			tooltipManager.add(new Tooltip(String.format("Session: %s", QuantityFormatter.formatNumber(plugin.getThisSession()))));
		}

		return null;
	}
}
