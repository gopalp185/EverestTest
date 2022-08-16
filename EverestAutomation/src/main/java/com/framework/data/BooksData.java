package com.framework.data;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;

public class BooksData {
	public List<String> header = new ArrayList<>();

	public HashMap<String, String> readDataLineByLine(String file) {
		HashMap<String, String> booklist = new HashMap<>();
		try {

			FileReader filereader = new FileReader(file);

			CSVReader csvReader = new CSVReader(filereader);
			String[] nextRecord;
			int col = 0;
			int row = 0;
			while ((nextRecord = csvReader.readNext()) != null) {

				for (String cell : nextRecord) {
					if (row == 0) {
						header.add(cell);
					}
					if (col % 2 == 0) {
						booklist.put(header.get(0) + "_" + row, cell);
					} else if (col % 2 == 1) {
						booklist.put(header.get(1) + "_" + row, cell);
					}
					col = col + 1;
				}
				row = row + 1;
				System.out.println();

			}
			csvReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return booklist;
	}

}
