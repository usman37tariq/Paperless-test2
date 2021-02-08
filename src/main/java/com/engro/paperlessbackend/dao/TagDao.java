package com.engro.paperlessbackend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.engro.paperlessbackend.dto.TagDataDto;
import com.engro.paperlessbackend.dto.TagDataRowDto;
import com.engro.paperlessbackend.entities.Tag;
import com.engro.paperlessbackend.utils.Constants;

/**
 * To Manage DB Function for Tags Create Tags Table Get Tag Data
 * 
 * @author Ehsan Waris
 *
 */
@Component
public class TagDao {
	private static Logger logger = LoggerFactory.getLogger(TagDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	public static final String TAG_TYPE_TEXT = "TEXT";
	public static final String TAG_TYPE_NUMERIC = "NUMERIC";

//	private static final String TABLE_CHECKLIST = "tbl_checklist";
//	private static final String TABLE_CHECKLIST_SCHEDULE = "tbl_checklist_schedule";
//	private static final String COLUMN_CHECKLIST_ID = "checklist_id";
//	private static final String TABLE_USER = "users";
//	private static final String COLUMN_USER_ID = "user_id";

//	private static final String COLUMN_ID = "id";
	private static final String COLUMN_TIMESTAMP = "time_stamp";
	private static final String COLUMN_VALUE = "value";
	private static final String COLUMN_QUALITY = "quality";
	private static final String COLUMN_REMARKS = "remarks";
//	private static final String COLUMN_CHECKLIST_ID = "checklist_id";
	private static final String COLUMN_CHECKLIST_ID_FK = "checklist_id_fk";
//	private static final String COLUMN_CHECKLIST_SCHEDULE_ID = "checklist_schedule_id";
	private static final String COLUMN_CHECKLIST_SCHEDULE_ID_FK = "checklist_schedule_id_fk";
//	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_USER_ID_FK = "user_id_fk";
	private static final String COLUMN_DATA_TIMESTAMP = "data_timestamp";

	public boolean createTagTable(String tableName, String tagType) {

		String type;
		if (tagType.equals(TAG_TYPE_TEXT)) {
			type = "varchar";
		} else {
			type = "numeric(30, 10)";
		}
		try {
			String sqlCreateTagTable = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + COLUMN_TIMESTAMP
					+ " Timestamp without time zone NOT NULL," + COLUMN_VALUE + " " + type + "," + COLUMN_QUALITY
					+ " integer," + COLUMN_REMARKS + " varchar," + COLUMN_CHECKLIST_ID_FK + " int,"
					+ COLUMN_CHECKLIST_SCHEDULE_ID_FK + " int NOT NULL UNIQUE," + COLUMN_USER_ID_FK + " varchar(100),"
					+ COLUMN_DATA_TIMESTAMP + " Timestamp without time zone NOT NULL UNIQUE," + "CONSTRAINT " + tableName
					+ "_pkey PRIMARY KEY (" + COLUMN_TIMESTAMP + "));";

			jdbcTemplate.execute(sqlCreateTagTable);

			logger.info("TableName[{}] Created Successfully.", tableName);

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}

		try {
			String indexName = "index_" + tableName + "_" + COLUMN_DATA_TIMESTAMP;

			String sqlCreateIndexOnDataTimestamp = "CREATE INDEX IF NOT EXISTS " + indexName + " ON " + tableName + " USING btree ("
					+ COLUMN_DATA_TIMESTAMP + " ASC NULLS LAST)  INCLUDE(" + COLUMN_DATA_TIMESTAMP
					+ ") TABLESPACE pg_default;";
			logger.info("Create table sql[{}]", sqlCreateIndexOnDataTimestamp);
			jdbcTemplate.execute(sqlCreateIndexOnDataTimestamp);
			logger.info("Index :[{}] Created Successfully.", indexName);

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}

		return true;
	}

	public boolean insertValueIntoTagTable(String tableName, TagDataDto tagData, String fieldType) {

		try {
			String sqlInsertQuery = "INSERT INTO " + tableName + " (" + COLUMN_TIMESTAMP + ", " + COLUMN_VALUE + ", "
					+ COLUMN_QUALITY + ", " + COLUMN_REMARKS + ", " + COLUMN_CHECKLIST_ID_FK + ", "
					+ COLUMN_CHECKLIST_SCHEDULE_ID_FK + ", " + COLUMN_USER_ID_FK + ", " + COLUMN_DATA_TIMESTAMP
					+ ") VALUES(?, ?, ?, ?, ?, ?, ?, ?) ";
//			String tagValue;
//			String tagQuality;
//			if (tagData.getValue() == null || tagData.getValue().trim().isEmpty()) {
//				//sqlInsertQuery += null + ", ";
//				tagValue = null;
//
//			} else {
//				sqlInsertQuery += "'" + tagData.getValue() + "', ";
//				tagValue = tagData.getValue();
//			}
//			sqlInsertQuery += "'" + tagData.getQuality() + "', ";
//			if (tagData.getRemarks() != null) {
//				sqlInsertQuery += "'" + tagData.getRemarks() + "', ";
//			} else {
//				sqlInsertQuery += null + ", ";
//			}
//			sqlInsertQuery += "'" + tagData.getChecklistId() + "', " + "'" + tagData.getChecklistScheduleId() + "', "
//					+ "'" + tagData.getUserId() + "', '" + tagData.getDataTimestamp() + "');";

			logger.info("[{}],[{}],[{}],[{}],[{}],[{}],[{}],[{}],[{}]", sqlInsertQuery, tagData.getValue(), tagData.getQuality(),
					tagData.getRemarks(), tagData.getChecklistId(), tagData.getChecklistScheduleId(),
					tagData.getUserId(), tagData.getDataTimestamp());

			if (fieldType.equals(Constants.TAG_TYPE_TEXT)) {
				jdbcTemplate.update(sqlInsertQuery, tagData.getTimeStamp(), tagData.getValue(), tagData.getQuality(),
						tagData.getRemarks(), tagData.getChecklistId(), tagData.getChecklistScheduleId(),
						tagData.getUserId(), tagData.getDataTimestamp());
			} else {
				
				
				Double value =null;
				if(tagData.getValue()!=null && Constants.isNumeric(tagData.getValue())) {
					value = Double.parseDouble(tagData.getValue());
				}
				jdbcTemplate.update(sqlInsertQuery, tagData.getTimeStamp(), value, tagData.getQuality(),
						tagData.getRemarks(), tagData.getChecklistId(), tagData.getChecklistScheduleId(),
						tagData.getUserId(), tagData.getDataTimestamp());
			}
			logger.info("Insertion Successful");
			return true;
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}
	
	public boolean insertValueIntoTagTable_old(String tableName, TagDataDto tagData) {

		try {
			String sqlInsertQuery = "INSERT INTO " + tableName + " (" + COLUMN_TIMESTAMP + ", " + COLUMN_VALUE + ", "
					+ COLUMN_QUALITY + ", " + COLUMN_REMARKS + ", " + COLUMN_CHECKLIST_ID_FK + ", "
					+ COLUMN_CHECKLIST_SCHEDULE_ID_FK + ", " + COLUMN_USER_ID_FK + ", " + COLUMN_DATA_TIMESTAMP
					+ ") VALUES("

					+ "'" + tagData.getTimeStamp() + "', ";
			
			if (tagData.getValue() == null || tagData.getValue().trim().isEmpty()) {
				sqlInsertQuery += null + ", ";

			} else {
				sqlInsertQuery += "'" + tagData.getValue() + "', ";
			}
			sqlInsertQuery += "'" + tagData.getQuality() + "', ";
			if (tagData.getRemarks() != null) {
				sqlInsertQuery += "'" + tagData.getRemarks() + "', ";
			} else {
				sqlInsertQuery += null + ", ";
			}
			sqlInsertQuery += "'" + tagData.getChecklistId() + "', " + "'" + tagData.getChecklistScheduleId() + "', "
					+ "'" + tagData.getUserId() + "', '" + tagData.getDataTimestamp() + "');";

			logger.info("[{}]", sqlInsertQuery);

			jdbcTemplate.execute(sqlInsertQuery);

			logger.info("Insertion Successful");
			return true;
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	public List<TagDataRowDto> getTagData(String tableName, String startDate, String endDate) {
		List<TagDataRowDto> dataList = new ArrayList<>();
		try {
			String sqlSelectQuery = "SELECT " + COLUMN_DATA_TIMESTAMP + ", " + COLUMN_VALUE + " FROM " + tableName
					+ " WHERE " + COLUMN_DATA_TIMESTAMP + " BETWEEN '" + startDate + "' AND '" + endDate + "' ORDER BY "
					+ COLUMN_DATA_TIMESTAMP + " ASC";

			logger.info(" [{}]", sqlSelectQuery);

			return jdbcTemplate.query(sqlSelectQuery, new Object[] {}, new RowMapper<TagDataRowDto>() {
				@Override
				public TagDataRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {

					TagDataRowDto rec = new TagDataRowDto();
					rec.setTimestamp(rs.getTimestamp(COLUMN_DATA_TIMESTAMP).getTime());
					rec.setValue(rs.getObject(COLUMN_VALUE));
					return rec;
				}
			});

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dataList;
	}

	public List<Tag> getTagDataAllFields(String tableName, String startDate, String endDate, int checklistId) {
		List<Tag> dataList = new ArrayList<>();
		try {
			// + COLUMN_TIMESTAMP + ", " + COLUMN_DATA_TIMESTAMP + ", " + COLUMN_VALUE + ",
			// " + COLUMN_REMARKS

			String sqlSelectQuery = "SELECT  * " + " FROM " + tableName + " WHERE " + COLUMN_CHECKLIST_ID_FK + " = "
					+ checklistId + " AND " + COLUMN_DATA_TIMESTAMP + " >= '" + startDate + "' AND "
					+ COLUMN_DATA_TIMESTAMP + " <= '" + endDate + "' ORDER BY " + COLUMN_DATA_TIMESTAMP
					+ " ASC LIMIT 1";

			logger.info(" [{}]", sqlSelectQuery);

			return jdbcTemplate.query(sqlSelectQuery, new Object[] {}, new RowMapper<Tag>() {
				@Override
				public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

					Tag rec = new Tag();
					rec.setChecklistId(rs.getInt(COLUMN_CHECKLIST_ID_FK));
					rec.setChecklistScheduleId(rs.getInt(COLUMN_CHECKLIST_SCHEDULE_ID_FK));
					rec.setQuality(rs.getInt(COLUMN_QUALITY));
					rec.setRemarks(rs.getString(COLUMN_REMARKS));
					rec.setTimestamp(rs.getTimestamp(COLUMN_TIMESTAMP));
					rec.setDataEntryTimestamp(rs.getTimestamp(COLUMN_DATA_TIMESTAMP));
					rec.setValue(rs.getObject(COLUMN_VALUE));
					rec.setUserId(rs.getString(COLUMN_USER_ID_FK));
					return rec;
				}
			});

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dataList;
	}

	public List<Tag> getTagDataForwardDirection(String tableName, String date, int checklistId) {
		List<Tag> dataList = new ArrayList<>();
		try {
			// + COLUMN_TIMESTAMP + ", " + COLUMN_DATA_TIMESTAMP + ", " + COLUMN_VALUE + ",
			// " + COLUMN_REMARKS
			String sqlSelectQuery = "SELECT * " + " FROM " + tableName + " WHERE " + COLUMN_CHECKLIST_ID_FK + " = "
					+ checklistId + " AND " + COLUMN_DATA_TIMESTAMP + " > '" + date + "' ORDER BY "
					+ COLUMN_DATA_TIMESTAMP + " ASC LIMIT 1";

			logger.info(" [{}]", sqlSelectQuery);

			return jdbcTemplate.query(sqlSelectQuery, new Object[] {}, new RowMapper<Tag>() {
				@Override
				public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

					Tag rec = new Tag();
					rec.setChecklistId(rs.getInt(COLUMN_CHECKLIST_ID_FK));
					rec.setChecklistScheduleId(rs.getInt(COLUMN_CHECKLIST_SCHEDULE_ID_FK));
					rec.setQuality(rs.getInt(COLUMN_QUALITY));
					rec.setRemarks(rs.getString(COLUMN_REMARKS));
					rec.setTimestamp(rs.getTimestamp(COLUMN_TIMESTAMP));
					rec.setDataEntryTimestamp(rs.getTimestamp(COLUMN_DATA_TIMESTAMP));
					rec.setValue(rs.getObject(COLUMN_VALUE));
					rec.setUserId(rs.getString(COLUMN_USER_ID_FK));
					return rec;
				}
			});

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dataList;
	}

	public List<Tag> getTagDataForwardDirection(String tableName, String date, int checklistId, int scheduleId) {
		List<Tag> dataList = new ArrayList<>();
		try {
			// + COLUMN_TIMESTAMP + ", " + COLUMN_DATA_TIMESTAMP + ", " + COLUMN_VALUE + ",
			// " + COLUMN_REMARKS
			String sqlSelectQuery = "SELECT * " + " FROM " + tableName + " WHERE " 
			+ COLUMN_CHECKLIST_ID_FK + " = "+ checklistId 
			+ " AND " 
			+ COLUMN_CHECKLIST_SCHEDULE_ID_FK + " <> "+ scheduleId
			+ " AND " 
			+ COLUMN_DATA_TIMESTAMP + " > '" + date 
			+ "' ORDER BY "+ COLUMN_DATA_TIMESTAMP 
			+ " ASC LIMIT 1";

			logger.info(" [{}]", sqlSelectQuery);

			return jdbcTemplate.query(sqlSelectQuery, new Object[] {}, new RowMapper<Tag>() {
				@Override
				public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

					Tag rec = new Tag();
					rec.setChecklistId(rs.getInt(COLUMN_CHECKLIST_ID_FK));
					rec.setChecklistScheduleId(rs.getInt(COLUMN_CHECKLIST_SCHEDULE_ID_FK));
					rec.setQuality(rs.getInt(COLUMN_QUALITY));
					rec.setRemarks(rs.getString(COLUMN_REMARKS));
					rec.setTimestamp(rs.getTimestamp(COLUMN_TIMESTAMP));
					rec.setDataEntryTimestamp(rs.getTimestamp(COLUMN_DATA_TIMESTAMP));
					rec.setValue(rs.getObject(COLUMN_VALUE));
					rec.setUserId(rs.getString(COLUMN_USER_ID_FK));
					return rec;
				}
			});

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dataList;
	}
	public List<Tag> getTagDataBackwardDirection(String tableName, String date, int checklistId) {
		List<Tag> dataList = new ArrayList<>();
		try {
//			+ COLUMN_TIMESTAMP + ", " + COLUMN_DATA_TIMESTAMP + ", " + COLUMN_VALUE + ", " + COLUMN_REMARKS
			String sqlSelectQuery = "SELECT * " + " FROM " + tableName + " WHERE " + COLUMN_CHECKLIST_ID_FK + " = "
					+ checklistId + " AND " + COLUMN_DATA_TIMESTAMP + " < '" + date + "' ORDER BY "
					+ COLUMN_DATA_TIMESTAMP + " DESC LIMIT 1";

			logger.info(" [{}]", sqlSelectQuery);

			return jdbcTemplate.query(sqlSelectQuery, new Object[] {}, new RowMapper<Tag>() {
				@Override
				public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

					Tag rec = new Tag();
					rec.setChecklistId(rs.getInt(COLUMN_CHECKLIST_ID_FK));
					rec.setChecklistScheduleId(rs.getInt(COLUMN_CHECKLIST_SCHEDULE_ID_FK));
					rec.setQuality(rs.getInt(COLUMN_QUALITY));
					rec.setRemarks(rs.getString(COLUMN_REMARKS));
					rec.setTimestamp(rs.getTimestamp(COLUMN_TIMESTAMP));
					rec.setDataEntryTimestamp(rs.getTimestamp(COLUMN_DATA_TIMESTAMP));
					rec.setValue(rs.getObject(COLUMN_VALUE));
					rec.setUserId(rs.getString(COLUMN_USER_ID_FK));
					return rec;
				}
			});

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dataList;
	}
	public List<Tag> getTagDataBackwardDirection(String tableName, String date, int checklistId, int scheduleId) {
		List<Tag> dataList = new ArrayList<>();
		try {
//			+ COLUMN_TIMESTAMP + ", " + COLUMN_DATA_TIMESTAMP + ", " + COLUMN_VALUE + ", " + COLUMN_REMARKS
			String sqlSelectQuery = "SELECT * " + " FROM " + tableName + " WHERE " 
					+ COLUMN_CHECKLIST_ID_FK + " = "+ checklistId 
					+ " AND "
					+ COLUMN_CHECKLIST_SCHEDULE_ID_FK + " <> "+ scheduleId
					+ " AND " 
					+ COLUMN_DATA_TIMESTAMP + " < '" + date 
					+ "' ORDER BY " + COLUMN_DATA_TIMESTAMP 
					+ " DESC LIMIT 1";

			logger.info(" [{}]", sqlSelectQuery);

			return jdbcTemplate.query(sqlSelectQuery, new Object[] {}, new RowMapper<Tag>() {
				@Override
				public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

					Tag rec = new Tag();
					rec.setChecklistId(rs.getInt(COLUMN_CHECKLIST_ID_FK));
					rec.setChecklistScheduleId(rs.getInt(COLUMN_CHECKLIST_SCHEDULE_ID_FK));
					rec.setQuality(rs.getInt(COLUMN_QUALITY));
					rec.setRemarks(rs.getString(COLUMN_REMARKS));
					rec.setTimestamp(rs.getTimestamp(COLUMN_TIMESTAMP));
					rec.setDataEntryTimestamp(rs.getTimestamp(COLUMN_DATA_TIMESTAMP));
					rec.setValue(rs.getObject(COLUMN_VALUE));
					rec.setUserId(rs.getString(COLUMN_USER_ID_FK));
					return rec;
				}
			});

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dataList;
	}
	public List<Tag> getTagDataFirstValueInTimeRange(String tableName, String startDate, String endDate, int checklistId) {
		List<Tag> dataList = new ArrayList<>();
		try {
//			+ COLUMN_TIMESTAMP + ", " + COLUMN_DATA_TIMESTAMP + ", " + COLUMN_VALUE + ", " + COLUMN_REMARKS
			String sqlSelectQuery = "SELECT * " + " FROM " + tableName + " WHERE " + COLUMN_CHECKLIST_ID_FK + " = "
					+ checklistId + " AND " 
					+ COLUMN_DATA_TIMESTAMP + " >= '" + startDate +"'"
					+ " AND " 
					+ COLUMN_DATA_TIMESTAMP + " <= '" + endDate 
					+ "' ORDER BY "
					+ COLUMN_DATA_TIMESTAMP + " ASC LIMIT 1";

			logger.info(" [{}]", sqlSelectQuery);

			return jdbcTemplate.query(sqlSelectQuery, new Object[] {}, new RowMapper<Tag>() {
				@Override
				public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

					Tag rec = new Tag();
					rec.setChecklistId(rs.getInt(COLUMN_CHECKLIST_ID_FK));
					rec.setChecklistScheduleId(rs.getInt(COLUMN_CHECKLIST_SCHEDULE_ID_FK));
					rec.setQuality(rs.getInt(COLUMN_QUALITY));
					rec.setRemarks(rs.getString(COLUMN_REMARKS));
					rec.setTimestamp(rs.getTimestamp(COLUMN_TIMESTAMP));
					rec.setDataEntryTimestamp(rs.getTimestamp(COLUMN_DATA_TIMESTAMP));
					rec.setValue(rs.getObject(COLUMN_VALUE));
					rec.setUserId(rs.getString(COLUMN_USER_ID_FK));
					return rec;
				}
			});

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dataList;
	}

	public List<Tag> getTagDataAllFields(String tableName, Integer scheduleId) {
		List<Tag> dataList = new ArrayList<>();
		try {
			// + COLUMN_TIMESTAMP + ", "+ COLUMN_DATA_TIMESTAMP + ", " + COLUMN_VALUE + ", "
			// + COLUMN_REMARKS + ", "
//					+ COLUMN_USER_ID_FK 

			String sqlSelectQuery = "SELECT * " + " FROM " + tableName + " WHERE " + COLUMN_CHECKLIST_SCHEDULE_ID_FK
					+ " = " + scheduleId;

			logger.info(" [{}]", sqlSelectQuery);

			return jdbcTemplate.query(sqlSelectQuery, new Object[] {}, new RowMapper<Tag>() {
				@Override
				public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

					Tag rec = new Tag();
					rec.setChecklistId(rs.getInt(COLUMN_CHECKLIST_ID_FK));
					rec.setChecklistScheduleId(rs.getInt(COLUMN_CHECKLIST_SCHEDULE_ID_FK));
					rec.setQuality(rs.getInt(COLUMN_QUALITY));
					rec.setRemarks(rs.getString(COLUMN_REMARKS));
					rec.setTimestamp(rs.getTimestamp(COLUMN_TIMESTAMP));
					rec.setDataEntryTimestamp(rs.getTimestamp(COLUMN_DATA_TIMESTAMP));
					rec.setValue(rs.getObject(COLUMN_VALUE));
					rec.setUserId(rs.getString(COLUMN_USER_ID_FK));
					return rec;
				}
			});

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dataList;
	}

	public boolean updateValueInTagTable(String tableName, TagDataDto tagData, String fieldType) {

		try {
			String sqlUpdateQuery = "UPDATE " + tableName + " SET " + COLUMN_VALUE + " = ? ";
			
			sqlUpdateQuery += ", " + COLUMN_REMARKS + " = ? ";
			
			sqlUpdateQuery += ", " + COLUMN_DATA_TIMESTAMP + " = ? ";

			sqlUpdateQuery += " WHERE " + COLUMN_CHECKLIST_SCHEDULE_ID_FK + " = ? ;";

//			if (tagData.getValue() == null || tagData.getValue().trim().isEmpty()) {
//				sqlUpdateQuery += null;
//
//			} else {
//				sqlUpdateQuery += "'" + tagData.getValue() + "'";
//			}
//
//			
//
//			if (tagData.getRemarks() == null || tagData.getRemarks().trim().isEmpty()) {
//				sqlUpdateQuery += null;
//
//			} else {
//				sqlUpdateQuery += "'" + tagData.getRemarks() + "'";
//			}

			
			logger.info("[{}], [{}], [{}], [{}], [{}]", sqlUpdateQuery, tagData.getValue(), tagData.getRemarks(), tagData.getDataTimestamp(), tagData.getChecklistScheduleId());

			if (fieldType.equals(Constants.TAG_TYPE_TEXT)) {
				jdbcTemplate.update(sqlUpdateQuery, tagData.getValue(), tagData.getRemarks(), tagData.getDataTimestamp(), tagData.getChecklistScheduleId());
			} else {
				
				Double value =null;
				if(tagData.getValue()!=null && Constants.isNumeric(tagData.getValue())) {
					value = Double.parseDouble(tagData.getValue());
				}
				jdbcTemplate.update(sqlUpdateQuery, value, tagData.getRemarks(), tagData.getDataTimestamp(), tagData.getChecklistScheduleId());
			}
			
			//jdbcTemplate.update(sqlUpdateQuery);

			logger.info("Updation Successful");
			return true;
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}
	
	public boolean updateValueInTagTable_old(String tableName, TagDataDto tagData) {

		try {
			String sqlUpdateQuery = "UPDATE " + tableName + " SET " + COLUMN_VALUE + " = ";
			if (tagData.getValue() == null || tagData.getValue().trim().isEmpty()) {
				sqlUpdateQuery += null;

			} else {
				sqlUpdateQuery += "'" + tagData.getValue() + "'";
			}

			sqlUpdateQuery += ", " + COLUMN_REMARKS + " = ";

			if (tagData.getRemarks() == null || tagData.getRemarks().trim().isEmpty()) {
				sqlUpdateQuery += null;

			} else {
				sqlUpdateQuery += "'" + tagData.getRemarks() + "'";
			}

			sqlUpdateQuery += ", " + COLUMN_DATA_TIMESTAMP + " = '" + tagData.getDataTimestamp() + "'";

			sqlUpdateQuery += " WHERE " + COLUMN_CHECKLIST_SCHEDULE_ID_FK + " = " + tagData.getChecklistScheduleId()
					+ ";";

			logger.info("[{}]", sqlUpdateQuery);

			jdbcTemplate.execute(sqlUpdateQuery);

			logger.info("Updation Successful");
			return true;
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return false;
	}

	public boolean dropTagTableByName(String tableName) {

		try {
			String dropQuery = "DROP TABLE IF EXISTS " + tableName;

			logger.info("[Drop Query] - [{}]", dropQuery);

			jdbcTemplate.execute(dropQuery);

			logger.info("Updation Successful");
		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
			return false;
		}
		return true;
	}

	public boolean checkIfTagDataExistByChecklistIdAndDataTimestamp(String tableName, int checklistId,
			Timestamp dataTimestamp) {

		try {
//			String sqlSelectQuery = "SELECT COUNT(*) " + " FROM " + tableName + " WHERE " + COLUMN_CHECKLIST_ID_FK
//					+ " != " + checklistId + " AND " + COLUMN_DATA_TIMESTAMP + " = '" + dataTimestamp + "';";

			String sqlSelectQuery = "SELECT COUNT(*) " + " FROM " + tableName + " WHERE " + COLUMN_DATA_TIMESTAMP
					+ " = '" + dataTimestamp + "';";

			logger.info(" [{}]", sqlSelectQuery);

			int tagRecords = jdbcTemplate.queryForObject(sqlSelectQuery, Integer.class);

			if (tagRecords > 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return false;
	}

	public boolean checkIfTagDataExistByScheduleIdAndDataTimeStamp(String tableName, int scheduleId,
			Timestamp dataTimestamp) {
		try {
			String sqlSelectQuery = "SELECT COUNT(*) " + " FROM " + tableName + " WHERE "
					+ COLUMN_CHECKLIST_SCHEDULE_ID_FK + " != " + scheduleId + " AND " + COLUMN_DATA_TIMESTAMP + " = '"
					+ dataTimestamp + "';";

			logger.info(" [{}]", sqlSelectQuery);

			int tagRecords = jdbcTemplate.queryForObject(sqlSelectQuery, Integer.class);

			if (tagRecords > 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("[Exception] - [{}]", e);
		}
		return false;
	}
	
	public List<Tag> getLatestDataForTag(String tagName) {
		List<Tag> dataList = new ArrayList<>();
		try {

			String sqlSelectQuery = "SELECT  * " + " FROM " + tagName + " LIMIT 1";

			logger.info(" [{}]", sqlSelectQuery);

			return jdbcTemplate.query(sqlSelectQuery, new Object[] {}, new RowMapper<Tag>() {
				@Override
				public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

					Tag rec = new Tag();
					rec.setChecklistId(rs.getInt(COLUMN_CHECKLIST_ID_FK));
					rec.setChecklistScheduleId(rs.getInt(COLUMN_CHECKLIST_SCHEDULE_ID_FK));
					rec.setQuality(rs.getInt(COLUMN_QUALITY));
					rec.setRemarks(rs.getString(COLUMN_REMARKS));
					rec.setTimestamp(rs.getTimestamp(COLUMN_TIMESTAMP));
					rec.setDataEntryTimestamp(rs.getTimestamp(COLUMN_DATA_TIMESTAMP));
					rec.setValue(rs.getObject(COLUMN_VALUE));
					rec.setUserId(rs.getString(COLUMN_USER_ID_FK));
					return rec;
				}
			});

		} catch (Exception ex) {
			logger.error("[Exception] - [{}]", ex);
		}
		return dataList;
	}
}