package bbl.io.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.file.Path;

public class CodeCommentsSeparation {

	public static void main(String[] args) throws Exception
	{
		// args[0] - file path for file contaning both Java class code and comments
		// args[1] - result file with only code
		// args[2] - result file with only comments
		// example args[0] "src/bbl/io/test/InputOutputTest.java"
		// TODO
		// from one file contains code and comments to crate two files
		// one with only comments and second with only code
		if(args.length<3)
		{
			String exc=String.format("Parametrs less than 3. <InputFileName> <CodeFileName> <CommentFileName>");
			throw new Exception(exc);	
		}
		String inputFileName = args[0];
		String codeFileName = args[1];
		String commentFileName = args[2];
		Path path=Path.of(inputFileName);
		System.out.println(path);
		Path pathCur=Path.of(".");
		System.out.println(pathCur.toAbsolutePath().normalize());
		try(BufferedReader input = new BufferedReader(new FileReader(inputFileName));)
		{
			BufferedWriter code = new BufferedWriter(new FileWriter(codeFileName));
			BufferedWriter comment = new BufferedWriter(new FileWriter(commentFileName));
					
					String str=input.readLine();
					while(str!=null)
					{
						
						if(str.trim().startsWith("//"))
						{
							comment.write(str+"\n");
						}
						else
						{
							code.write(str+"\n");
						}
						str=input.readLine();
					}
			code.close();
			comment.close();
		}
		
	}


}
