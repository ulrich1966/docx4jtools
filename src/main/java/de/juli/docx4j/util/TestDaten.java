package de.juli.docx4j.util;

import java.util.HashMap;
import java.util.Map;

import org.docx4j.model.fields.merge.DataFieldName;

public class TestDaten {
	public static Map<String, String> genareate(){
		Map<String, String> map = new HashMap<>();
		map.put("name", "Ich AG");
		map.put("first_name", "Ulrich");
		map.put("last_name", "Kloodt");
		map.put("city", "Bremen");
		map.put("zip", "28203");
		map.put("street", "Ostendorpsrt 36");
		map.put("sex", "geherter");
		map.put("appell", "Herr");
		map.put("salary", "10.000");
		map.put("title", "Softwareentwickler");
		return map;
	}

	public static Map<DataFieldName, String> genareateFieds(){
		Map<DataFieldName, String> map = new HashMap<>();
		map.put(new DataFieldName("name"), "Ich AG");
		map.put(new DataFieldName("first_name"), "Ulrich");
		map.put(new DataFieldName("last_name"), "KLoodt");
		map.put(new DataFieldName("city"), "Bremen");
		map.put(new DataFieldName("zip"), "28203");
		map.put(new DataFieldName("street"), "Ostendorpstraﬂe 36");
		map.put(new DataFieldName("sex"), "geherter");
		map.put(new DataFieldName("appell"), "Herr");
		map.put(new DataFieldName("salary"), "10.000");
		map.put(new DataFieldName("title"), "Softwareentwickler");
		return map;
	}

	public static Map<DataFieldName, String> testFieds(){
		Map<DataFieldName, String> map = new HashMap<>();
		map.put(new DataFieldName("benutzerfeld"), "Moin Benutzerfeld");
		map.put(new DataFieldName("variable"), "Moin Variable");
		return map;
	}

	public static Map<String, String> testFiedsAsString() {
		Map<String, String> map = new HashMap<>();
		map.put("name", "Ich AG");
		map.put("first_name", "Ulrich");
		map.put("last_name", "KLoodt");
		map.put("city", "Bremen");
		map.put("zip", "28203");
		map.put("street", "Ostendorpsrt 36");
		return map;
	}
}
