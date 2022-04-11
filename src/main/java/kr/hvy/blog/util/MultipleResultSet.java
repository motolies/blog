package kr.hvy.blog.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Getter
@Setter
public class MultipleResultSet {

	private HashMap<String, Object> tables = new HashMap<String, Object>();
	private HashMap<String, Object> resultMessage = new HashMap<String, Object>();

	private int page = 1;
	private int pageSize = 10;
	private int offset = 0;
	private int totalCount = 0;
	private int totalPages;
	private int begin;
	private int end;

	public MultipleResultSet() {
	}

	public MultipleResultSet(int page) throws RuntimeException {
		this.setPage(page);
	}

	public MultipleResultSet(int page, int pageSize) throws RuntimeException {
		this.setPage(page);
		this.setPageSize(pageSize);
	}

	public int getPageIndex() {
		return page - 1;
	}

	public void processResultSet(CallableStatement callableSt) {
		try {

			int tCount = 0;
			boolean isResult = callableSt.execute();
			while (isResult) {
				List<HashMap<String, Object>> table = new ArrayList<>();
				ResultSet rs = callableSt.getResultSet();

				// 칼럼 개수
				ResultSetMetaData md = rs.getMetaData();
				int columns = md.getColumnCount();
				// 테이블 생성
				while (rs.next()) {
					HashMap<String, Object> row = new HashMap<String, Object>();
					for (int i = 1; i <= columns; ++i) {
						String colName = md.getColumnLabel(i);
						row.put(colName, rs.getObject(colName));
					}
					table.add(row);
				}

				tables.put(MessageFormat.format("table{0}", tCount), table);
				rs.close();
				isResult = callableSt.getMoreResults();
				tCount += 1;
			}
		} catch (SQLException e) {
			log.info("processResultSet: " + e.toString());
		}
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.totalPages = (int) Math.ceil((double) totalCount / (double) this.pageSize);
		this.begin = Math.max(1, this.page - 4);
		this.end = Math.min(this.page + 5, this.totalPages == 0 ? 1 : this.totalPages);
	}

	public void setPage(int page) throws RuntimeException {

		if (page < 1)
			throw new RuntimeException("1보다 작은 수를 page에 할당 할 수 없습니다.");

		this.offset = (page - 1) * this.pageSize;
		this.page = page;
	}

	public void setPageSize(int pageSize) throws RuntimeException {
		if (pageSize < 1)
			throw new RuntimeException("1보다 작은 수를 pageSize에 할당 할 수 없습니다.");
		this.offset = (this.page - 1) * pageSize;
		this.pageSize = pageSize;
	}

}
