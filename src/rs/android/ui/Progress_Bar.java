package rs.android.ui;
import android.content.*;
import android.os.*;

public class Progress_Bar
extends android.widget.ProgressBar
{
	public class Set_Progress
	implements java.lang.Runnable
	{
		public int val;
		
		public void run()
		{
			setProgress(this.val);
		}
	}
	
	public class Inc
	implements java.lang.Runnable
	{
		public void run()
		{
			incrementProgressBy(1);
		}
	}
	
	public class Show_Msg
	implements java.lang.Runnable
	{
		public android.content.Context ctx;
		public String msg;
		
		public void run()
		{
			rs.android.ui.Util.Show_Note(this.ctx, msg);
		}
	}
	
	public android.os.Handler handler;
	public android.content.Context ctx;
	
	public void Set_Progress(int val)
	{
		Set_Progress set_prog_fn;
	
		set_prog_fn=new Set_Progress();
		set_prog_fn.val=val;
		this.handler.post(set_prog_fn);
	}
	
	public void Inc()
	{
		Inc inc_fn;

		inc_fn=new Inc();
		this.handler.post(inc_fn);
	}
	
	public Progress_Bar(android.content.Context ctx)
	{
		super(ctx, null, android.R.attr.progressBarStyleHorizontal);
		this.handler=new android.os.Handler();
	}
	
	public void Show_Msg(android.content.Context ctx, String msg)
	{
		Show_Msg msg_fn;
		
		msg_fn=new Show_Msg();
		msg_fn.ctx=ctx;
		msg_fn.msg=msg;
		this.handler.post(msg_fn);
	}
}
