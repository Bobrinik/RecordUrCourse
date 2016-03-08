
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Timer;
/**
 * An example of using the PagePanel class to show PDFs. For more advanced
 * usage including navigation and zooming, look at the com.sun.pdfview.PDFViewer class.
 *
 * @author joshua.marinacci@sun.com
 */
public class Main {
	static int page_counter = 1;
	static PDFFile pdffile;
	static PrintWriter writer;
	static long start_time = 0;
	Timer t;
	static Thread recordingThread;
	static RecorderThread r;



	public static void setup() throws IOException {

		//set up the frame and panel
		final JFrame frame = new JFrame("Lecture Follower");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final PagePanel panel = new PagePanel();
		JPanel panel_container = new JPanel();
		ToolBar toolBar= new ToolBar();
		
		panel_container.add(panel);

		frame.add(panel_container);

		JButton nextpage = new JButton("Next Page");
		JButton previouspage = new JButton("Previous Page");
		JButton start = new JButton("Start");
		JButton stop = new JButton("Stop");
		final JTextComponent pathToPdf = new JTextField();
		JButton loadFile = new JButton("Load PDF");
		
		toolBar.add(pathToPdf);
		toolBar.add(loadFile);
		toolBar.add(previouspage);
		toolBar.add(nextpage);
		toolBar.add(start);
		toolBar.add(stop);		
		toolBar.attachTo(frame);


		frame.pack();
		frame.setVisible(true);


		// show the first page
		nextpage.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				page_counter++;
				getPDFpage(panel, pdffile,page_counter);
				frame.pack();
				frame.setVisible(true);
				writer.println(((System.currentTimeMillis() - start_time)/1000+" at page"+page_counter));
			}

		});

		previouspage.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(page_counter > 0){
					page_counter--;
				}
				getPDFpage(panel, pdffile,page_counter);
				frame.pack();
				frame.setVisible(true);

				writer.println("On "+((System.currentTimeMillis() - start_time)/1000+"th second at page "+page_counter));
			}

		});

		loadFile.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(pathToPdf.getText());
				try {
					pdffile = load_PDF_from_buffer(pathToPdf.getText());
					page_counter =1;
				} catch (IOException e) {
					e.printStackTrace();
				}
				getPDFpage(panel, pdffile, page_counter);

				try {
					writer = new PrintWriter(pathToPdf.getText().replaceAll(".pdf","_timing.txt"),"UTF-8");
					System.out.println("Writer has started");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				frame.pack();
				frame.setVisible(true);
			}

		});

		start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				start_time = System.currentTimeMillis();
				r = new RecorderThread ( pathToPdf.getText());
				recordingThread = new Thread(r);
				recordingThread.start();
			}});

		stop.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				start_time = 0;
				writer.close();
				System.out.println("Writer stopped");
				r.stop();
				try {
					recordingThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});

	}

	private static void getPDFpage(PagePanel panel, PDFFile pdffile, int page_counter) {
		PDFPage page = pdffile.getPage(page_counter);
		panel.showPage(page);
	}

	private static PDFFile load_PDF_from_buffer(String pdf_file) throws FileNotFoundException, IOException {
		//System.out.println(pdf_file);
		File file = new File(pdf_file);
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdffile = new PDFFile(buf);

		return pdffile;
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Main.setup();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}
