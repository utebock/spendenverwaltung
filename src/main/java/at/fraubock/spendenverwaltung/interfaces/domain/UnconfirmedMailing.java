package at.fraubock.spendenverwaltung.interfaces.domain;

public class UnconfirmedMailing {
	
	private Mailing mailing;
	private String creator;
	
	public UnconfirmedMailing(Mailing mailing, String creator) {
		this.mailing = mailing;
		this.creator = creator;
	}
	
	public Mailing getMailing() {
		return mailing;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((mailing == null) ? 0 : mailing.hashCode());
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
		UnconfirmedMailing other = (UnconfirmedMailing) obj;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (mailing == null) {
			if (other.mailing != null)
				return false;
		} else if (!mailing.equals(other.mailing))
			return false;
		return true;
	}

	public String getCreator() {
		return creator;
	}
}
