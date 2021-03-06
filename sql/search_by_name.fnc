CREATE OR REPLACE FUNCTION search_by_name(term_name text, term_type text DEFAULT NULL,
 term_lang text DEFAULT 'ru',
 term_name_begin timestamp DEFAULT NULL,
 term_name_end timestamp DEFAULT NULL) RETURNS text AS $$
DECLARE
 result text = '';
 main_record uuid;
 empty_result text = '';
 childs_list uuid[] := '{}';
 i int;
BEGIN

 -- checking input parameters
 IF term_name ISNULL THEN
  RAISE EXCEPTION 'term_name should not be null';
  RETURN '';
 END IF;

 IF term_name_begin ISNULL THEN
  term_name_begin := '-infinity';
 END IF;

 IF term_name_end ISNULL THEN
  term_name_end := 'infinity';
 END IF;

 -- first get identifiers of objects

 -- get first object, which contains *term_name*

 IF term_type <> NULL THEN

  SELECT rec.qualifier FROM record rec
   LEFT OUTER JOIN name rec_name ON rec.qualifier=rec_name.qualifier
   LEFT OUTER JOIN document doc_begin ON rec_name.begin_document_id = doc_begin.id
   LEFT OUTER JOIN document doc_end ON rec_name.end_document_id = doc_end.id
  INTO main_record
  WHERE rec_name.name = term_name
   AND rec_name.lang = term_lang
   AND rec_name.type = term_type
   AND doc_begin.document_date >= term_name_begin
   AND doc_end.document_date <= term_name_end
  GROUP BY rec.qualifier LIMIT 1;

 ELSE

  SELECT rec.qualifier FROM record rec
   LEFT OUTER JOIN name rec_name ON rec.qualifier=rec_name.qualifier
   LEFT OUTER JOIN document doc_begin ON rec_name.begin_document_id = doc_begin.id
   LEFT OUTER JOIN document doc_end ON rec_name.end_document_id = doc_end.id
  INTO main_record
  WHERE rec_name.name=term_name
   AND rec_name.lang=term_lang
   AND doc_begin.document_date >= term_name_begin
   AND doc_end.document_date <= term_name_end
  GROUP BY rec.qualifier LIMIT 1;

 END IF;

 RAISE DEBUG '%',main_record;
 
 IF main_record ISNULL THEN
 	RETURN empty_result;
 END IF;	
 
 
 -- get child records
 childs_list := get_object_childs(main_record,term_name_begin,term_name_end);
 
 RAISE DEBUG '%',childs_list;

 -- create xml text with identifiers

FOR i IN array_lower(childs_list, 1) .. array_upper(childs_list, 1)
  LOOP
    result := childs_list[i] + ' ';
  END LOOP;
 

 RETURN result;
END;
$$ LANGUAGE plpgsql;
