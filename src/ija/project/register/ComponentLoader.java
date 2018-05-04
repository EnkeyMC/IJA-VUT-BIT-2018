package ija.project.register;

import ija.project.schema.BlockType;
import ija.project.schema.Type;
import ija.project.xml.XmlActiveNode;
import ija.project.xml.XmlDom;

import java.io.File;
import java.net.URL;

/**
 * Loads BlockTypes and data Types from XML and registers them
 */
public class ComponentLoader {

	/**
	 * Load and register components from file specified by URL
	 * @param path URL path
	 */
	public static void loadFromXML(URL path) {
		XmlDom xmlDom = new XmlDom();
		xmlDom.parseFile(path);
		loadFromXML(xmlDom);
	}

	/**
	 * Load and register components from file
	 * @param file file
	 */
	public static void loadFromXML(File file) {
		XmlDom xmlDom = new XmlDom();
		xmlDom.parseFile(file);
		loadFromXML(xmlDom);
	}

	/**
	 * Load and register components from XML
	 * @param node XML node
	 */
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

	/**
	 * Load block types from XML
	 * @param child XML node
	 */
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

	/**
	 * Load data types from XML
	 * @param child XML node
	 */
	private static void loadDataTypes(XmlActiveNode child) {
		Type type;
		for (XmlActiveNode typeNode : child.childIterator()) {
			type = new Type();
			type.fromXML(typeNode);
			TypeRegister.reg(type);
		}

	}
}
