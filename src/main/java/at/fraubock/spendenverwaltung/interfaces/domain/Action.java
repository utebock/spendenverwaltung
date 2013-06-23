package at.fraubock.spendenverwaltung.interfaces.domain;

import java.util.Date;

/**
 * domain model representing an action performed by a user of the application
 * 
 * @author philipp muhoray
 * 
 */
public class Action {

	private Integer id;
	private String actor;
	private Date time;
	private Type type;
	private Entity entity;
	private Integer entityId;
	private String payload;

	public enum Type {
		INSERT("insert", "erstellt"), UPDATE("update", "bearbeitet"), DELETE(
				"delete", "gel\u00F6scht");

		private String name;
		private String displayableName;

		private Type(String name, String displayableName) {
			this.name = name;
			this.displayableName = displayableName;
		}

		@Override
		public String toString() {
			return displayableName;
		}

		public String getName() {
			return name;
		}

		public static Type getByName(String name) {
			for (Type type : Type.values()) {
				if (type.getName().equals(name)) {
					return type;
				}
			}
			return null;
		}
	}

	public enum Entity {
		PERSON("persons", "Person"), ADDRESSES("addresses", "Adresse"), LIVES_AT(
				"livesat", "Adresszuweisung"), IMPORT("imports", "Import"), DONATION(
				"donations", "Spende"), FILTER(
				"filter", "Filter"), MAILING("mailings", "Aussendung"), DONATION_CONFIRMATION(
				"donation_confirmations", "Spendenbest\u00e4tigung");

		private String name;
		private String displayableName;

		private Entity(String name, String displayableName) {
			this.name = name;
			this.displayableName = displayableName;
		}

		@Override
		public String toString() {
			return displayableName;
		}

		public String getName() {
			return name;
		}

		public static Entity getByName(String name) {
			for (Entity entity : Entity.values()) {
				if (entity.getName().equals(name)) {
					return entity;
				}
			}
			return null;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
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

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result
				+ ((entityId == null) ? 0 : entityId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Action other = (Action) obj;
		if (actor == null) {
			if (other.actor != null)
				return false;
		} else if (!actor.equals(other.actor))
			return false;
		if (entity != other.entity)
			return false;
		if (entityId == null) {
			if (other.entityId != null)
				return false;
		} else if (!entityId.equals(other.entityId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
