using System;
using System.Collections.Generic;
using System.Collections;
using System.Text.RegularExpressions;

namespace myLang
{
	public static class Extension
	{
		// str		: @"if (o) a = 100"
		// regexp	: @"\s*(('if'|'else')|(\d+)|([a-zA-Z_]\w*))\s*"
		static public IEnumerable<string[]> matchAll(string str, string regexp) {
//			Pattern p = matchPattern(regexp);
			Regex pattern = new Regex(regexp);
//			Matcher m = p.matcher(str);
			MatchCollection m = pattern.Matches (str);
//			while (m.find()) {
			foreach (Match match in m) {
				int count = match.Groups.Count;
				string[] groups = new string[count];
				for (int i = 0; i < count; i++) {
					groups [i] = ((Match)match.Groups [i]).Value;
				}
				yield return groups;
			}
		}

		public static string Take(string str, int offset, int num)
		{
			string s = "";
			for (int i = 0; i < num; i++) {
				if (str.Length > offset + i)
					s += str [offset + i];
				else
					break;
			}
			return s;
		}
	}
}
