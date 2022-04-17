package org.trdc.announcement.utils;

import org.jsoup.select.Elements;

/**
 * Author Ibrahim Jamali
 */
public class Transaction {
	private String hash;
	private String method;
	private String age;
	private String from;
	private String type;
	private String to;
	private String quantity;

	public Transaction() {
	}

	public Transaction(String row) {
		String[] model = row.split("\\|\\|");
		if (model.length == 7) {
			this.hash = model[0];
			this.method = model[1];
			this.age = model[2];
			this.from = model[3];
			this.type = model[4];
			this.to = model[5];
			this.quantity = model[6];
		}
	}

	public Transaction(String hash, String method, String age, String from, String type, String to, String quantity) {
		this.hash = hash;
		this.method = method;
		this.age = age;
		this.from = from;
		this.type = type;
		this.to = to;
		this.quantity = quantity;
	}

	public Transaction(Elements elements) {
		this.hash = elements.get(0).text();
		this.method = elements.get(1).text();
		this.age = elements.get(2).text();
		this.from = elements.get(4).text();
		this.type = elements.get(5).text();
		this.to = elements.get(6).text();
		this.quantity = elements.get(7).text();
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return hash + "||" + method + "||" + age + "||" + from + "||" + type + "||" + to + "||" + quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		return true;
	}

}
