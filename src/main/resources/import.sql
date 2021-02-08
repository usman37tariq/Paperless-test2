--1-Organization entry
INSERT INTO organization (organization_id, address, business_type, email, id, name, number_of_tags, number_of_users, package, phone, role, status) VALUES ('epcl', 'address','business_type','email', 1,'EPCL', 1000,1000, 'package', 'phone', 'admin', 'status');

--2-Department entry
INSERT INTO tbl_department(department_id, is_deleted, department_name, org_id_fk) VALUES (1,0, 'epcl_dept', 'epcl');

--3-Section entry
INSERT INTO tbl_section(section_id,is_deleted, section_name, department_id_fk) VALUES (1,0, 'epcl-dept-sec', 1);

--4-Role entry
INSERT INTO tbl_role(role_id, role_name, role_description) VALUES (1, 'admin', 'admin');

--4-Admin user entry
INSERT INTO users(user_id, address, company, dashboard_order, date, date_added, date_inactive, designation, division, email, language, password, phone_number, "position", role, status, user_name,  organization_id, section, user_role) VALUES ('admin', '', 'epcl', '', now(), now(), null, 'admin', 'division', 'email', 'language', 'admin', 123456789, 'position', 'string', 1, 'admin', 'epcl', 1, 1);

--6-Resources entries
INSERT INTO tbl_resource(resource_id, resource_name) VALUES (1,'Hierarchy Builder');
INSERT INTO tbl_resource(resource_id, resource_name) VALUES (2,'Asset Builder');
INSERT INTO tbl_resource(resource_id, resource_name) VALUES (3,'Checklist Builder');
INSERT INTO tbl_resource(resource_id, resource_name) VALUES (4,'Data Visualization');
INSERT INTO tbl_resource(resource_id, resource_name) VALUES (5,'Data Collector');
INSERT INTO tbl_resource(resource_id, resource_name) VALUES (6,'User Management');

--7-Role resource entries
INSERT INTO tbl_role_resource(role_id_fk, resource_id_fk, can_add, can_delete, can_edit, can_view) VALUES (1, 1, 1, 1, 1, 1);
INSERT INTO tbl_role_resource(role_id_fk, resource_id_fk, can_add, can_delete, can_edit, can_view) VALUES (1, 2, 1, 1, 1, 1);
INSERT INTO tbl_role_resource(role_id_fk, resource_id_fk, can_add, can_delete, can_edit, can_view) VALUES (1, 3, 1, 1, 1, 1);
INSERT INTO tbl_role_resource(role_id_fk, resource_id_fk, can_add, can_delete, can_edit, can_view) VALUES (1, 4, 1, 1, 1, 1);
INSERT INTO tbl_role_resource(role_id_fk, resource_id_fk, can_add, can_delete, can_edit, can_view) VALUES (1, 5, 1, 1, 1, 1);
INSERT INTO tbl_role_resource(role_id_fk, resource_id_fk, can_add, can_delete, can_edit, can_view) VALUES (1, 6, 1, 1, 1, 1);

--8-Group entries
INSERT INTO tbl_group(group_id, group_description, group_name) VALUES (1, 'Group Description', 'admin_group');

--9-User Group entry
INSERT INTO tbl_user_group(group_id_fk, user_id_fk) VALUES (1,'admin');

--10-Hierarchy entry
INSERT INTO tbl_hierarchy(node_active_status, node_description, node_name, node_parent_id, sap_ref_num, node_type) VALUES (1, 'epcl node', 'EPCL', -1, 'SAP_EPCL', '0');

--11-Worlkflow_level entries in tbl_workflow_level
INSERT INTO tbl_workflow_level VALUES (0, 'APPROVED');
INSERT INTO tbl_workflow_level VALUES (1, 'DATA ENTRY PENDING');
INSERT INTO tbl_workflow_level VALUES (2, 'FIRST APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (3, 'SECOND APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (4, 'THIRD APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (5, 'FOURTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (6, 'FIFTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (7, 'SIXTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (8, 'SEVENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (9, 'EIGHTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (10, 'NINTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (11, 'TENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (12, 'ELEVENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (13, 'TWELFTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (14, 'THIRTEENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (15, 'FOURTEENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (18, 'FIFTEENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (16, 'SIXTEENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (17, 'SEVENTEENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (19, 'EIGHTEENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (20, 'NINETEENTH APPROVAL PENDING');
INSERT INTO tbl_workflow_level VALUES (21, 'TWENTIETH APPROVAL PENDING');


--12-Add foreign key constraint in tbl_template_structure
ALTER TABLE tbl_template_structure ADD CONSTRAINT tbl_template_structure_template_id_fk_fkey FOREIGN KEY(template_id_fk) REFERENCES paperless.tbl_template(template_id);

--13-Add foreign key constraints in tbl_template_hierarchy
ALTER TABLE tbl_template_hierarchy ADD CONSTRAINT tbl_template_hierarchy_node_id_fk_fkey FOREIGN KEY (node_id_fk) REFERENCES paperless.tbl_hierarchy (node_id);
ALTER TABLE tbl_template_hierarchy ADD CONSTRAINT tbl_template_hierarchy_template_id_fk_fkey FOREIGN KEY (template_id_fk) REFERENCES paperless.tbl_template (template_id);


