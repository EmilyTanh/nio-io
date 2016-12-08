package com.beebank.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOTest {

	public static void main(String[] args) {
		FileInputStream fis = null;
		FileOutputStream fos = null;

		FileChannel fcin = null;
		FileChannel fcout = null;
		ByteBuffer buffer = ByteBuffer.allocate(86);//得到缓冲区，缓冲区容量为86
		
		try {
			fis = new FileInputStream(new File("test1.txt"));
			fos = new FileOutputStream(new File("test2.txt"));
			
			fcin = fis.getChannel();//文件输出通道
			fcout = fos.getChannel();//文件写入通道
			int bytes = fcin.read(buffer);
			
//			buffer.mark();//对当前位置进行标记position
			buffer.put(new byte[]{1,2,3,4,'\n'});//往缓存区中添加数据
			while(buffer.hasRemaining()){//判断当前位置(position)与limit中间是否存在元素，若存在，写入通道fcout
				buffer.flip();//使buffer当前位置定位到开始位置，默认是在buffer最后
				fcout.write(buffer); //写入文件操作(buffer读到channel)
			}
			
			System.err.println(buffer.limit());
			
//			buffer.clear();
//			buffer.reset();
			buffer.rewind();//1、读buffer中的数据，将position的位置设为0，limit不变
			if(bytes != -1){
				 byte[] bt=buffer.array();
				 System.out.print(System.nanoTime() + new String(bt, 0, bytes));
				 System.out.println();
				 System.out.println(buffer.get(1));//get方法得到的是某一个ascii码值，从0开始
//				 System.out.println("位置："+ buffer.position() +",大小：" + buffer.limit());
//				 buffer.flip();//2、使buffer当前位置定位到开始位置，将limit定位到之前position的位置
				 System.out.println("位置："+ buffer.position() +",大小：" + buffer.limit());
				 fcout.write(buffer); //写入文件操作
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(fis != null){
					fis.close();
				}
				if (fcin != null) {
					fcin.close();
				}
				if (fcout != null) {
					fcout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
