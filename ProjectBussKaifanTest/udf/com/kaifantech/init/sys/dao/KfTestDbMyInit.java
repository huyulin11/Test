package com.kaifantech.init.sys.dao;

import java.util.ArrayList;
import java.util.List;

import com.kaifantech.init.sys.params.AppConfKeys;

public class KfTestDbMyInit extends BaseDao {
	protected static void createDB(List<String> sqls) {
		sqls.add("CREATE DATABASE `" + AppDbs.OP_DB + "`");
		sqls.add("CREATE DATABASE `" + AppDbs.BUSS_DB + "`");
		sqls.add("CREATE DATABASE `" + AppDbs.IOT_DB + "`");
		sqls.add("CREATE DATABASE `" + AppDbs.DE_DB + "`");
		sqls.add("CREATE DATABASE `" + AppDbs.WMS_DB + "`");
	}

	protected static void truncate(List<String> sqls) {
		sqls.add("truncate table " + AppDbs.CONF_DB + "." + AppConfKeys.WMS_ALLOC_LOCK);
		sqls.add("truncate table " + AppTables.WMS_RECEIPT_REQUEST_DETAIL);
		sqls.add("truncate table " + AppTables.WMS_RECEIPT_REQUEST_MAIN);
		sqls.add("truncate table " + AppTables.WMS_SHIPMENT_REQUEST_DETAIL);
		sqls.add("truncate table " + AppTables.WMS_SHIPMENT_REQUEST_MAIN);
		sqls.add("truncate table " + AppTables.WMS_TRANSFER_REQUEST_DETAIL);
		sqls.add("truncate table " + AppTables.WMS_TRANSFER_REQUEST_MAIN);
		sqls.add("truncate table " + AppDbs.CONF_DB + "." + AppConfKeys.COMBINED_TU_INFO);
		sqls.add("truncate table " + AppTables.ALLOC_TXM_INFO);
		sqls.add("update " + AppTables.ALLOCATION_ITEM_INFO + " set num=0,status=1 where 1=1 ");
	}

	protected static void doExe(List<String> sqls) {
		for (String sql : sqls) {
			try {
				jdbcMyTemplate().execute(sql);
				System.out.println(sql + ";");
			} catch (Exception e) {
				System.err.println(e.getMessage() + ",ERR:" + sql);
			}
		}
	}

	public static void main(String[] args) {
		List<String> sqls = new ArrayList<>();
		createDB(sqls);
		doExe(sqls);
	}
}
