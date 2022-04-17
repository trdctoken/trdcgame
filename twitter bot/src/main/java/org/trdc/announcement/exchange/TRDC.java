package org.trdc.announcement.exchange;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.trdc.announcement.utils.Client;
import org.trdc.announcement.utils.Transaction;
import org.trdc.announcement.utils.Util;
/**
 * Author Ibrahim Jamali
 */
public abstract class TRDC implements Runnable {

	private static final Logger logger = LogManager.getLogger(TRDC.class);

	private Client client;
	private Properties properties;
	private String name;

	private String condition;

	private String gameExplorerUrl;
	private String announcementFile;
	private Set<Transaction> oldAnnouncements;

	public TRDC(Client client, Properties properties) {
		this.name = this.getClass().getSimpleName().toLowerCase();
		this.setProperties(properties);
		this.setGameExplorerUrl(properties.getProperty(this.name + "_explorer_url"));
		this.setAnnouncementFile(properties.getProperty(this.name + "_storage_file"));
		this.setOldAnnouncements(Util.getHashSetFromFile(this.getAnnouncementFile()).stream().map(Transaction::new)
				.collect(Collectors.toCollection(HashSet::new)));
		this.setClient(client);
	}

	public void setOldAnnouncements(Set<Transaction> oldAnnouncements) {
		if (oldAnnouncements != null)
			this.oldAnnouncements = oldAnnouncements;
	}

	public Set<Transaction> getOldAnnouncements() {
		return oldAnnouncements;
	}

	public void setGameExplorerUrl(String contract) {
		if (contract != null)
			this.gameExplorerUrl = contract;
		else
			throw new RuntimeException("URL must be not null");
	}

	public String getGameExplorerUrl() {
		return gameExplorerUrl;
	}

	public void setClient(Client client) {
		if (client != null)
			this.client = client;
		else
			throw new RuntimeException("client must be not null");

	}

	public void setAnnouncementFile(String announcementFile) {
		if (announcementFile != null)
			this.announcementFile = announcementFile;
		else
			throw new RuntimeException("announcementFile must be not null");
	}

	public String getAnnouncementFile() {
		return announcementFile;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Client getClient() {
		return client;
	}

	public Properties getProperties() {
		return properties;
	}

	public abstract Set<Transaction> parse(String announcements);

	public abstract String request();

	public void notify(Transaction transaction) {
		StringBuffer body = new StringBuffer("🔥🔥 TRDC " + name.toUpperCase() + " -> ");
		switch (transaction.getMethod()) {
		case "Claim Reward":
			body.append("🎁 A player has been claimed ").append(transaction.getQuantity()).append(" #TRDC");
			break;
		case "Buy Weapon":
			body.append("🔫 A weapon has been bought for ").append(transaction.getQuantity()).append(" #TRDC");
			break;
		case "Buy Card":
			body.append("🃏 A card has been bought for ").append(transaction.getQuantity()).append(" #TRDC");
			break;
		case "Run Cop":
			body.append("👮 The cop catches a thief and ").append(transaction.getQuantity())
					.append(" #TRDC has been burned");
			break;
		case "Withdrawal Token":
			body.append("💵 ").append(transaction.getQuantity()).append(" #TRDC has been withdrawn");
			break;
		default:
			break;
		}

		body.append("\n📢 For more details click on the link bellow \n").append("🔗 https://bscscan.com/tx/")
				.append(transaction.getHash());
//		System.out.println(body);
		Util.twitter(body.toString());
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCondition() {
		return condition;
	}

	@Override
	public void run() {
		Pattern pattern = Pattern.compile(this.condition, Pattern.CASE_INSENSITIVE);
		String announcements = this.request();
		if (announcements != null && pattern.matcher(announcements).find()) {
			Set<Transaction> newAnnouncements = this.parse(announcements);
			if (newAnnouncements != null && newAnnouncements.size() > 0
					&& !newAnnouncements.equals(this.getOldAnnouncements())) {
				Set<Transaction> differences = new HashSet<Transaction>(newAnnouncements);
				differences.removeAll(this.getOldAnnouncements());
				this.setOldAnnouncements(newAnnouncements);
				Util.putHashSetInFile(this.getAnnouncementFile(), newAnnouncements.parallelStream()
						.map(Transaction::toString).collect(Collectors.toCollection(HashSet::new)));
				for (Transaction model : differences) {
					notify(model);
				}
			}
		} else {
			logger.error("There is a problem with api endpoint of " + this.name);
		}

	}

	public String getName() {
		return name;
	}
}
