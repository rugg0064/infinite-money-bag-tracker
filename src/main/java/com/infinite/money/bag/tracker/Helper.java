package com.infinite.money.bag.tracker;

import java.awt.Color;

public class Helper
{
	public final static String MONEY_BAG_SUCCESS_MESSAGE = "You've obtained a single coin! Wow!";
	public static int MONEY_BAG_ITEM_ID = 27440;

	static Color getCountColor(int count) {
		Color color;
		if (count < 100000) {
			color = new Color(0xFF, 0xFF, 0x00);
		} else if (count < 10000000) {
			color = new Color(0xFF, 0xFF, 0xFF);
		} else {
			color = new Color(0x00, 0xFF, 0x80);
		}
		return color;
	}

	static String formatCount(int count) {
		String text;
		if (count < 100000) {
			text = String.valueOf(count);
		} else if (count < 10000000) {
			text = (count / 1000) + "K";
		} else {
			text = (count / 1000000) + "M";
		}
		return text;
	}
}
