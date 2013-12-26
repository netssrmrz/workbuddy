package rs.android;

public class Zip_Entry
{
	public byte[] data;
	public java.util.zip.ZipEntry entry;

	public static void Write_Zip_Entry(
	  java.util.zip.ZipOutputStream zip,
		byte[] data, String name)
	{
		java.util.zip.ZipEntry entry;

		entry = new java.util.zip.ZipEntry(name);
		try
		{
			zip.putNextEntry(entry);
			zip.write(data);
			zip.closeEntry();
		}
		catch (java.io.IOException e)
		{}
	}

	public static void Write_Zip_Entry(java.util.zip.ZipOutputStream zip, Zip_Entry entry)
	{
		Write_Zip_Entry(zip, entry.data, entry.entry.getName());
	}
	
	public static Zip_Entry Read_Zip_Entry(java.util.zip.ZipInputStream zip)
	{
		Zip_Entry res=null;
		int data;
		java.io.ByteArrayOutputStream buff=null;
		java.util.zip.ZipEntry entry=null;

		try
		{ 
		  entry = zip.getNextEntry();
		  if (entry != null)
		  {
				buff = new java.io.ByteArrayOutputStream();
				do
				{
					data = zip.read();
					if (data != -1)
						buff.write(data);
				}
				while (data != -1);
			  zip.closeEntry();
		  }
		}
		catch (java.io.IOException e)
		{
			buff = null;
		}

		if (Util.NotEmpty(buff))
		{
		  res = new Zip_Entry();
			res.data = buff.toByteArray();
			res.entry = entry;
		}
		return res;
	}
}
