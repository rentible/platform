package org.wallride.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PagingUtil {

	private PagingUtil() {

	}

	public static List<Integer> getPageNumbers(Integer totalPages, Integer currentPage) {
		List<Integer> pageNumbers = new ArrayList<>();
		if (totalPages > 0) {
			int lastPageOfPagination;
			int firstPageOfPagination;
			if (totalPages <= 7) {
				firstPageOfPagination = 1;
				lastPageOfPagination = totalPages;
			} else {
				firstPageOfPagination = currentPage;
				lastPageOfPagination = currentPage + 6;
			}
			pageNumbers = IntStream.rangeClosed(firstPageOfPagination, lastPageOfPagination).boxed().collect(Collectors.toList());
		}
		return pageNumbers;
	}

}
