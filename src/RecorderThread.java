public class RecorderThread implements Runnable{
	static JavaSoundRecorder recorder;
	public  RecorderThread(String filename){
		recorder = JavaSoundRecorder.getRecord(filename.replaceAll(".pdf", "_audio.wav"));		
	}

	@Override
	public void run() {
		recorder.start();			
	}
	
	public void stop(){
		System.out.println("trying to stop");
		recorder.finish();
	}

}
