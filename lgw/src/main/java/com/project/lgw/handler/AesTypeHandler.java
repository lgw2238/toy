package com.project.lgw.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;

import com.project.lgw.util.AES256Util;


@MappedJdbcTypes(JdbcType.VARCHAR)
public class AesTypeHandler implements TypeHandler {

	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
		/** Password Encoding **/
		if(parameter != null) {
			String encode = null;
			try {
				AES256Util aes256 = new AES256Util("");
				encode = aes256.encode((String) parameter);
			} catch (Exception e) {
				String errmsg = String.format("Fail to encrypt :: [%s]", parameter);
				throw new SQLException(errmsg);
			}
			ps.setString(i, encode);
		} else {
			ps.setString(i, (String) parameter);
		}

	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName);
		if(value != null) {
			String decode = null;
			try {
				AES256Util aes256 = new AES256Util("");
				decode = aes256.decode(value);
			} catch (Exception e) {
				String errmsg = String.format("Fail to decrypt :: [%s]", columnName);
				throw new SQLException(errmsg);
			}
			return decode;
		} else {
			return rs.getString(columnName);
		}
//		return rs.getString(columnName);
	}

	@Override
	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		String value = rs.getString(columnIndex);
		if(value != null) {
			String decode = null;
			try {
				AES256Util aes256 = new AES256Util("");
				decode = aes256.decode(value);
			} catch (Exception e) {
				String errmsg = String.format("Fail to decrypt :: [%s]", columnIndex);
				throw new SQLException(errmsg);
			}
			return decode;
		} else {
			return rs.getString(columnIndex);
		}
//		return rs.getString(columnIndex);
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String value = cs.getString(columnIndex);
		if(value != null) {
			String decode = null;
			try {
				AES256Util aes256 = new AES256Util("");
				decode = aes256.decode(value);
			} catch (Exception e) {
				String errmsg = String.format("Fail to decrypt :: [%s]", columnIndex);
				throw new SQLException(errmsg);
			}
			return decode;
		} else {
			return cs.getString(columnIndex);
		}
//		return cs.getString(columnIndex);
	}

}
