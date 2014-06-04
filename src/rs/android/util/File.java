package rs.android.util;

public class File
{
	public static void Dump_Dir(java.io.File dir)
	{
		String[] filenames;

		if (dir!=null && dir.isDirectory())
		{
		  filenames=dir.list();
		  if (rs.android.Util.NotEmpty(filenames))
		  {
			  for (String filename: filenames)
				{
					android.util.Log.d("Dump_Dir", filename);
				}
		  }
		}
	}
	
	public static boolean Copy_File(String in_filepath, String out_filepath)
	{
		java.io.FileInputStream in_stream;
		java.io.OutputStream out_stream;
		byte[] buffer = new byte[1024];
		int length;
		boolean res=false;

		try
		{
			in_stream = new java.io.FileInputStream(in_filepath);
			out_stream = new java.io.FileOutputStream(out_filepath);

			while ((length = in_stream.read(buffer)) > 0)
			{
				out_stream.write(buffer, 0, length);
			}

			out_stream.flush();
			out_stream.close();
			in_stream.close();			
			res=true;
		}
		catch (Exception e)
		{
			android.util.Log.d("rs.android.Util.Copy_File()", e.toString());
		}
		return res;
	}
	
	public static android.net.Uri Save_File(String name, String data)
	{
		String state;
		java.io.File sd_dir, csv_file;
		java.io.FileOutputStream csv_stream;
		android.net.Uri res=null;

		state = android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state))
		{
			sd_dir = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS);
			sd_dir.mkdirs();
			if (rs.android.Util.NotEmpty(data))
			{
				csv_file = new java.io.File(sd_dir, name);
				try
				{
					csv_stream = new java.io.FileOutputStream(csv_file);
					csv_stream.write(data.getBytes());
					csv_stream.close();
				}
				catch (Exception e)
				{
					csv_file=null;
				}

				if (csv_file!=null)
				  res = android.net.Uri.fromFile(csv_file);
			}
		}
		return res;
	}
	
	public static boolean Exists(String filename)
	{
		boolean res=false;
		java.io.File download_dir;
		String state;
		java.io.File file;

		state = android.os.Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(state))
		{
			download_dir = android.os.Environment.
			  getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS);
			download_dir.mkdirs();

			file=new java.io.File(download_dir, filename);
			res=file.exists();
		}
		return res;
	}
}
