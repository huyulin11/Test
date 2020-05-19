package com.kaifantech.init.sys.dao;

import java.util.ArrayList;
import java.util.List;

public class KfTestDbMyInit extends BaseDao {
	protected static void createDB(List<String> sqls) {
		sqls.add("CREATE DATABASE `" + UdfDbs.OP_DB + "`");
		sqls.add("CREATE DATABASE `" + UdfDbs.BUSS_DB + "`");
		sqls.add("CREATE DATABASE `" + UdfDbs.IOT_DB + "`");
		sqls.add("CREATE DATABASE `" + UdfDbs.DE_DB + "`");
		sqls.add("CREATE DATABASE `" + UdfDbs.WMS_DB + "`");
	}

	protected static void truncate(List<String> sqls) {
		sqls.add("truncate table " + AppTables.WMS_RECEIPT_REQUEST_DETAIL);
		sqls.add("truncate table " + AppTables.WMS_RECEIPT_REQUEST_MAIN);
		sqls.add("truncate table " + AppTables.WMS_SHIPMENT_REQUEST_DETAIL);
		sqls.add("truncate table " + AppTables.WMS_SHIPMENT_REQUEST_MAIN);
		sqls.add("truncate table " + AppTables.WMS_TRANSFER_REQUEST_DETAIL);
		sqls.add("truncate table " + AppTables.WMS_TRANSFER_REQUEST_MAIN);
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
