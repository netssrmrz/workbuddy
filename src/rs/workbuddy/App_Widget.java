package rs.workbuddy;
import android.widget.*;
import android.content.*;

public class App_Widget 
  extends android.appwidget.AppWidgetProvider
{
	public static final int BUTTON_ID_WORK=1;
	public static final int BUTTON_ID_LUNCH=2;
	public static final int BUTTON_ID_HOME=3;
	public static final int BUTTON_ID_UNDO=4;
	
	public android.widget.Button work_button;
	public android.widget.Button lunch_button;
	public android.widget.Button home_button;
	public android.widget.Button undo_button;
	
	@Override
	public void onUpdate(android.content.Context ctx, android.appwidget.AppWidgetManager man, int[] widget_ids)
	{
		int c;
		android.widget.RemoteViews view;
		android.app.PendingIntent p;
		android.content.Intent i;
		rs.workbuddy.Db db;
		rs.workbuddy.db.App_Widget widget;
		String button_text;
		
		db=new rs.workbuddy.Db(ctx);
		for (c=0; c<widget_ids.length; c++)
		{
			widget=rs.workbuddy.db.App_Widget.Select_By_Id(db, (long)widget_ids[c]);
			button_text=Project.Get_Project_Name(db, widget.project_id);
			
			view=new android.widget.RemoteViews(ctx.getPackageName(), R.layout.widget_layout_2);
			view.setTextViewText(R.layout.widget_layout_2, button_text);
			
			i=new android.content.Intent("rs.workbuddy.App_Widget.BUTTON_CLICKED");
			i.putExtra("widget_id", widget.id);
			p=android.app.PendingIntent.getBroadcast(ctx, BUTTON_ID_WORK, i, android.app.PendingIntent.FLAG_UPDATE_CURRENT);
			view.setOnClickPendingIntent(R.id.widget_work_button, p);

			man.updateAppWidget(widget_ids[c], view);
		}
		db.Close();
	}
	
	@Override
	public void onReceive(android.content.Context ctx, android.content.Intent i)
	{
		long widget_id;
		rs.workbuddy.db.App_Widget app_widget;
		rs.workbuddy.Db db;
		Long rounding;
		
		super.onReceive(ctx, i);
		
		if (i.getAction().equals("rs.workbuddy.App_Widget.BUTTON_CLICKED"))
		{
			db=new rs.workbuddy.Db(ctx);
			widget_id=i.getLongExtra("widget_id", 0);
			app_widget=rs.workbuddy.db.App_Widget.Select_By_Id(db, widget_id);
			rounding = rs.workbuddy.Settings_Activity.Get_Rounding(ctx);
			
			Work_Event.Save_New(db, app_widget.event_type, app_widget.project_id, rounding);
		}
	}
	
	@Override
	public void onDeleted(android.content.Context ctx, int[] widget_ids)
	{
		super.onDeleted(ctx, widget_ids);
	}
	
	@Override
	public void onDisabled(android.content.Context ctx)
	{
		super.onDisabled(ctx);
	}
	
	@Override
	public void onEnabled(android.content.Context ctx)
	{
		super.onEnabled(ctx);
	}
}
