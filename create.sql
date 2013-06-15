USE ubspenderverwaltung;

drop view if exists validated_addresses;
drop view if exists validated_persons;
drop view if exists validated_donations;

drop table if exists sentmailings;
drop table if exists sent_mailings;
drop table if exists mailings;
drop table if exists unsent_mailings;

drop table if exists connected_criterion;
drop table if exists mountedfilter_criterion;
drop table if exists property_criterion;
drop table if exists filter;
drop table if exists criterion;

drop table if exists livesat;
drop table if exists donations;
drop table if exists imports;
drop table if exists persons;
drop table if exists addresses;

drop table if exists mailing_templates;

CREATE TABLE addresses ( -- for querying, you may want to use validated_addresses
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	street VARCHAR(1024) NOT NULL, -- including all address lines, e.g. 'Karlsplatz 14/5'
	postcode VARCHAR(20) NOT NULL, -- must be 4-digit if country = 'Österreich'. Some countries may use characters as well.
	city VARCHAR(1024) NOT NULL,
	country VARCHAR(120) NOT NULL DEFAULT 'Österreich' -- German name of country (must be the same for same country)
);

CREATE TABLE persons ( -- for querying, you may want to use validated_persons
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	givenname VARCHAR(120), -- including second names, e.g. 'Klaus Maria'
	surname VARCHAR(120),
	email VARCHAR(1024), -- must be a valid email address, containing user, '@' and domain
	sex ENUM('male','female','family','company') NOT NULL,
	title VARCHAR(1024), -- concatenated academic titles, e.g. 'Dipl.-Ing. BA OStR'
	company VARCHAR(120), -- company name
	telephone VARCHAR(120), -- may have any user-readable form (e.g. '+431242424', '01/ 24 24 - 24', '06501234567')
	emailnotification BOOLEAN NOT NULL DEFAULT TRUE, -- whether the person wishes to receive email notifications
	postalnotification BOOLEAN NOT NULL DEFAULT TRUE, -- whether the person wishes to receive postal notifications
	note VARCHAR(1024)
);

CREATE TABLE imports (
    id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    creator VARCHAR(30) NOT NULL, -- user who created the import (database user name)
    import_date DATE NOT NULL,
    source VARCHAR(30) NOT NULL -- e.g. 'SMS-Aktion', 'Hypo-Export', 'Hypo-Export automatisch', 'native', ...
);

CREATE TABLE donations ( -- for querying, you may want to use validated_donations
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	personid INTEGER UNSIGNED NOT NULL,
	amount BIGINT NOT NULL, -- in EUR-cents
	donationdate TIMESTAMP NOT NULL, -- only day relevant (i.e., hours, minutes, seconds, milliseconds neglected)
	dedication VARCHAR(1024),
	type ENUM('sms','bar','bank transfer','merchandise','online') NOT NULL,
	note VARCHAR(1024),
        import INTEGER UNSIGNED DEFAULT NULL REFERENCES imports(id) ON DELETE SET NULL, -- if NULL, this donation is considered confirmed/validated. If not null, it is not yet validated by any user.
	FOREIGN KEY(personid) REFERENCES persons(id) ON DELETE CASCADE
);

CREATE TABLE livesat (
	pid INTEGER UNSIGNED REFERENCES persons(id) ON DELETE CASCADE,
	aid INTEGER UNSIGNED REFERENCES addresses(id) ON DELETE CASCADE,
        ismain BOOLEAN NOT NULL, -- if true, the address is the person's main address. a person must not have more than one main address. a person may have several addresses, but no main address specified.
        PRIMARY KEY(pid, aid)
);

# filter

CREATE TABLE criterion ( # an abstract criterion defining a condition
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	type ENUM('validated_persons','validated_donations','mailings','validated_addresses') NOT NULL # the filter type of the filter this criterion belongs to
);

CREATE TABLE filter ( # defines a filter for a specific entity
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	criterion INTEGER UNSIGNED, # the criterion for this filter
	type ENUM('validated_persons','validated_donations','mailings','validated_addresses') NOT NULL, # the entity this filter is applicable to
	name VARCHAR(120), # a name for this filter. can be null when anonymous
	anonymous BOOLEAN NOT NULL DEFAULT FALSE, # anonymous filters are created inside other filters and only exist there
    private BOOLEAN NOT NULL DEFAULT TRUE, # have all users access to this filter?!
    owner VARCHAR(50) REFERENCES mysql.user(user), # name of the user who created this filter
	FOREIGN KEY(criterion) REFERENCES criterion(id)
);

CREATE TABLE connected_criterion ( # a concrete criterion that logically connects two abstract criterias
	id INTEGER UNSIGNED PRIMARY KEY REFERENCES criterion(id),
	operand1 INTEGER UNSIGNED NOT NULL,
	operand2 INTEGER UNSIGNED,
	logical_operator ENUM('AND','OR','XOR','AND_NOT','OR_NOT','NOT') NOT NULL,
	FOREIGN KEY(operand1) REFERENCES criterion(id),
	FOREIGN KEY(operand2) REFERENCES criterion(id)
);

CREATE TABLE property_criterion ( # a concrete criterion that compares an entities property with a given value
	id INTEGER UNSIGNED PRIMARY KEY REFERENCES criterion(id),
	property VARCHAR(120) NOT NULL, # the property of the entity to be compared
	relational_operator ENUM('EQUALS','GREATER','LESS','GREATER_EQ','LESS_EQ','LIKE','UNEQUAL','IS_NULL','IS_NOT_NULL') NOT NULL,
	# the value the property is compared to (only either one is set)
	numValue DOUBLE, 
	strValue VARCHAR(1024),
	dateValue DATE,
	daysBack INT, # if the property is a date, it will be compared with the current date minus daysBack
	boolValue BOOLEAN
);

CREATE TABLE mountedfilter_criterion ( # a criterion which applies another filter. the result will be compared to a given constraint.
	id INTEGER UNSIGNED PRIMARY KEY REFERENCES criterion(id),
	mount INTEGER UNSIGNED NOT NULL, # the filter that will be applied to a set of entities related to this entity
	relational_operator ENUM('EQUALS','GREATER','LESS','GREATER_EQ','LESS_EQ','LIKE','UNEQUAL','IS_NULL','IS_NOT_NULL') NOT NULL,
	count INT, # if set, the number of the filter result will be compared with this value
	property VARCHAR(120), # if set, either the sum or the average of this property will be compared with the given value
	sum DOUBLE,
	avg DOUBLE,
	FOREIGN KEY(mount) REFERENCES filter(id)
);

#mailings

CREATE TABLE mailings (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
        mailing_date DATE NOT NULL,
	mailing_type ENUM('allgemeiner Dankesbrief', 'Dankesbrief', 'Dauerspender Dankesbrief',
		'Einzelspenden Dankesbrief', 'Erlagscheinversand', 'Infomaterial',
		'Spendenaufruf', 'Spendenbrief', 'T-Shirt Versand'),
	mailing_medium ENUM('email', 'postal'),
    unconfirmed INTEGER UNSIGNED DEFAULT NULL REFERENCES unsent_mailings(id) ON DELETE SET NULL -- if NULL, this mailing is considered confirmed/validated.
);

CREATE TABLE sent_mailings (
	mailing_id INTEGER UNSIGNED REFERENCES mailings(id) ON DELETE CASCADE,
	person_id INTEGER UNSIGNED REFERENCES persons(id) ON DELETE CASCADE,
	PRIMARY KEY(mailing_id, person_id)
);

CREATE TABLE mailing_templates (
	id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	file_name VARCHAR(120) NOT NULL,
	file BLOB NOT NULL
);

CREATE TABLE unsent_mailings (
    id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    creator VARCHAR(30) NOT NULL -- user who created the import (database user name)
);

-- views for validated data:

CREATE VIEW validated_donations AS SELECT * FROM donations WHERE import IS NULL; -- only validated donations (i.e. no pending imports)

CREATE VIEW validated_persons AS SELECT * FROM persons p WHERE NOT EXISTS (SELECT id FROM donations d WHERE d.import IS NOT NULL AND d.personid = p.id); -- only validated persons (i.e. no pending imports)

CREATE VIEW validated_addresses AS SELECT * FROM addresses a WHERE NOT EXISTS (SELECT * FROM livesat l JOIN persons p ON (l.pid = p.id) WHERE l.aid = a.id AND p.id NOT IN (SELECT id FROM validated_persons)); -- only validated addresses (i.e. no pending imports)