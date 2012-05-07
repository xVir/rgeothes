-- returns all object history of the object, specified by id
-- in the specified time frame
-- all input parameters should not be null
CREATE OR REPLACE FUNCTION get_object_history(object_qualifier uuid, 
	date_begin timestamp, 
	date_end timestamp)
RETURNS SETOF uuid AS $$
DECLARE
 result_list uuid[];
 forward_neighbor uuid;
 back_neighbor uuid;
BEGIN
	
	SELECT array_append(result_list,object_qualifier) INTO result_list;
	
	back_neighbor := object_qualifier;
	forward_neighbor := object_qualifier;
	
	-- getting back neighbor
	SELECT r_ref.record_to_qualifier FROM 
	thesaurus_record rec  
		JOIN record_reference r_ref ON rec.previous_record_reference_id=r_ref.id
		JOIN thesaurus_name rec_name ON r_ref.record_to_qualifier = rec_name.qualifier
		JOIN thesaurus_document begin_doc ON rec_name.begin_document_id = begin_doc.document_date
		JOIN thesaurus_document end_doc ON rec_name.end_document_id = end_doc.document_date
	INTO back_neighbor
	WHERE rec.qualifier = back_neighbor 
		AND begin_doc.document_date <= date_end
		AND end_doc.document_date >= date_begin;
	
	-- getting forward neighbor
	SELECT rec.qualifer  FROM  record_reference r_ref 
		JOIN thesaurus_record rec ON rec.previous_record_reference_id=r_ref.id
		JOIN thesaurus_name rec_name ON rec.qualifier=rec_name.qualifier
		JOIN thesaurus_document begin_doc ON rec_name.begin_document_id = begin_doc.document_date
		JOIN thesaurus_document end_doc ON rec_name.end_document_id = end_doc.document_date
	INTO forward_neighbor
	WHERE r_ref.record_to_qualifier = forward_neighbor
		AND begin_doc.document_date <= date_end
		AND end_doc.document_date >= date_begin;		 
		
	IF back_neighbor <> NULL THEN
		SELECT array_append(result_list,back_neighbor) INTO result_list;
	END IF;
	
	IF forward_neighbor <> NULL THEN
		SELECT array_append(result_list,forward_neighbor) INTO result_list;
	END IF;
	
	
	
END;
$$ LANGUAGE plpgsql;