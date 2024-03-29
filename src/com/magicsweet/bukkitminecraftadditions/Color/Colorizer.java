package com.magicsweet.bukkitminecraftadditions.Color;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ChatColor;

public class Colorizer {
	
	public static List<String> format(List<String> stringList) {
		return stringList.stream().map(string -> {
			final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
			
			Matcher match = pattern.matcher(string);
			while (match.find()) {
				String color = string.substring(match.start(), match.end());
				
				string = string.replace(color, ChatColor.of(color).toString());
				match = pattern.matcher(string);
			}
			
			return ChatColor.translateAlternateColorCodes('&', string);
		}).collect(Collectors.toList());
	}
	
	public static String format(String string) {
		final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
		
		Matcher match = pattern.matcher(string);
		while (match.find()) {
			String color = string.substring(match.start(), match.end());
			
			string = string.replace(color, ChatColor.of(color).toString());
			
			match = pattern.matcher(string);
		}
		
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	@Deprecated
	public static List<String> formatLegacy(List<String> stringList) {
		return stringList.stream().map(string -> {
			final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
			
			Matcher match = pattern.matcher(string);
			while (match.find()) {
				String color = string.substring(match.start(), match.end());
				
				string = string.replace(color, convertHexColorToNearestMinecraftColor(color));
				match = pattern.matcher(string);
			}
			
			return ChatColor.translateAlternateColorCodes('&', string);
		}).collect(Collectors.toList());
	}
	
	@Deprecated
	public static String formatLegacy(String string) {
		final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
		
		Matcher match = pattern.matcher(string);
		while (match.find()) {
			String color = string.substring(match.start(), match.end());
			
			string = string.replace(color, convertHexColorToNearestMinecraftColor(color));
			
			match = pattern.matcher(string);
		}
		
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	
	private static String convertHexColorToNearestMinecraftColor(String hex) {
		
		HashMap<Color, String> mcColors = new HashMap<>();
		
		mcColors.put(Color.decode("#000000"), "0");
		mcColors.put(Color.decode("#0000AA"), "1");
		mcColors.put(Color.decode("#00AA00"), "2");
		mcColors.put(Color.decode("#00AAAA"), "3");
		mcColors.put(Color.decode("#AA0000"), "4");
		mcColors.put(Color.decode("#AA00AA"), "5");
		mcColors.put(Color.decode("#FFAA00"), "6");
		mcColors.put(Color.decode("#AAAAAA"), "7");
		mcColors.put(Color.decode("#555555"), "8");
		mcColors.put(Color.decode("#5555FF"), "9");
		mcColors.put(Color.decode("#55FF55"), "a");
		mcColors.put(Color.decode("#55FFFF"), "b");
		mcColors.put(Color.decode("#FF5555"), "c");
		mcColors.put(Color.decode("#FF5555"), "d");
		mcColors.put(Color.decode("#FFFF55"), "e");
		mcColors.put(Color.decode("#FFFFFF"), "f");
		
		var initial = Color.decode(hex);
		
		var differences = new HashMap<Color, Integer>();
		
		mcColors.keySet().forEach(key -> {
			int r, g, b;
			r = initial.getRed() - key.getRed(); if (r < 0) r = r*-1;
			g = initial.getGreen() - key.getGreen(); if (g < 0) g = g*-1;
			b = initial.getBlue() - key.getBlue(); if (b < 0) b = b*-1;
			
			differences.put(key, r + g + b);
			
		});
		
		int c = Integer.MAX_VALUE;
		Color color = null;
		
		for (var col: differences.keySet()) {
			var val = differences.get(col);
			if (val < c) {
				c = val;
				color = col;
			}
		}
		
		if (color != null) {
			return "&" + mcColors.get(color);
		} else {
			return null;
		}
	}
}
