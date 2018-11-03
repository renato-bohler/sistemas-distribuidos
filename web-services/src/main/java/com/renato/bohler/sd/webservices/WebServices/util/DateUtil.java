package com.renato.bohler.sd.webservices.WebServices.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DateUtil {

	public Date getDateFromString(String dateString) {
		if (dateString == null) {
			return null;
		}

		Date date = null;
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			date = df.parse(dateString);
		} catch (ParseException e) {
		}
		return date;
	}
}
