package com.beebank.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOAccessFileTest {

	/** 
	 * 使用io类的RandomAccessFile进行访问文件内容
	 * r 代表以只读方式打开指定文件 
     * rw 以读写方式打开指定文件 
     * rws 读写方式打开，并对内容或元数据都同步写入底层存储设备 
     * rwd 读写方式打开，对文件内容的更新同步更新至底层存储设备
	 * @Title: Read 
	 * @Description: TODO
	 * @author haiqing.tan
	 * 2016年9月13日
	 */
	public static void read(String path, int pointCut){
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(path, "r"); //只读 
			//获取RandomAccessFile对象文件指针的位置，初始位置是0  
			System.out.println("RandomAccessFile文件指针的初始位置:"+raf.getFilePointer());  
			raf.seek(pointCut);//移动文件指针位置  
			byte[]  buff=new byte[1024];  
			//用于保存实际读取的字节数  
			int hasRead=0;  
			//循环读取  
			while((hasRead=raf.read(buff))>0){  
			    //打印读取的内容,并将字节转为字符串输入  
			    System.out.println(new String(buff,0,hasRead));  
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void write(String path){
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(path, "rw"); //读写
			//将记录指针移动到文件最后  
	        raf.seek(raf.length());  
	        raf.write("我是来搞笑的 \n".getBytes());  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String path = "test1.txt";
		int point = 0;
		read(path, point);
//		write(path);
	}

}
