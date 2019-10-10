package com.rongji.dfish.misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormatRecogniser {
	public String format;

	public DateFormatRecogniser() {
	}

	public DateFormatRecogniser(String format) {
		this.format = format.trim().replaceAll("[0-9]*", "");
	}

	public String getFormat(String flag) {
		String result = "";
		if (format.contains("y") || format.contains("Y")) {
			if (format.contains("-")) {
				result += "yyyy-";
			} else if (format.contains("/")) {
				result += "yyyy/";
			} else {
				result += "yyyy";
			}
		}
		if (format.toLowerCase().contains("y")
				|| format.toLowerCase().contains("d")) {
			if (format.toLowerCase().contains("m")
					&& (Math.abs(((format.toLowerCase().indexOf("m")) - (format
							.toLowerCase().lastIndexOf("y")))) < 3 || Math
							.abs(((format.toLowerCase().indexOf("d")) - (format
									.toLowerCase().lastIndexOf("m")))) < 3)) {
				if (format.contains(" ")) {
					if ((format.indexOf(" ") - format.toLowerCase()
							.indexOf("m")) > 0) {
						result += "MM";
					}
				} else {
					result += "MM";
				}
			}
		}

		if ((format.toLowerCase().contains("d"))) {
			if (((format.substring(0, format.indexOf("d"))).toLowerCase()
					.contains("m"))) {
				if (format.contains("-")) {
					result += "-dd";
				} else if (format.contains("/")) {
					result += "/dd";
				} else {
					result += "dd";
				}
			} else {
				result += "dd";
			}

		}
		result += " ";

		if (format.contains("h") || format.contains("H")) {
			if ("java".equalsIgnoreCase(flag)
					|| "javascript".equalsIgnoreCase(flag)) {
				result += "HH";
			} else if ("oracle".equalsIgnoreCase(flag)) {
				result += "HH24";
			} else {
				result = "请输入正确标识！";
				return result;
			}
			if (format.contains(":")) {
				result += ":";
			}
		}
		if (format.toLowerCase().contains("h")
				|| format.toLowerCase().contains("s")) {
			if ((format.toLowerCase().contains("m") || format.toLowerCase()
					.contains("i"))
					&& (Math.abs(((format.toLowerCase().indexOf("m")) - (format
							.toLowerCase().lastIndexOf("h")))) < 3
							|| Math
									.abs(((format.toLowerCase().indexOf("s")) - (format
											.toLowerCase().lastIndexOf("m")))) < 3
							|| Math
									.abs(((format.toLowerCase().indexOf("i")) - (format
											.toLowerCase().lastIndexOf("h")))) < 3 || Math
							.abs(((format.toLowerCase().indexOf("s")) - (format
									.toLowerCase().lastIndexOf("i")))) < 3)) {
				if (format.contains(" ")) {
					if ((format.indexOf(" ") - format.toLowerCase()
							.lastIndexOf("m")) < 0
							|| (format.indexOf(" ") - format.toLowerCase()
									.lastIndexOf("i")) < 0) {
						if ("java".equalsIgnoreCase(flag)) {
							result += "mm";
						} else if ("javascript".equalsIgnoreCase(flag)) {
							result += "ii";
						} else if ("oracle".equalsIgnoreCase(flag)) {
							result += "MI";
						} else {
							result = "请输入正确标识！";
							return result;
						}
					}
				} else {
					if ("java".equalsIgnoreCase(flag)) {
						result += "mm";
					} else if ("javascript".equalsIgnoreCase(flag)) {
						result += "ii";
					} else if ("oracle".equalsIgnoreCase(flag)) {
						result += "MI";
					} else {
						result = "请输入正确标识！";
						return result;
					}
				}

			}
		}

		if (format.contains("s") || format.contains("S")) {
			if (format.indexOf(":") > 0) {
				result += ":";
			}
			if ("java".equalsIgnoreCase(flag)
					|| "javascript".equalsIgnoreCase(flag)) {
				result += "ss";
			} else if ("oracle".equalsIgnoreCase(flag)) {
				result += "SS";
			} else {
				result = "请输入正确标识！";
				return result;
			}
		}

		return result.trim();
	}

	public static void main(String[] args) throws ParseException {
//		DateFormatRecogniser dp = new DateFormatRecogniser(
//				"yyyY-Mm-Dd Hh:Mm:sS");
		DateFormatRecogniser dp= new DateFormatRecogniser("mm-dd");
		System.out.println(dp.getFormat("java"));
		System.out.println(dp.getFormat("javascript"));
		System.out.println(dp.getFormat("oracle"));
		
		dp= new DateFormatRecogniser("HH:mm");
		System.out.println(dp.getFormat("java"));
		System.out.println(dp.getFormat("javascript"));
		System.out.println(dp.getFormat("oracle"));
		SimpleDateFormat sdf=new SimpleDateFormat("y-M-d");
		System.out.println(sdf.format(sdf.parse("2008-08-08")));
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf2.format(sdf2.parse("2008-08-08")));

	}
}
