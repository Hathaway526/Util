package com.mds.mydb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDbHelper {
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	// ��������
	static {
		try {
			Class.forName(DbProperties.getInstance().StingReadConfig("driverName"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ������������ָ�����ݿ� ��ȡ���Ӷ���
	public Connection getConnection() {
		try {
			try {
				conn = DriverManager.getConnection(DbProperties.getInstance().StingReadConfig("url"),
						DbProperties.getInstance().StingReadConfig("user"),
						DbProperties.getInstance().StingReadConfig("password"));
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	// �ر����ж��� ���� Ԥ���� �����
	public void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����������ɾ�� ˵��:��ִ�гɹ��������صĽ��Ϊ��������
	 * 
	 * @param sql    ��Ҫִ�е�sql���
	 * @param params ִ��sql���Ĳ���
	 * @return
	 */
	public int doUpdate(String sql, List<Object> params) {
		int result = 0;
		// ��ȡ����
		conn = getConnection();
		// Ԥ����
		try {
			ps = conn.prepareStatement(sql);
			// ���ò����õ������
			this.setParams(ps, params);
			// Ԥ����ִ�еõ��������
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// �ر����ж���
			this.closeAll(conn, ps, rs);
		}
		return result;
	}

	/**
	 * ����������ɾ�� ˵��:��Щ���Ҫôͬʱ�ɹ���Ҫô��ʧ��
	 * 
	 * @param sqls   ����sql���
	 * @param params ִ�в���
	 * @return
	 */
	public int doUpdate(List<String> sqls, List<Object> params) {
		int result = 0;
		// ��ȡ����
		conn = getConnection();
		try {
			// ��������ִ���漰������ ���������ύ��ʽΪ�ֶ�
			conn.setAutoCommit(false);
			// �ж�sql��伯��
			if (sqls != null && sqls.size() > 0) {
				// ѭ��ÿһ�����ִ��
				for (int i = 0; i < sqls.size(); i++) {
					ps = conn.prepareStatement(sqls.get(i));
					// ���ò���
					ps.setObject(i + 1, params.get(i));
					// ִ�в�����
					result = ps.executeUpdate();
				}
			}
			// �ֶ��ύ����
			conn.commit();
		} catch (SQLException e) {
			// ���ִ�����ع�����
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			// �ظ������Զ��ύ
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// �ر����ж���
			this.closeAll(conn, ps, rs);
		}
		return result;
	}

	/**
	 * ����Ԥ����Ĳ���
	 * 
	 * @param ps     Ԥ����
	 * @param params ��������
	 * @throws SQLException
	 */
	public void setParams(PreparedStatement ps, List<Object> params) throws SQLException {
		if (params != null && params.size() > 0) {
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}
		}
	}

	/**
	 * ��ѯsql��䵥�����
	 * 
	 * @param sql             ��ѯ��sql���
	 * @param paramsִ��sql�������
	 * @return
	 */
	public Map<String, Object> findSingleObject(String sql, List<Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		// ��ȡ����
		conn = getConnection();
		// Ԥ����
		try {
			ps = conn.prepareStatement(sql);
			// ���ò���
			this.setParams(ps, params);
			// ִ�в�ѯ�õ������
			rs = ps.executeQuery();
			// ��ȡ���ݿ�ñ������ֶ���
			List<String> names = getAllColumnName(rs);
			if (rs.next()) {
				// ѭ��names
				for (String name : names) {
					map.put(name, rs.getObject(name));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// �رն���
			this.closeAll(conn, ps, rs);
		}
		return map;
	}

	/**
	 * ��ѯsql���������
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findMulitObject(String sql, List<Object> params) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// ����
		conn = this.getConnection();
		// Ԥ����
		try {
			ps = conn.prepareStatement(sql);
			// ���ò���
			this.setParams(ps, params);
			// ִ�в�ѯ�õ������
			rs = ps.executeQuery();
			// ��ȡ��������
			List<String> names = this.getAllColumnName(rs);
			while (rs.next()) { // ע�����ﲻ����if ��Ϊʹ��if�Ļ�ֻ��ִ��һ�� ���õ��Ľ���ͳ��˲�һ������
				Map<String, Object> map = new HashMap<String, Object>();
				// ѭ������
				for (String name : names) {
					map.put(name, rs.getObject(name));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * ���ݽ������ȡ���ݿ��е������б���
	 * 
	 * @param rs
	 * @return
	 */
	private List<String> getAllColumnName(ResultSet rs) {
		List<String> names = new ArrayList<String>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				names.add(rsmd.getColumnName(i + 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return names;
	}

}
