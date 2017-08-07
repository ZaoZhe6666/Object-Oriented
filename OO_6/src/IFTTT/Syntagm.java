package IFTTT;

public class Syntagm {
		private boolean valid=true;
		private int style=0;
		private String path="";
		private String trigger="";
		private String task="";
		private int level=1;
		private long big=0;
		private long time=0;
		private String name="";
		
		
		
		public Syntagm(String path,String trigger,String task,int style){
			this.path=path;
			this.trigger=trigger;
			this.task=task;
			this.style=style;
		}
		
		public Syntagm(int level,String path,boolean isd,long big,long time,String name){
			this.path = path;
			this.level = level;
			this.style=(isd)?1:0;
			this.big=big;
			this.time=time;
			this.name=name;
		}
		
		
		public boolean getvalid(){
			return valid;
		}
		public String getpath(){
			return path;
		}
		public String gettrigger(){
			return trigger;
		}
		public String gettask(){
			return task;
		}
		public int getstyle(){
			return style;
		}
		public long getbig(){
			return big;
		}
		public long gettime(){
			return time;
		}
		public String getname(){
			return name;
		}
		public int getlevel(){
			return level;
		}
		public void setname(String name){
			this.name=name;
			return;
		}
		public void setvalid(boolean valid){
			this.valid=valid;
			return;
		}
		public void setpath(String path){
			this.path=path;
			return;
		}
		public void settrigger(String trigger){
			this.trigger=trigger;
			return;
		}
		public void settask(String task){
			this.task=task;
			return;
		}
		public void setstyle(int style){
			this.style=style;
			return;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}
