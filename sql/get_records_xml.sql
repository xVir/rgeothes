CREATE OR REPLACE FUNCTION get_records_xml(sqlQuery text)
  RETURNS text AS
$BODY$
DECLARE
 
current_record RECORD; 
current_name RECORD;
current_doc RECORD;
current_location RECORD;
belong_to_reference RECORD;
contains_reference RECORD;

xml_result text = '';
result text = '';
current_uuid uuid;


BEGIN

result := E'<?xml version="1.0" encoding="UTF-8" standalone="yes"?>\n';
result := result || E'<list>\n';

FOR current_record IN EXECUTE sqlQuery LOOP
 --RAISE NOTICE 'record: %', current_record.qualifier;
 current_uuid := current_record.qualifier;

 result := result || E'<record>\n';
 result := result || format(E'<qualifier>%s</qualifier>\n',current_uuid);

 --names
 result := result || E'<names>\n';
 FOR current_name IN SELECT * FROM name WHERE qualifier=current_uuid LOOP
  result := result || E'<name>\n';
  result := result || format(E'<name>%s</name>\n',current_name.name);
  result := result || format(E'<type>%s</type>\n',current_name.type);
  result := result || format(E'<language>%s</language>\n',current_name.lang);

  IF current_name.end_document_id IS NOT NULL THEN
    SELECT * INTO current_doc FROM document WHERE document.id = current_name.begin_document_id;
    result := result || E'<beginDocument>\n';
    result := result || format(E'<uri>%s</uri>\n',current_doc.uri);
    result := result || format(E'<description>%s</description>\n',current_doc.description);
    result := result || format(E'<date>%s</date>\n',current_doc.document_date);
    result := result || format(E'<creationDate>%s</creationDate>\n',current_doc.creation_date);
    result := result || E'</beginDocument>\n';
  END IF;

  IF current_name.end_document_id IS NOT NULL THEN
    SELECT * INTO current_doc FROM document WHERE document.id = current_name.end_document_id;
    result := result || E'<endDocument>\n';
    result := result || format(E'<uri>%s</uri>\n',current_doc.uri);
    result := result || format(E'<description>%s</description>\n',current_doc.description);
    result := result || format(E'<date>%s</date>\n',current_doc.document_date);
    result := result || format(E'<creationDate>%s</creationDate>\n',current_doc.creation_date);
    result := result || E'</endDocument>\n';
  END IF;

  result := result || E'</name>\n';
 END LOOP;
 result := result || E'</names>\n';
 
 --locations
 result := result || E'<locations>\n';
 FOR current_location IN SELECT * FROM location WHERE qualifier=current_uuid LOOP
  --location types: point, e. t. c....
  IF current_location.location_type='point' THEN
    result := result || E'<point>\n';
    result := result || format(E'<latitude>%s</latitude>',current_location.location_point[0] );
    result := result || format(E'<longitude>%s</longitude>',current_location.location_point[1]);

    IF current_location.begin_document_id IS NOT NULL THEN
     SELECT * INTO current_doc FROM document WHERE document.id = current_location.end_document_id;
     result := result || E'<beginDocument>\n';
     result := result || format(E'<uri>%s</uri>\n',current_doc.uri);
     result := result || format(E'<description>%s</description>\n',current_doc.description);
     result := result || format(E'<date>%s</date>\n',current_doc.document_date);
     result := result || format(E'<creationDate>%s</creationDate>\n',current_doc.creation_date);
     result := result || E'</beginDocument>\n';
    END IF;

    IF current_location.end_document_id IS NOT NULL THEN
     SELECT * INTO current_doc FROM document WHERE document.id = current_location.end_document_id;
     result := result || E'<endDocument>\n';
     result := result || format(E'<uri>%s</uri>\n',current_doc.uri);
     result := result || format(E'<description>%s</description>\n',current_doc.description);
     result := result || format(E'<date>%s</date>\n',current_doc.document_date);
     result := result || format(E'<creationDate>%s</creationDate>\n',current_doc.creation_date);
     result := result || E'</endDocument>\n';
    END IF;
   
    result := result || E'</point>\n';
  END IF;
  
 END LOOP;
 result := result || E'</locations>\n';

 --contains
 result := result || E'<contains>\n';
 FOR contains_reference IN SELECT * FROM record_reference WHERE record_reference.record_to_qualifier=current_uuid LOOP
  
  result := result || E'<recordReference>\n';

  result := result || format(E'<recordFromQualifier>%s</recordFromQualifier>\n', contains_reference.record_from_qualifier);
  result := result || format(E'<recordToQualifier>%s</recordToQualifier>\n', contains_reference.record_to_qualifier);

  IF contains_reference.begin_document_id IS NOT NULL THEN
    SELECT * INTO current_doc FROM document WHERE document.id = contains_reference.begin_document_id;
    result := result || E'<beginDocument>\n';
    result := result || format(E'<uri>%s</uri>\n',current_doc.uri);
    result := result || format(E'<description>%s</description>\n',current_doc.description);
    result := result || format(E'<date>%s</date>\n',current_doc.document_date);
    result := result || format(E'<creationDate>%s</creationDate>\n',current_doc.creation_date);
    result := result || E'</beginDocument>\n';
  END IF;

  IF contains_reference.end_document_id IS NOT NULL THEN
   SELECT * INTO current_doc FROM document WHERE document.id = contains_reference.end_document_id;
   result := result || E'<endDocument>\n';
   result := result || format(E'<uri>%s</uri>\n',current_doc.uri);
   result := result || format(E'<description>%s</description>\n',current_doc.description);
   result := result || format(E'<date>%s</date>\n',current_doc.document_date);
   result := result || format(E'<creationDate>%s</creationDate>\n',current_doc.creation_date);
   result := result || E'</endDocument>\n';
  END IF;

  result := result || E'</recordReference>\n';
 
 END LOOP;
 result := result || E'</contains>\n'; 

 --belongTo
 result := result || E'<belongTo>\n';
 FOR belong_to_reference IN SELECT * FROM record_reference WHERE record_reference.record_from_qualifier=current_uuid LOOP
  
  result := result || E'<recordReference>\n';

  result := result || format(E'<recordFromQualifier>%s</recordFromQualifier>\n', belong_to_reference.record_from_qualifier);
  result := result || format(E'<recordToQualifier>%s</recordToQualifier>\n', belong_to_reference.record_to_qualifier);

  IF belong_to_reference.begin_document_id IS NOT NULL THEN
    SELECT * INTO current_doc FROM document WHERE document.id = belong_to_reference.begin_document_id;
    result := result || E'<beginDocument>\n';
    result := result || format(E'<uri>%s</uri>\n',current_doc.uri);
    result := result || format(E'<description>%s</description>\n',current_doc.description);
    result := result || format(E'<date>%s</date>\n',current_doc.document_date);
    result := result || format(E'<creationDate>%s</creationDate>\n',current_doc.creation_date);
    result := result || E'</beginDocument>\n';
  END IF;

  IF belong_to_reference.end_document_id IS NOT NULL THEN
   SELECT * INTO current_doc FROM document WHERE document.id = belong_to_reference.end_document_id;
   result := result || E'<endDocument>\n';
   result := result || format(E'<uri>%s</uri>\n',current_doc.uri);
   result := result || format(E'<description>%s</description>\n',current_doc.description);
   result := result || format(E'<date>%s</date>\n',current_doc.document_date);
   result := result || format(E'<creationDate>%s</creationDate>\n',current_doc.creation_date);
   result := result || E'</endDocument>\n';
  END IF;

  result := result || E'</recordReference>\n';
 
 END LOOP;
 result := result || E'</belongTo>\n';


 result := result || E'</record>\n';

END LOOP;

result := result || E'</list>\n';

xml_result := result;
RAISE NOTICE 'xml_result: %', xml_result;
RETURN xml_result;
END;
$BODY$
  LANGUAGE plpgsql;

SELECT get_records_xml('SELECT t.qualifier FROM record t LIMIT 2');