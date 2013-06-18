package at.fraubock.spendenverwaltung.util;

import java.util.Date;

import at.fraubock.spendenverwaltung.interfaces.domain.Action.Entity;
import at.fraubock.spendenverwaltung.interfaces.domain.Action.Type;

/**
 * value object storing values for searching {@link Action} entities. a null
 * value means that the attribute should not be considered. all attributes meant
 * to be connected conjunctively (AND).
 *
 * @author philipp muhoray
 * 
 */
public class ActionSearchVO {

	private String actor;
	private Date from;
	private Date to;
	private Type type;
	private Entity entity;
	private String payload;

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "ActionSearchVO [actor=" + actor + ", from=" + from + ", to="
				+ to + ", type=" + type + ", entity=" + entity + ", payload="
				+ payload + "]";
	}

}
