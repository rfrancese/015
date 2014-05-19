package it.listeners;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class Cancella implements OnClickListener{
	private LinearLayout linear;
	private LinearLayout root;
	
	public Cancella(LinearLayout root, LinearLayout linear)
	{
		this.linear=linear;
		this.root=root;
	}
	
	public void onClick(View v) {
		root.removeView(linear);
		
	}

}
