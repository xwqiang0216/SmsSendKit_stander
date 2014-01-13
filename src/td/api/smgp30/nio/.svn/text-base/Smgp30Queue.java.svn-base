package td.api.smgp30.nio;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.hskj.form.SmsMessage;




public class Smgp30Queue {
	private int add_count = 0;
	private int max_size;
	private int max_wait_size;
	private long max_wait_time;
	private int submit_size;
	private int submit_index = 0;
	private List<Smgp30MessageUtil> list;

	public Smgp30Queue(int max_size , int max_wait_time ,int max_wait_size){
		list = new ArrayList<Smgp30MessageUtil>();
		this.max_size = max_size;
		this.max_wait_time = max_wait_time;
		this.max_wait_size = max_wait_size;
		submit_index = -1;
	}

	synchronized public boolean addMessage(Smgp30MessageUtil messageUtil){
		if(messageUtil.getSmsMessage() != null){
			add_count ++;
			list.add(submit_index+1,messageUtil);
		}else{
			list.add(messageUtil);
		}
		
		
		return true;
		

	}
	synchronized public int getQueueSize(){
		return add_count ;
	}
	synchronized public String getQueueInfo(){
		return "list.size:"+list.size() + "   submit_size:"+submit_size+"   submit_index:"+submit_index;
	}
	synchronized public int submitMessage(DataOutputStream  out){
		Smgp30MessageUtil tmp = null;
		try{
			while(submit_index < list.size() - 1){
				tmp = list.get(submit_index + 1 );
				if(tmp.getSmsMessage() == null){
					tmp.getSmgpMessage().write(out);
					list.remove(submit_index + 1);

				}else{
					if(submit_index > max_wait_size - 1){
						break;
					}else{
						tmp = list.get(submit_index + 1);
						tmp.getSmgpMessage().write(out);
						tmp.setSubmit_time(System.currentTimeMillis());
						submit_index++;
						submit_size++;
						return 1;
					}
				}

			}
		}catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		return 0;

	}

	synchronized public List<Smgp30MessageUtil> checkTimeOut(){
		List<Smgp30MessageUtil> tmpList = new ArrayList<Smgp30MessageUtil>();
		for(int i = 0 ; i < submit_size ; i++){
			if(list.get(i).getSmsMessage() != null && System.currentTimeMillis() - list.get(i).getSubmit_time() > max_wait_time * 1000){
				tmpList.add(list.remove(i));
				submit_size -- ;
				submit_index -- ;
				add_count --;
				i--;
			}
		}
		return tmpList;

	}
	
	synchronized public SmsMessage resp(String seq){
		Smgp30MessageUtil tmpMessage = null;
		for(int i = 0 ; i < submit_size ; i++){
			if(seq.equalsIgnoreCase(String.valueOf(list.get(i).getSmgpMessage().getSe()))){
				
				tmpMessage = list.remove(i);
				submit_size -- ;
				submit_index -- ;
				add_count --;
				break;
			}
		}
		if(tmpMessage != null){
			return tmpMessage.getSmsMessage();
		}
		return null;

	}
	
	synchronized public void reCreateQueue(){
		
		for(int i = 0 ; i < list.size() ; i++){
			if(list.get(i).getSmsMessage() == null){
				list.remove(i);
				i--;
			}else{
				list.get(i).getSmgpMessage().getHeader().reCreateSeq();
			}
		}
		submit_size = 0 ; 
		submit_index = -1;
		add_count = list.size();
	}
}
