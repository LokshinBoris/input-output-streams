package bbl.io.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

class InputOutputTest {

	private static final String STREAM_FILE = "stream-file";
	private static final String HELLO = "Hello";
	private static final String WRITER_FILE = "writer-file";
	
	@Test
		// Testing PrintStream class
	// Creates PrintStream object and connect it to file with name STREAM_FILE
	void printStreamTest() throws Exception
	{
		// try recourses block allows automatic closing after exiting
		try (PrintStream printStream=new PrintStream(STREAM_FILE);)
		{
		printStream.println(HELLO);
		}
		
		BufferedReader reader = new BufferedReader(new FileReader(STREAM_FILE));
		assertEquals(HELLO,reader.readLine());
	}

	void printWriterTest() throws Exception
	{
		PrintWriter printWriter=new PrintWriter(WRITER_FILE);
		printWriter.println(HELLO);
		printWriter.close();
		BufferedReader reader = new BufferedReader(new FileReader(WRITER_FILE));
		assertEquals(HELLO,reader.readLine());
	}
	
	@Test
	void pathTest()
	{
		Path pathCurrent= Path.of("..");
//		System.out.printf("%s - path\".\"\n",pathCurrent);
//		System.out.printf("%s - normalized path \".\"\n",pathCurrent.normalize());
		System.out.printf("%s - %s\n",pathCurrent.toAbsolutePath().normalize(),Files.isDirectory(pathCurrent)? "dir": "file");
		pathCurrent = pathCurrent.toAbsolutePath().normalize();
		System.out.printf("count of levels is %d\n", pathCurrent.getNameCount());		
	}
	
	@Test
	void printDirectoryTest() throws IOException
	{
		// printDirectory("C:\\WORKER\\PARK10\\Desktop.ini",2);
		printDirectory("C:\\",2);
	}

	public class MyFileVisitResult extends SimpleFileVisitor<Path>
	{
		private int shift=0;
		public MyFileVisitResult(int shift)
		{
			this.shift=shift;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
		{
			if(dir.getNameCount()>0)
			{
				System.out.printf("%s %s <dir>\n", " ".repeat(shift*4), dir.getFileName().toString());
				shift++;
			}
			else
			{
				System.out.printf("%s.\n"," ".repeat(shift*4));
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException 
		{
			String type = "<";
			if(attrs.isDirectory()) type=type.concat("dir>");
			else if(attrs.isRegularFile()) type=type.concat("file>");
				 else type=type.concat("other>");

			System.out.printf("%s %s %s\n", " ".repeat(shift*4), file.getFileName().toString(),type);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException 
		{			
			if(exc instanceof AccessDeniedException)
			{	
				
				String str=file.getNameCount()>1? file.getParent().getFileName().toString(): file.getFileName().toString();
				System.out.printf("%s %s <dir> access denied\n", " ".repeat(shift*4), str);
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
		{
			shift --;
			if(exc!=null)
			{
				System.err.println(exc);
			}
				return FileVisitResult.CONTINUE;
		}
	}
	private void printDirectory(String dirPathStr, int depth) throws IOException
	{
	
		// print directory content in the format
		// if depth == -1 all levels should printing out
		// exceptions

		// <name> - <dir / file>
		//      <name>
		// using Files.walkFileTree
		Path path= Path.of(dirPathStr).toAbsolutePath().normalize();
		 File mf=new File(path.toString());
		if (mf.exists() && mf.isDirectory())
		{	
			MyFileVisitResult mfv=new MyFileVisitResult(0);  
			if(depth==-1) depth=Integer.MAX_VALUE;
			Files.walkFileTree(path, new HashSet<>(), depth, mfv); 
		}
		else
		{
			System.out.printf("Path %s not exist or not directory\n",path.toString());
		}
	}
}
