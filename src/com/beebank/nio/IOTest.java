package com.beebank.nio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class IOTest {
	

	public static void main(String[] args) {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("test1.txt")));//读文件流
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("test3.txt")));//写文件流
			String line = null;
			while((line = reader.readLine()) != null){
				System.out.println(System.nanoTime() + line);
				writer.write(line);
				writer.newLine();//每读一行，就新写一行
				writer.flush();//需要及时清理流的缓冲区，万一文件过大就有可能无法写入了
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
