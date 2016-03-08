import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class ToolBar {

	JToolBar toolbar;
	
	ToolBar(){
	    toolbar = new JToolBar();
	    toolbar.setRollover(true);	
	}
	
	public void add(Component element){
	    this.toolbar.add(element);
	    this.toolbar.addSeparator();
		
	}
	
	
	public void attachTo(JFrame frame){
	    Container contentPane = frame.getContentPane();
	    contentPane.add(toolbar, BorderLayout.NORTH);
	}
}
