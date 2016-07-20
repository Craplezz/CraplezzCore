package me.mani.clcore.locale;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocaleManager {

	private static final String DE = "de_DE";
	
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> localeCollection;
	
	private Map<String, String> locales = new HashMap<>();
	
	public LocaleManager(MongoClient mongoClient, String database, String collection) {
		mongoDatabase = mongoClient.getDatabase(database);
		localeCollection = mongoDatabase.getCollection(collection);
		load();
	}

	private void load() {
		Document document = localeCollection.find(new Document("type", "locale").append("language", DE)).first();
		for (Document localeDocument : (List<Document>) document.get("data"))
			locales.put(localeDocument.getString("key"), localeDocument.getString("value"));
	}
	
	public String getLocale(String key) {
		return locales.get(key);
	}
	
	public String translate(String key, Object... args) {
		String message = locales.get(key);
		for (int i = 0; ; i++) {
			if (!message.contains("{" + i + "}") || args.length <= i)
				break;
			message = message.replace("{" + i + "}", args[i].toString());
		}
		return message;
	}
	
}
