-- returns all object history of the object, specified by id
-- in the specified time frame
-- all input parameters should not be null
DROP FUNCTION get_object_childs (object_qualifier uuid,
 date_begin timestamp,
 date_end timestamp);

CREATE OR REPLACE FUNCTION get_object_childs(object_qualifier uuid,
 date_begin timestamp,
 date_end timestamp)
RETURNS uuid[] AS $$
DECLARE
 result_list uuid[] := '{}';
 childs_list uuid[] := '{}';
 temp_list uuid[] := '{}';
 r uuid;
BEGIN

result_list:= ARRAY(  SELECT rec.qualifier FROM record rec
  	LEFT OUTER JOIN record_reference r_ref ON rec.belong_to_id=r_ref.id
  	LEFT OUTER JOIN record parent_record ON r_ref.record_to_qualifier=parent_record.qualifier
	LEFT OUTER JOIN document begin_doc ON r_ref.begin_document_id = begin_doc.id
	LEFT OUTER JOIN document end_doc ON r_ref.end_document_id = end_doc.id
 -- INTO result_list
  WHERE parent_record.qualifier=object_qualifier	);
  
  --AND begin_doc.document_date <= date_begin

 IF result_list <> NULL THEN
 	
	FOR r IN SELECT * FROM result_list
    LOOP
		SELECT * FROM get_object_childs(r,date_begin,date_end) INTO temp_list;
        SELECT array_cat(childs_list,temp_list) INTO childs_list;
    END LOOP;
	
 END IF;
 
 SELECT * FROM array_cat(result_list,childs_list) INTO result_list;
 
 RETURN result_list;

END;
$$ LANGUAGE plpgsql;
