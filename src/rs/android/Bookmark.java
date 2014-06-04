package rs.android;

public class Bookmark
{
  public String name;
	public int start_pos;
	public int end_pos;

  public static Bookmark[] Get_Bookmarks(String data)
	{
		Bookmark[] res=null;
		Bookmark bookmark;
		java.util.ArrayList<Bookmark> bookmarks;
		int pos=0;

		//android.util.Log.d("Bookmark.Get_Bookmarks()", "Entry");
		bookmarks = new java.util.ArrayList<Bookmark>();

		do
		{
			bookmark = Next_Bookmark(data, pos);
			if (bookmark != null)
			{
				bookmarks.add(bookmark);
				pos=bookmark.end_pos;
			}
		}
		while (bookmark != null);
		
		if (rs.android.Util.NotEmpty(bookmarks))
		{
			res=new Bookmark[bookmarks.size()];
			res=bookmarks.toArray(res);
		}

		return res;
	}

	public static Bookmark Next_Bookmark(String data, int start_pos)
	{
		Bookmark res=null;
		int pos=start_pos, tag_start, tag_end;
		String tag_str, tag_attrs[], attr_tokens[];

		//android.util.Log.d("Bookmark.Next_Bookmark()", "Entry");
		tag_start = data.indexOf("<w:bookmarkStart ", pos);
		if (tag_start != -1)
		{
			//android.util.Log.d("Next_Bookmark()", data.substring(tag_start));
			tag_end = data.indexOf("/>", tag_start);
			tag_str = data.substring(tag_start + 1, tag_end);
			//android.util.Log.d("Next_Bokkmark()", tag_str);
			tag_attrs = tag_str.split(" ");
			if (Util.NotEmpty(tag_attrs))
			{
				res = new Bookmark();
				for (String attr_str: tag_attrs)
				{
					attr_tokens = attr_str.split("=");
					if (attr_tokens[0].equals("w:name"))
						res.name = attr_tokens[1].replace("\"", "");
				}
				res.start_pos = tag_start;
				res.end_pos = tag_end + 2;
				//android.util.Log.d("Next_Bookmark()", rs.android.Util.Obj_To_String(res));
			}
		}
		return res;
	}
}
