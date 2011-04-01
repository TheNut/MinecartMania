package com.afforess.minecartmaniacore.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.afforess.minecartmaniacore.Item;
import com.afforess.minecartmaniacore.MinecartManiaCore;
import com.afforess.minecartmaniacore.MinecartManiaWorld;

public class CoreSettingParser implements SettingParser{
	private static final double version = 1.2;
	
	public boolean isUpToDate(Document document) {
		try {
			NodeList list = document.getElementsByTagName("version");
			Double version = MinecartManiaConfigurationParser.toDouble(list.item(0).getChildNodes().item(0).getNodeValue(), 0);
			return version == CoreSettingParser.version;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean read(Document document) {
		Object value;
		NodeList list;
		String setting;

		try {
			//Parse Simple Settings First
			setting = "MinecartsKillMobs";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toBool(list.item(0).getChildNodes().item(0).getNodeValue());
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "MinecartsClearRails";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toInt(list.item(0).getChildNodes().item(0).getNodeValue(), 1);
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "KeepMinecartsLoaded";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toBool(list.item(0).getChildNodes().item(0).getNodeValue());
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "MinecartsReturnToOwner";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toBool(list.item(0).getChildNodes().item(0).getNodeValue());
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "MaximumMinecartSpeedPercent";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toInt(list.item(0).getChildNodes().item(0).getNodeValue(), 165);
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "DefaultMinecartSpeedPercent";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toInt(list.item(0).getChildNodes().item(0).getNodeValue(), 100);
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "Range";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toInt(list.item(0).getChildNodes().item(0).getNodeValue(), 2);
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "MaximumRange";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toInt(list.item(0).getChildNodes().item(0).getNodeValue(), 25);
			MinecartManiaWorld.getConfiguration().put(setting, value);
			
			setting = "StackAllItems";
			list = document.getElementsByTagName(setting);
			value = MinecartManiaConfigurationParser.toBool(list.item(0).getChildNodes().item(0).getNodeValue());
			MinecartManiaWorld.getConfiguration().put(setting, value);
	
			//read arrays
			ControlBlockList.controlBlocks = new ArrayList<ControlBlock>();
			list = document.getElementsByTagName("ControlBlock");
			for (int temp = 0; temp < list.getLength(); temp++) {
				Node n = list.item(temp);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) n;
					ControlBlock cb = new ControlBlock();
					
					NodeList templist = element.getElementsByTagName("BlockType").item(0).getChildNodes();
					Node tempNode = (Node) templist.item(0);
					cb.setType(MinecartManiaConfigurationParser.toItem(getNodeValue(tempNode)));
					
					templist = element.getElementsByTagName("SpeedMultiplier").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setMultiplierState(parseRedstoneState(getParentFirstAttributeValue(tempNode)));
					cb.setMultiplier(MinecartManiaConfigurationParser.toDouble(getNodeValue(tempNode), 1.0));
					
					templist = element.getElementsByTagName("Catch").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setCatcherState(parseRedstoneState(getParentFirstAttributeValue(tempNode)));
					cb.setCatcherBlock(MinecartManiaConfigurationParser.toBool(getNodeValue(tempNode)));
					
					templist = element.getElementsByTagName("LauncherSpeed").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setLauncherState(parseRedstoneState(getParentFirstAttributeValue(tempNode)));
					cb.setLauncherSpeed(MinecartManiaConfigurationParser.toDouble(getNodeValue(tempNode), 0.0));
					
					templist = element.getElementsByTagName("Eject").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setEjectorState(parseRedstoneState(getParentFirstAttributeValue(tempNode)));
					cb.setEjectorBlock(MinecartManiaConfigurationParser.toBool(getNodeValue(tempNode)));
					
					templist = element.getElementsByTagName("Platform").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setPlatformState(parseRedstoneState(getParentFirstAttributeValue(tempNode)));
					cb.setPlatformBlock(MinecartManiaConfigurationParser.toBool(getNodeValue(tempNode)));
					
					templist = element.getElementsByTagName("Station").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setStationState(parseRedstoneState(getParentFirstAttributeValue(tempNode)));
					cb.setStationBlock(MinecartManiaConfigurationParser.toBool(getNodeValue(tempNode)));
					
					templist = element.getElementsByTagName("SpawnMinecart").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setSpawnState(parseRedstoneState(getParentFirstAttributeValue(tempNode)));
					cb.setSpawnMinecart(MinecartManiaConfigurationParser.toBool(getNodeValue(tempNode)));
					
					templist = element.getElementsByTagName("KillMinecart").item(0).getChildNodes();
					tempNode = (Node) templist.item(0);
					cb.setKillState(parseRedstoneState(getParentFirstAttributeValue(tempNode)));
					cb.setKillMinecart(MinecartManiaConfigurationParser.toBool(getNodeValue(tempNode)));

					ControlBlockList.controlBlocks.add(cb);
				}
			 }
			 
			list = document.getElementsByTagName("ItemAlias");
			for (int temp = 0; temp < list.getLength(); temp++) {
				Node n = list.item(temp);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) n;
					
					NodeList templist = element.getElementsByTagName("AliasName").item(0).getChildNodes();
					Node tempNode = (Node) templist.item(0);
					String key = tempNode != null ? tempNode.getNodeValue() : null;
					
					ArrayList<Item> values = new ArrayList<Item>();
					templist = element.getElementsByTagName("ItemType").item(0).getChildNodes();
					for (int i = 0; i < templist.getLength(); i++) {
						tempNode = (Node) templist.item(i);
						values.add(MinecartManiaConfigurationParser.toItem(tempNode != null ? tempNode.getNodeValue() : null));
					}
					
					ItemAliasList.aliases.put(key, values);
				}
			}
		}
		catch (Exception e) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean write(File configFile) {
		try {
			JarFile jar = new JarFile(MinecartManiaCore.MinecartManiaCore);
			JarEntry entry = jar.getJarEntry("MinecartManiaConfiguration.xml");
			InputStream is = jar.getInputStream(entry);
			FileOutputStream os = new FileOutputStream(configFile);
			byte[] buf = new byte[(int)entry.getSize()];
			is.read(buf, 0, (int)entry.getSize());
			os.write(buf);
			os.close();
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private String getNodeValue(Node node) {
		if (node == null) return null;
		return node.getNodeValue();
	}
	
	private String getParentFirstAttributeValue(Node node) {
		if (node == null || node.getParentNode() == null || node.getParentNode().getAttributes() == null) return null;
		return getNodeValue(node.getParentNode().getAttributes().item(0));
	}
	
	private RedstoneState parseRedstoneState(String str) {
		if (str == null || str.equalsIgnoreCase("default")) return RedstoneState.Default;
		if (str.toLowerCase().contains("enable")) return RedstoneState.Enables;
		if (str.toLowerCase().contains("disable")) return RedstoneState.Disables;
		return RedstoneState.Default;
	}
}
