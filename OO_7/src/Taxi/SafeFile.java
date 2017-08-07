package Taxi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

public class SafeFile {
	private static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";
	
	
	synchronized public int AddFile(String path){
		if(path.matches(matches)){
			File myFilePath = new File(path);
			try{
				if(myFilePath.exists()){//���ļ��Ѵ���
					if(myFilePath.isDirectory()){
						System.out.println("����ͬ���ļ��У�");
					}
					else{
						System.out.println("Ŀ���ļ��Ѵ��ڣ�");
					}
					return 0;
				}
				myFilePath.createNewFile();
			}catch(Exception e){
				return 0;
			}
		}
		else{
			System.out.println("��������ȷ·����");
			return 0;
		}
		return 1;
	}
	
	synchronized public int AddFilebox(String path){
		if(path.matches(matches)){
			File myFilePath = new File(path);
			try{
				if(myFilePath.exists()){//���ļ��Ѵ���
					if(myFilePath.isDirectory()){
						System.out.println("Ŀ���ļ����Ѵ��ڣ�");
					}
					else{
						System.out.println("����ͬ���ļ���");
					}
					return 0;
				}
				myFilePath.mkdirs();
			}catch(Exception e){
				return 0;
			}
		}
		else{
			System.out.println("��������ȷ·����");
			return 0;
		}
		return 1;
	}
	
	synchronized public int DeleteFile(String path){
		if(path.matches(matches)){
			File myFilePath = new File(path);
			try{
				if(myFilePath.exists()){//���ļ��Ѵ���
					if(myFilePath.isFile()){
						myFilePath.delete();
						return 1;
					}
				}
			}catch(Exception e){
			}
		}
		else{
			System.out.println("��������ȷ·����");
		}
		return 0;
	}
	
	synchronized public int DeleteFilebox(String path){
		if(path.matches(matches)){
			File myFilePath = new File(path);
			try{
				if(myFilePath.exists()){//���ļ��Ѵ���
					if(myFilePath.isDirectory()){
						File[] files = myFilePath.listFiles();
						int flag=0;
						for(int i=0;i<files.length;i++){
							if(files[i].isFile()){
								if(DeleteFile(files[i].getAbsolutePath())==0){
									flag=1;
									break;
								}
							}
							else{
								if(DeleteFilebox(files[i].getAbsolutePath())==0){
									flag=1;
									break;
								}
							}
						}
						if(flag==1){
							return 0;
						}
						
						if(!myFilePath.delete()){
							return 0;
						}
						return 1;
					}
				}
			}catch(Exception e){
			}
		}
		else{
			System.out.println("��������ȷ·����");
		}
		return 0;
	}
	
	synchronized public int SeeFile(String path){
		if(path.matches(matches)){
			File myFilePath = new File(path);
			try{
				if(myFilePath.exists()){//���ļ��Ѵ���
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
					if(myFilePath.isDirectory()){
						File[] files = myFilePath.listFiles();
						int sum=0;
						for(int i=0;i<files.length;i++){
							if(files[i].isFile()){
								sum+=files[i].length();
							}
						}
						System.out.println("�ļ�������Ϊ��"+myFilePath.getName());
						System.out.println("�ļ��д�СΪ:"+sum+"B");
						System.out.println("�ļ������һ���޸�ʱ��Ϊ��"+dateFormat.format(myFilePath.lastModified()));
					}
					else{
						System.out.println("�ļ�����Ϊ��"+myFilePath.getName());
						System.out.println("�ļ���СΪ:"+myFilePath.length()+"B");
						System.out.println("�ļ������һ���޸�ʱ��Ϊ��"+dateFormat.format(myFilePath.lastModified()));
					}
					return 1;
				}
			}catch(Exception e){
				return 0;
			}
		}
		else{
			System.out.println("��������ȷ·����");
			return 0;
		}
		return 0;
	}
	
	synchronized public int RenameFile(String path,String oldname,String newname){
		if(path.matches(matches)){
			File oldfile = new File(path+"\\"+oldname);
			File newfile = new File(path+"\\"+newname);
			try{
				if(!oldfile.exists()){
					System.out.println("ԭ�ļ��������ڣ�");
					return 0;
				}
				if(oldfile.isDirectory()){
					System.out.println("�ļ��в��ɸ���");
					return 0;
				}
				if(newfile.exists()){
					return 0;
				}
				if(oldname.equals(newname)){
					System.out.println("�¾��ļ����޲���");
					return 0;
				}
				oldfile.renameTo(newfile);
				return 1;
			}catch(Exception e){
			}
		}
		else{
			System.out.println("��������ȷ·����");
		}
		return 0;
	}
	
	synchronized public int MoveFile(String path,String newpath){
		if(path.matches(matches)){
			File oldPath = new File(path);
			File newPath = new File(newpath);
			try{
				if(!oldPath.exists()){
					return 0;
				}
				if(oldPath.isDirectory()){
					System.out.println("�ļ��в����ƶ���");
					return 0;
				}
				if(newPath.exists()){
					System.out.println("���ļ�·���Ѵ���ͬ���ļ�/�ļ��У�");
					return 0;
				}
				if(oldPath.equals(newPath)){
					System.out.println("�¾��ļ����޲���");
					return 0;
				}
				oldPath.renameTo(newPath);
				return 1;
			}catch(Exception e){
			}
		}
		else{
			System.out.println("��������ȷ·����");
		}
		return 0;
	}
	
	synchronized public int MoveFile(String path,String newpath,String filename){
		if(path.matches(matches)){
			File oldPath = new File(path+"\\"+filename);
			File newPath = new File(newpath+"\\"+filename);
			try{
				if(!oldPath.exists()){
					System.out.println("ԭ�ļ������ڣ�");
					return 0;
				}
				if(oldPath.isDirectory()){
					System.out.println("�ļ��в����ƶ���");
					return 0;
				}
				if(newPath.exists()){
					System.out.println("���ļ�·���Ѵ���ͬ���ļ�/�ļ��У�");
					return 0;
				}
				if(oldPath.equals(newPath)){
					System.out.println("�¾��ļ����޲���");
					return 0;
				}
				oldPath.renameTo(newPath);
				return 1;
			}catch(Exception e){
			}
		}
		else{
			System.out.println("��������ȷ·����");
		}
		return 0;
	}
	
	synchronized public int WriteFile(String path){
		if(path.matches(matches)){
			File myFilePath = new File(path);
			try{
				if(myFilePath.exists()){//���ļ��Ѵ���
					if(myFilePath.isFile()){
						 FileWriter fileWritter = new FileWriter(myFilePath.getAbsoluteFile(),true);
			             BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			             bufferWritter.write("2333");
			             bufferWritter.close();
			             return 1;
					}
					else{
						System.out.println("�ļ��в��ɱ�д��ֵ��");
						return 0;
					}
				}
				else{
					System.out.println("�ļ������ڣ�");
				}
			}catch(Exception e){
			}
		}
		else{
			System.out.println("��������ȷ·����");
		}
		return 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
