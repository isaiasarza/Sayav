package SAYAV2.SAYAV2.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date>{

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String marshal(Date v) throws Exception {
		synchronized (dateFormat) {
            return dateFormat.format(v);
        }
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		 synchronized (dateFormat) {
	            return dateFormat.parse(v);
	        }
	}

	@Override
	public String toString() {
		return "DateAdapter [dateFormat=" + dateFormat + "]";
	}

	
}
