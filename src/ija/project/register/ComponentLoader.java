package ija.project.register;

import ija.project.schema.BlockType;
import ija.project.schema.Type;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlDom;

import java.io.File;
import java.net.URL;

public class ComponentLoader {
	public static void loadFromXML(URL path) {
		XmlDom xmlDom = new XmlDom();
		xmlDom.parseFile(path);
		loadFromXML(xmlDom);
	}

	public static void loadFromXML(File file) {
		XmlDom xmlDom = new XmlDom();
		xmlDom.parseFile(file);
		loadFromXML(xmlDom);
	}

	public static void loadFromXML(XmlActiveNode node) {
		node.getCurrentNode("register");
		for (XmlActiveNode child : node.childIterator()) {
			if (child.getTag().equals("blocktypes")) {
				loadBlockTypes(child);
			} else if (child.getTag().equals("types")) {
				loadDataTypes(child);
			}
		}
	}

	private static void loadBlockTypes(XmlActiveNode child) {
		String catName;
		BlockType blockType;
		for (XmlActiveNode category : child.childIterator()) {
			if (category.getTag().equals("category")) {
				catName = category.getAttribute("name");

				for (XmlActiveNode blockTypeNode : category.childIterator()) {
					blockType = new BlockType();
					blockType.fromXML(blockTypeNode);
					blockType.setCategory(catName);
					BlockTypeRegister.reg(catName, blockType);
				}
			}
		}
	}

	private static void loadDataTypes(XmlActiveNode child) {
		Type type;
		for (XmlActiveNode typeNode : child.childIterator()) {
			type = new Type();
			type.fromXML(typeNode);
			TypeRegister.reg(type);
		}

	}
}
