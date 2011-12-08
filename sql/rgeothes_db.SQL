/*
Created: 28.08.2011
Modified: 09.12.2011
Project: RgeoThes
Model: UTI-Integration
Company: ICT SBRAS
Author: Danil Skachkov
Version: 1.11.8.28
Database: PostgreSQL 9.0
*/

-- Create user data types section -------------------------------------------------

CREATE TYPE "point_type" AS
 ( p point )
;

CREATE TYPE "circle_type" AS
 ( data circle
 )
;

CREATE TYPE "line_type" AS
 ( line path )
;

CREATE TYPE "rectangle_type" AS
 ( rect box )
;

CREATE TYPE "polygon_type" AS
 ( p polygon )
;

-- Create tables section -------------------------------------------------

-- Table thesaurus_record

CREATE TABLE "thesaurus_record"(
 "qualifier" UUID NOT NULL,
 "previous_record_reference_id" Bigint,
 "belong_to_id" Bigint
)
WITH (OIDS=FALSE)
;

-- Add keys for table thesaurus_record

ALTER TABLE "thesaurus_record" ADD CONSTRAINT "Key1" PRIMARY KEY ("qualifier")
;

-- Table thesaurus_name

CREATE TABLE "thesaurus_name"(
 "id" BigSerial NOT NULL,
 "name" Character varying(100) NOT NULL,
 "type" Character varying(50) NOT NULL,
 "lang" Character varying(10) NOT NULL,
 "qualifier" UUID NOT NULL,
 "begin_document_id" Bigint,
 "end_document_id" Bigint
)
WITH (OIDS=FALSE)
;

-- Add keys for table thesaurus_name

ALTER TABLE "thesaurus_name" ADD CONSTRAINT "Key2" PRIMARY KEY ("id","qualifier")
;

-- Table thesaurus_location

CREATE TABLE "thesaurus_location"(
 "qualifier" UUID NOT NULL,
 "id" BigSerial NOT NULL,
 "begin_document_id" Bigint,
 "end_document_id" Bigint,
 "location_type" Character varying(20) NOT NULL,
 "point_simple" "point_type",
 "rectangle_simple" "rectangle_type",
 "polygon_simple" "polygon_type",
 "line_simple" "line_type",
 "circle_simple" "circle_type"
)
WITH (OIDS=FALSE)
;

-- Add keys for table thesaurus_location

ALTER TABLE "thesaurus_location" ADD CONSTRAINT "Key3" PRIMARY KEY ("qualifier","id")
;

-- Table thesaurus_document

CREATE TABLE "thesaurus_document"(
 "id" BigSerial NOT NULL,
 "uri" Text NOT NULL,
 "description" Text NOT NULL,
 "document_date" Date NOT NULL,
 "creation_date" Date NOT NULL
)
WITH (OIDS=FALSE)
;

-- Add keys for table thesaurus_document

ALTER TABLE "thesaurus_document" ADD CONSTRAINT "Key4" PRIMARY KEY ("id")
;

-- Table record_reference

CREATE TABLE "record_reference"(
 "id" BigSerial NOT NULL,
 "document_id" Bigint NOT NULL,
 "record_from_qualifier" UUID NOT NULL,
 "record_to_qualifier" UUID NOT NULL
)
WITH (OIDS=FALSE)
;

-- Add keys for table record_reference

ALTER TABLE "record_reference" ADD CONSTRAINT "Key5" PRIMARY KEY ("id","record_from_qualifier","record_to_qualifier")
;

-- Create relationships section ------------------------------------------------- 

ALTER TABLE "thesaurus_name" ADD CONSTRAINT "ref_name" FOREIGN KEY ("qualifier") REFERENCES "thesaurus_record" ("qualifier") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "thesaurus_location" ADD CONSTRAINT "Relationship4" FOREIGN KEY ("qualifier") REFERENCES "thesaurus_record" ("qualifier") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "thesaurus_name" ADD CONSTRAINT "begin_document" FOREIGN KEY ("begin_document_id") REFERENCES "thesaurus_document" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "thesaurus_name" ADD CONSTRAINT "end_document" FOREIGN KEY ("end_document_id") REFERENCES "thesaurus_document" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "record_reference" ADD CONSTRAINT "Relationship6" FOREIGN KEY ("document_id") REFERENCES "thesaurus_document" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "record_reference" ADD CONSTRAINT "Relationship7" FOREIGN KEY ("record_from_qualifier") REFERENCES "thesaurus_record" ("qualifier") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "record_reference" ADD CONSTRAINT "Relationship8" FOREIGN KEY ("record_to_qualifier") REFERENCES "thesaurus_record" ("qualifier") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "thesaurus_location" ADD CONSTRAINT "Relationship9" FOREIGN KEY ("begin_document_id") REFERENCES "thesaurus_document" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE "thesaurus_location" ADD CONSTRAINT "Relationship10" FOREIGN KEY ("end_document_id") REFERENCES "thesaurus_document" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
;





