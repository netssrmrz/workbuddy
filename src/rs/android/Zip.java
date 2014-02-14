package rs.android;
import java.io.*;
import android.net.*;

public class Zip
{
	public java.io.File template_file, timesheet_file;
	
  public void Copy(String src_filename, String dst_filename)
	{
		java.util.zip.ZipInputStream template_zip;
		java.util.zip.ZipOutputStream timesheet_zip;
		java.io.FileInputStream template_stream;
		java.io.FileOutputStream timesheet_stream;
		String template_name, state;
		java.io.File download_dir;
		rs.android.Zip_Entry zip_entry;

		state = android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state))
		{
			download_dir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS);
			download_dir.mkdirs();

			try
			{
				template_name = src_filename;
				template_file = new java.io.File(download_dir, template_name);
				template_stream = new java.io.FileInputStream(template_file);
				template_zip = new java.util.zip.ZipInputStream(template_stream);

				timesheet_file = new java.io.File(download_dir, dst_filename);
				timesheet_stream = new java.io.FileOutputStream(timesheet_file);
				timesheet_zip = new java.util.zip.ZipOutputStream(timesheet_stream);

				do
				{
					zip_entry = rs.android.Zip_Entry.Read_Zip_Entry(template_zip);
					if (zip_entry != null)
					{
						this.On_Copy_Entry(zip_entry);
						rs.android.Zip_Entry.Write_Zip_Entry(timesheet_zip, zip_entry);
					}
				}
				while (zip_entry != null);

				timesheet_zip.close();
				timesheet_stream.close();
				template_zip.close();
				template_stream.close();
				
				this.On_Copy_Finish(android.net.Uri.fromFile(timesheet_file));
			}
			catch (java.io.IOException e)
			{
				android.util.Log.d("workbuddy", e.toString());
				this.On_Copy_Finish(null);
			}
		}
	}

	public static int Count_Entries(String filename)
	{
		int res=0;
		java.util.zip.ZipFile zip_file;
		String state;
		java.io.File download_dir, template_file;

		//android.util.Log.d("Zip.Copy()", "Entry");
		state = android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state))
		{
			download_dir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS);
			download_dir.mkdirs();

			try
			{
				template_file = new java.io.File(download_dir, filename);
		    zip_file = new java.util.zip.ZipFile(template_file);
			}
			catch (java.io.IOException e)
			{
				zip_file = null;
			
			}
			if (zip_file != null)
				res = zip_file.size();
		}
		return res;
	}

	public void On_Copy_Entry(rs.android.Zip_Entry entry)
	{

	}
	
	public void On_Copy_Finish(android.net.Uri file_uri)
	{
		
	}
}
