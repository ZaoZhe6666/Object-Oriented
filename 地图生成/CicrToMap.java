package test;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CicrToMap {
	static String CicrFileAddress="C:/Users/章健飞/Desktop/123.circ";
	static String MapFileAddress="C:/Users/章健飞/Desktop/map.txt";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		char[] input=new char[100000];
		int i=0;
		String filename = CicrToMap.CicrFileAddress;
		File file = new File(filename);
		InputStreamReader reader = null;
        try {
            System.out.println("以字符为单位读取文件内容，一次读一个字节：");
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                if (((char) tempchar) != '\r') {
                	input[i++]=(char) tempchar;
                	if(i==100000){
                		System.out.println("the input file is too large!");
                		break;
                	}
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String in=String.copyValueOf(input);
        System.out.println(in);
        
        String regEx="(<wire from=\\\"\\(\\d+,\\d+\\)\\\" to=\\\"\\(\\d+,\\d+\\)\\\"/>)";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(in);
		
		Wire[] wires=new Wire[12800];
		int iw=0;
		while(m.find()){
			wires[iw++]=new Wire(m.group());
		}
		Map map=new Map();
		for(int j=0;j<iw;j++){
			map.lineup(wires[j]);
		}
		
		map.outputtofile(CicrToMap.MapFileAddress);
	}
	
	

}

class Wire{
	Point from;
	Point to;
	
	public Wire(String in){
		this.from	=	new Point();
		this.to		=	new Point();
		System.out.println(in);
		//in="<wire from="(110,50)" to="(110,110)"/>"
		String regEx="<wire from=\\\"\\((\\d+),(\\d+)\\)\\\" to=\\\"\\((\\d+),(\\d+)\\)\\\"/>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(in);
		if(m.find()){
			System.out.println(m.group(1)+" "+m.group(2));
			System.out.println(m.group(3)+" "+m.group(4));
		}
		this.from.x=Integer.parseInt(m.group(1));
		this.from.y=Integer.parseInt(m.group(2));
		this.to.x=Integer.parseInt(m.group(3));
		this.to.y=Integer.parseInt(m.group(4));
	}
	
	public void print(){
		System.out.println("from "+this.from+" to "+this.to);
	}
}

class Map{
	int[][] map;
	
	public Map(){
		this.map=new int[80][80];
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				this.map[i][j]=0;
			}
		}
	}
	
	public void lineup(Wire w){
		w.print();
		if(w.from.x==w.to.x){
			//竖线
			int x=w.from.x/10;
			for(int y=w.from.y/10;y<w.to.y/10;y++){
				if(x>=80||y>=80){
					System.out.print("超出80x80:");
					w.print();
					break;
				}
				if(this.map[x][y]%2==0){
					this.map[x][y]+=1;
				}
			}
		}
		else if(w.from.y==w.to.y){
			//横线
			int y=w.from.y/10;
			for(int x=w.from.x/10;x<w.to.x/10;x++){
				if(x>=80||y>=80){
					System.out.print("超出80x80:");
					w.print();
					break;
				}
				if(this.map[x][y]<2){
					this.map[x][y]+=2;
				}
			}
		}
		else{
			System.out.println("Error!");
		}
	}
	
	public void outputtofile(String url){
		String output = new String();
		char[] tempCharArray=new char[80];
		for(int j=0;j<80;j++){
			for(int i=0;i<80;i++){
				tempCharArray[i]=(char) ('0'+this.map[i][j]);
			}
			output+=String.copyValueOf(tempCharArray)+"\n";
		}
		
		FileOutputStream fop=null;
		File file;
		try{
			file=new File(URLDecoder.decode((url),"UTF-8"));
			fop=new FileOutputStream(file);
			if(!file.exists()){
				file.createNewFile();
			}
			byte[] contentInBytes=output.getBytes();
			
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		}catch(IOException e) {

			System.out.println("IO Error!");
		  } finally {
		   try {
		    if (fop != null) {
		     fop.close();
		    }
		   } catch (IOException e) {

			   System.out.println("IO Error!");
		   }
		 }
	}
}