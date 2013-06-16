SET SESSION innodb_strict_mode=on;

USE ubspenderverwaltung;

DROP TRIGGER IF EXISTS addresses_log_insert;
DROP TRIGGER IF EXISTS addresses_log_update;
DROP TRIGGER IF EXISTS addresses_log_delete;
DROP TRIGGER IF EXISTS persons_log_insert;
DROP TRIGGER IF EXISTS persons_log_update;
DROP TRIGGER IF EXISTS persons_log_delete;
DROP TRIGGER IF EXISTS livesat_log_insert;
DROP TRIGGER IF EXISTS livesat_log_update;
DROP TRIGGER IF EXISTS livesat_log_delete;
DROP TRIGGER IF EXISTS imports_log_insert;
DROP TRIGGER IF EXISTS imports_log_update;
DROP TRIGGER IF EXISTS imports_log_delete;
DROP TRIGGER IF EXISTS donations_log_insert;
DROP TRIGGER IF EXISTS donations_log_update;
DROP TRIGGER IF EXISTS donations_log_delete;
DROP TRIGGER IF EXISTS connected_criterion_log_insert;
DROP TRIGGER IF EXISTS connected_criterion_log_update;
DROP TRIGGER IF EXISTS connected_criterion_log_delete;
DROP TRIGGER IF EXISTS property_criterion_log_insert;
DROP TRIGGER IF EXISTS property_criterion_log_update;
DROP TRIGGER IF EXISTS property_criterion_log_delete;
DROP TRIGGER IF EXISTS mountedfilter_criterion_log_insert;
DROP TRIGGER IF EXISTS mountedfilter_criterion_log_update;
DROP TRIGGER IF EXISTS mountedfilter_criterion_log_delete;
DROP TRIGGER IF EXISTS filter_log_insert;
DROP TRIGGER IF EXISTS filter_log_update;
DROP TRIGGER IF EXISTS filter_log_delete;
DROP TRIGGER IF EXISTS mailings_log_insert;
DROP TRIGGER IF EXISTS mailings_log_update;
DROP TRIGGER IF EXISTS mailings_log_delete;
DROP TRIGGER IF EXISTS unconfirmed_mailings_log_insert;
DROP TRIGGER IF EXISTS unconfirmed_mailings_log_update;
DROP TRIGGER IF EXISTS unconfirmed_mailings_log_delete;
DROP TRIGGER IF EXISTS sent_mailings_log_insert;
DROP TRIGGER IF EXISTS sent_mailings_log_update;
DROP TRIGGER IF EXISTS sent_mailings_log_delete;
DROP TRIGGER IF EXISTS mailing_templates_log_insert;
DROP TRIGGER IF EXISTS mailing_templates_log_update;
DROP TRIGGER IF EXISTS mailing_templates_log_delete;
DROP TRIGGER IF EXISTS donation_confirmation_templates_log_insert;
DROP TRIGGER IF EXISTS donation_confirmation_templates_log_update;
DROP TRIGGER IF EXISTS donation_confirmation_templates_log_delete;
DROP TRIGGER IF EXISTS donation_confirmations_log_insert;
DROP TRIGGER IF EXISTS donation_confirmations_log_update;
DROP TRIGGER IF EXISTS donation_confirmations_log_delete;
DROP TRIGGER IF EXISTS single_donation_confirmation_log_insert;
DROP TRIGGER IF EXISTS single_donation_confirmation_log_update;
DROP TRIGGER IF EXISTS single_donation_confirmation_log_delete;
DROP TRIGGER IF EXISTS multiple_donations_confirmation_log_insert;
DROP TRIGGER IF EXISTS multiple_donations_confirmation_log_update;
DROP TRIGGER IF EXISTS multiple_donations_confirmation_log_delete;

DROP VIEW IF EXISTS validated_addresses;
DROP VIEW IF EXISTS validated_persons;
DROP VIEW IF EXISTS validated_donations;

DROP TABLE IF EXISTS actions;

DROP TABLE IF EXISTS multiple_donations_confirmation;
DROP TABLE IF EXISTS single_donation_confirmation;
DROP TABLE IF EXISTS donation_confirmations;

DROP TABLE IF EXISTS donation_confirmation_templates;
DROP TABLE IF EXISTS mailing_templates;

DROP TABLE IF EXISTS sent_mailings;
DROP VIEW IF EXISTS confirmed_mailings;
DROP TABLE IF EXISTS unconfirmed_mailings;
DROP TABLE IF EXISTS mailings;

DROP TABLE IF EXISTS connected_criterion;
DROP TABLE IF EXISTS mountedfilter_criterion;
DROP TABLE IF EXISTS property_criterion;
DROP TABLE IF EXISTS filter;
DROP TABLE IF EXISTS criterion;

DROP TABLE IF EXISTS livesat;
DROP TABLE IF EXISTS donations;
DROP TABLE IF EXISTS imports;
DROP TABLE IF EXISTS persons;
DROP TABLE IF EXISTS addresses;


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
	personid INTEGER UNSIGNED REFERENCES persons(id) ON DELETE CASCADE, -- if NULL, donation is anonymous
	amount BIGINT NOT NULL, -- in EUR-cents
	donationdate DATE NOT NULL, -- only day relevant (i.e., hours, minutes, seconds, milliseconds neglected)
	dedication VARCHAR(1024),
	type ENUM('sms','bar','bank transfer','merchandise','online') NOT NULL,
	note VARCHAR(1024),
        import INTEGER UNSIGNED DEFAULT NULL REFERENCES imports(id) ON DELETE SET NULL -- if NULL, this donation is considered confirmed/validated. If not null, it is not yet validated by any user.
);

CREATE TABLE livesat (
	pid INTEGER UNSIGNED REFERENCES persons(id) ON DELETE CASCADE,
	aid INTEGER UNSIGNED REFERENCES addresses(id) ON DELETE CASCADE,
        ismain BOOLEAN NOT NULL, -- if true, the address is the person's main address. a person must not have more than one main address. a person may have several addresses, but no main address specified.
        PRIMARY KEY(pid, aid)
);

-- filter

CREATE TABLE criterion ( -- an abstract criterion defining a condition. must either refer to a connected_criterion, a property_criterion or a mountedfilter_criterion.
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	-- a criterion must always be owned by either a filter or a connected_criterion!
	type ENUM('validated_persons','validated_donations','confirmed_mailings','validated_addresses') NOT NULL -- the entity of the filter this criterion can be added to
);

CREATE TABLE filter ( -- defines a filter for a specific entity
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	criterion INTEGER UNSIGNED REFERENCES criterion(id) ON DELETE RESTRICT, -- the criterion for this filter. May only consist of property criterions for properties of the entity defined by 'type'.
	type ENUM('validated_persons','validated_donations','confirmed_mailings','validated_addresses') NOT NULL, -- the entity this filter is applicable to
	name VARCHAR(120), -- a name for this filter. can be null when anonymous
	anonymous BOOLEAN NOT NULL DEFAULT FALSE, -- anonymous filters are created inside other filters and only exist there
	privacy_status ENUM('privat', 'anzeigen', 'anzeigen, bearbeiten', 'anzeigen, bearbeiten, löschen') NOT NULL DEFAULT 'privat', -- have all users access to this filter?!
	owner VARCHAR(30) NOT NULL -- name of the user who created this filter
);

CREATE TABLE connected_criterion ( -- a concrete criterion that logically connects two abstract criterias
	id INTEGER UNSIGNED PRIMARY KEY REFERENCES criterion(id),
	-- the operand criterions must be for the same type (person/donation/address/mailing). this criterion's type is specified by those types.
	operand1 INTEGER UNSIGNED NOT NULL REFERENCES criterion(id),
	operand2 INTEGER UNSIGNED REFERENCES criterion(id), -- null iff logical_operator is 'NOT'
	logical_operator ENUM('AND','OR','XOR','AND_NOT','OR_NOT','NOT') NOT NULL
);

CREATE TABLE property_criterion ( -- a concrete criterion that compares an entities property with a given value
	id INTEGER UNSIGNED PRIMARY KEY REFERENCES criterion(id),
	property ENUM('givenname','surname','sex','email','company','emailnotification','postalnotification','title','person_note','telephone',
	'amount','donationdate','dedication','donation_type','donation_note',
	'mailing_date','mailing_type','mailing_medium',
	'street','postcode','city','ismain','country') NOT NULL, -- the property (column name) of the entity to be compared. Filters containing an 'ismain' criterion may solemnly be used as mounted into a person criterion, i.e. have no meaning on its own.
	-- this criterion's type is specified by the property used.
	relational_operator ENUM('EQUALS','GREATER','LESS','GREATER_EQ','LESS_EQ','LIKE','UNEQUAL','IS_NULL','IS_NOT_NULL') NOT NULL, -- must fit the property type, e.g. for ismain, you may only use 'EQUALS', 'UNEQUAL', 'IS_NULL', 'IS_NOT_NULL'
	-- the value the property is compared to (exactly one of those 5 must be set, except for when relational_operator is IS_NULL or IS_NOT_NULL, in which case none must be set). Which one is set must be according to the type of the property specified in 'property'.
	numValue DOUBLE, 
	strValue VARCHAR(1024),
	dateValue DATE,
	daysBack INT, -- if the property is a date, it will be compared with the current date minus daysBack
	boolValue BOOLEAN
);

CREATE TABLE mountedfilter_criterion ( -- a criterion which applies another filter. the result will be compared to a given constraint.
	id INTEGER UNSIGNED PRIMARY KEY REFERENCES criterion(id),
	mount INTEGER UNSIGNED NOT NULL REFERENCES filter(id), -- the filter that will be applied to a set of entities related to this entity
	-- if the mounted filter is of type mailing, address or donation, this criterion is of type person.
	-- if the mounted filter is of type mailing, this criterion is of type person.
	relational_operator ENUM('EQUALS','GREATER','LESS','GREATER_EQ','LESS_EQ','LIKE','UNEQUAL','IS_NULL','IS_NOT_NULL') NOT NULL,
	-- either count XOR property must be set. (i.e. not both)
	count INT, -- if set, the number of the filter result will be compared with this value
	property ENUM('givenname','surname','sex','email','company','emailnotification','postalnotification','title','person_note','telephone',
	'amount','donationdate','dedication','donation_type','donation_note',
	'mailing_date','mailing_type','mailing_medium',
	'street','postcode','city','ismain','country'), -- if set, either sum XOR avg must be set. the property (column name) of the entity for the mount filter. Must be a column name of the table the mounted filter operates on (must match its type).
	sum DOUBLE, -- must only be not-null if property is set. sum of the filter result's given property will be compared.
	avg DOUBLE -- must only be not-null if property is set. arithmetic average of the filter result's given property will be compared.
);

-- mailings

CREATE TABLE mailings (
	id INTEGER UNSIGNED PRIMARY KEY AUTO_INCREMENT,
        mailing_date DATE NOT NULL,
	mailing_type ENUM('allgemeiner Dankesbrief', 'Dankesbrief', 'Dauerspender Dankesbrief', 'Einzelspenden Dankesbrief', 'Infomaterial', 'Spendenaufruf', 'Spendenbrief'),
	mailing_medium ENUM('email', 'postal'),
	template INTEGER UNSIGNED REFERENCES mailing_templates(id) ON DELETE RESTRICT,
	UNIQUE(mailing_date, mailing_type, mailing_medium, template)
);

CREATE TABLE unconfirmed_mailings ( -- mailings that are not yet confirmed by the user
	id INTEGER UNSIGNED PRIMARY KEY REFERENCES mailings(id) ON DELETE CASCADE,
	creator VARCHAR(30) NOT NULL -- user who created the import (database user name)
);

CREATE VIEW confirmed_mailings AS SELECT * FROM mailings WHERE id NOT IN (SELECT id FROM unconfirmed_mailings); -- only confirmed mailings

CREATE TABLE sent_mailings (
	mailing_id INTEGER UNSIGNED NOT NULL REFERENCES mailings(id) ON DELETE CASCADE,
	person_id INTEGER UNSIGNED NOT NULL REFERENCES persons(id) ON DELETE CASCADE,
	PRIMARY KEY(mailing_id, person_id)
);

-- templates:

CREATE TABLE mailing_templates ( -- mailing template files in ODT/DOC format
	id INTEGER UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(120) NOT NULL, -- name of the template
	data BLOB NOT NULL
);

CREATE TABLE donation_confirmation_templates ( -- donation confirmation templates in ODT/DOC format
	id INTEGER UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(120) NOT NULL UNIQUE, -- name of the template
	data BLOB NOT NULL
);

-- donation confirmations:

CREATE TABLE donation_confirmations ( -- abstract confirmation, either for a single or multiple donations. must reference either single_donation_confirmations or multiple_donations_confirmations.
	id INTEGER UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	personid INTEGER UNSIGNED NOT NULL REFERENCES persons(id) ON DELETE CASCADE, -- donator
	template INTEGER UNSIGNED NOT NULL REFERENCES donation_confirmation_templates(id) ON DELETE RESTRICT,
	confirmation_date DATE NOT NULL -- date the confirmation was created
);

CREATE TABLE single_donation_confirmation ( -- confirmation for a single donation
	id INTEGER UNSIGNED PRIMARY KEY REFERENCES donation_confirmation(id),
	donationid INTEGER UNSIGNED NOT NULL REFERENCES donations(id) -- person of this donation MUST be the same as person of the confirmation
);

CREATE TABLE multiple_donations_confirmation ( -- confirmation for donations in a given period
	id INTEGER UNSIGNED PRIMARY KEY REFERENCES donation_confirmation(id),
	from_date DATE NOT NULL,
	to_date DATE NOT NULL -- must not be earlier then from. from/to to be understood inclusive.
);


-- action history

CREATE TABLE actions (
	id INTEGER UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
	actor VARCHAR(30) NOT NULL, -- user name of user having performed the action
	time TIMESTAMP NOT NULL,
	type ENUM('insert','update','delete') NOT NULL,
	entity ENUM('persons','addresses','livesat','imports','donations','criterion','filter','mailings','sent_mailings','mailing_templates', 'donation_confirmation_templates','donation_confirmations') NOT NULL,
	entityid VARCHAR(30) NOT NULL, -- id of entity that has changed. for livesat and sent_mailings, which are identified by two integers, the entityid will be saved with a dash in the middle, i.e. '118/54'.
	payload VARCHAR(10240) -- if type = 'insert', the data that has been inserted (w/o id), if type = 'updated', the new data, if type='delete', the old data. BLOBS not contained.
);


-- views for validated data:

CREATE VIEW validated_donations AS SELECT * FROM donations WHERE import IS NULL; -- only validated donations (i.e. no pending imports)

CREATE VIEW validated_persons AS SELECT * FROM persons p WHERE NOT EXISTS (SELECT id FROM donations d WHERE d.import IS NOT NULL AND d.personid = p.id); -- only validated persons (i.e. no pending imports)

CREATE VIEW validated_addresses AS SELECT * FROM addresses a WHERE NOT EXISTS (SELECT * FROM livesat l JOIN persons p ON (l.pid = p.id) WHERE l.aid = a.id AND p.id NOT IN (SELECT id FROM validated_persons)); -- only validated addresses (i.e. no pending imports)

-- triggers for action history:

DELIMITER //

CREATE TRIGGER addresses_log_insert AFTER INSERT ON addresses FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'addresses', CAST(NEW.id AS CHAR(30)), CONCAT(NEW.street, ', ', NEW.postcode, ', ', NEW.city, ', ', NEW.country));
END;//

CREATE TRIGGER addresses_log_update AFTER UPDATE ON addresses FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'addresses', CAST(NEW.id AS CHAR(30)), CONCAT(NEW.street, ', ', NEW.postcode, ', ', NEW.city, ', ', NEW.country));
END;//

CREATE TRIGGER addresses_log_delete AFTER DELETE ON addresses FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'addresses', CAST(OLD.id AS CHAR(30)), CONCAT(OLD.street, ', ', OLD.postcode, ', ', OLD.city, ', ', OLD.country));
END;//


CREATE TRIGGER persons_log_insert AFTER INSERT ON persons FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'persons', CAST(NEW.id AS CHAR(30)), CONCAT(IFNULL(NEW.title, 'no title'), ', ', IFNULL(NEW.givenname, 'no given name'), ', ', IFNULL(NEW.surname, 'no surname'), ', ', IFNULL(NEW.company, 'no company'), ', ', IFNULL(NEW.email, 'no email'), ', ', NEW.sex, ', ', IFNULL(NEW.telephone, 'no phone'), ', ', 'email: ', NEW.emailnotification, ', ', 'postal: ', NEW.postalnotification, ', ', 'note: ', IFNULL(NEW.note, 'none')));
END;//

CREATE TRIGGER persons_log_update AFTER UPDATE ON persons FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'persons', CAST(NEW.id AS CHAR(30)), CONCAT(IFNULL(NEW.title, 'no title'), ', ', IFNULL(NEW.givenname, 'no given name'), ', ', IFNULL(NEW.surname, 'no surname'), ', ', IFNULL(NEW.company, 'no company'), ', ', IFNULL(NEW.email, 'no email'), ', ', NEW.sex, ', ', IFNULL(NEW.telephone, 'no phone'), ', ', 'email: ', NEW.emailnotification, ', ', 'postal: ', NEW.postalnotification, ', ', 'note: ', IFNULL(NEW.note, 'none')));
END;//

CREATE TRIGGER persons_log_delete AFTER DELETE ON persons FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'persons', CAST(OLD.id AS CHAR(30)), CONCAT(IFNULL(OLD.title, 'no title'), ', ', IFNULL(OLD.givenname, 'no given name'), ', ', IFNULL(OLD.surname, 'no surname'), ', ', IFNULL(OLD.company, 'no company'), ', ', IFNULL(OLD.email, 'no email'), ', ', OLD.sex, ', ', IFNULL(OLD.telephone, 'no phone'), ', ', 'email: ', OLD.emailnotification, ', ', 'postal: ', OLD.postalnotification, ', ', 'note: ', IFNULL(OLD.note, 'none')));
END;//


CREATE TRIGGER livesat_log_insert AFTER INSERT ON livesat FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'livesat', CONCAT(CAST(NEW.pid AS CHAR(14)), '/', CAST(NEW.aid AS CHAR(14))), IF(NEW.ismain, 'main', 'not main'));
END;//

CREATE TRIGGER livesat_log_update AFTER UPDATE ON livesat FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'livesat', CONCAT(CAST(NEW.pid AS CHAR(14)), '/', CAST(NEW.aid AS CHAR(14))), IF(NEW.ismain, 'main', 'not main'));
END;//

CREATE TRIGGER livesat_log_delete AFTER DELETE ON livesat FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'livesat', CONCAT(CAST(OLD.pid AS CHAR(14)), '/', CAST(OLD.aid AS CHAR(14))), IF(OLD.ismain, 'was main', 'was not main'));
END;//


CREATE TRIGGER imports_log_insert AFTER INSERT ON imports FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'imports', CAST(NEW.id AS CHAR(30)), CONCAT(NEW.creator, ', ', NEW.import_date, ', ', NEW.source));
END;//

CREATE TRIGGER imports_log_update AFTER UPDATE ON imports FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'imports', CAST(NEW.id AS CHAR(30)), CONCAT(NEW.creator, ', ', NEW.import_date, ', ', NEW.source));
END;//

CREATE TRIGGER imports_log_delete AFTER DELETE ON imports FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'imports', CAST(OLD.id AS CHAR(30)), CONCAT(OLD.creator, ', ', OLD.import_date, ', ', OLD.source));
END;//


CREATE TRIGGER donations_log_insert AFTER INSERT ON donations FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'donations', CAST(NEW.id AS CHAR(30)), CONCAT('person: ', NEW.personid, ', ', NEW.amount, ' EUR-cents', ', ', NEW.donationdate, ', ', 'dedication: ', IFNULL(NEW.dedication, 'none'), ', ', NEW.type, ', ', 'note: ', IFNULL(NEW.note, 'none')));
END;//

CREATE TRIGGER donations_log_update AFTER UPDATE ON donations FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'donations', CAST(NEW.id AS CHAR(30)), CONCAT('person: ', NEW.personid, ', ', NEW.amount, ' EUR-cents', ', ', NEW.donationdate, ', ', 'dedication: ', IFNULL(NEW.dedication, 'none'), ', ', NEW.type, ', ', 'note: ', IFNULL(NEW.note, 'none')));
END;//

CREATE TRIGGER donations_log_delete AFTER DELETE ON donations FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'donations', CAST(OLD.id AS CHAR(30)), CONCAT('person: ', OLD.personid, ', ', OLD.amount, ' EUR-cents', ', ', OLD.donationdate, ', ', 'dedication: ', IFNULL(OLD.dedication, 'none'), ', ', OLD.type, ', ', 'note: ', IFNULL(OLD.note, 'none')));
END;//


CREATE TRIGGER connected_criterion_log_insert AFTER INSERT ON connected_criterion FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'criterion', CAST(NEW.id AS CHAR(30)), CONCAT(IF(NEW.operand2 IS NULL, CONCAT(NEW.logical_operator, ' ', NEW.operand1), CONCAT(NEW.operand1, ' ', NEW.logical_operator, ' ', NEW.operand2)), '; on type ', (SELECT type FROM criterion WHERE id = NEW.id)));
END;//

CREATE TRIGGER connected_criterion_log_update AFTER UPDATE ON connected_criterion FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'criterion', CAST(NEW.id AS CHAR(30)), CONCAT(IF(NEW.operand2 IS NULL, CONCAT(NEW.logical_operator, ' ', NEW.operand1), CONCAT(NEW.operand1, ' ', NEW.logical_operator, ' ', NEW.operand2)), '; on type ', (SELECT type FROM criterion WHERE id = NEW.id)));
END;//

CREATE TRIGGER connected_criterion_log_delete AFTER DELETE ON connected_criterion FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'criterion', CAST(OLD.id AS CHAR(30)), CONCAT(IF(OLD.operand2 IS NULL, CONCAT(OLD.logical_operator, ' ', OLD.operand1), CONCAT(OLD.operand1, ' ', OLD.logical_operator, ' ', OLD.operand2))));
END;//


CREATE TRIGGER property_criterion_log_insert AFTER INSERT ON property_criterion FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'criterion', CAST(NEW.id AS CHAR(30)), CONCAT(NEW.property, ' ', NEW.relational_operator, ' ', IFNULL(NEW.numValue, ''), IFNULL(NEW.strValue, ''), IFNULL(NEW.dateValue, ''), IF(NEW.daysBack IS NULL, '', CONCAT(NEW.daysBack, ' days back')), IFNULL(NEW.boolValue, ''), '; on type ', (SELECT type FROM criterion WHERE id = NEW.id)));
END;//

CREATE TRIGGER property_criterion_log_update AFTER UPDATE ON property_criterion FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'criterion', CAST(NEW.id AS CHAR(30)), CONCAT(NEW.property, ' ', NEW.relational_operator, ' ', IFNULL(NEW.numValue, ''), IFNULL(NEW.strValue, ''), IFNULL(NEW.dateValue, ''), IF(NEW.daysBack IS NULL, '', CONCAT(NEW.daysBack, ' days back')), IFNULL(NEW.boolValue, ''), '; on type ', (SELECT type FROM criterion WHERE id = NEW.id)));
END;//

CREATE TRIGGER property_criterion_log_delete AFTER DELETE ON property_criterion FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'criterion', CAST(OLD.id AS CHAR(30)), CONCAT(OLD.property, ' ', OLD.relational_operator, ' ', IFNULL(OLD.numValue, ''), IFNULL(OLD.strValue, ''), IFNULL(OLD.dateValue, ''), IF(OLD.daysBack IS NULL, '', CONCAT(OLD.daysBack, ' days back')), IFNULL(OLD.boolValue, '')));
END;//


CREATE TRIGGER mountedfilter_criterion_log_insert AFTER INSERT ON mountedfilter_criterion FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'criterion', CAST(NEW.id AS CHAR(30)), CONCAT('mount filter #', NEW.mount, ' ON ', IF (NEW.count IS NULL, IF(NEW.sum IS NULL, CONCAT('average ', NEW.relational_operator, ' ', NEW.avg), CONCAT('sum ', NEW.relational_operator, ' ', NEW.sum)), CONCAT('count ', NEW.relational_operator, ' ', NEW.count)), '; on type ', (SELECT type FROM criterion WHERE id = NEW.id)));
END;//

CREATE TRIGGER mountedfilter_criterion_log_update AFTER UPDATE ON mountedfilter_criterion FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'criterion', CAST(NEW.id AS CHAR(30)), CONCAT('mount filter #', NEW.mount, ' ON ', IF (NEW.count IS NULL, IF(NEW.sum IS NULL, CONCAT('average ', NEW.relational_operator, ' ', NEW.avg), CONCAT('sum ', NEW.relational_operator, ' ', NEW.sum)), CONCAT('count ', NEW.relational_operator, ' ', NEW.count)), '; on type ', (SELECT type FROM criterion WHERE id = NEW.id)));
END;//

CREATE TRIGGER mountedfilter_criterion_log_delete AFTER DELETE ON mountedfilter_criterion FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'criterion', CAST(OLD.id AS CHAR(30)), CONCAT('mount filter #', OLD.mount, ' ON ', IF (OLD.count IS NULL, IF(OLD.sum IS NULL, CONCAT('average ', OLD.relational_operator, ' ', OLD.avg), CONCAT('sum ', OLD.relational_operator, ' ', OLD.sum)), CONCAT('count ', OLD.relational_operator, ' ', OLD.count))));
END;//


CREATE TRIGGER filter_log_insert AFTER INSERT ON filter FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'filter', CAST(NEW.id AS CHAR(30)), CONCAT('criterion #', NEW.criterion, ', type ', NEW.type, IF(NEW.name IS NULL, '', CONCAT('name: "', NEW.name, '", ')), IF(NEW.anonymous, 'anonymous, ', ''), 'privacy: ', NEW.privacy_status, ', owner: ', NEW.owner));
END;//

CREATE TRIGGER filter_log_update AFTER UPDATE ON filter FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'filter', CAST(NEW.id AS CHAR(30)), CONCAT('criterion #', NEW.criterion, ', type ', NEW.type, IF(NEW.name IS NULL, '', CONCAT('name: "', NEW.name, '", ')), IF(NEW.anonymous, 'anonymous, ', ''), 'privacy: ', NEW.privacy_status, ', owner: ', NEW.owner));
END;//

CREATE TRIGGER filter_log_delete AFTER DELETE ON filter FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'filter', CAST(OLD.id AS CHAR(30)), CONCAT('criterion #', OLD.criterion, ', type ', OLD.type, IF(OLD.name IS NULL, '', CONCAT('name: "', OLD.name, '", ')), IF(OLD.anonymous, 'anonymous, ', ''), 'privacy: ', OLD.privacy_status, ', owner: ', OLD.owner));
END;//


CREATE TRIGGER mailings_log_insert AFTER INSERT ON mailings FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'mailings', CAST(NEW.id AS CHAR(30)), CONCAT(NEW.mailing_type, ', ', NEW.mailing_medium, ', ', NEW.mailing_date, IF(NEW.template IS NULL, '',CONCAT('template #', NEW.template))));
END;//

CREATE TRIGGER mailings_log_update AFTER UPDATE ON mailings FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'mailings', CAST(NEW.id AS CHAR(30)), CONCAT(NEW.mailing_type, ', ', NEW.mailing_medium, ', ', NEW.mailing_date, IF(NEW.template IS NULL, '',CONCAT('template #', NEW.template))));
END;//

CREATE TRIGGER mailings_log_delete AFTER DELETE ON mailings FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'mailings', CAST(OLD.id AS CHAR(30)), CONCAT(OLD.mailing_type, ', ', OLD.mailing_medium, ', ', OLD.mailing_date, IF(OLD.template IS NULL, '',CONCAT('template #', OLD.template))));
END;//

CREATE TRIGGER unconfirmed_mailings_log_insert AFTER INSERT ON unconfirmed_mailings FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'mailings', CAST(NEW.id AS CHAR(30)), CONCAT('now considered unconfirmed. Creator: ', NEW.creator));
END;//

CREATE TRIGGER unconfirmed_mailings_log_update AFTER UPDATE ON unconfirmed_mailings FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'mailings', CAST(NEW.id AS CHAR(30)), CONCAT('now considered unconfirmed. Creator: ', NEW.creator));
END;//

CREATE TRIGGER unconfirmed_mailings_log_delete AFTER DELETE ON unconfirmed_mailings FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'mailings', CAST(OLD.id AS CHAR(30)), 'now considered confirmed.');
END;//


CREATE TRIGGER sent_mailings_log_insert AFTER INSERT ON sent_mailings FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'sent_mailings', CONCAT(CAST(NEW.mailing_id AS CHAR(14)), '/', CAST(NEW.person_id AS CHAR(14))), NULL);
END;//

CREATE TRIGGER sent_mailings_log_update AFTER UPDATE ON sent_mailings FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'sent_mailings', CONCAT(CAST(NEW.mailing_id AS CHAR(14)), '/', CAST(NEW.person_id AS CHAR(14))), NULL);
END;//

CREATE TRIGGER sent_mailings_log_delete AFTER DELETE ON sent_mailings FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'sent_mailings', CONCAT(CAST(OLD.mailing_id AS CHAR(14)), '/', CAST(OLD.person_id AS CHAR(14))), NULL);
END;//


CREATE TRIGGER mailing_templates_log_insert AFTER INSERT ON mailing_templates FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'mailing_templates', CAST(NEW.id AS CHAR(30)), CONCAT('name: "', NEW.name, '"'));
END;//

CREATE TRIGGER mailing_templates_log_update AFTER UPDATE ON mailing_templates FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'mailing_templates', CAST(NEW.id AS CHAR(30)), CONCAT('name: "', NEW.name, '"'));
END;//

CREATE TRIGGER mailing_templates_log_delete AFTER DELETE ON mailing_templates FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'mailing_templates', CAST(OLD.id AS CHAR(30)), CONCAT('name: "', OLD.name, '"'));
END;//


CREATE TRIGGER donation_confirmation_templates_log_insert AFTER INSERT ON donation_confirmation_templates FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'donation_confirmation_templates', CAST(NEW.id AS CHAR(30)), CONCAT('name: "', NEW.name, '"'));
END;//

CREATE TRIGGER donation_confirmation_templates_log_update AFTER UPDATE ON donation_confirmation_templates FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'donation_confirmation_templates', CAST(NEW.id AS CHAR(30)), CONCAT('name: "', NEW.name, '"'));
END;//

CREATE TRIGGER donation_confirmation_templates_log_delete AFTER DELETE ON donation_confirmation_templates FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'donation_confirmation_templates', CAST(OLD.id AS CHAR(30)), CONCAT('name: "', OLD.name, '"'));
END;//


CREATE TRIGGER donation_confirmations_log_insert AFTER INSERT ON donation_confirmations FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'insert', 'donation_confirmations', CAST(NEW.id AS CHAR(30)), CONCAT('person #', NEW.personid, ', ', 'template #', NEW.template, ', date: ', NEW.confirmation_date));
END;//

CREATE TRIGGER donation_confirmations_log_update AFTER UPDATE ON donation_confirmations FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'donation_confirmations', CAST(NEW.id AS CHAR(30)), CONCAT('person #', NEW.personid, ', ', 'template #', NEW.template, ', date: ', NEW.confirmation_date));
END;//

CREATE TRIGGER donation_confirmations_log_delete AFTER DELETE ON donation_confirmations FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'delete', 'donation_confirmations', CAST(OLD.id AS CHAR(30)), CONCAT('person #', OLD.personid, ', ', 'template #', OLD.template, ', date: ', OLD.confirmation_date));
END;//

CREATE TRIGGER single_donation_confirmation_log_insert AFTER INSERT ON single_donation_confirmation FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'donation_confirmations', CAST(NEW.id AS CHAR(30)), CONCAT('for single donation #', NEW.donationid));
END;//

CREATE TRIGGER single_donation_confirmation_log_update AFTER UPDATE ON single_donation_confirmation FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'donation_confirmations', CAST(NEW.id AS CHAR(30)), CONCAT('for single donation #', NEW.donationid));
END;//

CREATE TRIGGER single_donation_confirmation_log_delete AFTER DELETE ON single_donation_confirmation FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'donation_confirmations', CAST(OLD.id AS CHAR(30)), CONCAT('for single donation #', OLD.donationid));
END;//

CREATE TRIGGER multiple_donations_confirmation_log_insert AFTER INSERT ON multiple_donations_confirmation FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'donation_confirmations', CAST(NEW.id AS CHAR(30)), CONCAT('for all donations between ', NEW.from_date, ' to ', NEW.to_date));
END;//

CREATE TRIGGER multiple_donations_confirmation_log_update AFTER UPDATE ON multiple_donations_confirmation FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'donation_confirmations', CAST(NEW.id AS CHAR(30)), CONCAT('for all donations between ', NEW.from_date, ' to ', NEW.to_date));
END;//

CREATE TRIGGER multiple_donations_confirmation_log_delete AFTER DELETE ON multiple_donations_confirmation FOR EACH ROW
BEGIN
	INSERT INTO actions(actor, time, type, entity, entityid, payload) VALUES (SUBSTRING_INDEX(USER(),'@',1), NOW(), 'update', 'donation_confirmations', CAST(OLD.id AS CHAR(30)), CONCAT('for all donations between ', OLD.from_date, ' to ', OLD.to_date));
END;//




DELIMITER ;



